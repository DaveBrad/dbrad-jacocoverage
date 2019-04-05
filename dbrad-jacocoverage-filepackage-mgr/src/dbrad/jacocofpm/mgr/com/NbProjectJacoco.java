/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocofpm.mgr.com;

import dbrad.jacocofpm.config.IdeConfigOfProject;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.config.NbFileSrcCodePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JTable;
import org.netbeans.api.project.Project;

/**
 * Class the represents an IDE's project for jacoco processing, linking IDE
 * configuration information, Netbeans project and dbrad-jacocoverage structures
 * together.
 *
 *
 *
 *
 * @author dbradley
 */
public class NbProjectJacoco {

    /**
     * The IDE configuration information for Jacoco settings to be used for
     * processing.
     */
    IdeProjectJacocoverageConfig ideProjectConfig4Jacoco;

    /**
     * Is this the Root project, that is the NB-project. Alternative is
     * associate-project.
     */
    private NbProjectJacoco principleNbProjectJacoco;

    /** the NB projects PfJTable object that does the display of the settings */
    private JTable nbProjectsPfJTable;

    /** the source-code folder pairs that the project(s) are to be
     * jacocoverage'd are stored into
     */
    private ArrayList<FileSrcCodeDir> srcCodeDirPairList = new ArrayList<>();

    /**
     * List of all depending-on/associate-projects that relate to this project.
     * (A KEY: list that will be sorted appropriately.)
     */
    private ArrayList<String> dependingOnNbProjectsKeyArr = new ArrayList<>();

    /**
     * HashMapof all depending-on/associate-projects that relate to this project
     * in the form of this class type.
     * <p>
     * Key: string name of project/associate-project
     * <p>
     * Element: a NB-project-jacoco structure so as to cascade process in the
     * same manner
     */
    private HashMap<String, NbProjectJacoco> dependingOnNbProjectsHash = new HashMap<>();

    /**
     * Create a Netbeans project jacoco object for representing a project that
     * may select packages for inclusion in dbrad-jacocoverage.
     */
    public NbProjectJacoco() {
        // indicate this is a root-racoco
        this.principleNbProjectJacoco = null;

        // get the NB project.properties file and content
        try {
            this.ideProjectConfig4Jacoco = IdeProjectJacocoverageConfig.createForSelectedProject();  // forFile(buildClassDirFile);

        } catch (IOException ex) {
            // too critical error so things may not precede
            throw new RuntimeException("Critical failure.", ex.getCause());
        }
        setNbProjectJacoco();
    }

    /**
     * Create a Netbeans project jacoco object for representing an
     * associate-project that may select packages for inclusion in
     * dbrad-jacocoverage.
     *
     * @param nbProject                Netbeans project this object represents
     * @param principleNbProjectJacoco an associate-projects principle-project
     */
    public NbProjectJacoco(Project nbProject, NbProjectJacoco principleNbProjectJacoco) {
        // indicate this is NOT a principle-jacoco
        this.principleNbProjectJacoco = principleNbProjectJacoco;

        try {
            boolean isRootProject = principleNbProjectJacoco == null;

            if (isRootProject) {
                // if this is a root project it is being created for the first
                // time on a opening the project.properties
                //
                // any previous IDE-config stuff needs to be removed so
                // the data is as at this.point in time
                IdeConfigOfProject.clearIdeConfigs();

                this.ideProjectConfig4Jacoco = IdeProjectJacocoverageConfig.reuseForProject(nbProject);
            } else {
                this.ideProjectConfig4Jacoco = IdeProjectJacocoverageConfig.reuseForAssociateProject(nbProject);
            }

        } catch (IOException ex) {
            // too critical error so things may not precede
            throw new RuntimeException("Critical failure.", ex.getCause());
        }
        setNbProjectJacoco();
    }

    private void setNbProjectJacoco() {
        if (this.ideProjectConfig4Jacoco.nbProjectJacoco == null) {
            this.ideProjectConfig4Jacoco.nbProjectJacoco = this;
        }
        ArrayList<NbFileSrcCodePair> srcCodeDirPairListTmp = this.ideProjectConfig4Jacoco.getAllSourceCodeFiles();

        this.srcCodeDirPairList.clear();

        for (NbFileSrcCodePair nbFileSrcCodePair : srcCodeDirPairListTmp) {
            this.srcCodeDirPairList.add(
                    new FileSrcCodeDir(nbFileSrcCodePair.getAbsolutePath(),
                            nbFileSrcCodePair.openIdeFileObject,
                            this,
                            nbFileSrcCodePair.isTestSrcCode(),
                            nbFileSrcCodePair.getSrcFolderDirDisplayName(),
                            nbFileSrcCodePair.getClassesAssociatedFile()
                    ));
        }
        if (this.isPrincipleProject()) {
            getDependsOnProjects();
        }
    }

