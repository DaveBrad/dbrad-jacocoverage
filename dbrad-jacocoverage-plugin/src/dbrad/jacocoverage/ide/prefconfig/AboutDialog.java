/* 
 * Copyright (c) 2017-2018 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.ide.prefconfig;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Class that is the about dialog.
 */
public class AboutDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Create an about dialog object and display.
     *
     * @param parent the frame/window the dialog associates with
     * @param modal  is the dialog modal
     */
    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;

        int w = super.getWidth() + 130;
        int h = super.getHeight() + 20;

        int posX = (screenWidth / 2) - (w / 2);
        int posY = (screenHeight / 2) - (h / 2);

        super.setBounds(posX, posY, w, h);

        jLabelAbout.setText(
                "<html>"
                + "<body style='padding-left: 8px;'>"
                + "Copyright © 2017-2018 dbradley."
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "License: <i>dbrad-jacocoverage plugin license</i>"
                + "<br><br>"
                + "<p>dbrad-jacocoverage is a significant modification of origin: tikione-jacocoverage.</p>"
                + "&nbsp;&bull; Significant changes to the origin, in essence a different program,"
                + " and is renamed."
                + "<p>&nbsp;&bull; Identifies plug-in as different from other offerings.</p>"
                + "<br><p>A plug-in for Netbeans 8.2(+) which provides code coverage from within the"
                + " IDE using JaCoCoverage (jacocoagent) from "
                + " www.eclemma.org as an on-the-fly coverage tool.</p>"
                + "<br><p>Providing project source &amp; test-source coverage reports for"
                + " J2SE, J2EE and NBM projects. With coverage inclusion of the "
                + " principle-project, its associated/library dependent/sub-projects. "
                + "</p>"
                + "<br><p>'Run w/jacocoverage', 'Test w/jacocoverage' are usable at the principle-project"
                + " level or on individual files (main, or test JUnit, TestNg)."
                + "</p>"
                + "<br><p>Origin was licensed under MIT, WTFPL and some JAR parts LGPL. With the"
                + " significant modifications this program is now licensed under"
                + " its own sub-license: dbrad-jacocoverage plugin license."
                + "</p>"
                + "<br><p>In short-form: free to use executable and modify source (but needs to be renamed"
                + " different from 'dbrad-jacocoverage').</p>"
                + "<br><p>Documentation is copyrighted.</p>"
                + "<br><br>"
                + "</body>"
                + "</html>"
        );

        // Origin tikione-jacocoverage about license statement:
        //        jLabelAbout.setText("<html>Copyright © 2013-2015 Jonathan Lermitage &lt;jonathan.lermitage@gmail.com&gt;<br>\n"
        //                + "This work is free. You can redistribute it and/or modify it under the\n"
        //                + "terms of the Do What The Fuck You Want To Public License, Version 2,\n"
        //                + "as published by Sam Hocevar. See http://www.wtfpl.net for more details.</html>");
        // jButtonSeeWTFPL.setToolTipText("<html><body>Visit the <b>WTFPL</b> website<br>http://www.wtfpl.net</body></html>");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelAbout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.title")); // NOI18N
        setResizable(false);

        jLabelAbout.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAbout, org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.jLabelAbout.text_1")); // NOI18N
        jLabelAbout.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabelAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelAbout;
    // End of variables declaration//GEN-END:variables
}

//99 Change the license in the about
