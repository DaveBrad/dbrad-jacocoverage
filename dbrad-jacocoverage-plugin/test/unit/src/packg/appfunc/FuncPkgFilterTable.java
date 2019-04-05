/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc;

import dbrad.jacocoverage.plugin.config.pf.PfCellRndrPackageFormatCol;
import dbrad.jacocoverage.plugin.config.pf.PfJTable;
import dbrad.jacocoverage.plugin.config.pf.PfTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.MenuElement;
import javax.swing.table.DefaultTableCellRenderer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.TextAreaOperator;
import org.openide.util.Exceptions;

/**
 * Class to emulate special actions on the package filter table which are
 * unsupported through the current operators and actions of JellyTools or Jemmy.
 * <p>
 * <ol>
 * <li>check-box on-off (only on the top row of a a source-code group</li>
 * <li>check-box cover </li>
 * <li>column package-string
 * <ol>
 * <li>col-render: color of background</li>
 * <li>popup menu
 * <ol>
 * <li> affected-by: package-string (another project affects this)
 * <p>
 * single popup menu of affecting project item
 * </li>
 * <li> affecting: package-string [impact on other project(s)]
 * <ol>
 * <li> contains multiple menu-item(s) affected by this project (click moves to
 * that sub-project)</li>
 * </oL>
 * </li>
 * </ol>
 * </li>
 * </ol>
 * </li>
 * </ol>
 * <p>
 * This is notice-able for MousePressed, MouseReleased actions.
 * <p>
 * ComponentOperator has pressMouse/releaeMouse but is not fitted for doing the
 * action on a specific MouseButton.
 *
 * @author dbradley
 */
public class FuncPkgFilterTable extends FuncProjectProperties {

    /**
     * The color of a cell and what it represents.
     */
    public enum PackageColor {
        /**
         * The none-affected color to test against for a cell in a package filter table.
         */
        NONE_AFFECTED_COLOR,
        /**
         * The affect-by color to test against for a cell in a package filter table.
         */
        AFFECTED_BY_COLOR,
        /**
         * The affecting color to test against for a cell in a package filter table.
         */
        AFFECTING_COLOR
    }
    /** ON-OFF column integer */
    public static int ON_OFF_COL = PfTableModel.PKGFLTR_ON_OFF;
    /** COVER column integer */
    public static int COVER_COL = PfTableModel.PKGFLTR_COVERAGE;
    /** TEST or SOURCE code column integer */
    public static int TEST_COL = PfTableModel.PKGFLTR_IS_TEST_SRC_DIR;
    /** ICON column integer */
    public static int ICON_COL = PfTableModel.PKGFLTR_ICON;
    /** Source/test source-code folder name column integer */
    public static int SRC_FOLDER = PfTableModel.PKGFLTR_SRC_CODE_LABEL;
    /** Package filter string column integer */
    public static int PKG_STR = PfTableModel.PKGFLTR_PACKAGE;

    private JTableOperator pfTableOper;
    private PfJTable pfTable;
    private PfTableModel model;

    private TextAreaOperator includesTextAreaOperator;

    private final boolean rootTable;

    private JPopupMenuOperator popupMenu;

    FuncPkgFilterTable(JTableOperator pfTableOper, boolean rootTable) {
        super();
        this.rootTable = rootTable;

        this.pfTableOper = pfTableOper;

        this.pfTable = (PfJTable) pfTableOper.getSource();
        this.model = (PfTableModel) pfTable.getModel();
    }

   /**
    * Get the package filter table operator for this objects table.
    * 
    * @return JTableOperator
    */
    public JTableOperator getTableOperator() {
        return this.pfTableOper;
    }

    @Override
    public String getIncludesTextArea() {
        hasClosedCondition();

        // for this to work we have to get a higher dialog and then
        // process from the super
        Component comp = this.getSource();

        int limit = 20;
        while (limit > 0) {
            if (comp instanceof JDialog) {
                break;
            }
            comp = comp.getParent();
            limit--;
        }
        this.projPropertiesDialog = new JDialogOperator((JDialog) comp);

        return super.getIncludesTextArea();
    }

