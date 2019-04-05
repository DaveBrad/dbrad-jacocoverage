/* Copyright (c) 2017 dbradley. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocoverage.plugin.config.WrappedAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;

/**
 *
 * @author dbradley
 */
public class PfTableCellAccessiblityAction extends WrappedAction implements ActionListener {

    /**
     * The table that is being cell-focused controlled
     */
    private final PfJTable table;

    /**
     * Create an action control for key strokes that cause the change for
     * cell-focus.
     */
    PfTableCellAccessiblityAction(PfJTable table, KeyStroke keyStroke) {
        super(table, keyStroke);
        this.table = table;
    }

    /*
     *  Provide the custom behaviour of the Action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        PfTableModel tableModel = (PfTableModel) this.table.getModel();
//99 document
        int currentRow = this.table.getSelectedRow();
        int currentColumn = this.table.getSelectedColumn();

        // based on the column actions need to be done
        // (best gues on most used optimization)
        if (currentColumn == PfTableModel.PKGFLTR_COVERAGE) {
            // need to toggle the setting for coverage (low use 1)
            tableModel.selectedRowColumn(this.table, currentRow, currentColumn);

        } else if (currentColumn == PfTableModel.PKGFLTR_PACKAGE) {
            // need to popup menu for package (low use 2)

            // positon the menu to popup close to the cell
            tableModel.popupEventProcess(table, currentRow, currentColumn, null);

        } else if (currentColumn == PfTableModel.PKGFLTR_ON_OFF) {
            // need to toggle the setting for on/off (low use 3)
            tableModel.selectedRowColumn(this.table, currentRow, currentColumn);
        }
    }
}
