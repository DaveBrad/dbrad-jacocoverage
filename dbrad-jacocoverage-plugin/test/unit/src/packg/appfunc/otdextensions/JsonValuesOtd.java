/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import dbrad.jacocofpm.json.JsonMap;
import java.util.ArrayList;
import java.util.EnumSet;
import static junit.framework.TestCase.assertEquals;
import org.jtestdb.objecttestdata.ObjectTestData;
import org.jtestdb.objecttestdata.OtdAut;
import org.jtestdb.objecttestdata.OtdElementGroup;
import org.jtestdb.objecttestdata.OtdSingleWithTypes;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_ALL;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_EXCLUDE;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;

/**
 * Class object-test-data extension that records the Json values for comparison
 * at verification points.
 * <p>
 * The capture and verification of JSON data is for its content and not the JSON
 * formatting, so the Jacoco-dbrad OTD class will capture the data in a set
 * order. This orders the data capture to ensure any changes made by the Jackson
 * (JSON) or the application-under-test ordering/processing of the data does not
 * impact how the JSON OTD processes the data.
 *
 * @author dbradley
 */
public class JsonValuesOtd extends OtdSingleWithTypes {

    /**
     * The types that the JSON file supports and the data that may be captured.
     */
    public static enum JsonDataTypes {
        /** Capture all the settings types */
        JSON_ALL,
        /**
         * Only capture the general settings values */
        JSON_PREFERENCES,
        /**
         * Only capture the exclude packages settings values */
        JSON_EXCLUDE,
        /**
         * Only capture the filter packages settings values */
        JSON_FILTER
    }

    private ProcessJsonFile jsonFileContent;

    /** nojdoc */
    public JsonValuesOtd() {
        super(JsonDataTypes.class);
    }

    /** nojdoc
     *
     * @param jsonFilePath
     * @param sourceDirectory
     * @param packageName
     * @param testDataClassName
     */
    public JsonValuesOtd(String jsonFilePath, String sourceDirectory, String packageName, String testDataClassName) {
        super(jsonFilePath, sourceDirectory, packageName, testDataClassName, JsonDataTypes.class);
    }

    /** nojdoc
     *
     * @param action
     * @param expectedIndex
     * @param tdeg
     *
     * @return
     */
    @Override
    protected Object processTDEG(OtdElementGroup.ProcessAction action, int expectedIndex, OtdElementGroup tdeg) {

        // get the data into local variables for the purpose of processing
        // following
        JsonDataTypes jsonDataType = (JsonDataTypes) tdeg.getElementN(0);
        String stringOfSetting = (String) tdeg.getElementN(1);

        Object result = null;

        switch (action) {
            case TOSTRING_TDEG:
                // 
                String resltStr = String.format("%s %s", jsonDataType, stringOfSetting);

                result = resltStr;
                break;

            case VERIFY_TDEG:
                // the assumption is that the user provides a valid 
                // and expected position into the table, if this does 
                // not fit then we assume an application error. OR, this 
                // is a calling test code error and needs to be fixed
                Object[] thisObj = this.getTdegDataValuesFromArr(expectedIndex);

                assertEquals(thisObj[0], jsonDataType);

                // OTD data value in thisObj 
                String otdDataAtdi = (String) thisObj[1];

                // the user-defined-dir of the preferences under test uses a temporary
                // directory for each test being run; this will change each time
                // a test is run, so any database file will have a mismatch
                //
                // "C:/Users/dbradley/AppData/Local/Temp/tstJacoco_3990660208015912788.dir/worksp"
                //
                if (otdDataAtdi.contains("tstJacoco_")) {
                    // need to change the tstJacoco_ to the current setting of
                    // the temporary file as extracted fromone of the file
                    // path item available
                    int tstJacocoStartIdx = otdDataAtdi.indexOf("tstJacoco_");
                    int tstJacocoDirEndIdx = otdDataAtdi.indexOf(".dir", tstJacocoStartIdx) + 4;

                    String part1 = otdDataAtdi.substring(0, tstJacocoStartIdx);
                    String part2 = otdDataAtdi.substring(tstJacocoDirEndIdx);

                    // extract the current setting
                    String jsonFileStr = (String) this.mySingleObj;

                    int jsonTstJacocoStartIdx = jsonFileStr.indexOf("tstJacoco_");
                    int jsonTstJacocoDirEndIdx = jsonFileStr.indexOf(".dir", jsonTstJacocoStartIdx) + 4;

                    String partTstJacocoString = jsonFileStr.substring(jsonTstJacocoStartIdx, jsonTstJacocoDirEndIdx);

                    // the fwd slashes were maintained
                    otdDataAtdi = String.format("%s%s%s", part1, partTstJacocoString, part2);
                }
                assertEquals(otdDataAtdi, stringOfSetting);

                break;
            default:
                throw new AssertionError(action.name());
        }
        return result;
    }

