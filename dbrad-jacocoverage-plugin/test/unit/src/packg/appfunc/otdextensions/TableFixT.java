/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import dbrad.jacocofpm.mgr.com.PackageAffectingState;
import dbrad.jacocoverage.plugin.config.pf.PfTableModel;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_FILE_HIDDEN;
import java.util.EnumSet;
import org.jtestdb.objecttestdata.OtdAut;
import org.jtestdb.objecttestdata.OtdElementGroup;
import org.jtestdb.objecttestdata.OtdFixtureClass;
import org.jtestdb.objecttestdata.OtdSingle;
import packg.appfunc.FuncPkgFilterTable;

/**
 * Class that is the OTD fixture for a package-filter table (principle-project
 * tab or associated-project tabs).
 * <p>
 * Extracts the data from the application-under-test for package-filter tables.
 *
 * @author dbradley
 */
public class TableFixT extends OtdFixtureClass {

    /* for single */
    private FuncPkgFilterTable funcPkgFilterTable;

    /**
     *
     * @param keyStr
     * @param funcPkgFilterTable
     */
    public TableFixT(String keyStr, FuncPkgFilterTable funcPkgFilterTable) {
        super(OtdSingle.class, keyStr);
        this.funcPkgFilterTable = funcPkgFilterTable;
    }

    /**
     *
     * @param pkgFilterTableOtd
     */
    public TableFixT(TableOtd pkgFilterTableOtd) {
        super(TableOtd.class);
        OtdAut.MapKey[] collectionData = pkgFilterTableOtd.getCollectionOtdAutData();

        for (OtdAut.MapKey otdMapKey : collectionData) {
            String keyStrInt = otdMapKey.getKeyStr();
            // needs to be object used for collection and not the object singles are made from
            FuncPkgFilterTable fcPkgFilterTable = (FuncPkgFilterTable) otdMapKey.getSingleObject();

            TableFixT xxx = new TableFixT(keyStrInt, fcPkgFilterTable);

            appendOtdFixtureItem(xxx);
        }
    }

    /**
     *
     * @param os
     */
    @Override
    public void test(Object[] os) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param es
     */
    @Override
    final public void refresh(EnumSet<?> es) {
        // for a collection the enumset is not required.
        throw new UnsupportedOperationException("Not supported for OtdCollection [without types].");
    }

    /**
     * nojdoc
     */
    @Override
    final public void refresh() {
        // get data for the keystr provided, the keystr is a Package filter table project-name
        //
        int rowCount = this.funcPkgFilterTable.getSource().getRowCount();

        for (int row = 0; row < rowCount; row++) {
            OtdElementGroup tdeg = getTdegOfPkgFilterTableRow(this.funcPkgFilterTable, row);
            appendTdegItem(tdeg);
        }
        // append the includes-textarea for the aggregation of all tables
        // package strings
        String includeString = this.funcPkgFilterTable.getIncludesTextArea();

        OtdElementGroup tdeg = OtdElementGroup.newAdd(rowCount, includeString);
        appendTdegItem(tdeg);

    }

    /**
     *
     * @param funcPfJTable
     * @param row
     *
     * @return
     */
    public static OtdElementGroup getTdegOfPkgFilterTableRow(FuncPkgFilterTable funcPfJTable, int row) {
        // each column of the table is to be acquired
        Boolean rootTableBool = funcPfJTable.isRootTable();

        Boolean onOff = (Boolean) funcPfJTable.getValueAt(row, PfTableModel.PKGFLTR_ON_OFF);

        FilePackageCoverStateEnum coverageState
                = (FilePackageCoverStateEnum) funcPfJTable.getValueAt(row, PfTableModel.PKGFLTR_COVERAGE);

        Boolean isTestDir = (Boolean) funcPfJTable.getValueAt(row, PfTableModel.PKGFLTR_IS_TEST_SRC_DIR);

        String srcCodeLabel = (String) funcPfJTable.getValueAt(row, PfTableModel.PKGFLTR_SRC_CODE_LABEL);
        String packageStr = (String) funcPfJTable.getValueAt(row, PfTableModel.PKGFLTR_PACKAGE);

        // the color of the package-column indicates a state (affected/affecting) that
        // is an active piece of data
        FilePackageOfSrc fileSrc = (FilePackageOfSrc) funcPfJTable.getValueAt(row, PKGFLTR_FILE_HIDDEN);
        PackageAffectingState affectState = fileSrc.getAffectedBySibling();

        OtdElementGroup tdeg = OtdElementGroup.newAdd(row, rootTableBool,
                onOff, coverageState, isTestDir,
                srcCodeLabel, packageStr, affectState);

        return tdeg;
    }
}
