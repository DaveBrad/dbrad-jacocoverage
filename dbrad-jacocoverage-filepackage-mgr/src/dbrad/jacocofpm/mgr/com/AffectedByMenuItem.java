/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JTable;

/**
 * Class for displaying the sibling item of an affected-by-sibling
 * package-format row in a package-filtering table.
 *
 * @author dbradley
 */
public class AffectedByMenuItem extends JMenuItem implements ActionListener {

    private final FilePackageOfSrc fileAffectingObj;

    @SuppressWarnings("LeakingThisInConstructor")
    AffectedByMenuItem(FilePackageOfSrc fileAffectingObj) {
        this.fileAffectingObj = fileAffectingObj;

        FilePackageOfSrc siblingSrcFile = fileAffectingObj.affectedBySiblingSource;

        String packageFormatString = siblingSrcFile.getPackageFormat();
        if(packageFormatString.isEmpty()){
            packageFormatString = "*";
        }
        String affectingByProjName = siblingSrcFile.nbProjectJacoco.getProjectDisplayName();
        String affectingBySrcFolderName = siblingSrcFile.getSrcFolderDirDisplayName();

        String menuItemText = String.format("'%s' %s : %s", packageFormatString,
                affectingByProjName, affectingBySrcFolderName);

        super.setText(menuItemText);

        super.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable assocTable = fileAffectingObj.affectedBySiblingSource.nbProjectJacoco.getPfJTable4NbProjectJacoco();

        fileAffectingObj.affectedBySiblingSource.flashInDisplayCount = FileSrcCodeDir.FLASHER_NUMBER;
        e.setSource(fileAffectingObj.affectedBySiblingSource);
        ((ActionListener) assocTable).actionPerformed(e);
    }
}
