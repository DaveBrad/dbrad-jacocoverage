/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocofpm.mgr.com;

import dbrad.jacocofpm.config.NbFileSrcCodePair;
import dbrad.jacocofpm.json.Json1ProjLevel;
import dbrad.jacocofpm.json.Json2ProjSrcCodeFolder;
import dbrad.jacocofpm.json.Json3PackageFormat;
import dbrad.jacocofpm.json.Json4Data;
import dbrad.jacocofpm.json.JsonMap;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_NO;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.NOT_AFFECTED;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.openide.filesystems.FileObject;

/**
 *
 * <p>
 * A file source-code can be from different areas: 1) The current project
 * running and its src, test, src2, test2 arrangements 2) Sibling project src,
 * test, src2, test2 arrangements 3) Open free-form src, src2, src3
 *
 * @author dbradley
 */
public class FileSrcCodeDir extends NbFileSrcCodePair {

    private static final long serialVersionUID = 1L;

    /**
     * a NB jacoco project object associating the source-code-folder with a
     * Netbeans project
     */
    public NbProjectJacoco nbProjectJacoco;

    private boolean onCodeFolderState = false;

    /** the parent of this object */
    protected FilePackageOfSrc myParent;

    /** array list of the sub-package children of this package */
    protected ArrayList<FilePackageOfSrc> subPackageArr = new ArrayList<>(4);

    /** the number of flashes that may be done */
    public static final int FLASHER_NUMBER = 6;
    
    /** the flashing display counter */
    public int flashInDisplayCount = 0;

    /**
     * When the includes is calculated there is the potential for other sibling
     * includes patterns to override and affect the settings for coverage of
     * this.
     */
    protected PackageAffectingState affectedBySiblingState;

    /**
     * The affected-by sibling source package file that is affecting this
     * package and its coverage state.
     */
    public FilePackageOfSrc affectedBySiblingSource;

    /**
     * The array of affecting objects this package is
     */
    private final ArrayList<FilePackageOfSrc> affectingObjectsArr = new ArrayList<>();

    /**
     * Set if this is a packaging-filter of my project, and not an additional
     * package being processed (implying do not have access to annotate/hi-lite
     * source code
     */
    public boolean isMyProject = true;

    /**
     * Create a source-code file directory item for root/base.
     *
     * @param pathname                 string of the full path
     * @param openIdeFileObj           Netbeans FileObject for interaction with
     *                                 Netbeans
     * @param nbProjectJacoco          a NB jacoco project object associating
     *                                 the source-code-folder with a Netbeans
     *                                 project
     * @param isTestCode               true if a test-source-code folder
     * @param codesFolderNbDisplayName display name for the source-code-folder
     * @param classesAssociatedFile    the classes-binary location file path
     *                                 that associate with the source code
     */
    public FileSrcCodeDir(String pathname,
            FileObject openIdeFileObj,
            NbProjectJacoco nbProjectJacoco,
            boolean isTestCode,
            String codesFolderNbDisplayName,
            FileClassesDir classesAssociatedFile) {
        //
        super(pathname, openIdeFileObj, isTestCode, codesFolderNbDisplayName, classesAssociatedFile);

        this.nbProjectJacoco = nbProjectJacoco;
        this.affectedBySiblingState = NOT_AFFECTED;
        this.onCodeFolderState = !isTestCode;
    }

    /**
     * Create a source-code file directory item for root/base.
     *
     * @param pathname       string of the full path
     * @param openIdeFileObj Netbeans FileObject for interaction with Netbeans
     * @param isTestCode     true if a test-source-code folder
     */
    public FileSrcCodeDir(String pathname, FileObject openIdeFileObj, boolean isTestCode) {
        //
        super(pathname, openIdeFileObj, isTestCode);
        this.affectedBySiblingState = NOT_AFFECTED;
        this.onCodeFolderState = !isTestCode;
    }

