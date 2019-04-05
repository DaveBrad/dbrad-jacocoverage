/* Copyright (c) 2017-2018 dbradly. All rights reserved. */
package packg.appfunc;

import javax.swing.JPanel;
import org.netbeans.jellytools.Bundle;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JRadioButtonOperator;
import org.netbeans.jemmy.operators.JSpinnerOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import packg.zoperation.tstenv.PrepareProject;

/**
 *
 * @author dbradley
 */
public class FuncGeneralTab extends FuncProjectProperties {

    private JTabbedPaneOperator tabbPanel;
    private ContainerOperator genContainerOper;

    private JCheckBoxOperator genEnableHiLiteing;
    private JCheckBoxOperator genEnableHiLiteMultiInstruction;

    private JRadioButtonOperator genByProject;
    private JRadioButtonOperator genByGrouped;

    private JCheckBoxOperator genKeepXmlReport;

    private JCheckBoxOperator genShowShortReportConsole;

    private JCheckBoxOperator genGenerateHtmlReport;
    private JCheckBoxOperator genAutoOpenReport;

    // inner dialogs
    private JRadioButtonOperator genRadioDefault;
    private JRadioButtonOperator genRadioUserDefinedDirectory;

    private JTextFieldOperator genRadioUserDefinedDirectoryTextField;

    private JCheckBoxOperator genTimeStampAndRetain;
    private JSpinnerOperator genSpinnerPreviousVersion;

    /** nojdoc */
    public FuncGeneralTab() {
    }

    /** nojdoc
     *
     * @param prepareProject
     * @param tabbPanel */
    public FuncGeneralTab(PrepareProject prepareProject, JTabbedPaneOperator tabbPanel) {
        super(prepareProject);
        this.tabbPanel = tabbPanel;
    }

    /** nojdoc */
    private void selectGeneralTab() {
        hasClosedCondition();
        String tabText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                "PrjcfgAntJavasePanel.jPanelGeneralOptions.TabConstraints.tabTitle");

        tabbPanel.selectPage(tabText);

