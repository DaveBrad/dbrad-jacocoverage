/* Copyright (c) 2017 dbradley. All rights reserved. */
package packg.appfunc.otdextensions;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import org.jtestdb.objecttestdata.OtdFileUtils;

/**
 * Class that processes a JSON file by loading the contents of the file into an
 * array and whilst keeping it current for any newer changes. The array list can
 * be stored into a OTD-object as string to compare against (mass data that is).
 *
 * @author dbradley
 */
final public class ProcessJsonFile {

    private final File jsonFILE;
    private long dateTimeOfFile = -1;

    /**
     * Lines of the JSON file
     */
    ArrayList<String> linesOfJsonFile = new ArrayList<>();

    /**
     * Create a JSON file processing object.
     *
     * @param jsonFilePath the full path to the JSON file
     */
    public ProcessJsonFile(String jsonFilePath) {
        this.jsonFILE = new File(jsonFilePath);
    }

    /**
     * Get the JSON file settings into an array list.
     *
     * @param settingLabel
     *
     * @return array list of string that are the settings in the JSON file
     */
    final public ArrayList<String> getSettingsFor(String settingLabel) {

        // update the settings from any newer file (a newer file occurs
        // due to a change in JaCoCo settings)
        updateFileNewer();

        ArrayList<String> typeList = new ArrayList<>();

        // "preferences" : {
        // "adtalpkgs.listing" : [ ],
        //
        // each type of record will be the setting-label followed by a
        // colon and then a bracket.
        //
        // find all data between the brackets
        //
        boolean processBetweenBrackets = false;

        String bracketOpen = "";
        String bracketClosed = "";
        long likeBracketCount = 0;

        for (String s : this.linesOfJsonFile) {
            if (!processBetweenBrackets) {
                if (!s.contains(settingLabel)) {
                    continue;
                }
                // "preferences" : {
                // "adtalpkgs.listing" : [ ],
                // "pkgfltr.listing" : {},
                String typeLines = s.replaceAll(" ", "");
                String[] splitTypeLine = typeLines.split(":");

                bracketOpen = splitTypeLine[1].substring(0, 1);

                switch (bracketOpen) {
                    case "{":
                        bracketClosed = "}";
                        break;
                    case "[":
                        bracketClosed = "]";
                        break;
                    case "(":
                        bracketClosed = ")";
                        break;
                    default:
                        throw new RuntimeException(String.format("Bracket not coded: %s", bracketOpen));
                }
                processBetweenBrackets = true;
            }
            //
            // count the open brackets on the line and the closed brackets on the line
            // (reuse the Dimension-class width = open, height = close
            Dimension counter = countOpenClosedBrackets(s, bracketOpen, bracketClosed);

            likeBracketCount += counter.width;
            likeBracketCount -= counter.height;

            typeList.add(s);

            if (likeBracketCount == 0) {
                break;
            }
        }
        return typeList;
    }

    /**
     * Processing the JSON file needs to understand the open and closed brackets
     * that encapsulate data.
     *
     * @param s
     * @param openBracket
     * @param closedBracket
     *
     * @return
     */
    private Dimension countOpenClosedBrackets(String s, String openBracket, String closedBracket) {
        Dimension counter = new Dimension(0, 0);

        char openC = openBracket.charAt(0);
        char closedC = closedBracket.charAt(0);

        int strLen = s.length();

        for (int i = 0; i < strLen; i++) {
            char c = s.charAt(i);
            if (c == openC) {
                counter.width++;

            } else if (c == closedC) {
                counter.height++;
            }
        }
        return counter;
    }

    /**
     * Update the file newer setting so as to allow the most recent JSON file to
     * be tested against.
     */
    final public void updateFileNewer() {
        long filesDateTime = this.jsonFILE.lastModified();
        if (dateTimeOfFile == filesDateTime) {
            return;
        }
        // load the file into the array-list so as to make it the current setting
        linesOfJsonFile.clear();
        OtdFileUtils.readFile(jsonFILE.getAbsolutePath(), linesOfJsonFile);
        dateTimeOfFile = filesDateTime;
    }

    /**
     * Get the full-path for the JSON file for a project.
     *
     * @return string of full-path
     */
    final public String getFullPath() {
        return this.jsonFILE.getAbsolutePath().replaceAll("\\\\", "/");
    }
}
