/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_BY_PARENT;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_NO;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_YES;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
import java.io.File;
import java.util.ArrayList;

/**
 * Class that represents a package has Java-files directly associated within it.
 *
 * @author dbradley
 */
public class FilePackageOfSrc extends FileSrcCodeDir {

    private static final long serialVersionUID = 1L;

    /** the base source-code directory for this package */
    protected FileSrcCodeDir srcCodeDirBase;

    /** set true if this the base file */
    protected boolean baseFileBool = false;

    /** indicates if Java files are found in its structure */
    private boolean hasJavaFiles;

    /**
     * the coverage state for this package
     */
    public FilePackageCoverStateEnum coverStateEnum;

    /**
     * The package-end-string is in fact the last string in a package address
     * ie. io.util.pkgendstring
     * <p>
     * Thus a full package string would be all the parents end-string
     */
    protected String packageEndString;

    /**
     * Create a file-package-of-source object using the base parent, path and
     * source-code-folder kind (src/test) (called for processing from a base
     * source-code-folder as the start of a package structure.
     *
     * @param srcCodeDirBase base parent package (which is known to have no
     * @param pathname       path name for this package
     * @param isTestCode     true if a test-source-code folder
     */
    public FilePackageOfSrc(FileSrcCodeDir srcCodeDirBase, String pathname, boolean isTestCode) {
        super(pathname, null, isTestCode);
        this.myParent = null;
        this.packageEndString = "";

        this.srcCodeDirBase = srcCodeDirBase;
        this.nbProjectJacoco = srcCodeDirBase.nbProjectJacoco;

        this.baseFileBool = true;
    }

    /**
     * Create a file-package-of-source object using the parent, path and
     * source-code-folder kind (src/test)
     *
     * @param myParent   parent package
     * @param pathname   path name for this package
     * @param isTestCode true if a test-source-code folder
     */
    public FilePackageOfSrc(FilePackageOfSrc myParent, String pathname, boolean isTestCode) {
        super(pathname, null, isTestCode);
        this.myParent = myParent;

        if (myParent.coverStateEnum != COVER_NO) {
            this.coverStateEnum = COVER_BY_PARENT;
        }
        this.nbProjectJacoco = myParent.nbProjectJacoco;
    }

    /**
     * Calculate if this has any explicit java-files in the folder, while
     * setting the package-string for this object.
     *
     * @param srcCodeFile the source-code directory this package represents
     */
    public void initializePackageData(FileSrcCodeDir srcCodeFile) {
        this.srcCodeDirBase = srcCodeFile;

        File[] listOfFilesAndDir = this.listFiles();

        // faster than apache libraries as this only goes until the
        // first .java file is found
        hasJavaFiles = false;
        for (File s4FileOrDir : listOfFilesAndDir) {
            if (s4FileOrDir.isFile()) {
                if (s4FileOrDir.getAbsolutePath().endsWith(".java")) {
                    hasJavaFiles = true;
                    break;
                }
            }
        }
        setPackageEndString();
    }

    /**
     * Set the package end string (that is: the package this represents
     * grandparent-pck.parent-pck.my-pck ====&gt; my-pck)
     * <p>
     */
    private void setPackageEndString() {
        String myPath = this.getAbsolutePath().replace(File.separator, "/");

        int index = myPath.lastIndexOf("/");

        String endString;
        if (index == -1) {
            endString = myPath;
        } else {
            endString = myPath.substring(index + 1); // remove the leading slash
        }
        if (this.myParent != null) {
            if (!this.myParent.baseFileBool) {
                endString = String.format(".%s", endString);
            }
        }
        this.packageEndString = endString;
    }

