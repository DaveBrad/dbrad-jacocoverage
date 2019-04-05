/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.awt.Color;
import java.awt.event.KeyEvent;
import junit.framework.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.appfunc.FuncExcludeTab;
import packg.appfunc.FuncPkgFilterTable;
import packg.appfunc.FuncProjectProperties;
import packg.appfunc.otdextensions.JsonValuesOtd;
import packg.appfunc.otdextensions.TableOtd;
import packg.testdataclasses._OtddataClass;
import packg.zoperation.ann.JacocoCloser;
import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.ann.JacocoTestMethod;
import packg.zoperation.tstenv.DbradJacocoJellyTestCase;
import packg.zoperation.tstenv.PrepareProject;

/**
 *
 * @author dbradley
 */
@JacocoTestClass(dependsOnClass = PoorPackagesSE.class)
public class ExcludePackageErrors extends DbradJacocoJellyTestCase {

    /** nojdoc
     *
     * @return
     */
    final public static Test suite() {
        return A_RunnerOfAllTest.runAsSingleSuite();
    }

    private JTabbedPaneOperator tabbPanel;
    private static final String testModelName = "JavaLibraryTestModel1";

    private static PrepareProject prepareProject;
    private static FuncProjectProperties fcPP;

    private static FuncExcludeTab fcET;
    private static JLabelOperator errMsgLabel;
    private static JTextFieldOperator addTF;

    private static JButtonOperator addButtOper;

    private static TableOtd packageFilterTableOtd;
    private static JsonValuesOtd jsonOtd;

    private static FuncPkgFilterTable fRootTable;

