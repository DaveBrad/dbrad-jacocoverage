/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocofpm.mgr.com;

import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_NO;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_A_SIBLING;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTING_OTHERS;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.NOT_AFFECTED;
import java.util.ArrayList;

/**
 * Class that represents affecting package information as displayed in packaging
 * tables. Each source-code folder (src and/or test directories) are a package
 * and needs to be analyzed for affecting relationships.
 * <p>
 * As packages (across the various source-code-folders) are identified they are
 * added to the analyzer which determines any affecting/affected relationships.
 * <p>
 * The source-code-folder packages are processed in the order over root and
 * associate projects (sub-projects)
 * <p>
 * The analyzer is re-determined when any changes happen to
 * <ul>
 * <li>the package table
 * <ul><li>'code-source-ON/OFF' state,</li>
 * <li>the 'cover-ON/OFF' state, or </li>
 * </ul>
 * </li>
 * <li>the 'exclude packages' table</li>
 * </ul>
 *
 *
 * @author dbradley
 */
public class PackageAffectingAnalyzer {

    private final String packageFormat;

    /**
     * All package-formats are affected by a super_package-format to this
     * sub-package item.
     */
    private PackageAffectingAnalyzer affectedByDifferentPrimary = null;

    /** The primary package-format that affects all others. */
    private IncludeSrcCodeDirInfo primaryCoverYes = null;

    /** Those items of the same package-format that are affected by the primary.
     */
    private final ArrayList<IncludeSrcCodeDirInfo> srcCodeDirInfoArrCoverOther = new ArrayList<>();

    /** Create the Package affecting analyzer object for a package-format.
     *
     * @param packageFormat string of the package-format this represents
     */
    public PackageAffectingAnalyzer(String packageFormat) {
        this.packageFormat = packageFormat;
    }

    /**
     * Add an include source-code-dir-info item while selecting a primary
     * (AFFECTING_SIBLING) with others being secondary
     * (AFFECTED_BY_OTHER_SIBLING).
     *
     * @param srcCodeDirInfo the package-file source-code-dir item
     */
    public void add(IncludeSrcCodeDirInfo srcCodeDirInfo) {
        // clear any affecting object data in preparation for 
        // setting the data later.
        srcCodeDirInfo.originSrcCodeFile.clearAffectingObjectsArr();

        if (primaryCoverYes == null) {
            // if the item is set no, it cannot affect others, so may not be the primary
            if (srcCodeDirInfo.originSrcCodeFile.coverStateEnum != COVER_NO) {
                // this item affecting all other items
                primaryCoverYes = srcCodeDirInfo;
                return;
            }
        }
        // item is affected by a primary item
        srcCodeDirInfoArrCoverOther.add(srcCodeDirInfo);
    }

    /**
     * Check if super-package affecting this package.
     * <p>
     * A super-package would be
     * <pre>
     * super: xxx.         xxx.yyy.
     * sub:   xxx.yyy.     xxx.yyy.ccc
     * </pre>
     *
     * @param superPkgAnalyzer the super-package
     */
    public void checkForSuperPackageFormat(PackageAffectingAnalyzer superPkgAnalyzer) {
        if (this.affectedByDifferentPrimary != null) {
            // cannot overwrite this condition
            return;
        }
        // for this to work the processing has to be in ascending order
        // of the package-formats
        String myPackage = this.packageFormat + ".";

        String superMaybePackage = superPkgAnalyzer.packageFormat + ".";

        // super-package format empty is '*' so overrides all
        if (superPkgAnalyzer.packageFormat.isEmpty()
                || myPackage.startsWith(superMaybePackage)) {
            this.affectedByDifferentPrimary = superPkgAnalyzer;
        }
    }

    /**
     * Set the state of the packages' affecting color in the table-displays.
     *
     * @param excludesList array of excludes list which over writes all
     */
    public void setAffectingState(ArrayList<String> excludesList) {

        String myPkgPattern = this.packageFormat + ".";

        // the excludes list overrides all data
        for (String exclPattern : excludesList) {

            if (myPkgPattern.startsWith(exclPattern)) {
                if (this.primaryCoverYes != null) {
                    this.primaryCoverYes.originSrcCodeFile
                            .setAffectingState(null, AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES);
                }

                for (IncludeSrcCodeDirInfo inclSrcCodeInfo : srcCodeDirInfoArrCoverOther) {
                    inclSrcCodeInfo.originSrcCodeFile
                            .setAffectingState(null, AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES);
                }
                return;
            }
        }

        // determine the state to apply for this package-format
        if (this.affectedByDifferentPrimary != null) {
            // a different primary is affecting this state

            // an external or super-package is affecting this package-format
            IncludeSrcCodeDirInfo affectingAgent = this.affectedByDifferentPrimary.getAffector();

            if (this.primaryCoverYes != null) {
                // any siblings are affected by the primary, while the
                // primary is affected-by a different primary agent
                if (affectingAgent == null) {
                    this.primaryCoverYes.originSrcCodeFile
                            .setAffectingState(null, NOT_AFFECTED);
                } else {
                    this.primaryCoverYes.originSrcCodeFile
                            .setAffectingState(affectingAgent.originSrcCodeFile, AFFECTED_BY_A_SIBLING);
                }
                // any children are affected by this primary
                affectingAgent = this.primaryCoverYes;
            }
            // process the affected items
            for (IncludeSrcCodeDirInfo inclSrcCodeInfo : srcCodeDirInfoArrCoverOther) {
                if (affectingAgent == null) {
                    inclSrcCodeInfo.originSrcCodeFile
                            .setAffectingState(null, NOT_AFFECTED);
                } else {
                    inclSrcCodeInfo.originSrcCodeFile
                            .setAffectingState(affectingAgent.originSrcCodeFile, AFFECTED_BY_A_SIBLING);
                }
            }

        } else if (this.primaryCoverYes != null) {
            // the primary is affecting-others
            this.primaryCoverYes.originSrcCodeFile
                    .setAffectingState(this.primaryCoverYes.originSrcCodeFile,
                            AFFECTING_OTHERS);

            // sibling source are affectedby the primary
            for (IncludeSrcCodeDirInfo inclSrcCodeInfo : srcCodeDirInfoArrCoverOther) {
                // the primary is affecting others, so need to update it
                this.primaryCoverYes.originSrcCodeFile
                        .getAffectingObjectsArr().add(inclSrcCodeInfo.originSrcCodeFile);

                // being affected processing
                inclSrcCodeInfo.originSrcCodeFile
                        .setAffectingState(this.primaryCoverYes.originSrcCodeFile,
                                AFFECTED_BY_A_SIBLING);
            }
        } else {
            // nothing is affecting the others
            for (IncludeSrcCodeDirInfo inclSrcCodeInfo : srcCodeDirInfoArrCoverOther) {
                inclSrcCodeInfo.originSrcCodeFile
                        .setAffectingState(null, NOT_AFFECTED);
            }
        }
    }

    private IncludeSrcCodeDirInfo getAffector() {
        if (this.affectedByDifferentPrimary != null) {
            return this.affectedByDifferentPrimary.getAffector();
        }
        if (this.primaryCoverYes != null) {
            return this.primaryCoverYes;
        }
        return null;
    }
}