    /**
     * Get the package-format string for this package representation processing
     * the parents to build the full package relationship.
     *
     * @return string of grandparent-pck.parent-pck.my-pck
     */
    public String getPackageFormat() {
        // does this by looking into all the parents of this, so backwards
        // processing

        // Netbeans has some folder structures that can be for
        // 'unit' or 'qa-functional' testing for its some files. This
        // presents issuesfor dbrad-jacocoverage as a best guess for the
        // package format needs to be guessed at against the folder.
        //
        // if a test-directory
        //
        // the default test folder
        // 1a 'test/org/testfiles/
        // 2a 'test/unit/org/testfiles/
        // 3a 'test/qa-functional/org/testfiles/
        //
        // additional test folders have a different name
        // 1b 'test2/org/testfiles/
        // 2b 'test2/unit/org/testfiles/
        // 3b 'test2/qa-functional/org/testfiles/   
        if (this.myParent == null) {
            return this.packageEndString;
        }
        // a parent package string may not start with a '.', so adjust it
        String parentPackageStr = this.myParent.getPackageFormat();

        String packageFormat = String.format("%s%s", parentPackageStr, this.packageEndString);

        if (packageFormat.startsWith(".")) {
            if (packageFormat.length() > 1) {
                packageFormat = packageFormat.substring(1);
            } else {
                packageFormat = "";
            }
        }
        return packageFormat;
    }

    /**
     * Get the list of packages for this format package and add to a list-array.
     *
     * @param addToListArray      list-array of FilePackageOfSrc objects
     * @param parentPackageFormat String of the parent package for this item so
     *                            as to format the a complete relationship
     * @param top                 if this package-string is the top of the stack
     *                            (base)
     *
     * @return modified list-array (if applicable)
     */
    final protected ArrayList<FilePackageOfSrc> getListOfPackages(ArrayList<FilePackageOfSrc> addToListArray,
            String parentPackageFormat,
            boolean top) {

        // this is a top?
        if (top) {
            if (this.myParent != null) {
                return this.myParent.getListOfPackages(addToListArray, parentPackageFormat, top);
            }
        }
        // forward processing
        String packageFormat = String.format("%s%s", parentPackageFormat, this.packageEndString);

        addToListArray.add(this);

        for (FilePackageOfSrc child : this.subPackageArr) {
            child.getListOfPackages(addToListArray, packageFormat, false);
        }
        return addToListArray;
    }

    /**
     * Set the coverage state to the provided new state and set parent, siblings
     * and/or children appropriately.
     *
     * @param newCoverState new state to set
     */
    public void setCoverState(FilePackageCoverStateEnum newCoverState) {

        // set the new state regardless and then set the childern based on this
        // new state,
        //
        // then c
        FilePackageCoverStateEnum nuCoverState = newCoverState;

        // need to set my state based on the parent
        //
        // if the parent is no then the  
        if (this.myParent != null) {
            if (this.myParent.coverStateEnum != COVER_NO) {
                // this will cause a cascade
                nuCoverState = COVER_BY_PARENT;
            }
        }

        this.coverStateEnum = nuCoverState;
        // the parent state overrides the child states
        if (!this.subPackageArr.isEmpty()) {
            // determine the child state to set
            FilePackageCoverStateEnum childStateSetting = null;
            switch (this.coverStateEnum) {
                case COVER_YES:
                case COVER_BY_PARENT:
                    childStateSetting = COVER_BY_PARENT;
                    break;

                case COVER_NO:
                    childStateSetting = COVER_YES;
                    break;
            }
            // at the top of some files and as such a master-controller
            for (FilePackageOfSrc child : this.subPackageArr) {
                child.setCoverState(childStateSetting);
            }
        }
    }

    /**
     * Set the parent to NO, as this child state is being set to override its
     * settings and thus will cause the parent to be off.
     *
     * @param childThatMadeRequest the child object making request
     */
    private void setParentNO(FilePackageOfSrc childThatMadeRequest) {
        if (this.myParent != null) {
            this.myParent.setParentNO(childThatMadeRequest);
        }
        this.coverStateEnum = COVER_NO;
        //
        // all but my origin childs change to YES
        String originaPackage = childThatMadeRequest.getPackageFormat();

        for (FilePackageOfSrc fileChild : this.subPackageArr) {
            if (fileChild.hasJavaFiles) {
                //
                String filePackage = fileChild.getPackageFormat();

                // skip the origin package
                if (originaPackage.equals(filePackage)) {
                    continue;
                }
                if (fileChild.coverStateEnum != COVER_NO) {
                    fileChild.setCoverState(COVER_YES);
                }
            }
        }
        this.coverStateEnum = COVER_NO;
    }