    /**
     * Apply the basic settings from the JSON map to this object so JSON
     * configuration settings are currently applied. This is needed at the
     * run-time of the coverage processing of data collected
     *
     *
     * @param jsonSettings current JSON map settings
     */
    public void applyJsonMap(JsonMap jsonSettings) {
        // there are the package settings and the on-off setting. The package
        // settings (filters|) have been calcuated already, so only the on-off
        // setting needs to be applied.
        //
        // "pkgfltr.listing" : {               -- getProjectIncludePackages
        // "TestHelloWorldMulti_1" : {         -- projectName
        //  "Source Packages" : {              -- source-code-folder-name
        //   "" : {                            -- package-filter top level "" or "*"
        //    "on" : true,                     -- on-off setting (WHAT NEEDS TO BE APPLIED)
        //    "isTst" : false,
        //    "hasJava" : true,
        //    "pfCvr" : "COVER_YES"
        //  },
        //
        String projectName = this.getNbProjectDisplayName();

        Json1ProjLevel json1Incl = jsonSettings.getProjectIncludePackages();

        Json2ProjSrcCodeFolder json2Incl = json1Incl.get(projectName);

        // if no data then rely on the default settings
        if (json2Incl == null) {
            return;
        }
        // now find the source-code-folder-name key for the JSON 2
        String sourceCodeFolderName = this.getSrcFolderDirDisplayName();

        Json3PackageFormat json3SrcCodeFldrName = json2Incl.get(sourceCodeFolderName);
        if (json3SrcCodeFldrName == null) {
            // shouldn't be but covered by this code
            return;
        }
        // need the default key for the top package which is blank string
        // or "*" (the latter is incase code is changed to store "*" as the
        // top package string.
        Json4Data json4PkgFilter = json3SrcCodeFldrName.get("");

        if (json4PkgFilter == null) {
            // has it changed to "*"
            json4PkgFilter = json3SrcCodeFldrName.get("*");

            if (json4PkgFilter == null) {
                return;
            }
        }
        // get the "on" setting
        boolean onSetting = json4PkgFilter.isOn();

        this.setOnCodeFolderState(onSetting);

        for (FilePackageOfSrc subFiles : this.subPackageArr) {
            subFiles.calculateSubPackges(this);
        }
    }

    /**
     * Calculate the sub-packages of this source-code path
     *
     * @param fileSrcCodeBaseObj the base source-code object to root/base
     *                           against
     */
    final public void calculateSubPackges(FileSrcCodeDir fileSrcCodeBaseObj) {

        if (!(this instanceof FilePackageOfSrc)) {
            FilePackageOfSrc rootPackage = new FilePackageOfSrc(this, this.getAbsolutePath(), this.isTestSrcCode());

            this.subPackageArr.add(rootPackage);

            rootPackage.calculateSubPackges(fileSrcCodeBaseObj);
            return;
        }
        // get the directories of this source/package 
        //
        File[] subPkgArr = this.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        // define the parent for the child, the FileSrcCodeDir has
        // no parent per-say
        FilePackageOfSrc childsParent
                = (this instanceof FilePackageOfSrc) ? (FilePackageOfSrc) this : null;

        // now process each sub-package and recurse on it for further stuff to do
        for (File subPkgItem : subPkgArr) {
            FilePackageOfSrc child
                    = new FilePackageOfSrc(childsParent,
                            subPkgItem.getAbsolutePath(),
                            this.isTestSrcCode());

            this.subPackageArr.add(child);
            //  cascade down to have all things completed
            child.calculateSubPackges(fileSrcCodeBaseObj);
            child.initializePackageData(fileSrcCodeBaseObj);
        }
        Collections.sort(subPackageArr);
    }

    /**
     * Get a list of file packages that associate with this file package from
     * the top.
     *
     * @return array list of all packages for this
     */
    final public ArrayList<FilePackageOfSrc> getListOfPackagesFromRoot() {
        // getListOfPackages will get all the packages from top-to-bottom of
        // any FilePackageOfSrc object
        //
        if (this.subPackageArr.isEmpty()) {
            calculateSubPackges(this);
        }
        //
        ArrayList<FilePackageOfSrc> myPackagesArr = new ArrayList<>(20);

        for (FilePackageOfSrc myChild : this.subPackageArr) {
            // adjust to start at the top (true) for each sub-package
            myChild.getListOfPackages(myPackagesArr, "", true);
        }
        return myPackagesArr;
    }

    /**
     * Get the location of the XML report associated with this package (based
     * from root) to report into.
     *
     * @param reportIntoDirFile the report directory for placing reports into
     *
     * @return a File path absolute for the XML report
     */
    final public File getXmlReportFile(File reportIntoDirFile) {
        return getReportFileLocationFor(reportIntoDirFile, "xml");
    }

