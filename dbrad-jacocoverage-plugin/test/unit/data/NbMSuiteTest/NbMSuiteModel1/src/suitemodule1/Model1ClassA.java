/* Copyright (c) 2017-2018 dbradly. All rights reserved. */
package suitemodule1;

import suitemodule0.Model0ClassA;

/**
 *
 * @author dbradley
 */
public class Model1ClassA {
    
    public static void printModel1ClassA(String source){
        Model0ClassA.printModel0ClassA(Model1ClassA.class.getSimpleName());
        
        System.out.printf("%s: Model1ClassA\n", source);
    }
}
