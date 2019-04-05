/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.AffectedByPopup;
import dbrad.jacocofpm.mgr.com.AffectingPopup;
import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_A_SIBLING;
import dbrad.jacocoverage.plugin.GlobalsConfig;
import dbrad.jacocofpm.json.Json2ProjSrcCodeFolder;
import dbrad.jacocofpm.json.Json3PackageFormat;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_COVERAGE;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_FILE_HIDDEN;
import dbrad.jacocofpm.json.Json4Data;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.netbeans.api.project.Project;

/**
 * Table model for the packages filter UI.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class PfTableModel extends DefaultTableModel implements MouseListener {

    private static final long serialVersionUID = 1L;

    // - - - - - -     column information structure    - - - - - - -
    //
    public final static int PKGFLTR_ON_OFF;
    public final static int PKGFLTR_COVERAGE;
    public final static int PKGFLTR_ICON;
    public final static int PKGFLTR_IS_TEST_SRC_DIR;
    public final static int PKGFLTR_SRC_CODE_LABEL;
    public final static int PKGFLTR_PACKAGE;
    public final static int PKGFLTR_FILE_HIDDEN;

    /* Table columns heading strings, class-type of, and can-edit settings. */
    private static String[] headingsArr;
    private static Class<?>[] typesArr;
    private static boolean[] canEditArr;

    private static boolean[] columnCellFocusableArr;

    /* column table datasettings */
    private static TableColumnSettings[] columnTableArr;

    static {
        // the use of addColumnAssociations makes adding columns easier and
        // ensures data items match up (better maintanence of code for
        // modification)
        //
        // *******   IF ADDING HERE ALSO CHANGE THE : addRow : METHOD BELOW
        //
        // heading -- string of columns heading
        // clzType -- class-type of the column
        // canEdit -- true if column can be edited
        // cellFocusable -- true if cell focus control allowed
        // tabColumnSettings -- the settings for the column applied

        PKGFLTR_ON_OFF = addColumnAssociations("On", Boolean.class,
                true, // canEdit
                true, // cellFocusable
                new TableColumnSettings(30, 0, 30, false, false));

        PKGFLTR_COVERAGE = addColumnAssociations("Cover", FilePackageCoverStateEnum.class,
                true,// canEdit
                true, // cellFocusable
                new TableColumnSettings(48, 0, 48, false, false));

        PKGFLTR_IS_TEST_SRC_DIR = addColumnAssociations(" ", boolean.class,
                false, // canEdit
                false, // cellFocusable
                new TableColumnSettings(30, 0, 30, false, false));

        PKGFLTR_ICON = addColumnAssociations("", Icon.class,
                false, // canEdit
                false, // disableCellFocusForColumn
                new TableColumnSettings(20, 0, 20, false, false));

        PKGFLTR_SRC_CODE_LABEL = addColumnAssociations("Folder", String.class,
                false, // canEdit
                false, // cellFocusable
                new TableColumnSettings(65, 0, 200, true, false));

        PKGFLTR_PACKAGE = addColumnAssociations("Packages", String.class,
                false, // canEdit
                true, // cellFocusable
                new TableColumnSettings(-1, -1, -1, true, false));

        PKGFLTR_FILE_HIDDEN = addColumnAssociations("INTERNAL_FILE", FilePackageOfSrc.class,
                false, // canEdit
                false, // cellFocusable
                new TableColumnSettings(20, 0, 20, false, true));
    }

    /**
     * Add a row with the explicit column class-type matching (use this method
     * over the super-class to avoid class-type cast errors).
     *
     * @param stateEnum
     * @param srcDirType          boolean false is source (src) or true is
     *                            source-test (test)
     * @param srcDirName
     * @param packageFormattedStr
     * @param packageFileData
     */
    public void addRow(Boolean onOffState,
            FilePackageCoverStateEnum stateEnum,
            boolean isTestDirSource,
            String srcDirName,
            String packageFormattedStr,
            FilePackageOfSrc packageFileData) {

        // the stateEnum is the initial state of the 
        packageFileData.setCoverState(stateEnum);

        // the icon to apply depends on the file information
        ImageIcon displayIcon;
        if (packageFileData.isBaseFile()) {
            displayIcon = GlobalsConfig.ICON_NB_JAVA_FOLDER;
        } else {
            displayIcon = GlobalsConfig.ICON_NB_JAVA_PKG;
        }

        super.addRow(new Object[]{onOffState, packageFileData.coverStateEnum, isTestDirSource,
            displayIcon,
            srcDirName, packageFormattedStr, packageFileData});
    }
    //
    // - end - - -     column information structure    - - - end - - 

    private final PfJTable jTablePackageFilter;

    //99
    public PfTableModel(PfJTable myModelsTable) {
        super(new Object[][]{}, headingsArr);

        this.jTablePackageFilter = myModelsTable;
    }

    public Json2ProjSrcCodeFolder createJson2MapFromProjFilterTable() {
        Json2ProjSrcCodeFolder json2SrcCodeFolderHash = new Json2ProjSrcCodeFolder(4);

        int nbLines = this.getRowCount();

        for (int rowI = 0; rowI < nbLines; rowI++) {

            String srcCodeFolderName = (String) this.getValueAt(rowI, PKGFLTR_SRC_CODE_LABEL);

            if (!json2SrcCodeFolderHash.containsKey(srcCodeFolderName)) {
                // allocate the memory for the 2nd hash-map used for package-string
                json2SrcCodeFolderHash.put(srcCodeFolderName, new Json3PackageFormat(4));
            }
            Json3PackageFormat packageHash = json2SrcCodeFolderHash.get(srcCodeFolderName);

            // get the Json data fields that will tell everything about what is being processed
            FilePackageOfSrc fileData = (FilePackageOfSrc) this.getValueAt(rowI, PKGFLTR_FILE_HIDDEN);

            // the table does not contain the real data as the renderer does manipulation
            // as what is displayed
            //
            // the is-test-dir is none transitional data (that is it is static
            Boolean isTestDir = (Boolean) this.getValueAt(rowI, PKGFLTR_IS_TEST_SRC_DIR);

            // the following fields are actually stored in the file-data associated with
            // the row
            Boolean onOffStateB = (Boolean) fileData.isOnCodeFolderState();
            Boolean hasJavaFilesAndChildren = fileData.hasJavaFilesInPathAndSubPaths();
            FilePackageCoverStateEnum state = fileData.coverStateEnum;

            // JSON data object
            Json4Data json4DataToStore = new Json4Data(onOffStateB, isTestDir, hasJavaFilesAndChildren, state);

            // store the data away against the package-format
            String packageStringFormated = (String) this.getValueAt(rowI, PKGFLTR_PACKAGE);
            packageHash.put(packageStringFormated, json4DataToStore);
        }

        return json2SrcCodeFolderHash;
    }

    @Override
    public Object getValueAt(int row, int column) {
        // the table model data that is displayed is a reflection of the hidden data item in
        // the table for the columns (that the cell renderering does things  to display the
        // actual data
        //
        // attempting to keep the table cells and the actual data in sync is too complex
        // so this is simpler and easier to maintain
        FilePackageOfSrc rowFileData = (FilePackageOfSrc) super.getValueAt(row, PKGFLTR_FILE_HIDDEN);

        // fill this in and return as object
        Object objToReturn = null;

        // unfortunately switch does not work for non-constant variables
        if (column == PKGFLTR_ON_OFF) {
            objToReturn = rowFileData.isOnCodeFolderState();

        } else if (column == PKGFLTR_COVERAGE) {
            objToReturn = rowFileData.coverStateEnum;

        } else if (column == PKGFLTR_IS_TEST_SRC_DIR) {
            objToReturn = rowFileData.isTestSrcCode();

        } else if (column == PKGFLTR_ICON) {
            objToReturn = super.getValueAt(row, PKGFLTR_ICON);

        } else if (column == PKGFLTR_SRC_CODE_LABEL) {
            objToReturn = rowFileData.getSrcCodeDirBase().getSrcFolderDirDisplayName();

        } else if (column == PKGFLTR_PACKAGE) {
            objToReturn = rowFileData.getPackageFormat();

        } else if (column == PKGFLTR_FILE_HIDDEN) {
            objToReturn = rowFileData;
        }
        return objToReturn;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return typesArr[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEditArr[columnIndex];
    }

    /**
     * Is the cell focusable within the table via the table cell-focus change
     * mechanism.
     * <p>
     * The ON/OFF, COVER and Packages columns are allowed to cell-focus.
     * However, a row that is not the root package is not focusable.
     *
     * @param rowIndex    row
     * @param columnIndex column
     *
     * @return true if cell is focusable
     */
    public boolean isCellFocusable(int rowIndex, int columnIndex) {
        if (columnIndex == PKGFLTR_ON_OFF) {
            // is the row we are on a root package as defined by the
            // packages string '*'.

            FilePackageOfSrc packageSrcFile
                    = (FilePackageOfSrc) this.getValueAt(rowIndex, PKGFLTR_FILE_HIDDEN);

            // for a none basefile display an empty cell
            if (!packageSrcFile.isBaseFile()) {
                return false;
            }
        }
        return columnCellFocusableArr[columnIndex];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing    
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // popup a menu if affected-by-sibling or affecting-sibling conditions/states apply

        // needs to be right-mouse button
        if (e.getButton() != MouseEvent.BUTTON3) {
            return;
        }
        Point ePoint = e.getPoint();

        JTable eTable = (JTable) e.getSource();

        // calculate the 
        popupEventProcess((JTable) e.getSource(),
                eTable.rowAtPoint(ePoint),
                eTable.columnAtPoint(ePoint), 
                ePoint);
    }

    /**
     * Popup a menu for the affecting or affected-by context menu to overlap the
     * cell. May be done via accessibility or mouse click.
     *
     * @param eTable the table that is in context
     * @param row    cell
     * @param column cell
     * @param ePoint the mouse point on the screen, null position in cell
     *
     */
    void popupEventProcess(JTable eTable, int row, int column, Point ePoint) {

        // now is it a valid table column and state for a popup
        if (column != PKGFLTR_PACKAGE) {
            return;
        }
        // get row for the mouse event
        int rowConv = eTable.convertRowIndexToModel(row);

        FilePackageOfSrc fileExtOfCell = (FilePackageOfSrc) this.getValueAt(rowConv, PKGFLTR_FILE_HIDDEN);

        // position calculation for the popup to present itself is different
        // for mouse (ePoint has value) and accessibility (no ePoint)
        int popupY;
        int popupX;

        if (ePoint != null) {
            // calculate relative to the mouse position
            popupY = ePoint.y + 5;
            popupX = ePoint.x + 10;

        } else {
            // calculate relative to the cell's location
            Rectangle cellRect = this.jTablePackageFilter.getCellRect(rowConv, column, true);
            popupY = 5 + cellRect.y;
            popupX = 100 + cellRect.x;
        }
        // show any applicable popup
        switch (fileExtOfCell.getAffectedBySibling()) {
            case NOT_AFFECTED:
            case AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES:
                break;

            case AFFECTED_BY_A_SIBLING:
                // create the popup 
                AffectedByPopup affectedByPopup = new AffectedByPopup(fileExtOfCell);

                affectedByPopup.show(eTable, popupX, popupY);
                break;

            case AFFECTING_OTHERS:
                // create the popup 
                AffectingPopup affectingPopup = new AffectingPopup(fileExtOfCell);

                affectingPopup.show(eTable.getParent(), popupX, popupY);
                break;

            default:
                throw new AssertionError(fileExtOfCell.getAffectedBySibling().name());
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void mouseReleased(MouseEvent e) {

        JTable eTable = (JTable) e.getSource();

        // get row and column for the mouse event
        Point ePoint = e.getPoint();
        int row = eTable.rowAtPoint(ePoint);
        int column = eTable.columnAtPoint(ePoint);

        selectedRowColumn(eTable, row, column);
    }

    //99
    public void selectedRowColumn(JTable eTable, int row, int column) {
        int rowConv = eTable.convertRowIndexToModel(row);

        FilePackageOfSrc fileExtOfCell = (FilePackageOfSrc) this.getValueAt(rowConv, PKGFLTR_FILE_HIDDEN);

        if (column == PKGFLTR_COVERAGE) {
            // process the row coverage state and make the appropriate changes to
            // the state
            fileExtOfCell.togglePkgCoverageState();

        } else if (column == PKGFLTR_ON_OFF) {
            // only a base-file of a source-code-folder may be selected to cause a
            // change to the state
            if (!fileExtOfCell.isBaseFile()) {
                return;
            }
            // otherwise is the base cell so change the data
            fileExtOfCell.toggleOnCodeFolderState();

            // with the change in the state need to reapply the row-filtering
            // so row are displayed or not according to the filter
            TableRowSorter<PfTableModel> sorter = new TableRowSorter(this);
            sorter.setRowFilter(new PfRowFilter());

            jTablePackageFilter.setRowSorter(sorter);

        } else {
            return;
        }
        // all the data has been affected so repainting the table will help
        //
        // recalculate the merge settings
        PfJTableDatum datum = (PfJTableDatum) this.jTablePackageFilter.datum;

        // reset the Affect state for each row
        int rowCnt = this.jTablePackageFilter.getRowCount();

        for (int r = 0; r < rowCnt; r++) {
            FilePackageOfSrc filePkgOfSrc = (FilePackageOfSrc) this.getValueAt(r, PKGFLTR_FILE_HIDDEN);

            filePkgOfSrc.setNotAffected();
        }
        // recalculate the merge information and then repaint
        ArrayList<String> excludeList
                = NbUtilsPlugin.getExcludesPatternsAsArrayList(datum.nbProjectJacoco.getPrincipleNbProject());

        datum.nbProjectJacoco.mergeAllSourcesForDisplay(excludeList);

        this.jTablePackageFilter.javaSePanel.store();

        // set the includes display appropriately
        Project nbProjectL = datum.nbProjectJacoco.getPrincipleNbProject();

        String includePackagesString
                = NbUtilsPlugin.getProjectIncludesAsStr(nbProjectL, ":", ".");

        this.jTablePackageFilter.javaSePanel.jtextareaIncludeStringDisplay.setText(includePackagesString);

        datum.nbProjectJacoco.repaint4Project();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    /**
     * Add each column one by one into static generic array (not collections) so
     * as to improve CPU processing of the models data.
     * <p>
     * Provides better maintenance approach to coding these associations. Also,
     * an explicit class-typed for addRow helps as well.
     *
     * @param heading           string of columns heading
     * @param clzType           class-type of the column
     * @param canEdit           true if column can be edited
     * @param cellFocusable     true if focus to cell within JTable cell focus
     *                          control (This is not swing focus traversal.)
     * @param tabColumnSettings the settings for the column applied against the
     *                          table
     */
    static private int addColumnAssociations(String heading, Class<?> clzType, boolean canEdit,
            boolean cellFocusable,
            TableColumnSettings tabColumnSettings) {
        // allocate storage (structure) if nothing allocated 
        // ( depends on the group of arrays behavour being the same)
        //
        if (headingsArr == null) {
            headingsArr = new String[0];
            typesArr = new Class<?>[0];
            canEditArr = new boolean[0];
            columnCellFocusableArr = new boolean[0];

            columnTableArr = new TableColumnSettings[0];
        }
        // add to the table dynamically (yes collection lists could be used
        // but this information is used by cell renderering and a 
        // arr.[index] vs a arrList.get(index) is faster and less prone
        // 
        int indexOfColumn = headingsArr.length;
        int nuSizeOfColumns = indexOfColumn + 1;

        // begin adding the parameters to generic arrays (not collections)
        headingsArr = Arrays.copyOf(headingsArr, nuSizeOfColumns);
        headingsArr[indexOfColumn] = heading;

        typesArr = Arrays.copyOf(typesArr, nuSizeOfColumns);
        typesArr[indexOfColumn] = clzType;

        columnCellFocusableArr = Arrays.copyOf(columnCellFocusableArr, nuSizeOfColumns);
        columnCellFocusableArr[indexOfColumn] = cellFocusable;

        canEditArr = Arrays.copyOf(canEditArr, nuSizeOfColumns);
        canEditArr[indexOfColumn] = canEdit;

        // set the column index into the TableColumnSettings by creating another object
        // this will avoid the table from having mismatched indexes
        columnTableArr = Arrays.copyOf(columnTableArr, nuSizeOfColumns);
        columnTableArr[indexOfColumn]
                = new TableColumnSettings(indexOfColumn, tabColumnSettings);

        return indexOfColumn;
    }

    public void setColumnSettingsOfTable() {

        for (TableColumnSettings tcs : columnTableArr) {
            tcs.setColumnData(jTablePackageFilter);
        }
    }
}

//99 consider own class
class TableColumnSettings {

    int columnIndex;

    int preferredWidth;
    int minWidth;
    int maxWidth;

    boolean resizeAble;
    boolean hiddenColumn;

    TableColumnSettings(int preferredWidth, int minWidth, int maxWidth, boolean resizeAble, boolean hiddenColumn) {
        this.columnIndex = -1;

        this.preferredWidth = preferredWidth;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;

        this.resizeAble = resizeAble;
        this.hiddenColumn = hiddenColumn;
    }

    TableColumnSettings(int columnIndex, TableColumnSettings data) {
        this.columnIndex = columnIndex;

        this.preferredWidth = data.preferredWidth;
        this.minWidth = data.minWidth;
        this.maxWidth = data.maxWidth;

        this.resizeAble = data.resizeAble;
        this.hiddenColumn = data.hiddenColumn;
    }

    void setColumnData(JTable jTablePackageFilter) {
        TableColumn tabCol = jTablePackageFilter.getColumnModel().getColumn(columnIndex);

        if (this.hiddenColumn) {
            jTablePackageFilter.getColumnModel().removeColumn(tabCol);
            return;
        }

        if (this.preferredWidth != -1) {
            // the column is not to have any sizes thus ac
            tabCol.setPreferredWidth(this.preferredWidth);
            tabCol.setMinWidth(this.minWidth);
            tabCol.setMaxWidth(this.maxWidth);
        }
        tabCol.setResizable(this.resizeAble);
    }
}
