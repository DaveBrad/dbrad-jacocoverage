package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
 
import java.util.EnumSet;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;
import packg.appfunc.otdextensions.JsonValuesOtd;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;

/**
 * Class code for an application-class-file-processor-instance (ACFPI) for
 * an object-test-data generated test data class. The static methods within
 * the ACFPI file represent application-test-data-instances (ATDI) which hold
 * test-data-element-group (TDEG) data. (Except 'getResetDataFor' which the 
 * framework uses for reset-data-for capabilities.)
 */
public final class NewPackageInclusionSEJsonTD {
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
	 * ATDI method _v0_initial__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_initial__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_initial__", "initial", acfpiObj, EnumSet.of( JSON_PREFERENCES) );
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
	 * ATDI method _v1_saveTheDataToJson__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v1_saveTheDataToJson__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v1_saveTheDataToJson__", "saveTheDataToJson", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"JavaLibraryTestModel1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 14, JSON_FILTER, "        },"  );
		atdiObj.add( 15, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 16, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 17, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 18, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 19, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 20, JSON_FILTER, "        },"  );
		atdiObj.add( 21, JSON_FILTER, "        \"org.jlib.model1\" : {"  );
		atdiObj.add( 22, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 24, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 25, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 26, JSON_FILTER, "        },"  );
		atdiObj.add( 27, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 28, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 30, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 31, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 32, JSON_FILTER, "        }"  );
		atdiObj.add( 33, JSON_FILTER, "      }"  );
		atdiObj.add( 34, JSON_FILTER, "    }"  );
		atdiObj.add( 35, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(36);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_addAPackageAndClassJson__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_addAPackageAndClassJson__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_addAPackageAndClassJson__", "addAPackageAndClassJson", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"JavaLibraryTestModel1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 14, JSON_FILTER, "        },"  );
		atdiObj.add( 15, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 16, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 17, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 18, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 19, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 20, JSON_FILTER, "        },"  );
		atdiObj.add( 21, JSON_FILTER, "        \"org.jlib.additionalpackage\" : {"  );
		atdiObj.add( 22, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 24, JSON_FILTER, "          \"hasJava\" : false,"  );
		atdiObj.add( 25, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 26, JSON_FILTER, "        },"  );
		atdiObj.add( 27, JSON_FILTER, "        \"org.jlib.model1\" : {"  );
		atdiObj.add( 28, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 30, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 31, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 32, JSON_FILTER, "        },"  );
		atdiObj.add( 33, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 34, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 35, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 36, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 37, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 38, JSON_FILTER, "        }"  );
		atdiObj.add( 39, JSON_FILTER, "      }"  );
		atdiObj.add( 40, JSON_FILTER, "    }"  );
		atdiObj.add( 41, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(42);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0z_removeAPackageAndClassJson__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0z_removeAPackageAndClassJson__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0z_removeAPackageAndClassJson__", "removeAPackageAndClassJson", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"JavaLibraryTestModel1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 14, JSON_FILTER, "        },"  );
		atdiObj.add( 15, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 16, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 17, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 18, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 19, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 20, JSON_FILTER, "        },"  );
		atdiObj.add( 21, JSON_FILTER, "        \"org.jlib.model1\" : {"  );
		atdiObj.add( 22, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 24, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 25, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 26, JSON_FILTER, "        },"  );
		atdiObj.add( 27, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 28, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 30, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 31, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 32, JSON_FILTER, "        }"  );
		atdiObj.add( 33, JSON_FILTER, "      }"  );
		atdiObj.add( 34, JSON_FILTER, "    }"  );
		atdiObj.add( 35, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(36);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_removeAPackageAndClassJson__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_removeAPackageAndClassJson__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_removeAPackageAndClassJson__", "removeAPackageAndClassJson", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"JavaLibraryTestModel1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 14, JSON_FILTER, "        },"  );
		atdiObj.add( 15, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 16, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 17, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 18, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 19, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 20, JSON_FILTER, "        },"  );
		atdiObj.add( 21, JSON_FILTER, "        \"org.jlib.model1\" : {"  );
		atdiObj.add( 22, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 24, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 25, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 26, JSON_FILTER, "        },"  );
		atdiObj.add( 27, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 28, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 30, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 31, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 32, JSON_FILTER, "        }"  );
		atdiObj.add( 33, JSON_FILTER, "      }"  );
		atdiObj.add( 34, JSON_FILTER, "    }"  );
		atdiObj.add( 35, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(36);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
