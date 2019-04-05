/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class used to mark a method as a test-case-method of a test-case
 * class within the dbrad-jacocoverage test environment.
 * <pre>
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
 * </pre>
 * <p>
 * The TestCaseOrganizer ensures test-case-methods annotated thus will be in the
 * appropriate sequential order as defined by the dependsOn setting. There is
 * no checking that sequencing is out-of-whack.
 *
 * @author dbradley
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface JacocoTestMethod {

    /**
     * optional dependsOn="previous test-case-method name"
     *
     * @return the string of the previous test-case-method name
     */
    String dependsOn() default "";
}
