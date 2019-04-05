/* Copyright (c) 2017 dbradly. All rights reserved. */

package dbrad.jacocoverage.plugin.config.ep;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author dbradley
 */
public class EpTextField extends JTextField implements KeyListener {

    private static final long serialVersionUID = 0L;
    
 

    @SuppressWarnings("LeakingThisInConstructor")
    public EpTextField() {
        super();
        super.addKeyListener(this);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public EpTextField(String text) {
        super(text);
        super.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String myText = this.getText();

        if (myText.startsWith("->")) {
            this.setText(myText.substring(2));
        }
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
