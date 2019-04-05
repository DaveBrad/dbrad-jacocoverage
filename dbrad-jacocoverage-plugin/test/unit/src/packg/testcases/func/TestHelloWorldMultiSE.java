/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.testcases.func;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import junit.framework.Test;
import static junit.framework.TestCase.assertEquals;
import org.netbeans.jellytools.EditorOperator;
import org.netbeans.jellytools.EditorWindowOperator;
import org.netbeans.jellytools.FilesTabOperator;
import org.netbeans.jellytools.OutputTabOperator;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.ActionNoBlock;
import org.netbeans.jellytools.nodes.JavaNode;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import packg.appfunc.FuncGeneralTab;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.JsonValuesOtd;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;
import packg.appfunc.otdextensions.TableOtd;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.tstenv.PrepareProject;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = TestWithHelloWorld.class)
public class TestHelloWorldMultiSE extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private static final String TestHWMulti_2 = "TestHelloWorldMulti_2";
    private static final String TestHWMulti_1 = "TestHelloWorldMulti_1";
    private static final String TestHWMultiple = "TestHelloWorldMultiple";

    private static final String SOURCE_PACKAGES = "Source Packages";
    private static final String TEST_PACKAGES = "Test Packages";

    // - - - -  objects created and for use by following sequential testcases
    private static PrepareProject prepareProject;

    private static FuncProjectProperties fcPP;

    private static TableOtd packageFilterTableOtd;
    private static JsonValuesOtd jsonOtd;

    static ArrayList<File> modelArr;

    // when testing for the report files is required there are three possible
    // files (the file paths are defined once)
    private static File file_0;
    private static File file_1;
    private static File file_2;

    /** nojdoc */
    @JacocoTestMethod
    public void prepareLaunchIde() {

        prepareProject = new PrepareProject();

        // jelly-test-case, root-model, associated projects
        fcPP = prepareProject.openProjects(this, TestHWMultiple,
                TestHWMulti_2,
                TestHWMulti_1);

        if (packageFilterTableOtd == null) {
            String myClassName = getClass().getSimpleName();

            packageFilterTableOtd = new TableOtd(fcPP,
                    _OtddataClass.sourceDir(), _OtddataClass.packageString(),
                    myClassName + "TD");

            jsonOtd = new JsonValuesOtd(fcPP.getJsonFilePath(),
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "JsonTD");

            // the default directory is being processed for report files, set the 
            // reports we are expecting
            File reportDir = prepareProject.getForProjNameFileItem("TestHelloWorldMultiple",
                    ".jacocodbrad/reportDir");

            // there should be 3 files in the directory
            file_0 = new File(reportDir, "TestHelloWorldMultiple_Source Packages.report.html");
            file_1 = new File(reportDir, "TestHelloWorldMulti_1_Source Packages.report.html");
            file_2 = new File(reportDir, "TestHelloWorldMulti_2_Source Packages.report.html");
        }
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void changeToProjectSpecific() {
        // change to project specific
        fcPP.radioProjectSpecific().clickMouse();
        pauseMs(1000);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "changeToProjectSpecific")
    public void associatedProjectEnabled() {
        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        packageFilterTableOtd.dumpAll(0, getName());
        packageFilterTableOtd.verifyAll(0, getName());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "associatedProjectEnabled")
    public void setAssocTablesOn() {
        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        FuncPkgFilterTable fcTB_root = fcPP.getPfJTableForProjectName("TestHelloWorldMultiple");
        fcTB_root.setOn4OnOff(SOURCE_PACKAGES);
        fcTB_root.setOff4OnOff(TEST_PACKAGES);

        FuncPkgFilterTable fcTB_1 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_1");
        fcTB_1.setOn4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_2 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_2");
        fcTB_2.setOn4OnOff(SOURCE_PACKAGES);

        FuncGeneralTab fcGen = fcPP.funcGeneralTabb();
        fcGen.setOnGenerateHtmlReport();
        fcGen.setOffAutoOpenHtmlReport();

        packageFilterTableOtd.dumpAll(0, getName());
        packageFilterTableOtd.verifyAll(0, getName());

        fcPP.buttonOK();

        JsonValuesOtd jsonOtdfil = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_FILTER));
        jsonOtdfil.verify();

        // run the test
        pauseMs(1000);
        new ActionNoBlock("Window|Output", null).performMenu();
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();
        pauseMs(1000);

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Clean and build");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (clean,jar)");
        // wait for a message appears in output
        oto.waitText("BUILD SUCCESSFUL");

        callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Run w/jacocoverage");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesOn")
    public void setAssocTablesOnCheckOutput() {

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output
//        oto.close();

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMulti_1 - Source Packages",
            "    3     0     0    hello.HelloPrint.java",
            "",
            "TestHelloWorldMulti_2 - Source Packages",
            "    3     0     0    hello.world.WorldPrint.java",
            "    3     0     0    otherpck.WorldPrintOtherPacke.java",
            "",
            "End of report",
            ""}) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesOnCheckOutput")
    public void setAssocTablesOnCheckReportFiles() {
        assertEquals(true, file_0.exists());
        assertEquals(true, file_1.exists());
        assertEquals(true, file_2.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesOnCheckReportFiles")
    public void setAssocTablesOOnly1On() {
        fcPP = prepareProject.nuFuncProjectProperties();
        pauseMs(500);

        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        FuncPkgFilterTable fcTB_root = fcPP.getPfJTableForProjectName("TestHelloWorldMultiple");
        fcTB_root.setOn4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_1 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_1");
        fcTB_1.setOff4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_2 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_2");
        fcTB_2.setOn4OnOff(SOURCE_PACKAGES);

        FuncGeneralTab fcGen = fcPP.funcGeneralTabb();
        fcGen.setOnGenerateHtmlReport();
        fcGen.setOffAutoOpenHtmlReport();

        packageFilterTableOtd.newMyObject(fcPP);
        packageFilterTableOtd.dumpAll(0, getName());
        packageFilterTableOtd.verifyAll(0, getName());

        fcPP.buttonOK();

        JsonValuesOtd jsonOtdfil = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_FILTER));
        jsonOtdfil.verify();

        // run the test
        pauseMs(1000);
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Run w/jacocoverage");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesOOnly1On")
    public void setAssocTablesOOnly1OnCheckOutput() {
        new ActionNoBlock("Window|Output", null).performMenu();

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output
        oto.close();

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            // the following is removed from the results output
            // "TestHelloWorldMulti_1 - Source Packages",
            // "    3     0     0    hello.HelloPrint.java",
            // "",
            "TestHelloWorldMulti_2 - Source Packages",
            "    3     0     0    hello.world.WorldPrint.java",
            "    3     0     0    otherpck.WorldPrintOtherPacke.java",
            "",
            "End of report",
            ""}) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesOOnly1OnCheckOutput")
    public void setAssocTablesOOnly1OnCheckReportFiles() {
        assertEquals(true, file_0.exists());
        assertEquals(false, file_1.exists());
        assertEquals(true, file_2.exists());
    }

    /**
     * nojdoc
     */
    @JacocoTestMethod(dependsOn = "setAssocTablesOOnly1OnCheckReportFiles")
    public void setAssocTables1AssociateProjOnly() {
        fcPP = prepareProject.nuFuncProjectProperties();
        pauseMs(500);

        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        FuncPkgFilterTable fcTB_root = fcPP.getPfJTableForProjectName("TestHelloWorldMultiple");
        fcTB_root.setOff4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_1 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_1");
        fcTB_1.setOff4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_2 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_2");
        fcTB_2.setOn4OnOff(SOURCE_PACKAGES);

        FuncGeneralTab fcGen = fcPP.funcGeneralTabb();
        fcGen.setOnGenerateHtmlReport();
        fcGen.setOffAutoOpenHtmlReport();

        packageFilterTableOtd.newMyObject(fcPP);
        packageFilterTableOtd.dumpAll(0, getName());
        packageFilterTableOtd.verifyAll(0, getName());

        fcPP.buttonOK();

        JsonValuesOtd jsonOtdfil = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_FILTER));
        jsonOtdfil.verify();

        // run the test
        pauseMs(1000);
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Run w/jacocoverage");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTables1AssociateProjOnly")
    public void setAssocTables1AssociateProjOnlyCheckOutput() {
        new ActionNoBlock("Window|Output", null).performMenu();

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output
        oto.close();

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            // the following is removed from the results output
            // "TestHelloWorldMultiple - Source Packages",
            // "    0     0     7    .TestHelloWorldMultipleDefault.java",
            // "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            // "",
            // the following is removed from the results output
            // "TestHelloWorldMulti_1 - Source Packages",
            // "    3     0     0    hello.HelloPrint.java",
            // "",
            "TestHelloWorldMulti_2 - Source Packages",
            "    3     0     0    hello.world.WorldPrint.java",
            "    3     0     0    otherpck.WorldPrintOtherPacke.java",
            "",
            "End of report",
            ""}) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTables1AssociateProjOnlyCheckOutput")
    public void setAssocTables1AssociateProjOnlyCheckReportFiles() {
        assertEquals(false, file_0.exists());
        assertEquals(false, file_1.exists());
        assertEquals(true, file_2.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTables1AssociateProjOnlyCheckReportFiles")
    public void setAssocTablesNothingSet() {
        fcPP = prepareProject.nuFuncProjectProperties();
        pauseMs(500);

        fcPP.funcAssociateProjects().hasAssociateProjects(true);

        FuncPkgFilterTable fcTB_root = fcPP.getPfJTableForProjectName("TestHelloWorldMultiple");
        fcTB_root.setOff4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_1 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_1");
        fcTB_1.setOff4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable fcTB_2 = fcPP.getPfJTableForProjectName("TestHelloWorldMulti_2");
        fcTB_2.setOff4OnOff(SOURCE_PACKAGES);

        FuncGeneralTab fcGen = fcPP.funcGeneralTabb();
        fcGen.setOnGenerateHtmlReport();
        fcGen.setOffAutoOpenHtmlReport();

        packageFilterTableOtd.newMyObject(fcPP);
        packageFilterTableOtd.dumpAll(0, getName());
        packageFilterTableOtd.verifyAll(0, getName());

        fcPP.buttonOK();

        JsonValuesOtd jsonOtdfil = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_FILTER));
        jsonOtdfil.verify();

        // run the test
        pauseMs(1000);
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Run w/jacocoverage");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesNothingSet")
    public void setAssocTablesNothingSetCheckErrorDialog() {
        JDialogOperator nothingSetOn = new JDialogOperator("Error");

        // get the two lines from the error dialog
        JLabelOperator labelLine1 = new JLabelOperator(nothingSetOn, 0);
        String textActual1 = labelLine1.getText();

        JLabelOperator labelLine2 = new JLabelOperator(nothingSetOn, 1);
        String textActual2 = labelLine2.getText();

        assertEquals("All ON settings are 'off' for source and test folder(s),", textActual1);
        assertEquals("basically asking for w/jacocoverage with nothing to process against.",
                textActual2);

        JButtonOperator okButt = new JButtonOperator(nothingSetOn, "OK");
        okButt.push();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setAssocTablesNothingSetCheckErrorDialog")
    public void testWCoverage() {
        fcPP = prepareProject.nuFuncProjectProperties();
        pauseMs(500);

        FuncPkgFilterTable fcRootTab = fcPP.getPfJTableForProjectName(TestHWMultiple);

        fcRootTab.setOn4OnOff(SOURCE_PACKAGES);
        fcRootTab.setOn4OnOff("Test Packages");

        fcPP.buttonOK();

        pauseMs(1000);
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Test w/jacocoverage");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    0     0     7    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMultiple - Test Packages",
            "    3     0     0    isolate.IsolatedNotUnitTest.java",
            "    3     0     0    unit.isolate.IsolatedTest.java",
            "",
            "End of report",
            ""
        }) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }

        // close this output
        oto.close();
        //
        String[] splitArr = testStringToMatch.split("\\*");

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testWCoverage")
    public void runWCoverage() {
        fcPP = prepareProject.nuFuncProjectProperties();
        pauseMs(500);
        fcPP.buttonOK();

        pauseMs(1000);
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        JPopupMenuOperator callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Run w/jacocoverage");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMultiple - Test Packages",
            "    0     0     3    isolate.IsolatedNotUnitTest.java",
            "    0     0     3    unit.isolate.IsolatedTest.java",
            "",
            "End of report",
            ""
        }) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        oto.close();

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runWCoverage")
    public void runEditedTestToCallMain() {
        new ActionNoBlock("Window|Files", null).performMenu();

        FilesTabOperator fto = new FilesTabOperator();

        // work with nodes
        Node projectNode = fto.getProjectNode(TestHWMultiple);
        projectNode.select();

        JavaNode jlibNode = new JavaNode(projectNode, "test|unit|isolate|IsolatedTest.java");
        jlibNode.select();

        JPopupMenuOperator callPopup = jlibNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Open");

        EditorOperator eo = new EditorOperator("IsolatedTest.java");

        // add an import line
        eo.select("import org.junit.Test;");
        int lineNo = eo.getLineNumber() + 1;
        eo.setCaretPositionToLine(lineNo);

        eo.insert("import testhelloworldmultiple.TestHelloWorldMultiple;\n");

        eo.save();

        // add a new test case method
        eo.select("}");
        lineNo = eo.getLineNumber() + 1;
        eo.setCaretPositionToLine(lineNo);

        eo.insert("\n");
        eo.insert("@Test\n");
        eo.insert("public void text2Test(){\n");
        eo.insert("    TestHelloWorldMultiple.main(null);\n");
        eo.insert("}\n");

        eo.save();
        pauseMs(500);

        // test w/jacocoverage
        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        pauseMs(1000);

        Node ptoProjectNode = pto.getProjectRootNode("TestHelloWorldMultiple");
        ptoProjectNode.select();

        callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Clean and build");

        pauseMs(3000);

        callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Test w/jacocoverage");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMultiple - Test Packages",
            "    3     0     0    isolate.IsolatedNotUnitTest.java",
            "    5     0     0    unit.isolate.IsolatedTest.java",
            "",
            "End of report",
            ""
        }) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        oto.close();

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runEditedTestToCallMain")
    public void testFileEditorContextIsolatedTestAllAssociatedOn() {
        fcPP = prepareProject.nuFuncProjectProperties();

        FuncPkgFilterTable test_1Table = fcPP.getPfJTableForProjectName(TestHWMulti_1);
        test_1Table.setOn4OnOff(SOURCE_PACKAGES);

        FuncPkgFilterTable test_2Table = fcPP.getPfJTableForProjectName(TestHWMulti_2);
        test_2Table.setOn4OnOff(SOURCE_PACKAGES);

        fcPP.buttonOK();

        // test w/jacocoverage
        new ActionNoBlock("Window|Projects", null).performMenu();

        // test file via the editor context menu
        EditorOperator editorOper = EditorWindowOperator.selectPage("IsolatedTest.java");
        editorOper.enterMouse();
        pauseMs(500);

        editorOper.clickForPopup(100, 100);
        pauseMs(1500);

        JPopupMenuOperator callPopupEditor = new JPopupMenuOperator();
        callPopupEditor.pushMenu("Test File w/jacocoverage");

        pauseMs(1000);

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMultiple - Test Packages",
            "    0     0     3    isolate.IsolatedNotUnitTest.java",
            "    5     0     0    unit.isolate.IsolatedTest.java",
            "",
            "TestHelloWorldMulti_1 - Source Packages",
            "    3     0     0    hello.HelloPrint.java",
            "",
            "TestHelloWorldMulti_2 - Source Packages",
            "    3     0     0    hello.world.WorldPrint.java",
            "    3     0     0    otherpck.WorldPrintOtherPacke.java",
            "",
            "End of report",
            ""
        }) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        oto.close();

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testFileEditorContextIsolatedTestAllAssociatedOn")
    public void testWCoverageFileProjectMenu() {
        fcPP = prepareProject.nuFuncProjectProperties();
        fcPP.buttonOK();

        new ActionNoBlock("Window|Files", null).performMenu();

        FilesTabOperator pto = new FilesTabOperator();

        // work with nodes
        Node projectNode = pto.getProjectNode(TestHWMultiple);
        projectNode.select();

        JavaNode jlibNode = new JavaNode(projectNode, "test|unit|isolate|IsolatedTest.java");
        jlibNode.select();
        pauseMs(2000); // allow the popup to appear
        //
        // a pause after the callPopup is implemented due to a instability issue, the 
        // popup would fail 1 in 10 times. Suspect that the test-results output
        // and output views may be processing and causes a timeout issue to happen
        // 
        //(WindowWaiter.WaitWindowTimeout
        JPopupMenuOperator callPopup = jlibNode.callPopup();
        pauseMs(3000); // 500 is still an issue, changed to 1000, 1000 had border
        // line issue so changed to 3000
        //
        callPopup.pushMenu("Test File w/jacocoverage");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorldMultiple (jacocoverage report)");
        // wait for a message appears in output
        oto.waitText("End of report");

        // get the text
        String wholeOutput = oto.getText();
        // close this output

        String testStringToMatch = "";
        for (String s : new String[]{
            "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===",
            // the * represent a date-time stamp that is different for each run
            // for the most part this-stamp is left untested
            "Recorded: *",
            "Covered | Partially covered | Not covered | Java Class",
            "",
            "TestHelloWorldMultiple - Source Packages",
            "    0     0     7    .TestHelloWorldMultipleDefault.java",
            "    6     0     1    testhelloworldmultiple.TestHelloWorldMultiple.java",
            "",
            "TestHelloWorldMultiple - Test Packages",
            "    0     0     3    isolate.IsolatedNotUnitTest.java",
            "    5     0     0    unit.isolate.IsolatedTest.java",
            "",
            "TestHelloWorldMulti_1 - Source Packages",
            "    3     0     0    hello.HelloPrint.java",
            "",
            "TestHelloWorldMulti_2 - Source Packages",
            "    3     0     0    hello.world.WorldPrint.java",
            "    3     0     0    otherpck.WorldPrintOtherPacke.java",
            "",
            "End of report",
            ""
        }) {
            testStringToMatch = String.format("%s%s\n", testStringToMatch, s);
        }
        //
        String[] splitArr = testStringToMatch.split("\\*");

        oto.close();

        assertTrue(wholeOutput.startsWith(splitArr[0]));

        String a2ndPart = splitArr[1].substring(0, 10);
        String wholeOutputEndswith = wholeOutput.substring(wholeOutput.indexOf(a2ndPart));
        assertEquals(splitArr[1], wholeOutputEndswith);
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }
    //-------------------------------

    /** nojdoc
     *
     * @param testName */
    public TestHelloWorldMultiSE(String testName) {
        super(testName);
    }
}
