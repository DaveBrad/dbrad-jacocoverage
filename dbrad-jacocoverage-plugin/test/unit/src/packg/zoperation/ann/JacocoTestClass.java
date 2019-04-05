/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class used to mark a class as a test-case-class within the 
 * dbrad-jacocoverage test environment.
 * <pre>
 * @JaccocTestClass
 * public class ClassTc1 {
 *
 *   @JacocoTestMethod                              base test-case-method
 *   public void tcA(){ ::::
 *
 *   @JacocoTestMethod (dependsOn = "tcA")          next test-case-method
 *   public void tcB(){ ::::
 *
 *   @JacocoTestMethod (dependsOn = "tcB")          next test-case-method
 *   public void tcC(){ ::::
 *
 *   @JacocoCloser
 *   public void closerTC{ ::::
 * }
 *
 * @JaccocTestClass (dependsOnClass = "ClassTc1")
 * public class ClassTc2 {
 *
 * </pre>
 * <p>
 * The TestCaseOrganizer ensures test-case-classes annotated thus will be in the
 * appropriate sequential order as defined by the dependsOnClass setting. There
 * is no checking that sequencing is out-of-whack.
 *
 * @author dbradley
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface JacocoTestClass {

    /**
     * optional dependsOnClass="previous test-case-class name"
     *
     * @return the string of the previous test-case-class name
     */
    public Class dependsOnClass() default JacocoTestClass.class;
}
