/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm.util;

import java.net.MalformedURLException;
import java.net.URL;
import org.netbeans.api.project.Project;
import org.openide.awt.HtmlBrowser;
import org.openide.util.Exceptions;

/**
 * Some NetBeans related utilities.
 *
 * @author Graeme Ingleby (pre-2017)
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class NBUtils {

    /**
     * launch the default browser to display an URL.
     *
     * @param url the URL to display.
     */
    public static void extBrowser(String url) {
        try {
            HtmlBrowser.URLDisplayer.getDefault().showURL(new URL(url));
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Indicate if a project is about a certain type.
     * <br>See
     * <a href="http://wiki.netbeans.org/DevFaqActionAllAvailableProjectTypes">DevFaqActionAllAvailableProjectTypes</a>
     * for help.
     *
     * @param project the project.
     * @param prjtype the targeted project type.
     *
     * @return true if supported, otherwise false.
     */
    public static boolean isProjectSupported(Project project, NBProjectTypeEnum... prjtype) {
        boolean supported = false;
        if (null != project) {
            String projectClass = project.getClass().getName();

            for (NBProjectTypeEnum type : prjtype) {
                String typeQname = type.qname();
                
                if (type.isStrict() ? projectClass.equals(typeQname) : projectClass.startsWith(typeQname)) {
                    supported = true;
                    break;
                }
            }
        }
        return supported;
    }

    private NBUtils() {
    }
}
