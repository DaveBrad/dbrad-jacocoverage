/* Copyright (c) 2017 dbradley. All rights reserved. 
 */
package packg.zoperation.tstenv;

import packg.zoperation.ann.JacocoTestClass;
import packg.zoperation.ann.JacocoCloser;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import junit.framework.TestCase;
import org.netbeans.junit.NbModuleSuite;
import org.openide.util.Exceptions;
import packg.zoperation.ann.JacocoClassStandalone;
import packg.zoperation.ann.JacocoTestMethod;

/**
 * Class that organizes the dbrad-jacocoverage test-case-methods order of
 * sequential execution by building a list of methods from a test-case-class
 * within the dbrad-jacocoverage test environment.
 * <p>
 * GUI tests require that tests run in a sequence and some organization is
 * required to make this happen. This class is one of a group of classes that
 * are part of the dbrad-jacocoverage test environment.
 * <p>
 * See/view the file (file view) <br>
 * 'test/qa-functional/src/_documentation/Functional_test_design.html' <br>
 * document for a brief on the functional test design and how-to code
 * test-classes/methods for sequential functional test running.
 *
 * @author dbradley
 */
public class TestCaseOrganizer {

    /**
     * Holder of marked classes and methods that are organized as sequential
     * run-able.
     * <p>
     * key: marked-test-case element: organized array-list of
     * marked-test-methods (test-cases) within a test-class
     */
    private final HashMap<Class<? extends TestCase>, ArrayList<String>> clzzAndTestMethodHash = new HashMap<>();

    /** organized list of classes as keys to 'clzzAndTestMethodHash' */
    private final ArrayList<Class<? extends TestCase>> keyOfOrganizedClassesList = new ArrayList<>();

    /** Create a test case organizer instance */
    public TestCaseOrganizer() {
        // 
    }

    /**
     * Add the test-cases in sequential order from all the classes as coded by
     * the @JacocoTestClass and @JacocoTestMethod annotation directives.
     *
     * @param conf       NbModuleSuite.Configuration test-cases are to be added
     *                   to
     * @param clz4Single null for all classes to be scanned for, or only a
     *                   single class to scan on
     *
     * @return the updated configuration (NbModuleSuite.Configuration)
     *
     * @throws URISyntaxException if the URI of the test-cases cannot be
     *                            processed (unlikely to happen, but is critical
     *                            if it does happen)
     */
    public NbModuleSuite.Configuration addNbModuleSuiteTestCases(NbModuleSuite.Configuration conf,
            Class<? extends TestCase> clz4Single) throws URISyntaxException {

        if (clz4Single == null) {
            loadTestClassesFromClassPath();

            // organize the classes that are marked and store in common variable
            organizeClassesMarked();
        } else {
            keyOfOrganizedClassesList.add(clz4Single);
        }

        // classes are half the task, need to associate test-case-methods with the classes
        for (Class<? extends TestCase> clzTestCase : keyOfOrganizedClassesList) {
            ArrayList<String> testCasesOrganizedList = organizeMethodsMarked(clzTestCase);

            clzzAndTestMethodHash.put(clzTestCase, testCasesOrganizedList);
        }

        for (Class<? extends junit.framework.TestCase> clzTestCase : keyOfOrganizedClassesList) {
            ArrayList<String> testCasesOrganizedList = clzzAndTestMethodHash.get(clzTestCase);

            String[] testNameArray = testCasesOrganizedList.toArray(new String[testCasesOrganizedList.size()]);

            conf = conf.addTest(clzTestCase, testNameArray);
        }

        return conf;
    }

