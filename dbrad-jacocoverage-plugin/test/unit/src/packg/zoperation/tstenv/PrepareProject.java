/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.tstenv;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import static junit.framework.TestCase.fail;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.OutputOperator;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.actions.ActionNoBlock;
import org.netbeans.jellytools.nodes.ProjectRootNode;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.WindowOperator;
import org.openide.util.Exceptions;
import packg.appfunc.FuncProjectProperties;
import packg.zoperation.NbJavacDialog;
import static packg.zoperation.NbJavacDialog.nb10AndJdk11Support;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 * Class that prepares Netbeans project loading into a functional test GUI test
 * case that uses Jelly, Jemmy and NBModuleSuite capabilities within the
 * dbrad-jacocoverage test environment.
 * <p>
 * It provides capabilities to copy Project test models to a working-folder and
 * perform the Netbeans project-view open-project for each test-model.
 * Additionally, provides access to the temporary file structure that is created
 * for the testing.
 *
 * @author dbradley
 */
public class PrepareProject {

    /** The test-case-class that will have projects opened for it. */
    protected JellyTestCase testCase;

    /** List of all models (File) to be loaded and referenced. */
    protected ArrayList<File> modelList;

    /** The qa-functional source-code directory. */
    protected String sourceDir4TestQaFunctional;

    /** Create an instance of the PrepareProject object. */
    public PrepareProject() {
        // do nothing
        
        // except this action due to apache-netbeans and JDK 11 support
        NbJavacDialog.nb10AndJdk11Support();
    }

    /**
     * Open projects for a JellyTestCase class which has launched the Netbeans
     * GUI via the Jelly/NBModuleSuite capabilities. This is done in preparation
     * for testing on a root-project's project-properties dialog.
     * <p>
     * The test-models are copied from the qa-functional/data (Functional Test
     * Data) folder each time the test-case class is executed.
     *
     * @param testCase           JellyTestCase object that does the
     *                           Jelly/NBModuleSuite GUI launch
     * @param rootProject        String that matches the file-name of a model
     *                           folder of a Netbeans project (as found in
     *                           Functional Test Data)
     * @param associatedProjects String file-name of any associated projects
     *                           that the user wishes to open alongside the
     *                           root-project
     *
     * @return FuncProjectProperties object, which is the root-projects
     *         project-properties dialog
     */
    public FuncProjectProperties openProjects(JellyTestCase testCase,
            String rootProject,
            String... associatedProjects) {
        
        openProjectsNoPrjPropDialog(testCase, rootProject, associatedProjects);

        FuncProjectProperties fcPP = nuFuncProjectProperties();
        return fcPP;
    }

    /**
     * Open projects for a JellyTestCase class which has launched the Netbeans
     * GUI via the Jelly/NBModuleSuite capabilities.
     * <p>
     * The test-models are copied from the qa-functional/data (Functional Test
     * Data) folder each time the test-case class is executed.
     *
     * @param testCase           JellyTestCase object that does the
     *                           Jelly/NBModuleSuite GUI launch
     * @param rootProject        String that matches the file-name of a model
     *                           folder of a Netbeans project (as found in
     *                           Functional Test Data)
     * @param associatedProjects String file-name of any associated projects
     *                           that the user wishes to open alongside the
     *                           root-project
     *
     *
     */
    public void openProjectsNoPrjPropDialog(JellyTestCase testCase,
            String rootProject,
            String... associatedProjects) {

        // record the testcase this 'PrepareProject' is being run for
        this.testCase = testCase;
        // the dataDir is test/qa-functional/data and the source is test/qa-functional/src, need
        // to get this source directory

        // this class is being run out of the .../build/..../classes directory which in Netbeans
        // is rooted from the same place as source
        String thisClassRuntime = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath();

        // convert to the test/qa-functional/src directory
        // this structure appears to be fixed, but if it changes then things will
        // fail
        thisClassRuntime = thisClassRuntime.replace("/build/", "/");

        this.sourceDir4TestQaFunctional = thisClassRuntime.replace("/classes", "/src");

        int lenAssocProjs = associatedProjects.length;

        // plus + 1 is for the root-project over the associate-projects (latter being optional)
        String[] modelsToLoadArr = new String[lenAssocProjs + 1];

        if (associatedProjects.length
                > 0) {
            System.arraycopy(associatedProjects, 0, modelsToLoadArr, 0, lenAssocProjs);
        }
        modelsToLoadArr[lenAssocProjs] = rootProject;

        // load the associated projects first, and then the root-project
        modelList = TestModelCopy.copyTestModelIntoTempDirectory(testCase, modelsToLoadArr);

                new ActionNoBlock("Window|Projects", null).performMenu();
        resizeProjectTabSplit();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        try {
            for (File fileOfModel : modelList) {
                testCase.openProjects(fileOfModel.getAbsolutePath());
            }
        } catch (IOException ex) {
            throw new RuntimeException("IO exception", ex.getCause());
        }

    }

