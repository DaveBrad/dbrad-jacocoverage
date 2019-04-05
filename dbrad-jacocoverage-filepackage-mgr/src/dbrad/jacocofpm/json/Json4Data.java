/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.json;

import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import java.util.HashMap;

/**
 * Class for the JSON data of the filter settings of each package-format that a
 * source-code folder and Netbeans project may have.
 * <p>
 * Used for load and store of the JSON interface for data management and
 * persistence of a projects needs.
 * <p>
 * This data object directly relates to settings in the package-filter table for
 * principle and associate-project tabs in project-specific settings.
 * <pre>
 * JsonMap
 * 1    K: General preferences
 *   1      "project-specific"        data:
 *   1        ::
 *   1        ::
 *
 * 1   K: Excludes-filter
 *  N+    exclude-package-filter-string1  K:
 *   1      onoroff                       E: data:
 *
 * 1   K: Package-filter-tables
 *  N+   Json1ProjLevel               K: project name
 *   N+    Json2ProjSrcCodeFolder      K: source-code-folder name (scr/test)
 *    N+     Json3PackageFormat         K: package-format string (pck1_1.pck1_2)
 *     1       Json4Data                 E:
 *                "on"                    data:
 *                "isTst"                 data:
 *                "hasJava"               data:
 *                "pfCvr"                 data:
 *
 * N+ multiple keys of      K: key     E: element    data: is data item
 * 1  one-of
 * </pre>
 *
 * @author dbradley
 */
public class Json4Data {

    // the field names that are used in the JSON structure and act as keys
    // to the actual data
    private static final String ON = "on";
    private static final String IS_TST = "isTst";
    private static final String HAS_JAVA = "hasJava";
    private static final String PF_CVR = "pfCvr";

    /* Is the coverage for the package-format on or off */
    private boolean on;

    /* Is a source-code or test-code package of source */
    private boolean isTst;

    /* Known to have a Java file within its path. */
    private boolean hasJava;

    /* The coverage that is expected of the package (most important data */
    private FilePackageCoverStateEnum pfCvr;

    /**
     * Create
     *
     * @param onOffState
     * @param isTestDir
     * @param hasJavaFilesAndChildren
     * @param pfCoverageState
     */
    public Json4Data(boolean onOffState, boolean isTestDir, boolean hasJavaFilesAndChildren, FilePackageCoverStateEnum pfCoverageState) {
        this.on = onOffState;
        this.isTst = isTestDir;
        this.hasJava = hasJavaFilesAndChildren;
        this.pfCvr = pfCoverageState;
    }

    private Json4Data() {
        // block from creation
    }

    /**
     * Create a Json4Data object from the input of a HashMap of the data.
     *
     * @param hashMap4Data
     *
     * @return
     */
    public static Json4Data createFromHash(HashMap<String, Object> hashMap4Data) {

        boolean isOnLoad = (boolean) hashMap4Data.get(ON);
        boolean isTstLoad = (boolean) hashMap4Data.get(IS_TST);
        boolean hasJavaLoad = (boolean) hashMap4Data.get(HAS_JAVA);

        String pfCvrString = (String) hashMap4Data.get(PF_CVR);
        FilePackageCoverStateEnum pfCvrLoad = FilePackageCoverStateEnum.valueOf(pfCvrString);

        return new Json4Data(isOnLoad, isTstLoad, hasJavaLoad, pfCvrLoad);
    }

    /**
     * Is the package/source-folder is ON.
     *
     * @return true of the package/source-folder is ON
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Get the coverage state (cover, no-cover, covered-by-parent) state.
     *
     * @return FilePackageCoverStateEnum coverage state
     */
    public FilePackageCoverStateEnum getPfCvr() {
        return pfCvr;
    }

    /**
     * Is
     *
     * @return
     */
    public boolean isIsTst() {
        return isTst;
    }

    /**
     * Is the package containing Java files
     *
     * @return true if known to have Java files
     */
    public boolean isHasJava() {
        return hasJava;
    }

}
