/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import javax.swing.JPopupMenu;

/**
 * Create a popup menu for an affected-by package-format cell in a
 * package-filter/excludes table item.
 *
 * @author dbradley
 */
public class AffectedByPopup extends JPopupMenu {

    /**
     * Create the popup menu with the menu-item for the file that is affecting
     * the cell .
     *
     * @param fileAffectingObj file-package that relates to a package-format of
     *                         a cell
     */
    public AffectedByPopup(FilePackageOfSrc fileAffectingObj) {

        super.setName("popup.pftable.package");  
        super.add(new AffectedByMenuItem(fileAffectingObj));
    }
}
