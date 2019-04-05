/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocofpm.config;

import dbrad.jacocofpm.mgr.com.FileClassesDir;
import dbrad.jacocofpm.util.NBProjectTypeEnum;
import dbrad.jacocofpm.util.NBUtils;
import static dbrad.jacocofpm.util.UtilsFileMgmt.checkRegex;
import static dbrad.jacocofpm.util.UtilsFileMgmt.getGroupsFromRegex;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.SubprojectProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author dbradley
 */
public class IdeConfigOfProject {

    private Project nbProject;
    private final String nbProjectDirectory;

    private final boolean isNbModuleBool;

    /**
     * Regular expression to recognize "${key}" patterns in Properties files
     * used by NetBeans projects.
     */
    private static final String PA_NBPROPKEY_SHORTCUT = "\\$\\{([^\\}]+)\\}";

    /**
     * Compiled regular expression to recognize "${key}" patterns in Properties
     * files used by NetBeans projects.
     */
    private static final Pattern CPA_NBPROPKEY_SHORTCUT = Pattern.compile(PA_NBPROPKEY_SHORTCUT);

    /**
     * A general cache for IDE project configuration instances that may use
     * dbrad-jacocoverage.
     * <p>
     * A running IDE instance typically has more than one project with each
     * having its own configuration relationship. The relationship is static for
     * the running IDE instance.
     * <p>
     * The following table determines and stores the configuration 'File'
     * relationship to a selected project (selected is a project within context
     * and any menu actions).
     * <pre>
     * IDE
     *      IDE preferences  (global IDE jacoco settings)
     *     + Project1
     *     + Project2
     *       + dbrad-jacocoverage per project override
     *         + per project jacoco settings
     *           + root-project
     *             + table
     *               + src-code-folder1
     *               + [src-code-folder2]
     *               + test-src-code-folder1
     *               + [test-src-code-folder2]
     *          [ + additional-project (sibling Project1, Project3, Project[n]) ]
     *          [ + exclude setting ]
     *
     *     + Project3
     *     + Project[n]
     *
     *
     * </pre>
     * <pre>
     * key1: IDE's project-file location 'File'
     * key2: type of class for an Ide-.......... object
     * element: an IDe configuration of project data holder object
     * </pre>
     */
    private static Map<File, HashMap<Class<?>, IdeConfigOfProject>> allIdeProjJacocoConfigMap
            = Collections.synchronizedMap(new HashMap<>(8));

    /**
     * Clear the IDE configuration data for all the IDEs projects that are
     * associated.
     */
    public static void clearIdeConfigs() {
        allIdeProjJacocoConfigMap.clear();
    }

    /**
     * Hidden like a singleton as there is only one of these per IDE project.
     *
     * @param projectsConfigFileLocation the location of the projects
     *                                   configuration file (project.properties)
     *
     * @throws java.io.IOException
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected IdeConfigOfProject(File projectsConfigFileLocation) throws IOException {
        //
        if (allIdeProjJacocoConfigMap == null) {
            allIdeProjJacocoConfigMap = Collections.synchronizedMap(new HashMap<>(8));
        }
        //
        Project[] projectArr = OpenProjects.getDefault().getOpenProjects();

        String projectWithPath = projectsConfigFileLocation.getAbsolutePath().replaceAll("\\\\", "/");
        for (Project proj : projectArr) {
            String projPath = proj.getProjectDirectory().getPath().replaceAll("\\\\", "/");

            if (projPath.equals(projectWithPath)) {
                this.nbProject = proj;
                break;
            }
        }
        // set if this an NBModule
        this.isNbModuleBool
                = NBUtils.isProjectSupported(this.nbProject, NBProjectTypeEnum.NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE);

        // get the project directory which all files will root too
        this.nbProjectDirectory = this.getNbProjectDirPath() + File.separator;

        // store for reuse
        HashMap<Class<?>, IdeConfigOfProject> ideClassConfigHash;

        if (!allIdeProjJacocoConfigMap.containsKey(projectsConfigFileLocation)) {
            // do not have the store for the project-file so need to add it
            ideClassConfigHash = new HashMap<>(4);
        } else {
            // reuse existing
            ideClassConfigHash = allIdeProjJacocoConfigMap.get(projectsConfigFileLocation);
        }
        // there is only one type of sub-IdeConfigOfProject kind per project 
        // based on its sub-class-type
        Class<?> clzMe = this.getClass();
        ideClassConfigHash.put(clzMe, this);

        allIdeProjJacocoConfigMap.put(projectsConfigFileLocation, ideClassConfigHash);
    }

    /**
     * Get selected project's configuration manager.
     *
     * @param projectsConfigFileLocation the location of the projects
     *                                   configuration file (project.properties)
     *
     * @return IDE project's configuration manager object.
     *
     * @throws IOException if cannot load configuration.
     */
    public static IdeConfigOfProject forFile(File projectsConfigFileLocation)
            throws IOException {

        IdeConfigOfProject pc = getExistingForIdeConfigOfProjSubClass(projectsConfigFileLocation, IdeConfigOfProject.class);

        // new or reuse
        if (pc == null) {
            // create a new project's configuration manager
            pc = new IdeConfigOfProject(projectsConfigFileLocation);
        }
        return pc;
    }

