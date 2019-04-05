/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class used to mark a class as a test-case-class within the 
 * dbrad-jacocoverage test environment, but is standalone and should
 * never be included within collection of test-cases.
 * <p>
 * The TestCaseOrganizer ensures test-case-classes annotated  will excluded from
 * any sequential order mechanisms. There
 * is no checking that sequencing is out-of-whack.
 *
 * @author dbradley
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface JacocoClassStandalone {

    /**
     * @return nothing that applies
     */
    public Class dependsOnClass() default JacocoClassStandalone.class;
}
