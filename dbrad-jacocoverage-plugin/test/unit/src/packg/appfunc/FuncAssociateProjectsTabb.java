/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import static junit.framework.TestCase.fail;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTableOperator;

/**
 *
 * @author dbradley
 */
public class FuncAssociateProjectsTabb extends FuncProjectProperties {

    private ArrayList<String> orderOfTabbArr = new ArrayList<>();
    private HashMap<String, FuncPkgFilterTable> tabbTableHash;

    private AssociateJTabbedPaneOperator associatedProjectsInnerTabbPaneOper;
    private boolean enabledAssociatedProjectTab;
    private JPanel jPanelAssociatedProject;

    FuncAssociateProjectsTabb(JDialogOperator projPropertiesDialog) {
        super();
        projSpecificTabbPane = new JTabbedPaneOperator(projPropertiesDialog);

        // dynamically find the associated-projects page
        int pageIndex = projSpecificTabbPane.findPage(ASSOCIATED_PROJECTS);
        jPanelAssociatedProject = (JPanel) projSpecificTabbPane.getComponentAt(pageIndex);

        enabledAssociatedProjectTab = projSpecificTabbPane.isEnabledAt(pageIndex);
    }

    /**
     * @return  */
    public JTabbedPaneOperator innerTabbPaneOperator() {
        hasClosedCondition();

        return this.associatedProjectsInnerTabbPaneOper;
    }

    /**
     * Has an associate-project tab that is enabled for the current incarnation
     * of the project-properties dialog (there are limitations for this call).
     * <p>
     * A new AssociateProjectTabbFunc is required every time a
     * project-properties dialog is launched. That is, the
     * AssociateProjectTabbFunc is old when original dialog is closed.
     *
     *
     * @param expectEnabled
     *
     * @return
     */
    public boolean hasAssociateProjects(boolean expectEnabled) {
        hasClosedCondition();

        String expect = expectEnabled ? "enabled" : "disabled";
        String actual = enabledAssociatedProjectTab ? "enabled" : "disabled";

        // determine expected vs actual state
        if (expectEnabled != enabledAssociatedProjectTab) {
            fail(String.format("Expect '%s'. Actual '%s' associated-project tab.", expect, actual));
        }
        // has already been determined in a state that maybe processed 
        // but has the data been loaded
        if (enabledAssociatedProjectTab) {
            // if the associatedProjectTabbPane is null the tabb has not been processed
            if (associatedProjectsInnerTabbPaneOper == null) {
                // there are a number of sub-components each representing a
                // PfJTable which have not been loaded

                // most explicit linkage, subject to failure
                associatedProjectsInnerTabbPaneOper
                        = new AssociateJTabbedPaneOperator((JTabbedPane) jPanelAssociatedProject.getComponent(0));

                int currentSelected = projSpecificTabbPane.getSelectedIndex();

                projSpecificTabbPane.selectPage(ASSOCIATED_PROJECTS);

                pauseMs(500);

                // have the associated pane, now need to get all the sub-panels and
                // thus the sub-PfJTable'  
                int tabbedCount = associatedProjectsInnerTabbPaneOper.getTabCount();

                // each component will have a tabbName which we will invoke to get
                // the table in place
                for (int i = 0; i < tabbedCount; i++) {
                    String tabbName = associatedProjectsInnerTabbPaneOper.getTitleAt(i);
                    orderOfTabbArr.add(tabbName);
                    pauseMs(500);

                    getFuncPfTable(tabbName);
                }
                projSpecificTabbPane.selectPage(currentSelected);
            }
        }
        return enabledAssociatedProjectTab;
    }

    private void checkEnabled() {
        hasAssociateProjects(true);
    }

