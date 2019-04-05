/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocofpm.util;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 * Some utilities.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class UtilsFileMgmt {

    /**
     * The white-space character ({@code &#92;u0020}).
     */
    public static final char SPACE = '\u0020';

    /**
     * The tabulation character ({@code &#92;u0009}).
     */
    public static final char TAB = '\u0009';

    /**
     * Check a regular expression.
     *
     * @param src     the text to examine.
     * @param pattern the compiled regular expression, targeted area are
     *                enclosed with parenthesis.
     *
     * @return <code>true</code> if the regular expression is checked, otherwise
     *         <code>false</code>.
     */
    public static boolean checkRegex(String src, Pattern pattern) {
        return pattern.matcher(src).find();
    }

    /**
     * Return the capturing groups from the regular expression in the string.
     *
     * @param src              the string to search in.
     * @param pattern          the compiled pattern.
     * @param expectedNbMatchs the expected tokens matched, for performance
     *                         purpose.
     *
     * @return all the strings matched (in an ArrayList).
     */
    public static List<String> getGroupsFromRegex(String src, Pattern pattern, int expectedNbMatchs) {
        List<String> res = new ArrayList<>(expectedNbMatchs);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            for (int group = 1; group <= matcher.groupCount(); group++) {
                res.add(matcher.group(group));
            }
        }
        return res;
    }

    /**
     * Get the JaCoCo binary report file of the given project.
     *
     * @param ideJacocoConfig the project to get JaCoCo report file.
     * @param contextFile     Netbeans context FileObject that allows
     *                        determination of a context-menu selected item
     *
     * @return the JaCoCo report file.
     */
    public static File getJacocoBinReportFile(IdeProjectJacocoverageConfig ideJacocoConfig,
            FileObject contextFile) {

        String prjdir = ideJacocoConfig.getNbProjectDirPath(); // NBUtils.getNbProjectDirPath(nbProject);

        String bindir;
        if (prjdir.contains(",") || prjdir.contains(";") || prjdir.contains("=")) {
            //
            // use the systems temporary directory structure
            //
            // FIXED GitHub #9 JavaAgent doesn't allow comma in report file's path (comma is used to separate parameters).
            // FIXED 20130625 extended GitHub #9 principle to other sensible characters.
            bindir = System.getProperty("java.io.tmpdir");
        } else {
            // place the jacoco.exec-nnnnnnnnnnnn file in the projects working directory
            // tmp location
            bindir = ideJacocoConfig.getProjectDbradJacocoTmpDirPath();
        }
        // make the binary-report-file unique via a time stamp
        String execFileName;
        if (contextFile != null) {
            // use the file name, remove the .java file part
            execFileName = contextFile.getName();
        } else {
            execFileName = "jacoco";
        }
        // a timestamp to milli-seconds so users may know what to use        
        return new File(bindir, execFileName + ".exec-" + createTimeStampStr4ExecReport());
    }

    /**
     * Create a time-stamp string as file name content for the exec report file.
     *
     * @param runRequestDateTimeStamp Date instance of the time-stamp
     *
     * @return string in yyyyMMddHHmmssSSS format
     */
    private static String createTimeStampStr4ExecReport() {
        Date nowTime = Calendar.getInstance().getTime();

        return String.format("%s",
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowTime));
    }

//99    private static File makeDirs(File path) {
//        if (!path.isDirectory()) {
//            path.mkdirs();
//        }
//        return path;
//    }
    /**
     * Indicate if a Java line describes a finished instruction, otherwise a
     * part of a multi-line instruction (or no instruction).
     *
     * @param inst the Java line.
     *
     * @return {@code true} if instruction is finished, otherwise {@code false}.
     */
    public static boolean isIntructionFinished(String inst) {
        String trim = org.apache.commons.lang3.StringUtils.strip(inst);
        boolean finished = trim.endsWith(";") || trim.endsWith("}") || trim.endsWith("{");
        if (!finished && (trim.contains(";") || trim.contains("}") || trim.contains("{"))) {
            // Remove strings (a string could contains a semi-colon) and comments and check again.
            trim = trim.replaceAll("\\\\\"", "").replaceAll("\".*\"", "");
            trim = org.apache.commons.lang3.StringUtils.strip(trim.replaceAll(";[^;]*//.*$", ";").replaceAll("/\\*.*\\*/", ""));
            finished = trim.endsWith(";") || trim.endsWith("}") || trim.endsWith("{");
        }
        return finished;
    }

    /**
     * Compress a file to Zip. The archive contains an entry named as the source
     * file.
     *
     * @param src       the source file to compress.
     * @param dst       the zipped output file.
     * @param entryname the name of the entry stored in the zipped file.
     * @param async     if {@code true}, the compression process will be done in
     *                  a separate parallel thread, otherwise the current
     *                  thread.
     */
    public static void zip(final File src, final File dst, final String entryname, boolean async) {
        if (async) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    zip(src, dst, entryname);
                }
            }).start();
        } else {
            zip(src, dst, entryname);
        }
    }

    /**
     * Compress a file to Zip. The archive contains an entry named as the source
     * file
     *
     * @param src       the source file to compress.
     * @param dst       the zipped output file.
     * @param entryname the name of the entry stored in the zipped file.
     *
     * @throws FileNotFoundException if the source file doesn't exist.
     */
//    @SuppressWarnings("NestedAssignment")
    private static void zip(File src, File dst, String entryname) {
        byte[] buffer = new byte[512];
        try {
            try (FileOutputStream dstStrm = new FileOutputStream(dst); ZipOutputStream zipStrm = new ZipOutputStream(dstStrm)) {
                try (FileInputStream srcStrm = new FileInputStream(src)) {
                    ZipEntry entry = new ZipEntry(entryname);
                    zipStrm.putNextEntry(entry);
                    int len;
                    while ((len = srcStrm.read(buffer)) > 0) {
                        zipStrm.write(buffer, 0, len);
                    }
                }
                zipStrm.closeEntry();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private UtilsFileMgmt() {
    }
}
