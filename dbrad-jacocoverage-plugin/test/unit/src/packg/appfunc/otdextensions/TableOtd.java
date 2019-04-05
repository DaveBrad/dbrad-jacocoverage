/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import dbrad.jacocofpm.mgr.com.PackageAffectingState;
import java.util.ArrayList;
import static junit.framework.TestCase.assertEquals;
import org.jtestdb.objecttestdata.ObjectTestData;
import org.jtestdb.objecttestdata.OtdAut;
import org.jtestdb.objecttestdata.OtdCollection;
import org.jtestdb.objecttestdata.OtdElementGroup;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;

/**
 * Class of object-test-data that stores the data of the all package-filter
 * tables at any given verification-point. This covers the principle-projects
 * table and any associated-tables of the project.
 * <p>
 * <p>
 * For the objecttestdata_19_1.jar to be used it needs to be in the class path and
 * this is done via the following:
 * <pre>
 * nbproject/project.properties add lines
 *
 *    test.qa-functional.cp.extra=./test/aids/otdjar/objecttestdata_19_1.jar
 *    test.qa-functional.runtime.cp.extra=./test/aids/otdjar/objecttestdata_19_1.jar
 * </pre>
 *
 * @author dbradley
 */
public class TableOtd extends OtdCollection {

    /**
     * Create an OTD object that captures the contents of a package-filter table
     * and its cells.
     */
    public TableOtd() {
        super();
    }

    /**
     * Create a test-data class file/object for processing the PfJTable data of
     * the dbrad-coverage GUI representation of data.
     *
     * @param funcProjectProperties the object that is used for collecting data
     *                              from
     * @param sourceDirectory       the source-directory for the generated-code
     *                              and test-data class to be stored
     * @param packageName           the package name for the class
     * @param testDataClassName     the name of the Class for this test-data
     *
     */
    public TableOtd(FuncProjectProperties funcProjectProperties,
            String sourceDirectory, String packageName, String testDataClassName) {
        //
        super(funcProjectProperties, sourceDirectory, packageName, testDataClassName);
    }

    /**
     * Add a package-filter table row of data to a data-object where parameters
     * represent data settings of the row.
     *
     * @param rowNoAndOrder        the row number in the table
     * @param rootOrAssociateTable table is a root (true) or associate table
     *                             (false)
     * @param onOff                the source-code folder is on for package
     *                             processing
     * @param cover                the coverage state
     * @param sourceType           source-code (false) or test-source-code
     *                             (true)
     * @param codeFolderName       the source-code folder name
     * @param packageFilter        string of the package-filter strings to apply
     * @param affectState          the affected state of the package-filter
     *
     * @return the order in which the data is added to a data-object
     */
    final public int add(int rowNoAndOrder,
            boolean rootOrAssociateTable,
            boolean onOff,
            FilePackageCoverStateEnum cover,
            Boolean sourceType,
            String codeFolderName,
            String packageFilter,
            PackageAffectingState affectState
    ) {
        // the row order, and the version-string (which is the project-name of the table being represented)
        int order = add1Super(rowNoAndOrder, rootOrAssociateTable,
                onOff, cover, sourceType, codeFolderName, packageFilter, affectState);

        return order;
    }

    /**
     *
     * @param orderLast
     * @param includesPackageAggregate
     *
     * @return
     */
    final public int add(int orderLast, String includesPackageAggregate) {
        int order = add1Super(orderLast, includesPackageAggregate);
        return order;
    }

    /**
     * Dump all the PfJTable tables (root and associated) information for
     * processing.
     *
     * @param version   version number of the data
     * @param testLabel string of the test the data is associated with
     */
    final public void dumpAll(int version, String testLabel) {
        // not with-types 

        super.dumpCollection1Super(newAutObjOfCollection(myCollectionObject()),
                version, testLabel);
    }

    /**
     *
     * @param versionNumber
     * @param testLabel
     */
    public void verifyAll(int versionNumber, String testLabel) {
        // collection type
        TableFixT pp = new TableFixT(this);

        super.verifyAll1Super(versionNumber, testLabel, pp, true);
    }

