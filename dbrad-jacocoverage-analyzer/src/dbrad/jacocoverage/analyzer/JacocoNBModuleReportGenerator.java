/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Graeme Ingleby (pre-2017).
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.analyzer;

import dbrad.jacocofpm.mgr.com.FileSrcCodeDir;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportGroupVisitor;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.MultiSourceFileLocator;
import org.jacoco.report.html.HTMLFormatter;
import org.jacoco.report.xml.XMLFormatter;

/**
 * Class for generating NBM reports, with modifications for dbrad-jacocoverage.
 *
 * @author Graeme Ingleby (pre-2017)
 * @author dbradley (2017)
 */
public class JacocoNBModuleReportGenerator {

    /**
     * Type of report to create XML, HTML,.....
     */
    public enum NbModReportType {
        /** create XML report */
        XML,
        /** create HTML report */
        HTML
    }

    private static final String DEF_ENCODING = "UTF-8";

    private final boolean isGLobalOverridden;

    private final IReportVisitor visitor;
    private final IReportGroupVisitor groupVisitor;
    private final File executionDataFile;
    private final ExecFileLoader execFileLoader;

    /**
     * Create the object for NBM report generation.
     *
     * @param executationDataFile data file from coverage request
     * @param isMergeOn           boolean true if to merge.
     * @param rootProjectName     the principle-root project name for the report
     * @param timeStampString     the time-stamp for the execution for the
     *                            report header
     * @param reportdir           directory to store report into
     * @param reportType          the report type to create
     * @param isGLobalOverridden  is project specific setting
     *
     * @throws IOException on any IO problems
     */
    public JacocoNBModuleReportGenerator(File executationDataFile,
            boolean isMergeOn,
            String rootProjectName, String timeStampString,
            File reportdir, NbModReportType reportType,
            boolean isGLobalOverridden) throws IOException {
        //
        this.isGLobalOverridden = isGLobalOverridden;

        this.executionDataFile = executationDataFile;
        execFileLoader = JaCoCoReportAnalyzer.processExecFileToMerge(executionDataFile, isMergeOn);

//99        // get an array of all the execution files
//        File parentExecDirFile = executionDataFile.getParentFile();
//
//        File[] allExecFilesArr = parentExecDirFile.listFiles();
//
//        execFileLoader.load(executionDataFile);
        if (reportType == NbModReportType.XML) {
            XMLFormatter xmlformatter = new XMLFormatter();
            xmlformatter.setOutputEncoding(DEF_ENCODING);
            visitor = xmlformatter.createVisitor(new FileOutputStream(reportdir));
        } else {
            HTMLFormatter htmlFormater = new HTMLFormatter();
            visitor = htmlFormater.createVisitor(new FileMultiReportOutput(reportdir));
        }
        visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
                execFileLoader.getExecutionDataStore().getContents());

        groupVisitor = visitor.visitGroup(
                String.format("JaCoCo by-grouped for: %s %s",
                        rootProjectName, timeStampString));
    }

    /**
     *
     * @param projectName       string project name for report
     * @param srcCodeDirPairs   source/test code and classes directory pairs
     * @param classDirectories  class directories array
     * @param sourceDirectories source/test code directories
     * @param excludesList      array list of excludes string(s)
     *
     * @throws IOException
     */
    public void processNBModule(String projectName,
            ArrayList<FileSrcCodeDir> srcCodeDirPairs,
            List<String> classDirectories,
            List<String> sourceDirectories,
            ArrayList<String> excludesList) throws IOException {

        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

        //
        for (FileSrcCodeDir srcCodePair : srcCodeDirPairs) {

            if (this.isGLobalOverridden) {
                if (!srcCodePair.isOnCodeFolderState()) {
                    continue;
                }
            }
            JaCoCoReportAnalyzer.analyzerAddClassesWithExcludesFiltering(
                    srcCodePair, analyzer, excludesList);
            //      analyzer.analyzeAll(new File(classDirectory));
        }
        IBundleCoverage bundleCoverage = coverageBuilder.getBundle(projectName);

        MultiSourceFileLocator sourceLocator = new MultiSourceFileLocator(4);

        for (String sourceDirectory : sourceDirectories) {
            sourceLocator.add(new DirectorySourceFileLocator(new File(sourceDirectory), DEF_ENCODING, 4));
        }
        groupVisitor.visitBundle(bundleCoverage, sourceLocator);
    }

    /**
     * End the report generator on the visitor.
     *
     * @throws IOException
     */
    public void end() throws IOException {
        visitor.visitEnd();
    }
}
