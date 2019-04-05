/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import junit.framework.Test;
import static junit.framework.TestCase.assertEquals;
import org.netbeans.jellytools.FilesTabOperator;
import org.netbeans.jellytools.actions.ActionNoBlock;
import org.netbeans.jellytools.nodes.JavaNode;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.appfunc.FuncPkgFilterTable;
import static packg.appfunc.FuncPkgFilterTable.PKG_STR;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.JsonValuesOtd;
import packg.appfunc.otdextensions.TableOtd;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.tstenv.PrepareProject;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = ExcludePackageErrors.class)
//@JacocoTestClass(dependsOnClass = PoorPackagesSE.class)
public class NewPackageInclusionSE extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private static final ArrayList<File> createdTempFileArr = new ArrayList<>();

    private static File testFile;

    private static String nbProjectDir;

    private static String testModelsDir;

    private static PrepareProject prepareProject;

    private static FuncProjectProperties fcPP;

    private static TableOtd packageFilterTableOtd;
    private static TableOtd packageFilterTableOtd2;

    private static JsonValuesOtd jsonOtd;

    private static FuncPkgFilterTable fRootTable;

    private static File copyOfTestModel1;
    private static final String testModelName = "JavaLibraryTestModel1";

    /**
     *
     * @param testName
     */
    public NewPackageInclusionSE(String testName) {
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

        JsonValuesOtd tst = jsonOtd.getSingle(0, "initial",
                EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES));
        tst.verify();

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "checkRadios")
    public void changeToProjectSpecific() {
        // change to project specific
        fcPP.radioProjectSpecific().clickMouse();
        pauseMs(1000);

        JsonValuesOtd globalTst = jsonOtd.getSingle(0, "globalSetting", EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES));
        globalTst.verify();

        // buttons are changed
        assertEquals(false, fcPP.radioGlobal().isSelected());
        assertEquals(true, fcPP.radioProjectSpecific().isSelected());

        JsonValuesOtd projectTst = jsonOtd.getSingle(0, "projectSetting", EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES));
        projectTst.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "changeToProjectSpecific")
    public void saveTheDataToJson() {
        fcPP.buttonOK();

        JsonValuesOtd projectTst = jsonOtd.getSingle(1, getName(),
                EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_FILTER));
        projectTst.verify();
    }

    /** nojdoc */
    private void addAdditionalPackage() {
        new ActionNoBlock("Window|Files", null).performMenu();

        FilesTabOperator fto = new FilesTabOperator();

        // work with nodes
        Node projectNode = fto.getProjectNode(testModelName);
        projectNode.select();

        JavaNode jlibNode = new JavaNode(projectNode, "src|org|jlib");
        jlibNode.select();

        JPopupMenuOperator callPopup = jlibNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("New|Folder");

        JDialogOperator newFolderDialog = new JDialogOperator("New Folder");
        JTextFieldOperator testField = new JTextFieldOperator(newFolderDialog, "folder");
        testField.setText("additionalpackage");

        JButtonOperator finishButtonOper = new JButtonOperator(newFolderDialog, "Finish");
        finishButtonOper.push();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "saveTheDataToJson")
    public void addAPackageAndClass() {
        // adding a package will change the stuff on the JSON filter
        // and table information
        //
        // one-way is to add a package via file-management
        //
        // but better is via the interface and GUI
        //
        addAdditionalPackage();

        // verify the item is added to the table
        fcPP = prepareProject.nuFuncProjectProperties();

        FuncPkgFilterTable fcRootTab = fcPP.funcRootTable();
        fcRootTab.findRowColOf(PKG_STR, "Source Pac*s", "additionalpackage");

        fcPP.buttonCancel();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "addAPackageAndClass")
    public void addAPackageAndClassJson() {
        // the additional package should be added to the JSON
        JsonValuesOtd projectTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_FILTER));
        projectTst.verify();
    }

    /** nojdoc */
    private void removeAdditionalPackage() {
        new ActionNoBlock("Window|Files", null).performMenu();

        FilesTabOperator fto = new FilesTabOperator();

        // work with nodes
        Node projectNode = fto.getProjectNode(testModelName);
        projectNode.select();

        JavaNode jlibNode = new JavaNode(projectNode, "src|org|jlib|additionalpackage");
        jlibNode.select();

        JPopupMenuOperator callPopup = jlibNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Delete");

        JDialogOperator newFolderDialog = new JDialogOperator("Confirm Object Deletion");

        JButtonOperator yesButtonOper = new JButtonOperator(newFolderDialog, "Yes");
        yesButtonOper.push();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "addAPackageAndClassJson")
    public void removeAPackageAndClass() {

        removeAdditionalPackage();

        // determine item is removed from the table (tests nothing happens
        // due to mismatched data due to a refactor)
        fcPP = prepareProject.nuFuncProjectProperties();

        FuncPkgFilterTable fcRootTab = fcPP.funcRootTable();

        String aeMsg = "failed assertionerror test";
        try {
            fcRootTab.findRowColOf(PKG_STR, "Source Pac*s", "additionalpackage");
        } catch (AssertionError ae) {
            aeMsg = ae.getMessage();
        }
        assertEquals("NOT found: 'Source Pac.*s' AND contains 'additionalpackage' pair.", aeMsg);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "removeAPackageAndClass")
    public void removeAPackageAndClassJson() {
        // the additional package should be removed from the JSON
        JsonValuesOtd projectTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_FILTER));
        projectTst.verify();
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }
}
