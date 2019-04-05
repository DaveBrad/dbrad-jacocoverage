/* Copyright (c) 2017-2018 dbradly. All rights reserved. */
package packg.appfunc;

import static dbrad.jacocoverage.plugin.config.ep.EpTableModel.COL_EXCL_APPLY_BOOL;
import static dbrad.jacocoverage.plugin.config.ep.EpTableModel.COL_EXCL_PACKAGE_STRING;
import javax.swing.JPanel;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.zoperation.tstenv.PrepareProject;

/**
 *
 * @author dbradley
 */
public class FuncExcludeTab extends FuncProjectProperties {

    /** nojdoc */
    public final static int COL_APPLY = COL_EXCL_APPLY_BOOL;
    /** nojdoc */
    public final static int COL_EXCLUDE_PACKAGE = COL_EXCL_PACKAGE_STRING;

    private final JTabbedPaneOperator tabbPanel;

    private static final String EXCLUDE_PACKAGES = "Exclude packages";
    private ContainerOperator excludeContainerOper;

    private JLabelOperator errorMsgField;

    private JButtonOperator buttonAdd;
    private JTextFieldOperator textFieldAddEntry;
    private JButtonOperator buttonRemove;

    private JCheckBoxOperator checkboxTestng;
    private JCheckBoxOperator checkboxJunit;
    private JCheckBoxOperator checkboxJunitExtension;

    private JTableOperator excludeTableOper;

    /** nojdoc
     * @param prepareProject
     * @param tabbPanel */
    public FuncExcludeTab(PrepareProject prepareProject, JTabbedPaneOperator tabbPanel) {
        super(prepareProject);
        this.tabbPanel = tabbPanel;
    }

    /** nojdoc */
    private void selectExcludeTab() {
        hasClosedCondition();
        tabbPanel.selectPage(EXCLUDE_PACKAGES);

        if (this.excludeContainerOper == null) {
            int idx = this.tabbPanel.findPage(EXCLUDE_PACKAGES);
            JPanel jp = (JPanel) this.tabbPanel.getComponent(idx);
            this.excludeContainerOper = new ContainerOperator(jp);
        }

        if (this.errorMsgField == null) {
            this.errorMsgField = new JLabelOperator(this.excludeContainerOper, 1);
        }
    }

    /** nojdoc
     * @return  */
    public JLabelOperator getErrorMsgLabel() {
        selectExcludeTab();
        return this.errorMsgField;
    }

    /** nojdoc
     * @return  */
    public JCheckBoxOperator getTestng() {
        selectExcludeTab();

        if (this.checkboxTestng == null) {
            this.checkboxTestng = new JCheckBoxOperator(this.excludeContainerOper,
                    "org.testng.*");
        }
        return this.checkboxTestng;
    }

    /** nojdoc
     * @return  */
    public JCheckBoxOperator getJunit() {
        selectExcludeTab();

        if (this.checkboxJunit == null) {
            this.checkboxJunit = new JCheckBoxOperator(this.excludeContainerOper,
                    "org.junit.*");
        }
        return this.checkboxJunit;
    }

    /** nojdoc
     * @return  */
    public JCheckBoxOperator getJunitExtension() {
        selectExcludeTab();

        if (this.checkboxJunitExtension == null) {
            this.checkboxJunitExtension = new JCheckBoxOperator(this.excludeContainerOper,
                    "org.junit.*");
        }
        return this.checkboxJunitExtension;
    }

    /** nojdoc
     * @return  */
    public JButtonOperator getAddButton() {
        selectExcludeTab();

        if (this.buttonAdd == null) {
            this.buttonAdd = new JButtonOperator(this.excludeContainerOper, "Add");
        }
        return this.buttonAdd;
    }

    /** nojdoc
     * @return  */
    public JTextFieldOperator getAddTextField() {
        selectExcludeTab();

        if (this.textFieldAddEntry == null) {
            this.textFieldAddEntry = new JTextFieldOperator(this.excludeContainerOper);
        }
        return this.textFieldAddEntry;
    }

    /** nojdoc
     * @return  */
    public JButtonOperator getRemoveButton() {
        selectExcludeTab();

        if (this.buttonRemove == null) {
            this.buttonRemove = new JButtonOperator(this.excludeContainerOper, "Remove");
        }
        return this.buttonRemove;
    }

    /** nojdoc
     * @return  */
    public JTableOperator getExcludeTable() {
        selectExcludeTab();

        if (this.excludeTableOper == null) {
            this.excludeTableOper = new JTableOperator(this.excludeContainerOper);
        }
        return this.excludeTableOper;
    }

    /** nojdoc
     * @param excludePackge
     * @return  */
    public int findRowWith(String excludePackge) {
        selectExcludeTab();

        int rowCnt = this.excludeTableOper.getRowCount();

        for (int rowI = 0; rowI < rowCnt; rowI++) {
            String excludeString = (String) getValueAt(rowI, COL_EXCLUDE_PACKAGE);

            if (excludeString.equals(excludePackge)) {
                return rowI;
            }
        }
        return -1;

    }

    /** nojdoc */
    private String notFoundMsg(String excludePackge) {
        return String.format("'Exclude package' ('%s') not found in table.", excludePackge);
    }

    /** nojdoc
     * @param excludePackge */
    public void selectRow(String excludePackge) {

        int rowI = findRowWith(excludePackge);
        if (rowI == -1) {
            throw new AssertionError(notFoundMsg(excludePackge));
        }
        this.excludeTableOper.selectCell(rowI, COL_APPLY);
    }

    /** nojdoc */
    private void setOnOffApply(String excludePackge, boolean appliedState) {
        int rowI = findRowWith(excludePackge);
        if (rowI == -1) {
            throw new AssertionError(notFoundMsg(excludePackge));
        }
        Boolean applyBool = (Boolean) getValueAt(rowI, COL_APPLY);

        if (applyBool != appliedState) {
            this.excludeTableOper.clickOnCell(rowI, COL_APPLY);
        }
    }

    /** nojdoc
     * @param excludePackge */
    public void setOnApply(String excludePackge) {
        setOnOffApply(excludePackge, true);
    }

    /** nojdoc
     * @param excludePackge */
    public void setOffApply(String excludePackge) {
        setOnOffApply(excludePackge, false);
    }

    /**
     * @param row
     * @param column
     * @return  */
    public Object getValueAt(int row, int column) {
        hasClosedCondition();
        return this.excludeTableOper.getModel().getValueAt(row, column);
    }

    /** nojdoc */
    public void doAdd() {
        getAddButton().push();
    }

    /** nojdoc */
    public void doRemove() {
        getRemoveButton().push();
    }

//    public void buttonAddEnabled(boolean enabledStateExpected){
//        assertEquals(enabledStateExpected, getAddButton().isEnabled() );
//    }
}