    /**
     * Find the testable classes that are loaded and provide a hash of them for
     * organization.
     *
     * @return hash of marked classes (key: marked class, element:
     *         dependsOnClass marked class)
     */
    private HashMap<Class<? extends TestCase>, Class<? extends TestCase>> findTestableClasses() {
        // the list of loaded-classes should contain classes that are marked
        // for testing, extract the classes
        ArrayList<Class<? extends TestCase>> listOfClasses = getClassesLoadedList();

        // have a list of the classes, now need to select only those are
        // marked with JacocoTestMethod
        HashMap<Class<? extends TestCase>, Class<? extends TestCase>> markedClassesHash = new HashMap<>();

        // a for(Object obj : vect) will have a concurrent modification issue
        // so good old fashioned 'for i' loop
        int lenList = listOfClasses.size();
        for (int i = 0; i < lenList; i++) {

            Object obj = listOfClasses.get(i);
            if (obj instanceof Class) {
                Class<? extends TestCase> clzObj = (Class<? extends TestCase>) obj;

                // exclude any standalone jacoco classes
                if (clzObj.isAnnotationPresent(JacocoClassStandalone.class)) {
                    continue;
                }

                // filter the class by annotation
                boolean jacocTcBool = clzObj.isAnnotationPresent(JacocoTestClass.class);

                if (jacocTcBool) {
                    Annotation annotationObj = clzObj.getAnnotation(JacocoTestClass.class);
                    markedClassesHash.put(clzObj, ((JacocoTestClass) annotationObj).dependsOnClass());
                }
            }
        }
        return markedClassesHash;
    }

    /**
     * Get a list of classes that are loaded.
     *
     * @return array-list of classes loaded
     */
    private ArrayList<Class<? extends TestCase>> getClassesLoadedList() {
        // get the classes from those loaded by the class-loader
        ClassLoader clzLoader = this.getClass().getClassLoader();
        Class<?> clzLoaderClass = clzLoader.getClass();

        // by rights should always find the classes from the 'classes'
        // field, however, there is the possibility that loader code is
        // changed and thus this approach will break (Thus, critical fail)
        boolean notFound = true;
        Field classesFieldObj = null;

        // limit the loop, and critical fail if the classes not fould 
        int criticalError = 20;

        // loop until not found or critical-error occurs
        while (notFound) {
            try {
                // looking for the "classes" fiel, also in the super-classes if necessary
                classesFieldObj = clzLoaderClass.getDeclaredField("classes");
                notFound = false; // found

            } catch (NoSuchFieldException ex) {
                clzLoaderClass = clzLoaderClass.getSuperclass();
            } catch (SecurityException ex) {
                throw new RuntimeException("Critical error: security condition.", ex.getCause());
            }
            // control while loop
            criticalError--;
            if (criticalError < 0) {
                throw new RuntimeException("Critical: Cannot find 'classes' in class loader to determine loaded classes.");
            }
        }
        // the 'classes' field is likely inaccessible, so use reflection 
        // to by-pass, but restore when done
        @SuppressWarnings("null")
        boolean accessible = classesFieldObj.isAccessible();
        classesFieldObj.setAccessible(true);

        // convert the 'classes' field object-instance to processible class-type
        ArrayList<Class<? extends TestCase>> listOfLoadedClasses = new ArrayList<>();
        try {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector vectorOfLoadedClasses = (Vector) classesFieldObj.get(clzLoader);

            // make a copy of the classes from obsolete Vector to anArrayList 
            // with class-type matching
            for (Object clzObj : vectorOfLoadedClasses) {
                Class<? extends TestCase> clzOOO = (Class<? extends TestCase>) clzObj;

                listOfLoadedClasses.add(clzOOO);
            }

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Critical: Unable to process field.", ex.getCause());
        }
        // restore field accessibility
        classesFieldObj.setAccessible(accessible);

        return listOfLoadedClasses;
    }

