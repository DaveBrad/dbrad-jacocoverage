/* Copyright (c) 2018 dbradley.  */
package packg.testcases.func;

import junit.framework.Test;
import org.netbeans.jellytools.OptionsOperator;
import org.netbeans.jellytools.actions.OptionsViewAction;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;
import packg.zoperation.tstenv.PrepareProject;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 * Class that tests the global preferences dialog as best as can be.
 *
 * @author dbradley
 */
@JacocoTestClass
public class GlobalPref extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private static final String testModelName = "JavaLibraryTestModel1";
    private static PrepareProject prepareProject;

    private static OptionsOperator optionsOper;
    private static JTabbedPaneOperator javaTabbedPane;

    /**
     *
     * @param testName
     */
    public GlobalPref(String testName) {
        super(testName);
    }

    /** Test case 1. */
    @JacocoTestMethod
    public void prepareLaunchIde() {
        prepareProject = new PrepareProject();
        prepareProject.openProjectsNoPrjPropDialog(this, testModelName);
    }

    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void openPreferences() {
        new OptionsViewAction().performMenu();

        optionsOper = new OptionsOperator();
        optionsOper.selectCategory("Java");

        javaTabbedPane = new JTabbedPaneOperator(optionsOper);
        javaTabbedPane.selectPage("JaCoCoverage-dbrad");
    }

    @JacocoTestMethod(dependsOn = "openPreferences")
    public void about() {
        // the about button causes a static blocking dialog to be presented, so a 
        // no block action will need to be done
        JButtonOperator aboutButt = new JButtonOperator(javaTabbedPane, "About");
        aboutButt.pushNoBlock();

        JDialogOperator aboutDlg = new JDialogOperator("About dbrad-jacocoverage");
        pauseMs(2000);
        aboutDlg.close();
    }

    @JacocoTestMethod(dependsOn = "about")
    public void cancelDlg() {
        JButtonOperator cancelButt = new JButtonOperator(optionsOper, "Cancel");
        cancelButt.pushNoBlock();
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        //
        prepareProject.closeAllProjects();
    }
}
