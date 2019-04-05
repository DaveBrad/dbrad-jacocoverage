/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.tstenv;

import java.io.File;
import java.util.ArrayList;
import org.jtestdb.objecttestdata.OtdFileUtils;
import org.netbeans.jellytools.JellyTestCase;

/**
 * Class to copy a test-model from the test/qa-functional/data folder to a
 * temporary working folder for processing the models in a test environment.
 *
 * @author dbradley
 */
public class TestModelCopy {

    private static File workspaceDir;

    private static File modelsDir;

    /** Create an object to allow processing */
    public TestModelCopy() {
    }

    /**
     * Copy a test-model-folder (a Netbeans project folder) to a temporary
     * working folder.
     * <p>
     * When test-cases launch the Netbeans-module an independent Netbeans IDE
     * window is launch on the desktop. The IDE window will be empty and have no
     * projects open in the ProjectView; thus, a project needs to be opened.
     * <p>
     * Selecting a 'copied' test-model project-folder to a temporary working
     * directory allows the project to be manipulated and then trashed when
     * finished. So when the test is repeated the same fresh project is
     * reloaded/copied.
     * <p>
     * Netbeans works differently when "Test" of "Debug test" are used to invoke
     * test. The main differences is the "user.dir" is different, so cannot be
     * used as a test-executing-project knowledge point. So this method uses the
     * Java 'resources' architecture.
     *
     * @param jellyTestCase          the JellyTestCase Class where the
     *                               test-model resource folder is associated
     *                               with.
     * @param testModelFolderNameArr test-model-folder that needs to be copied
     *                               from the resource folder to a working
     *                               temporary folder.
     *
     * @return <code>File</code> of the temporary directory full-path of the
     *         test-model (to be used to Netbeans-IDE open-project on)
     */
    public static ArrayList<File> copyTestModelIntoTempDirectory(JellyTestCase jellyTestCase,
            String... testModelFolderNameArr) {

        // setup resources for the data-source of models and temporary 
        // working folder into place.
        File dataDir = jellyTestCase.getDataDir();
        String resourcesFolderPath = dataDir.getAbsolutePath();

        File tempDir = OtdFileUtils.createTempSystemTempDirectory("tstJacoco");

        // provide a workspace in the temp folder
        workspaceDir = new File(tempDir, "worksp");
        workspaceDir.mkdir();

        // create the temporary folder for models, then copy the models
        // into place
        modelsDir = new File(tempDir, "modelsDir");
        modelsDir.mkdir();

        System.out.printf("DbradJacoco testing tempDir:\n** %s\n", modelsDir.getAbsoluteFile());

        ArrayList<File> fileForModelArr = new ArrayList<>();

        // for each model to be loaded prepare to copy
        for (String testModelFolderName : testModelFolderNameArr) {

            File testModelFromResource = new File(resourcesFolderPath, testModelFolderName);

            // copy the model to the temporary directory for processing
            OtdFileUtils.copyDirectoryIntoDirectory(testModelFromResource, modelsDir);

            fileForModelArr.add(new File(modelsDir, testModelFolderName));
        }
        return fileForModelArr;
    }

    /**
     * Get the work-space dir for tests to use.
     *
     * @return File of the workspace directory
     */
    public static File getWorkspaceDir() {
        return workspaceDir;
    }

    /**
     * Get the models dir for tests to use.
     *
     * @return File of the models directory
     */
    public static File getModelsDir() {
        return modelsDir;
    }

}
