/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.io.File;
import java.util.ArrayList;
import junit.framework.Test;
import packg.appfunc.FuncAssociateProjectsTabb;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.JsonValuesOtd;
import packg.zoperation.tstenv.PrepareProject;
import packg.appfunc.otdextensions.TableOtd;
import packg.appfunc.otdextensions.TablePopupOtd;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;
import static packg.appfunc.FuncPkgFilterTable.PackageColor.NONE_AFFECTED_COLOR;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = SingleSE.class)
public class AssociateSE extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private static final String JavaTestModelWithDependents_7 = "JavaTestModelWithDependents_7";
    private static final String JavaTestModelWithDependents_6 = "JavaTestModelWithDependents_6";
    private static final String JavaTestModelWithDependents_5 = "JavaTestModelWithDependents_5";
    private static final String JavaTestModelWithDependents_4 = "JavaTestModelWithDependents_4";
    private static final String JavaTestModelWithDependents_3 = "JavaTestModelWithDependents_3";
    private static final String JavaTestModelWithDependents_2 = "JavaTestModelWithDependents_2";
    private static final String JavaTestModelWithDependents_1 = "JavaTestModelWithDependents_1";
    private static final String JavaTestModelWithDependents = "JavaTestModelWithDependents";

    // - - - -  objects created and for use by following sequential testcases
    private static PrepareProject prepareProject;

    private static FuncProjectProperties fcPP;
    private static FuncAssociateProjectsTabb fcAP;

    private static File rootModel;

    private static final ArrayList<File> createdTempFileArr = new ArrayList<>();

    private static File testFile;

    private static String nbProjectDir;

    private static String testModelsDir;

    private static TableOtd packageFilterTableOtd;
    private static JsonValuesOtd jsonOtd;
    private static TablePopupOtd popupOtd;

    // --------------------
    /** nojdoc
     *
     * @param testName */
    public AssociateSE(String testName) {
        super(testName);
    }

    static ArrayList<File> modelArr;

    /** nojdoc */
    @JacocoTestMethod
    public void prepareLaunchIde() {

        prepareProject = new PrepareProject();

        // jelly-test-case, root-model, associated projects
        fcPP = prepareProject.openProjects(this, JavaTestModelWithDependents,
                JavaTestModelWithDependents_7,
                JavaTestModelWithDependents_6,
                JavaTestModelWithDependents_5,
                JavaTestModelWithDependents_4,
                JavaTestModelWithDependents_3,
                JavaTestModelWithDependents_2,
                JavaTestModelWithDependents_1);

        if (packageFilterTableOtd == null) {
            String myClassName = getClass().getSimpleName();

            packageFilterTableOtd = new TableOtd(fcPP,
                    _OtddataClass.sourceDir(), _OtddataClass.packageString(),
                    myClassName + "TD");

            jsonOtd = new JsonValuesOtd(fcPP.getJsonFilePath(),
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "JsonTD");

            popupOtd = new TablePopupOtd(
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "PopupTD"
            );

        }
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void changeToProjectSpecific() {
        // change to project specific
        fcPP.radioProjectSpecific().clickMouse();
        pauseMs(1000);

        // buttons are changed
        assertEquals(false, fcPP.radioGlobal().isSelected());
        assertEquals(true, fcPP.radioProjectSpecific().isSelected());

        JsonValuesOtd JsonAll = jsonOtd.getAllTypes(0, "associateInitial");
        JsonAll.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "changeToProjectSpecific")
    public void associatedProjectEnabled() {
        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        packageFilterTableOtd.dumpAll(0, "associateAllData");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "associatedProjectEnabled")
    public void processAssociatedProjects() {
        fcAP = fcPP.funcAssociateProjects();
        fcPP.tabbPaneOperator().selectPage("Associated projects");
        pauseMs(1000);

        fcAP.innerTabbPaneOperator().selectPage(JavaTestModelWithDependents_2);

        FuncPkgFilterTable fcPfTable = fcAP.getFuncPfTable(JavaTestModelWithDependents_2);
        fcPfTable.packageCellExpected(0, "*", NONE_AFFECTED_COLOR);

        JsonValuesOtd JsonAll = jsonOtd.getAllTypes(1, "assocproj");
        JsonAll.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "processAssociatedProjects")
    public void saveOffTheProject() {
        fcPP.buttonOK();

        JsonValuesOtd JsonAll = jsonOtd.getAllTypes(2, "saveOffTheProject");
        JsonAll.verify();

    }

//    @JacocoTestMethod(dependsOn = "processAssociatedProjects")
//    public void verifyBasicPopup() {
//
//        FuncPkgFilterTable fcPfTable = fcAP.getFuncPfTable(JavaTestModelWithDependents_2);
//
//        // select a cell that results in a popup
//        fcPfTable.pressMouse(0, PfTableModel.PKGFLTR_PACKAGE, MouseButton.SECONDARY);
//
//        TablePopupOtd popupOtdfirst = popupOtd.getSingle(fcPfTable, 0, "first");
//        popupOtdfirst.verify(fcPfTable);
//    }
//
//    @JacocoTestMethod(dependsOn = "verifyBasicPopup")
//    public void clickPopupAndTransferToRoot() {
//        FuncPkgFilterTable fcPfTable = fcAP.getFuncPfTable(JavaTestModelWithDependents_2);
//
//        String projName2 = fcPfTable.getProjNameOfTable();
//        assertEquals(JavaTestModelWithDependents_2, projName2);
//
//        MatcherMI mi = fcPfTable.miWildcard("'*'");
//        fcPfTable.selectMenuItemPause(mi);
//
//        FuncPkgFilterTable fcPfTableActiveNow = fcPP.getActiveTable();
//        String tableRootProjName = fcPfTableActiveNow.getProjNameOfTable();
//
//        assertEquals(JavaTestModelWithDependents, tableRootProjName);
//
//        TableOtd currTableOtd
//                = packageFilterTableOtd.getSingle(0, "clickPopup", JavaTestModelWithDependents);
//        currTableOtd.verify();
//
//        fcPfTableActiveNow.pressMouse(0, PfTableModel.PKGFLTR_PACKAGE, MouseButton.SECONDARY);
//
//        TablePopupOtd tablePopupRoot = popupOtd.getSingle(fcPfTableActiveNow,
//                1, JavaTestModelWithDependents);
//        
//        tablePopupRoot.verify(fcPfTableActiveNow);
//    }
    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }
}
