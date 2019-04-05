/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dbradley
 */
public class PfCellRndrSrcTypeCol extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private final static JLabel SRC_DIR_TYPE = new JLabel("src");
    private final static JLabel TEST_DIR_TYPE = new JLabel("test");

    public PfCellRndrSrcTypeCol() {
        // 
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
        // there is only one column for this data type (boolean -> ), however
        // there is the potential for others to be added later
        if (column == PfTableModel.PKGFLTR_IS_TEST_SRC_DIR) {
            if ((boolean) value) {
                return TEST_DIR_TYPE;
            }
            return SRC_DIR_TYPE;
        }
        return c;
    }
}
