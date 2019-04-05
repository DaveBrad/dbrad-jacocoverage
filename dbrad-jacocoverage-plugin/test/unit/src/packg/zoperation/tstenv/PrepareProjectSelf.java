/* Copyright (c) 2018 dbradley. All rights reserved. */
package packg.zoperation.tstenv;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import org.jtestdb.objecttestdata.OtdFileUtils;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.openide.util.Exceptions;
import packg.appfunc.FuncProjectProperties;

/**
 * Class that is a PrepareProject extension with more specific requirements to
 * process the dbrad-jacocoverage-suite program and use itself to get code
 * coverage results.
 *
 * @author dbradley
 */
public class PrepareProjectSelf extends PrepareProject {

    /** temporary directory to source code structure into */
    public static String tmpDirectory4Copy = System.getProperty("user.home") + "/nbmselftest";
    public static String tmpDirectory4CopyReports = tmpDirectory4Copy + "/rpts";

    /**
     * source-code directory for the dbrad-jacocoverage project to be
     * determined. (Copy from here).
     */
    static String sourceCodeStructureDir;

    /**
     * Create an instance for testing the dbrad-jacocoverage program.
     */
    public PrepareProjectSelf() {

        System.out.printf("===== temp directory being used =====\n%s\n"
                + "=====================================\n", tmpDirectory4Copy);

        File tmpDir = new File(tmpDirectory4Copy);
        if (!tmpDir.isDirectory()) {
            tmpDir.mkdirs();
        }
        File tmpDirRpts = new File(tmpDirectory4CopyReports);
        if (!tmpDirRpts.isDirectory()) {
            tmpDirRpts.mkdirs();
        }
    }

    /**
     * Not supported version, use
     * <code>openProjects(JellyTestCase testCase)</code> instead.
     *
     * @param testCase
     * @param principleProject
     * @param associatedProjects
     *
     * @return
     */
    @Override
    public FuncProjectProperties openProjects(JellyTestCase testCase,
            String principleProject,
            String... associatedProjects) {

        throw new RuntimeException("Not supported access use alternative, openProjects(testCase);");
    }

    /**
     *
     * @param testCase
     */
    public void openProjectsSelf(JellyTestCase testCase,
            String suiteProject,
            String principleProject,
            String... associatedProjects) {
        // record the testcase this 'PrepareProjectSelf' is being run for
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

        // copy the dbrad-jacocoverage projects from this running IDE environment
        // to the test temporary directory in preparation for self testing
        File suiteInnerDir = copySelfIntoWorkingDirectory();

        // organize the projects for self appropriately
        int lenAssocProjs = associatedProjects.length;

        // plus + 1 is for the root-project over the associate-projects (latter being optional)
        String[] modelsToLoadArr = new String[lenAssocProjs + 1];

        if (associatedProjects.length > 0) {
            System.arraycopy(associatedProjects, 0, modelsToLoadArr, 0, lenAssocProjs);
        }
        // work the associate projects first
        int i = 0;
        for (String assocPrjStr : associatedProjects) {
            modelsToLoadArr[i] = assocPrjStr;
            i++;
        }
        modelsToLoadArr[lenAssocProjs] = principleProject;
        modelList = new ArrayList<>();

        for (String prjName : modelsToLoadArr) {
            modelList.add(new File(suiteInnerDir, prjName));
        }

        resizeProjectTabSplit();

        try {
//            for (File fileOfModel : modelList) {
//                testCase.openProjects(fileOfModel.getAbsolutePath());
//            }
            // open the suite project
            //            File suiteModel = new File(suiteInnerDir); //, "dbrad-jacocoverage-suite");
            testCase.openProjects(suiteInnerDir.getAbsolutePath());

        } catch (IOException ex) {
            throw new RuntimeException("IO exception", ex.getCause());
        }
        // need to open the required projects of the suite    JtabbedPaneOperator
        ProjectsTabOperator pto = new ProjectsTabOperator();

        Node suiteNode = pto.getProjectRootNode(suiteProject);
        suiteNode.callPopup();

        TestBasicUtils.pauseMs(500);
        JPopupMenuOperator popup1 = new JPopupMenuOperator();
        //
        // the following does not work due to Netbeans IDE using a none compliant
        // form of the popup menu
        //popup1.pushMenu(new String[]{"Open Required Projects", "Open All Projects"}); 
        //
        // so an acutal working arrangement is coded by explicitly getting the
        // second popup
        JMenuItem menuItem1 = popup1.pushMenu("Open Required Projects");
        JMenuItemOperator menuItem1Oper = new JMenuItemOperator(menuItem1);

        // structure of menu is known, and is unlikely to change
        MenuElement[] sub2ndMenu = menuItem1Oper.getSubElements();
        JPopupMenuOperator popup2 = new JPopupMenuOperator((JPopupMenu) sub2ndMenu[0]);
        popup2.pushMenu("Open All Projects");

        TestBasicUtils.pauseMs(2000);
    }

