/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.ep;

import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import dbrad.jacocoverage.plugin.config.JTableConfigDatumObj;

/**
 *
 * @author dbradley
 */
public class EpJTableDatum extends JTableConfigDatumObj {

    NbProjectJacoco nbProjectJacoco;
    EpJPanel epJPanel;

    public EpJTableDatum(NbProjectJacoco nbProjectJacoco, EpJPanel epJPanel) {
        this.nbProjectJacoco = nbProjectJacoco;
        this.epJPanel = epJPanel;
    }
}
