/* Copyright (c) 2018 dbradley. All rights reserved. */
package packg.testcases.cvr.comn;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import org.jtestdb.objecttestdata.OtdFileUtils;
import org.netbeans.jellytools.OutputTabOperator;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.nodes.JavaNode;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.appfunc.FuncAssociateProjectsTabb;
import packg.appfunc.FuncGeneralTab;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;
import static packg.testcases.cvr.SelfTest_SelectFuncTC.pluginPrj;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;
import packg.zoperation.tstenv.PrepareProjectSelf;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 *
 * @author dbradley
 */
abstract public class SelfTestCommonBase extends DbradJacocoJellyTestCase {

    protected static boolean testingFinished = false;

    protected static String nameOfTestToCoverage;

    protected static String functionalDirStr = "func";

    protected static String extPrj = "dbrad-jacoco-agent-ext";
    protected static String libPrj = "dbrad-jacocoverage-lib";
    protected static String filePckMgrPrj = "dbrad-jacocoverage-filepackage-mgr";
    protected static String analyzerPrj = "dbrad-jacocoverage-analyzer";

    protected static String pluginPrj = "dbrad-jacocoverage-plugin";
    protected static String suitePrj = "dbrad-jacocoverage-suite";

    protected static PrepareProjectSelf prepareProjectSelf;
    protected static FuncProjectProperties fcPP;

    /**
     *
     * @param testName
     */
    public SelfTestCommonBase(String testName) {
        super(testName);
    }

    @Override
    protected int timeOut() {
        // need project.properties => test.timeout=3600000
        return 3600000;
    }

    @JacocoTestMethod(dependsOn = "keepTestRunningTC1")
    public void keepTestRunningTC2() {
        keepTestingRunning();
    }

    @JacocoTestMethod(dependsOn = "keepTestRunningTC2")
    public void keepTestRunningTC3() {
        keepTestingRunning();
    }

    @JacocoTestMethod(dependsOn = "keepTestRunningTC3")
    public void keepTestRunningTC4() {
        keepTestingRunning();
    }

    @JacocoTestMethod(dependsOn = "keepTestRunningTC4")
    public void keepTestRunningTC5() {
        keepTestingRunning();
    }