    /**
     * Organize the marked classes in to an order of sequential execution as per
     *
     * @JacocoTestClass dependsOnClass relationship.
     *
     */
    private void organizeClassesMarked() {
        HashMap<Class<? extends TestCase>, Class<? extends TestCase>> foundClassHash = findTestableClasses();

        ArrayList<Class<? extends TestCase>> clzzKeyList = new ArrayList<>();

        for (Class<? extends TestCase> clz : foundClassHash.keySet()) {
            Class<?> clzDependsOn = foundClassHash.get(clz);

            if (clzDependsOn == JacocoTestClass.class) {
                // this is the root class
                //99 need to deal with doubles
                keyOfOrganizedClassesList.add(clz);
            } else {
                clzzKeyList.add(clz);
            }
        }
        // process the classes
        int keysLenArr = clzzKeyList.size();
        for (int i = 0; i < keysLenArr; i++) {
            // 
            Class<? extends TestCase> lastMarkedClz = keyOfOrganizedClassesList.get(keyOfOrganizedClassesList.size() - 1);

            for (Class<? extends TestCase> keyClz : clzzKeyList) {
                Class<? extends TestCase> clzDependsOn = foundClassHash.get(keyClz);

                if (clzDependsOn == lastMarkedClz) {
                    keyOfOrganizedClassesList.add(keyClz);
                    break;
                }
            }
            lastMarkedClz = keyOfOrganizedClassesList.get(keyOfOrganizedClassesList.size() - 1);
            clzzKeyList.remove(lastMarkedClz);
        }

        // provide a progress message as to the class order
        String clzRunOrderString = "Class order is: \n";
        for (Class<?> clzOrder : keyOfOrganizedClassesList) {
            clzRunOrderString += String.format("    %s\n", clzOrder.getSimpleName());
        }
        TestBasicUtils.printTestingMsg(System.out, clzRunOrderString);
    }

    /**
     * Organize the methods marked (annotated) with @JacocoTestMethod or
     *
     * @JacocoCloser and present an array in the sequential order of the
     * test-case-method names to be executed.
     *
     * @param testClass the test-case-class to process for sequential testing
     *
     * @return array-list of test-case-methods names as a sequence
     */
    private ArrayList<String> organizeMethodsMarked(Class<?> testClass) {
        ArrayList<String> testCaseOrderArr = new ArrayList<>();

        // key: method-name   element: dependsOn
        HashMap<String, String> testCaseHash = new HashMap<>();

        // key: dependsOn method name element: method initially assigned
        // catches methods with dependsOn setting is double used
        HashMap<String, String> dependsOnUsedHash = new HashMap<>();

        ArrayList<String> testCaseNoOrderArr = new ArrayList<>();

        // store the methods that are annotated with JacocoTestMethod
        Method[] mthdsArr = testClass.getMethods();

        String closerMethodName = null;

        for (Method mthd : mthdsArr) {
            // only if something is marked with JacocoTestMethod
            Annotation[] annotateArr = mthd.getAnnotations();

            for (Annotation atn : annotateArr) {
                if (atn.annotationType() == JacocoTestMethod.class) {
                    String strOfMthd = mthd.getName();
                    String dependson = ((JacocoTestMethod) atn).dependsOn();

                    // determine if dependsOn is used more than once
                    if (dependsOnUsedHash.containsKey(dependson)) {

                        String msg = String.format("Second use of dependsOn=%s\n"
                                + "  Between method: %s <-> %s\n"
                                + "  Please correct double usage.\n",
                                dependson,
                                strOfMthd,
                                dependsOnUsedHash.get(dependson));

                        throw new RuntimeException(msg);
                    }
                    dependsOnUsedHash.put(dependson, strOfMthd);

                    testCaseNoOrderArr.add(strOfMthd);
                    testCaseHash.put(strOfMthd, dependson);

                } else if (atn.annotationType() == JacocoCloser.class) {
                    if (closerMethodName != null) {
                        throw new RuntimeException("More then one method with @JacocoCloser annotation.");
                    }
                    String strOfMthd = mthd.getName();
                    closerMethodName = strOfMthd;
                }
            }
        }
        ArrayList<String> testCaseOrder4ClassArr = new ArrayList<>();

        int numOfMethods = testCaseNoOrderArr.size();

        for (int i = 0; i < numOfMethods; i++) {

            for (String keyTestCaseName : testCaseHash.keySet()) {
                String dependsOnStr = testCaseHash.get(keyTestCaseName);

                if (testCaseOrder4ClassArr.isEmpty()) {
                    if (dependsOnStr.isEmpty()) {
                        testCaseOrder4ClassArr.add(0, keyTestCaseName);
                        break;
                    }
                } else {
                    String lastItemStr = testCaseOrder4ClassArr.get(testCaseOrder4ClassArr.size() - 1);

                    if (lastItemStr.equals(dependsOnStr)) {
                        testCaseOrder4ClassArr.add(keyTestCaseName);
                        break;
                    }
                }
            }
            String lastStoredStr = testCaseOrder4ClassArr.get(testCaseOrder4ClassArr.size() - 1);
            testCaseHash.remove(lastStoredStr);
        }
        // add the testcase to the ordered list
        for (String tc : testCaseOrder4ClassArr) {
            testCaseOrderArr.add(tc);
        }
        // add the closer method at the end
        if (closerMethodName != null) {
            testCaseOrderArr.add(closerMethodName);
        }
        return testCaseOrderArr;
    }

