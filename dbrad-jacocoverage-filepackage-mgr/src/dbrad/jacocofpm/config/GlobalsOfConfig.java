/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm.config;

import java.util.Arrays;
import java.util.List;

/**
 * Global data.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class GlobalsOfConfig {

    /**
     * Color themes are associated to XML configuration files: here are their
     * filename prefix. See the {@code fr.tikione.jacocoverage.plugin.resources}
     * package for XML configuration files.
     */
    public static final List<String> THEME_PREFIX = Arrays.asList("", "norwaytoday__");

    /** The name of the NetBeans console tab where JaCoCo reports are displayed. */
    public static final String TXTREPORT_TABNAME = " (jacocoverage report)";

    /** Project preference: override globals. */
    public static final String PROP_PRJ_OVERRIDE_GLOBALS = "JaCoCoverage.Prj.OverrideGlobals";

    /** User preference: customization of the JavaAgent passed to the Ant task. */
    public static final String PROP_TEST_ANT_TASK_JAVAAGENT = "JaCoCoverage.JavaAgent.AntTaskJavaagent";

    /** User preference: enable extended code highlighting. */
    public static final String PROP_ENABLE_HIGHLIGHTEXTENDED = "JaCoCoverage.Editor.EnableCodeHighlightingExtended";

    /** User preference: enable code highlighting. */
    public static final String PROP_ENABLE_HIGHLIGHT = "JaCoCoverage.Editor.EnableCodeHighlighting";

    /** User preference: JaCoCoverage themePrefix. */
    public static final String PROP_THEME = "JaCoCoverage.Editor.Theme";

    /** User preference: by-project-report reports. */
    public static final String PROP_BY_PROJECT_REPORT = "JaCoCoverage.ByReport.Project";

    /** User preference: is merge on (that do not delete. */
    public static final String PROP_MERGE_ON = "JaCoCoverage.MergeOn";
    
    /** User preference: retain the XML file (that do not delete. */
    public static final String PROP_RETAIN_XML_FILE = "JaCoCoverage.Retain.xml";

    /** User preference: show a minimal textual JaCoCo report in a NetBeans
     * console tab. */
    public static final String PROP_ENABLE_CONSOLE_REPORT = "JaCoCoverage.NbConsole.EnableReport";

    /**
     * User preference: generate a complete HTML JaCoCo report.
     */
    public static final String PROP_ENABLE_HTML_REPORT = "JaCoCoverage.Html.EnableReport";

    /** User preference: automatically open generated complete HTML JaCoCo
     * report. */
    public static final String PROP_AUTOOPEN_HTML_REPORT = "JaCoCoverage.Html.AutoOpenReport";

    /** User preference: exclude org.testng. */
    public static final String PROP_EXCLUDE_TESTNG = "JaCoCoverage.Excl.Testng";

    /** User preference: exclude org.junit. */
    public static final String PROP_EXCLUDE_JUNIT = "JaCoCoverage.Excl.Junit";

    /** User preference: exclude junit. */
    public static final String PROP_EXCLUDE_JUNIT_EXTENSION = "JaCoCoverage.Excl.JunitExtension";

    /** User preference: user defined directory is default. (Boolean) Last
     * setting is retained
     */
    public static final String PROP_REPORTS_DIR_DEFAULT = "JaCoCoverage.report.default.dir";
    /**
     * User preference: user defined directory path. (String) directory-string.
     * <p>
     * Last setting is retained.
     */
    public static final String PROP_REPORTS_DIR_PATH = "JaCoCoverage.report.user.defined.dir";

    /** User preference: retention reports. (Boolean) */
    public static final String PROP_REPORTS_TIMESTAMP_STATE = "JaCoCoverage.report.timestamp";

    /** User preference: retention reports. (Integer-string) */
    public static final String PROP_REPORTS_RETENTION_VALUE = "JaCoCoverage.report.retain";

    /** Default project's configuration value: override globals. */
    public static final boolean DEF_PRJ_OVERRIDE_GLOBALS = false;

    /** Default configuration value: enable code highlighting. */
    public static final boolean DEF_ENABLE_HIGHLIGHT = true;

    /** Default configuration value: enable extended code highlighting. */
    public static final boolean DEF_ENABLE_HIGHLIGHTEXTENDED = true;

    /** Default configuration value: JaCoCoverage themePrefix. */
    public static final int DEF_THEME = 0;

    /** Default configuration value: what to do with JaCoCo workfiles. */
    public static final int DEF_JACOCOWORKFILES_RULE = 0; // 0: keep, 1: keep zipped, 2: delete

    /** Default configuration value: by project report is the choice default */
    public static final boolean DEF_RADIO_BY_PROJECT_REPORT = true;

    /** Default configuration value: merge on.
     */
    public static final boolean DEF_MERGE_ON = true;
    
    /** Default configuration value: retain the XML file.
     */
    public static final boolean DEF_RETAIN_XML_FILE = false;

    /** Default configuration value: show a minimal textual JaCoCo report in a
     * NetBeans console tab. */
    public static final boolean DEF_ENABLE_CONSOLE_REPORT = true;

    /** Default configuration value: generate a complete HTML JaCoCo report. */
    public static final boolean DEF_ENABLE_HTML_REPORT = true;

    /** Default configuration value: automatically open generated complete HTML */
    public static final boolean DEF_AUTOOPEN_HTML_REPORT = false;

    private GlobalsOfConfig() {
    }
}