    /**
     * Get the location of the HTML report associated with this package (based
     * from root) to report into.
     *
     * @param reportIntoDirFile the report directory for placing reports into
     *
     * @return a File path absolute for the HTML report
     */
    final public File getHtmlReportFile(File reportIntoDirFile) {
        return getReportFileLocationFor(reportIntoDirFile, "html");
    }

    /**
     * Get the location of XML/HTML report associated with this package (based
     * from root) to report into.
     *
     * @param reportIntoDirFile the report directory for placing reports into
     *
     * @return a File path absolute for the XML/HTML report
     */
    private File getReportFileLocationFor(File reportIntoDirFile, String xmlOrHtmlStr) {
        // 'projectName_Source Packages.report.xml'
        String xmlReportFileName = String.format("%s_%s.report.%s",
                nbProjectJacoco.ideProjectConfig4Jacoco.getNbProjectDisplayName(),
                this.getSrcFolderDirDisplayName(),
                xmlOrHtmlStr);

        File reportFile = new File(String.format("%s/%s",
                reportIntoDirFile.getAbsolutePath(), xmlReportFileName));

        return reportFile;
    }

    /**
     * Remove old reports from the reports directory depending on report
     * settings.
     *
     * @param reportIntoDirFile the report directory for removing reports from
     */
    final public void removeOldReports(File reportIntoDirFile) {
        if (nbProjectJacoco.usePrincipleNbPrjJac().ideProjectConfig4Jacoco.isReportsTimestampForm()) {
            return;
        }
        // the files are being stored statically
        File xmlRepDelete = getXmlReportFile(reportIntoDirFile).getParentFile();
        FileUtils.deleteQuietly(xmlRepDelete);

        File htmlRepDelete = getHtmlReportFile(reportIntoDirFile).getParentFile();
        FileUtils.deleteQuietly(htmlRepDelete);

        htmlRepDelete.mkdirs();
    }

    /**
     * Build the reports title.
     *
     * @return string of a reports title
     */
    final public String buildReportTitle() {
        return String.format("%s: %s",
                this.nbProjectJacoco.ideProjectConfig4Jacoco.getNbProjectDisplayName(),
                this.getSrcFolderDirDisplayName());
    }

    /**
     * Is the on-code-folder setting state on.
     *
     * @return true if on
     */
    final public boolean isOnCodeFolderState() {
        return this.onCodeFolderState;
    }

    /**
     * Set the state of the on-code-folder setting.
     *
     * @param onCodeFolderState boolean state
     */
    public void setOnCodeFolderState(boolean onCodeFolderState) {
        this.onCodeFolderState = onCodeFolderState;
    }

    /**
     * Get the Netbeans project display name.
     *
     * @return string of the display name
     */
    final public String getNbProjectDisplayName() {
        return this.nbProjectJacoco.getProjectDisplayName();
    }

    /**
     * Get the sub-packages for this file-package
     *
     * @return array-list of the sub-packages for this
     */
    final public ArrayList<FilePackageOfSrc> getSubPackageArr() {
        return subPackageArr;
    }

    /**
     * Get the package strings for the source files.
     *
     * @param packageAlreadyHash hash for packages already acquired
     *
     * @return the include source code information
     */
    private ArrayList<IncludeSrcCodeDirInfo> getAllPackageStringsAndSource() {

        ArrayList<IncludeSrcCodeDirInfo> listAllPackages = new ArrayList<>();

        for (FilePackageOfSrc subPkgFile : this.subPackageArr) {
            String packageFormat = subPkgFile.getPackageFormat();
            listAllPackages.add(new IncludeSrcCodeDirInfo(packageFormat, subPkgFile));

            subPkgFile.getAllPackageStringsAndSourceCascade(listAllPackages);
        }
        return listAllPackages;
    }

    /**
     * Get the package strings for the source files in a cascade fashion.
     *
     * @param packageAlreadyHash hash for packages already acquired
     * @param listAllPackages    array list to add file-packages too
     *
     * @return the include source code information
     */
    final void getAllPackageStringsAndSourceCascade(
            //            HashMap<String, String> packageAlreadyHash,
            ArrayList<IncludeSrcCodeDirInfo> listAllPackages) {
        // check the setting for if included is set
        for (FilePackageOfSrc subPkgFile : this.subPackageArr) {
            String packFormat = subPkgFile.getPackageFormat();

//            if (!packageAlreadyHash.containsKey(packFormat)) {
//                packageAlreadyHash.put(packFormat, "");
            listAllPackages.add(new IncludeSrcCodeDirInfo(packFormat, subPkgFile));
//            }
            subPkgFile.getAllPackageStringsAndSourceCascade(listAllPackages);
        }
    }