    /**
     * Load the test-case-class onto the current class-loader so as to process
     * any annotations, so as to organize classes and methods into sequential
     * order processing.
     *
     * @throws URISyntaxException
     */
    private void loadTestClassesFromClassPath() throws URISyntaxException {
        ClassLoader classLoader = this.getClass().getClassLoader();

        String packageName = this.getClass().getName().replaceAll("\\.", "/");

        String className = packageName + ".class";

        URI selfPackage = classLoader.getResource(className).toURI();

        // assume all the classes of this package class are going to be
        // associated with the testing (doing otherwise is more complex and
        // does not have any significant gain (K.I.S.S.)
        //
        // the classes directory is what we want for testing purpsoes
        // If a JAR then there is a problem and its not appropriate
        //
        // none package is not supported by JellyTools/junit.framework so
        // good assumption that test-classes will be a sub-item
        String packageStr = selfPackage.getPath();

        String classesDir = packageStr.substring(0, packageStr.lastIndexOf(packageName));
        int stripClassDir = classesDir.length();

        classesDir = String.format("%s%s", classesDir,
                packageName.substring(0, packageName.indexOf("/")));

        loadClass(new File(classesDir), classLoader, stripClassDir);
    }

    /**
     * Load a class from its package-name and class-load object.
     *
     * @param packageDir      the package to process files from
     * @param clzLoader       the class-loader to load a class onto
     * @param stripFrontChars number of characters to strip from the front of a
     *                        class full path
     */
    private void loadClass(File packageDir, ClassLoader clzLoader, int stripFrontChars) {
        // there is an mismatch between the Java classes package name formats and
        // java File listfiles strings and get File.getAbsolutePath
        //
        // package-name will be 
        // '/home/user1/NetBeansProjects/dbrad-jacoco....plugin/build/test/unit/classes/packg
        // '/G:/ws_area/git_clone_repos/dbrad-jacoco....plugin/build/test/unit/classes/packg
        //
        // HOWEVER, when doing a file list
        // '/home/user1/NetBeansProjects/dbrad-jacoco....plugin/build/test/unit/classes/packg
        // 'G:/ws_area/git_clone_repos/dbrad-jacoco....plugin/build/test/unit/classes/packg
        //
        // NB: the leading slash is not present for the Windows file-list processing
        //      (Java pacjake-names were with a leading slash
        //
        // Thus any calculations between Windows or Linux using path lengths will
        // cause a problem

        File[] listOfDir = packageDir.listFiles();

        for (File f : listOfDir) {
            if (f.isDirectory()) {
                loadClass(f, clzLoader, stripFrontChars);
                continue;
            }
            // assume its a class file, oops potential
            String clzNameToLoad = f.getAbsolutePath();

            if (!clzNameToLoad.endsWith(".class")) {
                continue;
            }
            int stripFrontCharsAdjusted = stripFrontChars;
            if (clzNameToLoad.charAt(0) != '/') {
                // assume need to do an adjustment
                stripFrontCharsAdjusted--;
            }
            try {
                clzNameToLoad = clzNameToLoad.substring(stripFrontCharsAdjusted, clzNameToLoad.length() - 6);

                clzNameToLoad = clzNameToLoad.replaceAll("\\\\", ".");
                clzNameToLoad = clzNameToLoad.replaceAll("/", ".");

                // remove the ".class" from the name
                clzLoader.loadClass(clzNameToLoad);
            } catch (ClassNotFoundException ex) {
                // nothing can be done other than the report
                Exceptions.printStackTrace(ex);
            }
        }
    }
    // should now have all the tests loaded and ready to run or
    // review for annotations
}