    final String getProjectDisplayName() {
        return this.ideProjectConfig4Jacoco.getNbProjectDisplayName();
    }

    /**
     * Repaint all the package-filter tables if actions affect the tables data
     * that is displayed.
     * <p>
     * There are conditions where the tables may no longer apply, so deal with
     * nulls.
     */
    final public void repaint4Project() {
        // perform the repaint only on a root-nbProject
        if (isPrincipleProject()) {
            // perform the repaint on the depending on items first
            for (String key : dependingOnNbProjectsKeyArr) {
                NbProjectJacoco dependNbProjJac = dependingOnNbProjectsHash.get(key);
                dependNbProjJac.nbProjectsPfJTable.repaint();
            }
        }
        this.nbProjectsPfJTable.repaint();
    }

    /**
     * Merge all sources for display will determine what is being duplicated
     * across the various source-code files being processed. Thus the list of
     * packages is organized.
     * <p>
     * There is the potential for sub-projects to use the same sub-sub-project
     * so this merge only causes one of project to be in the list.
     *
     * @param excludeList list of exclude-packages to be applied
     *
     * @return organized list of source-code folder pairs
     */
    public ArrayList<FileSrcCodeDir> mergeAllSourcesForDisplay(ArrayList<String> excludeList) {
        // get all the source files for this process which includes 'this/self'
        // and any associated-projects as source 
        ArrayList<FileSrcCodeDir> allAllSourceCodeDirList = new ArrayList<>();

        // need to goto the root project
        NbProjectJacoco mergeRootProjJacoco;
        if (this.isPrincipleProject()) {
            mergeRootProjJacoco = this;
        } else {
            mergeRootProjJacoco = this.principleNbProjectJacoco;
        }
        //
        // only those sources that are "on" need to be considered
        gatherSrcCodeDir(mergeRootProjJacoco, allAllSourceCodeDirList);

        // process the associate-projects
        for (String sKey : mergeRootProjJacoco.dependingOnNbProjectsKeyArr) {
            NbProjectJacoco assocProj = mergeRootProjJacoco.dependingOnNbProjectsHash.get(sKey);
            gatherSrcCodeDir(assocProj, allAllSourceCodeDirList);
        }
        // have all the sources now need to process all-all
        if (!allAllSourceCodeDirList.isEmpty()) {
            // if the srcCodeDirPairList is empty, it is known that all the
            // projects (root and associated/sub) are not set for processing
            // so they may not be merged
            //
            // However, there are source-code-dir which may be returned.
            if (!mergeRootProjJacoco.srcCodeDirPairList.isEmpty()) {
                // the merge operates on the pair, there is at least one so may get the
                // '0' element and perform a merge (to set or reset the merge data)
                mergeRootProjJacoco.srcCodeDirPairList.get(0)
                        .mergeAllSourcePairsGetIncludePackageStringsList(allAllSourceCodeDirList, excludeList);
            }
        }
        return allAllSourceCodeDirList;
    }

    /**
     * Gather the source-code folders for a project into an ALL list of
     * source-code folders (That is, principle-project and/or
     * associated-projects.)
     *
     * @param mergeProjJacoco         merge NB-project-jacoco object to be
     *                                merged
     * @param allAllSourceCodeDirList list of all source-code folders (src &
     *                                test)
     */
    private void gatherSrcCodeDir(NbProjectJacoco mergeProjJacoco, ArrayList<FileSrcCodeDir> allAllSourceCodeDirList) {
        // only those sources that are "on" need to be considered
        for (FileSrcCodeDir srcFileDir : mergeProjJacoco.srcCodeDirPairList) {
            if (srcFileDir.isOnCodeFolderState()) {
                allAllSourceCodeDirList.add(srcFileDir);
            }
        }
    }

