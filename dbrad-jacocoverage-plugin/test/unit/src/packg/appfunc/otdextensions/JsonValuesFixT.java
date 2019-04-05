/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc.otdextensions;

import dbrad.jacocofpm.json.JsonMap;
import java.util.ArrayList;
import java.util.EnumSet;
import org.jtestdb.objecttestdata.OtdElementGroup;
import org.jtestdb.objecttestdata.OtdFixtureClass;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_ALL;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_EXCLUDE;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;

/**
 *
 * @author dbradley
 */
public class JsonValuesFixT extends OtdFixtureClass {

    JsonValuesOtd otd;

    /**
     * Create an OTD fixture object for the JSON values.
     * 
     * @param otd the OTD object to base fixture values from.
     */
    public JsonValuesFixT(JsonValuesOtd otd) {
        super(JsonValuesOtd.class, "dummykey");
        this.otd = otd;
    }

    /**
     * Test NOT SUPPORTED.
     * @param os 
     */
    @Override
    public void test(Object[] os) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Refresh the fixtures OTD data as per the OTD object data and the Types of
     * data in the enumSet.
     * 
     * @param enumSetTypes set of the kinds of data to produce.
     */
    @Override
    public void refresh(EnumSet<?> enumSetTypes) {
        boolean allSet = enumSetTypes.contains(JSON_ALL);

        ProcessJsonFile jsonFileContent = new ProcessJsonFile(otd.getJsonFilePath());
        jsonFileContent.updateFileNewer();

        int indexOrder = 0;
        ArrayList<String> tmpList;

        if (enumSetTypes.contains(JSON_PREFERENCES) || allSet) {

            tmpList = jsonFileContent.getSettingsFor(JsonMap.JSON_GENERAL);

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
                OtdElementGroup tdeg = OtdElementGroup.newAdd(indexOrder, JSON_PREFERENCES, content);
                appendTdegItem(tdeg);

                indexOrder++;
            }
        }
        if (enumSetTypes.contains(JSON_EXCLUDE) || allSet) {
            tmpList = jsonFileContent.getSettingsFor(JsonMap.JSON_EXCLUDE_PACKAGES);

            // public int add(int order, String projectName, JsonDataTypes jsonType, String dataString) {
            for (String content : tmpList) {
                OtdElementGroup tdeg = OtdElementGroup.newAdd(indexOrder, JSON_EXCLUDE, content);
                appendTdegItem(tdeg);

                indexOrder++;
            }
        }
        if (enumSetTypes.contains(JSON_FILTER) || allSet) {
            tmpList = jsonFileContent.getSettingsFor(JsonMap.JSON_PKGFILTER);

            // public int add(int order, String projectName, JsonDataTypes jsonType, String dataString) {
            for (String content : tmpList) {
                OtdElementGroup tdeg = OtdElementGroup.newAdd(indexOrder, JSON_FILTER, content);
                appendTdegItem(tdeg);

                indexOrder++;
            }
        }
    }

    /**
     * Refresh the data as a single object, NOT SUPPORTED.
     */
    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
