/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.testcases.func;

import java.net.URISyntaxException;
import packg.zoperation.tstenv.TestCaseOrganizer;
import java.util.logging.Level;
import junit.framework.Test;
import junit.framework.TestCase;
import org.junit.internal.Classes;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.junit.NbModuleSuite;
import org.openide.util.Exceptions;

/**
 * Class that runs all test-case-classes within the dbrad-jacocoverage test
 * environment, done by gathering all test-case-classes in the environment and
 * constructing a run-time test-case class-and-method order of test cases to
 * run.
 * <p>
 * 'A_' on the class name causes the file to be listed at the top of the folder
 * within most IDEs.
 * <p>
 * See/view the file (file view) <br>
 * 'test/qa-functional/src/_documentation/Functional_test_design.html' <br>
 * document for a brief on the functional test design and how-to code
 * test-classes/methods for sequential functional test running.
 *
 * @author dbradley
 */
public class A_RunnerOfAllTest extends JellyTestCase {

    private static boolean runningAlready = false;

    /**
     * Creates suite from particular test cases.
     *
     * @return NbModuleConfiguration for the test framework to run
     */
    public static Test suite() {
        return prepNbModuleSuite(null);
    }

    /**
     * This test case takes a long time to run, so setting a long timeout
     * addresses issues of NbTestCase timeout interactions.
     *
     * @return millisecond timeout value
     */
    @Override
    protected int timeOut() {
        return 3600000;
    }

    /**
     * Prepare the Netbeans-Module-Suite (runner) for processing a
     * test-case-class for sequential running. Also, add organized sequential
     * test-case-methods to be run within the Netbeans-Module-Suite (runner)
     * architecture.
     *
     * @param clzToRun test-case-method to run Netbeans GUI test on
     *
     * @return the Test class for the Netbeans-Module-Suite (runner) with the
     *         test-case-methods added
     */
    private static Test prepNbModuleSuite(Class<? extends TestCase> clzToRun) {
        runningAlready = true;
        NbModuleSuite.Configuration conf = NbModuleSuite.emptyConfiguration();

        conf = conf.reuseUserDir(false);
        conf = conf.enableClasspathModules(true);

        conf = conf.clusters(".*");
        conf = conf.enableModules(".*");
        conf = conf.failOnException(Level.INFO);
        conf = conf.failOnMessage(Level.SEVERE);

        // A critical error may occurred and may not precede as the URI for classes
        // is unavailable or in error (should for the most part not happen unless
        // the runtime environment is set to restrict loading classes)
        TestCaseOrganizer organizer = new TestCaseOrganizer();
        try {
            conf = organizer.addNbModuleSuiteTestCases(conf, clzToRun);

        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        // launch runner for the test-case-class
        return conf.suite();
    }

    /**
     * Run-as-single-suite the invoking class (extended JellyToolsTest)
     * providing a supporting Netbeans window to operator Jelly and Jemmy
     * against for the classes test-case methods to be execute as a
     * single/standalone test run.
     *
     * @return Test instance for NBModuleSuite
     */
    public static Test runAsSingleSuite() {
        if (runningAlready) {
            return null;
        }
        // get the invoking class 
        Exception ee = new Exception();

        StackTraceElement[] stackTraceEleArr = ee.getStackTrace();
        String invokingClass = stackTraceEleArr[1].getClassName();

        Class<?> cl = null;
        try {
            cl = Classes.getClass(invokingClass);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        Class<? extends TestCase> singleClassToRun = (Class<? extends TestCase>) cl;

        Test testObj = prepNbModuleSuite(singleClassToRun); 
        
        return testObj;
    }

    /**
     * Create a test object.
     *
     * @param testName the name associated with the test
     */
    public A_RunnerOfAllTest(String testName) {
        super(testName);
    }
}