    /** nojdoc
     *
     * @param otd
     * @param oos
     * @param enumSetTypes
     */
    @Override
    protected void add2CodeStatements4SingleWithTypes(ObjectTestData otd, OtdAut.ObjOfSingle oos,
            EnumSet<?> enumSetTypes) {

        boolean allSet = enumSetTypes.contains(JSON_ALL);

        if (this.jsonFileContent == null) {
            this.jsonFileContent = new ProcessJsonFile(mySingleObject());
        }
        this.jsonFileContent.updateFileNewer();

        int indexOrder = 0;
        ArrayList<String> tmpList;

        if (enumSetTypes.contains(JSON_PREFERENCES) || allSet) {

            tmpList = this.jsonFileContent.getSettingsFor(JsonMap.JSON_GENERAL);

            // public int add(int order, String projectName, JsonDataTypes jsonType, String dataString) {
            for (String content : tmpList) {
                // if the tstJacoco_ temporary dir then need to modify the 
                // data to be platform independent
                //
                int idx = content.indexOf("tstJacoco_");
                if (idx > -1) {
                    int idxFwdPart = content.indexOf(": \"");

                    String part1 = content.substring(0, idxFwdPart + 3);

                    content = String.format("%s%s", part1, content.substring(idx));
                }
                add3CodeGenSuper(otd, indexOrder, JSON_PREFERENCES, content);
                indexOrder++;
            }
        }
        if (enumSetTypes.contains(JSON_EXCLUDE) || allSet) {
            tmpList = this.jsonFileContent.getSettingsFor(JsonMap.JSON_EXCLUDE_PACKAGES);

            // public int add(int order, String projectName, JsonDataTypes jsonType, String dataString) {
            for (String content : tmpList) {
                add3CodeGenSuper(otd, indexOrder, JSON_EXCLUDE, content);
                indexOrder++;
            }
        }
        if (enumSetTypes.contains(JSON_FILTER) || allSet) {
            tmpList = this.jsonFileContent.getSettingsFor(JsonMap.JSON_PKGFILTER);

            // public int add(int order, String projectName, JsonDataTypes jsonType, String dataString) {
            for (String content : tmpList) {
                add3CodeGenSuper(otd, indexOrder, JSON_FILTER, content);
                indexOrder++;
            }
        }
    }

    /** nojdoc
     *
     * @param order
     * @param jsonType
     * @param dataString
     *
     * @return
     */
    public int add(int order, JsonDataTypes jsonType, String dataString) {
        return super.add1Super(order, EnumSet.of(jsonType), jsonType, dataString);
    }

    /** nojdoc
     *
     * @param es
     *
     * @return
     */
    @Override
    protected String provideCode4WithTypes(EnumSet<?> es) {
        // use the first two letters of the JsonDataTypes element to
        // identify the item
        EnumSet<JsonDataTypes> jdtEs = (EnumSet<JsonDataTypes>) es;

        String jdtCode = "";

        // all, then ignore all following settings
        if (es.contains(JSON_ALL)) {
            return "AL";
        }
        // need to process the other settings for a code
        for (JsonDataTypes jdt : jdtEs) {
            String twoLetters = jdt.toString().substring(0, 2);

            jdtCode = String.format("_%s", jdtCode, twoLetters);
        }
        return jdtCode;
    }

    /** nojdoc
     * @param version
     * @param testLabel
     * @param withTypes
     * @return  */
    public JsonValuesOtd getSingle(int version, String testLabel, EnumSet<?> withTypes) {
        JsonValuesOtd nuSingle
                = (JsonValuesOtd) super.get1Super(version, testLabel, withTypes);

        return nuSingle;
    }

    /** nojdoc
     * @param version
     * @param testLabel
     * @return  
     */
    public JsonValuesOtd getAllTypes(int version, String testLabel) {
        JsonValuesOtd getOtd
                = (JsonValuesOtd) (JsonValuesOtd) super.get1Super(
                        version, testLabel, EnumSet.of(JsonDataTypes.JSON_ALL));
        getOtd.mySingleObj = mySingleObject();

        return getOtd;
    }

    /** nojdoc
     * @return  
     */
    public String getJsonFilePath() {
        return this.mySingleObject();
    }

    /** nojdoc */
    public void verify() {
        JsonValuesFixT fixT = new JsonValuesFixT(this);
        super.verify1Super(fixT, true);
    }

    /** nojdoc
     * @return  
     */
    @Override
    protected String mySingleObject() {
        return (String) this.mySingleObj;
    }
}
