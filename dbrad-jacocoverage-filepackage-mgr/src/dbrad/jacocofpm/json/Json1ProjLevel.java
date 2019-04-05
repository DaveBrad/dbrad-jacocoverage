/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.json;

import java.util.HashMap;

/**
 * Class that represents the highest (1st-level) of the JSON data is a HashMap
 * extension.
 * <p>
 * Key: project-name
 * <p>
 * element: 2nd-level of data (source-code (src/test) folder)
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
public class Json1ProjLevel extends HashMap<String, Json2ProjSrcCodeFolder> {

    private static final long serialVersionUID = 0L;

    /**
     * Create the JSON 1st level data object, with a size specified.
     *
     * @param size integer for the size
     */
    public Json1ProjLevel(int size) {
        super(size);
    }
}