    /** nojdoc */
    @JacocoTestMethod
    public void prepareLaunchIde() {
        prepareProject = new PrepareProject();

        fcPP = prepareProject.openProjects(this, testModelName);

        if (packageFilterTableOtd == null) {
            String myClassName = getClass().getSimpleName();

            packageFilterTableOtd = new TableOtd(fcPP,
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "TD");

            jsonOtd = new JsonValuesOtd(fcPP.getJsonFilePath(),
                    _OtddataClass.sourceDir(),
                    _OtddataClass.packageString(),
                    myClassName + "JsonTD");
        }
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "prepareLaunchIde")
    public void checkRadios() {

        fcPP.radioProjectSpecific().push();
        fcPP.pauseMs(500);

        assertEquals(false, fcPP.radioGlobal().isSelected());
        assertEquals(true, fcPP.radioProjectSpecific().isSelected());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "checkRadios")
    public void openExcludeTable() {
        fcET = fcPP.funcExcludeTabb();
        errMsgLabel = fcET.getErrorMsgLabel();
        addTF = fcET.getAddTextField();

        addButtOper = fcET.getAddButton();
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "openExcludeTable")
    public void tstEndWithDotStar() {
        addTF.typeText("*");
        fcET.pauseMs(50);

        checkOnErrorPackage("Must end with '.*'.");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstEndWithDotStar")
    public void tstStarInvalid1stChar() {
        addTF.typeText(".*");
        checkOnErrorPackage("Invalid 1st character '*', package part 1 [*].");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstStarInvalid1stChar")
    public void tstStarInvalidWildcard() {
        addTF.clearText();
        assertEquals(" ", errMsgLabel.getText());

        addTF.typeText("f*.*");
        checkOnErrorPackage("Invalid character '*', package part 1 [f*]. Wildcard disallowed within package-name.");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstStarInvalidWildcard")
    public void tstStarInvalidWildcardDifferentPart() {
        addTF.clearText();

        addTF.typeText("fffff.part2.part3g*gg.*");
        checkOnErrorPackage("Invalid character '*', package part 3 [part3g*gg]. Wildcard disallowed within package-name.");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstStarInvalidWildcardDifferentPart")
    public void tstBackToBackSeparators() {
        addTF.clearText();

        addTF.typeText("fffff..part2.*");
        checkOnErrorPackage("Back-to-back separators found ('..' invalid).");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstBackToBackSeparators")
    public void tstAllInvalid() {
        addTF.clearText();

        addTF.typeText(".*");
        checkOnErrorPackage("\".*\" is disallowed, basically exclude all?");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstAllInvalid")
    public void tstValid() {
        addTF.clearText();

        assertEquals(" ", errMsgLabel.getText());
        assertEquals(Color.black, errMsgLabel.getForeground());
        assertFalse(addButtOper.isEnabled());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstValid")
    public void tstValidPackagesAndAddBtn() {
        addTF.clearText();

        addTF.typeText("pack1.pack2.*");
        checkValidPackage();

        // once the item is added, it is add button becomes disabled
        fcET.doAdd();
        fcET.pauseMs(500);

        assertFalse(addButtOper.isEnabled());
        assertEquals(" ", errMsgLabel.getText());
        assertEquals(Color.black, errMsgLabel.getForeground());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstValidPackagesAndAddBtn")
    public void tstDuplicatePackage() {
        addTF.clearText();

        addTF.typeText("pack1.pack2.*");
        checkOnErrorPackage("Duplicate package name in table.");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstDuplicatePackage")
    public void tstValidPackageByEnter() {
        addTF.clearText();

        addTF.typeText("pack1.pack2.pack3.*");

        // add button is enabled for a valid entry, but once enter is typed (pushKey)
        // the button becomes disabled.
        checkValidPackage();

        addTF.pushKey(KeyEvent.VK_ENTER);
        fcPP.pauseMs(2000);
        assertFalse(addButtOper.isEnabled());
        assertEquals(" ", errMsgLabel.getText());
        assertEquals(Color.black, errMsgLabel.getForeground());
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstValidPackageByEnter")
    public void tstInvalidPackage1stChar() {
        // valid start characters for a package are '_', and alpha-character
        // which is determined by the Java Character class. Testing Character
        // is not part of this testing, but the error messages that are displayed.
        addTF.clearText();
        addTF.typeText("1pack1.*");
        checkOnErrorPackage("Invalid 1st character '1', package part 1 [1pack1].");

        addTF.clearText();
        addTF.typeText("pack1.@pack2.*");
        checkOnErrorPackage("Invalid 1st character '@', package part 2 [@pack2].");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstInvalidPackage1stChar")
    public void tstInvalidPackageInsideChar() {
        // valid start characters for a package are '_', and alpha-character
        // which is determined by the Java Character class. Testing Character
        // is not part of this testing, but the error messages that are displayed.
        addTF.clearText();
        addTF.typeText("p&ack1.*");
        checkOnErrorPackage("Invalid character '&', package part 1 [p&ack1].");
    }

    /** nojdoc */
    @JacocoTestMethod(dependsOn = "tstInvalidPackageInsideChar")
    public void tstInvalidAndEnter() {
        // the enter is just ignored
        addTF.pushKey(KeyEvent.VK_ENTER);
        checkOnErrorPackage("Invalid character '&', package part 1 [p&ack1].");
    }

    /** nojdoc */
    @JacocoCloser
    public void closerTests() {
        prepareProject.closeAllProjects();
    }

    // --------------------------
    /**
     * Create class to test case running for NbModule running.
     * 
     * @param testName string
     */
    public ExcludePackageErrors(String testName) {
        super(testName);
    }

    /**
     * Check test-case package parts for error message with a certain string.
     *
     * @param errorMsgExpected string
     */
    private void checkOnErrorPackage(String errorMsgExpected) {
        assertEquals(errorMsgExpected, errMsgLabel.getText());
        assertEquals(Color.red, errMsgLabel.getForeground());
        assertFalse(addButtOper.isEnabled());
    }

    /**
     * Check test-case package parts for validity.
     *
     * @param errorMsgExpected string
     */
    private void checkValidPackage() {
        assertEquals(" ", errMsgLabel.getText());
        assertEquals(Color.black, errMsgLabel.getForeground());
        assertTrue(addButtOper.isEnabled());
    }
}
