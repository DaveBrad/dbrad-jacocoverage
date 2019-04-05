/* Copyright (c) 2017 dbradly. All rights reserved. */

package dbrad.jacocoverage.plugin.config.ep;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 *
 * @author dbradley
 */
public class EpRemoveButt extends JButton implements ActionListener {

    private static final long serialVersionUID = 0L;

    private EpJPanel epJPanel;

    private JTable jTableExcludePackages;

    public EpRemoveButt() {
        super.setMnemonic('r');
    }

    public void setPrgCfgData(EpJPanel epJPanel) {
        this.epJPanel = epJPanel;
        this.jTableExcludePackages = epJPanel.jTableExcludePackages;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * Remove from the exclusion package table row items.
         */
        EpTableModel tableModel = (EpTableModel) this.jTableExcludePackages.getModel();

        int[] rowsSelectedArr = this.jTableExcludePackages.getSelectedRows();

        // remove the selected rows in reverse order,otherwise an
        // exception will occur
        for (int i = rowsSelectedArr.length - 1; i > -1; i--) {
            tableModel.removeRow(rowsSelectedArr[i]);
        }
        // store the data away instead of waiting for the OK button
        this.epJPanel.saveToJson();
    }
}