    /** nojdoc
     *
     * @return */
    public PfJTable getSource() {
        return this.pfTable;
    }

    /** nojdoc
     *
     * @return */
    public PfTableModel getModel() {
        return this.model;
    }

    /** nojdoc
     *
     * @return */
    public boolean isRootTable() {
        return this.rootTable;
    }

    /** nojdoc
     *
     * @return */
    public String getProjNameOfTable() {
        return pfTable.getNbProjectName();
    }

    // the actions on a pfTable row are
    // 1) on-off (only on the top row of a a source-code group
    // 2) cover checkbox
    // 3) package-string column (
    // 3a) color of background
    // 3b) popup menu
    // 3b.1) affecting package-string
    // 3b.1.1)
    // 3b.1.1) contains a multiple menuitems affected by this project (click moves to that project-object)
    // 3b.2) affected-by another project's setting 
    // 3b.2.1) contains a single menuitem of the affecting project (click moves to that project-object)
    private Point getPointOfRowColumn(int row, int colum) {
        // the points will be coordinates within the table and not
        // the screen. MouseEvent provides coordinates within its source
        // (that is, the table)
        Rectangle rect = pfTableOper.getCellRect(row, colum, false);

        // select a point that is a little to the right-down from the top-left
        return new Point(rect.x + 5, rect.y + 5);
    }

    /**
     * Click the <b>On-off</b> state for the source-code-folder of the table.
     *
     * @param srcCodePattern a simple wildcard/exact string of the
     *                       source-code-folder to match with ('*' asterisk is
     *                       simple wildcard character)
     */
    public void clickOnOffFor(String srcCodePattern) {
        clickCol(ON_OFF_COL, srcCodePattern, "*");
    }

    /**
     * Set OFF state for the source-code-folder of the table.
     *
     * @param srcCodePattern a simple wildcard/exact string of the
     *                       source-code-folder to match with ('*' asterisk is
     *                       simple wildcard character)
     */
    public void setOff4OnOff(String srcCodePattern) {
        RowCol rc = findRowColOf(ON_OFF_COL, srcCodePattern, "*");
        setOffChecked(rc);
    }

    /**
     *  Set OFF state for the source-code-folder of the table.
     *
     * @param srcCodePattern a simple wildcard/exact string of the
     *                       source-code-folder to match with ('*' asterisk is
     *                       simple wildcard character)
     */
    public void setOn4OnOff(String srcCodePattern) {
        RowCol rc = findRowColOf(ON_OFF_COL, srcCodePattern, "*");
        setOnChecked(rc);
    }

    //-----------------
     /**
      * */
    private void setOnChecked(RowCol roCo) {
        hasClosedCondition();

        Boolean onOffBool = (Boolean) getValueAt(roCo.r, roCo.c);
        if (onOffBool) {
            return;
        }
        this.pfTableOper.clickOnCell(roCo.r, roCo.c);
    }

    /** nojdoc */
    private void setOffChecked(RowCol roCo) {
        hasClosedCondition();

        Boolean onOffBool = (Boolean) getValueAt(roCo.r, roCo.c);
        if (!onOffBool) {
            return;
        }
        this.pfTableOper.clickOnCell(roCo.r, roCo.c);
    }

    /**
     * Click the <b>Cover</b> state for the source-code-folder AND
     * package-filter contains of the table.
     *
     * @param srcCodePattern  a simple wildcard/exact string of the
     *                        source-code-folder to match with ('*' asterisk is
     *                        simple wildcard character)
     *
     * @param packageContains string for use to find an exact/portion-of match
     *                        within the package-filter (NO wildcard)
     */
    public void clickCoverFor(String srcCodePattern, String packageContains) {
        clickCol(COVER_COL, srcCodePattern, packageContains);
    }