    /**
     * Check if the package is to be
     *
     * @param packageFormatOfClass package-format of the form xxx.yyy.zzz.
     *
     * @return true if set to covered for the package
     */
    final public boolean checkIsToBeCovered(String packageFormatOfClass) {
        // is this package set to cover-no which is exact versus
        // cover-on (cover-yes or cover-by-parent) settings

        // workspace 
//        HashMap<String, String> packageAlreadyHashWorkspace = new HashMap<>();
        // all the package paths
        ArrayList<IncludeSrcCodeDirInfo> coveredSettings = getAllPackageStringsAndSource();

        // find the COVER-NO condition if set (versus the cover-yes/-by-parent)
        // an exit as exact setting
        for (IncludeSrcCodeDirInfo iSrcCodeDirInfo : coveredSettings) {
            String sourceCodeCoverSettingPkg = iSrcCodeDirInfo.packageFormat;

            if (sourceCodeCoverSettingPkg.startsWith(packageFormatOfClass)) {
                if (iSrcCodeDirInfo.originSrcCodeFile.coverStateEnum == COVER_NO) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Merge all the source pairs to get the include package strings for all
     * pairs with consideration for excludes list too.
     *
     * @param allSrcCodePairs source-code pairs array list (for root and
     *                        associate-projects)
     * @param excludesList    array list of the excludes
     *
     * @return the includes pattern(s) for processing coverage reports
     */
    final ArrayList<String> mergeAllSourcePairsGetIncludePackageStringsList(
            ArrayList<FileSrcCodeDir> allSrcCodePairs,
            ArrayList<String> excludesList) {

        // on any FileSrcCodeDir we may do a merge to set settings and get the ending 
        // include-packages list
        // key: package-string no dot
        // element: a list of source-code-dir folders
        HashMap<String, PackageAffectingAnalyzer> packageToSrcCodeDirHash = new HashMap<>();
        ArrayList<String> packageToSrcCodeDirListAsKey = new ArrayList<>();

        for (FileSrcCodeDir allSrcOnePair : allSrcCodePairs) {

            // only if the source-code pair is on do we perform merge to determine
            // any sibling-affecting conditions
            if (allSrcOnePair.onCodeFolderState) {
                ArrayList<IncludeSrcCodeDirInfo> pckStrList
                        = allSrcOnePair.getAllPackageStringsAndSource();

                for (IncludeSrcCodeDirInfo inclSrcCodeInfoItem : pckStrList) {
                    String pkgFormatStr = inclSrcCodeInfoItem.packageFormat;

                    if (!packageToSrcCodeDirHash.containsKey(pkgFormatStr)) {
                        // 1st time this package-format has shown up
                        // alloc store
                        packageToSrcCodeDirHash.put(pkgFormatStr, new PackageAffectingAnalyzer(pkgFormatStr));

                        // record the key
                        packageToSrcCodeDirListAsKey.add(pkgFormatStr);
                    }
                    // add item to the list (the 1st one represents the AFFECTING_SIBLINGS
                    PackageAffectingAnalyzer inclSrcCodeDirAnalyzer
                            = packageToSrcCodeDirHash.get(pkgFormatStr);

                    inclSrcCodeDirAnalyzer.add(inclSrcCodeInfoItem);
                    packageToSrcCodeDirHash.put(pkgFormatStr, inclSrcCodeDirAnalyzer);       //99              
                }
            }
        }
        // by sorting the keys of packages we have the order in which to process too
        Collections.sort(packageToSrcCodeDirListAsKey);

        ArrayList<String> uniqueIncludePackageArr = new ArrayList<>();

        // from the sorted collection two things can be done
        // 1) the includes package list
        // 2) the Affecting-state can be applied
        String basePackage = null;
        PackageAffectingAnalyzer affectingAnlzer = null;

        for (String packageKey : packageToSrcCodeDirListAsKey) {
            PackageAffectingAnalyzer keyAffectingAnlzer = packageToSrcCodeDirHash.get(packageKey);

            if (basePackage == null) {
                basePackage = packageKey;
                affectingAnlzer = keyAffectingAnlzer;

                //99 what about "" package
                uniqueIncludePackageArr.add(packageKey);
            } else {
                // 
                String superPkgFormat = basePackage + ".";
                String subPkgKeyFormat = packageKey + ".";

                // an empty base package is '*' so overrides all patterns
                if (basePackage.isEmpty()
                        || subPkgKeyFormat.startsWith(superPkgFormat)) {
                    // this means the current package-key is overridden
                    // by the super-package-format
                    keyAffectingAnlzer.checkForSuperPackageFormat(affectingAnlzer);
                } else {
                    basePackage = packageKey;
                    affectingAnlzer = keyAffectingAnlzer;

                    //99 what about "" package
                    uniqueIncludePackageArr.add(packageKey);
                }
            }
        }
        // have all the data merge, now process the affecting settings
        // for each package (not unique arrangement)

        for (String packageKey : packageToSrcCodeDirListAsKey) {
            PackageAffectingAnalyzer keyAffectingAnlzer = packageToSrcCodeDirHash.get(packageKey);

            keyAffectingAnlzer.setAffectingState(excludesList);
        }

        return uniqueIncludePackageArr;

//        HashMap<String, ArrayList<IncludeSrcCodeDirInfo>> mergeResultHash = new HashMap<>();
//
//        // the mergedList continues all the packages with duplicate package names
//        // but different source-code-dir, need to calculate the double
//        for (ArrayList<IncludeSrcCodeDirInfo> pkgSrcCodeListItem : resultsPackageStringsArr) {
//
//            for (IncludeSrcCodeDirInfo inclSrcCodeItem : pkgSrcCodeListItem) {
//                String packageFormatStr = inclSrcCodeItem.packageFormat;
//
//                if (!mergeResultHash.containsKey(packageFormatStr)) {
//                    mergeResultHash.put(packageFormatStr, new ArrayList<>());
//                }
//                ArrayList<IncludeSrcCodeDirInfo> srcCodeOrigin = mergeResultHash.get(packageFormatStr);
//
//                srcCodeOrigin.add(inclSrcCodeItem);
//            }
//        }
//        // the merged list contains all the packages with a list of source-code
//        // originals, if there are two or more source-code-origins then there
//        // are affected-sibling conditions
//        ArrayList<IncludeSrcCodeDirInfo> mergedList = new ArrayList<>();
//
//        for (String pkgFormatKey : mergeResultHash.keySet()) {
//            ArrayList<IncludeSrcCodeDirInfo> srcCodeOriginList = mergeResultHash.get(pkgFormatKey);
//
//            // if more than one entry for the package-format we know there is an
//            // affects relationship of some kind
//            //
//            // HOWEVER, the source originator needs to be on-code-folder
//            // AND the is-coverage COVER_YES
//            //
//            // the former on-code-folder has already been processed
//            //
//            // remove from the list all items that are "not COVER_YES'
//            //
//            if (srcCodeOriginList.size() > 1) {
////                if (!srcCodeOriginList.isEmpty()) {
//                for (IncludeSrcCodeDirInfo srcCodeOrigin : srcCodeOriginList) {
//
//                    // the first item with true becomes the affecting others
//                    if (srcCodeOrigin.originSrcCodeFile.coverStateEnum == COVER_YES) {
//                        mergedList.add(srcCodeOrigin);
//                        break;
//                    }
//                }
//            }
//        }
//
//        // now have a list of unique package-strings from all source-code includes
//        Collections.sort(mergedList, new IncludeSrcCodeDirInfoComparator());
//
//        // convert to list of package-strings and a HashMap ( for setting 
//        // the sibling relationship)
//        ArrayList<String> includesList = new ArrayList<>();
//        HashMap<String, IncludeSrcCodeDirInfo> mergedHash = new HashMap<>();
//
//        for (IncludeSrcCodeDirInfo inclItem : mergedList) {
//            String packFormat = inclItem.packageFormat + ".";
//
//            includesList.add(packFormat + "*");
//            mergedHash.put(packFormat, inclItem);
//        }
//        // mark ALL source-code dir packages if affected by other directory 
//        // settings. Basically a sibling may have "*" which will set all
//        // siblings as "*", or "org.*" would cause sibling "org.*" to be
//        // considered as well
//        //
//        // this sibling effect is shown in the packing configuration tables
//        for (FileSrcCodeDir srcFilePair : allSrcCodePairs) {
//            srcFilePair.setAllSrcDirsSiblingSetting(mergedHash, excludesList);
//        }
//        return includesList;
    }

//99    final ArrayList<String> mergeAllSourcePairsGetIncludePackageStringsList_ORIG(
//            ArrayList<FileSrcCodeDir> allSrcCodePairs,
//            ArrayList<String> excludesList) {
//
//        // on any FileSrcCodeDir we may do a merge to set settings and get the ending 
//        // include-packages list
//        ArrayList<ArrayList<IncludeSrcCodeDirInfo>> resultsPackageStringsArr = new ArrayList<>(4);
//        HashMap<String, String> packageAlreadyHash = new HashMap<>();
//
//        for (FileSrcCodeDir allSrcOnePair : allSrcCodePairs) {
//            // only if the source-code pair is on do we perform merge to determine
//            // any sibling-affecting conditions
//            if (allSrcOnePair.onCodeFolderState) {
//                ArrayList<IncludeSrcCodeDirInfo> pckStrList
//                        = allSrcOnePair.getAllPackageStringsAndSource(packageAlreadyHash);
//
//                resultsPackageStringsArr.add(pckStrList);
//            }
//        }
//
//        HashMap<String, ArrayList<IncludeSrcCodeDirInfo>> mergeResultHash = new HashMap<>();
//
//        // the mergedList continues all the packages with duplicate package names
//        // but different source-code-dir, need to calculate the double
//        for (ArrayList<IncludeSrcCodeDirInfo> pkgSrcCodeListItem : resultsPackageStringsArr) {
//
//            for (IncludeSrcCodeDirInfo inclSrcCodeItem : pkgSrcCodeListItem) {
//                String packageFormatStr = inclSrcCodeItem.packageFormat;
//
//                if (!mergeResultHash.containsKey(packageFormatStr)) {
//                    mergeResultHash.put(packageFormatStr, new ArrayList<>());
//                }
//                ArrayList<IncludeSrcCodeDirInfo> srcCodeOrigin = mergeResultHash.get(packageFormatStr);
//
//                srcCodeOrigin.add(inclSrcCodeItem);
//            }
//        }
//        // the merged list contains all the packages with a list of source-code
//        // originals, if there are two or more source-code-origins then there
//        // are affected-sibling conditions
//        ArrayList<IncludeSrcCodeDirInfo> mergedList = new ArrayList<>();
//
//        for (String pkgFormatKey : mergeResultHash.keySet()) {
//            ArrayList<IncludeSrcCodeDirInfo> srcCodeOriginList = mergeResultHash.get(pkgFormatKey);
//
//            // if more than one entry for the package-format we know there is an
//            // affects relationship of some kind
//            //
//            // HOWEVER, the source originator needs to be on-code-folder
//            // AND the is-coverage COVER_YES
//            //
//            // the former on-code-folder has already been processed
//            //
//            // remove from the list all items that are "not COVER_YES'
//            //
//            if (srcCodeOriginList.size() > 1) {
////                if (!srcCodeOriginList.isEmpty()) {
//                for (IncludeSrcCodeDirInfo srcCodeOrigin : srcCodeOriginList) {
//
//                    // the first item with true becomes the affecting others
//                    if (srcCodeOrigin.originSrcCodeFile.coverStateEnum == COVER_YES) {
//                        mergedList.add(srcCodeOrigin);
//                        break;
//                    }
//                }
//            }
//        }
//
//        // now have a list of unique package-strings from all source-code includes
//        Collections.sort(mergedList, new IncludeSrcCodeDirInfoComparator());
//
//        // convert to list of package-strings and a HashMap ( for setting 
//        // the sibling relationship)
//        ArrayList<String> includesList = new ArrayList<>();
//        HashMap<String, IncludeSrcCodeDirInfo> mergedHash = new HashMap<>();
//
//        for (IncludeSrcCodeDirInfo inclItem : mergedList) {
//            String packFormat = inclItem.packageFormat + ".";
//
//            includesList.add(packFormat + "*");
//            mergedHash.put(packFormat, inclItem);
//        }
//        // mark ALL source-code dir packages if affected by other directory 
//        // settings. Basically a sibling may have "*" which will set all
//        // siblings as "*", or "org.*" would cause sibling "org.*" to be
//        // considered as well
//        //
//        // this sibling effect is shown in the packing configuration tables
//        for (FileSrcCodeDir srcFilePair : allSrcCodePairs) {
//            srcFilePair.setAllSrcDirsSiblingSetting(mergedHash, excludesList);
//        }
//        return includesList;
//    }
//99    /**
//     * Set the affected or affecting setting for sibling directory files
//     * (packages) for this source file directory.
//     *
//     * @param mergedHash   merging data for the pattern matching
//     * @param excludesList array list of the excludes
//     */
//    final protected void setAllSrcDirsSiblingSetting(HashMap<String, IncludeSrcCodeDirInfo> mergedHash,
//            ArrayList<String> excludesList) {
//
//        // assume not affected unless the tests below prove otherwise
//        this.affectedBySiblingState = NOT_AFFECTED;
//        this.affectingObjectsArr.clear();
//
//        if (this instanceof FilePackageOfSrc) {
//            FilePackageOfSrc self = ((FilePackageOfSrc) this);
//            String selfPkgStr = self.getPackageFormat() + ".";
//
//            // excludes are applied firat
//            for (String excludeExplicit : excludesList) {
//                if (selfPkgStr.startsWith(excludeExplicit)) {
//                    this.setAllSrcDirsSiblingSettingOnChildren(AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES, null);
//                    this.affectedBySiblingState = AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
//                    return;
//                }
//            }
//
//            if (mergedHash.containsKey(selfPkgStr)) {
//                // find the source-of which is affecting 'self' as it the same
//                // package setting
//                FilePackageOfSrc srcOfAffectingSelf = mergedHash.get(selfPkgStr).originSrcCodeFile;
//
//                // need to determine if affecting or affected
//                boolean setAffecting = (self.getSrcCodeDirBase() == srcOfAffectingSelf.getSrcCodeDirBase());
//
//                // if the source of is not on
//                PackageAffectingState affectingState = (setAffecting ? AFFECTING_OTHERS : AFFECTED_BY_A_SIBLING);
//
//                if (affectingState == AFFECTED_BY_A_SIBLING) {
//                    if (self == srcOfAffectingSelf) {
//                        affectingState = NOT_AFFECTED;
//                    } else {
//                        // affects-others, so set the state and record the list
//                        self.affectedBySiblingSource = srcOfAffectingSelf;
//
//                        srcOfAffectingSelf.affectingObjectsArr.add(self);
//                    }
//                    // affected siblings, with no more processing
//                    this.setAllSrcDirsSiblingSettingOnChildren(affectingState, srcOfAffectingSelf);
//                }
//                this.affectedBySiblingState = affectingState;
//
//            } else {
//                this.affectedBySiblingState = NOT_AFFECTED;
//
//                for (FilePackageOfSrc child : this.subPackageArr) {
//                    child.setAllSrcDirsSiblingSetting(mergedHash, excludesList);
//                }
//            }
//        } else {
//            this.affectedBySiblingState = NOT_AFFECTED;
//            if (!subPackageArr.isEmpty()) {
//                this.subPackageArr.get(0).setAllSrcDirsSiblingSetting(mergedHash, excludesList);
//            }
//        }
//    }
    /**
     * Set the affected or affecting setting for sibling directory files
     * (packages) for this source file directory children.
     *
     * @param state           the state to set the children of this file
     * @param affectingSource which file setting is causing the affect on the
     *                        children
     */
    private void setAllSrcDirsSiblingSettingOnChildren(PackageAffectingState state,
            FilePackageOfSrc affectingSource) {
        for (FileSrcCodeDir child : this.subPackageArr) {
            child.affectedBySiblingState = state;
            child.affectedBySiblingSource = affectingSource;
            child.setAllSrcDirsSiblingSettingOnChildren(state, affectingSource);
        }
    }

    /**
     * Get the affected-by state for this file.
     *
     * @return the affecting state
     */
    final public PackageAffectingState getAffectedBySibling() {
        return affectedBySiblingState;
    }

    /**
     * Set this file is not-affected.
     */
    final public void setNotAffected() {
        this.affectedBySiblingState = NOT_AFFECTED;
    }

    /**
     * Get the array of affecting objects this object affects.
     *
     * @return array-list of the objects being affecting
     */
    final ArrayList<FilePackageOfSrc> getAffectingObjectsArr() {
        return this.affectingObjectsArr;
    }
    
    /**
     * Cleat the affecting objects array due to a recalculation of association
     * with includes, excludes settings.
     */
    final public void clearAffectingObjectsArr(){
        this.affectingObjectsArr.clear();
    }
}
