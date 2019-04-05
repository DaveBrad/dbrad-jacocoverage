/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JTable;

/**
 * Class for displaying the sibling item of an affecting-other
 * package-format row in a package-filtering table.
 *
 * @author dbradley
 */
public class AffectingMenuItem extends JMenuItem implements ActionListener {

    //99 change the var-name
    private final FilePackageOfSrc fileAffectingObj;

    @SuppressWarnings("LeakingThisInConstructor")
    AffectingMenuItem(FilePackageOfSrc fileAffectingObj) {
        this.fileAffectingObj = fileAffectingObj;

        FilePackageOfSrc siblingSrcFile = fileAffectingObj;

        String packageFormatString = siblingSrcFile.getPackageFormat();

        if (packageFormatString.isEmpty()) {
            packageFormatString = "*";
        }
        String affectingByProjName = siblingSrcFile.nbProjectJacoco.getProjectDisplayName();
        String affectingBySrcFolderName = siblingSrcFile.getSrcFolderDirDisplayName();

        String menuItemText = String.format("'%s' %s : %s",
                packageFormatString,
                affectingByProjName,
                affectingBySrcFolderName);

        super.setText(menuItemText);

        super.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable assocTable = fileAffectingObj.nbProjectJacoco.getPfJTable4NbProjectJacoco();

        fileAffectingObj.flashInDisplayCount = FileSrcCodeDir.FLASHER_NUMBER;
        e.setSource(fileAffectingObj);
        ((ActionListener) assocTable).actionPerformed(e);
    }
}
