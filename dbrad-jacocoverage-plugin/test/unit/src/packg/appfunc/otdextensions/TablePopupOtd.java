/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import static junit.framework.TestCase.assertEquals;
import org.jtestdb.objecttestdata.ObjectTestData;
import org.jtestdb.objecttestdata.OtdAut;
import org.jtestdb.objecttestdata.OtdElementGroup;
import org.jtestdb.objecttestdata.OtdSingle;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import packg.appfunc.FuncPkgFilterTable;

/**
 * Class for test-data'ng any popup menu in a package-filter-table, regards of
 * the table (root or associate).
 * <p>
 * This is a free-form arrangement
 *
 * @author dbradley
 */
public class TablePopupOtd extends OtdSingle {

    /**
     * Create a package-filter table popup-menu object for a package column's
     * data representing the popup-menu items.
     */
    public TablePopupOtd() {
        //
    }

    /**
     *
     * @param sourceDirectory
     * @param packageName
     * @param testDataClassName
     */
    public TablePopupOtd(String sourceDirectory, String packageName, String testDataClassName) {
        // this is free-form so there null for the mySingleObj parameter
        super(null, sourceDirectory, packageName, testDataClassName);
    }

    /**
     *
     * @return
     */
    @Override
    protected Object mySingleObject() {
        return null;
    }

    /**
     *
     * @param otd
     * @param oos
     */
    @Override
    protected void add2CodeStatements4Single(ObjectTestData otd, OtdAut.ObjOfSingle oos) {
        // should be a popup menu
        JPopupMenuOperator popupOper = (JPopupMenuOperator) oos.objInAut;

        MenuElement[] menuArr = popupOper.getSubElements();
        ArrayList<String> menuList = new ArrayList<>();

        for (MenuElement menuI : menuArr) {
            if (menuI instanceof JMenuItem) {
                String menuText = ((JMenuItem) menuI).getText();
                menuList.add(menuText);
            }
        }
        for (int order = 0; order < menuList.size(); order++) {
            add3CodeGenSuper(otd, order, menuList.get(order));
        }
    }

    /**
     *
     * @param action
     * @param expectedIndex
     * @param tdeg
     *
     * @return
     */
    @Override
    protected Object processTDEG(OtdElementGroup.ProcessAction action, int expectedIndex, OtdElementGroup tdeg) {
        String menuItemText = (String) tdeg.getElementN(0);

        Object result = null;

        switch (action) {
            case TOSTRING_TDEG:
                // 
                result = String.format("%s", menuItemText);
                break;

            case VERIFY_TDEG:
                // the assumption is that the user provides a valid 
                // and expected position into the table, if this does 
                // not fit then we assume an application error. OR, this 
                // is a calling test code error and needs to be fixed
                Object[] thisObj = this.getTdegDataValuesFromArr(expectedIndex);

                assertEquals(thisObj[0], menuItemText);
                break;
            default:
                throw new AssertionError(action.name());
        }
        return result;
    }

    /**
     *
     * @param order
     * @param menuValue
     *
     * @return
     */
    public int add(int order, String menuValue) {
        return super.add1Super(order, menuValue);
    }

    /**
     *
     * @param funcPkgTable
     * @param version
     * @param name
     *
     * @return
     */
    public TablePopupOtd getSingle(FuncPkgFilterTable funcPkgTable, int version, String name) {
        // there should be a popup present to capture against
        JPopupMenuOperator popupOper = funcPkgTable.expectedPopup();
        OtdAut.ObjOfSingle someObject = newAutObj(popupOper);

        return (TablePopupOtd) super.get1SuperFreeForm(someObject, version, name);
    }

    /**
     *
     * @param funcPkgTable
     */
    public void verify(FuncPkgFilterTable funcPkgTable) {
        super.verify1Super(new TablePopupFixT(funcPkgTable), true);
    }
}
