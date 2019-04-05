/* Copyright (c) 2017 dbradly. All rights reserved. */

package dbrad.jacocoverage.plugin.config.projconfig;

import javax.swing.JTextArea;

/**
 *
 * @author dbradley
 */
public class TextareaIncludeDisplay extends JTextArea {

    private static final long serialVersionUID = 0L;

    private static final int TEXT_LENGTH = 106;

    public TextareaIncludeDisplay() {
        // for netbeans GUI designer
        super.setLineWrap(true);
        super.setWrapStyleWord(true);
    }

    @Override
    public void setText(String text) {
        // format the text for better displaying

        String textCpy = text;
        String newText = "";

        int colonIndex = textCpy.lastIndexOf(":", TEXT_LENGTH);
        if (textCpy.length() < TEXT_LENGTH) {
            colonIndex = 0;
        }

        while (colonIndex > 0) {
            newText = String.format("%s%s\n", newText, textCpy.substring(0, colonIndex));

            textCpy = textCpy.substring(colonIndex);
            colonIndex = textCpy.lastIndexOf(":", TEXT_LENGTH);

            if (textCpy.length() < TEXT_LENGTH) {
                colonIndex = 0;
            }
        }
        newText = String.format("%s%s", newText, textCpy);

        super.setText(newText);
    }
}
