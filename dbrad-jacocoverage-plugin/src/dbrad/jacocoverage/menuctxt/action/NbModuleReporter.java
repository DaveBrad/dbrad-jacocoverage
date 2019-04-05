/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Graeme Ingleby 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocoverage.analyzer.JacocoNBModuleReportGenerator;
import dbrad.jacocofpm.mgr.com.FileSrcCodeDir;
import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import static dbrad.jacocoverage.analyzer.JacocoNBModuleReportGenerator.NbModReportType.HTML;
import static dbrad.jacocoverage.analyzer.JacocoNBModuleReportGenerator.NbModReportType.XML;
import dbrad.jacocoverage.menuctxt.action.reqproc.JaCoCoRequestProcess;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.netbeans.api.project.Project;
import org.openide.util.Exceptions;

/**
 * Class for reporting on NbModule kind of project.
 * <p>
 * <p>
 * History: Original code by Graeme Ingleby has been restructured from a static
 * functional access approach to class instance processing. Projects are
 * processed differently via source-class pairs prior to processing.
 * <p>
 * The code has been rewritten to process the projects differently.
 *
 * @author Graeme Ingleby (pre-2017) as NbModuleReporter
 * @author dbradley (2017) Major changes to support more extensive reporting.
 *
 */
public class NbModuleReporter {

    private final File jacocoExecFile;
    private final boolean isMergeOn;

    private final Project nbProject;
    private final String rootProjectName;
    private final String timeStampStr;

    private final ArrayList<FileSrcCodeDir> clzzSrcsPairArr;
    private final ArrayList<String> excludesList;

    private final boolean isGlobalOverridden;

    /**
     * Create a NB-Module reporter instance for create coverage report for a
     * NBModule.
     *
     * @param ideNbProjConfig         IDE configuration of the project
     * @param jacocoExecFile          the execution binary file
     * @param runRequestDateTimeStamp the time-stamp for the report
     * @param clzzSrcsPairArr         the source/test-source code pairs
     * @param excludesList            string array of excludes from report
     * @param isGlobalOverridden      is project-specific (global overridden)
     */
    public NbModuleReporter(IdeProjectJacocoverageConfig ideNbProjConfig,
            File jacocoExecFile, boolean isMergeOnP,
            Date runRequestDateTimeStamp,
            ArrayList<FileSrcCodeDir> clzzSrcsPairArr,
            ArrayList<String> excludesList,
            boolean isGlobalOverridden) {

        // about this IDE project config settings
        this.nbProject = ideNbProjConfig.getNbProject();
        this.rootProjectName = ideNbProjConfig.getNbProjectDisplayName();

        // the jacoco report settings and info
        this.jacocoExecFile = jacocoExecFile;
        this.isMergeOn = isMergeOnP;

        this.timeStampStr = JaCoCoRequestProcess.createTimeStampStr4Reports(runRequestDateTimeStamp);

        // the classes that need to be processed by reporter
        this.clzzSrcsPairArr = clzzSrcsPairArr;
        this.excludesList = excludesList;

        this.isGlobalOverridden = isGlobalOverridden;
    }

