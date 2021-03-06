/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.ide.prefconfig;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Controller of {@link JaCoCoveragePanel}. Generated by NetBeans.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@OptionsPanelController.SubRegistration(
        location = "Java",
        displayName = "JaCoCoverage-dbrad",    // remove inspect error "#AdvancedOption_DisplayName_JaCoCoverage",
        keywords = "jacoco",                   // remove inspect error #AdvancedOption_Keywords_JaCoCoverage",
        keywordsCategory = "Java/JaCoCoverage")
public class JaCoCoverageOptionsPanelController extends OptionsPanelController {

    private JaCoCoveragePanel panel;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean changed;

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return getPanel().valid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        return changed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private JaCoCoveragePanel getPanel() {
        if (panel == null) {
            panel = new JaCoCoveragePanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
