/* Copyright (c) 2017. All rights reserved. */

import hello.HelloPrint;
import hello.world.WorldPrint;
import otherpck.WorldPrintOtherPacke;

/**
 *
 * @author dbradley
 */
public class TestHelloWorldMultipleDefault {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HelloPrint hp = new HelloPrint();
        WorldPrint wp = new WorldPrint();
        WorldPrintOtherPacke  wpe = new WorldPrintOtherPacke();
        
        System.out.printf("%s %s multiple project test : default package.\n", hp.getHelloString(), wp.getWorldString());  
        
        System.out.printf("%s.\n", wpe.getWorldString());   
    }
}