    /**
     * Process the HML report and return the 'index.html' file for browser
     * launch.
     *
     * @param reportDir file of the report directory to place reports in
     *
     * @return string of path to 'index.html' file to launch
     */
    public String processGetHtmlReport(File reportDir) {
        try {
            JacocoNBModuleReportGenerator reportGeneratorHtml = new JacocoNBModuleReportGenerator(
                    this.jacocoExecFile, this.isMergeOn,
                    this.rootProjectName,
                    this.timeStampStr, reportDir, HTML,
                    this.isGlobalOverridden);

            processAllProjectHtmlReport(this.nbProject, reportGeneratorHtml);

            reportGeneratorHtml.end();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new File(reportDir, "index.html").getAbsolutePath();
    }

    /**
     * Process the jacoco-exec file into an XMLfilefor processing by sub-tending
     * processes.
     *
     * @param xmlReportDir the directory location to store the XML report file
     *                     too
     */
    public void processXmlReport(File xmlReportDir) {
        try {
            JacocoNBModuleReportGenerator reportGeneratorXml = new JacocoNBModuleReportGenerator(
                    this.jacocoExecFile, this.isMergeOn,
                    this.rootProjectName,
                    this.timeStampStr, xmlReportDir, XML,
                    this.isGlobalOverridden);

            processAllProjectXmlReport(this.nbProject, reportGeneratorXml);
            reportGeneratorXml.end();

        } catch (IOException ex) {
            // should not happen,but if does then major problem (user's security)
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Process the jacoco-exec file into an HTML file for report purposes
     *
     * @param nbProject           project that the report is for
     * @param reportGeneratorHtml report generator object
     */
    private void processAllProjectHtmlReport(Project nbProject,
            JacocoNBModuleReportGenerator reportGeneratorHtml) throws IOException {

        List<String> moduleClassDirectories = new ArrayList<>(8);
        List<String> moduleSourceDirectories = new ArrayList<>(8);

        for (FileSrcCodeDir fileSrcCodeDir : this.clzzSrcsPairArr) {

            if (this.isGlobalOverridden) {
                if (!fileSrcCodeDir.isOnCodeFolderState()) {
                    continue;
                }
            }
            moduleClassDirectories.add(fileSrcCodeDir.getClassesAssociatedFile().getAbsolutePath());
            moduleSourceDirectories.add(fileSrcCodeDir.getAbsolutePath());

            // at run-time/build modules have classes generated which will now
            // be included now. NOTE: the .class files will be in the 
            // build/classes directory which is also where the principle projects
            // .class files will be placed.
            //
            addGeneratedClasses(fileSrcCodeDir.nbProjectJacoco, moduleSourceDirectories);
        }
        // produce the report
        reportGeneratorHtml.processNBModule(nbProject.getProjectDirectory().getName(),
                this.clzzSrcsPairArr,
                moduleClassDirectories, moduleSourceDirectories,
                this.excludesList);
    }

    private void processAllProjectXmlReport(Project nbProject,
            JacocoNBModuleReportGenerator reportGeneratorXml) throws IOException {

        List<String> moduleClassDirectories = new ArrayList<>(8);
        List<String> moduleSourceDirectories = new ArrayList<>(8);

        for (FileSrcCodeDir fileSrcCodeDir : this.clzzSrcsPairArr) {
            moduleClassDirectories.add(fileSrcCodeDir.getClassesAssociatedFile().getAbsolutePath());
            moduleSourceDirectories.add(fileSrcCodeDir.getAbsolutePath());

            // at run-time/build modules have classes generated which will now
            // be included now. NOTE: the .class files will be in the 
            // build/classes directory which is also where the principle projects
            // .class files will be placed.
            //
            addGeneratedClasses(fileSrcCodeDir.nbProjectJacoco, moduleSourceDirectories);
        }
        reportGeneratorXml.processNBModule(nbProject.getProjectDirectory().getName(),
                this.clzzSrcsPairArr,
                moduleClassDirectories, moduleSourceDirectories,
                this.excludesList);
    }

    private void addGeneratedClasses(NbProjectJacoco nbProjectJacocoL,
            List<String> moduleSourceDirectories) {
        // at run-time/build modules have classes generated which will now
        // be included now. NOTE: the .class files will be in the 
        // build/classes directory which is also where the principle projects
        // .class files will be placed.
        //
        Project nbProj;
        if (nbProjectJacocoL.isPrincipleProject()) {
            nbProj = nbProjectJacocoL.getPrincipleNbProject();
        } else {
            nbProj = nbProjectJacocoL.getAssociateNbProject();
        }
        String prjDir = nbProj.getProjectDirectory().getPath();
        File genDir = new File(prjDir + "/build/classes-generated");

        if (genDir.exists()) {
            moduleSourceDirectories.add(genDir.getPath());
        }
    }
}
