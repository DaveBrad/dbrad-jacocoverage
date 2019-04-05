package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
 
import java.util.EnumSet;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;
import packg.appfunc.otdextensions.JsonValuesOtd;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_ALL;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_EXCLUDE;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;

/**
 * Class code for an application-class-file-processor-instance (ACFPI) for
 * an object-test-data generated test data class. The static methods within
 * the ACFPI file represent application-test-data-instances (ATDI) which hold
 * test-data-element-group (TDEG) data. (Except 'getResetDataFor' which the 
 * framework uses for reset-data-for capabilities.)
 */
public final class SingleSEJsonTD {
	/**
	 * <pre>
	 * Resetting test data may be done on this ACFPI class by:
	 * -1 single TDEG- delete an 'atdiObj.add..' line to cause re-capture of TDEG only
	 * -2 ATDI method- delete the static 'ATDI method' to cause all data to be re-captured
	 *     for (only) that whole ATDI and it's TDEG units
	 * -3 re-capture all- set variable 'resetAllData' to 'true' if a re-dump of ALL
	 *    this class's data or delete the variable-code-line.
	 * -4 specific AddXyz- alter the getResetDataFor method for more specific resets
	 * </pre>
	 */
	/**
	 * <pre>
	 * Apply -3- to the 'resetAllData' variable
	 * WARNING: any reset will cause data removal/LOST [2, 3 are significant]
	 *          and reduced to just the current run execution.
	 * </pre>
	 */
	public static boolean resetAllData = false;

	/** 
	 * <pre>
	 * Get reset-data-for information at runtime for OtdAddXyz sub-classes
	 * to cause reset of exactly matching AddXyz TDEG items.
	 * Activate by altering the string part of:
	 *     'resetHash.put(AddXyz.class,"");'
	 * normal --> '   resetHash.put(AddOS.class, ""); '
	 * alter  --> '   resetHash.put(AddOS.class, "Windows 7"); '
	 *
	 * this example TDEG item will be reset:
	 *      atdiObj.addOS( 9, "Windows 7", 177 );
	 *
	 * @return OTD-framework AddXyz data
	 * </pre>
	 */
	public static HashMap<Class<? extends OtdAddXyz>, String> getResetDataFor(){
		HashMap<Class<? extends OtdAddXyz>, String> resetHash = new HashMap<>();

		resetHash.put(AddHost.class, "");
		resetHash.put(AddOS.class, "");

		return resetHash;
	}

