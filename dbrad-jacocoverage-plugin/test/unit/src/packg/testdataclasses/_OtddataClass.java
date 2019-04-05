/* Copyright (c) dbradley 2017. */
package packg.testdataclasses;

/**
 * This class is an empty class to be used to extract the package information
 * for the otddata folder (the package information) which its location for the
 * OTD test-data of this project. Any refactoring will automatically be applied.
 * <pre>
 * For example:
 *
 * packageFilterTableOtd = new PackageFilterTableOtd(
 * _OtddataClass.sourceDir,
 * _OtddataClass.packageString(),
 * "SingleSeTD", fcPP);
 * </pre> If the package folders are re-factored (via an IDE), this design will
 * automatically cause test-case scripts to be modified too.
 * <p>
 * The class-name begins with '_' so as to force it to be separated in the
 * packages folder list (either top or bottom) from real test-data class files
 * of the OTD-data collection.
 *
 * @author dbradley
 */
public class _OtddataClass {

    static private String sourceDir4TestQaFunctional = null;

    _OtddataClass() {
    }

    /**
     * Gets the package string for this class, which is co-located with other OTD
     * test-data classes.
     *
     * @return package string of this class
     */
    static public String packageString() {
        return _OtddataClass.class.getPackage().getName();
    }

    /** 
     * Gets the source directory path for class.
     * @return 
     */
    static public String sourceDir() {
        if (sourceDir4TestQaFunctional == null) {
            String thisClassRuntime = _OtddataClass.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            // convert to the test/qa-functional/src directory
            // this structure appears to be fixed, but if it changes then things will
            // fail
            thisClassRuntime = thisClassRuntime.replace("/build/", "/");
            sourceDir4TestQaFunctional = thisClassRuntime.replace("/classes", "/src");
        }
        return sourceDir4TestQaFunctional;
    }
}
