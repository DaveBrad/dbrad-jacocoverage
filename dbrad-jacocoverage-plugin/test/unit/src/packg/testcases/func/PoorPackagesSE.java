/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import junit.framework.Test;
import static junit.framework.TestCase.assertEquals;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.TableOtd;
import packg.appfunc.otdextensions.JsonValuesOtd;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.tstenv.PrepareProject;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = TestHelloWorldMultiSE.class)
public class PoorPackagesSE extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    // - - - -  objects created and for use by following sequential testcases
    private static PrepareProject prepareProject;
    private static FuncProjectProperties fcPP;

    private static File rootModel;

    private static TableOtd packageFilterTableOtd;
    private static JsonValuesOtd jsonOtd;

    private static final String testModelName = "TestModelPoorPackaging";

    /** nojdoc
     *
     * @param testName */
    public PoorPackagesSE(String testName) {
        super(testName);
    }

    static ArrayList<File> modelArr;

    /** nojdoc */
    @JacocoTestMethod
    public void prepareLaunchIde() {

        prepareProject = new PrepareProject();

        // jelly-test-case, root-model, associated projects
        fcPP = prepareProject.openProjects(this, testModelName);

        if (packageFilterTableOtd == null) {
            String myClassName = getClass().getSimpleName();

            packageFilterTableOtd = new TableOtd(fcPP,
                    _OtddataClass.sourceDir(), _OtddataClass.packageString(),
                    myClassName + "TD");

            jsonOtd = new JsonValuesOtd(
                    fcPP.getJsonFilePath(),
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "JsonTD"
            );
        }
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void loadsAsProjectSpecific() {
        // buttons are changed
        assertEquals(false, fcPP.radioGlobal().isSelected());
        assertEquals(true, fcPP.radioProjectSpecific().isSelected());

        JsonValuesOtd projectTst = jsonOtd.getSingle(0, "projectSetting",
                EnumSet.of(JsonValuesOtd.JsonDataTypes.JSON_ALL));

        projectTst.verify();

        TableOtd otdObj = packageFilterTableOtd.getSingle(0, "validatePkgFilterTable", testModelName);
        otdObj.verify();
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTestsPoor() {
        //
        prepareProject.closeAllProjects();
    }
}
