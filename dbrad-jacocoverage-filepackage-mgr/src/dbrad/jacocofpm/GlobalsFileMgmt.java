/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm;

/**
 * Global data.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class GlobalsFileMgmt {

    /**
     * User preference: use a custom JaCoCo jar instead of the bundled version.
     *
     * @since 1.5.2
     */
    public static final String PROP_USE_CUSTOM_JACOCO_JAR = "JaCoCoverage.JavaAgent.useCustomJacocoJar";

    /**
     * Default configuration value: use a custom JaCoCo jar instead of the
     * bundled version.
     *
     * @since 1.5.2
     */
    public static final boolean DEF_USE_CUSTOM_JACOCO_JAR = false;

    /**
     * User preference: path of custom JaCoCo jar to use instead of the bundled
     * version.
     *
     * @since 1.5.2
     */
    public static final String PROP_CUSTOM_JACOCO_JAR_PATH = "JaCoCoverage.JavaAgent.customJacocoJarPath";

    /**
     * Default configuration value: path of custom JaCoCo jar to use instead of
     * the bundled version.
     *
     * @since 1.5.2
     */
    public static final String DEF_CUSTOM_JACOCO_JAR_PATH = "/foo/bar/jacocoagent.jar";

    private GlobalsFileMgmt() {
    }
}