    /**
     * Get an existing, previously identified, <code>IdeConfigOfProject</code>
     * instance for the File location of the
     * <code>projectsConfigFileLocation</code> parameter.
     *
     * @param projectsConfigFileLocation the File location of the project to be
     *                                   processed
     * @param subClzOfIdeCfgOfProject    the sub-class type of a
     *                                   IdeConfigOfProject to process too
     *
     * @return IDE configuration of project for an existing sub-class
     */
    final protected static IdeConfigOfProject getExistingForIdeConfigOfProjSubClass(File projectsConfigFileLocation,
            Class<? extends IdeConfigOfProject> subClzOfIdeCfgOfProject) {

        // determine if there is an existing IdeConfigOfProject and reuse the object.
        // basically there is only ONE project and thus only ONE
        // IdeConfigOfProject/sub-class-IdeCondfigOfProject-extension (There may be many extension type objects
        // each having a slightly different use.)
        //
        if (!allIdeProjJacocoConfigMap.containsKey(projectsConfigFileLocation)) {
            return null;
        }
        // have at least one object, but is it of the matching class/sub-class type
        HashMap<Class<?>, IdeConfigOfProject> ideClassTypeHash
                = allIdeProjJacocoConfigMap.get(projectsConfigFileLocation);

        if (!ideClassTypeHash.containsKey(subClzOfIdeCfgOfProject)) {
            return null;
        }
        // have an object of the class-type so return it for reuse
        return ideClassTypeHash.get(subClzOfIdeCfgOfProject);
    }

    /**
     * Get all the source-code files (source and test directories) along with:
     * <ul>
     * <li>folder names,
     * <li>associated classes-directory,
     * <li>sub-directories and files structure,
     * <li>source or test directory
     * </ul>
     * <p>
     * Data is constructed each time it is called as a projects data may be
     * manipulated independently of dbrad-jacocoverage runtime.
     *
     * @return ArrayList of all the source files
     */
    public ArrayList<NbFileSrcCodePair> getAllSourceCodeFiles() {
        // DATA IS COLLECTED AT TIME OF CALL as the data may change
        // via other activities

        return getSourceAndTestCodeDirs();
    }

    /**
     * Get the source-code and test-source-code directories/folders for Java
     * projects (none NBModule).
     *
     * @return an array list of ALL file source-code(src and test) with classes
     *         directory
     */
    private ArrayList<NbFileSrcCodePair> getJavaProjectSrcCodePairs() {
        ArrayList<NbFileSrcCodePair> srcCodeDirPairList = new ArrayList<>();

        // all sources will be associated with classes, so define the File for this
        String srcClassesDir = getProperty("build.classes.dir");
        String absPath = nbProjectDirectory + srcClassesDir + File.separator;

        FileClassesDir buildClassDirFile = new FileClassesDir(absPath);

        // the test-code 
        String testClassesDir = getProperty("build.test.classes.dir");
        absPath = nbProjectDirectory + testClassesDir + File.separator;

        FileClassesDir buildTestClassDirFile = new FileClassesDir(absPath);

        //
        // Get project source folders (source and test).
        Sources srcs = ProjectUtils.getSources(this.nbProject);

        // this provides both source-code and test-source-code directories in the
        // order that Netbeans processes them.
        SourceGroup[] srcsArr = srcs.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);