    /**
     * Set OFF the <b>Cover</b> state for the source-code-folder AND
     * package-filter contains of the table.
     *
     * @param srcCodePattern  a simple wildcard/exact string of the
     *                        source-code-folder to match with ('*' asterisk is
     *                        simple wildcard character)
     *
     * @param packageContains string for use to find an exact/portion-of match
     *                        within the package-filter (NO wildcard)
     */
    public void setOffCoverOf(String srcCodePattern, String packageContains) {
        RowCol rc = findRowColOf(COVER_COL, srcCodePattern, packageContains);
        setOffChecked(rc);
    }

    /**
     * Set ON the <b>Cover</b> state for the source-code-folder AND
     * package-filter contains of the table.
     *
     * @param srcCodePattern  a simple wildcard/exact string of the
     *                        source-code-folder to match with ('*' asterisk is
     *                        simple wildcard character)
     *
     * @param packageContains string for use to find an exact/portion-of match
     *                        within the package-filter (NO wildcard)
     */
    public void setOnCoverOf(String srcCodePattern, String packageContains) {
        RowCol rc = findRowColOf(COVER_COL, srcCodePattern, packageContains);
        setOnChecked(rc);
    }

    /**
     * ClickColumn a RowCol (row, column) value for code-folder-cell string
     * pattern (with simple '*' wildcard) AND a package-filter-cell that
     * contains all or part of a <code>packageContains</code> string.
     * <p>
     * A package-filter-cell may contain an '*'; thus, no wildcard capability
     * but just a unique portion of the string will cause a match. (Note: a
     * package-filter-cell is unique with any given source-code-folder-cell.)
     *
     * @param columnRepresent the column for the <code>RowCol</code> to
     *                        represent after code-folder and package-filter are
     *                        found (the found would represent the row part of
     *                        <code>RowCol</code>)
     *
     * @param srcCodePattern  a simple wildcard/exact string of the
     *                        source-code-folder to match with ('*' asterisk is
     *                        simple wildcard character)
     *
     * @param packageContains string for use to find an exact/portion-of match
     *                        within the package-filter (NO wildcard)
     *
     * @return <code>RowCol</code> object that is the cell of interest and is on
     *         the code-folder and package-filter row
     *
     * @throws AssertionError if an expected item not found
     */
    private RowColCellAction clickCol(int columnRepresent, String srcCodePattern,
            String packageContains) {
        hasClosedCondition();

        RowCol roCo = findRowColOf(columnRepresent, srcCodePattern, packageContains);

        this.pfTableOper.clickOnCell(roCo.r, roCo.c);

        return new RowColCellAction(this, roCo);
    }

