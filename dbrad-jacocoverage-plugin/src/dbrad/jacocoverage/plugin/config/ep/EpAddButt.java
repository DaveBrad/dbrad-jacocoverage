/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocoverage.plugin.config.ep;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Class for the exclude packages add button.
 *
 * @author dbradley
 */
public class EpAddButt extends JButton implements KeyListener, ActionListener {

    private static final long serialVersionUID = 0L;

    private EpJPanel epJPanel;
    private JTable excludePackagesJTable;
    private JLabel exclErrorMessageLabel;
    private JTextField addExclTextfield;

    public EpAddButt() {
        super.setEnabled(false);
    }

    public void setPrgCfgData(EpJPanel epJPanel) {
        this.epJPanel = epJPanel;
        this.excludePackagesJTable = epJPanel.jTableExcludePackages;
        this.exclErrorMessageLabel = epJPanel.exclErrorMessageLabel;
        this.addExclTextfield = epJPanel.epTextField1;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //
        char c = e.getKeyChar();

        if (c == KeyEvent.VK_ENTER) {
            // consume 'enter' so the OK button does not get it and close out
            // the dialog
            e.consume();

            // if the ADD-button is enabled it allows the item to be added
            // - avoid relying on OK being pressed to save the settings
            // - 
            if (this.isEnabled()) {
                this.doClick();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();

        //do not do verification when enter is done
        if (c == KeyEvent.VK_ENTER) {
            return;
        }
        // only if the verification passes is there a chance of a 
        // add-button action allowed

        // assume invalid until proved otherwise (closes a potential window for
        // a click on add button while the text-field key is being processed
        this.setEnabled(false);
        this.setEnabled(verify((JTextField) e.getSource()));
    }

    /**
     * Verify the text field has valid entries.
     *
     * @param inputTF the text field that is being processed
     *
     * @return true if valid
     */
    private boolean verify(JTextField inputTF) {
        String updatedPackageStr = inputTF.getText().trim();
        inputTF.setText(updatedPackageStr);   // remove leading/lagging spaces-whitespace

        return validateForPackageString(updatedPackageStr);
    }

    /**
     * Perform the validation for package names formats and uniqueness of the
     * package from the rest of the table
     *
     * @param updatedPackageStr the text field contents
     *
     * @return true if valid
     */
    private boolean validateForPackageString(String updatedPackageStr) {
        // set the valid or not-valid condition for the package
        boolean validState = validPackageName(updatedPackageStr);
        validState = validState && uniquePackageString(updatedPackageStr);

        return validState;
    }

    /**
     * Validate the package-string, as per most Java naming conventions. Set the
     * error message label content if there is an error.
     *
     * @param packageString string from cell and to be a package string
     *
     * @return true if valid
     */
    private boolean validPackageName(String packageString) {

        if (packageString.trim().isEmpty()) {
            // the text-field is empty and no entry and no value
            setErrorMessage(" ");
            return false;
        }
        if (packageString.contains("..")) {
            // have two periods together
            setErrorMessage("Back-to-back separators found ('..' invalid).");
            return false;
        }

        String[] splitParts = packageString.split("\\.");

        // each part needs to fit the Java naming convention for
        // a) first character
        // b) following characters
        // c) as an include/exclude path the last item needs to be an *
        int splitLen = splitParts.length;

        for (int partCnt = 0; partCnt < splitLen; partCnt++) {
            String s = splitParts[partCnt];

            char[] sAsCharArr = s.toCharArray();

            // check the parts characts
            boolean firstChar = true;
            for (char c : sAsCharArr) {
                // test the characters one-by-one
                if (firstChar) {
                    // a) first character
                    if (!Character.isJavaIdentifierStart(c)) {
                        // c) as an include/exclude path the last item needs to be an *

                        if (!(s.equals("*") && partCnt == splitLen - 1)) {
                            setErrorMessage(String.format("Invalid 1st character '%c', package part %d [%s].", c, partCnt + 1, s));
                            return false;
                        }
                    }
                    firstChar = !firstChar;
                } else {
                    // b) following characters
                    if (!Character.isJavaIdentifierPart(c)) {
                        String errMsg = String.format("Invalid character '%c', package part %d [%s].", c, partCnt + 1, s);
                        if (c == '*') {
                            errMsg += " Wildcard disallowed within package-name.";
                        }
                        setErrorMessage(errMsg);
                        return false;
                    }
                }
            }
        }
        // must end with '.*'
        if (!packageString.endsWith(".*")) {
            setErrorMessage("Must end with '.*'.");
            return false;
        }
        // cannot be '.*'
        if (packageString.equals(".*")) {
            setErrorMessage("\".*\" is disallowed, basically exclude all?");
            return false;
        }
        // all valid no error message
        setErrorMessage("");
        return true;
    }

    /**
     * Check the package-string at some row is not a duplicate of another row.
     * Set the error message label content if there is an error.
     *
     * @param myRow         integer of the row being processed (my-row)
     *
     * @param packageString string from cell and to be a package string
     *
     * @return true if valid
     */
    private boolean uniquePackageString(String packageString) {

        EpTableModel modelL = (EpTableModel) this.excludePackagesJTable.getModel();

        // prepare to scan all the row for duplicate of the new/updated row
        int rowCount = this.excludePackagesJTable.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            String valueAtStr = (String) modelL.getValueAt(i, EpTableModel.COL_EXCL_PACKAGE_STRING);

            if (packageString.equals(valueAtStr)) {
                setErrorMessage("Duplicate package name in table.");
                return false;
            }
        }
        setErrorMessage(" ");
        return true;
    }

    /**
     * Set the text of the error message label context, ensure is red indicating
     * error, black if no error message.
     *
     * @param errorMsg the string of message
     */
    private void setErrorMessage(String errorMsg) {
        if (errorMsg.isEmpty()) {
            // keeps the JLabel occupying space on its parent JPanel, otherwise it
            // shrinks
            errorMsg = " ";
        }
        // set errors for the entry using colors on the edit text-field
        if (errorMsg.equals(" ")) {
            this.exclErrorMessageLabel.setForeground(Color.black);
        } else {
            this.exclErrorMessageLabel.setForeground(Color.red);
        }
        this.exclErrorMessageLabel.setText(errorMsg);
    }

    @Override
    @SuppressWarnings("Convert2Lambda")
    public void actionPerformed(ActionEvent evt) {
        /**
         * Add to the exclude package table another row for an item.
         */
        EpTableModel tableModel = (EpTableModel) this.excludePackagesJTable.getModel();
        tableModel.addRowViaAddButton(this.addExclTextfield.getText());

        JButton addButt = (JButton) evt.getSource();
        addButt.setEnabled(false);

        this.epJPanel.saveToJson();
    }
}