        for (SourceGroup srcGrp : srcsArr) {
            // get the property key for the source-code directory
            // '${src.dir} is a pointer into the project.properties file for the directory to the root
            String srcPropertyKey = srcGrp.getName();
            srcPropertyKey = srcPropertyKey.substring(2, srcPropertyKey.length() - 1);
            boolean isTest = srcPropertyKey.startsWith("test");

            String sgDispName = srcGrp.getDisplayName();

            // get the file path and convert to a FileSrcCodeDir object    
            FileObject fileObj = srcGrp.getRootFolder();

            NbFileSrcCodePair nbFileSrcCodeDir = new NbFileSrcCodePair(fileObj.getPath(), fileObj,
                    isTest, sgDispName, (isTest ? buildTestClassDirFile : buildClassDirFile));

            srcCodeDirPairList.add(nbFileSrcCodeDir);
        }
        return srcCodeDirPairList;
    }

    /**
     * Get the source-code and test-source-code directories/folders for a
     * NBModule project.
     *
     * @return an array list of ALL file source-code(src and test) with classes
     *         directory
     */
    private ArrayList<NbFileSrcCodePair> getNbModuleSrcCodePairs() {
        // NBMODULE does not have setting in the project.properties and as
        // such is the difference to JavaSE in getting the source files directories
        ArrayList<NbFileSrcCodePair> srcCodeDirPairList = new ArrayList<>();

        // NbModule source and class directories do not use the
        // project.properties file for the paths
        String prjDir = this.nbProject.getProjectDirectory().getPath();

        Sources source = ProjectUtils.getSources(this.nbProject);
        SourceGroup[] groups = source.getSourceGroups("java");

        // process each source-code group
        for (SourceGroup srcGrp : groups) {
            String aSourceCodeFolder = srcGrp.getRootFolder().getPath();

            String srcPropertyKey = srcGrp.getName();
            srcPropertyKey = srcPropertyKey.substring(2, srcPropertyKey.length() - 1);
            boolean isTest = srcPropertyKey.startsWith("test");

            // the classes directory are likely defaults for NB modules, so do a best
            // guess setting
            FileClassesDir fileClassesDir;
            if (isTest) {
                // test.unit.src.dir
                String[] keySplitArr = srcPropertyKey.split("\\.");
                fileClassesDir = new FileClassesDir(prjDir + "/build/test/" + keySplitArr[1] + "/classes");
            } else {
                // assume this will exist regardless
                // only the one classes directory per project, nothing complex
                fileClassesDir = new FileClassesDir(prjDir + "/build/classes");
            }
            //
            NbFileSrcCodePair grpPair = new NbFileSrcCodePair(aSourceCodeFolder,
                    srcGrp.getRootFolder(),
                    isTest,
                    srcGrp.getDisplayName(), fileClassesDir);

            srcCodeDirPairList.add(grpPair);
        }
        return srcCodeDirPairList;
    }

    /**
     * Get the source-code and test-source-code directories/folders for the
     * project.
     *
     * @return an array list of ALL file source-code(src and test) with classes
     *         directory
     */
    private ArrayList<NbFileSrcCodePair> getSourceAndTestCodeDirs() {
        if (this.isNbModuleBool) {
            return getNbModuleSrcCodePairs();
        }
        return getJavaProjectSrcCodePairs();
    }

    /**
     * Get a key value from a Properties object, with support of NetBeans key
     * references (aka "${key}"). TODO: fix crash with javaee
     * 'build.classes.dir' property
     * <p>
     * Data is constructed each time it is called as a projects data may be
     * manipulated independently of dbrad-jacocoverage runtime.
     *
     * @param key the key value to get value.
     *
     * @return the key value.
     */
    public String getProperty(String key) {

        String value = convertPropertyToString(key);
        if (value == null) {
            return null;
        }

        int security = 0;

        while (security++ < 80 && checkRegex(value, CPA_NBPROPKEY_SHORTCUT)) {
            List<String> refs = getGroupsFromRegex(value, CPA_NBPROPKEY_SHORTCUT, 3);

            for (String ref : refs) {
                value = value.replaceFirst(PA_NBPROPKEY_SHORTCUT, convertPropertyToString(ref));
            }
        }
        return value;
    }

    /**
     * Convert a property to a string by converting all the ${xxxxx} items to a
     * value.
     *
     * @return string of the property.
     */
    private String convertPropertyToString(String propertyKey) {
        Properties nbProperties = this.getNbProjectProperties();

        String propValue = nbProperties.getProperty(propertyKey);

        if (propValue == null) {
            return null;
        }

        while (true) {

            // 1) dddd=${bbbbbb}somestring
            // 2) eeee=${ccc${fffff}}somestring (not implemented)
            //
            int innerKeyIndex = propValue.indexOf("${");

            if (innerKeyIndex == -1) {
                break;
            }
            int innerKeyIndexEnd = propValue.indexOf("}", innerKeyIndex);
            String innerKey = propValue.substring(innerKeyIndex + 2, innerKeyIndexEnd);
            String innerValue = convertPropertyToString(innerKey);

            propValue = propValue.replace("${" + innerKey + "}", innerValue);

        }
        return propValue;
    }

    /**
     * Get the full path of the directory containing a given project.
     *
     * @return the directory path containing the project.
     */
    final public String getNbProjectDirPath() {
        FileObject fo = this.nbProject.getProjectDirectory();
        return fo.getPath();
    }

    /**
     * Generate a string representing the project. Two different projects should
     * have different representation.
     *
     * @return a representation of the project.
     */
    final public String getNbProjectId() {
        return getNbProjectDirPath() + '_' + this.nbProject.toString();
    }

    /**
     * Get every selected project.
     *
     * @return the selected projects.
     */
    private static Collection<? extends Project> getAllSelectedProjects() {
        return Utilities.actionsGlobalContext().lookupAll(Project.class);
    }

    /**
     * Get the name of a project.
     *
     * @return the project's name.
     */
    final public String getNbProjectDisplayName() {
        return ProjectUtils.getInformation(this.nbProject).getDisplayName();
    }

    /**
     * Get the selected project. Return null if multiple projects are selected.
     *
     * @return the selected project or null if none or many.
     */
    final public static Project getSelectedNbProject() {
        //
        // Netbeans seems to have an issue when selecting in the Files view in that
        // 2 items are returned from the getAllSelectedProjects. The items
        // returned appear to be the same, so the code below does a filter.
        //
        // Otherwise there is an issue and a null is returned.

        Collection<? extends Project> prjs = getAllSelectedProjects();

        Project lastSelectedPrj = null;
        int countPrjSelected = 0;

        for (Project prj : prjs) {
            if (lastSelectedPrj == null) {
                lastSelectedPrj = prj;
                countPrjSelected++;
                continue;
            }
            if (lastSelectedPrj == prj) {
                continue;
            }
            countPrjSelected++;
        }
        if (countPrjSelected != 1) {
            return null;
        }
        return lastSelectedPrj;
    }

    private Properties getNbProjectProperties() {
        //99 is there a way to get the properties file without the hardcoded link below.
        //
        // ProjectUtils
        FileObject prjPropsFo = this.nbProject.getProjectDirectory().getFileObject("nbproject/project.properties");

        // 
        Properties prjProps = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = prjPropsFo.getInputStream();
            prjProps.load(inputStream);

        } catch (IOException ex) {

            // if this happens then there is a basic critical issue as a project should always have
            // a properties file.
            throw new RuntimeException("Project property file exception: CRITICAL unknown.", ex.getCause());

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex1) {
                    Exceptions.printStackTrace(ex1);
                }
            }
        }

        return prjProps;
    }

    /**
     * Get the Netbeans project this object represents within the IDE.
     *
     * @return Netbeans project
     */
    final public Project getNbProject() {
        return nbProject;
    }

    /**
     * Get the depends-on-project (sub-projects) for the project associated with
     * the root IDE configuration.
     *
     * @return array list of Netbeans Project
     */
    public ArrayList<Project> getDependsOnNbProjects() {
        ArrayList<Project> nbDependsOnProjectsList = new ArrayList<>();

        // get the project sub-projects/depends-on-projects
        SubprojectProvider provider = this.nbProject.getLookup().lookup(SubprojectProvider.class);
        Set<? extends Project> projSet = provider.getSubprojects();

        if (projSet == null) {
            return nbDependsOnProjectsList;
        }

        for (Project projDependingOnItem : projSet) {
            // ensure there are no duplicates
            if (nbDependsOnProjectsList.contains(projDependingOnItem)) {
                continue;
            }
            nbDependsOnProjectsList.add(projDependingOnItem);
            getDependsOnNbProjectsCascade(projDependingOnItem, nbDependsOnProjectsList);

        }
        return nbDependsOnProjectsList;
    }

    /**
     * Get the depends-on projects list (that is, Netbeans sub-project)
     * associated with the sub-project provided in a cascade manner.
     *
     * @param subProj                 Netbeans sub-project to get the
     * @param nbDependsOnProjectsList the list to add the depends-on projects to
     */
    public void getDependsOnNbProjectsCascade(Project subProj, ArrayList<Project> nbDependsOnProjectsList) {
        // get the project sub-projects/depends-on-projects
        SubprojectProvider provider = subProj.getLookup().lookup(SubprojectProvider.class);
        Set<? extends Project> projSet = provider.getSubprojects();

        // there are no sub-projects in the cascade, so return
        if (projSet == null) {
            return;
        }
        // each sub-project needs to be added to the arrangement, but avoid duplicates
        for (Project projDependingOnItem : projSet) {
            // ensure there are no duplicates
            if (nbDependsOnProjectsList.contains(projDependingOnItem)) {
                continue;
            }
            // not a duplicate
            nbDependsOnProjectsList.add(projDependingOnItem);
            getDependsOnNbProjectsCascade(projDependingOnItem, nbDependsOnProjectsList);
        }
    }
}
