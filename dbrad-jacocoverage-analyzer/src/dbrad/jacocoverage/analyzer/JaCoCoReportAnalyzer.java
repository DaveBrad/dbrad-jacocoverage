/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.analyzer;

import dbrad.jacocofpm.mgr.com.FileSrcCodeDir;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.jacoco.report.xml.XMLFormatter;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * JaCoCo reports related utilities.
 * <br>See <a href="http://www.eclemma.org/jacoco/trunk/doc/index.html">JaCoCo
 * online documentation</a>.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class JaCoCoReportAnalyzer {

    /**
     * Encoding used by JaCoCo.
     */
    private static final String DEF_ENCODING = "UTF-8";

    /**
     * NetBeans console: color associated to covered instructions.
     */
    private static final Color CONSOLE_COVERED = new Color(44, 126, 0);

    /**
     * NetBeans console: color associated to partially covered instructions.
     */
    private static final Color CONSOLE_PARTIALLY_COVERED = new Color(186, 93, 0);

    /**
     * NetBeans console: color associated to not covered instructions.
     */
    private static final Color CONSOLE_NOT_COVERED = new Color(199, 0, 1);

    private JaCoCoReportAnalyzer() {
    }

    /**
     * Connect to a remote JaCoCo Java Agent and collect coverage data. The
     * remote agent has to run in output mode {@code tcpserver} and request
     * execution data.
     * <br>See
     * <a href="http://www.eclemma.org/jacoco/trunk/doc/agent.html">JaCoCo agent
     * configuration</a>.
     * <br>See
     * <a href="http://www.eclemma.org/jacoco/trunk/doc/examples/java/ExecutionDataClient.java">TCP
     * client example code</a>.
     *
     * @param address       coverage agent's TCP address (JaCoCo agent default
     *                      address is loopback interface, aka localhost).
     * @param port          coverage agent's TCP port (JaCoCo agent default port
     *                      is 6300).
     * @param jacocoexec    the binary report to dump collected coverage data
     *                      to.
     * @param prjClassesDir directory containing project's compiled classes.
     * @param prjSourcesDir the directory containing project's Java source
     *                      files.
     */
    public static void tcpToBinary(String address, int port,
            File jacocoexec,
            File prjClassesDir,
            File prjSourcesDir) {
        // TODO dump coverage data from remote agent (scheduled for 1.2.0 or later)
    }

    /**
     * Process the execution binary file into JaCoCo structures for processing
     * to coverage details, and merge (load) a merge file if needed as per the
     * settings.
     *
     * @param jacocoexec the JaCoCo binary report.
     * @param isMergeOn  boolean true if to merge.
     *
     * @return ExecFileLoader object for processing into
     *
     * @throws IOException if an I/O error occurs.
     */
    public static ExecFileLoader processExecFileToMerge(File jacocoexec, boolean isMergeOn)
            throws IOException {
        ExecFileLoader execFileLoader = new ExecFileLoader();
        execFileLoader.load(jacocoexec);

        // a merge file is kept along side the execution binary file
        File merge = new File(jacocoexec.getParentFile().getAbsolutePath() + "/merge");

        // this is a psuedo merge
        if (isMergeOn) {
            // if an existing merge file load it 
            if (merge.exists()) {
                execFileLoader.load(merge);
            }
            // then save a newer merge file
            execFileLoader.save(merge, false);
        } else {
            // merge off so clean up any old merge files
            merge.delete();
        }
        return execFileLoader;
    }

    /**
     * Load a JaCoCo binary report and convert it to HTML.
     * <br>See
     * <a href="http://www.eclemma.org/jacoco/trunk/doc/examples/java/ReportGenerator.java">report
     * generator example code</a>.
     *
     * @param jacocoexec    the JaCoCo binary report.
     * @param isMergeOn     boolean true if to merge.
     * @param srcCodePair   the directory containing project's Java source and
     *                      compiled classes.
     * @param reportIntoDir the directory containing where reports are to be
     *                      placed into
     * @param excludesList  array list of exclude string(s)
     *
     * @return absolute path of HTML report {@code index.html} file for the
     *         purpose of launching in the default HTML browser
     *
     * @throws FileNotFoundException if the JaCoCo binary report, compiled
     *                               classes or Java sources files directory
     *                               can't be found.
     * @throws IOException           if an I/O error occurs.
     */
    public static String toHtmlReport(FileSrcCodeDir srcCodePair, File jacocoexec,
            boolean isMergeOn,
            File reportIntoDir, ArrayList<String> excludesList)
            throws FileNotFoundException, IOException {

        ExecFileLoader execFileLoader = processExecFileToMerge(jacocoexec, isMergeOn);

        ExecutionDataStore executionDataStore = execFileLoader.getExecutionDataStore();
        SessionInfoStore sessionInfoStore = execFileLoader.getSessionInfoStore();

//        // Load the JaCoCo binary report.
//        FileInputStream fiStream = new FileInputStream(jacocoexec);
//
//        // working objects for processing the bin jacoc-executing results file
//        ExecutionDataStore executionDataStore = new ExecutionDataStore();
//        SessionInfoStore sessionInfoStore = new SessionInfoStore();
//        try {
//            // process the data in the binary file
//            //99 would this area filter the includes etc.....
//            ExecutionDataReader executionDataReader = new ExecutionDataReader(fiStream);
//
//            executionDataReader.setExecutionDataVisitor(executionDataStore);
//
//            executionDataReader.setSessionInfoVisitor(sessionInfoStore);
//
//            while (executionDataReader.read()) {
//            }
//        } finally {
//            fiStream.close();
//        }
        //99 this will have all the processed data
        // Convert the binary report to HTML.
        CoverageBuilder coverageBuilder = new CoverageBuilder();

        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

        String excludesActive = analyzerAddClassesWithExcludesFiltering(srcCodePair,
                analyzer, excludesList);

        // prepare the HTML for report generation
        HTMLFormatter htmlformatter = new HTMLFormatter();

        IBundleCoverage bundleCoverage;
        if (excludesActive.isEmpty()) {
            bundleCoverage = coverageBuilder.getBundle(
                    String.format("JaCoCo analysis: \"%s\" (uses JaCoCo from EclEmma)",
                            srcCodePair.buildReportTitle()));
        } else {
            bundleCoverage = coverageBuilder.getBundle(
                    String.format("JaCoCo analysis: \"%s\" (uses JaCoCo from EclEmma) Exclusion applied",
                            srcCodePair.buildReportTitle()));

            htmlformatter.setFooterText(String.format("Excluded: %s",
                    excludesActive.substring(1)));
        }
        // 
        IReportVisitor repVisitor
                = htmlformatter.createVisitor(
                        new FileMultiReportOutput(srcCodePair.getHtmlReportFile(reportIntoDir)));

        //
        Collection<ExecutionData> execDataContentArr = executionDataStore.getContents();
        List<SessionInfo> sessInfoArr = sessionInfoStore.getInfos();

        repVisitor.visitInfo(sessInfoArr, execDataContentArr);

        repVisitor.visitBundle(bundleCoverage,
                new DirectorySourceFileLocator(srcCodePair, DEF_ENCODING, 4));

        repVisitor.visitEnd();
        return new File(srcCodePair.getHtmlReportFile(reportIntoDir), "index.html").getAbsolutePath();
    }

    /**
     * Load a JaCoCo binary report and convert it to XML&#46;
     * <br>See
     * <a href="http://www.eclemma.org/jacoco/trunk/doc/examples/java/ReportGenerator.java">report
     * generator example code</a>.
     *
     * @param jacocoexec    the JaCoCo binary report.
     * @param isMergeOn     boolean true if to merge.
     * @param srcCodePair   the directory containing project's Java source and
     *                      compiled classes.
     * @param reportIntoDir the directory containing where reports are to be
     *                      placed into
     * @param excludesList  array list of exclude string(s)
     *
     * @throws FileNotFoundException if the JaCoCo binary report, compiled
     *                               classes or Java sources files directory
     *                               can't be found.
     * @throws IOException           if an I/O error occurs.
     */
    public static void toXmlReport(FileSrcCodeDir srcCodePair, File jacocoexec,
            boolean isMergeOn,
            File reportIntoDir, ArrayList<String> excludesList)
            throws FileNotFoundException, IOException {
        // Load the JaCoCo binary report.

        ExecFileLoader execFileLoader = processExecFileToMerge(jacocoexec, isMergeOn);

        ExecutionDataStore executionDataStore = execFileLoader.getExecutionDataStore();
        SessionInfoStore sessionInfoStore = execFileLoader.getSessionInfoStore();

//        FileInputStream fis = new FileInputStream(jacocoexec);
//
//        ExecutionDataStore executionDataStore = new ExecutionDataStore();
//
//        SessionInfoStore sessionInfoStore = new SessionInfoStore();
//
//        try {
//            ExecutionDataReader executionDataReader = new ExecutionDataReader(fis);
//
//            executionDataReader.setExecutionDataVisitor(executionDataStore);
//
//            executionDataReader.setSessionInfoVisitor(sessionInfoStore);
//
//            while (executionDataReader.read()) {
//            }
//        } finally {
//            fis.close();
//        }
        // Convert the binary report to XML.
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

        String excludesApply = analyzerAddClassesWithExcludesFiltering(srcCodePair,
                analyzer, excludesList);

        IBundleCoverage bundleCoverage;

        if (excludesApply.isEmpty()) {
            bundleCoverage = coverageBuilder.getBundle(
                    "JaCoCoverage analysis (powered by JaCoCo from EclEmma)");
        } else {
            bundleCoverage = coverageBuilder.getBundle(
                    "JaCoCoverage analysis (powered by JaCoCo from EclEmma) EXCLUDES APPLIED");
        }
        //
        XMLFormatter xmlformatter = new XMLFormatter();
        xmlformatter.setOutputEncoding(DEF_ENCODING);

        IReportVisitor visitor
                = xmlformatter.createVisitor(
                        new FileOutputStream(srcCodePair.getXmlReportFile(reportIntoDir)));

        visitor.visitInfo(sessionInfoStore.getInfos(), executionDataStore.getContents());
        visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(srcCodePair, DEF_ENCODING, 4));

        visitor.visitEnd();
    }

    /**
     * Add from classes directory provided and process each single file class.
     *
     * @param analyzer     Jacoco report analyzer to include classes to
     * @param srcCodePair  the source and classes directory pair to process
     * @param excludesList the excludes filter list
     *
     * @return String of the excludes patterns that were applied as excludes
     *
     * @throws IOException when there is an IO issue
     */
    public static String analyzerAddClassesWithExcludesFiltering(FileSrcCodeDir srcCodePair,
            Analyzer analyzer, ArrayList<String> excludesList) throws IOException {

        // length of the base directory where the package portion of a file-path
        File classFile = srcCodePair.getClassesAssociatedFile();
        int baseDirLen = classFile.getAbsolutePath().length() + 1;

        // process the whole classes directory
        String excludeStr = analyzerAddSingleClass(srcCodePair,
                analyzer, classFile, baseDirLen, excludesList, "");
        excludeStr = excludeStr.replaceAll("/", ".");

        return excludeStr;
    }

    /**
     * Add a single class from the classes directory provided if it is not
     * excluded.
     *
     * @param analyzer     Jacoco report analyzer to include class to
     * @param fileOrDir    file or directory File item to process
     * @param baseDirLen   length of the base directory where the package
     *                     portion of a file-path
     * @param excludesList the excludes filter list
     *
     * @throws IOException when there is an IO issue
     */
    private static String analyzerAddSingleClass(FileSrcCodeDir srcCodePair,
            Analyzer analyzer, File fileOrDir,
            int baseDirLen, ArrayList<String> excludesList,
            String exclusionsFound) throws IOException {
        if (fileOrDir.isDirectory()) {
            File[] fileList = fileOrDir.listFiles();

            for (File fileItem : fileList) {
                exclusionsFound = analyzerAddSingleClass(srcCodePair,
                        analyzer, fileItem, baseDirLen, excludesList, exclusionsFound);
            }
        }
        // assume a file, 
        // needs to be a class file to process for coverage reports
        if (fileOrDir.getName().endsWith(".class")) {
            // is the class to be reported or not (the order of checks
            // determines report additional information)
            // a) not if exclude patterns matches 
            // b) not if the cover-on setting of off for the whole
            //
            String clazzFullPath = fileOrDir.getPath();
            String clazzPkgPathPart = clazzFullPath.substring(baseDirLen).replaceAll("\\\\", "/");

            // ensure all items that are excludes do not get into reports
            // 
            // - change from file-path to package-format
            // - remove the .class suffix 
            //
            int lenClassPath = clazzPkgPathPart.length() - 5;
            String clazzPkgPath = clazzPkgPathPart
                    .replaceAll("/", ".") // xxx/yyy/xxx/classname.class =>  xxx.yyy.xxx.classname.class
                    .substring(0, lenClassPath); // xxx.yyy.xxx.classname.class => xxx.yyy.xxx.classname.

            for (String pkgToExclude : excludesList) {
                if (clazzPkgPath.startsWith(pkgToExclude)) {
                    // additional information
                    exclusionsFound = exclusionsFound + ":" + pkgToExclude + "*";
                    return exclusionsFound;
                }
            }
            // is to be covered so add to the list to be analyzed
            if (srcCodePair.checkIsToBeCovered(clazzPkgPath)) {
                analyzer.analyzeAll(fileOrDir);
            }
        }
        return exclusionsFound;
    }

    /**
     * Load JaCoCo coverage data and show it to a NetBeans console tab.
     *
     * @param coverageData              the JaCoCo coverage data to show.
     * @param consoleReportHeadingStart state for outputting the console report
     *                                  heading only once
     * @param consoleReportFooterEnd    state for outputting the console report
     *                                  footer at end of report
     * @param timeStampDT               string of coverage start time-stamp
     * @param tabName4OutputView        the name of the NetBeans console tab to
     *                                  open
     * @param projectNameReportingOn    the project name of the NBProject being
     *                                  processed (selected and its
     *                                  associated-project)
     *
     * @throws IOException if a NetBeans console related error occurs.
     */
    public static void toConsoleReport(Map<String, JavaClass> coverageData,
            boolean consoleReportHeadingStart,
            boolean consoleReportFooterEnd,
            String timeStampDT,
            String tabName4OutputView,
            String projectNameReportingOn)
            throws IOException {
        InputOutput io = IOProvider.getDefault().getIO(tabName4OutputView, false);

        try {
            if (consoleReportHeadingStart) {
                io.getOut().reset();

                IOColorPrint.print(io, "=== JaCoCoverage report (powered by JaCoCo from EclEmma) ===\n", Color.GRAY);

                IOColorPrint.print(io, "Recorded: " + timeStampDT + "\n", Color.GRAY);
                IOColorPrint.print(io, "Covered | Partially covered | Not covered | Java Class\n\n", Color.GRAY);
            }
            List<JavaClass> sortedClasses = new ArrayList<>(coverageData.values());

            Collections.sort(sortedClasses);
            io.getOut().print(projectNameReportingOn + '\n');

            for (JavaClass jclass : sortedClasses) {
                IOColorPrint.print(io, String.format("%5s", jclass.getNumberCoveredLines()), CONSOLE_COVERED);
                IOColorPrint.print(io, " " + String.format("%5s", jclass.getNumberPartiallyCoveredLines()), CONSOLE_PARTIALLY_COVERED);
                IOColorPrint.print(io, " " + String.format("%5s", jclass.getNumberNotCoveredLines()), CONSOLE_NOT_COVERED);
                Color classCovColor;

                // get the package and class names and change to be Java period
                // versus slashes
                // '/mypackage/subpackge/classname.java
                String packageNameString = jclass.getPackageName();
                packageNameString = packageNameString.substring(1);
                packageNameString = packageNameString.replaceAll("/", ".");

                // 'mypackage.subpackge.classname.java
                io.getOut().print("    " + packageNameString);

                String classNameString = jclass.getClassName();

                // define the coloring schema for the output fields
                boolean existCL = jclass.getNumberCoveredLines() > 0;
                boolean existPCL = jclass.getNumberPartiallyCoveredLines() > 0;
                boolean existNCL = jclass.getNumberNotCoveredLines() > 0;

                if (existCL) {
                    if (existNCL || existPCL) {
                        classCovColor = CONSOLE_PARTIALLY_COVERED;
                    } else {
                        classCovColor = CONSOLE_COVERED;
                    }
                } else {
                    if (existPCL) {
                        classCovColor = CONSOLE_PARTIALLY_COVERED;
                    } else {
                        classCovColor = CONSOLE_NOT_COVERED;
                    }
                }
                IOColorPrint.print(io, classNameString + '\n', classCovColor);
            }

            if (consoleReportFooterEnd) {
                IOColorPrint.print(io, "\nEnd of report\n", Color.GRAY);
                // IOColorPrint.print(io, "You can ask questions and report bugs by visiting: ", Color.GRAY);
                // IOColorPrint.print(io, "https://github.com/jonathanlermitage/tikione-jacocoverage", Color.BLUE);
            }
            io.getOut().println();
        } finally {
            io.getOut().close();
        }
    }
}
