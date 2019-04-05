/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import static dbrad.jacocofpm.mgr.com.FilePackageUtil.cloneLabel4HasFocusObj;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_FILE_HIDDEN;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dbradley
 */
public class PfCellRndrOnCodeFolderState extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static JLabel onState;
    private static JLabel onStateHasFocus;
    
    private static JLabel offState;
    private static JLabel offStateHasFocus;
    
    private static JLabel noneBaseFileState;
    
    public PfCellRndrOnCodeFolderState() {
        super();

        if (onState == null) {
            onState = new JLabel("", SwingConstants.CENTER);
            onState.setIcon(new ImageIcon(getClass().getResource("resources/icon/on_code_folder.png")));
            
            onStateHasFocus = cloneLabel4HasFocusObj(onState);
                    
            offState = new JLabel("", SwingConstants.CENTER);
            offState.setIcon(new ImageIcon(getClass().getResource("resources/icon/off_code_folder.png")));

            offStateHasFocus = cloneLabel4HasFocusObj(offState);
            
            noneBaseFileState = new JLabel(" ", SwingConstants.CENTER);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        PfCellRndrOnCodeFolderState c 
                = (PfCellRndrOnCodeFolderState) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        
        // there is only one column for this data type (FilePackageCoverStateEnum), so no need to
        // check the column number
        int rowConverted = table.convertRowIndexToModel(row);

        FilePackageOfSrc packageSrcFile = (FilePackageOfSrc) table.getModel().getValueAt(rowConverted, PKGFLTR_FILE_HIDDEN);

        // for a none basefile display an empty cell
        if (!packageSrcFile.isBaseFile()) {
            return noneBaseFileState;
        }

        if (packageSrcFile.isOnCodeFolderState()) {
            return hasFocus ? onStateHasFocus : onState;
        }
        return hasFocus ? offStateHasFocus : offState;
    }
}
