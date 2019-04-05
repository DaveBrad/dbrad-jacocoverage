/* Copyright (c) 2017-2018 dbradley. All rights reserved. */

package tstbase;

import suitemodule1.Model1ClassA;
import suitemodule2.Model2ClassA;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dbradley
 */
public class NbM2Test {

    public NbM2Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void nbM2TestSimple() {
        System.out.println("nbM2TestSimple run");
    }
@Test
    public void nbM2TestMod2Class() {
        Model2ClassA.printModel2ClassA(NbM2Test.class.getSimpleName());
    }
@Test
    public void nbM2TestMod1Class() {
        Model1ClassA.printModel1ClassA(NbM2Test.class.getSimpleName());
    }
}
