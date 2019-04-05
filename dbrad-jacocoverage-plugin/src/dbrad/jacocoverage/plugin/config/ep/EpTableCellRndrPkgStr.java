/* Copyright (c) 2017 dbradly. All rights reserved. */

package dbrad.jacocoverage.plugin.config.ep;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dbradley
 */
public class EpTableCellRndrPkgStr extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static Color overriddenBackColor = null;

    public EpTableCellRndrPkgStr() {
        if (overriddenBackColor == null) {
            overriddenBackColor = new Color(242, 242, 242);
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

        EpTableModel model = (EpTableModel) table.getModel();

        Boolean overridden = (Boolean) model.getValueAt(row, EpTableModel.COL_EXCL_APPLY_OVERRIDDEN_BY_SUPER);

        if (overridden) {
            c.setForeground(Color.gray);
            c.setBackground(overriddenBackColor);
        } else {
            c.setForeground(Color.black);
            c.setBackground(Color.white);
        }
        return c;
    }
}
