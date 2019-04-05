/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.util.EnumSet;
import junit.framework.Test;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;
import packg.zoperation.tstenv.PrepareProject;
import packg.appfunc.otdextensions.TableOtd;
import packg.appfunc.otdextensions.JsonValuesOtd;
import packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = GlobalPref.class)
public class SingleSE extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private static final String testModelName = "JavaLibraryTestModel1";

    private static PrepareProject prepareProject;

    private static FuncProjectProperties fcPP;

    private static TableOtd packageFilterTableOtd;
    private static TableOtd packageFilterTableOtd2;

    private static JsonValuesOtd jsonOtd;

    private static FuncPkgFilterTable fRootTable;

    /**
     *
     * @param testName
     */
    public SingleSE(String testName) {
        super(testName);
    }

    /** Test case 1. */
    @JacocoTestMethod
    public void prepareLaunchIde() {
        prepareProject = new PrepareProject();
    
        fcPP = prepareProject.openProjects(this, testModelName);

        if (packageFilterTableOtd == null) {
            String myClassName = getClass().getSimpleName();

            packageFilterTableOtd = new TableOtd(fcPP,
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "TD");

            jsonOtd = new JsonValuesOtd(fcPP.getJsonFilePath(),
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "JsonTD");
        }
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void checkRadios() {
        assertEquals(true, fcPP.radioGlobal().isSelected());
        assertEquals(false, fcPP.radioProjectSpecific().isSelected());

        JsonValuesOtd tst = jsonOtd.getSingle(3, "initial", EnumSet.of(JsonDataTypes.JSON_PREFERENCES));
        tst.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "checkRadios")
    public void changeToProjectSpecific() {
        // change to project specific
        fcPP.radioProjectSpecific().clickMouse();
        pauseMs(1000);

        JsonValuesOtd globalTst = jsonOtd.getSingle(0, "globalSetting", EnumSet.of(JsonDataTypes.JSON_PREFERENCES));
        globalTst.verify();

        // buttons are changed
        assertEquals(false, fcPP.radioGlobal().isSelected());
        assertEquals(true, fcPP.radioProjectSpecific().isSelected());

        JsonValuesOtd projectTst = jsonOtd.getSingle(0, "projectSetting", EnumSet.of(JsonDataTypes.JSON_PREFERENCES));
        projectTst.verify();

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "changeToProjectSpecific")
    public void associateProjectDisabled() {
        fcPP.funcAssociateProjects().hasAssociateProjects(false);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "associateProjectDisabled")
    public void validatePkgFilterTable() {
        JsonValuesOtd tst = jsonOtd.getSingle(0, "initialproject", EnumSet.of(JsonDataTypes.JSON_PREFERENCES));
        tst.verify();

        JsonValuesOtd tst2 = jsonOtd.getAllTypes(0, "initalall");
        tst2.verify();

        TableOtd otdObj = packageFilterTableOtd.getSingle(0, "validatePkgFilterTable", "JavaLibraryTestModel1");
        otdObj.verify();

        packageFilterTableOtd.dumpAll(1, "validatePkgFilterTable");
        packageFilterTableOtd.verifyAll(1, "validatePkgFilterTable");

        fcPP.buttonOK();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "validatePkgFilterTable")
    public void checkPersistence() {
        fcPP = prepareProject.nuFuncProjectProperties();
        fcPP.funcRootTable();

        // there will be a new project-properties dialog launched, but it should
        // contain the same data; HOWEVER, the 'packageFilterTableOtd' will also
        // change, but the test-data from the previous test-data should be the same
        // SO reuse the coded data is what is happening here
        packageFilterTableOtd2 = new TableOtd(fcPP,
                _OtddataClass.sourceDir(),
                _OtddataClass.packageString(),
                getClass().getSimpleName() + "TD2");

        // reuse the version and test-label data as it should persist, but
        // must dumpAll so as to set the collection data object identifiers
        // (OTD will reuse the code regardless)
        packageFilterTableOtd2.dumpAll(1, "validatePkgFilterTable");
        packageFilterTableOtd2.verifyAll(1, "validatePkgFilterTable");

        JsonValuesOtd tst = jsonOtd.getSingle(0, "initialpersist", EnumSet.of(JsonDataTypes.JSON_PREFERENCES));
        tst.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "checkPersistence")
    public void testCoverCascade() {
        fRootTable = fcPP.funcRootTable();

        fRootTable.clickCoverFor("Source Packages", "*");
        packageFilterTableOtd2.dumpAll(2, "testCoverCascade");
        packageFilterTableOtd2.verifyAll(2, "testCoverCascade");

        //
        fRootTable.clickCoverFor("Source Packages", "org");
        packageFilterTableOtd2.dumpAll(3, "testCoverCascade");
        packageFilterTableOtd2.verifyAll(3, "testCoverCascade");

        //
        fRootTable.clickCoverFor("Source Packages", "org.jlib");
        packageFilterTableOtd2.dumpAll(4, "testCoverageCascade");
        packageFilterTableOtd2.verifyAll(4, "testCoverageCascade");

        //
        fRootTable.clickCoverFor("Source Packages", "org.jlib.model1");
        packageFilterTableOtd2.dumpAll(5, "testCoverCascade");
        packageFilterTableOtd2.verifyAll(5, "testCoverCascade");

        //
        fRootTable.clickCoverFor("Source Packages", "org.jlib.pkga");
        packageFilterTableOtd2.dumpAll(6, "testCoverCascade");
        packageFilterTableOtd2.verifyAll(6, "testCoverCascade");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testCoverCascade")
    public void testCoverRevert() {
        fRootTable = fcPP.funcRootTable();

        fRootTable.clickCoverFor("Source Packages", "*");
        packageFilterTableOtd2.verifyAll(1, "validatePkgFilterTable");

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testCoverRevert")
    public void testCoverExplicit() {
        //
        fRootTable.clickCoverFor("Source Packages", "org.jlib.model1");
        packageFilterTableOtd2.verifyAll(4, "testCoverageCascade");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testCoverExplicit")
    public void testOffExplicit() {
        //
        fRootTable.clickOnOffFor("Source Packages");
        packageFilterTableOtd2.dumpAll(10, "onoff_to_off");
        packageFilterTableOtd2.verifyAll(10, "onoff_to_off");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testOffExplicit")
    public void testOffToOnExplicit() {
        //
        fRootTable.clickOnOffFor("Source Packages");

        // should revert to this data
        packageFilterTableOtd2.verifyAll(4, "testCoverageCascade");
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }
}
