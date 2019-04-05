/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action.reqproc;

import dbrad.jacocofpm.config.GlobalsOfConfig;
import dbrad.jacocoverage.analyzer.JaCoCoReportAnalyzer;
import dbrad.jacocoverage.analyzer.JaCoCoXmlReportParser;
import dbrad.jacocoverage.analyzer.JavaClass;
import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import dbrad.jacocofpm.mgr.com.FileSrcCodeDir;
import dbrad.jacocoverage.menuctxt.action.ActionJacocoOnAntTaskJ2SE;
import dbrad.jacocoverage.menuctxt.action.NbModuleReporter;
import dbrad.jacocoverage.plugin.anno.AbstractCoverageAnnotation;
import dbrad.jacocofpm.json.Json3PackageFormat;
import dbrad.jacocofpm.json.Json2ProjSrcCodeFolder;
import dbrad.jacocofpm.json.Json4Data;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import dbrad.jacocofpm.json.Json1ProjLevel;
import dbrad.jacocofpm.json.JsonMap;
import dbrad.jacocofpm.mgr.com.FileClassesDir;
import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import dbrad.jacocoverage.menuctxt.action.reqproc.htmext.ByProjectHtmlIndexFile;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.module.api.AntProjectCookie;
import org.apache.tools.ant.module.api.AntTargetExecutor;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.HtmlBrowser;
import org.openide.execution.ExecutorTask;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.xml.sax.SAXException;

/**
 * Class which performs the Jacocoagent request and results in reports being
 * generated based on IDE projects dbrad-jacocoverage settings.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class JaCoCoRequestProcess {

    /** The prefix for timestamped directories. */
    public static final String TIME_STAMP_STRING = "ts_";

    private static final Logger LOGGER = Logger.getLogger(ActionJacocoOnAntTaskJ2SE.class.getName());

    private final IdeProjectJacocoverageConfig ideNbProjConfig;

    private final boolean areFoldersSelected;

