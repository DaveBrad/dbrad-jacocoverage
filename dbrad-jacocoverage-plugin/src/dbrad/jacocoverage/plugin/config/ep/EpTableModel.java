/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 */
package dbrad.jacocoverage.plugin.config.ep;

import static dbrad.jacocoverage.plugin.config.ep.EpTableModel.COL_EXCL_PACKAGE_STRING;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 * Table model for the exclude packages filter UI.
 *
 * @author dbradley (2017)
 */
public class EpTableModel extends DefaultTableModel implements MouseListener {

    private static final long serialVersionUID = 1L;

    public final static int COL_EXCL_APPLY_BOOL = 0;
    public final static int COL_EXCL_PACKAGE_STRING = 1;
    public final static int COL_EXCL_APPLY_OVERRIDDEN_BY_SUPER = 2;

    private final EpJTable epTable;
    private final EpJPanel ePanel;

    private static EpPackageFormatComparator packageFormatComparator;

    /**
     * Table columns type.
     */
    private final Class<?>[] types = new Class<?>[]{Boolean.class, String.class, Boolean.class};

    /**
     * Indicates if table columns are editable.
     */
    private final boolean[] canEdit = new boolean[]{true, false, false};

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public EpTableModel(JTable table) {
        super(new Object[][]{}, new String[]{"Apply", "Exclude packages", "overridden"});

        if (packageFormatComparator == null) {
            packageFormatComparator = new EpPackageFormatComparator();
        }
        this.epTable = (EpJTable) table;

        EpJTableDatum eDatum = (EpJTableDatum) this.epTable.datum;
        this.ePanel = eDatum.epJPanel;

        // need to explicitly control the cell focus of the table for forward
        // and backward
        // (Provided for support of accessibility.)
        new EpTableCellFocusControlAction(this.epTable, KeyStroke.getKeyStroke("TAB"));
        new EpTableCellFocusControlAction(this.epTable, KeyStroke.getKeyStroke("shift TAB"));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    public void addRow(Boolean applyBool, String packageFormat) {
        super.addRow(new Object[]{applyBool, packageFormat});
    }

    public void addRowViaAddButton(String addItem) {
        super.addRow(new Object[]{(Boolean) true, addItem});
    }

    @SuppressWarnings("unchecked")
    public void sortRows() {
        // prevent a sort if there is a validation error
        List<?> data = this.getDataVector();
        Collections.sort(data, packageFormatComparator);

        validateOverridden();
    }

    public void validateOverridden() {
        // the packages may be apply-mode and thus will affect the child
        ArrayList<String> packagesApplyArr = new ArrayList<>(4);

        int rowsLen = this.getRowCount();
        for (int rowI = 0; rowI < rowsLen; rowI++) {
            Boolean applySet = (Boolean) this.getValueAt(rowI, COL_EXCL_APPLY_BOOL);

            // assume not overridden
            Boolean overridden = false;
            this.setValueAt(overridden, rowI, COL_EXCL_APPLY_OVERRIDDEN_BY_SUPER);

            // get the package-string and remove the end '*'
            String packStr = (String) this.getValueAt(rowI, COL_EXCL_PACKAGE_STRING);
            String packStrNoWC = packStr.substring(0, packStr.length() - 1);

            // check each currently applied against this newer item
            for (String applyingPackage : packagesApplyArr) {
                if (packStrNoWC.startsWith(applyingPackage)) {
                    // aaa.bbb  startswith aaa. therefore the item is overridden
                    overridden = true;

                    this.setValueAt(overridden, rowI, COL_EXCL_APPLY_OVERRIDDEN_BY_SUPER);
                    break;
                }
            }
            // if the apply is NOT then do not process the data as affecting anything
            // else
            if (!applySet) {
                continue;
            }
            // if not already overridden this may impact others
            if (!overridden) {
                packagesApplyArr.add(packStrNoWC);
            }
        }
        epTable.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // nothing to do
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // nothing to do
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        EpJTable eTable = (EpJTable) e.getSource();

        Point ePoint = e.getPoint();
        int column = eTable.columnAtPoint(ePoint);

        if (column == EpTableModel.COL_EXCL_APPLY_BOOL) {
            // the setting has changed so we only need to store the data away
            this.ePanel.saveToJson();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // nothing to do
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // nothing to do
    }

    public boolean isCellFocusable(int rowIndex, int columnIndex) {
        // only the 'apply' column focusable while all rows are focusable 
        return columnIndex == COL_EXCL_APPLY_BOOL;
    }
}

@SuppressWarnings("rawtypes")
class EpPackageFormatComparator implements Comparator {

    EpPackageFormatComparator() {
        //
    }

    @Override
    public int compare(Object a, Object b) {
        List<?> v1 = (List) a;
        List<?> v2 = (List) b;

        String o1s = (String) v1.get(COL_EXCL_PACKAGE_STRING);
        String o2s = (String) v2.get(COL_EXCL_PACKAGE_STRING);

        return o1s.compareTo(o2s);
    }
}