    @JacocoTestMethod(dependsOn = "keepTestRunningTC5")
    public void endRunAllTest() {

        // need to ensure wehave a completed test
        OutputTabOperator oto = new OutputTabOperator("dbrad.jacocoverage.plugin (test-single)");

        // wait for the collection task to complete
        pauseMs(5000);
        oto.waitText("BUILD SUCCESSFUL");

        // process the console for the test-bed-IDE and make reports
        String testOutput = oto.getText();
        String[] testOutByLineArr = testOutput.split(String.format("\n"));

        // get the test run information and place them in to the development-IDE
        // console by an indirect manner
        String[] resultsStrArr = new String[]{" ", " ", " ", " "};

        for (String testResultLine : testOutByLineArr) {
            // Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 58.263 sec
            if (testResultLine.contains("Tests run:")) {
                resultsStrArr[0] = "=======================================================================";
                resultsStrArr[1] = testResultLine;
                resultsStrArr[2] = "= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =";
                System.out.println(resultsStrArr[0]);
                System.out.println(resultsStrArr[1]);
                System.out.println(resultsStrArr[2]);

                break;
            }
        }
        // create the console report that will match an ts_xxxxxxxxxxx_xxxxx_reportDir
        // by getting the latest file of
        File rptsDirFile = new File(PrepareProjectSelf.tmpDirectory4CopyReports);

        File[] rptsDirFileList = rptsDirFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("ts_");
            }
        });

        // need to get the latest of the ts_ dir, should always happen unless 
        // there is a major problem with the processing of reports and/or the 
        // system file S/W.
        //
        // the latest will be the last in ascending order
        Arrays.sort(rptsDirFileList);
        File latestRptDirFile = rptsDirFileList[rptsDirFileList.length - 1];

        // store the test-bed-IDE console to a file within the latest 
        // ts_ directory
        String lastConsoleFilePath = String.format("%s/%s",
                latestRptDirFile.getAbsolutePath(),
                "ConsoleReport_ofSelfTest.txt");

        // provide the results values and the report itself
        OtdFileUtils.writeFile(lastConsoleFilePath, resultsStrArr, false);
        OtdFileUtils.writeFile(lastConsoleFilePath, testOutByLineArr, true);

        // as the console report is part of the ts_ structure, it will be 
        // storage managed (deleted) with the ts_ structure processing
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        pauseMs(2000);
    }

    final protected void openSuiteProjectSuper() {
        ProjectsTabOperator prjTab = new ProjectsTabOperator();

        // set to tree view
        Node suiteNode = prjTab.getProjectRootNode(suitePrj);
        suiteNode.callPopup();
        pauseMs(500);
        JPopupMenuOperator popup1 = new JPopupMenuOperator();

        // ComponentOperator.WaitStateTimeout   default is 30000ms seems small
        // 
        // The following is a border-line-condition timeout value for the clean-and-build
        // request; in that about 1 in 10 tries were failing at this point (of the request.
        //
        // so the following timeout value changes are employed to address the 
        // border-line-condition
        // 1) double the timeout
        // 2) restore the original timeout after the 
        //
        long origSetting = JemmyProperties.getCurrentTimeout("ComponentOperator.WaitStateTimeout");
        JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", origSetting * 2);

        // wait on clean-and-build to complete
        popup1.pushMenu("Clean and Build");
        OutputTabOperator oto = new OutputTabOperator("dbrad-jacocoverage-suite (clean,build)");

        // wait for a message appears in output
        oto.waitText("BUILD SUCCESSFUL");

        // restore the original setting for the timeout
        JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", origSetting);
    }

    final protected void configForSelfSuper() {

        fcPP = prepareProjectSelf.nuFuncProjectProperties();

        // make project specific
        fcPP.radioProjectSpecific().clickMouse();

        // general tab
        // - set timestamp
        // - set path for user-defined directory
        // - by-project
        FuncGeneralTab fcGen = fcPP.funcGeneralTabb();

        fcGen.setOnTimeStamp();
        fcGen.clickByProject();

        fcPP.buttonOK();

        fcPP = prepareProjectSelf.nuFuncProjectProperties();
        fcGen = fcPP.funcGeneralTabb();

        fcGen.clickUserDefinedDirectory();

        // a file chooser dialog should be presented
        JDialogOperator fileChooser = new JDialogOperator("Open");

        JTextFieldOperator fileNameField = new JTextFieldOperator(fileChooser);
        fileNameField.setText(PrepareProjectSelf.tmpDirectory4CopyReports);

        JButtonOperator openButton = new JButtonOperator(fileChooser, "Open");
        openButton.push();

        // associate-tab settings
        // - set dbrad-jacocoverage-analyzer source-code
        // - set dbrad-jacocoverage-filepackage-mgr source-code
        FuncAssociateProjectsTabb fcAssoc = fcPP.funcAssociateProjects();

        FuncPkgFilterTable funcAnalyzer = fcAssoc.getFuncPfTable(analyzerPrj);
        funcAnalyzer.setOn4OnOff("Source Packages");

        FuncPkgFilterTable funcFileMgr = fcAssoc.getFuncPfTable(filePckMgrPrj);
        funcFileMgr.setOn4OnOff("Source Packages");
        pauseMs(1000);

        // store away
        fcPP.buttonOK();
    }

    final protected void startWithNameOfTestToCoverage(String nameOfTestToCoverage) {
        ProjectsTabOperator prjTab = new ProjectsTabOperator();
        Node projectNode = prjTab.getProjectRootNode(pluginPrj);

        // select the file and do the Test File w/jacocoverage invoke
        JavaNode jlibNode = new JavaNode(projectNode, "Unit Test Packages|packg|testcases|"
                + functionalDirStr
                + "|"
                + nameOfTestToCoverage);

        JPopupMenuOperator callPopup2 = jlibNode.callPopup();
        pauseMs(1000);
        callPopup2.pushMenu("Test File w/jacocoverage");
        pauseMs(500);
    }

    protected void keepTestingRunningManual() {
        while (true) {
            pauseMs(3600000);
        }
    }

    protected void keepTestingRunning() {
        // need to ensure we have a completed test
        OutputTabOperator oto = new OutputTabOperator("dbrad.jacocoverage.plugin (test-single)");
        // wait for a message appears in output

        pauseMs(200);
        // the stop button in the output is enabled while a process is running,
        // once it changes to disabled we know the test file w/jacocverage has completed
        JButtonOperator buttStop = oto.btnStop();
        boolean btnSet = true;

        // perform a loop for 500 seconds (which will be repeated a
        // number of times by the keepTestRunningTC2/3/4/5 pseudo test-cases
        int timeLimit = 500000;
        int sampleTime = 5000;

        while (btnSet) {
            // as long as the stop button is enabled, the test-bed-IDE is
            // actively testing
            btnSet = buttStop.isEnabled();

            if (btnSet) {
                pauseMs(sampleTime);
                // have we timed out by counting down the timer
                timeLimit -= sampleTime;
                btnSet = !(timeLimit <= 0);
            }
        }
    }
}
