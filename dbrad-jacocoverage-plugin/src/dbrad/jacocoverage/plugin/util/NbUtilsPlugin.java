/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.util;

import dbrad.jacocoverage.analyzer.JavaClass;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_YES;
import dbrad.jacocoverage.plugin.anno.AbstractCoverageAnnotation;
import dbrad.jacocoverage.plugin.anno.CoverageAnnotation;
import dbrad.jacocoverage.plugin.anno.CoverageGlyphedAnnotation;
import dbrad.jacocoverage.plugin.anno.EditorCoverageStateEnum;
import dbrad.jacocofpm.config.Config;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.json.Json1ProjLevel;
import dbrad.jacocofpm.json.Json2ProjSrcCodeFolder;
import dbrad.jacocofpm.json.Json3PackageFormat;
import dbrad.jacocofpm.json.Json4Data;
import dbrad.jacocofpm.util.UtilsFileMgmt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.StyledDocument;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.modules.InstalledFileLocator;
import org.openide.nodes.Node;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;

/**
 * Some NetBeans related utilities.
 *
 * @author Graeme Ingleby (pre-2017)
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class NbUtilsPlugin {

    /**
     * Close a NetBeans console tab.
     *
     * @param tabName the name on the tab.
     *
     * @throws IOException if an I/O error occurs.
     */
    public static void closeConsoleTab(String tabName)
            throws IOException {
        IOProvider.getDefault().getIO(tabName, false).closeInputOutput();
    }

    /**
     * Color (in editor) all the document representing the Java class.
     *
     * @param ideJacocoConfig IDE configuration for a project
     * @param jclass          the Java class informations and coverage data.
     * @param multiLnInst     enable coloring of multi-lines instructions.
     * @param srcDir          the folder containing Java sources. Needed only if
     *                        {@code multiLnInst} is true, otherwise you can use
     *                        {@code null}.
     */
    @SuppressWarnings({"AssignmentToForLoopParameter", "UnnecessaryLabelOnBreakStatement"})
    public static void colorDoc(IdeProjectJacocoverageConfig ideJacocoConfig, JavaClass jclass, boolean multiLnInst, File srcDir) {

        String classResource = jclass.getPackageName() + jclass.getClassName();

        String prjId = ideJacocoConfig.getNbProjectId();

        int theme = Config.getCfgTheme();
        FIND_JAVA_FO:
        for (FileObject curRoot : GlobalPathRegistry.getDefault().getSourceRoots()) {
            FileObject fileObject = curRoot.getFileObject(classResource);

            if (fileObject != null && "java".equalsIgnoreCase(fileObject.getExt())) {
                try {
                    DataObject dataObject = DataObject.find(fileObject);
                    Node node = dataObject.getNodeDelegate();

                    EditorCookie editorCookie = node.getLookup().lookup(EditorCookie.class);

                    Map<Integer, dbrad.jacocoverage.analyzer.CoverageStateEnum> coverage = jclass.getCoverage();
                    Map<Integer, String> coverageDesc = jclass.getCoverageDesc();

                    if (editorCookie != null) {
                        StyledDocument doc = editorCookie.openDocument();

                        if (doc != null) {
                            int startLine = 0;
                            int endLine = NbDocument.findLineNumber(doc, doc.getLength());

                            Line.Set lineset = editorCookie.getLineSet();
                            for (int covIdx : coverage.keySet()) {
                                if (covIdx >= startLine && covIdx <= endLine) {
                                    Line line = lineset.getOriginal(covIdx);
                                    EditorCoverageStateEnum coverageState;
                                    switch (coverage.get(covIdx)) {
                                        case COVERED:
                                            coverageState = EditorCoverageStateEnum.COVERED;
                                            break;
                                        case NOT_COVERED:
                                            coverageState = EditorCoverageStateEnum.NOT_COVERED;
                                            break;
                                        case PARTIALLY_COVERED:
                                            coverageState = EditorCoverageStateEnum.PARTIALLY_COVERED;
                                            break;
                                        default:
                                            coverageState = EditorCoverageStateEnum.COVERED;
                                    }
                                    AbstractCoverageAnnotation annotation;

                                    if (coverageDesc.containsKey(covIdx)) {
                                        annotation = new CoverageGlyphedAnnotation(
                                                coverageState,
                                                prjId,
                                                jclass.getPackageName() + jclass.getClassName(),
                                                covIdx,
                                                coverageDesc.get(covIdx),
                                                theme);
                                    } else {
                                        annotation = new CoverageAnnotation(
                                                coverageState,
                                                prjId,
                                                jclass.getPackageName() + jclass.getClassName(),
                                                covIdx,
                                                theme);
                                    }
                                    annotation.attach(line);
                                    line.addPropertyChangeListener(annotation);
                                }
                            }
                            if (multiLnInst) {
                                // Patch by GWI
                                //  old: File javafile = new File(srcDir, jclass.getPackageName() + jclass.getClassName());
                                //  new: File javafile = new File(fileObject.getPath());
                                File javafile = new File(fileObject.getPath());
                                List<String> javalines = org.apache.commons.io.FileUtils.readLines(javafile, "utf-8");
                                int nblines = javalines.size();

                                for (int lineIdx = 0; lineIdx < nblines; lineIdx++) {
                                    boolean isCovered = coverage.containsKey(lineIdx);
                                    boolean isCoveredDesc = coverageDesc.containsKey(lineIdx);

                                    if ((isCovered || isCoveredDesc)
                                            && (lineIdx + 1 < nblines)
                                            && (!coverage.containsKey(lineIdx + 1) && !coverageDesc.containsKey(lineIdx + 1))
                                            && (!UtilsFileMgmt.isIntructionFinished(javalines.get(lineIdx)))) {
                                        EditorCoverageStateEnum coverageState;
                                        switch (coverage.get(lineIdx)) {
                                            case COVERED:
                                                coverageState = EditorCoverageStateEnum.COVERED;
                                                break;
                                            case NOT_COVERED:
                                                coverageState = EditorCoverageStateEnum.NOT_COVERED;
                                                break;
                                            case PARTIALLY_COVERED:
                                                coverageState = EditorCoverageStateEnum.PARTIALLY_COVERED;
                                                break;
                                            default:
                                                coverageState = EditorCoverageStateEnum.COVERED;
                                        }
                                        coverage.put(lineIdx + 1, coverage.get(lineIdx));
                                        AbstractCoverageAnnotation annotation = new CoverageAnnotation(
                                                coverageState,
                                                prjId,
                                                jclass.getPackageName() + jclass.getClassName(),
                                                lineIdx + 1,
                                                theme);
                                        Line line = lineset.getOriginal(lineIdx + 1);
                                        annotation.attach(line);
                                        line.addPropertyChangeListener(annotation);
                                    }
                                }
                            }
                        }
                        break FIND_JAVA_FO;
                    }
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return;
    }

    /**
     * Get the JaCoCo-Agent JAR file that is registered in the IDE.
     *
     * @return the JaCoCo-Agent JAR.
     */
    public static File getJacocoAgentJar() {
        if (Config.isCfgUseCustomJacocoJar()) {
            File jacocoagent = new File(Config.getCfgCustomJacocoJarPath());
            if (jacocoagent.exists()) {
                return jacocoagent;
            }
        }
        return InstalledFileLocator.getDefault().locate("modules/ext/jacocoagent.jar", "dbrad.jacoco.agent.lib", false);
    }

    /**
     * Retrieve the list of Java packages of a given project. Each element is a
     * fully qualified package name, e.g.  <code>foo</code>, <code>foo.bar</code>
     * and <code>foo.bar.too</code>. Elements are separated with a given
     * separator string.
     *
     * @param project   the project to list Java packages.
     * @param separator the separator string.
     * @param prefix    a prefix to append to the end of each package name (can
     *                  be empty).
     *
     * @return a list of Java package names.
     */
    public static String getProjectIncludesAsStr(Project project, String separator, String prefix) {

        IdeProjectJacocoverageConfig prjCfgRoot;

        try {
            prjCfgRoot = IdeProjectJacocoverageConfig.reuseForProject(project);

            Json1ProjLevel json1ProjData = prjCfgRoot.jsonMapData.getProjectIncludePackages();

            // key1: source-code folder
            // key2: the packager-filter setting
            ArrayList<String> listOfInclPkgsArr = new ArrayList<>();

            for (String projName : json1ProjData.keySet()) {
                Json2ProjSrcCodeFolder json2SrcCodeFolderHash = json1ProjData.get(projName);

                for (String srcCodeFolder : json2SrcCodeFolderHash.keySet()) {
                    Json3PackageFormat json3PkgFormatHash = json2SrcCodeFolderHash.get(srcCodeFolder);

                    for (String packageFormat : json3PkgFormatHash.keySet()) {
                        Json4Data json4Data = json3PkgFormatHash.get(packageFormat);

                        // the package needs to be included if conditoions are
                        // met
                        // 1) the on-code-folder for the source-code-folder is on (not screened out)
                        // 2) the package-format is cover-yes
                        // 3) the package-format is known to have java-files
                        //
                        // this data is from the Json file as it is the method to
                        // share data between d8fferent threads/processes of this
                        // application
                        if (json4Data.isOn() && json4Data.getPfCvr() == COVER_YES && json4Data.isHasJava()) {
                            // an empty packageFormat is an 'all' "*" condition for the includes
                            if (packageFormat.isEmpty()) {
                                return "*";
                            }
                            // avoid duplicates, which is possible for multiple projects that
                            // have like packaging patterns 
                            String storeItem = packageFormat + ".";

                            if (!listOfInclPkgsArr.contains(storeItem)) {
                                listOfInclPkgsArr.add(storeItem);
                            }
                        }
                    }
                }
            }

            // in lambda form would be the following code, HOWEVER, the 'return'
            // when the first packageFormat.isEmpty() causes a compile error.
            // SO LONG FORM is necessary OTHERWISE THERE IS A LOT OF PROCESSING
            // THAT DOES NOT NEED TO BE USED
            //
            //json1ProjData.forEach((projName, json2SrcCodeFolderHash) -> {
            //
            //    json2SrcCodeFolderHash.forEach((srcCodeFolder, json3PkgFormatHash) -> {
            //
            //        json3PkgFormatHash.forEach((packageFormat, json4Data) -> {
            //            // the package needs to be included if conditoions are
            //            // met
            //            // 1) the on-code-folder for the source-code-folder is on (not screened out)
            //            // 2) the package-format is cover-yes
            //            // 3) the package-format is known to have java-files
            //            //
            //            // this data is from the Json file as it is the method to
            //            // share data between d8fferent threads/processes of this
            //            // application
            //            if (json4Data.isOn() && json4Data.getPfCvr() == COVER_YES && json4Data.isHasJava()) {
            //                // an empty packageFormat is an 'all' "*" condition for the includes
            //                if (packageFormat.isEmpty()) {
            //                    return "*";
            //
            //                }
            //                // avoid duplicates, which is possible for multiple projects that
            //                // have like packaging patterns 
            //                String storeItem = packageFormat + ".";
            //
            //                if (!listOfInclPkgsArr.contains(storeItem)) {
            //                    listOfInclPkgsArr.add(storeItem);
            //                }
            //            }
            //        });
            //    });
            //});
            // sort the order of the package-strings
            Collections.sort(listOfInclPkgsArr);

            // now remove sub--package-format patterns.
            // org.                super of the following items so the under-packages make no
            //                        sense to have a processing too
            // org.abc.            sub-- of 'org.'
            // org.abc.def.
            ArrayList<Integer> indexToRemoveArr = new ArrayList<>();

            String lastPackage = null;
            for (int i = 0;
                    i < listOfInclPkgsArr.size();
                    i++) {
                if (lastPackage != null) {
                    if (listOfInclPkgsArr.get(i).startsWith(lastPackage)) {
                        // this will remove duplicate and 
                        indexToRemoveArr.add(i);
                        continue;
                    }
                }
                lastPackage = listOfInclPkgsArr.get(i);
            }
            for (int j = indexToRemoveArr.size() - 1;
                    j > -1; j--) {
                // indexToRemoveArr is of Integer and not int, so do the conversion
                // manually for the remove(int) to work
                int elementToRemove = indexToRemoveArr.get(j);
                listOfInclPkgsArr.remove(elementToRemove);
            }
            String inclPkgStr = "";
            for (String pkgStrDot : listOfInclPkgsArr) {
                inclPkgStr = String.format("%s%s%s*", inclPkgStr, separator, pkgStrDot);
            }
            // strip the leading 'separator' 

            if (!inclPkgStr.isEmpty()) {
                if (inclPkgStr.startsWith(":.")) {
                    inclPkgStr = inclPkgStr.substring(1);
                }
                //99 need a better design
                String inclPkgStrTrimed = inclPkgStr.substring(1);

                if (!inclPkgStrTrimed.startsWith("*")) {
                    return inclPkgStrTrimed;
                }
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "*";
    }

    /**
     * Get the excludes settings as a string that is usable for the Jacocoagent
     * excludes argument.
     *
     * @param project the project to get the excludes settings from
     *
     * @return string of exclude patterns for the Jacocoagent argument, null if
     *         no exclude patterns
     */
    public static String getExcludesForJacocoagentArgument(Project project) {
        ArrayList<String> listArr = getExcludesPatternsAsArrayList(project);

        if (listArr.isEmpty()) {
            return null;
        }
        // convert to a string with * wildcard for the Jacocoagent 
        String exclStr = "";
        for (String item : listArr) {
            exclStr = String.format("%s:%s*", exclStr, item);
        }
        // remove the leading ':'
        return exclStr.substring(1);
    }

    /**
     * Get the excludes patterns in an array list without the wildcard character
     * for easier pattern matching.
     *
     * @param project the project to get the excludes settings from
     *
     * @return array of exclude patterns with no wildcard character
     */
    public static ArrayList<String> getExcludesPatternsAsArrayList(Project project) {
        IdeProjectJacocoverageConfig prjCfg;

        ArrayList<String> exclStrList = new ArrayList<>(10);
        try {
            prjCfg = IdeProjectJacocoverageConfig.reuseForProject(project);

            if (prjCfg.isExcludedOrgTestngSet()) {
                exclStrList.add("org.testng.");
            }
            if (prjCfg.isExcludedOrgJunitSet()) {
                exclStrList.add("org.junit.");
            }
            if (prjCfg.isExcludedJunitExtensionSet()) {
                exclStrList.add("junit.");
            }
            // need to get the settings from the JSON table
            HashMap<String, Boolean> excludeDataHash = prjCfg.jsonMapData.getExcludePackages();

            // the exclude packages are true or false as to be set, so only 
            // collect the exclude items that are set
            for (String packageStr : excludeDataHash.keySet()) {
                // is set exclude true
                if (excludeDataHash.get(packageStr)) {
                    // remove the '*' ending character
                    exclStrList.add(packageStr.substring(0, packageStr.length() - 1));
                }
            }
            Collections.sort(exclStrList);

            // The sorted array for exclude-string-list will allow the
            // processing of the strings to remove override conditions
            // eg. unit.  overrides   unit.isolate.
            String patternThatOverrides = null;

            // the array list for all the valid exclude strings
            ArrayList<String> excludeStringList = new ArrayList<>();

            // go through the list and remove the override conditions
            for (String pckExclStr : exclStrList) {
                if (patternThatOverrides == null) {
                    patternThatOverrides = pckExclStr;
                    excludeStringList.add(pckExclStr);
                    continue;
                }
                // pckExclStr vs  patternThatOverrides
                // unit.isolate.  vs unit.  the former is overridden
                if (pckExclStr.startsWith(patternThatOverrides)) {
                    // overridden
                    continue;
                }
                excludeStringList.add(pckExclStr);

                // no sub-package patterns found, so set ad the next match
                patternThatOverrides = pckExclStr;
            }
            return excludeStringList;

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     * Get the Netbeans project for the file-object from the open projects of
     * the IDE.
     *
     * @param nbFileObj a Netbeans FileObject item (file or folder) that is
     *                  within an openNB-Project
     *
     * @return Project instance of the nbFileObj's open project
     */
    public static Project getNbProjectForNbFileObject(FileObject nbFileObj) {
        Project projOwner = FileOwnerQuery.getOwner(nbFileObj);

        // find the project by asolute path comparison
        String selectedFileFullPath = convertPathToFwdSlash(nbFileObj.getPath());

        // get the open projects to find what project maps to the
        // selected FileObject
        Project[] openProjectArr = OpenProjects.getDefault().getOpenProjects();

        // this is a brute force way to do this, but netbeans API's weren't doing
        // it
        Project foundProj = null;
        int countOfCharactersMatching = -1;

        for (Project nbProj : openProjectArr) {
            String projectsFullPath = convertPathToFwdSlash(nbProj.getProjectDirectory().getPath()) + "/";

            // a project could have the same name at the beginning of the file
            // name, so have to be more exact in determining a fileObjects
            // parent Project.
            if (selectedFileFullPath.startsWith(projectsFullPath)) {
                int len = projectsFullPath.length();
                if (len > countOfCharactersMatching) {
                    // the length would get the cases of project paths that have a
                    // /user1/suitename/  vs /user1/suitename/suitesubproject/
                    // for a file  /user1/suitename/suitesubproject/src/package1/package2/file.java

                    countOfCharactersMatching = len;
                    foundProj = nbProj;
                }
            }
        }
        return foundProj;
    }

    /**
     * Get the file's root source-code for both 'src' or 'test' file path (with
     * lagging '/' to indicate a folder/directory).
     *
     * @param nbProject Netbeans project to check with
     * @param nbFileObj Netbeans FileObject to check in test-source directory
     *
     * @return true if a test-source item, false if otherwise (source directory)
     */
    public static String getFilesSrcCodeFolderPath(Project nbProject, FileObject nbFileObj) {
        String contextFileStr = convertPathToFwdSlash(nbFileObj.getPath());

        // Get project source folders (source and test).
        Sources srcs = ProjectUtils.getSources(nbProject);

        // this provides both source-code and test-source-code directories in the
        // order that Netbeans processes them.
        SourceGroup[] srcsArr = srcs.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);

        for (SourceGroup srcGrp : srcsArr) {
            // get the property key for the source-code directory
            // '${src.dir} is a pointer into the project.properties file for the directory to the root
            String srcPropertyKey = srcGrp.getName();
            srcPropertyKey = srcPropertyKey.substring(2, srcPropertyKey.length() - 1);
            boolean isTestOrSrc = srcPropertyKey.startsWith("test.")
                    || srcPropertyKey.startsWith("src.");

            if (!isTestOrSrc) {
                continue;
            }
            // is a test directory, so if the context file then is it test
            FileObject srcCodeTestFileObjDir = srcGrp.getRootFolder();
            String srcCodeTestFileObjDirStr = convertPathToFwdSlash(srcCodeTestFileObjDir.getPath());

            if (contextFileStr.startsWith(srcCodeTestFileObjDirStr)) {
                return srcCodeTestFileObjDirStr + "/";
            }
        }
        return null;
    }

    /**
     * Is the FileObject within a test-source directory of the Netbeans project.
     *
     * @param nbProject Netbeans project to check with
     * @param nbFileObj Netbeans FileObject to check in test-source directory
     *
     * @return true if a test-source item, false if otherwise (source directory)
     */
    public static boolean isFileObjectInTestDir(Project nbProject, FileObject nbFileObj) {
        String contextFileStr = convertPathToFwdSlash(nbFileObj.getPath());

        // Get project source folders (source and test).
        Sources srcs = ProjectUtils.getSources(nbProject);

        // this provides both source-code and test-source-code directories in the
        // order that Netbeans processes them.
        SourceGroup[] srcsArr = srcs.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);

        for (SourceGroup srcGrp : srcsArr) {
            // get the property key for the source-code directory
            // '${src.dir} is a pointer into the project.properties file for the directory to the root
            String srcPropertyKey = srcGrp.getName();
            srcPropertyKey = srcPropertyKey.substring(2, srcPropertyKey.length() - 1);
            boolean isTest = srcPropertyKey.startsWith("test.");

            if (!isTest) {
                continue;
            }
            // is a test directory, so if the context file then is it test
            FileObject srcCodeTestFileObjDir = srcGrp.getRootFolder();
            String srcCodeTestFileObjDirStr = convertPathToFwdSlash(srcCodeTestFileObjDir.getPath());

            if (contextFileStr.startsWith(srcCodeTestFileObjDirStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert a file path to a forward-slash format only.
     *
     * @param path converted to forward-slash format
     *
     * @return
     */
    public static String convertPathToFwdSlash(String path) {
        return path.replaceAll("\\\\", "/");
    }

    private NbUtilsPlugin() {
    }
}