    /**
     * Toggle the package coverage state for this package-format and set its
     * state according to its parent state, and process children too.
     * <p>
     * The children will be affected by this state change. The parent of this
     * object may overlay/override this state implicitly.
     */
    public void togglePkgCoverageState() {
        FilePackageCoverStateEnum currState = this.coverStateEnum;

        switch (currState) {
            case COVER_YES:
                // YES to NO: implying all head of childern become YES
                //
                // if at the top of the packaging list, cannot affect the parent
                if (this.myParent != null) {
                    this.myParent.setParentNO(this);
                }
                this.setCoverState(COVER_NO);

                break;

            case COVER_NO:
                // NO to YES: implying all childern are now controlled by
                // parent
                this.setCoverState(COVER_YES);
                break;

            case COVER_BY_PARENT:
                // take control away from parent and set sibilings to state as well
                if (this.myParent != null) {
                    this.myParent.setParentNO(this);
                    for (FilePackageOfSrc childToCHange : this.myParent.subPackageArr) {
                        // childern changed to YES
                        childToCHange.setCoverState(COVER_YES);
                    }
                }
                break;
        }
    }

    /**
     * Has this file package-folder have Java files in its path or sub-paths.
     *
     * @return true if has Java files present
     */
    public Boolean hasJavaFilesInPathAndSubPaths() {

        if (this.hasJavaFiles) {
            return true;
        }
        // else process the children
        if (!this.subPackageArr.isEmpty()) {
            // there only needs to be one child with hasJavaFiles for super-package to
            // be considered needed in path of jacocoagent includes setting
            for (FilePackageOfSrc child : this.subPackageArr) {
                if (child.hasJavaFilesInPathAndSubPaths()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Toggle the source-code-folder ON state.
     */
    public void toggleOnCodeFolderState() {
        this.setOnOffState(!this.isOnCodeFolderState());
    }

    /**
     * Set the source-code-folder state to that provided.
     *
     * @param nuOnOffState true set ON, false set OFF
     */
    private void setOnOffState(boolean nuOnOffState) {
        this.setOnCodeFolderState(nuOnOffState);

        // else process the children
        if (!this.subPackageArr.isEmpty()) {
            // there only needs to be one child with hasJavaFiles for super-package to
            // be considered needed in path of jacocoagent includes setting
            for (FilePackageOfSrc child : this.subPackageArr) {
                child.setOnOffState(nuOnOffState);
            }
        }
    }

    @Override
    public void setOnCodeFolderState(boolean onCodeFolderState) {
        super.setOnCodeFolderState(onCodeFolderState);

        // if we are the base file then the source-code-dir object needs to
        // be updated too.
        if (this.baseFileBool) {
            this.srcCodeDirBase.setOnCodeFolderState(onCodeFolderState);
        }
    }

    /**
     * Is this a base file (in that is has no parent package-format file
     * relationship)
     *
     * @return true if the base file
     */
    public boolean isBaseFile() {
        return this.baseFileBool;
    }

    /**
     * Get the source-code-folder/package base for this package-format
     *
     * @return base source-code-folder directory
     */
    public FileSrcCodeDir getSrcCodeDirBase() {
        return srcCodeDirBase;
    }

    @Override
    public String getSrcFolderDirDisplayName() {
        return this.getSrcCodeDirBase().getSrcFolderDirDisplayName();
    }

    /**
     * Set any exclude-package settings to overlay the package-format this
     * instance represents.
     *
     * @param excludeList list of all exclude-packages to compare against
     */
    final public void setExcludePackages(ArrayList<String> excludeList) {
        String myPackageFormat = getPackageFormat() + ".";

        for (String exclPack : excludeList) {
            if (myPackageFormat.startsWith(exclPack)) {
                this.affectedBySiblingState = AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
            }
        }
    }

    /**
     * Set the affecting state for a package-format with settings on the
     * children.
     *
     * @param affectorSrc the source-package that is causing the affecting state
     * @param state       the state
     */
    final public void setAffectingState(FilePackageOfSrc affectorSrc,
            PackageAffectingState state) {

        // set the relationships of this to the affector
        this.affectedBySiblingSource = affectorSrc;
        this.affectedBySiblingState = state;

        // cascade to process any sub-packages of me
        // and process the children
        if (!this.subPackageArr.isEmpty()) {
            // process the children
            for (FilePackageOfSrc child : this.subPackageArr) {
                child.setAffectingState(affectorSrc, state);
            }
        }
    }
}
