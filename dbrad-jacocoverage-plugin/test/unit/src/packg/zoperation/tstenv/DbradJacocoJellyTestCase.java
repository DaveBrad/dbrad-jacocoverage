/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.zoperation.tstenv;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import org.netbeans.jellytools.JellyTestCase;
import org.openide.util.Exceptions;
import packg.zoperation.ann.JacocoCloser;

/**
 * The dbrad-jacocoverage class to be extended for JellyTestCase classes in the
 * 'testcases' directory.
 * <p>
 * Provides the setup and teardown methods for each test-case-method that is
 * running. There is no special requirements for setup or teardown of the test
 * cases.
 *
 * @author dbradley
 */
public class DbradJacocoJellyTestCase extends JellyTestCase {

    /**
     * Needs to be static as the test-runner create new objects per method call
     * and as such is unable to pass information across the boundary.
     */
    static boolean previousTestPass = true;

    static Class<?> runningTestCaseClass = null;

    /**
     * Create an instance of this extension of a JellyTestCase s
     *
     * @param testName string name of test name
     */
    public DbradJacocoJellyTestCase(String testName) {
        super(testName);
    }
    
    @Override
    protected int timeOut() {
        return 3600000;
    }
    

    /** Method called before each test case. */
    @Override
    final public void setUp() {
        String simpleName = getClass().getSimpleName();
        System.out.println("######## " + simpleName + ":" + getName() + " #######");
    }

    /** Method called after each test case. */
    @Override
    final public void tearDown() {
        System.out.println("TEARDOWN " + getName() + " #######");
    }

    /**
     * Runs the test case and collects the results in TestResult and has been
     * modified for running in the dbrad-jacoco testing architecture for
     * purposes of aborting testing for following dependent test-cases.
     *
     * @param result
     */
    @Override
    public void run(TestResult result) {
        // need to change the previous fail condition if the class being tested is
        // different
        Class<?> clzNowRunning = this.getClass();

        // provide progress message of the classes being run in the dbrad-jacocoverage
        // test architecture
        if (runningTestCaseClass != clzNowRunning) {
            // this message occurs only on a change of class running
            String stateKind;
            if (runningTestCaseClass == null) {
                stateKind = "Starting ";
            } else {
                stateKind = "Changed  ";
            }
            String mthdNameRunning = getName();

            TestBasicUtils.printTestingMsg(System.out, String.format(" %sclass: %s %s\n",
                    stateKind,
                    clzNowRunning.getSimpleName(),
                    mthdNameRunning));

            // this is a change of class and as such any error/failures from the
            // previous class no longer apply.
            previousTestPass = true;
            runningTestCaseClass = clzNowRunning;
        }
        // based on a previous test in the same class failing
        if (!previousTestPass) {
            // in the Dbrad-jacoco test environment there is a last test-case that does some
            // special clean up (as JellyTestCase does not support the AfterClass capability)
            // and is annotated with @JacocoCloser
            //
            // if this is the method to be run, then let it run so as to cleanup
            boolean isJacocoCloserTestCaseMethod = false;

            try {
                // get the method that is being run and get its annotations
                Method mthd = this.getClass().getMethod(getName());
                Annotation[] annotationArr = mthd.getAnnotations();

                // determine if the @JacocoCloser is set on the method, and set to
                // indicate so (do not process and other annotations
                for (Annotation ann : annotationArr) {
                    if (ann.annotationType() == JacocoCloser.class) {
                        isJacocoCloserTestCaseMethod = true;
                        break;
                    }
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                // nothing can be done
            }
            // nothing can be done
            // if the method is not a @JacocoCloser method, fail it with the indicator
            // that the test-case-method depended on the previous TC (this is the
            // design structure of these test cases as they are functional in nature
            // and need to run sequentially)
            if (!isJacocoCloserTestCaseMethod) {
                // give the reason for a fail and suppress the traceback as it cannot be
                // processed as an error/debug as its not
                AssertionFailedError ae = new AssertionFailedError("depends on previous testcase");
                ae.setStackTrace(new StackTraceElement[0]);

                result.addFailure(this, ae);
                return;
            }
            // otherwise, is @JacocoCloser test-case method
        }
        // run the test as no previous tests have failed, OR the test case is a JacocoCloser
        // method which is a cleanup type of testcase
        super.run(result);
        previousTestPass = result.wasSuccessful();
    }
}
