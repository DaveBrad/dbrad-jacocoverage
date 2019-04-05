/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packg.zoperation;

import java.util.Properties;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.openide.util.NbBundle;
import static packg.zoperation.tstenv.TestBasicUtils.pauseMs;

/**
 *
 * @author dbradley
 */
public class NbJavacDialog {

    public static void nb10AndJdk11Support() {
        
        String nbVersion = NbBundle.getBundle("org.netbeans.core.startup.Bundle")
                .getString("currentVersion"); 
        
        // netbeans 8.x does not support Jdk 11
        if(nbVersion.contains("IDE 8.")){
            return;
        }
        Properties propTs = System.getProperties();

        // check if we are running with old JAVA version, as at JDK 9 the
        // number went  1.7 -- 1.8 -- 9 -- 10 -- 11
        String version = System.getProperty("java.vm.specification.version");

//        if (!version.startsWith("1.")) {
        // JDK 11 is the standard and relys on nb_javac being installed
        // a dialog popup will cause the following click to freeze 
        // test code execution catTree.clickOnPath(treePath);
        //
        // so do the click in a a thread for the popup dialog
//            Thread someThread = new Thread() {
//
//                @Override
//                public void run() {
        pauseMs(1000);
        // if the project has no JSON data file then a popup dialog pops up indicating 
        // that a new file will be created
        Thread someThread = new Thread() {

            @Override
            public void run() {
                pauseMs(1000);
                // if the project has no JSON data file then a popup dialog pops up indicating 
                // that a new file will be created
                try {
                    JDialogOperator dialogOpWarning = new JDialogOperator("Java features limited");

                    JButtonOperator dialogOkButton
                            = new JButtonOperator(dialogOpWarning, "Install nb-javac");
//                     JButtonOperator dialogOkButton
//                            = new JButtonOperator(dialogOpWarning, "cancel");
                    dialogOkButton.clickMouse();

                } catch (org.netbeans.jemmy.TimeoutExpiredException te) {
                    // there is the possibilty
                }
            }
        };
        // Start a thread to respond to a popup message dialog that is expected
        someThread.start();
        pauseMs(1000);

        pluginInstallerDialog();
    }

    static public void pluginInstallerDialog() {
        pauseMs(5000);
        // if the project has no JSON data file then a popup dialog pops up indicating 
        // that a new file will be created
        try {
            JDialogOperator dialogOpWarning = new JDialogOperator("Plugin installer");

            JButtonOperator dialogNextButton
                    = new JButtonOperator(dialogOpWarning, "Next");
            dialogNextButton.clickMouse();

            ContainerOperator excludeContainerOper
                    = new ContainerOperator(dialogOpWarning);

            JCheckBoxOperator acceptLicenseChkbox
                    = new JCheckBoxOperator(excludeContainerOper,
                            "I accept the terms in all of the license agreements.");
            acceptLicenseChkbox.clickMouse();;

            JButtonOperator dialogInstallButton
                    = new JButtonOperator(dialogOpWarning, "Install");
            dialogInstallButton.clickMouse();

            pauseMs(10000);
            JButtonOperator dialogFinishButton
                    = new JButtonOperator(dialogOpWarning, "Finish");
            dialogFinishButton.clickMouse();

        } catch (org.netbeans.jemmy.TimeoutExpiredException te) {
            // there is the possibilty
        }
        pauseMs(5000);
    }
}
