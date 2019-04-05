/* Copyright (c) 2017-2018 dbradley. All rights reserved. */

package tstqa;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import suitemodule2.Model2ClassA;

/**
 *
 * @author dbradley
 */
public class NbMod2QaTestScript {
    
    public NbMod2QaTestScript() {
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
    public void nb2ModQaTestMthd(){
        Model2ClassA.printModel2ClassA(NbMod2QaTestScript.class.getSimpleName());
    }
}