    /**
     * Close all the projects that have been opened for a test-case.
     */
    public void closeAllProjects() {
        this.testCase.closeOpenedProjects();
        FuncProjectProperties.closeHangingDialogs();
    }

    /**
     * Create and return a new different function-project-properties object to
     * process against.
     *
     * @return FuncProjectProperties object
     */
    public FuncProjectProperties nuFuncProjectProperties() {
        new ActionNoBlock("Window|Projects", null).performMenu();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        // open the project properties dialog
        ProjectsTabOperator pto = new ProjectsTabOperator();
        ProjectRootNode node_TT_console = pto.getProjectRootNode(getRootProjName());
        node_TT_console.select();
        node_TT_console.properties();

        FuncProjectProperties fcPP = new FuncProjectProperties(this);
        pauseMs(2000);

        return fcPP;
    }

    /**
     * Get the source-directory for the qa-functional source code.
     *
     * @return string of directory path
     */
    public String getSourceDir4TestQaFunctional() {
        return this.sourceDir4TestQaFunctional;
    }

    /**
     * Get for a project-name a File object for an directory-or-file item
     * expected to be in the project's root directory.
     *
     * @param projName the name of the project to look for
     * @param itemPath the directory-or-file item string path to the item, if
     *                 null or empty the project's directory is provided
     *
     * @return File of the item in the project's folder structure
     */
    public File getForProjNameFileItem(String projName, String itemPath) {
        File projPathFile = getFileForProjName(projName);

        if (itemPath == null || itemPath.isEmpty()) {
            return projPathFile;
        }
        return new File(projPathFile, itemPath);
    }

    /**
     * Go through the project-models open for this PrepareProjects object and
     * return its File directory location (root of the project).
     *
     * @param projName the name of the project to look for
     *
     * @return File of the project directory location
     */
    private File getFileForProjName(String projName) {
        for (File f : modelList) {
            if (f.getName().equals(projName)) {
                return f;
            }
        }
        fail(String.format("Failure to get for project: %s\n", projName));
        return null; // not reachable
    }

    /**
     * Get the root-project-name that was opened in the project-view
     *
     * @return string of root project name
     */
    public String getRootProjName() {
        return modelList.get(modelList.size() - 1).getName();
    }

    /**
     * Get the work-space dir for tests to use.
     *
     * @return File of the workspace directory
     */
    public File getWorkspaceDir() {
        return TestModelCopy.getWorkspaceDir();
    }

    /**
     * Get the models dir for tests to use.
     *
     * @return File of the models directory
     */
    public File getModelsDir() {
        return TestModelCopy.getModelsDir();
    }

    protected void resizeProjectTabSplit() {
        // make the testing IDE a smaller size window so as to be better 
        // presented on the monitor
        WindowOperator winOper = new WindowOperator(0);  // IDE window
        winOper.resize(1400, 759);
        if (this instanceof PrepareProjectSelf) {
            winOper.move(135, 113);
        }

        // prepare the two inner windows that will be a split on the screen
        new ActionNoBlock("Window|Projects", null).performMenu();
        new ActionNoBlock("Window|Output", null).performMenu();
        // allow to happen
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
        // there is now a split pane between the project and output views.
        // HOWEVER, Netbeans IDE does not use JSplitPane but uses internal
        // org.netbeans.core.window.view.ui.MultiSplitPane class. This
        // is not supported by Jelly Tools so basically is hard to test or GUI
        // manipulate.
        //
        // To achieve resizing of the Projects-view split panel we use AWT Robot
        // which allows a upper level process to change the size.
        ProjectsTabOperator ptOper = new ProjectsTabOperator();
        Point ptOperLocation = ptOper.getLocationOnScreen();

        int ptOperWidth = ptOper.getWidth();

        int rightSideProjView = ptOperLocation.x + ptOperWidth;

        OutputOperator outOper = new OutputOperator();
        Point outOperLocation = outOper.getLocationOnScreen();

        int leftSideOutputView = outOperLocation.x;

        // find the x-coordinate range between the projects-view and the
        // output-view, where the mid-point should be the split divider component
        // (another none-Swing).   
        int xSplitCoord = rightSideProjView + (leftSideOutputView - rightSideProjView) / 2;
        int ySplitCoord = ptOperLocation.y + 20;

        try {
            // do the action to resize the projects-view split-pane 
            Robot robot = new Robot();

            // select the split-pane-divider and activate
            robot.mouseMove(xSplitCoord, ySplitCoord);
            robot.mousePress(InputEvent.BUTTON1_MASK);

            // make the width 332 wide, then
            // drag/move mouse to reduce the size to ~332 wide
            int adjustSize = 332 - ptOperWidth;

            robot.mouseMove(xSplitCoord + adjustSize, ySplitCoord);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

        } catch (AWTException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
