/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.util;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * A combo-box used to display a list of icons of left-side and text on right-side.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class IcoTxtComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    /**
     * Create the JLabel for the icons.
     */
    public IcoTxtComboBoxRenderer() {
        super.setOpaque(true);
        super.setHorizontalAlignment(SwingConstants.LEFT);
        super.setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        ImageIcon icon = (ImageIcon) value;
        setText(icon.getDescription());
        setIcon(icon);
        return this;
    }
}