    /**
     * Copy the dbrad-jacocoverage-suite to a temporary working directory
     * without the build, var and .git directories (those directories that are
     * transient). This provides a current copy of the source for the purpose of
     * testing self.
     *
     * @return File of the suite directory in the temporary testing structure
     */
    private File copySelfIntoWorkingDirectory() {
        String thisClassRuntimePath = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath();

        // 
        String suiteString = "-suite/";
        int suiteBasePathIdx = thisClassRuntimePath.indexOf(suiteString);

        String projectBaseOfSuitePath = thisClassRuntimePath.substring(0, suiteBasePathIdx); //  + suiteString;

        int baseDbradCoverageProjPathIdx = projectBaseOfSuitePath.lastIndexOf("/");

        sourceCodeStructureDir = projectBaseOfSuitePath.substring(0, baseDbradCoverageProjPathIdx);

        File sourceCodeStructureDirFile = new File(sourceCodeStructureDir);

        // need to copy to the same top level structure.
        String lastDirName = sourceCodeStructureDirFile.getName();
        File tmpDirectory4CopyFile = new File(tmpDirectory4Copy + "/" + lastDirName);

        if (tmpDirectory4CopyFile.isDirectory()) {

            OtdFileUtils.rmDirAll(tmpDirectory4CopyFile);

            // using org.apache.commons.io.FileUtils would be better, but Netbeans 8.2
            // and Jemmy have a 1.4 version and this conflicts with any other attempt
            // at loading a newer version (testing will stop with UndefineClass exception
            //
            //            try {
            //                // the directory exists and it would be a good idea to trash its
            //                // contents so there is no chance of picking up old files
            //                org.apache.commons.io.FileUtils.deleteDirectory(tmpDirectory4CopyFile);
            //            } catch (IOException ex) {
            //                Exceptions.printStackTrace(ex);
            //            }
            //
            // give a little time to allow things to settle and the delete
            // above complete
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        // create a new directory ready for a clean copy
        tmpDirectory4CopyFile.mkdirs();

        // 
        FileFilter ff = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // need to exclude any thing that is /build/ or /.git/ as these contain 
                // large storage that is waste CPU to copy
                String fullpath = pathname.getAbsolutePath();

                // assuming paths will be lower-case as this is within a
                // Netbeans IDE enviroment (which is LC)
                //
                // the following directories will not be copied so as to
                // reduce copying, avoid hanging IDE cache, avoid incidental
                // git repository interactions
                // 1) build directories (contains classes, JARs and as such may
                //    have potential mismatches
                // 2) .git,basically under test the git interaction does not apply
                // 3) var directory has a cache which could cause issues as a 
                //    reused condition (a test IDE will need to origin source-code
                //    but all other stuff is a potential collision)
                if (fullpath.contains("build")) {
                    fullpath = fullpath.replaceAll("\\\\", "/");

                    // by doing this it is at a directory level, so all
                    // sub-file/directories are ignored too
                    return !fullpath.endsWith("/build");

                } else if (fullpath.contains(".git")) {
                    fullpath = fullpath.replaceAll("\\\\", "/");

                    if (fullpath.endsWith("/.git")) {
                        // by doing this it is at a directory level, so all
                        // sub-file/directories are ignored too
                        return false;
                    }
                    // also does /.gitignore file too
                    return !fullpath.contains("/.git");

                } else if (fullpath.contains("var")) {
                    fullpath = fullpath.replaceAll("\\\\", "/");

                    if (fullpath.endsWith("/var")) {
                        // by doing this it is at a directory level, so all
                        // sub-file/directories are ignored too
                        return false;
                    }
                }
                // not any of the filters
                return true;
            }
        };

        OtdFileUtils.copyDirectoryIntoDirectory(sourceCodeStructureDirFile, new File(tmpDirectory4Copy), ff);

        // using org.apache.commons.io.FileUtils would be better, but Netbeans 8.2
        // and Jemmy have a 1.4 version and this conflicts with any other attempt
        // at loading a newer version (testing will stop with UndefineClass exception
        //        try {
        //            org.apache.commons.io.FileUtils.copyDirectory(sourceCodeStructureDirFile, tmpDirectory4CopyFile, ff, true);
        //            
        //            
        //        } catch (IOException ex) {
        //            Exceptions.printStackTrace(ex);
        //        }
        return new File(tmpDirectory4CopyFile, "dbrad-jacocoverage-suite");
    }
}
