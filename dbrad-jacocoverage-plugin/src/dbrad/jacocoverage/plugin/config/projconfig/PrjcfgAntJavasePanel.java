/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.config.projconfig;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import dbrad.jacocoverage.plugin.config.IStorable;
import dbrad.jacocoverage.plugin.config.assocp.BuildAssociatedProjects;
import dbrad.jacocoverage.plugin.config.ep.EpJPanel;
import dbrad.jacocoverage.plugin.config.gen.jPanelGeneralSaveFiles;
import dbrad.jacocoverage.plugin.config.pf.PfJPanel;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import dbrad.jacocofpm.util.NBUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.Mnemonics;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Configuration panel at project level.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class PrjcfgAntJavasePanel extends javax.swing.JPanel implements IStorable {

    private final ProjectCustomizer.Category cat;

    private static final long serialVersionUID = 1L;

    public NbProjectJacoco nbProjectJacoco;

    private boolean loadingSinkStoring;

    /**
     * Creates new form PrjcfgAntJavasePanel.
     *
     * @param context
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public PrjcfgAntJavasePanel(Lookup context, ProjectCustomizer.Category cat)
            throws FileNotFoundException,
            IOException {
        super();

        Project nbProject = context.lookup(Project.class);

        // when creating a new dbrad-jacocoverage project.properties dialog
        // a new NbProjectJacoco instance is created
        this.nbProjectJacoco = new NbProjectJacoco(nbProject, null);

        initComponents();
        // <editor-fold defaultstate="collapsed" desc="Tooltips">
        // Warning: background of tooltips is black on Ubuntu. Avoid coloring links with blue.
//      jButtonSocialTwitter.setToolTipText("<html><body>Jonathan Lermitage on <b>Twitter</b> (author of JaCoCoverage)<br>"
//        + "https://twitter.com/JLermitage</body></html>");
//      jButtonSocialFacebook.setToolTipText("<html><body>Jonathan Lermitage on <b>Facebook</b> (author of JaCoCoverage)<br>"
//        + "https://www.facebook.com/jonathan.lermitage</body></html>");
//      jButtonSocialGithub.setToolTipText("<html><body>Jonathan Lermitage  on <b>GitHub</b> (author of JaCoCoverage)<br>"
//        + "https://github.com/jonathanlermitage</body></html>");
//      jButtonSocialJojohome.setToolTipText("<html><body>Jonathan Lermitage devblog (author of JaCoCoverage)<br>"
//        + "http://lermitage.biz</body></html>");
//      jButtonOnlineHelp.setToolTipText("<html><body>Online help page of JaCoCoverage<br>"
//        + "https://github.com/jonathanlermitage/tikione-jacocoverage/blob/master/README.md</body></html>");
        // </editor-fold>

        this.cat = cat;

        load();
    }

    public void loadPreferencesFromDefault(IdeProjectJacocoverageConfig ideProjectConfig) {
        boolean projectSpecific = ideProjectConfig.isProjectSpecific();

        jRadioButtonUseGlobalOptions.setSelected(!projectSpecific);

        // is the project overriden for its settings
        enableProjectsideCfgUI(projectSpecific);

        jRadioButtonUseProjectSpecificOptions.setSelected(projectSpecific);

        // merge setting
        jCheckboxMergeOn.setSelected(ideProjectConfig.isMergeOnSet());

        // high lighting
        jCheckBoxEnableHighlighting.setSelected(ideProjectConfig.isHighlightingSet());
        jCheckBoxEnableHighlightingExtended.setSelected(ideProjectConfig.isHighlightingExtendedSet());
        jCheckBoxEnableHighlightingExtended.setEnabled(jCheckBoxEnableHighlighting.isSelected());

        // the by-xxxxxxx-report setting
        boolean res = ideProjectConfig.isByProjectReportsSet();
        jRadioByProject.setSelected(res);
        jRadioByGrouped.setSelected(!res);

        jCheckBoxRetainXmlFile.setSelected(ideProjectConfig.isRetainXmlFileSet());

        jCheckBoxEnableConsoleReport.setSelected(ideProjectConfig.isConsoleReportSet());

        jCheckBoxEnableHtmlReport.setSelected(ideProjectConfig.isHtmlReportSet());
        jCheckBoxOpenHtmlReport.setSelected(ideProjectConfig.isAutoOpenHtmlReportSet());
        jCheckBoxOpenHtmlReport.setEnabled(jCheckBoxEnableHtmlReport.isSelected());
    }

    private void load() {
        loadingSinkStoring = true;

        boolean jsonVersusMemoryUpdate = false;
        try {
            IdeProjectJacocoverageConfig ideProjectConfig = IdeProjectJacocoverageConfig.reuseForSelectedProject();

            // load the general parameters
            loadPreferencesFromDefault(ideProjectConfig);

            // the explicit excludes for test frameworks
            jPanelExcludePackages.jCheckBoxExcludedOrgTestng.setSelected(ideProjectConfig.isExcludedOrgTestngSet());
            jPanelExcludePackages.jCheckBoxExcludedOrgJunit.setSelected(ideProjectConfig.isExcludedOrgJunitSet());
            jPanelExcludePackages.jCheckBoxExcludedJunitExtension.setSelected(ideProjectConfig.isExcludedJunitExtensionSet());

            // load the reports default directory and timestamp settings
            boolean repDefaultDirBool = ideProjectConfig.isReportDefaultDir();
            jPanelGeneralSaveFilesOnGenTab.jRadioButtDefaultReportDirectory.setSelected(repDefaultDirBool);
            jPanelGeneralSaveFilesOnGenTab.jRadioButtUserDefinedDirectory.setSelected(!repDefaultDirBool);

            jPanelGeneralSaveFilesOnGenTab.jTextfieldUserDefinedDirectory.setText(ideProjectConfig.getReportUserDefinedDirPath());
            jPanelGeneralSaveFilesOnGenTab.jTextfieldUserDefinedDirectory.setEnabled(!repDefaultDirBool);

            // the spinner value is enabled when the reports-timestamp is set, otherwise its disabled
            boolean repTSBool = ideProjectConfig.isReportsTimestampForm();
            jPanelGeneralSaveFilesOnGenTab.timeStampedReports.setSelected(repTSBool);

            jPanelGeneralSaveFilesOnGenTab.jSpinnerRetainReports.setValue(ideProjectConfig.getReportRetainValueN());
            jPanelGeneralSaveFilesOnGenTab.jSpinnerRetainReports.setEnabled(repTSBool);

            // load the various tabs and process the current setup
            jPanelExcludePackages.loadExcludePackagesFromJson(this.nbProjectJacoco);

            jsonVersusMemoryUpdate = jPanelPackageFilter.loadPkgFilterFromJson(this.nbProjectJacoco);
            BuildAssociatedProjects.buildAndLoadAssociatedProjects(associatedTabPane, this.nbProjectJacoco);

            // at this point all the table data is in place, need to do a 
            // merge data action to get all relationships displayed correctly
            ArrayList<String> excludeList
                    = NbUtilsPlugin.getExcludesPatternsAsArrayList(this.nbProjectJacoco.getPrincipleNbProject());

            this.nbProjectJacoco.mergeAllSourcesForDisplay(excludeList);

            // 
            Project nbProjectL = ideProjectConfig.nbProjectJacoco.getPrincipleNbProject();
            String includePackagesString = NbUtilsPlugin.getProjectIncludesAsStr(nbProjectL, ":", ".");

            jtextareaIncludeStringDisplay.setText(includePackagesString);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        loadingSinkStoring = false;
        if (jsonVersusMemoryUpdate) {
            // some additional data was added for the package-filters from 
            // memory, over and above the JSON file configuration
            store();
        }
    }

    @Override
    public void store() {
        if (loadingSinkStoring) {
            return;
        }
        try {
            // the principle-project to store data against
            Project nbProject = nbProjectJacoco.getPrincipleNbProject();
            IdeProjectJacocoverageConfig jsonProjFile = IdeProjectJacocoverageConfig.reuseForProject(nbProject);

            // jPanelAssociatedProjects.storeToJsonMap();
            // is not an action that can be done as the associate-projects
            // are a sub-part of jPanelPackageFilter.storeToJsonMap();
            jPanelPackageFilter.storeToJsonMap();

            jPanelExcludePackages.storeExcludePackages();
            // - - - - - 
            jsonProjFile.setProjectSpecific(jRadioButtonUseProjectSpecificOptions.isSelected());
            // - - - - -
            jsonProjFile.setMergeOn(jCheckboxMergeOn.isSelected());
            // - - - - - 
            jsonProjFile.setHighlighting(jCheckBoxEnableHighlighting.isSelected());
            jsonProjFile.setHighlightingExtended(jCheckBoxEnableHighlightingExtended.isSelected());

            jsonProjFile.setByProjectReports(jRadioByProject.isSelected());

            jsonProjFile.setRetainXmlFile(jCheckBoxRetainXmlFile.isSelected());

            jsonProjFile.setConsoleReport(jCheckBoxEnableConsoleReport.isSelected());

            jsonProjFile.setHtmlReport(jCheckBoxEnableHtmlReport.isSelected());
            jsonProjFile.setOpenHtmlReport(jCheckBoxOpenHtmlReport.isSelected());

            // - - - - - exclude test framework settings that apply to project-specific, however always for global
            jsonProjFile.setExcludeOrgTestng(jPanelExcludePackages.jCheckBoxExcludedOrgTestng.isSelected());
            jsonProjFile.setExcludeOrgJunit(jPanelExcludePackages.jCheckBoxExcludedOrgJunit.isSelected());
            jsonProjFile.setExcludeJunitExtension(jPanelExcludePackages.jCheckBoxExcludedJunitExtension.isSelected());

            // - - - - - 
            // data for the report directory and timestamp-retention settings
            jsonProjFile.setReportDefaultDirState(jPanelGeneralSaveFilesOnGenTab.jRadioButtDefaultReportDirectory.isSelected());
            jsonProjFile.setReportUserDefinedDirState(jPanelGeneralSaveFilesOnGenTab.jTextfieldUserDefinedDirectory.getText());

            jsonProjFile.setReportTimestampState(jPanelGeneralSaveFilesOnGenTab.timeStampedReports.isSelected());
            jsonProjFile.setReportRetainSpinnerInt((int) jPanelGeneralSaveFilesOnGenTab.jSpinnerRetainReports.getValue());

            // - - - - - - - - - 
            jsonProjFile.jsonMapData.store();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void enableProjectsideCfgUI(boolean enable) {
        jPanelProjectSpecificUI.setVisible(enable);
        if (enable) {
            jCheckboxMergeOn.setEnabled(enable);
            jCheckboxMergeOn.setSelected(false);

            jCheckBoxEnableConsoleReport.setEnabled(enable);
            jCheckBoxEnableHighlighting.setEnabled(enable);
            jCheckBoxEnableHtmlReport.setEnabled(enable);

            jCheckBoxEnableHighlightingExtended.setEnabled(jCheckBoxEnableHighlighting.isSelected());
            jCheckBoxOpenHtmlReport.setEnabled(jCheckBoxEnableHtmlReport.isSelected());

            JRadioButton defaultRadio = jPanelGeneralSaveFilesOnGenTab.jRadioButtDefaultReportDirectory;
            defaultRadio.setSelected(true);
            jPanelGeneralSaveFilesOnGenTab.enablejRadioButtDefaultReportDirectory(defaultRadio);

            JCheckBox timestampChkBox = jPanelGeneralSaveFilesOnGenTab.timeStampedReports;
            timestampChkBox.setSelected(timestampChkBox.isSelected());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupUseGlobalOptions = new ButtonGroup();
        buttonGroupByKindReport = new ButtonGroup();
        jCheckBox1 = new JCheckBox();
        jRadioButtonUseGlobalOptions = new JRadioButton();
        jRadioButtonUseProjectSpecificOptions = new JRadioButton();
        jPanelProjectSpecificUI = new JPanel();
        jPanel4IncludesTextarea = new JPanel();
        jScrollPane3 = new JScrollPane();
        jtextareaIncludeStringDisplay = new TextareaIncludeDisplay();
        tabbbedPaneSpecificProjCfg = new JTabbedPane();
        jPanelGeneralOptions = new JPanel();
        jCheckBoxEnableHighlighting = new JCheckBox();
        jCheckBoxEnableHighlightingExtended = new JCheckBox();
        jRadioByProject = new JRadioButton();
        jRadioByGrouped = new JRadioButton();
        jCheckBoxEnableConsoleReport = new JCheckBox();
        jCheckBoxEnableHtmlReport = new JCheckBox();
        jCheckBoxOpenHtmlReport = new JCheckBox();
        jButtonOnlineHelp = new JButton();
        jPanelGeneralSaveFilesOnGenTab = new jPanelGeneralSaveFiles();
        jCheckBoxRetainXmlFile = new JCheckBox();
        jCheckboxMergeOn = new JCheckBox();
        jLabel1 = new JLabel();
        jPanelPackageFilter = new PfJPanel();
        jPanelAssociatedProjects = new JPanel();
        associatedTabPane = new JTabbedPane();
        jPanelExcludePackages = new EpJPanel();

        Mnemonics.setLocalizedText(jCheckBox1, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBox1.text")); // NOI18N

        setPreferredSize(new Dimension(760, 560));

        buttonGroupUseGlobalOptions.add(jRadioButtonUseGlobalOptions);
        Mnemonics.setLocalizedText(jRadioButtonUseGlobalOptions, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jRadioButtonUseGlobalOptions.text")); // NOI18N
        jRadioButtonUseGlobalOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButtonUseGlobalOptionsActionPerformed(evt);
            }
        });

        buttonGroupUseGlobalOptions.add(jRadioButtonUseProjectSpecificOptions);
        Mnemonics.setLocalizedText(jRadioButtonUseProjectSpecificOptions, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jRadioButtonUseProjectSpecificOptions.text")); // NOI18N
        jRadioButtonUseProjectSpecificOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButtonUseProjectSpecificOptionsActionPerformed(evt);
            }
        });

        jPanel4IncludesTextarea.setBorder(BorderFactory.createTitledBorder(null, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jPanel4IncludesTextarea.border.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", 2, 10))); // NOI18N

        jtextareaIncludeStringDisplay.setEditable(false);
        jtextareaIncludeStringDisplay.setBorder(null);
        jtextareaIncludeStringDisplay.setColumns(20);
        jtextareaIncludeStringDisplay.setRows(2);
        jScrollPane3.setViewportView(jtextareaIncludeStringDisplay);

        GroupLayout jPanel4IncludesTextareaLayout = new GroupLayout(jPanel4IncludesTextarea);
        jPanel4IncludesTextarea.setLayout(jPanel4IncludesTextareaLayout);
        jPanel4IncludesTextareaLayout.setHorizontalGroup(jPanel4IncludesTextareaLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4IncludesTextareaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4IncludesTextareaLayout.setVerticalGroup(jPanel4IncludesTextareaLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4IncludesTextareaLayout.createSequentialGroup()
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        tabbbedPaneSpecificProjCfg.setBorder(BorderFactory.createTitledBorder(null, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.tabbbedPaneSpecificProjCfg.border.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", 2, 10))); // NOI18N

        Mnemonics.setLocalizedText(jCheckBoxEnableHighlighting, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxEnableHighlighting.text")); // NOI18N
        jCheckBoxEnableHighlighting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxEnableHighlightingActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckBoxEnableHighlightingExtended, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxEnableHighlightingExtended.text")); // NOI18N

        buttonGroupByKindReport.add(jRadioByProject);
        Mnemonics.setLocalizedText(jRadioByProject, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jRadioByProject.text")); // NOI18N
        jRadioByProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioByProjectActionPerformed(evt);
            }
        });

        buttonGroupByKindReport.add(jRadioByGrouped);
        Mnemonics.setLocalizedText(jRadioByGrouped, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jRadioByGrouped.text")); // NOI18N
        jRadioByGrouped.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioByGroupedActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckBoxEnableConsoleReport, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxEnableConsoleReport.text")); // NOI18N
        jCheckBoxEnableConsoleReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxEnableConsoleReportActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckBoxEnableHtmlReport, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxEnableHtmlReport.text")); // NOI18N
        jCheckBoxEnableHtmlReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxEnableHtmlReportActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckBoxOpenHtmlReport, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxOpenHtmlReport.text")); // NOI18N
        jCheckBoxOpenHtmlReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxOpenHtmlReportActionPerformed(evt);
            }
        });

        jButtonOnlineHelp.setIcon(new ImageIcon(getClass().getResource("/dbrad/jacocoverage/plugin/resources/icon/famfamfam_help.png"))); // NOI18N
        Mnemonics.setLocalizedText(jButtonOnlineHelp, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jButtonOnlineHelp.text")); // NOI18N
        jButtonOnlineHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonOnlineHelpActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckBoxRetainXmlFile, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckBoxRetainXmlFile.text")); // NOI18N
        jCheckBoxRetainXmlFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBoxRetainXmlFileActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jCheckboxMergeOn, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jCheckboxMergeOn.text")); // NOI18N
        jCheckboxMergeOn.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jCheckboxMergeOnStateChanged(evt);
            }
        });
        jCheckboxMergeOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckboxMergeOnActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(jLabel1, NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jLabel1.text")); // NOI18N

        GroupLayout jPanelGeneralOptionsLayout = new GroupLayout(jPanelGeneralOptions);
        jPanelGeneralOptions.setLayout(jPanelGeneralOptionsLayout);
        jPanelGeneralOptionsLayout.setHorizontalGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonOnlineHelp)
                    .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                        .addComponent(jCheckBoxEnableHighlighting)
                        .addGap(195, 195, 195)
                        .addComponent(jCheckboxMergeOn))
                    .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                        .addComponent(jRadioByProject)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioByGrouped, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jCheckBoxOpenHtmlReport))
                            .addComponent(jCheckBoxEnableHtmlReport)
                            .addComponent(jCheckBoxEnableConsoleReport)
                            .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(GroupLayout.Alignment.LEADING, jPanelGeneralOptionsLayout.createSequentialGroup()
                                    .addComponent(jCheckBoxEnableHighlightingExtended)
                                    .addGap(126, 126, 126)
                                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addComponent(jPanelGeneralSaveFilesOnGenTab, GroupLayout.PREFERRED_SIZE, 708, GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBoxRetainXmlFile, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanelGeneralOptionsLayout.setVerticalGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanelGeneralOptionsLayout.createSequentialGroup()
                .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGeneralOptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxEnableHighlighting)
                            .addComponent(jCheckboxMergeOn))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxEnableHighlightingExtended)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelGeneralOptionsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioByProject)
                            .addComponent(jRadioByGrouped))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanelGeneralOptionsLayout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jCheckBoxRetainXmlFile)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxEnableConsoleReport, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxEnableHtmlReport)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxOpenHtmlReport)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelGeneralSaveFilesOnGenTab, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOnlineHelp)
                .addContainerGap())
        );

        jPanelGeneralSaveFilesOnGenTab.setJavaSePanel(this);
        jLabel1.getAccessibleContext().setAccessibleName(NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jLabel1.AccessibleContext.accessibleName")); // NOI18N

        tabbbedPaneSpecificProjCfg.addTab(NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jPanelGeneralOptions.TabConstraints.tabTitle"), jPanelGeneralOptions); // NOI18N
        tabbbedPaneSpecificProjCfg.addTab(NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jPanelPackageFilter.TabConstraints.tabTitle"), jPanelPackageFilter); // NOI18N
        jPanelPackageFilter.setProjectConfig(this.nbProjectJacoco);

        GroupLayout jPanelAssociatedProjectsLayout = new GroupLayout(jPanelAssociatedProjects);
        jPanelAssociatedProjects.setLayout(jPanelAssociatedProjectsLayout);
        jPanelAssociatedProjectsLayout.setHorizontalGroup(jPanelAssociatedProjectsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAssociatedProjectsLayout.createSequentialGroup()
                .addGap(0, 45, Short.MAX_VALUE)
                .addComponent(associatedTabPane, GroupLayout.PREFERRED_SIZE, 728, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
        );
        jPanelAssociatedProjectsLayout.setVerticalGroup(jPanelAssociatedProjectsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAssociatedProjectsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(associatedTabPane, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        associatedTabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbbedPaneSpecificProjCfg.addTab(NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jPanelAssociatedProjects.TabConstraints.tabTitle"), jPanelAssociatedProjects); // NOI18N
        tabbbedPaneSpecificProjCfg.addTab(NbBundle.getMessage(PrjcfgAntJavasePanel.class, "PrjcfgAntJavasePanel.jPanelExcludePackages.TabConstraints.tabTitle"), jPanelExcludePackages); // NOI18N
        jPanelExcludePackages.setProjectConfig(this);

        GroupLayout jPanelProjectSpecificUILayout = new GroupLayout(jPanelProjectSpecificUI);
        jPanelProjectSpecificUI.setLayout(jPanelProjectSpecificUILayout);
        jPanelProjectSpecificUILayout.setHorizontalGroup(jPanelProjectSpecificUILayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProjectSpecificUILayout.createSequentialGroup()
                .addComponent(jPanel4IncludesTextarea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelProjectSpecificUILayout.createSequentialGroup()
                .addComponent(tabbbedPaneSpecificProjCfg)
                .addContainerGap())
        );
        jPanelProjectSpecificUILayout.setVerticalGroup(jPanelProjectSpecificUILayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProjectSpecificUILayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel4IncludesTextarea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbbedPaneSpecificProjCfg, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        TitledBorder tBorder1 = (TitledBorder)jPanel4IncludesTextarea.getBorder();
        tBorder1.setBorder(new LineBorder(Color.black, 1, true));
        TitledBorder tBorder2 = (TitledBorder)tabbbedPaneSpecificProjCfg.getBorder();
        tBorder2.setBorder(new LineBorder(Color.black, 1, true));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonUseGlobalOptions)
                    .addComponent(jRadioButtonUseProjectSpecificOptions)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelProjectSpecificUI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jRadioButtonUseGlobalOptions)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonUseProjectSpecificOptions)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProjectSpecificUI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonUseGlobalOptionsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButtonUseGlobalOptionsActionPerformed
        enableProjectsideCfgUI(false);
        this.store();
    }//GEN-LAST:event_jRadioButtonUseGlobalOptionsActionPerformed

    private void jRadioButtonUseProjectSpecificOptionsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButtonUseProjectSpecificOptionsActionPerformed
        enableProjectsideCfgUI(true);
        // store will do the initial settings for project specific and the
        // following load will cause the package-filter tables to be loaded
        // from memory-for-project to the JSON file
        this.store();
    }//GEN-LAST:event_jRadioButtonUseProjectSpecificOptionsActionPerformed

    private void jCheckBoxRetainXmlFileActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRetainXmlFileActionPerformed
        this.store();
    }//GEN-LAST:event_jCheckBoxRetainXmlFileActionPerformed

    private void jButtonOnlineHelpActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonOnlineHelpActionPerformed
        NBUtils.extBrowser("https://github.com/jonathanlermitage/tikione-jacocoverage/blob/master/README.md");
    }//GEN-LAST:event_jButtonOnlineHelpActionPerformed

    private void jCheckBoxOpenHtmlReportActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxOpenHtmlReportActionPerformed
        this.store();
    }//GEN-LAST:event_jCheckBoxOpenHtmlReportActionPerformed

    private void jCheckBoxEnableHtmlReportActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxEnableHtmlReportActionPerformed
        jCheckBoxOpenHtmlReport.setEnabled(jCheckBoxEnableHtmlReport.isSelected());
        this.store();
    }//GEN-LAST:event_jCheckBoxEnableHtmlReportActionPerformed

    private void jCheckBoxEnableConsoleReportActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxEnableConsoleReportActionPerformed
        this.store();
    }//GEN-LAST:event_jCheckBoxEnableConsoleReportActionPerformed

    private void jRadioByGroupedActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioByGroupedActionPerformed
        this.store();
    }//GEN-LAST:event_jRadioByGroupedActionPerformed

    private void jRadioByProjectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioByProjectActionPerformed
        this.store();
    }//GEN-LAST:event_jRadioByProjectActionPerformed

    private void jCheckBoxEnableHighlightingActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBoxEnableHighlightingActionPerformed
        jCheckBoxEnableHighlightingExtended.setEnabled(jCheckBoxEnableHighlighting.isSelected());
        this.store();
    }//GEN-LAST:event_jCheckBoxEnableHighlightingActionPerformed

    private void jCheckboxMergeOnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckboxMergeOnActionPerformed
        // if the setting for the merge on is changed, need to delete the 
        // merge file (if present)
        Project nbProj = this.nbProjectJacoco.getPrincipleNbProject();
        IdeProjectJacocoverageConfig.clearMergeData(nbProj);
        
        // store the value away
        this.store();
    }//GEN-LAST:event_jCheckboxMergeOnActionPerformed

    private void jCheckboxMergeOnStateChanged(ChangeEvent evt) {//GEN-FIRST:event_jCheckboxMergeOnStateChanged

    }//GEN-LAST:event_jCheckboxMergeOnStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTabbedPane associatedTabPane;
    public ButtonGroup buttonGroupByKindReport;
    private ButtonGroup buttonGroupUseGlobalOptions;
    private JButton jButtonOnlineHelp;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBoxEnableConsoleReport;
    private JCheckBox jCheckBoxEnableHighlighting;
    private JCheckBox jCheckBoxEnableHighlightingExtended;
    private JCheckBox jCheckBoxEnableHtmlReport;
    private JCheckBox jCheckBoxOpenHtmlReport;
    private JCheckBox jCheckBoxRetainXmlFile;
    private JCheckBox jCheckboxMergeOn;
    private JLabel jLabel1;
    private JPanel jPanel4IncludesTextarea;
    private JPanel jPanelAssociatedProjects;
    public EpJPanel jPanelExcludePackages;
    private JPanel jPanelGeneralOptions;
    private jPanelGeneralSaveFiles jPanelGeneralSaveFilesOnGenTab;
    public PfJPanel jPanelPackageFilter;
    private JPanel jPanelProjectSpecificUI;
    private JRadioButton jRadioButtonUseGlobalOptions;
    private JRadioButton jRadioButtonUseProjectSpecificOptions;
    public JRadioButton jRadioByGrouped;
    public JRadioButton jRadioByProject;
    private JScrollPane jScrollPane3;
    public TextareaIncludeDisplay jtextareaIncludeStringDisplay;
    private JTabbedPane tabbbedPaneSpecificProjCfg;
    // End of variables declaration//GEN-END:variables
}