    /**
     * Find a RowCol (row, column) value for code-folder-cell string pattern
     * (with simple '*' wildcard) AND a package-filter-cell that contains all or
     * part of a <code>packageContains</code> string.
     * <p>
     * A package-filter-cell may contain an '*'; thus, no wildcard capability
     * but just a unique portion of the string will cause a match. (Note: a
     * package-filter-cell is unique with any given source-code-folder-cell.)
     *
     * @param columnRepresent the column for the <code>RowCol</code> to
     *                        represent after code-folder and package-filter are
     *                        found (the found would represent the row part of
     *                        <code>RowCol</code>)
     *
     * @param srcCodePattern  a simple wildcard/exact string of the
     *                        source-code-folder to match with ('*' asterisk is
     *                        simple wildcard character)
     *
     * @param packageContains string for use to find an exact/portion-of match
     *                        within the package-filter (NO wildcard)
     *
     * @return <code>RowCol</code> object that is the cell of interest and is on
     *         the code-folder and package-filter row
     *
     * @throws AssertionError if an expected item not found
     */
    public RowCol findRowColOf(int columnRepresent, String srcCodePattern,
            String packageContains) {
        hasClosedCondition();
        this.selectTable();
        pauseMs(200);

        // the source-code-folder-name and packagePattern are unique for each row within
        // the package-table
        // wildcard would be nice, but '*' asterisk is used in the package contains
        // field so does not apply but does work for code-folder
        //
        // need to change simple '*' to '.*' for the pattern to be a good pattern
        // for the Matcher and Pattern classes
        srcCodePattern = srcCodePattern.replace("*", ".*");
        Pattern codeFolderPatternCompiled = Pattern.compile(srcCodePattern);

        int rowCnt = this.pfTableOper.getRowCount();

        for (int rowI = 0; rowI < rowCnt; rowI++) {
            String codeFolderCell = (String) getValueAt(rowI, SRC_FOLDER);

            Matcher matchToCodeFolder = codeFolderPatternCompiled.matcher(codeFolderCell);
            // the source-code-folder needs to match 1st
            if (!matchToCodeFolder.matches()) {
                continue;
            }
            // check package contains now
            String packageStrCell = (String) getValueAt(rowI, PKG_STR);

            if (packageStrCell.equals("")) {
                // what is displayed vs internal data
                packageStrCell = "*";
            }
            // test for found condition
            if (packageStrCell.contains(packageContains)) {
                return new RowCol(rowI, columnRepresent);
            }
        }
        throw new AssertionError(String.format("NOT found: '%s' AND contains '%s' pair.",
                srcCodePattern, packageContains));
    }

    /** nojdoc
     *
     * @param rowCol
     * @param button */
    public void pressAndReleaseMouse(RowCol rowCol, int button) {
        hasClosedCondition();

        pressMouse(rowCol, button);
        pauseMs(150);
        releaseMouse(rowCol, button);
        pauseMs(150);

    }

    /** nojdoc
     *
     * @param row
     * @param col
     * @param button */
    public void pressAndReleaseMouse(int row, int col, int button) {
        hasClosedCondition();

        RowCol rowColInt = new RowCol(row, col);
        pressAndReleaseMouse(rowColInt, button);
    }

    /** nojdoc
     *
     * @param rowCol
     * @param button */
    public void pressMouse(RowCol rowCol, int button) {
        pressMouse(rowCol.r, rowCol.c, button);
    }

    /** nojdoc
     *
     * @param row
     * @param column
     * @param button */
    public void pressMouse(int row, int column, int button) {
        hasClosedCondition();
        this.selectTable();

        performMouseListenerMethod("mousePressed",
                getPointOfRowColumn(row, column), button);
    }

    /** nojdoc
     *
     * @param rowCol
     * @param button */
    public void releaseMouse(RowCol rowCol, int button) {
        releaseMouse(rowCol.r, rowCol.c, button);
    }

    /** nojdoc
     *
     * @param row
     * @param column
     * @param button */
    public void releaseMouse(int row, int column, int button) {
        hasClosedCondition();
        this.selectTable();

        performMouseListenerMethod("mouseReleased",
                getPointOfRowColumn(row, column), button);
    }

    /** nojdoc */
    private void performMouseListenerMethod(String methodName, Point point, int button) {
        // to emulate a mouse-press/release from a direct invoke of the MouseListener methods
        // need to invoke indirectly via a thread.
        // WHY? 
        // -- MouseReleased/Pressed is an event rather than an invoke
        // -- A direct invoke could result in a "hang" condition for any test script
        //
        // a basic message that will allow the action to be done
        MouseEvent me = new MouseEvent(pfTable,
                0, 0, 260,
                point.x, point.y,
                1, true, button);

        Class<?> clz = model.getClass();

        // so do the mouse-action in a a thread for the popup dialog
        Thread someThread = new Thread() {

            @Override
            public void run() {
                try {
                    Method mthd = clz.getMethod(methodName, MouseEvent.class);

                    mthd.invoke(model, new Object[]{me});

                } catch (NoSuchMethodException | SecurityException
                        | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException ex) {
                    // should not happen unless code is changed in the
                    // plugin
                    Exceptions.printStackTrace(ex);
                }
            }
        };
        // Start a thread to perform the mouse-action
        someThread.start();
    }

