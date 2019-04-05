/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import static dbrad.jacocofpm.mgr.com.FilePackageUtil.cloneLabel4HasFocusObj;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * ENUM that represents the coverage (cover) state of a package in the filtering
 * package tables. As the state is changed it may affect child package states
 * and the states represents the relationship.
 * <p>
 * The state will be a JLabel is an icon to be displayed in filter tables.
 *
 *
 * @author dbradley
 */
public enum FilePackageCoverStateEnum {
    /**
     * this package-format-string is to be included in the coverage
     */
    COVER_YES,
    /**
     * this package-format-string is to be included in the coverage because its
     * parent is COVER_YES
     */
    COVER_BY_PARENT,
    /**
     * this package-format-string is to be not included in the coverage (it does
     * not mean exclude)
     */
    COVER_NO;

    /**
     * The JLabel that is filled with a icon that represents the ENUM state.
     */
    private static JLabel[] coverEnumJLabelArr = null;

    /**
     * The JLabel that is filled with a icon that represents the ENUM state when
     * the item has focus.
     */
    private static JLabel[] coverEnumJLabelHasFocusArr = null;

    /**
     * Get the JLabel that is to be displayed in the table. This is reused
     * multiple times for display in tables
     *
     * @param hasFocus boolean for the label if has focus requirements for its
     *                 display
     *
     * @return JLabel that maps to the state
     */
    public JLabel getLabelForState(boolean hasFocus) {
        // fill in the JLabels and set the icon that is appropriate
        if (coverEnumJLabelArr == null) {
            int len = FilePackageCoverStateEnum.values().length;

            // array for all the states
            coverEnumJLabelArr = new JLabel[len];
            coverEnumJLabelHasFocusArr = new JLabel[len];

            // build the array
            for (int i = 0; i < len; i++) {
                String ofEnum = FilePackageCoverStateEnum.values()[i].toString().toLowerCase();

                // the icon .png is acquired from the resources/icon folder
                // and is the ENUM name in lowercase
                String strOfEnum = String.format("resources/icon/%s.png",
                        ofEnum.toLowerCase()); // NOI18N

                coverEnumJLabelArr[i] = new JLabel("", SwingConstants.CENTER);
                coverEnumJLabelArr[i]
                        .setIcon(new ImageIcon(getClass().getResource(strOfEnum)));

                coverEnumJLabelHasFocusArr[i] = cloneLabel4HasFocusObj(coverEnumJLabelArr[i]);
            }
        }
        // return the JLabel for the ENUM state

        return hasFocus
                ? coverEnumJLabelHasFocusArr[this.ordinal()]
                : coverEnumJLabelArr[this.ordinal()];
    }
}