	 /**
	 * ATDI method _v3_initial__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v3_initial__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v3_initial__", "initial", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"false\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 6, JSON_PREFERENCES, "  }"  );
 
		atdiObj.methodDataCount(7);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_globalSetting__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_globalSetting__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_globalSetting__", "globalSetting", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.ByReport.Project\" : \"true\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlighting\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlightingExtended\" : \"true\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Junit\" : \"true\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.JunitExtension\" : \"true\","  );
		atdiObj.add( 6, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Testng\" : \"true\","  );
		atdiObj.add( 7, JSON_PREFERENCES, "    \"JaCoCoverage.Html.AutoOpenReport\" : \"false\","  );
		atdiObj.add( 8, JSON_PREFERENCES, "    \"JaCoCoverage.Html.EnableReport\" : \"true\","  );
		atdiObj.add( 9, JSON_PREFERENCES, "    \"JaCoCoverage.MergeOn\" : \"false\","  );
		atdiObj.add( 10, JSON_PREFERENCES, "    \"JaCoCoverage.NbConsole.EnableReport\" : \"true\","  );
		atdiObj.add( 11, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"true\","  );
		atdiObj.add( 12, JSON_PREFERENCES, "    \"JaCoCoverage.Retain.xml\" : \"false\","  );
		atdiObj.add( 13, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 14, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 15, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 16, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 17, JSON_PREFERENCES, "  }"  );
 
		atdiObj.methodDataCount(18);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_projectSetting__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_projectSetting__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_projectSetting__", "projectSetting", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.ByReport.Project\" : \"true\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlighting\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlightingExtended\" : \"true\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Junit\" : \"true\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.JunitExtension\" : \"true\","  );
		atdiObj.add( 6, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Testng\" : \"true\","  );
		atdiObj.add( 7, JSON_PREFERENCES, "    \"JaCoCoverage.Html.AutoOpenReport\" : \"false\","  );
		atdiObj.add( 8, JSON_PREFERENCES, "    \"JaCoCoverage.Html.EnableReport\" : \"true\","  );
		atdiObj.add( 9, JSON_PREFERENCES, "    \"JaCoCoverage.MergeOn\" : \"false\","  );
		atdiObj.add( 10, JSON_PREFERENCES, "    \"JaCoCoverage.NbConsole.EnableReport\" : \"true\","  );
		atdiObj.add( 11, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"true\","  );
		atdiObj.add( 12, JSON_PREFERENCES, "    \"JaCoCoverage.Retain.xml\" : \"false\","  );
		atdiObj.add( 13, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 14, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 15, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 16, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 17, JSON_PREFERENCES, "  }"  );
 
		atdiObj.methodDataCount(18);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_initialproject__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_initialproject__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_initialproject__", "initialproject", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.ByReport.Project\" : \"true\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlighting\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlightingExtended\" : \"true\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Junit\" : \"true\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.JunitExtension\" : \"true\","  );
		atdiObj.add( 6, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Testng\" : \"true\","  );
		atdiObj.add( 7, JSON_PREFERENCES, "    \"JaCoCoverage.Html.AutoOpenReport\" : \"false\","  );
		atdiObj.add( 8, JSON_PREFERENCES, "    \"JaCoCoverage.Html.EnableReport\" : \"true\","  );
		atdiObj.add( 9, JSON_PREFERENCES, "    \"JaCoCoverage.MergeOn\" : \"false\","  );
		atdiObj.add( 10, JSON_PREFERENCES, "    \"JaCoCoverage.NbConsole.EnableReport\" : \"true\","  );
		atdiObj.add( 11, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"true\","  );
		atdiObj.add( 12, JSON_PREFERENCES, "    \"JaCoCoverage.Retain.xml\" : \"false\","  );
		atdiObj.add( 13, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 14, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 15, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 16, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 17, JSON_PREFERENCES, "  }"  );
 
		atdiObj.methodDataCount(18);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_initalall_AL
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_initalall_AL(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_initalall_AL", "initalall", acfpiObj, EnumSet.of( JSON_ALL) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.ByReport.Project\" : \"true\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlighting\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlightingExtended\" : \"true\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Junit\" : \"true\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.JunitExtension\" : \"true\","  );
		atdiObj.add( 6, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Testng\" : \"true\","  );
		atdiObj.add( 7, JSON_PREFERENCES, "    \"JaCoCoverage.Html.AutoOpenReport\" : \"false\","  );
		atdiObj.add( 8, JSON_PREFERENCES, "    \"JaCoCoverage.Html.EnableReport\" : \"true\","  );
		atdiObj.add( 9, JSON_PREFERENCES, "    \"JaCoCoverage.MergeOn\" : \"false\","  );
		atdiObj.add( 10, JSON_PREFERENCES, "    \"JaCoCoverage.NbConsole.EnableReport\" : \"true\","  );
		atdiObj.add( 11, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"true\","  );
		atdiObj.add( 12, JSON_PREFERENCES, "    \"JaCoCoverage.Retain.xml\" : \"false\","  );
		atdiObj.add( 13, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 14, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 15, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 16, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 17, JSON_PREFERENCES, "  }"  );
		atdiObj.add( 18, JSON_EXCLUDE, "  \"exclpkgs.listing\" : { },"  );
		atdiObj.add( 19, JSON_FILTER, "  \"pkgfltr.listing\" : { },"  );
 
		atdiObj.methodDataCount(20);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_initialpersist__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_initialpersist__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_initialpersist__", "initialpersist", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
		atdiObj.add( 0, JSON_PREFERENCES, "  \"preferences\" : {"  );
		atdiObj.add( 1, JSON_PREFERENCES, "    \"JaCoCoverage.ByReport.Project\" : \"true\","  );
		atdiObj.add( 2, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlighting\" : \"true\","  );
		atdiObj.add( 3, JSON_PREFERENCES, "    \"JaCoCoverage.Editor.EnableCodeHighlightingExtended\" : \"true\","  );
		atdiObj.add( 4, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Junit\" : \"true\","  );
		atdiObj.add( 5, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.JunitExtension\" : \"true\","  );
		atdiObj.add( 6, JSON_PREFERENCES, "    \"JaCoCoverage.Excl.Testng\" : \"true\","  );
		atdiObj.add( 7, JSON_PREFERENCES, "    \"JaCoCoverage.Html.AutoOpenReport\" : \"false\","  );
		atdiObj.add( 8, JSON_PREFERENCES, "    \"JaCoCoverage.Html.EnableReport\" : \"true\","  );
		atdiObj.add( 9, JSON_PREFERENCES, "    \"JaCoCoverage.MergeOn\" : \"false\","  );
		atdiObj.add( 10, JSON_PREFERENCES, "    \"JaCoCoverage.NbConsole.EnableReport\" : \"true\","  );
		atdiObj.add( 11, JSON_PREFERENCES, "    \"JaCoCoverage.Prj.OverrideGlobals\" : \"true\","  );
		atdiObj.add( 12, JSON_PREFERENCES, "    \"JaCoCoverage.Retain.xml\" : \"false\","  );
		atdiObj.add( 13, JSON_PREFERENCES, "    \"JaCoCoverage.report.default.dir\" : \"true\","  );
		atdiObj.add( 14, JSON_PREFERENCES, "    \"JaCoCoverage.report.retain\" : \"3\","  );
		atdiObj.add( 15, JSON_PREFERENCES, "    \"JaCoCoverage.report.timestamp\" : \"false\","  );
		atdiObj.add( 16, JSON_PREFERENCES, "    \"JaCoCoverage.report.user.defined.dir\" : \"./.jacocodbrad/\""  );
		atdiObj.add( 17, JSON_PREFERENCES, "  }"  );
 
		atdiObj.methodDataCount(18);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
