/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocoverage.menuctxt.action.reqproc.JaCoCoRequestProcess;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.util.NBProjectTypeEnum;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EAR;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EJB;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_WEB;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import dbrad.jacocofpm.util.NBUtils;
import dbrad.jacocofpm.util.UtilsFileMgmt;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import javax.swing.AbstractAction;
import org.apache.tools.ant.module.api.AntProjectCookie;
import org.apache.tools.ant.module.api.AntTargetExecutor;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

/**
 * A toolkit that launches Ant tasks with the JaCoCo JavaAgent, colorizes Java
 * source files and shows a coverage report.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqRequestProcessor">DevFaqRequestProcessor</a>
 * for NetBeans threading tweaks.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqActionContextSensitive">DevFaqActionContextSensitive</a>
 * for context action tweaks.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqAddGlobalContext">DevFaqAddGlobalContext</a>
 * for global context and project tweaks.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author Graeme Ingleby (pre-2017)
 * @author dbradley (2017)
 */
@SuppressWarnings("CloneableImplementsClone")
public abstract class ActionJacocoOnAntTaskJ2SE
        extends AbstractAction
        implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Will be used in a future release
    //private static final String DEFAULT_EXCLUDES = "com.sun.*:org.apache.*:org.netbeans.*:junit.*:sun.*:org.openide.*:org.junit.*";
    /**
     * The Ant task to launch 'test' or 'test-single'.
     */
    private final String antTask;

    /**
     * The path to single test file.
     */
    private String singleTestFilepath;

    /**
     * The single test file.
     */
    private FileObject singleTestFileObject;

    /**
     * Additional properties passed to the Ant task.
     */
    private final Properties addAntTargetProps = new Properties();

    /**
     * Enable the context action on supported projects only.
     *
     * @param antTask additional properties passed to the Ant task.
     */
    public ActionJacocoOnAntTaskJ2SE(String antTask) {
        this.antTask = antTask;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        new RequestProcessor("JaCoCoverage Preparation Task", 3, true).post(new Runnable() {
            @Override
            public void run() {
                try {
                    runJacocoJavaagent(IdeProjectJacocoverageConfig.getSelectedNbProject());

                } catch (IllegalArgumentException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    /**
     * Run an Ant task with a JaCoCo Java Agent, collect and display coverage
     * data.
     *
     * @param project the project to launch Ant target from.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void runJacocoJavaagent(Project projectP)
            throws IOException {

        if (projectP == null) {
            // this is likely a file-test w/jacocoverage call so need to find the
            // project of the file
            Collection<? extends FileObject> selectedFileObjectCollection
                    = Utilities.actionsGlobalContext().lookupAll(FileObject.class);

            // should only have one file selected at this time
            FileObject selectedFile = (FileObject) selectedFileObjectCollection.toArray()[0];
            String selectedFileFullPath = selectedFile.getPath();

            // get the open projects to find what project maps to the
            // selected FileObject
            projectP = NbUtilsPlugin.getNbProjectForNbFileObject(selectedFile);

            if (projectP == null) {
                // this should not happen, but its possible due to poor-to-bad coding.
                String msg = "Unable to determine project for selected file to perform run/test on.";
                NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);

                return;
            }

            String projectsFullPath = projectP.getProjectDirectory().getPath();

            this.singleTestFilepath = selectedFileFullPath.substring(projectsFullPath.length() + 1);
            this.singleTestFilepath = NbUtilsPlugin.convertPathToFwdSlash(this.singleTestFilepath);

            this.singleTestFileObject = selectedFile;

            IdeProjectJacocoverageConfig.createForGivenProject(projectP);
        }
        final Project nbProjectOfFileOrProjectSelected = projectP;

        final IdeProjectJacocoverageConfig ideProjectConfig
                = IdeProjectJacocoverageConfig.reuseForProject(nbProjectOfFileOrProjectSelected);

        final boolean enableHighlight = ideProjectConfig.isHighlightingSet();
        final boolean enableConsoleReport = ideProjectConfig.isConsoleReportSet();
        final boolean enableHtmlReport = ideProjectConfig.isHtmlReportSet();

        if (enableHighlight || enableConsoleReport || enableHtmlReport) {

            final File binreport = UtilsFileMgmt.getJacocoBinReportFile(ideProjectConfig, this.singleTestFileObject);

            if (binreport.exists() && !binreport.delete()) {
                String msg = String.format("Cannot delete the previous JaCoCo report files, please delete them manually:\n%s\n",
                        binreport.getAbsolutePath());

                NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);

                // cannot precede to run the process to execute the code coverage
                return; // 
            }
            // Apply JaCoCo JavaAgent customization.
            final String antTaskJavaagentParam;

            // GWI patch: If NetBeans Module Project - use different JavaAgent settings
            final boolean isNBModule
                    = NBUtils.isProjectSupported(nbProjectOfFileOrProjectSelected, NBProjectTypeEnum.NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE);

            String jacocoAgentJarAbsPath = NbUtilsPlugin.getJacocoAgentJar().getAbsolutePath();

            // - - - - - includes packages - - - - - - - - 
            //
            // if the globals are not overridden get the includes path appropriately
            // otherwise do all classes
            String packagesToTest = "*";

            if (ideProjectConfig.isProjectSpecific()) {

                packagesToTest = NbUtilsPlugin.getProjectIncludesAsStr(nbProjectOfFileOrProjectSelected, ":", ".*");

                if (!packagesToTest.equals("*")) {
                    // get the additional includes and add it to the includes string
                    String additionalIncl = "";  // NOT SUPPORTTED ideProjectConfig.jsonMapData.getAdditionalPackagesIncludes();

                    if (!packagesToTest.isEmpty()) {
                        if (!additionalIncl.isEmpty()) {
                            packagesToTest += ":";
                        }
                    }
                    packagesToTest = String.format("%s%s", packagesToTest, additionalIncl);
                }
            }
            //999   the limit below would be bad
            if (packagesToTest.length() > 1000) { // GitHub#26: JaCoCo seems to fail if the includes list is too long
                packagesToTest = "*";
            }
            // - - - - - -  excludes packages - - - - - - - - - - - - -     
            String excludesAsString = NbUtilsPlugin.getExcludesForJacocoagentArgument(nbProjectOfFileOrProjectSelected);

            // - - - - - - jacocoagent parameters - - - - - - - - - - - - - 
            antTaskJavaagentParam = "\"" + jacocoAgentJarAbsPath
                    + "\"=includes=" + packagesToTest
                    + ",destfile=\"" + binreport.getAbsolutePath()
                    + "\""
                    + (excludesAsString == null ? "" : ",excludes=" + excludesAsString);

            FileObject scriptToExecute = nbProjectOfFileOrProjectSelected.getProjectDirectory().getFileObject("build", "xml");

            if (scriptToExecute == null) { // Fix for GitHub #16.
                scriptToExecute = nbProjectOfFileOrProjectSelected.getProjectDirectory().getFileObject("nbbuild", "xml");
            }
            DataObject dataObj = DataObject.find(scriptToExecute);
            AntProjectCookie antCookie = dataObj.getLookup().lookup(AntProjectCookie.class);

            AntTargetExecutor.Env env = new AntTargetExecutor.Env();
            AntTargetExecutor executor = AntTargetExecutor.createTargetExecutor(env);

            // Add the customized JaCoCo JavaAgent to the JVM arguments given to the Ant task. The JaCoCo JavaAgent is
            // appended to the existing list of JVM arguments that is given to the Ant task.
            Properties targetProps = env.getProperties();
            targetProps.putAll(addAntTargetProps);

            // Specify jvm args. Special case for Java Web projects.
            final boolean isJ2EE
                    = NBUtils.isProjectSupported(nbProjectOfFileOrProjectSelected,
                            NBProjectTypeEnum.J2EE, J2EE_EAR, J2EE_EJB, J2EE_WEB);

            // GWI patch: If NetBeans Module Project - use different JavaAgent settings
            //
            // dbradley: code modified to form reuse-able code structure
            String javaAgentSetting;

            if (isNBModule) {
                javaAgentSetting = "test.run.args";
            } else if (isJ2EE) {
                javaAgentSetting = "runmain.jvmargs";
            } else {
                javaAgentSetting = "run.jvmargs";
            }
            // the settings in the properties file will be applied to this
            // run of the jacocoagent
            String prjJvmArgs = ideProjectConfig.getProperty(javaAgentSetting);
            prjJvmArgs = (prjJvmArgs == null ? "" : prjJvmArgs);

            if (this.antTask.equals("test-single")) {
                String unitOrQaFunctional = this.singleTestFilepath.split("/")[1];

                if (unitOrQaFunctional.equals("unit") || unitOrQaFunctional.equals("qa-functional")) {
                    targetProps.put("test.type", unitOrQaFunctional); // "unit");
                }
                targetProps.put("continue.after.failing.tests", "true");

                // relative to the test directory
                // need to fix this problem
                String testSingleFileRelative = this.singleTestFilepath.substring(5);
                targetProps.put("javac.includes", testSingleFileRelative);

                if (isNBModule) {
                    // the structure is different from other arrangements
                    // but if path is unit/src/tstbase/NbM2Test.java then
                    // the test.includes needs to be 'tstbase/NbM2Test.Java'
                    // assume this is the case for qa-functional too
                    String leadingStrToRemove = String.format("%s/src/", unitOrQaFunctional);

                    String testInclStr = testSingleFileRelative.replace(leadingStrToRemove, "");
                    targetProps.put("test.includes", testInclStr);
                } else {
                    targetProps.put("test.includes", testSingleFileRelative);
                }
            }
            targetProps.put(javaAgentSetting,
                    String.format("%s -javaagent:%s", prjJvmArgs, antTaskJavaagentParam));

            // place into the environment the run jvm-args for the 
            // request process to pick up
            env.setProperties(targetProps);

            JaCoCoRequestProcess jacocoReqProc
                    = new JaCoCoRequestProcess(nbProjectOfFileOrProjectSelected,
                            ideProjectConfig,
                            binreport, isNBModule, isJ2EE);

            if (jacocoReqProc.areFoldersSelected()) {
                jacocoReqProc.runTheRequestProcess(executor, antCookie, antTask);
            } else {
                String msg = "All ON settings are 'off' for source and test folder(s),\n"
                        + "basically asking for w/jacocoverage with nothing to process against.\n";
                DialogDisplayer.getDefault()
                        .notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE));
            }

        } else {
            String msg = "Please enable at least one JaCoCoverage feature first (highlighting or reporting).";
            NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }

    /**
     * Get the ANT target properties object.
     *
     * @return Properties of the ANT target settings
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Properties getAddAntTargetProps() {
        return addAntTargetProps;
    }
}