    /**
     * Is the table the currently selected tabb.
     *
     * @return true if selected
     */
    public boolean isSelectedTable() {
        hasClosedCondition();

        return this.pfTableOper.isShowing();
    }

    /**
     * Select the table and make it active.
     */
    public void selectTable() {
        hasClosedCondition();

        JTabbedPaneOperator jpo;

        if (isRootTable()) {
            Component comp = (Component) this.pfTable;

            // find the tabbed-pane
            while (!(comp instanceof JTabbedPane)) {
                comp = comp.getParent();
            }
            // have the tabbed pane that contains the tabbed-pane
            jpo = new JTabbedPaneOperator((JTabbedPane) comp);
        } else {
            Component comp = (Component) this.pfTable;

            // find the tabbed-pane
            while (!(comp instanceof JTabbedPane)) {
                comp = comp.getParent();
            }
            // have the tabbed pane that contains the tabbed-pane
            jpo = new JTabbedPaneOperator((JTabbedPane) comp);

            // however need to select the associate-packages 
            comp = jpo.getParent();
            // find the tabbed-pane
            while (!(comp instanceof JTabbedPane)) {
                comp = comp.getParent();
            }
            // have the tabbed pane that contains the top-tabbed-pane
            JTabbedPaneOperator jpoTop;

            jpoTop = new JTabbedPaneOperator((JTabbedPane) comp);

            int idx = jpoTop.indexOfTab(ASSOCIATED_PROJECTS);

            jpoTop.selectPage(idx);
        }
        //
        String tableTabbName = this.getProjNameOfTable();
        int idxTable = jpo.indexOfTab(tableTabbName);

        jpo.selectPage(idxTable);
    }

    /** nojdoc
     *
     * @param row
     * @param textExpected
     * @param packageColorExpected
     */
    public void packageCellExpected(int row, String textExpected, PackageColor packageColorExpected) {
        hasClosedCondition();
        this.selectTable();

        Color backGroundExpected;
        switch (packageColorExpected) {
            case NONE_AFFECTED_COLOR:
                backGroundExpected = Color.white;
                break;
            case AFFECTED_BY_COLOR:
                backGroundExpected = PfCellRndrPackageFormatCol.LITE_GRAY;
                break;
            case AFFECTING_COLOR:
                backGroundExpected = PfCellRndrPackageFormatCol.LITE_BLUE;
                break;
            default:
                throw new AssertionError(packageColorExpected.name());
        }
        int column = PfTableModel.PKGFLTR_PACKAGE;

        PfCellRndrPackageFormatCol cellRender = new PfCellRndrPackageFormatCol();

        JTable sourceTable = (JTable) this.pfTableOper.getSource();

        DefaultTableCellRenderer comp
                = (DefaultTableCellRenderer) cellRender.getTableCellRendererComponent(sourceTable,
                        getValueAt(row, column), false, false, row, column);

        Color bgColorActual = comp.getBackground();
        String textActual = comp.getText();

        assertEquals("Back-ground color fail", backGroundExpected, bgColorActual);
        assertEquals("Text of cell mismatch", textExpected, textActual);

        int a = 1;

    }

    /** nojdoc
     *
     * @param row
     * @param column
     *
     * @return */
    public Object getValueAt(int row, int column) {
        hasClosedCondition();

        return this.pfTable.getModel().getValueAt(row, column);
    }

    /** nojdoc
     *
     * @return */
    public JPopupMenuOperator expectedPopup() {
        hasClosedCondition();

        // there is only one active popup menu, so this picks it up
        // and stores it
        JPopupMenuOperator fp = new JPopupMenuOperator();

        this.popupMenu = fp;
        return fp;
    }