    /**
     * Set as an associate-projects rather than principle-project.
     *
     * @param associateJTable the JTable for associate-package of a
     *                        principle-project.
     */
    final public void setAssociateProjectTable(JTable associateJTable) {
        this.nbProjectsPfJTable = associateJTable;
    }

    /**
     * Is this NB project jacoco instance a representation for a
     * principle-project.
     *
     * @return true is principle-project
     */
    final public boolean isPrincipleProject() {
        return (this.principleNbProjectJacoco == null);
    }

    /**
     * Use the principle-project for Netbeans project jacoco processing.
     *
     * @return principle-project for Netbeans jacoco
     */
    final public NbProjectJacoco usePrincipleNbPrjJac() {
        if (this.principleNbProjectJacoco == null) {
            return this;
        }
        return this.principleNbProjectJacoco;
    }

    /**
     * Get the principle-project that is associated with this project,that is
     * the root project for principle or associated project forms of this data
     * object.
     *
     * @return principle-project NB-project
     */
    final public Project getPrincipleNbProject() {
        if (!isPrincipleProject()) {
            return this.principleNbProjectJacoco.ideProjectConfig4Jacoco.getNbProject();
        }
        return this.ideProjectConfig4Jacoco.getNbProject();
    }

    /**
     * Get the associate-project this represents.
     *
     * @return associate-project NB-project
     *
     * @throws RuntimeException if not invoked correctly
     */
    final public Project getAssociateNbProject() {
        if (!isPrincipleProject()) {
            return this.ideProjectConfig4Jacoco.getNbProject();
        }
        throw new RuntimeException("Calling for sub-NbProject when root-NbProject.");
    }

    /**
     * Get the display-name for this project.
     *
     * @return
     */
    final public String getNbProjectDisplayName() {
        return this.ideProjectConfig4Jacoco.getNbProjectDisplayName();
    }

    /**
     * Get the depends-on (associate-projects, or in Netbeans terms
     * sub-projects) for this principle-project.
     */
    private void getDependsOnProjects() {

        ArrayList<Project> nbProjectDependingOnList = this.ideProjectConfig4Jacoco.getDependsOnNbProjects();

        if (nbProjectDependingOnList.isEmpty()) {
            this.dependingOnNbProjectsKeyArr.clear();
            this.dependingOnNbProjectsHash.clear();
            return;
        }
        // process each depending-on project and convert to NbProjectJacoco objects
        // which will expand Netbeans Project significantly but being very different 
        // from Netbeans; that is, dbrad-jacocoverage
        //
        nbProjectDependingOnList.forEach((nbProjectDependingOnItem) -> {

            // the depending-on projects are related to a root NbProjectJacoco
            NbProjectJacoco nbPrjJac = new NbProjectJacoco(nbProjectDependingOnItem, this);

            String projDispName = nbPrjJac.ideProjectConfig4Jacoco.getNbProjectDisplayName();

            this.dependingOnNbProjectsKeyArr.add(projDispName);
            this.dependingOnNbProjectsHash.put(projDispName, nbPrjJac);
        });
        // There is no order to the sub-projects/depending-on-projects so
        // sort for better presentation
        //
        Collections.sort(this.dependingOnNbProjectsKeyArr);
    }

    /**
     * Get the package-filter JTable for the Netbeans project that is
     * representing a JaCoCo set of inclusion data. (May be principle or
     * associate project table.)
     *
     * @return JTable for a package-filter
     */
    public JTable getPfJTable4NbProjectJacoco() {
        return this.nbProjectsPfJTable;
    }

    /**
     * Get the source-code pairs for all principle/associate-projects as an
     * array-list.
     *
     * @return array-list of file source-code pair
     */
    public ArrayList<FileSrcCodeDir> getSrcCodeDirPairList() {
        return this.srcCodeDirPairList;
    }

    /**
     * Get array-list of keys (strings) of the Netbeans projects that are
     * depend-on/associate projects of a principle-project being processed as
     * the coverage.
     *
     * @return array-list of keys
     */
    public ArrayList<String> getDependingOnNbProjectsKeyArr() {
        return this.dependingOnNbProjectsKeyArr;
    }

    /**
     * Get HashMap of the Netbeans projects that are depend-on/associate
     * projects of a principle-project being processed as the coverage.
     *
     * @return HashMap of depending-on projects
     */
    public HashMap<String, NbProjectJacoco> getDependingOnNbProjectsHash() {
        return this.dependingOnNbProjectsHash;
    }

}
