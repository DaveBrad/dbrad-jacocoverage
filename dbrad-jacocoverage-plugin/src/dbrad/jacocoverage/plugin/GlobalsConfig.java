/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin;

import dbrad.jacocoverage.plugin.config.pf.PfTableModel;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;

/**
 * Global data.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class GlobalsConfig {

    /** NetBeans icon representing a Java package. */
    public static final ImageIcon ICON_NB_JAVA_PKG = new ImageIcon(PfTableModel.class.getResource(
            "/dbrad/jacocoverage/plugin/resources/icon/netbeans_java_package.png"));

    /** NetBeans icon representing a Java folder. */
    public static final ImageIcon ICON_NB_JAVA_FOLDER = new ImageIcon(PfTableModel.class.getResource(
            "/dbrad/jacocoverage/plugin/resources/icon/netbeans_java_folder.png"));

    /** Preview of the regular light theme. */
    public static final String THEME_ICO_REGULAR = "/dbrad/jacocoverage/plugin/resources/icon/theme_default.png";

    /** Preview of the "Norway Today" dark theme. */
    public static final String THEME_ICO_NORWAYTODAY = "/dbrad/jacocoverage/plugin/resources/icon/theme_norwaytoday.png";

    /**
     * Load an internal resource.
     *
     * @param internalResource the path of internal resource.
     *
     * @return the loaded resource.
     *
     * @throws IOException if an I/O error occurs.
     */
    public static byte[] toBytes(String internalResource)
            throws IOException {
        byte[] content = null;
        InputStream is = GlobalsConfig.class.getResourceAsStream(internalResource);
        try {
            content = IOUtils.toByteArray(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return content;
    }

    private GlobalsConfig() {
    }
}
