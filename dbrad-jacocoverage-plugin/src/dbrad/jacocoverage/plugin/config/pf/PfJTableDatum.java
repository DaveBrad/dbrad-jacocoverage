/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import dbrad.jacocoverage.plugin.config.JTableConfigDatumObj;

/**
 *
 * @author dbradley
 */
public class PfJTableDatum extends JTableConfigDatumObj{
    
    public NbProjectJacoco nbProjectJacoco;
    
    public PfJTableDatum(NbProjectJacoco nbProjectJacoco){
        this.nbProjectJacoco = nbProjectJacoco;
    }
}