        int idx = this.tabbPanel.findPage(tabText);
        JPanel jp = (JPanel) this.tabbPanel.getComponent(idx);
        this.genContainerOper = new ContainerOperator(jp);
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getEnableHiLiteing() {
        selectGeneralTab();

        if (this.genEnableHiLiteing == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxEnableHighlighting.text");

            this.genEnableHiLiteing = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genEnableHiLiteing;
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getEnableHiLiteMultiInstruction() {
        selectGeneralTab();

        if (this.genEnableHiLiteMultiInstruction == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxEnableHighlightingExtended.text");

            this.genEnableHiLiteMultiInstruction = new JCheckBoxOperator(this.genContainerOper,
                    sText);
        }
        return this.genEnableHiLiteMultiInstruction;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator getByProject() {

        selectGeneralTab();

        if (this.genByProject == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jRadioByProject.text");

            this.genByProject = new JRadioButtonOperator(this.genContainerOper, sText);
        }
        return this.genByProject;

    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator getByGrouped() {
        selectGeneralTab();

        if (this.genByGrouped == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jRadioByGrouped.text");

            this.genByGrouped = new JRadioButtonOperator(this.genContainerOper, sText);
        }
        return this.genByGrouped;

    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getKeepXmlReport() {
        selectGeneralTab();

        if (this.genKeepXmlReport == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxRetainXmlFile.text");

            this.genKeepXmlReport = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genKeepXmlReport;
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getShowShortReportConsole() {
        selectGeneralTab();

        if (this.genShowShortReportConsole == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxEnableConsoleReport.text");

            this.genShowShortReportConsole = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genShowShortReportConsole;
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getGenerateHtmlReport() {
        selectGeneralTab();

        if (this.genGenerateHtmlReport == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxEnableHtmlReport.text");

            this.genGenerateHtmlReport = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genGenerateHtmlReport;
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getAutoOpenHtmlReport() {
        selectGeneralTab();

        if (this.genAutoOpenReport == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jCheckBoxOpenHtmlReport.text");

            this.genAutoOpenReport = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genAutoOpenReport;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator getDefault() {
        selectGeneralTab();

        if (this.genRadioDefault == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.gen.Bundle",
                    "jPanelGeneralSaveFiles.jRadioButtDefaultReportDirectory.text");

            this.genRadioDefault = new JRadioButtonOperator(this.genContainerOper, sText);
        }
        return this.genRadioDefault;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator getUserDefined() {
        selectGeneralTab();

        if (this.genRadioUserDefinedDirectory == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.gen.Bundle",
                    "jPanelGeneralSaveFiles.jRadioButtUserDefinedDirectory.text");

            this.genRadioUserDefinedDirectory = new JRadioButtonOperator(this.genContainerOper, sText);
        }
        return this.genRadioUserDefinedDirectory;
    }

    /** nojdoc
     *
     * @return */
    public JTextFieldOperator getUserReportDirField() {
        selectGeneralTab();

        if (this.genRadioUserDefinedDirectoryTextField == null) {
            this.genRadioUserDefinedDirectoryTextField = new JTextFieldOperator(this.genContainerOper);
        }
        return this.genRadioUserDefinedDirectoryTextField;
    }

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator getTimeStamp() {
        selectGeneralTab();

        if (this.genTimeStampAndRetain == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.gen.Bundle",
                    "jPanelGeneralSaveFiles.timeStampedReports.text");

            this.genTimeStampAndRetain = new JCheckBoxOperator(this.genContainerOper, sText);
        }
        return this.genTimeStampAndRetain;
    }

    /** nojdoc
     *
     * @return */
    public JSpinnerOperator getPreviousVersionValue() {
        selectGeneralTab();

        if (this.genSpinnerPreviousVersion == null) {
            this.genSpinnerPreviousVersion = new JSpinnerOperator(this.genContainerOper);
        }
        return this.genSpinnerPreviousVersion;
    }

    //---------
    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickEnableHiLiteing() {
        getEnableHiLiteing().push();
        return this.genEnableHiLiteing;
    }

    /** nojdoc */
    public void setOnEnableHiLiteing() {
        setOn(getEnableHiLiteing());
    }

    /** nojdoc */
    public void setOffEnableHiLiteing() {
        setOff(getEnableHiLiteing());
    }
    //---------

    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickEnableHiLiteMultiInstruction() {
        getEnableHiLiteMultiInstruction().push();
        return this.genEnableHiLiteMultiInstruction;
    }

    /** nojdoc */
    public void setOnEnableHiLiteMultiInstruction() {
        setOn(getEnableHiLiteMultiInstruction());
    }

    /** nojdoc */
    public void setOffEnableHiLiteMultiInstruction() {
        setOff(getEnableHiLiteMultiInstruction());
    }

    //--------------
    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator clickByProject() {
        getByProject().push();
        return this.genByProject;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator clickByGrouped() {
        getByGrouped().push();
        return this.genByGrouped;
    }

    // --------------
    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickKeepXmlReport() {
        getKeepXmlReport().push();
        return this.genKeepXmlReport;
    }

    /** nojdoc */
    public void setOnKeepXmlReport() {
        setOn(getKeepXmlReport());
    }

    /** nojdoc */
    public void setOffKeepXmlReport() {
        setOff(getKeepXmlReport());
    }

    /** nojdoc
     *
     * @return */
    //----------
    public JCheckBoxOperator clickShowShortReportConsole() {
        getShowShortReportConsole().push();
        return this.genShowShortReportConsole;
    }

    /** nojdoc */
    public void setOnShowShortReportConsole() {
        setOn(getShowShortReportConsole());
    }

    /** nojdoc */
    public void setOffShowShortReportConsole() {
        setOff(getShowShortReportConsole());
    }

    //----------
    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickGenerateHtmlReport() {
        getGenerateHtmlReport().push();
        return this.genGenerateHtmlReport;
    }

    /** nojdoc */
    public void setOnGenerateHtmlReport() {
        setOn(getGenerateHtmlReport());
    }

    /** nojdoc */
    public void setOffGenerateHtmlReport() {
        setOff(getGenerateHtmlReport());
    }

    //-----------
    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickAutoOpenHtmlReport() {
        getAutoOpenHtmlReport().push();
        return this.genAutoOpenReport;
    }

    /** nojdoc */
    public void setOnAutoOpenHtmlReport() {
        setOn(getAutoOpenHtmlReport());
    }

    /** nojdoc */
    public void setOffAutoOpenHtmlReport() {
        setOff(getAutoOpenHtmlReport());
    }

    //-----------
    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator clickDefault() {
        getDefault().push();
        return this.genRadioDefault;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator clickUserDefinedDirectory() {
        getUserDefined().push();
        return this.genRadioUserDefinedDirectory;
    }

    //--------
    /** nojdoc
     *
     * @return */
    public JCheckBoxOperator clickTimeStamp() {
        getTimeStamp().push();
        return this.genTimeStampAndRetain;
    }

    /** nojdoc */
    public void setOnTimeStamp() {
        setOn(getTimeStamp());
    }

    /**
     * nojdoc
     */
    public void setOffTimeStamp() {
        setOff(getTimeStamp());
    }

    //- - - - - - - - - - - - - - - - - - - - - - -
    /**
     * nojdoc
     *
     * @param chkbox
     */
    private void setOn(JCheckBoxOperator chkbox) {
        if (chkbox.isSelected()) {
            return;
        }
        chkbox.push();
    }

    /**
     * nojdoc
     *
     * @param chkbox
     */
    private void setOff(JCheckBoxOperator chkbox) {
        if (!chkbox.isSelected()) {
            return;
        }
        chkbox.push();
    }

    /**
     * nojdoc
     *
     * @param nuValue
     *
     * @return
     */
    public JSpinnerOperator setPreviousVersionValue(int nuValue) {
        getPreviousVersionValue();
        this.genSpinnerPreviousVersion.setValue(nuValue);

        return this.genSpinnerPreviousVersion;
    }
}
