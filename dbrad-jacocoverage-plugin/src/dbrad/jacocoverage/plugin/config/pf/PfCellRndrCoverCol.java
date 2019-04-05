/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import static dbrad.jacocofpm.mgr.com.FilePackageUtil.cloneLabel4HasFocusObj;
import dbrad.jacocofpm.mgr.com.PackageAffectingState;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_A_SIBLING;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_FILE_HIDDEN;
import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dbradley
 */
public class PfCellRndrCoverCol extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    //99 need comments
    private static JLabel AFFECTED_BY_SIBLING_COVER_YES;
    private static JLabel AFFECTED_BY_SIBLING_COVERED_BY_PARENT;
    private static JLabel AFFECTED_BY_SIBLING_COVER_NO;

    private static JLabel AFFECTED_BY_SIBLING_COVER_YES_HAS_FOCUS;
    private static JLabel AFFECTED_BY_SIBLING_COVERED_BY_PARENT_HAS_FOCUS;
    private static JLabel AFFECTED_BY_SIBLING_COVER_NO_HAS_FOCUS;

    public PfCellRndrCoverCol() {
        super();

        if (AFFECTED_BY_SIBLING_COVER_YES == null) {
            AFFECTED_BY_SIBLING_COVER_YES = new JLabel("", SwingConstants.CENTER);
            AFFECTED_BY_SIBLING_COVER_YES.setIcon(
                    new ImageIcon(getClass().getResource("resources/icon/affect_by_sibling_cover_yes.png")));

            AFFECTED_BY_SIBLING_COVER_YES_HAS_FOCUS
                    = cloneLabel4HasFocusObj(AFFECTED_BY_SIBLING_COVER_YES);
        }
        if (AFFECTED_BY_SIBLING_COVERED_BY_PARENT == null) {
            AFFECTED_BY_SIBLING_COVERED_BY_PARENT = new JLabel("", SwingConstants.CENTER);
            AFFECTED_BY_SIBLING_COVERED_BY_PARENT.setIcon(
                    new ImageIcon(getClass().getResource("resources/icon/affect_by_sibling_covered_by_parent.png")));

            AFFECTED_BY_SIBLING_COVERED_BY_PARENT_HAS_FOCUS
                    = cloneLabel4HasFocusObj(AFFECTED_BY_SIBLING_COVERED_BY_PARENT);
        }
        if (AFFECTED_BY_SIBLING_COVER_NO == null) {
            AFFECTED_BY_SIBLING_COVER_NO = new JLabel("", SwingConstants.CENTER);
            AFFECTED_BY_SIBLING_COVER_NO.setIcon(
                    new ImageIcon(getClass().getResource("resources/icon/affect_by_sibling_cover_no.png")));

            AFFECTED_BY_SIBLING_COVER_NO_HAS_FOCUS
                    = cloneLabel4HasFocusObj(AFFECTED_BY_SIBLING_COVER_NO);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        row = table.convertRowIndexToModel(row);

        // there is only one column for this data type (FilePackageCoverStateEnum), so no need to
        // check the column number
        FilePackageOfSrc file = (FilePackageOfSrc) table.getModel().getValueAt(row, PKGFLTR_FILE_HIDDEN);

        PackageAffectingState affectedState = file.getAffectedBySibling();

        if (affectedState == AFFECTED_BY_A_SIBLING
                || affectedState == AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES) {

            // define what type of icon label to be displayed
            switch (file.coverStateEnum) {
                case COVER_YES:
                    return hasFocus
                            ? AFFECTED_BY_SIBLING_COVER_YES_HAS_FOCUS
                            : AFFECTED_BY_SIBLING_COVER_YES;

                case COVER_BY_PARENT:
                    // if this is my parent that is covering me, then it is
                    // a different arrangement
                    File parentMe = file.getParentFile();

                    if (parentMe != null) {
                        FilePackageOfSrc affectingFile = file.affectedBySiblingSource;

                        //99 not making sense
                        if (affectingFile != null) {
                            if (!parentMe.getAbsolutePath().equals(affectingFile.getAbsolutePath())) {
                                return hasFocus
                                        ? AFFECTED_BY_SIBLING_COVERED_BY_PARENT_HAS_FOCUS
                                        : AFFECTED_BY_SIBLING_COVERED_BY_PARENT;
                            }
                        }
                    }
                    break;

                case COVER_NO:
                    return hasFocus
                            ? AFFECTED_BY_SIBLING_COVER_NO_HAS_FOCUS
                            : AFFECTED_BY_SIBLING_COVER_NO;
            }
        }
        return file.coverStateEnum.getLabelForState(hasFocus);
    }
}
