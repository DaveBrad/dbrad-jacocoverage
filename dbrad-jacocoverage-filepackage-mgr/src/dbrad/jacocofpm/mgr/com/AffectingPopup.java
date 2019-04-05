/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import javax.swing.JPopupMenu;

/**
 * Create a popup menu for an affecting-other package-format cell in a
 * package-filter/excludes table item.
 *
 * @author dbradley
 */
public class AffectingPopup extends JPopupMenu {

    /**
     * Create the popup menu object.
     *
     * @param fileAffectingObj the file that is affecting other, so as to
     *                         provide the affected-by listing in the popup menu
     */
    public AffectingPopup(FilePackageOfSrc fileAffectingObj) {

        super.setName("popup.pftable.package");
        fileAffectingObj.getAffectingObjectsArr().forEach((affectedItem) -> {
            super.add(new AffectingMenuItem(affectedItem));
        });
    }
}
