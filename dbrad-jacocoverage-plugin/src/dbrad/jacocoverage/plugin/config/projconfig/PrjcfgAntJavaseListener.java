/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.config.projconfig;

import dbrad.jacocoverage.plugin.config.IStorable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener to fire changes on project side configuration (or any {@link IStorable} object).
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class PrjcfgAntJavaseListener implements ActionListener {

    private final IStorable storable;

    public PrjcfgAntJavaseListener(IStorable storable) {
        super();
        this.storable = storable;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        storable.store();
    }
}
