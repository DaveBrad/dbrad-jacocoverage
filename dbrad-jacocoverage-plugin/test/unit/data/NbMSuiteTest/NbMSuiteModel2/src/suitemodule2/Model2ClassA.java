/* Copyright (c) 2017-2018 dbradley. All rights reserved. */
package suitemodule2;

import suitemodule0.Model0ClassA;
import suitemodule1.Model1ClassA;

/**
 *
 * @author dbradley
 */
public class Model2ClassA {
    
    public static void printModel2ClassA(String source){
        Model0ClassA.printModel0ClassA(Model2ClassA.class.getSimpleName());
        Model1ClassA.printModel1ClassA(Model2ClassA.class.getSimpleName());
        
        System.out.printf("%s: Model2ClassA\n", source);
    }
}
