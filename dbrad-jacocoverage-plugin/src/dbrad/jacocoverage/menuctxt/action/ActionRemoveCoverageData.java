/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocoverage.plugin.anno.AbstractCoverageAnnotation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

/**
 * A toolkit that removed colorization from Java source files.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqRequestProcessor">DevFaqRequestProcessor</a>
 * for NetBeans threading tweaks.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqActionContextSensitive">DevFaqActionContextSensitive</a>
 * for context action tweaks.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqAddGlobalContext">DevFaqAddGlobalContext</a>
 * for global context and project tweaks.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@SuppressWarnings("CloneableImplementsClone")
public abstract class ActionRemoveCoverageData
        extends AbstractAction
        implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ActionRemoveCoverageData.class.getName());

    /**
     * Enable the context action on supported projects only.
     */
    public ActionRemoveCoverageData() {
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        new RequestProcessor("JaCoCoverage Cleanup Task", 3, true).post(new Runnable() {
            @Override
            public void run() {
                try {
                    removePrjCoverageData(IdeProjectJacocoverageConfig.createForSelectedProject());

                } catch (IllegalArgumentException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    /**
     * Remove colorization from Java source files.
     *
     * @param ideProjectConfig the IDE selected project to that contains Java
     *                         sources.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void removePrjCoverageData(final IdeProjectJacocoverageConfig ideProjectConfig)
            throws IOException {
        new RequestProcessor("JaCoCoverage Cleanup Task", 3, true).post(new Runnable() {
            @Override
            public void run() {
                ProgressHandle progr = ProgressHandleFactory.createSystemHandle("JaCoCoverage Cleanup Task");

                if (ideProjectConfig == null) {
                    LOGGER.warning("Cannot find project to clear coverage data");
                } else {
                    try {
                        progr.setInitialDelay(400);
                        progr.start();
                        progr.switchToIndeterminate();
                        Project prj = IdeProjectJacocoverageConfig.getSelectedNbProject();

                        AbstractCoverageAnnotation.removeAll(ideProjectConfig.getNbProjectId());
                    } finally {
                        progr.finish();
                    }
                }
            }
        });
    }
}
