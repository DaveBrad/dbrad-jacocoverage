package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
 
import java.util.EnumSet;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_ALL;
import packg.appfunc.otdextensions.JsonValuesOtd;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_PREFERENCES;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_EXCLUDE;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;

/**
 * Class code for an application-class-file-processor-instance (ACFPI) for
 * an object-test-data generated test data class. The static methods within
 * the ACFPI file represent application-test-data-instances (ATDI) which hold
 * test-data-element-group (TDEG) data. (Except 'getResetDataFor' which the 
 * framework uses for reset-data-for capabilities.)
 */
public final class AssociateSEJsonTD {
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
	 * ATDI method _v0_associateInitial_AL
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_associateInitial_AL(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_associateInitial_AL", "associateInitial", acfpiObj, EnumSet.of( JSON_ALL) );
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
	 * ATDI method _v1_assocproj_AL
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v1_assocproj_AL(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v1_assocproj_AL", "assocproj", acfpiObj, EnumSet.of( JSON_ALL) );
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
	 * ATDI method _v2_saveOffTheProject_AL
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v2_saveOffTheProject_AL(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v2_saveOffTheProject_AL", "saveOffTheProject", acfpiObj, EnumSet.of( JSON_ALL) );
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
		atdiObj.add( 19, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 20, JSON_FILTER, "    \"JavaTestModelWithDependents\" : {"  );
		atdiObj.add( 21, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 22, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 23, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 24, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 25, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 26, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 27, JSON_FILTER, "        },"  );
		atdiObj.add( 28, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 29, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 30, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 31, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 32, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 33, JSON_FILTER, "        },"  );
		atdiObj.add( 34, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 35, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 36, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 37, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 38, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 39, JSON_FILTER, "        },"  );
		atdiObj.add( 40, JSON_FILTER, "        \"org.jlib.model1\" : {"  );
		atdiObj.add( 41, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 42, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 43, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 44, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 45, JSON_FILTER, "        },"  );
		atdiObj.add( 46, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 47, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 48, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 49, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 50, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 51, JSON_FILTER, "        }"  );
		atdiObj.add( 52, JSON_FILTER, "      }"  );
		atdiObj.add( 53, JSON_FILTER, "    },"  );
		atdiObj.add( 54, JSON_FILTER, "    \"JavaTestModelWithDependents_1\" : {"  );
		atdiObj.add( 55, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 56, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 57, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 58, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 59, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 60, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 61, JSON_FILTER, "        },"  );
		atdiObj.add( 62, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 63, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 64, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 65, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 66, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 67, JSON_FILTER, "        },"  );
		atdiObj.add( 68, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 69, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 70, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 71, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 72, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 73, JSON_FILTER, "        },"  );
		atdiObj.add( 74, JSON_FILTER, "        \"org.jlib.model_1\" : {"  );
		atdiObj.add( 75, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 76, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 77, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 78, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 79, JSON_FILTER, "        },"  );
		atdiObj.add( 80, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 81, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 82, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 83, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 84, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 85, JSON_FILTER, "        }"  );
		atdiObj.add( 86, JSON_FILTER, "      }"  );
		atdiObj.add( 87, JSON_FILTER, "    },"  );
		atdiObj.add( 88, JSON_FILTER, "    \"JavaTestModelWithDependents_2\" : {"  );
		atdiObj.add( 89, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 90, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 91, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 92, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 93, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 94, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 95, JSON_FILTER, "        },"  );
		atdiObj.add( 96, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 97, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 98, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 99, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 100, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 101, JSON_FILTER, "        },"  );
		atdiObj.add( 102, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 103, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 104, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 105, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 106, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 107, JSON_FILTER, "        },"  );
		atdiObj.add( 108, JSON_FILTER, "        \"org.jlib.model_2\" : {"  );
		atdiObj.add( 109, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 110, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 111, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 112, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 113, JSON_FILTER, "        },"  );
		atdiObj.add( 114, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 115, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 116, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 117, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 118, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 119, JSON_FILTER, "        }"  );
		atdiObj.add( 120, JSON_FILTER, "      }"  );
		atdiObj.add( 121, JSON_FILTER, "    },"  );
		atdiObj.add( 122, JSON_FILTER, "    \"JavaTestModelWithDependents_3\" : {"  );
		atdiObj.add( 123, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 124, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 125, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 126, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 127, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 128, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 129, JSON_FILTER, "        },"  );
		atdiObj.add( 130, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 131, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 132, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 133, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 134, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 135, JSON_FILTER, "        },"  );
		atdiObj.add( 136, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 137, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 138, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 139, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 140, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 141, JSON_FILTER, "        },"  );
		atdiObj.add( 142, JSON_FILTER, "        \"org.jlib.model_3\" : {"  );
		atdiObj.add( 143, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 144, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 145, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 146, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 147, JSON_FILTER, "        },"  );
		atdiObj.add( 148, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 149, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 150, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 151, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 152, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 153, JSON_FILTER, "        }"  );
		atdiObj.add( 154, JSON_FILTER, "      }"  );
		atdiObj.add( 155, JSON_FILTER, "    },"  );
		atdiObj.add( 156, JSON_FILTER, "    \"JavaTestModelWithDependents_4\" : {"  );
		atdiObj.add( 157, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 158, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 159, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 160, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 161, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 162, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 163, JSON_FILTER, "        },"  );
		atdiObj.add( 164, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 165, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 166, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 167, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 168, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 169, JSON_FILTER, "        },"  );
		atdiObj.add( 170, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 171, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 172, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 173, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 174, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 175, JSON_FILTER, "        },"  );
		atdiObj.add( 176, JSON_FILTER, "        \"org.jlib.model_4\" : {"  );
		atdiObj.add( 177, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 178, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 179, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 180, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 181, JSON_FILTER, "        },"  );
		atdiObj.add( 182, JSON_FILTER, "        \"org.jlib.pkgb\" : {"  );
		atdiObj.add( 183, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 184, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 185, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 186, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 187, JSON_FILTER, "        }"  );
		atdiObj.add( 188, JSON_FILTER, "      }"  );
		atdiObj.add( 189, JSON_FILTER, "    },"  );
		atdiObj.add( 190, JSON_FILTER, "    \"JavaTestModelWithDependents_5\" : {"  );
		atdiObj.add( 191, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 192, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 193, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 194, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 195, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 196, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 197, JSON_FILTER, "        },"  );
		atdiObj.add( 198, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 199, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 200, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 201, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 202, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 203, JSON_FILTER, "        },"  );
		atdiObj.add( 204, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 205, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 206, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 207, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 208, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 209, JSON_FILTER, "        },"  );
		atdiObj.add( 210, JSON_FILTER, "        \"org.jlib.model_5\" : {"  );
		atdiObj.add( 211, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 212, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 213, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 214, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 215, JSON_FILTER, "        },"  );
		atdiObj.add( 216, JSON_FILTER, "        \"org.jlib.pkgb\" : {"  );
		atdiObj.add( 217, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 218, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 219, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 220, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 221, JSON_FILTER, "        }"  );
		atdiObj.add( 222, JSON_FILTER, "      }"  );
		atdiObj.add( 223, JSON_FILTER, "    },"  );
		atdiObj.add( 224, JSON_FILTER, "    \"JavaTestModelWithDependents_6\" : {"  );
		atdiObj.add( 225, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 226, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 227, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 228, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 229, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 230, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 231, JSON_FILTER, "        },"  );
		atdiObj.add( 232, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 233, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 234, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 235, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 236, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 237, JSON_FILTER, "        },"  );
		atdiObj.add( 238, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 239, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 240, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 241, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 242, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 243, JSON_FILTER, "        },"  );
		atdiObj.add( 244, JSON_FILTER, "        \"org.jlib.model_6\" : {"  );
		atdiObj.add( 245, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 246, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 247, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 248, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 249, JSON_FILTER, "        },"  );
		atdiObj.add( 250, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 251, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 252, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 253, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 254, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 255, JSON_FILTER, "        }"  );
		atdiObj.add( 256, JSON_FILTER, "      }"  );
		atdiObj.add( 257, JSON_FILTER, "    },"  );
		atdiObj.add( 258, JSON_FILTER, "    \"JavaTestModelWithDependents_7\" : {"  );
		atdiObj.add( 259, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 260, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 261, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 262, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 263, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 264, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 265, JSON_FILTER, "        },"  );
		atdiObj.add( 266, JSON_FILTER, "        \"org\" : {"  );
		atdiObj.add( 267, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 268, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 269, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 270, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 271, JSON_FILTER, "        },"  );
		atdiObj.add( 272, JSON_FILTER, "        \"org.jlib\" : {"  );
		atdiObj.add( 273, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 274, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 275, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 276, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 277, JSON_FILTER, "        },"  );
		atdiObj.add( 278, JSON_FILTER, "        \"org.jlib.model_7\" : {"  );
		atdiObj.add( 279, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 280, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 281, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 282, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 283, JSON_FILTER, "        },"  );
		atdiObj.add( 284, JSON_FILTER, "        \"org.jlib.pkga\" : {"  );
		atdiObj.add( 285, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 286, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 287, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 288, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 289, JSON_FILTER, "        }"  );
		atdiObj.add( 290, JSON_FILTER, "      }"  );
		atdiObj.add( 291, JSON_FILTER, "    }"  );
		atdiObj.add( 292, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(293);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
