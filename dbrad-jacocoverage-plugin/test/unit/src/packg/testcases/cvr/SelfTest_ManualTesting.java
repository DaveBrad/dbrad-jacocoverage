/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.testcases.cvr;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import packg.testcases.cvr.comn.SelfTestCommonBase;
import packg.testcases.func.*;
import junit.framework.Test;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.ActionNoBlock;
import org.netbeans.jemmy.Timeout;
import org.netbeans.jemmy.drivers.input.KeyEventDriver;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import packg.zoperation.ann.JacocoClassStandalone;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.PrepareProjectSelf;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 *
 * @author dbradley
 */
@JacocoClassStandalone
public class SelfTest_ManualTesting extends SelfTestCommonBase {

    /** nojdoc
     *
     * @return */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    /**
     *
     * @param testName
     */
    public SelfTest_ManualTesting(String testName) {
        super(testName);
    }

    /** nojdoc */
    @JacocoTestMethod
    public void prepareNbmSelfTest() {
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
//        new ActionNoBlock("Window|Output", null).performShortcut();

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
    public void startManulTest() {
        pauseMs(2000);
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "startManulTest")
    public void keepTestRunningTC1() {
        keepTestingRunningManual();
    }

    // SelfTestCommonBase contains additional test-cases that add more time
    // to the testing cycle so as to prevent the test timeout settings
}