//    private final File reportIntoDir;
    private final File binReportFile;
    private final boolean isNbModule;
    private final boolean isJ2EE;

    private final boolean isMergeOn;

    private final boolean isProjectSpecific;

    private final boolean isHighlight;

    // - - - console report control and report output management
    /** the project reports are by-project (or grouped) */
    private final boolean isByProjectReports;

    /** retain XML files */
    private final boolean retainXmlFiles;

    /** enabled console setting */
    private final boolean isConsoleReport;

    /** console-report heading is only done once at the start of reporting */
    private boolean consoleReportHeadingStart;
    /** console-report heading is only done once at the start of reporting */
    private boolean consoleReportFooterEnd;

    // - - - HTML report settings
    private final boolean isHtmlReport;
    private final boolean openAutoHtmlReport;

    // - - - NbModule - - - 
    private NbModuleReporter nbModuleReporter;

    // - - -  NONE time stamp for reports 
    /** reports need to cleaned up when not time-stamped only once for reporting
     * of multiple project (sub-project) arrangements
     */
    private boolean removedOldReportsOnce;

    // - - -  time stamp for reports and when the jacoco starts
    /** the value of the timestamp start of processing
     */
    private final Date runRequestDateTimeStamp;

    /** if time-stamp enabled the File path to the directory for reports
     */
    private final File runRequestDateTimeStampFile;

    //- - - report names for the root and any sub-projects being processed
    /** the project name for the root project */
    private String projectNameOfRootProject;

    /** when processing a source-code-pair the project-name for display purposes
     * is required to be shared among reports.
     */
    private String srcPairProjectName;

    /** ArrayList for the excludes setting in a form that will allow excludes to
     * be applied.
     */
    private ArrayList<String> excludesList;

    /**
     * processing the report and generating HMTL etc
     */
    private Map<String, JavaClass> coverageData;

    /**
     * The source-code and classes that are paired for processing their coverage
     * information about. There are restriction as to what may be done; if the
     * source is no a part of the open-projects, no annotation/highlighting can
     * be done, but the HTML report for the classes can be done.
     */
    private ArrayList<FileSrcCodeDir> clzzSrcPairDirArr;

    /**
     * Create the instance to process the JaCoCo request binary report file.
     *
     * @param nbProject    the root Netbeans project
     * @param nbProjConfig the IIDE configuration for the project
     * @param binReportP   the binary report from the coverage execution
     * @param isNBModule   is the project a Netbeans module
     * @param isJ2EE       is the project of a J2EE type
     */
    public JaCoCoRequestProcess(Project nbProject, IdeProjectJacocoverageConfig nbProjConfig,
            File binReportP, boolean isNBModule, boolean isJ2EE) {

        // runtime information for processing a coverage
        this.binReportFile = binReportP;
        this.isNbModule = isNBModule;
        this.isJ2EE = isJ2EE;

        // this IDE's Netbeans configuration details with any reporting generation
        this.isMergeOn = nbProjConfig.isMergeOnSet();

        // - - -
        this.ideNbProjConfig = nbProjConfig;

        this.isProjectSpecific = nbProjConfig.isProjectSpecific();

        // - - - 
        this.isHighlight = nbProjConfig.isHighlightingSet();

        // - - -
        this.isByProjectReports = nbProjConfig.isByProjectReportsSet();

        this.retainXmlFiles = nbProjConfig.isRetainXmlFileSet();

        this.isConsoleReport = nbProjConfig.isConsoleReportSet();
        // console reports need controls for outputting heading and footer
        // once in the various multiple processing of associate-projects
        this.consoleReportHeadingStart = true;
        this.consoleReportFooterEnd = false;
        // - - - 
        this.isHtmlReport = nbProjConfig.isHtmlReportSet();

        // open automatically is allowed if the html generate is oenbale too
        if (this.isHtmlReport) {
            this.openAutoHtmlReport = nbProjConfig.isAutoOpenHtmlReportSet();
        } else {
            this.openAutoHtmlReport = false;
        }

        // removing reports needs to be done once, so when processing
        // multiple reports (due to associate-projects) need the
        // sema
        this.removedOldReportsOnce = false;

        // timestamp is used for console reports and/or timestamped settings
        // and only needs to be created once
        this.runRequestDateTimeStamp = Calendar.getInstance().getTime();
        this.runRequestDateTimeStampFile = getProjectReportIntoDir();

        this.excludesList = NbUtilsPlugin.getExcludesPatternsAsArrayList(nbProject);

        try {
            defineClassesAndSrcDirectories();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.areFoldersSelected = !this.clzzSrcPairDirArr.isEmpty();
    }

    /**
     * Are folder selected for processing into a report.
     *
     * @return true or false
     */
    public boolean areFoldersSelected() {
        return this.areFoldersSelected;
    }

    /**
     * Run the request to process a Jacoco request.
     *
     * @param executor  ANT executor
     * @param antCookie ANT cookie
     * @param antTask   ANT task
     *
     * @throws IOException any IO exception issues
     */
    public void runTheRequestProcess(AntTargetExecutor executor,
            AntProjectCookie antCookie,
            final String antTask)
            throws IOException {

        // Launch the Ant task with the JaCoCo JavaAgent.
        final ExecutorTask execute = executor.execute(antCookie, new String[]{antTask});

        new RequestProcessor("JaCoCoverage Collection Task", 3, true).post(new Runnable() {
            @Override
            public void run() {
                ProgressHandle progr = ProgressHandleFactory.createSystemHandle("JaCoCoverage Collection Task");

                try {
                    progr.setInitialDelay(400);
                    progr.start();
                    progr.switchToIndeterminate();

                    // Wait for the end of the Ant task execution. We do it in a new thread otherwise it would
                    // freeze the current one. This is a workaround for a known and old NetBeans bug: the ExecutorTask
                    // object provided by the NetBeans platform is not correctly wrapped.
                    int executeRes = execute.result();

                    if (binReportFile.exists()) {
                        processBinReport(projectNameOfRootProject);
                    } else {
                        AbstractCoverageAnnotation.removeAll(ideNbProjConfig.getNbProjectId());

                        NbUtilsPlugin.closeConsoleTab(GlobalsOfConfig.TXTREPORT_TABNAME);

                        String msg = "Ant Task or JaCoCo Agent failed, JaCoCoverage can't process data.\n"
                                + "(AntExitCode=" + executeRes + ", JacocoBinReportFound=" + binReportFile.exists() + ")";
                        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.WARNING_MESSAGE);
                        DialogDisplayer.getDefault().notify(nd);
                    }
                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);

                } catch (IOException | ParserConfigurationException | SAXException ex) {
                    Exceptions.printStackTrace(ex);

                } finally {
                    progr.finish();
                }
            }
        });
    }

    /**
     * Define the classes and source directories from root-project and any
     * associate-projects against the JSON configuration file.
     *
     * @throws IOException any IO exception issues
     */
    private void defineClassesAndSrcDirectories() throws IOException {
        /*
      for reports to happen on test the build.test.dir needs to be in the 
      classpath
         */
        // Load the generated JaCoCo coverage report. Special case for Java Web projects.
        String classDirFilePath;
        NbProjectJacoco nbProjectJacoco = new NbProjectJacoco(this.ideNbProjConfig.getNbProject(), null);

        this.projectNameOfRootProject = nbProjectJacoco.getNbProjectDisplayName();

        if (isJ2EE) {
            String prjDir = this.ideNbProjConfig.getNbProjectDirPath();
            FileObject openIdefileObj = this.ideNbProjConfig.getNbProject().getProjectDirectory();

            classDirFilePath = String.format("%s/build/web/WEB-INF/classes/", this.ideNbProjConfig.getNbProjectDirPath());

            FileClassesDir classesDirFile = new FileClassesDir(classDirFilePath);

            // now set the source file  
            String srcFolderLabel = this.ideNbProjConfig.getProperty("src.dir");
            //          String srcFolderLabel = Utils.getProperty(NBprojProperties, "test.src.dir");

            String srcDirFilePath = String.format("%s%s/", prjDir, srcFolderLabel);

            ArrayList<FileSrcCodeDir> pairArr = new ArrayList<>();
            pairArr.add(new FileSrcCodeDir(srcDirFilePath, openIdefileObj,
                    nbProjectJacoco, false, srcFolderLabel, classesDirFile));

            this.clzzSrcPairDirArr = pairArr;
        } else {
            // process the projects source-file directories and associate/set the
            // coverage-enum setting (COVER_YES, COVER_NO, COVER_BY_PARENT) from 
            // the JSON confiuration data for the package-filter.
            //
            this.clzzSrcPairDirArr = nbProjectJacoco.getSrcCodeDirPairList();

            if (!this.isProjectSpecific) {
                // is global setting

                // isNBModule so need to get all the project and subprojects
                // as a single arrangement 
                //
                // work lists
                ArrayList<FileSrcCodeDir> globalArr = new ArrayList<>(6);
                ArrayList<NbProjectJacoco> processNbProjectJacocoList = new ArrayList<>(6);

                // this NbProject module is the first one to process
                // so add the root
                processNbProjectJacocoList.add(nbProjectJacoco);

                if (this.isNbModule) {
                    // need to process through the depending projects so as
                    // to include alongside the original module
                    HashMap<String, NbProjectJacoco> nbProjectJacocoDependson
                            = nbProjectJacoco.getDependingOnNbProjectsHash();

                    for (String prjKey : nbProjectJacoco.getDependingOnNbProjectsKeyArr()) {
                        NbProjectJacoco nbProjJacWS = nbProjectJacocoDependson.get(prjKey);

                        // add any depends/sub-projects to the list, but avoid duplicates
                        if (processNbProjectJacocoList.contains(nbProjJacWS)) {
                            continue;
                        }
                        processNbProjectJacocoList.add(nbProjJacWS);
                    }
                }
                // go through the project list and process the source-code directories 
                // for inclusion in tests (not test-source-code directories)
                for (NbProjectJacoco nbProjJac : processNbProjectJacocoList) {
                    for (FileSrcCodeDir frsc : nbProjJac.getSrcCodeDirPairList()) {
                        // filter all test directories out of the test
                        if (frsc.isTestSrcCode()) {
                            continue;
                        }
                        globalArr.add(frsc);
                    }
                }
                // only the source folder(s) are considered for processing
                this.clzzSrcPairDirArr = globalArr;

                return;
            }
            // need to process the package-filter JSON configuration data onto 
            // source-files data.
            //
            // if the globals are overridden get the include packages information
            // for checking data gathering
            Json1ProjLevel json1ProjLevel = null;

            if (this.isProjectSpecific) {
                json1ProjLevel = ideNbProjConfig.jsonMapData.getProjectIncludePackages();
            }
            // process 
            //
            // key1: source-folder name (unique in the project)
            // key2: the package-format of a directory within a source-folder
            // ele: the JSON-data for package-filter(PfJsonData)
            //
            // only do the first source folder found
            //
            for (FileSrcCodeDir srcFilePair : this.clzzSrcPairDirArr) {

                srcFilePair.calculateSubPackges(srcFilePair);

                // a null for any part of the expected JSON keys
                // means  data is not in the JSON so defaults need to
                // be set
                Json3PackageFormat srcFileJsonDataHash = null;

                if (json1ProjLevel != null) {
                    String projName = srcFilePair.getNbProjectDisplayName();
                    Json2ProjSrcCodeFolder json2SrcCodeFolders = json1ProjLevel.get(projName);

                    if (json2SrcCodeFolders != null) {
                        srcFileJsonDataHash = json2SrcCodeFolders.get(srcFilePair.getSrcFolderDirDisplayName());
                    }
                }
                // begin cascade of the files to set explicit value
                // srcFileJsonDataHash == null will cause default settings to
                // be applied
                setFileJsonValuesCascade(srcFilePair, srcFileJsonDataHash);
            }
        }
        ArrayList<FileSrcCodeDir> jsonAppliedSourcePair
                = nbProjectJacoco.mergeAllSourcesForDisplay(this.excludesList);

        ArrayList<FileSrcCodeDir> adjustWithJsonAppliedList = new ArrayList<>();

        JsonMap jsonSettings = this.ideNbProjConfig.jsonMapData;

        // process each source-folder of the project (src and test) and create a 
        // report for each one
        //
        int lenPair = jsonAppliedSourcePair.size();

        for (int i = 0; i < lenPair; i++) {
            this.consoleReportFooterEnd = (i == lenPair - 1);

            FileSrcCodeDir filePair = jsonAppliedSourcePair.get(i);
            filePair.applyJsonMap(jsonSettings);

            if (filePair.isOnCodeFolderState()) {
                adjustWithJsonAppliedList.add(filePair);
            }
        }
        this.clzzSrcPairDirArr = adjustWithJsonAppliedList;
    }

    /**
     * Set the CoverageStateEnum explicitly for each File (Dir) within the
     * source-directory tree. Using recursive processing.
     *
     * @param srcFilePair         the File to process on
     * @param srcFileJsonDataHash the Hash structure which represents the
     *                            package-filter JSON data
     */
    private void setFileJsonValuesCascade(FileSrcCodeDir srcFilePair,
            HashMap<String, Json4Data> srcFileJsonDataHash) {
        //
        ArrayList<FilePackageOfSrc> filePkgOfSrcArr = srcFilePair.getSubPackageArr();

        for (FilePackageOfSrc filePkgOfSrc : filePkgOfSrcArr) {
            Json4Data pfJsonDataItem;
            if (srcFileJsonDataHash == null) {
                // something is new and not found in the JSON file, so the defaults will be set
                pfJsonDataItem = null;
            } else {
                // the package-format //99 new file not in the JSON but added causes an issue here
                pfJsonDataItem = srcFileJsonDataHash.get(filePkgOfSrc.getPackageFormat());
            }
            // explicitly set me (no formal setting due to the setting would
            // redo the cascade, and underneath there is the potential to have
            // a COVER_BY_PARENT 
            //
            // key: package-format of File
            // ele: JSON data that maps the File package-format
            if (pfJsonDataItem == null) {
                // this can happen when the files in a project have been changed
                // but the project-properties for dbrad-jacocoverage have not
                // so the JSON is out of whack. So the assumption is that the
                // setting are on or COVER_YES underneath.
                filePkgOfSrc.coverStateEnum = FilePackageCoverStateEnum.COVER_YES;
                filePkgOfSrc.setOnCodeFolderState(true);
            } else {
                // set as per the JSON file setting
                filePkgOfSrc.coverStateEnum = pfJsonDataItem.getPfCvr();
                filePkgOfSrc.setOnCodeFolderState(pfJsonDataItem.isOn());
            }
            filePkgOfSrc.setExcludePackages(this.excludesList);

            // recurse the children
            setFileJsonValuesCascade(filePkgOfSrc, srcFileJsonDataHash);
        }
    }

    /**
     * Process for console report generation/ or-not.
     *
     * @param projectNameReportingOn project-name to report against
     *                               (root-project or associate-project)
     *
     * @throws IOException                  some IO exception in an attempt
     *                                      create a report
     * @throws ParserConfigurationException parsing the binary file error
     * @throws SAXException                 XML issues likely
     *
     */
    private void processBinReport(String projectNameReportingOn)
            throws IOException, ParserConfigurationException, SAXException {

        // start a time so we know how long this took
        long st = System.currentTimeMillis();
        long et;

        defineClassesAndSrcDirectories();

        // the report type to produce is determined by the global or 
        // the project specific setting
        if (this.isByProjectReports) {
            reportByProject(projectNameReportingOn);
        } else {
            reportByGroupingBundled(projectNameReportingOn);
        }
        // indicate not running any testing at this time
        cleanupJaCoCoWorkfiles(this.binReportFile);

        et = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Coverage Collection Task took: {0} ms", et - st);
    }

    /**
     * Generate a report that is by grouped/bundled format.
     *
     * @param projectNameReportingOn project-name to report against
     *                               (root-project or associate-project)
     *
     * @throws IOException                  some IO exception in an attempt
     *                                      create a report
     * @throws ParserConfigurationException parsing the binary file error
     * @throws SAXException                 XML issues likely
     */
    private void reportByGroupingBundled(String projectNameReportingOn)
            throws ParserConfigurationException, SAXException, IOException {
        //
        File xmlReportFile = clzzSrcPairDirArr.get(0).getXmlReportFile(this.runRequestDateTimeStampFile);

        this.nbModuleReporter
                = new NbModuleReporter(this.ideNbProjConfig, this.binReportFile,
                        this.isMergeOn,
                        this.runRequestDateTimeStamp,
                        this.clzzSrcPairDirArr, this.excludesList,
                        this.isProjectSpecific);

        this.nbModuleReporter.processXmlReport(xmlReportFile);

        this.coverageData = JaCoCoXmlReportParser.getCoverageData(xmlReportFile);

        // Remove existing highlighting (from a previous coverage task), show reports and apply
        // highlighting on each Java source file.
        AbstractCoverageAnnotation.removeAll(this.ideNbProjConfig.getNbProjectId());

        this.srcPairProjectName = clzzSrcPairDirArr.get(0).nbProjectJacoco.getNbProjectDisplayName();

        processConsoleReport(projectNameReportingOn);
        processNbModHtmlReport(this.clzzSrcPairDirArr);
        processHighLighting(this.clzzSrcPairDirArr.get(0));
    }

    /**
     * Generate a report that is by project format.
     *
     * @param projectNameReportingOn project-name to report against
     *                               (root-project or associate-project)
     *
     * @throws IOException                  some IO exception in an attempt
     *                                      create a report
     * @throws ParserConfigurationException parsing the binary file error
     * @throws SAXException                 XML issues likely
     */
    private void reportByProject(String projectNameReportingOn) throws IOException, ParserConfigurationException, SAXException {
        // the JSON file settings need to be applied onto the 
        // file source-directories

        // process each source-folder of the project (src and test) and create a 
        // report for each one
        //
        int lenPair = this.clzzSrcPairDirArr.size();

        // Remove existing highlighting (from a previous coverage task), show reports and apply
        // highlighting on each Java source file.
        AbstractCoverageAnnotation.removeAll(this.ideNbProjConfig.getNbProjectId());

        ArrayList<String> reportURIList = new ArrayList<>();

        for (int i = 0; i < lenPair; i++) {
            this.consoleReportFooterEnd = (i == lenPair - 1);

            FileSrcCodeDir filePair = this.clzzSrcPairDirArr.get(i);

            // if the source-code-folder is not to be processed skip it
            if (!filePair.isOnCodeFolderState()) {
                continue;
            }
            // remove old reports if not timestamp-controlled
            if (!this.removedOldReportsOnce) {
                filePair.removeOldReports(this.runRequestDateTimeStampFile);
                this.removedOldReportsOnce = true;
            }
            // get the includes settings for the source file pair
            String packagesToTest
                    = NbUtilsPlugin.getProjectIncludesAsStr(ideNbProjConfig.getNbProject(), ":", ".*");

            String[] packagesToTestSplitArr = packagesToTest.split(":");

            // in String "." has specical meaning when used as parameters in 
            // String methods, this prevents any ambiguity 
            String period = ".";

            for (String splitStr : packagesToTestSplitArr) {
                // format is: 
                //    cc.*    cc.inline.*
                // need
                //    /cc     /cc/inline
                //
                // remove all ".*" from the string (if present)
                splitStr = splitStr.replaceAll(".*", "");

                // convert from '.' to '/' as the separator as '.' has 
                // a specical meaning for Java string split
                splitStr = splitStr.replaceAll(period, "/");
                splitStr = "/" + splitStr;
            }
            // each source-folder will have a different report xmlReport and
            // as such will process it differently
            JaCoCoReportAnalyzer.toXmlReport(filePair, this.binReportFile, this.isMergeOn,
                    this.runRequestDateTimeStampFile,
                    this.excludesList);

            this.coverageData = JaCoCoXmlReportParser
                    .getCoverageData(filePair.getXmlReportFile(this.runRequestDateTimeStampFile));

            this.srcPairProjectName = ideNbProjConfig.getNbProjectDisplayName();
            //
            processConsoleReport(filePair);
            //
            String report = processJ2HtmlReport(filePair);
            if (report != null) {
                reportURIList.add(report);
            }
            //
            processHighLighting(filePair);
        }

        ByProjectHtmlIndexFile byProjIndexFile
                = new ByProjectHtmlIndexFile(
                        this.projectNameOfRootProject,
                        createTimeStampStr4Reports(this.runRequestDateTimeStamp),
                        getProjectReportIntoDir(), reportURIList);

        File indexHtmlFile = byProjIndexFile.createTheFile();

        if (this.openAutoHtmlReport) {
            // does not work with Microsoft edge (MS problem)
            HtmlBrowser.URLDisplayer defaultBrowser = HtmlBrowser.URLDisplayer.getDefault();
            defaultBrowser.showURL(Utilities.toURI(indexHtmlFile).toURL());
        }
    }

    /**
     * Process for console report generation/ or-not for a source-code-pair.
     *
     * @param projectNameReportingOn project-name to report against
     *                               (root-project or associate-project)
     *
     * @throws IOException some IO exception in an attempt create a report
     */
    private void processConsoleReport(FileSrcCodeDir filePair) throws IOException {
        String projectNameConReport = filePair.getNbProjectDisplayName();
        String srcCodeFolderName = filePair.getSrcFolderDirDisplayName();

        // combine the project-name and source-code-folder into a single report name 
        processConsoleReport(projectNameConReport + " - " + srcCodeFolderName);
    }

    /**
     * Process for console report generation/ or-not.
     *
     * @param projectNameReportingOn project-name to report against
     *                               (root-project or associate-project)
     *
     * @throws IOException some IO exception in an attempt create a report
     */
    private void processConsoleReport(String projectNameReportingOn) throws IOException {

        if (this.isConsoleReport) {
            JaCoCoReportAnalyzer.toConsoleReport(this.coverageData,
                    this.consoleReportHeadingStart,
                    this.consoleReportFooterEnd,
                    createTimeStampStr4Reports(this.runRequestDateTimeStamp),
                    this.srcPairProjectName + GlobalsOfConfig.TXTREPORT_TABNAME,
                    projectNameReportingOn
            );
            this.consoleReportHeadingStart = false;
        }
    }

    /**
     * Process for HTML reports generation for NB-modules.
     *
     * @param fileSrcCodePair the source-code and class-dir pairs for a project
     *
     * @throws IOException some IO exception in an attempt create a report
     */
    private void processNbModHtmlReport(ArrayList<FileSrcCodeDir> fileSrcCodePairArr) throws IOException {

        if (this.isHtmlReport) {
            // GWI patch: If NetBeans Module Project - use different HTML Report

            // this.nbJaCoCoExtension was created when the XML report was created
            // and just need to reuse the same this.nbJaCoCoExtension
            String report
                    = this.nbModuleReporter.processGetHtmlReport(
                            fileSrcCodePairArr.get(0).getHtmlReportFile(this.runRequestDateTimeStampFile));

            if (this.openAutoHtmlReport) {
                HtmlBrowser.URLDisplayer.getDefault().showURL(Utilities.toURI(new File(report)).toURL());
            }
        }
    }

    /**
     * Process for HTML reports generation for J2 types of projects.
     *
     * @param fileSrcCodePair the source-code and class-dir for a project
     *
     * @throws IOException some IO exception in an attempt create a report
     */
    private String processJ2HtmlReport(FileSrcCodeDir fileSrcCodePair) throws IOException {

        String report = null;

        if (this.isHtmlReport) {
            if (!fileSrcCodePair.isOnCodeFolderState()) {
                return report;
            }
            // this will work but has NO filters for the reports
            // report =  NbModuleReporter.toHTMLReport(binReportFile,
            // fileSrcCodePair.getHtmlReportFile(this.runRequestDateTimeStampFile),
            // ideNbProjConfig.getNbProject());
            report = JaCoCoReportAnalyzer.toHtmlReport(fileSrcCodePair, this.binReportFile,
                    this.isMergeOn,
                    this.runRequestDateTimeStampFile, this.excludesList);
        }

        return report;
    }

    /**
     * Process for high-lighting generation/ or-not.
     *
     * @param fileSrcCodePair the source-code and class-dir for a project
     */
    private void processHighLighting(FileSrcCodeDir fileSrcCodePair) {

        if (!fileSrcCodePair.isMyProject || !this.isHighlight) {
            // 1) project is an additional-project where access to the
            //    source code to annotate/highlight is not possible as the
            //    files are unavailable to the java-editor in my-project
            // 2) highlight is not enabled
            return;
        }
        // GWI patch: GWI-Modified: New Coloring Code
        if (this.isNbModule) {
            for (final JavaClass jclass : this.coverageData.values()) {
                try {
                    NbUtilsPlugin.colorDoc(this.ideNbProjConfig,
                            jclass,
                            this.ideNbProjConfig.isHighlightingExtendedSet(), fileSrcCodePair);
                } catch (Throwable e) {
                    Logger.getGlobal().log(Level.SEVERE,
                            "Failed to color: {0} {1}",
                            new Object[]{jclass.getClassName(), fileSrcCodePair});
                }
            }
        } else {
            for (final JavaClass jclass : this.coverageData.values()) {
                NbUtilsPlugin.colorDoc(this.ideNbProjConfig,
                        jclass,
                        this.ideNbProjConfig.isHighlightingExtendedSet(),
                        fileSrcCodePair);
            }
        }
    }

    /**
     * Cleanup the Jacoco files used to process for various reports.
     *
     * @param binreport JaCoCo binary report file
     *
     * @throws IOException if workfiles can't be removed or moved.
     */
    private void cleanupJaCoCoWorkfiles(File binreport)
            throws IOException {
        //
        binreport.delete();
        // remove all the xml reports
        String reportDir;
        //
        if (this.ideNbProjConfig.isReportDefaultDir()) {
            reportDir = this.ideNbProjConfig.getReportDefaultDirPath();
        } else {
            reportDir = this.ideNbProjConfig.getReportUserDefinedDirPath();
        }
        //
        if (this.ideNbProjConfig.isReportsTimestampForm()) {
            reportDir = this.runRequestDateTimeStampFile.getAbsolutePath();
        }
        // get the xml files and remove them all (if not set to retain XML files)
        if (!this.retainXmlFiles) {
            File parentForXml = new File(reportDir);
            //
            String[] listArr = parentForXml.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });

            if (listArr != null) {
                for (String s : listArr) {
                    File xmlFile = new File(parentForXml, s);
                    xmlFile.delete();
                }
            }
        }
        cleanReportDirectories();
    }

    /**
     * Cleanup the report directories to the time-stamp retain settings and
     * time-stamp form if set.
     */
    private void cleanReportDirectories() {
        // a limit of zero implies no management needed
        if (this.ideNbProjConfig.getReportRetainValueN() == 0
                || !this.ideNbProjConfig.isReportsTimestampForm()) {
            return;
        }
        // need to perform a tidy on the directory for the timestamp
        // directories.
        FilenameFilter filterForTsUnderscore = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(TIME_STAMP_STRING);
            }
        };
        // get the base directory for the time-stamp to be processed
        String baseDir4ReportsPath;
        if (this.ideNbProjConfig.isReportDefaultDir()) {
            baseDir4ReportsPath = this.ideNbProjConfig.getReportDefaultDirPath();
        } else {
            baseDir4ReportsPath = this.ideNbProjConfig.getReportUserDefinedDirPath();
        }

        File[] dirFileList = new File(baseDir4ReportsPath).listFiles(filterForTsUnderscore);

        // only process if the directory contains more than the limit
        int limitOfTimeStampRetentionValue = this.ideNbProjConfig.getReportRetainValueN();

        if (dirFileList.length < limitOfTimeStampRetentionValue) {
            return;
        }
        // otherwise need to remove the oldest number of items
        int removeCount = dirFileList.length - limitOfTimeStampRetentionValue;

        // oldest files will be on top of the list due to their naming
        Arrays.sort(dirFileList);

        for (int i = 0; i < removeCount; i++) {
            File deleteItemFileDir = dirFileList[i];
            try {
                FileUtils.deleteDirectory(deleteItemFileDir);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * Get the directory for reports to be placed into with any time-stamp and
     * default/user-defined paths.
     *
     * @return <code>File</code> object of the directory to place reports into
     */
    private File getProjectReportIntoDir() {
        // determine the output directory (default or user-defined, using time-stamp or not)
        File reportDirBase;
        if (this.ideNbProjConfig.isReportDefaultDir()) {
            reportDirBase = new File(this.ideNbProjConfig.getReportDefaultDirPath());
        } else {
            reportDirBase = new File(this.ideNbProjConfig.getReportUserDefinedDirPath());
        }
        // if not timestamped then return 
        boolean timestampBool = this.ideNbProjConfig.isReportsTimestampForm();

        if (!timestampBool) {
            if (!reportDirBase.exists()) {
                reportDirBase.mkdirs();
            }
            return reportDirBase;
        }
        // is timestamp so need to do the setup
        //
        // determine the timestamp value to use (default directory or user defined directory)
        // the clean of the directory needs to be done at a later time based
        // on the timestamp format.
        //
        // the directory is to be created and named in a specific manner
        // 'ts_YYYYMMDD_HHMMSS_reportDir
        //
        String reportDirNam = String.format("%s%s_reportDir",
                TIME_STAMP_STRING,
                createTimeStampStr4Filename(this.runRequestDateTimeStamp));

        reportDirBase = new File(reportDirBase, reportDirNam);

        if (reportDirBase.exists()) {
            // add the milli_second to the list for processing
            reportDirNam = String.format("%s%s_reportDir",
                    TIME_STAMP_STRING,
                    createTimeStampStr4Filename(this.runRequestDateTimeStamp));

            reportDirBase = new File(reportDirBase.getParent(), reportDirNam);
        }
        // create the directories
        if (!reportDirBase.exists()) {
            reportDirBase.mkdirs();
        }
        // make the directory timestamp reusableby defined machine state as
        // running reports
        return reportDirBase;
    }

    /**
     * Create a time-stamp string as file name content.
     *
     * @param runRequestDateTimeStamp Date instance of the time-stamp
     *
     * @return string in yyyyMMdd_HHmmss_SSS format
     */
    public static String createTimeStampStr4Filename(Date runRequestDateTimeStamp) {
        return String.format("%s",
                new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")
                        .format(runRequestDateTimeStamp));
    }

    /**
     * Create a time-stamp string for display in a report.
     *
     * @param runRequestDateTimeStamp Date instance of the time-stamp
     *
     * @return string in yyyy-MM-dd HH:mm:ss format
     */
    public static String createTimeStampStr4Reports(Date runRequestDateTimeStamp) {
        return String.format("%s",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(runRequestDateTimeStamp));
    }
}