    /**
     * Check the menu items are wildcard items to the list of String[] and
     * String objects. If in error an {@link AssertionError} is thrown.
     * <p>
     * For an exact match enter just a String object, for a wildcard match
     * provide a String[] of portions for match.
     * <pre>
     * Popup-menu -> My test is matched exact with "My test" -> My test2 is
     * matched with WC "My" , "2"
     * <p>
     * checkMenuItemsWildCard( // PASS "My test", // exact match new
     * String[]{"My", "2"} // wildcard match (WC) );
     * <p>
     * checkMenuItemsWildCard( // FAIL "My test", // exact match new
     * String[]{"My", "2"} // wildcard match (WC) "Third line" // no Match found
     * );
     * </pre>
     *
     * @param objArr list of 'String' (exact match) or 'String[]' (match
     *               wild-card)
     *
     */
    public void checkMenuItemsOrder(MatcherMI... objArr) {
        ArrayList<MenuItemControl> menuItemsArr = menuStrings();

        if (menuItemsArr == null) {
            fail("ERROR: No menu items.");
            return;
        }

        if (objArr.length != menuItemsArr.size()) {
            fail("ERROR: Menu items expected different in sizes.");
            return;
        }
        // if only one object is it a String[] of one or a String
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            String menuStr2Check = menuItemsArr.get(i).menuString;

            boolean matchFound;

            if (obj instanceof WildcardMI) {
                // this will be a wild-card match check
                matchFound = matchWildCard(menuStr2Check, (WildcardMI) obj);
            } else {
                // this is an exact match check
                matchFound = menuStr2Check.equals(((ExactMI) obj).strExact);
            }
            // 
            if (!matchFound) {
                reportMismatch(i, menuItemsArr, objArr);
            }
        }
    }

    /** nojdoc */
    private void reportMismatch(int failOnI, ArrayList<MenuItemControl> menuItemsArr,
            MatcherMI... objArr) {
        // prepare to create an error message
        String errorMsg = "";

        for (int i = 0; i < menuItemsArr.size(); i++) {
            // line-by-line

            String expectStr = "";

            Object obj = objArr[i];
            if (obj instanceof ExactMI) {
                expectStr = ((ExactMI) obj).strExact;
            } else {
                String[] sArr = ((WildcardMI) obj).strArr;
                for (String s : sArr) {
                    expectStr = String.format("%s[%s] ", expectStr, s);
                }
            }
            String errI;
            if (failOnI == i) {
                errI = "ERR";
            } else {
                errI = "   ";
            }
            errorMsg = String.format("%s%s Expect: %s\n", errorMsg, errI, expectStr);
            errorMsg = String.format("%s%s Actual: %s\n", errorMsg, errI, menuItemsArr.get(i).menuString);

        }
        // throw an assertion error with a more useful error message
        throw new AssertionError(errorMsg);
    }

    /** nojdoc
     *
     * @param occurrence
     * @param menuString */
    public void selectMenuItemPause(int occurrence, MatcherMI menuString) {
        selectMenuItemExactOrWCWithPause(occurrence, menuString);
    }

    /** nojdoc
     *
     * @param menuString */
    public void selectMenuItemPause(MatcherMI menuString) {
        selectMenuItemExactOrWCWithPause(1, menuString);
    }

    /** nojdoc
     *
     * @return */
    public ArrayList<String> getLastPopupMenuList() {

        MenuElement[] menuArr = this.popupMenu.getSubElements();
        ArrayList<String> menuList = new ArrayList<>();

        for (MenuElement menuI : menuArr) {
            if (menuI instanceof JMenuItem) {
                String menuText = ((JMenuItem) menuI).getText();
                menuList.add(menuText);
            }
        }
        return menuList;
    }

    /** nojdoc */
    private void selectMenuItemExactOrWCWithPause(int occurrence, MatcherMI matcherMI) {
        // wildcard 
        hasClosedCondition();

        ArrayList<MenuItemControl> menuItemArr = this.menuStrings();

        MenuItemControl menuItemMatch = null;
        int occurrenceCount = 0;

        for (int i = 0; i < menuItemArr.size(); i++) {
            String menuString = menuItemArr.get(i).menuString;

            boolean matchCheckBool;
            if (matcherMI instanceof ExactMI) {
                matchCheckBool = menuString.equals(((ExactMI) matcherMI).strExact);
            } else {
                matchCheckBool = matchWildCard(menuString, (WildcardMI) matcherMI);
            }
            // on a match, is it the correct one
            if (matchCheckBool) {
                // there is the potential for duplicates so match the occurence only
                occurrenceCount++;

                if (occurrenceCount == occurrence) {
                    menuItemMatch = menuItemArr.get(i);

                    if (occurrence == 1) {
                        // get out of loop as found the first occurrence
                        break;
                    }
                }
            }
        }
        // if not found perform an assertionError
        if (menuItemMatch == null) {
            fail("No menu item found");
            return;
        }
        //
        menuItemMatch.menuItemOperator.push();

        pauseMs(500);
    }

    /** nojdoc */
    private ArrayList<MenuItemControl> menuStrings() {
        // the popup menu on record must still be showing
        if (!this.popupMenu.isShowing()) {
            fail("Popup menu is not showing.");
        }

        ArrayList<MenuItemControl> menuStringArr = new ArrayList<>();

        MenuElement[] menuElementArr = this.popupMenu.getSubElements();
        for (MenuElement menuEle : menuElementArr) {
            if (menuEle instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) menuEle;

                menuStringArr.add(
                        new MenuItemControl(
                                mi.getText(),
                                new JMenuItemOperator(mi)
                        ));
            }
        }
        if (menuStringArr.isEmpty()) {
            return null;
        }
        return menuStringArr;
    }

    /** nojdoc */
    private boolean matchWildCard(String mainString, WildcardMI wcMI) {

        String stringTest = mainString;
        int fromPosition = 0;
        int index;

        for (String s : wcMI.strArr) {
            try {
                index = stringTest.indexOf(s, fromPosition);

            } catch (ArrayIndexOutOfBoundsException e) {
                // this is an error for the from position
                return false;
            }

            if (index == -1) {
                return false;
            }
            // otherwise it is true found, so the next one has to show up after
            // the current found item
            // ..........111111111122222222
            // 0123456789012345678901234567
            //'abc def ghi jkl
            //def  *
            // ....4..7  s.length=3  4+3=7
            //
            fromPosition = index + s.length();
        }
        return true;
    }

    /** nojdoc
     *
     * @param exactMatchString
     *
     * @return */
    public MatcherMI miExact(String exactMatchString) {
        return this.new ExactMI(exactMatchString);
    }

    /** nojdoc
     *
     * @param partStringArr
     *
     * @return */
    public MatcherMI miWildcard(String... partStringArr) {
        return this.new WildcardMI(partStringArr);

    }

    /** nojdoc */
    protected void projectPropertiesCloser() {
        pfTableOper = null;
        pfTable = null;
        model = null;
        popupMenu = null;
    }

    /** nojdoc */
    public class MatcherMI {

        MatcherMI() {

        }
    }

    private class WildcardMI extends MatcherMI {

        String[] strArr;

        WildcardMI(String... strArrP) {
            super();
            this.strArr = strArrP;
        }
    }

    private class ExactMI extends MatcherMI {

        String strExact;

        ExactMI(String strExact) {
            super();
            this.strExact = strExact;
        }
    }
}

class MenuItemControl {

    String menuString;
    JMenuItemOperator menuItemOperator;

    MenuItemControl(String menuString, JMenuItemOperator menuItemOperator) {
        this.menuString = menuString;
        this.menuItemOperator = menuItemOperator;
    }
}