    /**
     * @param tabbName
     * @return  */
    public FuncPkgFilterTable getFuncPfTable(String tabbName) {
        hasClosedCondition();
        checkEnabled();

        if (tabbTableHash == null) {
            tabbTableHash = new HashMap<>();
        }
        // not readily available then create it
        if (!tabbTableHash.containsKey(tabbName)) {

            // the associated-projects tabbed pane contains a 'list' of
            // package-filter-tables within JPanels
            int indexOfPage = associatedProjectsInnerTabbPaneOper.findPage(tabbName);
            if (indexOfPage == -1) {
                fail(String.format("No associate-project by name '%s' found", tabbName));
            }
            // find and store the PfJtble away
            JPanel assocProjPanel = (JPanel) associatedProjectsInnerTabbPaneOper.getComponentAt(indexOfPage);
            associatedProjectsInnerTabbPaneOper.selectPage(indexOfPage);

            ContainerOperator tableContainer = new ContainerOperator(assocProjPanel);

            // within the the associated project page selected is a JTable
            JTableOperator innerTable = new JTableOperator(tableContainer);

            tabbTableHash.put(tabbName, new FuncPkgFilterTable(innerTable, false));
        }
        return tabbTableHash.get(tabbName);
    }

    /**
     * @return  */
    public ArrayList<FuncPkgFilterTable> getAssociatePfJTablesOperators() {
        hasClosedCondition();

        ArrayList<FuncPkgFilterTable> fcPkgFilterTableArr = new ArrayList<>();

        if (enabledAssociatedProjectTab) {

            // otherwise there are associated-projects, but need to create 
            // them if available
            if (orderOfTabbArr.isEmpty()) {
                // need to fill the table for all the sub-project associated projects
                hasAssociateProjects(true);
            }
            // return in order
            for (String key : orderOfTabbArr) {
                fcPkgFilterTableArr.add(tabbTableHash.get(key));
            }
        }
        return fcPkgFilterTableArr;
    }
}

/**
 * JTabbedPaneOperator extension to process the selectPage for scrolled layouts.
 * Jemmy does not appear to support the scrolled layout for JTabbedPane and as
 * such the selectPage does not select the page if the tab is NOT in the
 * viewport.
 *
 * @author dbradley
 */
class AssociateJTabbedPaneOperator extends JTabbedPaneOperator {

    public AssociateJTabbedPaneOperator(JTabbedPane b) {
        super(b);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, ComponentChooser chooser, int index) {
        super(cont, chooser, index);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, String text, int tabIndex, int index) {
        super(cont, text, tabIndex, index);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, String text, int index) {
        super(cont, text, index);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, String text) {
        super(cont, text);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont, int index) {
        super(cont, index);
    }

    public AssociateJTabbedPaneOperator(ContainerOperator cont) {
        super(cont);
    }

    @Override
    @SuppressWarnings("null")
    public Component selectPage(int indexOfPage) {
        // @SuppressWarnings("null") happens if the JTabbedPane is 
        // not what is expected the null points this out
        //
        //
        // the super.selectPage does not work well for scrolled layout
        // and as such needs to address getting tabs into veiw to work.
        //
        // the selectPage only appears to fail for SCROLL_TAB_LAYOIT
        int tabbPanePolicy = ((JTabbedPane) this.getSource()).getTabLayoutPolicy();

        if (tabbPanePolicy == JTabbedPane.SCROLL_TAB_LAYOUT) {
            // basically place the tab into view based upon its
            // tabRect coordinates within the viewport of the
            // JTabbedPane
            Rectangle tabRect = this.getBoundsAt(indexOfPage);

            JTabbedPane tabbedPaneSrc = (JTabbedPane) this.getSource();

            // dynamically find the viewport component as using a magic
            // number may nt apply to all Java versions to the second component
            // JViewport vp2 = (JViewport) tabbedPaneSrc.getComponent(2);
            Component[] compArr = tabbedPaneSrc.getComponents();

            JViewport vuPort = null;
            for (Component compItem : compArr) {
                if (compItem instanceof JViewport) {
                    vuPort = (JViewport) compItem;
                    break;
                }
            }
            // if there is a null pointer exception then the structure has been changed
            vuPort.scrollRectToVisible(tabRect);
            // yield would allow time for the tab to be scrolled into view, but
            // does not appear to be necessary
            //  Thread.yield();
        }
        // do the super selectPage now (as tab should have been scrolled into
        // view)
        return super.selectPage(indexOfPage);
    }
}
