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
 * The "Test with JaCoCoverage" contextual action registration for Java
 * projects. Start the "test" Ant task with the JaCoCo JavaAgent correctly
 * configured, colorize Java source files and show a coverage report.
 * <p>
 * NOTE WARNING: this class is invoked by the IDE in a manner that DOES NOT
 * SUPPORT class variables.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@ActionID(category = "Projects",
        id = "dbrad.jacocoverage.menuctxt.action.ProjectAntTestJ2SE")
@ActionRegistration(displayName = "#CTL_ProjectAntTestJ2SE",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true)
@ActionReference(path = "Projects/Actions",
        position = 1985)
@NbBundle.Messages("CTL_ProjectAntTestJ2SE=Test w/jacocoverage")
@SuppressWarnings("CloneableImplementsClone")
public class ProjectAntTestJ2SE
        extends ActionJacocoOnAntTaskJ2SE
        implements ContextAwareAction, Presenter.Popup {

    private static final long serialVersionUID = 1L;

    // NOTE WARNING: this class is invoked by the IDE in a manner that DOES NOT
    // SUPPORT class variables.
    //
    // Although below was a good idea and typical, the IDE acts like a singleton 
    // for this class and as such changing projects resulted in attempting to 
    // use OLD settings as it called the methods as if a singleton.
    //
    // THUS: any of these data items are acquired in each method.
    // 
    //    private final Project nbProject;
    //    private final boolean isJ2seType;
    //    private final boolean isNbModuleType;
    //
    /**
     * Create the project context menu Test w/jacocoverage item.
     */
    public ProjectAntTestJ2SE() {
        super("test");
        super.setEnabled(NBUtils.isProjectSupported(IdeProjectJacocoverageConfig.getSelectedNbProject(),
                NBProjectTypeEnum.J2SE, J2EE, J2EE_EAR, J2EE_EJB, J2EE_WEB, NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE));
        super.putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        super.putValue(Action.NAME, Bundle.CTL_ProjectAntTestJ2SE());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new ProjectAntTestJ2SE();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        return menuitem;
    }
}
