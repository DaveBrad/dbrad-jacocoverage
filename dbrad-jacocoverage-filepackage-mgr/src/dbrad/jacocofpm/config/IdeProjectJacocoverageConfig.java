/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm.config;

import dbrad.jacocofpm.json.JsonMap;
import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import java.io.File;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 * IDE dbrad-jacocoverage configuration manager which provides a project's
 * unique configuration file
 * <p>
 * <h1>History</h1>
 * <p>
 * 2017: dbradley major rework and restructure for dbrad-jacocoverage
 * <p>
 * 2016: Jonathan Lermitage parts remain/modified from tikione-jacocoverage
 * under WTFPL license)
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class IdeProjectJacocoverageConfig extends IdeConfigOfProject {

    /**
     * The Netbeans project data holder for Jacoco processing.
     */
    public NbProjectJacoco nbProjectJacoco;

    /**
     * The JSON map for data holder. This contains the file and its data for a
     * project in the IDE.
     */
    public JsonMap jsonMapData;

    /**
     * The project folder that is the default report, JSON and other storage for
     * dbrad-jacocoverage.
     */
    public static final String JACOCOVERAGE_FOLDER_NAME = ".jacocodbrad";

    /**
     * The folder that is temporary folder for execution binary files for
     * dbrad-jacocoverage.
     */
    public static final String JACOCOVERAGE_FOLDER_NAME_TMP = JACOCOVERAGE_FOLDER_NAME + "/tmp";

    /**
     * The dbrad-jacocoverage default root directory (relative address format).
     */
    public static final String JACOCOVERAGE_DATA_ROOTDIR = "./" + JACOCOVERAGE_FOLDER_NAME + "/";

    /**
     * The file path (external format) to the dbrad-jacocoverage default root
     * directory.
     */
    public static final String JACOCOVERAGE_DATA_DIR = JACOCOVERAGE_FOLDER_NAME + File.separator;

    /**
     * The JSON file path and file name for the JSON data storage.
     */
    public static final String JACOCO_JSON_FILE_PATH = JACOCOVERAGE_DATA_DIR + "project.json";

    /**
     * Create/reuse an IdeProjectJacocoverageConfig object for the Netbeans'
     * currently selected project.
     *
     * @return IdeProjectJacocoverageConfig object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeProjectJacocoverageConfig createForSelectedProject()
            throws IOException {
        //
        return reuseForProject(getSelectedNbProject());
    }

    /**
     * Create/reuse an IdeProjectJacocoverageConfig object for a given project.
     *
     * @param projectGiven Netbeans project to use to create IDE project
     *                     configuration object
     *
     * @return IdeProjectJacocoverageConfig object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeProjectJacocoverageConfig createForGivenProject(Project projectGiven)
            throws IOException {
        //
        return reuseForProject(projectGiven);
    }

    /**
     * Create/reuse an IdeProjectJacocoverageConfig object for the provided
     * Netbeans project.
     *
     * @param nbProject the Netbeans project to create from
     *
     * @return an IdeProjectJacocoverageConfig object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeProjectJacocoverageConfig reuseForProject(Project nbProject)
            throws IOException {

        File projectsConfigFileLocation = new File(nbProject.getProjectDirectory().getPath());

        Object pcObj = getExistingForIdeConfigOfProjSubClass(projectsConfigFileLocation, IdeProjectJacocoverageConfig.class);
        IdeProjectJacocoverageConfig pc = (IdeProjectJacocoverageConfig) pcObj;

        // new or reuse
        if (pc == null) {
            // create a new project's configuration manager
            pc = new IdeProjectJacocoverageConfig(projectsConfigFileLocation);
        }
        // need to reload the current settings of the JSON file on every reuse
        if (pc.jsonMapData == null) {
            pc.jsonMapData = new JsonMap(pc);
        }
        pc.jsonMapData.load();
        return pc;
    }

    /**
     * Create/reuse an IdeProjectJacocoverageConfig object for the provided
     * Netbeans project for associate project.
     * <p>
     * An associate project is a library related sub-project/depends-on-project
     * that relates to a principle-project.
     *
     * @param nbProject the Netbeans project to create from
     *
     * @return an IdeProjectJacocoverageConfig object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeProjectJacocoverageConfig reuseForAssociateProject(Project nbProject)
            throws IOException {

        File projectsConfigFileLocation = new File(nbProject.getProjectDirectory().getPath());

        Object pcObj = getExistingForIdeConfigOfProjSubClass(projectsConfigFileLocation, IdeProjectJacocoverageConfig.class);
        IdeProjectJacocoverageConfig pc = (IdeProjectJacocoverageConfig) pcObj;

        // new or reuse
        if (pc == null) {
            // create a new project's configuration manager
            pc = new IdeProjectJacocoverageConfig(projectsConfigFileLocation);
        }
        return pc;
    }

    /**
     * Create/reuse an IdeProjectJacocoverageConfig object for the or the
     * Netbeans' currently selected project.
     *
     * @return an IdeProjectJacocoverageConfig object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeProjectJacocoverageConfig reuseForSelectedProject()
            throws IOException {
        return reuseForProject(getSelectedNbProject());
    }

    /**
     * Create an IdeProjectJacocoverageConfig instance for a project at the
     * location path provided.
     * <p>
     * This contains all the data associated with the IDE project for
     * JaCoCoverage processing. Generally this is accessed through static
     * methods <code>reuseForProject</code>,
     * <code>reuseForAssociateProject</code>,
     * <code>createForGivenProject</code>.
     *
     * @param projectsConfigFileLocation File of the project location path
     *
     * @throws IOException if cannot load configuration.
     */
    protected IdeProjectJacocoverageConfig(File projectsConfigFileLocation) throws IOException {
        super(projectsConfigFileLocation);
    }

    /**
     * Get project's configuration value: Indicate if project overrides plugin's
     * globals options.
     *
     * @return configuration value.
     */
    public boolean isProjectSpecific() {
        return Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                .getProperty(GlobalsOfConfig.PROP_PRJ_OVERRIDE_GLOBALS,
                        Boolean.toString(GlobalsOfConfig.DEF_PRJ_OVERRIDE_GLOBALS)));
    }

    /**
     * Set project's configuration value: Indicate if project overrides plugin's
     * globals options.
     *
     * @param enbl configuration value.
     */
    public void setProjectSpecific(boolean enbl) {
        jsonMapData.getJsonPreferences().setProperty(GlobalsOfConfig.PROP_PRJ_OVERRIDE_GLOBALS,
                Boolean.toString(enbl));
    }

    /**
     * Get configuration value: is merge on
     *
     * @return configuration value.
     */
    public boolean isMergeOnSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_MERGE_ON,
                            Boolean.toString(GlobalsOfConfig.DEF_MERGE_ON)));
        } else {
            res = Config.isCfgMergeOn();
        }
        return res;
    }
    
    public static boolean isJsonFilePresent(Project nbProject){
        FileObject projDir = nbProject.getProjectDirectory();
        
        File jsonFile = new File(projDir.getPath() + "/" +JACOCO_JSON_FILE_PATH);
        
        return jsonFile.exists();
    }

    /**
     * Get configuration value: keep the XML file for external post processing
     *
     * @return configuration value.
     */
    public boolean isRetainXmlFileSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_RETAIN_XML_FILE,
                            Boolean.toString(GlobalsOfConfig.DEF_RETAIN_XML_FILE)));
        } else {
            res = Config.isCfgRetainXmlFile();
        }
        return res;
    }

    /**
     * Get configuration value: show a minimal textual JaCoCo report in a
     * NetBeans console tab.
     *
     * @return configuration value.
     */
    public boolean isConsoleReportSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_ENABLE_CONSOLE_REPORT,
                            Boolean.toString(GlobalsOfConfig.DEF_ENABLE_CONSOLE_REPORT)));
        } else {
            res = Config.isCfgConsoleReport();
        }
        return res;
    }

    /**
     * Get configuration value: enable code highlighting.
     *
     * @return configuration value.
     */
    public boolean isHighlightingSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHT,
                            Boolean.toString(GlobalsOfConfig.DEF_ENABLE_HIGHLIGHT)));
        } else {
            res = Config.isCfgHighlighting();
        }
        return res;
    }

    /**
     * Get configuration value: enable extended code highlighting.
     *
     * @return configuration value.
     */
    public boolean isHighlightingExtendedSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHTEXTENDED,
                            Boolean.toString(GlobalsOfConfig.DEF_ENABLE_HIGHLIGHTEXTENDED)));
        } else {
            res = Config.isCfgHighlightingExtended();
        }
        return res;
    }

    /**
     * Get configuration value: generate reports as project style.
     *
     * @return configuration value.
     */
    public boolean isByProjectReportsSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_BY_PROJECT_REPORT,
                            Boolean.toString(GlobalsOfConfig.DEF_RADIO_BY_PROJECT_REPORT)));
        } else {
            res = Config.isCfgByProjectReport();
        }
        return res;
    }

    /**
     * Get configuration value: generate a complete HTML JaCoCo report.
     *
     * @return configuration value.
     */
    public boolean isHtmlReportSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_ENABLE_HTML_REPORT,
                            Boolean.toString(GlobalsOfConfig.DEF_ENABLE_HTML_REPORT)));
        } else {
            res = Config.isCfgHtmlReport();
        }
        return res;
    }

    /**
     * Get configuration value: automatically open generated complete HTML
     * JaCoCo report.
     *
     * @return configuration value.
     */
    public boolean isAutoOpenHtmlReportSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_AUTOOPEN_HTML_REPORT,
                            Boolean.toString(GlobalsOfConfig.DEF_AUTOOPEN_HTML_REPORT)));
        } else {
            res = Config.isCfgAutoOpenHtmlReport();
        }
        return res;
    }

    /**
     * Is default directory for report location set.
     *
     * @return true if set fro default directory
     */
    public boolean isReportDefaultDir() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_REPORTS_DIR_DEFAULT));
        } else {
            // user defined reports directory not supported in globals mode
            // thus default dir is the case
            res = true;
        }
        return res;
    }

    /**
     * Get the default defined report directory path.
     *
     * @return string of an absolute-path
     */
    public String getReportDefaultDirPath() {

        if (isReportsTimestampForm()) {
            return getProjectDbradJacocoDirPath();
        }
        String reportsDir = String.format("%s/reportDir/", getProjectDbradJacocoDirPath());
        return reportsDir;
    }

    /**
     * Get the user defined report directory path. This is retained if the
     * default is changed at any time.
     *
     * @return string of an absolute-path
     */
    public String getReportUserDefinedDirPath() {
        return jsonMapData.getJsonPreferences().getProperty(GlobalsOfConfig.PROP_REPORTS_DIR_PATH);
    }

    /**
     * Is the retention of reports set.
     *
     * @return true if time-stamped format for reports
     */
    public boolean isReportsTimestampForm() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_REPORTS_TIMESTAMP_STATE));
        } else {
            // user defined reports directory not supported in globals mode
            res = false;
        }
        return res;
    }

    /**
     * Get the report retain value as set for the project specific settings.
     *
     * @return integer value of N setting (or default)
     */
    public int getReportRetainValueN() {
        String valueNString
                = jsonMapData.getJsonPreferences().getProperty(GlobalsOfConfig.PROP_REPORTS_RETENTION_VALUE);

        int valueN;
        try {
            // if exceptions then likely an issue with JSON value contents
            valueN = Integer.parseInt(valueNString);

        } catch (NumberFormatException e) {
            // default to 3 so as to prevent storage being used
            // if set to 0
            valueN = 3;
        }
        return valueN;
    }

    /**
     * Get configuration value: JaCoCoverage themePrefix.
     *
     * @return configuration value.
     */
    public int getTheme() {
        int res;
        if (isProjectSpecific()) {
            res = Integer.parseInt(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_THEME,
                            Integer.toString(GlobalsOfConfig.DEF_THEME)));
        } else {
            res = Config.getCfgTheme();
        }
        return res;
    }

    /**
     * Is excluded org.testng.* set.
     *
     * @return configuration value.
     */
    //99
    public boolean isExcludedOrgTestngSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_EXCLUDE_TESTNG,
                            Boolean.toString(GlobalsOfConfig.DEF_AUTOOPEN_HTML_REPORT)));
        } else {
            res = true;
        }
        return res;
    }

    /**
     * Is excluded org.junit.* set.
     *
     * @return configuration value.
     */
    //99
    public boolean isExcludedOrgJunitSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_EXCLUDE_JUNIT, Boolean.toString(true)));
        } else {
            res = true;
        }
        return res;
    }

    /**
     * Is excluded junit.* set.
     *
     * @return configuration value.
     */
    //99
    public boolean isExcludedJunitExtensionSet() {
        boolean res;
        if (isProjectSpecific()) {
            res = Boolean.parseBoolean(jsonMapData.getJsonPreferences()
                    .getProperty(GlobalsOfConfig.PROP_EXCLUDE_JUNIT_EXTENSION,
                            Boolean.toString(true)));
        } else {
            res = true;
        }
        return res;
    }

    /**
     * Set configuration value: set the by project reports
     *
     * @param enbl configuration value.
     */
    public void setByProjectReports(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_BY_PROJECT_REPORT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgByProjectReports(enbl);
        }
    }

    /**
     * Set configuration value: retain the XML file for external processing.
     *
     * @param enbl configuration value.
     */
    public void setRetainXmlFile(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_RETAIN_XML_FILE,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgRetainXmlFile(enbl);
        }
    }

    /**
     * Set configuration value: show a minimal textual JaCoCo report in a
     * NetBeans console tab.
     *
     * @param enbl configuration value.
     */
    public void setConsoleReport(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_ENABLE_CONSOLE_REPORT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgConsoleReport(enbl);
        }
    }

    /**
     * Set configuration value: merge on.
     *
     * @param enbl configuration value.
     */
    public void setMergeOn(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_MERGE_ON,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgHighlighting(enbl);
        }
    }

    /**
     * Set configuration value: enable code highlighting.
     *
     * @param enbl configuration value.
     */
    public void setHighlighting(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgHighlighting(enbl);
        }
    }

    /**
     * Set configuration value: enable extended code highlighting.
     *
     * @param enbl configuration value.
     */
    public void setHighlightingExtended(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHTEXTENDED,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgHighlightingExtended(enbl);
        }
    }

    /**
     * Set configuration value: generate a complete HTML JaCoCo report.
     *
     * @param enbl configuration value.
     */
    public void setHtmlReport(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_ENABLE_HTML_REPORT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgtmlReport(enbl);
        }
    }

    /**
     * Set configuration value: automatically open generated complete HTML
     * JaCoCo report.
     *
     * @param enbl configuration value.
     */
    public void setOpenHtmlReport(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_AUTOOPEN_HTML_REPORT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgOpenHtmlReport(enbl);
        }
    }

    /**
     * Set configuration value: exclude org.testng.*
     *
     * @param enbl configuration value.
     */
    public void setExcludeOrgTestng(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_EXCLUDE_TESTNG,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgExcludeOrgTestng(enbl);
        }
    }

    /**
     * Set configuration value: exclude org.junit.*
     *
     * @param enbl configuration value.
     */
    public void setExcludeOrgJunit(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_EXCLUDE_JUNIT,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgExcludeOrgJunit(enbl);
        }
    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param enbl configuration value.
     */
    public void setExcludeJunitExtension(boolean enbl) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_EXCLUDE_JUNIT_EXTENSION,
                            Boolean.toString(enbl));
        } else {
            Config.setCfgExcludeJunitExtension(enbl);
        }
    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param enbl configuration value.
     */
    public void setReportDefaultDirState(boolean enbl) {

        if (!isProjectSpecific()) {
            enbl = true;
        }
        jsonMapData.getJsonPreferences()
                .setProperty(GlobalsOfConfig.PROP_REPORTS_DIR_DEFAULT,
                        Boolean.toString(enbl));

    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param text configuration directory for defined reports.
     */
    public void setReportUserDefinedDirState(String text) {
        String textA = text.trim();

        if (textA.isEmpty()) {
            text = JACOCOVERAGE_DATA_ROOTDIR;
        }
        jsonMapData.getJsonPreferences()
                .setProperty(GlobalsOfConfig.PROP_REPORTS_DIR_PATH, text);
    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param enbl configuration value.
     */
    public void setReportTimestampState(boolean enbl) {
        if (!isProjectSpecific()) {
            enbl = false;
        }
        jsonMapData.getJsonPreferences()
                .setProperty(GlobalsOfConfig.PROP_REPORTS_TIMESTAMP_STATE,
                        Boolean.toString(enbl));
    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param value configuration value.
     */
    public void setReportRetainSpinnerInt(int value) {
        jsonMapData.getJsonPreferences()
                .setProperty(GlobalsOfConfig.PROP_REPORTS_RETENTION_VALUE, String.format("%d", value));
    }

    /**
     * Set configuration value: JaCoCoverage themePrefix.
     *
     * @param theme configuration value.
     */
    public void setTheme(int theme) {
        if (isProjectSpecific()) {
            jsonMapData.getJsonPreferences()
                    .setProperty(GlobalsOfConfig.PROP_THEME, Integer.toString(theme));
        } else {
            Config.setCfgTheme(theme);
        }
    }

    /**
     * Get the full path of the directory for processing the jacocoexec file.
     *
     * @return the directory containing the project.
     */
    private String getProjectDbradJacocoDirPath() {
        String dirpath = String.format("%s/%s", this.getNbProjectDirPath(), JACOCOVERAGE_FOLDER_NAME);
        return dirpath;
    }

    /**
     * Get the full path of the directory for processing the jacocoexec file.
     *
     * @return the directory containing the project.
     */
    final public File getProjectJsonFile() {
        String dirpath = String.format("%s/%s", this.getNbProjectDirPath(), JACOCO_JSON_FILE_PATH);

        return new File(dirpath);
    }

    /**
     * Get the full path of the directory for processing the jacocoexec file.
     *
     * @return the directory containing the project.
     */
    final public String getProjectDbradJacocoTmpDirPath() {
        String dirpath = String.format("%s/%s", this.getNbProjectDirPath(), JACOCOVERAGE_FOLDER_NAME_TMP);
        return dirpath;
    }
    
    /**
     * 
     */
    public static void clearMergeData(Project nbProj){
        String projDirStr = nbProj.getProjectDirectory().getPath();

        File projDirStrDbradFile
                = new File(projDirStr + "/"
                        + IdeProjectJacocoverageConfig.JACOCOVERAGE_FOLDER_NAME_TMP);

        // do the delete
        if (projDirStrDbradFile.exists()) {
            File mergeOnFile = new File(projDirStrDbradFile, "merge");
            if (mergeOnFile.exists()) {
                mergeOnFile.delete();
            }
        }
    }
}
