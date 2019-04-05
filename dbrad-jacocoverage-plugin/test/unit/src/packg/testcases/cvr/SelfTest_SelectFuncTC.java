/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.testcases.cvr;

import java.awt.event.KeyEvent;
import packg.testcases.cvr.comn.SelfTestCommonBase;
import packg.testcases.func.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import junit.framework.Test;
import org.junit.Rule;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.ActionNoBlock;
import org.netbeans.jemmy.Timeout;
import org.netbeans.jemmy.drivers.input.KeyEventDriver;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.openide.util.Exceptions;
import packg.zoperation.ann.JacocoClassStandalone;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.PrepareProjectSelf;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 *
 * @author dbradley
 */
@JacocoClassStandalone
public class SelfTest_SelectFuncTC extends SelfTestCommonBase {

    private final static String tempPropSelftest = "tempPropselftest";
    private static String runTestcaseName;

    /** nojdoc
     *
     * @return */
    final public static Test suite() {
        // suite is invoked by a different ClassLoader from the test-case
        // method invokes and as such any static variable and/or the class
        // instance cannot transfer information between different ClassLoaders.
        //
        // Too overcome this the test-case selected string is passed by setting
        // a property value temporarily.
        //
        runTestcaseName = getTestcaseToRun();
        if (runTestcaseName == null) {
            return null;
        }
        // set the temporary property value
        System.setProperty(tempPropSelftest, runTestcaseName);

        // start the testing request
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    /**
     *
     * @param testName
     */
    public SelfTest_SelectFuncTC(String testName) {
        super(testName);
    }

    @Override
    protected int timeOut() {
        return 3600000;
    }

    /** nojdoc */
    @JacocoTestMethod
    public void prepareNbmSelfTest() {
        // get the property for the test-case class to run and set it
        // as the run-test-case-name.
        //
        runTestcaseName = System.getProperty(tempPropSelftest);
        if (runTestcaseName == null) {
            assertTrue("TEMP PROPERTY FAILED : NO TEST CASE SELECTED", false);
        }
        // clear the temporary property
        System.clearProperty(tempPropSelftest);

        // begin the testing sequence of actions to run the test-case class
        prepareProjectSelf = new PrepareProjectSelf();

        prepareProjectSelf.openProjectsSelf(this, suitePrj,
                pluginPrj,
                extPrj,
                libPrj,
                filePckMgrPrj,
                analyzerPrj
        );
        //  
        new ActionNoBlock("Window|Output", null).performMenu();
        new ActionNoBlock("Window|Projects", null).performMenu();
        pauseMs(1000);

        // need to allow time for the menu to process, otherwise there is
        // a collision with the below clickForPopup request below.
        pauseMs(1000);

        // set to tree view
        ProjectsTabOperator prjTab = new ProjectsTabOperator();

        new KeyEventDriver().typeKey(prjTab,
                KeyEvent.VK_R, 'R',
                KeyEvent.ALT_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK, new Timeout("nnnn", 200));

        prjTab.clickForPopup();

        JPopupMenuOperator popup1 = new JPopupMenuOperator();
        popup1.pushMenu("View Java Packages as|tree", "|");

    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareNbmSelfTest")
    public void openSuiteProject() {
        openSuiteProjectSuper();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "openSuiteProject")
    public void configForSelf() {
        configForSelfSuper();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "configForSelf")
    public void startSelectedTest() {
        startWithNameOfTestToCoverage(runTestcaseName);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "startSelectedTest")
    public void keepTestRunningTC1() {
        keepTestingRunning();
    }

    // SelfTestCommonBase contains additional test-cases that add more time
    // to the testing cycle so as to prevent the test timeout settings
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    private static ArrayList<File> getListOfTestcases(File processDir, ArrayList<File> fileListArr) {

        if (fileListArr != null) {
            if (processDir.isDirectory()) {
                File[] listFileArr = processDir.listFiles(
                        new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        // the list files needs to be recurive to process
                        // for all directories, and then specific files
                        if (pathname.isDirectory()) {
                            return true;
                        }
                        // needs to be the func directory
                        File parentName = pathname.getParentFile();

                        if (!parentName.getName().equals(functionalDirStr)) {
                            return false;
                        }
                        // need to filter on a file name
                        String nameOfFile = pathname.getName();
                        // only java files
                        if (!nameOfFile.endsWith(".java")) {
                            return false;
                        }
                        // exclude NbmSelfTest modules
                        if (nameOfFile.startsWith("NbmSelfTest")) {
                            return false;
                        }
                        return true;
                    }

                });

                for (File processItem : listFileArr) {
                    fileListArr = getListOfTestcases(processItem, fileListArr);
                }
            } else {
                // else assume its a file that is a test-case
                fileListArr.add(processDir);
            }
            return fileListArr;
        }
        // the dataDir is test/qa-functional/data and the source is test/qa-functional/src, need
        // to get this source directory
        // this class is being run out of the .../build/..../classes directory which in Netbeans
        // is rooted from the same place as source
        String thisClassRuntime = SelfTest_SelectFuncTC.class.getProtectionDomain() //this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath();

        // get the list of test-cases
        // convert to the test/qa-functional/src directory
        // this structure appears to be fixed, but if it changes then things will
        // fail
        thisClassRuntime = thisClassRuntime.replace("/build/", "/");
        String sourceDir4TestQaFunctional = thisClassRuntime.replace("/classes", "/src");

        sourceDir4TestQaFunctional = sourceDir4TestQaFunctional.replaceAll(File.pathSeparator, "/");

        Package myPackage = SelfTest_SelectFuncTC.class.getPackage(); //this.getClass().getPackage();
        String myPackageStr = myPackage.getName();

        String testcaseDirStr = String.format("%s/%s", sourceDir4TestQaFunctional,
                myPackageStr.replaceAll("\\.", "/"));

        // this class is part of the testcases structure
        // packg.testcases.cvr alongside  packg.testcases.func
        //
        //  any change to the structure will cause this to break
        //
        File nbmSelfDir = new File(testcaseDirStr);

        // get all the testcases in the testcases dir
        // excluding NbmSelfTest
        fileListArr = new ArrayList<>(10);
        processDir = nbmSelfDir.getParentFile();

        return getListOfTestcases(processDir, fileListArr);
    }

    /**
     * Method that will dialog with the user to determine what
     * functional-test-case the user wishes to execute for self test.
     *
     * @return string of test-case name, or null if not selected
     */
    private static String getTestcaseToRun() {
        // get the list of legit test-case-classes
        ArrayList<File> fileListArr = getListOfTestcases(null, null);
        Collections.sort(fileListArr);

        // dialog presentation processing 
        Object[] possibilities = new Object[fileListArr.size()];

        for (int i = 0; i < fileListArr.size(); i++) {
            File item = fileListArr.get(i);
            possibilities[i] = item.getName(); // test case name under func
        }
        // the test case list in an alphabetic order
        JList list = new JList(possibilities);

        list.setVisibleRowCount(8);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jscrollpane = new JScrollPane();
        jscrollpane.setViewportView(list);

        // WARNING: if running in debug mode the dialog will go behind the IDE
        //99 how to make this not center of screen , but offset from parent
        int buttonType = JOptionPane.showOptionDialog(null,
                jscrollpane,
                "Select self-test-case to run from list",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, null, null);

        // selected item processing
        String selectedItem = (String) list.getSelectedValue();

        // need 'select' and 'OK' combined condition; otherwise fail
        boolean isSelectedItem = selectedItem != null && buttonType == JOptionPane.OK_OPTION;
        assertTrue("NO TEST CASE SELECTED", isSelectedItem);

        return selectedItem;
    }
}
