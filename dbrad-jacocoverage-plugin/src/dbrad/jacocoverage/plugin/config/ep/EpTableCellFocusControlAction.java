/* Copyright (c) 2017 dbradley. */
package dbrad.jacocoverage.plugin.config.ep;

import dbrad.jacocoverage.plugin.config.WrappedAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;

/**
 * Class that is used for control of cell-focus within the JTable as the cells
 * are not controlled by Swing traversal focus actions.
 * <p>
 * Basically the cell focus is table controlled not swing.
 *
 * @author dbradley
 */
class EpTableCellFocusControlAction extends WrappedAction implements ActionListener {

    /**
     * The table that is being cell-focused controlled
     */
    private final EpJTable epTable;

    /**
     * Create an action control for key strokes that cause the change for
     * cell-focus.
     */
    EpTableCellFocusControlAction(EpJTable epTable, KeyStroke keyStroke) {
        super(epTable, keyStroke);
        this.epTable = epTable;
    }

    /*
     *  Provide the custom behaviour of the Action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        EpTableModel tableModel = (EpTableModel) this.epTable.getModel();
        boolean notFocusableCell;

        // Within the loop do a focus change as if the original action.
        // HOWEVER, if this is a cell-column that is not allowed
        // focus, repeat.
        do {
            // change cell focus to the next cell
            invokeOriginalAction(e);
            
            int currentColumn = this.epTable.getSelectedColumn();
            int currentRow = this.epTable.getSelectedRow();
            
            int rowConverted = epTable.convertRowIndexToModel(currentRow);

            // if not focusable go to next cell-column for action
            notFocusableCell = !tableModel.isCellFocusable(rowConverted, currentColumn);
        } while (notFocusableCell);
    }
}