    /**
     * Get a single PfJTable's data represented by version, testAssociation and
     * Netbeans project-name of the PfJTable parameters from the test data-base
     * data-object.
     *
     * @param version         version number of the data
     * @param testAssociation string of the test the data is associated with
     * @param nbProjectName   string of the Netbeans project name of the
     *                        PfJTable being processed
     *
     * @return
     */
    final public TableOtd getSingle(int version, String testAssociation, String nbProjectName) {
        // the data-source
        FuncPkgFilterTable funcPkgFilterTable = myCollectionObject().getPfJTableForProjectName(nbProjectName);

        return (TableOtd) getSingle1Super(newAutObj(funcPkgFilterTable), version,
                testAssociation, nbProjectName);
    }

    /**
     *
     * @param objInAut
     * @param keyStr
     *
     * @return
     */
    @Override
    protected OtdAut.MapKey get2CodePrepareObjId(OtdAut.ObjOfSingle objInAut, String keyStr) {
        //99 need to document this interface correctly
        Object obj = objInAut.objInAut;

        FuncPkgFilterTable fcPft = (FuncPkgFilterTable) objInAut.objInAut;
        return OtdAut.mapKey.nu(fcPft, keyStr, newAutObj(fcPft));

    }

    /**
     *
     * @param objInAutOfCollection
     *
     * @return
     */
    @Override
    protected OtdAut.MapKey[] get2CodePrepareObjIdsOfCollection(OtdAut.ObjOfCollection objInAutOfCollection) {
        // the collection of objects will be the PfJTable objects for the main
        // and associate projects

        // ooc is the project-properties of the NBproject which holds the tables
        FuncProjectProperties fcProjProp = (FuncProjectProperties) objInAutOfCollection.objInAut4Collection;

        ArrayList<FuncPkgFilterTable> pfJTableList = fcProjProp.getAllPfJTablesOperatorsFromProjProp();

        OtdAut.MapKey[] mapKeyArr = new OtdAut.MapKey[pfJTableList.size()];

        for (int i = 0; i < pfJTableList.size(); i++) {
            FuncPkgFilterTable funcPfJTable = pfJTableList.get(i);

            //99  need to document the newAutObj approach, as it is not clear in the document
            //99 newAutObj is missing in documentation
            String projName = funcPfJTable.getProjNameOfTable();

            mapKeyArr[i] = OtdAut.mapKey.nu(funcPfJTable, projName, newAutObj(funcPfJTable));
        }
        return mapKeyArr;
    }

    /**
     *
     * @param atdiBeingCoded
     * @param objInAUT
     */
    @Override
    protected void add2CodeStatements4Collection(ObjectTestData atdiBeingCoded, OtdAut.ObjOfSingle objInAUT) {
        FuncPkgFilterTable funcPfJTable = (FuncPkgFilterTable) objInAUT.objInAut;

        int rowCount = funcPfJTable.getSource().getRowCount();

        for (int row = 0; row < rowCount; row++) {
            // get the data for the table and row from the fixture (as a common collect of the
            // data)
            OtdElementGroup tdeg = TableFixT.getTdegOfPkgFilterTableRow(funcPfJTable, row);

            // each column of the table is to be acquired
            Boolean rootOrAssociateTable = (Boolean) tdeg.getElementN(0);
            Boolean onOff = (Boolean) tdeg.getElementN(1);
            FilePackageCoverStateEnum coverageState = (FilePackageCoverStateEnum) tdeg.getElementN(2);

            Boolean isTest = (Boolean) tdeg.getElementN(3);

            String srcCodeLabel = (String) tdeg.getElementN(4);
            String packageStr = (String) tdeg.getElementN(5);

            // the color of the package-column indicates a state (affected/affecting) that
            // is an active piece of data
            PackageAffectingState affectState = (PackageAffectingState) tdeg.getElementN(6);

            // generate the code for this data item
            add3CodeGenSuper(atdiBeingCoded, row, rootOrAssociateTable,
                    onOff, coverageState, isTest,
                    srcCodeLabel, packageStr, affectState);
        }
        // get the includes package info
        String includesString = funcPfJTable.getIncludesTextArea();

        // all the row plus 1 line for the includes string
        add3CodeGenSuper(atdiBeingCoded, rowCount, includesString);
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

        if (tdeg.getElementN(0) instanceof String) {
            return processTDEGIncludesTextArea(action, expectedIndex, tdeg);
        }
        // most formats are a table row lines
        return processTDEGTableRow(action, expectedIndex, tdeg);
    }

