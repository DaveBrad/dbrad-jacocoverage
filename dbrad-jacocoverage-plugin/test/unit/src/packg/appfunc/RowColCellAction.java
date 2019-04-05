/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc;

import dbrad.jacocoverage.plugin.config.pf.PfTableModel;
import java.awt.event.MouseEvent;

/**
 *
 * @author dbradley
 */
public class RowColCellAction extends RowCol {

    /** nojdoc */
    private final FuncPkgFilterTable fcPkgTable;

    /** nojdoc
     * @param fcPkgTable
     * @param rowCol */
    public RowColCellAction(FuncPkgFilterTable fcPkgTable, RowCol rowCol) {
        super(rowCol.r, rowCol.c);
        this.fcPkgTable = fcPkgTable;
    }

    /** nojdoc */
    public void click() {
        this.fcPkgTable.getTableOperator().clickOnCell(this.r, this.c);
    }

    /** nojdoc
     * @param countClicks */
    public void clickWithCount(int countClicks) {
        this.fcPkgTable.getTableOperator().clickOnCell(this.r, this.c, countClicks);
    }

    /** nojdoc */
    public void pressMousePrim() {
        this.fcPkgTable.pressMouse(r, c, MouseEvent.BUTTON1);
    }

    /** nojdoc */
    public void pressMouseMid() {
        this.fcPkgTable.pressMouse(r, c, MouseEvent.BUTTON2);
    }

    /** nojdoc */
    public void pressMouseSec() {
        this.fcPkgTable.pressMouse(r, c, MouseEvent.BUTTON3);
    }

    /** nojdoc */
    public void releaseMousePrim() {
        this.fcPkgTable.releaseMouse(r, c, MouseEvent.BUTTON1);
    }

    /** nojdoc */
    public void releaseMouseMid() {
        this.fcPkgTable.releaseMouse(r, c, MouseEvent.BUTTON2);
    }

    /** nojdoc */
    public void releaseMouseSec() {
        this.fcPkgTable.releaseMouse(r, c, MouseEvent.BUTTON3);
    }

    /** nojdoc */
    public void popupOfRow() {
        // there is only one column that allows the popup
        this.fcPkgTable.pressMouse(this.r, PfTableModel.PKGFLTR_PACKAGE, MouseEvent.BUTTON3);
    }
}
