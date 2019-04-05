/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.testcases.func;

import dbrad.jacocoverage.menuctxt.action.reqproc.JaCoCoRequestProcess;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import junit.framework.Test;
import org.jtestdb.objecttestdata.OtdFileUtils;
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
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.appfunc.FuncGeneralTab;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.JsonValuesOtd;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;
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
@JacocoTestClass(dependsOnClass = AssociateSE.class)
public class TestWithHelloWorld extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private final static String testModelName = "TestHelloWorld";
    private static PrepareProject prepareProject;

    private static FuncProjectProperties fcPP;
    private static FuncGeneralTab fcGenTab;

    private static TableOtd packageFilterTableOtd;

    private static JsonValuesOtd jsonOtd;

    private static File time1stTsFile;

    /** nojdoc */
    @JacocoTestMethod
    public void loadHelloWorld() {
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
    @JacocoTestMethod(dependsOn = "loadHelloWorld")
    public void setupForRunHelloWorld() {
        // change to project specific
        fcPP.radioProjectSpecific().clickMouse();
        pauseMs(1000);

        fcGenTab = fcPP.funcGeneralTabb();
        fcGenTab.setOffAutoOpenHtmlReport();

        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "setupForRunHelloWorld")
    public void runHelloWorldProgram() {
        new ActionNoBlock("Window|Output", null).performMenu();
        new ActionNoBlock("Window|Files", null).performMenu();

        FilesTabOperator fto = new FilesTabOperator();

        // work with nodes
        Node projectNode = fto.getProjectNode(testModelName);
        projectNode.select();

        JavaNode jlibNode = new JavaNode(projectNode, "src|testhelloworld|TestHelloWorld.java");
        jlibNode.select();

        pauseMs(1000);
        JPopupMenuOperator callPopup = jlibNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Open");

        pauseMs(2000);

        new ActionNoBlock("Window|Projects", null).performMenu();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);
        ptoProjectNode.select();

        callPopup = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup.pushMenu("Clean and build");

        OutputTabOperator oto = new OutputTabOperator("TestHelloWorld (clean,jar)");
        // wait for a message appears in output
        oto.waitText("BUILD SUCCESSFUL");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runHelloWorldProgram")
    public void runHelloPreConditions() {
        // there should not be a 
        // .jacocodbrad/reportDir 

        File reportDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad/reportDir");
        assertEquals("error directory found", false, reportDir.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runHelloPreConditions")
    public void runHello() {
        // nothing to wait on task at the moment but hello-world should 
        // build & run within about 2 seconds
        pauseMs(2000);

        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runHello")
    public void runHelloPostConditions() {

        File reportDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad/reportDir");
        assertEquals("error1 directory NOT found", true, reportDir.exists());

        // .jacocodbrad/reportDir/TestHelloWorld_Source Packages.report.html directory
        File testhelloworldDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad/reportDir/TestHelloWorld_Source Packages.report.html");
        assertEquals("error2 directory NOT found", true, testhelloworldDir.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runHelloPostConditions")
    public void testTimeStamped() {
        // set timestamp for processing in the .jacocodbrad directory
        fcPP = prepareProject.nuFuncProjectProperties();

        FuncGeneralTab fcGT = fcPP.funcGeneralTabb();

        fcGT.clickTimeStamp();
        fcGT.setPreviousVersionValue(2);
        // no need to check the settings as the next code will do the checking
        // by implication
        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();

        // prepare to do multiple run w/jacocoverage 
        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        // process a first time-stamp file
        long startTime1stTsFile = timeStampFormatToLong("start");

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
        pauseMs(1000);

        long stopTime1stTsFile = timeStampFormatToLong("stop");

        time1stTsFile = getNewestTimeStampDirFromDefault();
        boolean testOn1st = inTimeStampRange(startTime1stTsFile,
                time1stTsFile.getName(),
                stopTime1stTsFile);

        assertEquals(true, testOn1st);

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "testTimeStamped")
    public void runProjectAndEditorContextHelloWorld2ndTime() {

        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        // process a second time-stamp file
        pauseMs(2000);
        long startTime2ndTsFile = timeStampFormatToLong("start");

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
        pauseMs(2000);

        long stopTime2ndTsFile = timeStampFormatToLong("stop");

        File time2ndTsFile = getNewestTimeStampDirFromDefault();
        boolean testOn2nd = inTimeStampRange(startTime2ndTsFile,
                time2ndTsFile.getName(),
                stopTime2ndTsFile);

        assertEquals(true, testOn2nd);
        // first time-stamp directory should remain
        assertEquals(true, time1stTsFile.exists());

        // doing a third run should remove the first time-stamp directory
        // process a third time-stamp file
        pauseMs(2000);
        long startTime3rdTsFile = timeStampFormatToLong("start");

        // Run file via the editor context menu
        EditorOperator editorOper = EditorWindowOperator.selectPage("TestHelloWorld.java");
        editorOper.enterMouse();

        editorOper.clickForPopup(100, 100);

        JPopupMenuOperator callPopupEditor = new JPopupMenuOperator();
        pauseMs(500);
        callPopupEditor.pushMenu("Run File w/jacocoverage");

        pauseMs(1000);

        long stopTime3rdTsFile = timeStampFormatToLong("stop");

        File time3rdTsFile = getNewestTimeStampDirFromDefault();

        boolean testOn3rd = inTimeStampRange(startTime3rdTsFile,
                time3rdTsFile.getName(),
                stopTime3rdTsFile);

        assertEquals(true, testOn3rd);
        // first time-stamp directory has been deleted
        //
        // provide time for the file to be 
        pauseMs(2000);
        assertEquals(false, time1stTsFile.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runProjectAndEditorContextHelloWorld2ndTime")
    public void onlyTwoTimestampFilesRemain() {
        File jacocodbradDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad");

        // there should only be 2 left in the folder
        String[] listOfItemsArr = getTimeStampDirList(jacocodbradDir);
        assertEquals(2, listOfItemsArr.length);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "onlyTwoTimestampFilesRemain")
    public void runToUserDefinedReportDirectory() {

        fcPP = prepareProject.nuFuncProjectProperties();

        FuncGeneralTab fcGT = fcPP.funcGeneralTabb();

        fcGT.clickUserDefinedDirectory();

        // a file chooser dialog should be presented
        JDialogOperator fileChooser = new JDialogOperator("Open");

        JTextFieldOperator fileNameField = new JTextFieldOperator(fileChooser);
        fileNameField.setText(prepareProject.getWorkspaceDir().getAbsolutePath());

        JButtonOperator openButton = new JButtonOperator(fileChooser, "Open");

        openButton.push();

        // okay the properties project
        pauseMs(1000);
        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();

        // prepare to do multiple run w/jacocoverage 
        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        // process a first time-stamp file
        long startTime1stTsFile = timeStampFormatToLong("start");

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
        pauseMs(1000);

        long stopTime1stTsFile = timeStampFormatToLong("stop");

        time1stTsFile = getNewestTimeStampDirFromUserDefined();
        boolean testOn1st = inTimeStampRange(startTime1stTsFile,
                time1stTsFile.getName(),
                stopTime1stTsFile);

        assertEquals(true, testOn1st);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runToUserDefinedReportDirectory")
    public void runHelloWorld2ndTimeUserDefinedReportDir() {

        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        // process a second time-stamp file
        pauseMs(2000);
        long startTime2ndTsFile = timeStampFormatToLong("start");

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
        pauseMs(2000);

        long stopTime2ndTsFile = timeStampFormatToLong("stop");

        File time2ndTsFile = getNewestTimeStampDirFromUserDefined();
        boolean testOn2nd = inTimeStampRange(startTime2ndTsFile,
                time2ndTsFile.getName(),
                stopTime2ndTsFile);

        assertEquals(true, testOn2nd);
        // first time-stamp directory should remain
        assertEquals(true, time1stTsFile.exists());

        // doing a third run should remove the first time-stamp directory
        // process a third time-stamp file
        pauseMs(2000);
        long startTime3rdTsFile = timeStampFormatToLong("start");

        callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");
        pauseMs(1000);

        long stopTime3rdTsFile = timeStampFormatToLong("stop");

        File time3rdTsFile = getNewestTimeStampDirFromUserDefined();

        boolean testOn3rd = inTimeStampRange(startTime3rdTsFile,
                time3rdTsFile.getName(),
                stopTime3rdTsFile);

        assertEquals(true, testOn3rd);
        // first time-stamp directory has been deleted
        pauseMs(2000);
        assertEquals(false, time1stTsFile.exists());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "runHelloWorld2ndTimeUserDefinedReportDir")
    public void onlyTwoTimestampFilesRemainUserDir() {
        File workspaceDir = prepareProject.getWorkspaceDir();

        // there should only be 2 left in the folder
        String[] listOfItemsArr = getTimeStampDirList(workspaceDir);
        assertEquals(2, listOfItemsArr.length);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "onlyTwoTimestampFilesRemainUserDir")
    public void noLimitOnSavedReports() {
        fcPP = prepareProject.nuFuncProjectProperties();
        FuncGeneralTab fcGT = fcPP.funcGeneralTabb();

        // cleanout the workspace of any existing reports
        File workspaceDir = prepareProject.getWorkspaceDir();
        OtdFileUtils.emptyDirectoryContents(workspaceDir);

        // set to no cleanup or retain all
        fcGT.setPreviousVersionValue(0);
        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();

        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        int makeReportCount = 6;
        for (int i = 0; i < makeReportCount; i++) {
            JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
            pauseMs(500);
            callPopup2.pushMenu("Run w/jacocoverage");

            pauseMs(1500);
        }

        // there should only be 'makeReportCount' in the folder
        String[] listOfItemsArr = getTimeStampDirList(workspaceDir);
        assertEquals(makeReportCount, listOfItemsArr.length);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "noLimitOnSavedReports")
    public void revertLimitTo2() {

        File latestFileFrom6 = getNewestTimeStampDirFromUserDefined();

        fcPP = prepareProject.nuFuncProjectProperties();
        FuncGeneralTab fcGT = fcPP.funcGeneralTabb();

        // set to no cleanup or retain all
        fcGT.setPreviousVersionValue(2);
        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();

        // perform another test so that 2 will only remain
        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");

        pauseMs(1500);

        // the latest directory from the batch of 6 should remain
        assertEquals(true, latestFileFrom6.exists());

        // there should only be '2' in the folder
        File workspaceDir = prepareProject.getWorkspaceDir();

        String[] listOfItemsArr = getTimeStampDirList(workspaceDir);

        assertEquals(2, listOfItemsArr.length);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "revertLimitTo2")
    public void revertLimitToDefaultDir() {

        File latestFileFrom6 = getNewestTimeStampDirFromUserDefined();

        fcPP = prepareProject.nuFuncProjectProperties();
        FuncGeneralTab fcGT = fcPP.funcGeneralTabb();

        fcGT.clickDefault();
        pauseMs(2000);

        // the user report dir field should be retained
        JTextFieldOperator repDirField = fcGT.getUserReportDirField();

        // need to match the slashes
        String wrkSpFwdSlashes = prepareProject.getWorkspaceDir().getAbsolutePath().replaceAll("\\\\", "/");
        String repDirFwdSlashes = repDirField.getText().replaceAll("\\\\", "/");

        assertEquals(wrkSpFwdSlashes, repDirFwdSlashes);

        // set to no cleanup or retain all
        fcGT.setPreviousVersionValue(2);
        fcPP.buttonOK();

        JsonValuesOtd jsonTst = jsonOtd.getSingle(0, getName(), EnumSet.of(JSON_PREFERENCES));
        jsonTst.verify();

        // perform another test so that 2 will only remain
        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Run w/jacocoverage");

        pauseMs(1500);

        // the latest directory from the batch of 6 should remain
        assertEquals(true, latestFileFrom6.exists());

        // there should only be '2' in the folder
        File defaultDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad");

        String[] listOfItemsArr = getTimeStampDirList(defaultDir);

        assertEquals(2, listOfItemsArr.length);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "revertLimitToDefaultDir")
    public void clearResults() {
        // perform another test so that 2 will only remain
        ProjectsTabOperator pto = new ProjectsTabOperator();
        Node ptoProjectNode = pto.getProjectRootNode(testModelName);

        JPopupMenuOperator callPopup2 = ptoProjectNode.callPopup();
        pauseMs(500);
        callPopup2.pushMenu("Reset (w/jacocoverage)");

        pauseMs(1000);
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    /**
     *
     * @param inDir
     *
     * @return
     */
    private String[] getTimeStampDirList(File inDir) {
        String[] listOfItemsArr = inDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(JaCoCoRequestProcess.TIME_STAMP_STRING);
            }
        });

        return listOfItemsArr;
    }

    /** nojdoc */
    private File getNewestTimeStampDirFromDefault() {
        File jacocodbradDir = prepareProject.getForProjNameFileItem(testModelName,
                ".jacocodbrad");

        return getNewestTimeStampDir(jacocodbradDir);
    }

    /** nojdoc */
    private File getNewestTimeStampDirFromUserDefined() {
        File userDir = prepareProject.getWorkspaceDir();

        return getNewestTimeStampDir(userDir);
    }

    /**
     *
     * @param dirOfReports
     *
     * @return
     */
    private File getNewestTimeStampDir(File dirOfReports) {
        String[] listArr = getTimeStampDirList(dirOfReports);
        // process each item and get the newest
        File newestFile = null;
        for (String fileName : listArr) {
            File processFile = new File(dirOfReports, fileName);

            if (newestFile == null) {
                newestFile = processFile;
            } else {
                // compare the times of creation
                if (newestFile.lastModified() < processFile.lastModified()) {
                    newestFile = processFile;
                }
            }
        }
        return newestFile;
    }

    /**
     *
     * @param testName
     */
    public TestWithHelloWorld(String testName) {
        super(testName);
    }

    /**
     * A long number that represents the date and time in integer form and from
     * a label like ts_20170101_125959_reportDir => 20170101125959
     * <p>
     * Use "start" or "stop" to get a current time stamp for now, with a 1100
     * millisecond delay to ensure time will not collide on 1 second timing.
     *
     * @param timeStampDirString timestamp dir name string convert to long, or
     *                           "start" or "stop"
     *
     * @return long of date and second value (not a Java date time)
     */
    private long timeStampFormatToLong(String timeStampDirString) {
        String tsString;

        if (timeStampDirString.startsWith(JaCoCoRequestProcess.TIME_STAMP_STRING)) {
            // process the file name provided 
            // 'ts_20170101_125959_reportDir
            // .0.....1........2......3
            //
            String[] splitArr = timeStampDirString.split("_");

            tsString = String.format("%s%s", splitArr[1], splitArr[2]);
        } else {
            if (timeStampDirString.equals("stop")) {
                // ensure any previos actions are at least 1 second
                // from now so as ensure time-stamps to 1 second
                // accuracy 
                pauseMs(1100);
            }

            Date timestampMarker = Calendar.getInstance().getTime();
            tsString = String.format("%s",
                    new SimpleDateFormat("yyyyMMddHHmmss").format(timestampMarker));

            if (timeStampDirString.equals("start")) {
                // provide any actions after the start to be performed
                // at least 1 second after
                pauseMs(1100);
            }
        }
        return Long.parseLong(tsString);
    }

    /**
     * Test the file/directory timestamp is between a start and stop time-stamp
     * pair. The use of this method requires the caller/user to ensure that the
     * times are at least 1 or 2 seconds apart so as to ensure the test will
     * work.
     *
     * @param startTimeStamp     long of a timestamp
     * @param timeStampDirString file/directory that is timestamped with the
     *                           format ts_20170101_125959_reportDir
     * @param stopTimeStamp      long of a timestamp
     *
     * @return
     */
    private boolean inTimeStampRange(long startTimeStamp,
            String timeStampDirString,
            long stopTimeStamp) {
        //
        long inbetweenTimeStamp = timeStampFormatToLong(timeStampDirString);

        if (startTimeStamp < inbetweenTimeStamp) {
            if (inbetweenTimeStamp < stopTimeStamp) {
                return true;
            }
        }
        return false;
    }
}