    /**
     *
     * @param action
     * @param expectedIndex
     * @param tdeg
     *
     * @return
     */
    private Object processTDEGTableRow(OtdElementGroup.ProcessAction action, int expectedIndex, OtdElementGroup tdeg) {
        // get the data into local variables for the purpose of processing
        // following
        Boolean rootTable = (Boolean) tdeg.getElementN(0);
        Boolean onOff = (Boolean) tdeg.getElementN(1);
        FilePackageCoverStateEnum coverageState = (FilePackageCoverStateEnum) tdeg.getElementN(2);
        Boolean isTest = (Boolean) tdeg.getElementN(3);
        String srcCodeLabel = (String) tdeg.getElementN(4);
        String packageStr = (String) tdeg.getElementN(5);
        PackageAffectingState affectState = (PackageAffectingState) tdeg.getElementN(6);

        Object result = null;

        switch (action) {
            case TOSTRING_TDEG:
                // this is a lot of data, so put on new lines
                String resltStr = String.format("%s %s %s %s %s %s %s",
                        rootTable, onOff, coverageState.toString(),
                        isTest,
                        srcCodeLabel,
                        packageStr, affectState.toString());

                result = resltStr;
                break;

            case VERIFY_TDEG:
                // the assumption is that the user provides a valid 
                // and expected position into the table, if this does 
                // not fit then we assume an application error. OR, this 
                // is a calling test code error and needs to be fixed
                Object[] thisObj = this.getTdegDataValuesFromArr(expectedIndex);

                assertEquals(thisObj[0], rootTable);
                assertEquals(thisObj[1], onOff);
                assertEquals(thisObj[2], coverageState);
                assertEquals(thisObj[3], isTest);
                assertEquals(thisObj[4], srcCodeLabel);
                assertEquals(thisObj[5], packageStr);
                assertEquals(thisObj[6], affectState);

                break;
            default:
                throw new AssertionError(action.name());
        }
        return result;
    }

    /**
     *
     * @param action
     * @param expectedIndex
     * @param tdeg
     *
     * @return
     */
    private Object processTDEGIncludesTextArea(OtdElementGroup.ProcessAction action, int expectedIndex, OtdElementGroup tdeg) {
// get the data into local variables for the purpose of processing
        // following
        String includesTextArea = (String) tdeg.getElementN(0);

        Object result = null;

        switch (action) {
            case TOSTRING_TDEG:
                // 
                result = String.format("%s", includesTextArea);
                break;

            case VERIFY_TDEG:
                // the assumption is that the user provides a valid 
                // and expected position into the table, if this does 
                // not fit then we assume an application error. OR, this 
                // is a calling test code error and needs to be fixed
                Object[] thisObj = this.getTdegDataValuesFromArr(expectedIndex);

                assertEquals(thisObj[0], includesTextArea);
                break;
            default:
                throw new AssertionError(action.name());
        }
        return result;
    }

    /**
     *
     * @param contextObj
     * @param version
     * @param testLabel
     * @param keyStr
     *
     * @return
     */
    @Override
    protected ObjectTestData getSingleHideProtected(OtdAut.ObjOfSingle contextObj, int version, String testLabel, String keyStr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * nojdoc
     */
    public void verify() {
        // single type
        FuncPkgFilterTable funcPkgFilterTable = (FuncPkgFilterTable) this.getSingleOtdAutData().getSingleObject();
        super.verify1Super(new TableFixT("", funcPkgFilterTable), true);
    }

    /**
     *
     * @return
     */
    @Override
    protected FuncProjectProperties myCollectionObject() {
        return (FuncProjectProperties) this.myCollectionObj;
    }
}
