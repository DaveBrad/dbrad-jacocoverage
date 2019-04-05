/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.util.NBProjectTypeEnum;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EAR;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EJB;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_WEB;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.NBMODULE;
import dbrad.jacocofpm.util.NBUtils;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

/**
 * The "Reset (w/jacocoverage)" contextual action registration for Ant projects.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@ActionID(category = "Project",
        id = "dbrad.jacocoverage.menuctxt.action.ProjectRemoveCoverageData")
@ActionRegistration(displayName = "#CTL_ProjectRemoveCoverageData",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true)
@ActionReference(path = "Projects/Actions",
        position = 1986,
        separatorAfter = 1988)
@NbBundle.Messages("CTL_ProjectRemoveCoverageData=Reset (w/jacocoverage)")
@SuppressWarnings("CloneableImplementsClone")
public class ProjectRemoveCoverageData
        extends ActionRemoveCoverageData
        implements ContextAwareAction, Presenter.Popup {

    private static final long serialVersionUID = 1L;

    public ProjectRemoveCoverageData() {
        super();
        super.setEnabled(NBUtils.isProjectSupported(IdeProjectJacocoverageConfig
                .getSelectedNbProject(),
                NBProjectTypeEnum.J2SE, J2EE, J2EE_EAR, J2EE_EJB, J2EE_WEB, NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE));
        super.putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        super.putValue(Action.NAME, Bundle.CTL_ProjectRemoveCoverageData());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new ProjectRemoveCoverageData();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        return menuitem;
    }
}
