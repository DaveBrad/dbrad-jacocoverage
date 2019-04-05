/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Class for utility methods that may be used for File Package data that
 * is used in the package filter table displays and file package cover state.
 * 
 * @author dbradley
 */
public class FilePackageUtil {

    /**
     * Clone a JLabel with a border for presentation with a border that is to
     * represent the JLabel with focus. (Not a deep clone.)
     *
     * @param cloneFromLabel clone from this label
     *
     * @return a newer label
     */
    public static JLabel cloneLabel4HasFocusObj(JLabel cloneFromLabel) {
        // clone the basics into a new object
        JLabel nuLabel = new JLabel(cloneFromLabel.getText(),
                cloneFromLabel.getIcon(),
                cloneFromLabel.getHorizontalAlignment());

        // add the border to represent focus condition
        Border nuBorder = new LineBorder(Color.black, 2);
        nuLabel.setBorder(nuBorder);

        return nuLabel;
    }
}
