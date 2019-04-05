/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm.config;

import dbrad.jacocofpm.GlobalsFileMgmt;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * Plugin configuration handler.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class Config {

    /**
     * JaCoCoverage plugin preference singleton.
     */
    private static final Preferences pref = NbPreferences.forModule(Config.class);

    /**
     * Ensures that future reads from this preference node and its descendants
     * reflect any changes that were committed to the persistent store (from any
     * VM) prior to the {@code sync} invocation. As a side-effect, forces any
     * changes in the contents of this preference node and its descendants to
     * the persistent store, as if the flush method had been invoked on this
     * node.
     */
    public static void sync() {
        try {
            pref.sync();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Forces any changes in the contents of this preference node and its
     * descendants to the persistent store. Once this method returns
     * successfully, it is safe to assume that all changes made in the subtree
     * rooted at this node prior to the method invocation have become permanent.
     */
    public static void flush() {
        try {
            pref.flush();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     * Get configuration value: retain the XML file.
     *
     * @return configuration value.
     */
    public static boolean isCfgMergeOn() {
        return false;
    }
    
    /**
     * Get configuration value: retain the XML file.
     *
     * @return configuration value.
     */
    public static boolean isCfgRetainXmlFile() {
        return pref.getBoolean(GlobalsOfConfig.PROP_RETAIN_XML_FILE,
                GlobalsOfConfig.DEF_RETAIN_XML_FILE);
    }

    /**
     * Get configuration value: show a minimal textual JaCoCo report in a
     * NetBeans console tab.
     *
     * @return configuration value.
     */
    public static boolean isCfgConsoleReport() {
        return pref.getBoolean(GlobalsOfConfig.PROP_ENABLE_CONSOLE_REPORT, GlobalsOfConfig.DEF_ENABLE_CONSOLE_REPORT);
    }

    /**
     * Get configuration value: enable code highlighting.
     *
     * @return configuration value.
     */
    public static boolean isCfgHighlighting() {
        return pref.getBoolean(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHT, GlobalsOfConfig.DEF_ENABLE_HIGHLIGHT);
    }

    /**
     * Get configuration value: enable extended code highlighting.
     *
     * @return configuration value.
     */
    public static boolean isCfgHighlightingExtended() {
        return pref.getBoolean(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHTEXTENDED, GlobalsOfConfig.DEF_ENABLE_HIGHLIGHTEXTENDED);
    }

    /**
     * Get configuration value: is by-project-report.
     *
     * @return configuration value.
     */
    public static boolean isCfgByProjectReport() {
        boolean res = pref.getBoolean(GlobalsOfConfig.PROP_BY_PROJECT_REPORT,
                GlobalsOfConfig.DEF_RADIO_BY_PROJECT_REPORT);

        return res;
    }

    /**
     * Get configuration value: generate a complete HTML JaCoCo report.
     *
     * @return configuration value.
     */
    public static boolean isCfgHtmlReport() {
        return pref.getBoolean(GlobalsOfConfig.PROP_ENABLE_HTML_REPORT, GlobalsOfConfig.DEF_ENABLE_HTML_REPORT);
    }

    /**
     * Get configuration value: automatically open generated complete HTML
     * JaCoCo report.
     *
     * @return configuration value.
     */
    public static boolean isCfgAutoOpenHtmlReport() {
        return pref.getBoolean(GlobalsOfConfig.PROP_AUTOOPEN_HTML_REPORT, GlobalsOfConfig.DEF_AUTOOPEN_HTML_REPORT);
    }

    /**
     * Get configuration value: use a custom JaCoCo jar instead of the bundled
     * version.
     *
     * @return configuration value.
     */
    public static boolean isCfgUseCustomJacocoJar() {
        return pref.getBoolean(GlobalsFileMgmt.PROP_USE_CUSTOM_JACOCO_JAR, GlobalsFileMgmt.DEF_USE_CUSTOM_JACOCO_JAR);
    }

    /**
     * Get configuration value: path of custom JaCoCo jar to use instead of the
     * bundled version.
     *
     * @return configuration value.
     */
    public static String getCfgCustomJacocoJarPath() {
        return pref.get(GlobalsFileMgmt.PROP_CUSTOM_JACOCO_JAR_PATH, GlobalsFileMgmt.DEF_CUSTOM_JACOCO_JAR_PATH);
    }

    /**
     * Get configuration value: JaCoCoverage themePrefix.
     *
     * @return configuration value.
     */
    public static int getCfgTheme() {
        return pref.getInt(GlobalsOfConfig.PROP_THEME, GlobalsOfConfig.DEF_THEME);
    }

    /**
     * Set configuration value: path of custom JaCoCo jar to use instead of the
     * bundled version.
     *
     * @param path configuration value.
     */
    public static void setCfgCustomJacocoJarPath(String path) {
        pref.put(GlobalsFileMgmt.PROP_CUSTOM_JACOCO_JAR_PATH, path);
    }

    /**
     * Set configuration value: merge on.
     *
     * @param enbl configuration value.
     */
    public static void setCfgMergeOn(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_MERGE_ON, enbl);
    }

    /**
     * Set configuration value: enable code highlighting.
     *
     * @param enbl configuration value.
     */
    public static void setCfgHighlighting(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHT, enbl);
    }

    /**
     * Set configuration value: enable extended code highlighting.
     *
     * @param enbl configuration value.
     */
    public static void setCfgHighlightingExtended(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_ENABLE_HIGHLIGHTEXTENDED, enbl);
    }

    /**
     * Set configuration value: the reports will be by-project.
     *
     * @param enbl configuration value.
     */
    public static void setCfgByProjectReports(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_BY_PROJECT_REPORT, enbl);
    }

    /**
     * Set configuration value: retain the XML file.
     *
     * @param enbl configuration value.
     */
    public static void setCfgRetainXmlFile(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_RETAIN_XML_FILE, enbl);
    }

    /**
     * Set configuration value: show a minimal textual JaCoCo report in a
     * NetBeans console tab.
     *
     * @param enbl configuration value.
     */
    public static void setCfgConsoleReport(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_ENABLE_CONSOLE_REPORT, enbl);
    }

    /**
     * Set configuration value: generate a complete HTML JaCoCo report.
     *
     * @param enbl configuration value.
     */
    public static void setCfgtmlReport(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_ENABLE_HTML_REPORT, enbl);
    }

    /**
     * Set configuration value: automatically open generated complete HTML
     * JaCoCo report.
     *
     * @param enbl configuration value.
     */
    public static void setCfgOpenHtmlReport(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_AUTOOPEN_HTML_REPORT, enbl);
    }

    /**
     * Set configuration value: exclude org.testng.*
     *
     * @param enbl configuration value.
     */
    public static void setCfgExcludeOrgTestng(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_EXCLUDE_TESTNG, enbl);
    }

    /**
     * Set configuration value: exclude org.junit.*
     *
     * @param enbl configuration value.
     */
    public static void setCfgExcludeOrgJunit(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_EXCLUDE_JUNIT, enbl);
    }

    /**
     * Set configuration value: exclude junit.*
     *
     * @param enbl configuration value.
     */
    public static void setCfgExcludeJunitExtension(boolean enbl) {
        pref.putBoolean(GlobalsOfConfig.PROP_EXCLUDE_JUNIT_EXTENSION, enbl);
    }

    /**
     * Set configuration value: JaCoCoverage themePrefix.
     *
     * @param theme configuration value.
     */
    public static void setCfgTheme(int theme) {
        pref.putInt(GlobalsOfConfig.PROP_THEME, theme);
    }

    /**
     * Set configuration value: use a custom JaCoCo jar instead of the bundled
     * version.
     *
     * @param enbl configuration value.
     */
    public static void setCfgUseCustomJacocoJar(boolean enbl) {
        pref.putBoolean(GlobalsFileMgmt.PROP_USE_CUSTOM_JACOCO_JAR, enbl);
    }

    private Config() {
    }
}
