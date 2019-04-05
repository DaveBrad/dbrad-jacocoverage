/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import java.util.EnumSet;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import org.jtestdb.objecttestdata.OtdElementGroup;
import org.jtestdb.objecttestdata.OtdFixtureClass;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import packg.appfunc.FuncPkgFilterTable;

/**
 * Class for table popup menu OTD fixture. This is a free-form arrangement in
 * that the table is persistent but the popup is fleeting and thus may only be
 * found when it occurs. thus, the refresh needs to acquire the popup at refresh
 * time. Under Jelly, Jemmy this is a timer of 60 seconds default.
 *
 * @author dbradley
 */
public class TablePopupFixT extends OtdFixtureClass {

    private final FuncPkgFilterTable fcPkgFilterTable;

    /**
     *
     * @param fcPkgFilterTable
     */
    public TablePopupFixT(FuncPkgFilterTable fcPkgFilterTable) {
        super(TablePopupOtd.class);

        this.fcPkgFilterTable = fcPkgFilterTable;
    }

    /**
     *
     * @param objects
     */
    @Override
    public void test(Object[] objects) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * nojdoc
     * @param withTypesSet
     */
    @Override
    public void refresh(EnumSet<?> withTypesSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * nojdoc
     */
    @Override
    public void refresh() {
        // this relates to a popup menu which is none-persistent so it needs to be
        // found each time a refresh is performed

        JPopupMenuOperator popupOper = this.fcPkgFilterTable.expectedPopup();

        MenuElement[] menuArr = popupOper.getSubElements();

        // convert data to the ATDI for the popup menu items
        int order = 0;
        for (MenuElement menuI : menuArr) {
            if (menuI instanceof JMenuItem) {
                String menuText = ((JMenuItem) menuI).getText();
                OtdElementGroup tdeg = OtdElementGroup.newAdd(order, menuText);

                this.appendTdegItem(tdeg);

                order++;
            }
        }
    }
}
