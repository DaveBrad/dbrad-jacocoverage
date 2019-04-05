package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
 
import java.util.EnumSet;
import static packg.appfunc.otdextensions.JsonValuesOtd.JsonDataTypes.JSON_FILTER;
import packg.appfunc.otdextensions.JsonValuesOtd;

/**
 * Class code for an application-class-file-processor-instance (ACFPI) for
 * an object-test-data generated test data class. The static methods within
 * the ACFPI file represent application-test-data-instances (ATDI) which hold
 * test-data-element-group (TDEG) data. (Except 'getResetDataFor' which the 
 * framework uses for reset-data-for capabilities.)
 */
public final class TestHelloWorldMultiSEJsonTD {
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
	 * ATDI method _v0_setAssocTablesOn__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_setAssocTablesOn__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_setAssocTablesOn__", "setAssocTablesOn", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"TestHelloWorldMulti_1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 14, JSON_FILTER, "        }"  );
		atdiObj.add( 15, JSON_FILTER, "      }"  );
		atdiObj.add( 16, JSON_FILTER, "    },"  );
		atdiObj.add( 17, JSON_FILTER, "    \"TestHelloWorldMulti_2\" : {"  );
		atdiObj.add( 18, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 19, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 20, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 21, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 22, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 24, JSON_FILTER, "        },"  );
		atdiObj.add( 25, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 26, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 27, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 28, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 30, JSON_FILTER, "        },"  );
		atdiObj.add( 31, JSON_FILTER, "        \"hello.world\" : {"  );
		atdiObj.add( 32, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 33, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 34, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 35, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 36, JSON_FILTER, "        },"  );
		atdiObj.add( 37, JSON_FILTER, "        \"otherpck\" : {"  );
		atdiObj.add( 38, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 39, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 40, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 41, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 42, JSON_FILTER, "        }"  );
		atdiObj.add( 43, JSON_FILTER, "      }"  );
		atdiObj.add( 44, JSON_FILTER, "    },"  );
		atdiObj.add( 45, JSON_FILTER, "    \"TestHelloWorldMultiple\" : {"  );
		atdiObj.add( 46, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 47, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 48, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 49, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 50, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 51, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 52, JSON_FILTER, "        },"  );
		atdiObj.add( 53, JSON_FILTER, "        \"testhelloworldmultiple\" : {"  );
		atdiObj.add( 54, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 55, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 56, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 57, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 58, JSON_FILTER, "        }"  );
		atdiObj.add( 59, JSON_FILTER, "      },"  );
		atdiObj.add( 60, JSON_FILTER, "      \"Test Packages\" : {"  );
		atdiObj.add( 61, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 62, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 63, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 64, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 65, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 66, JSON_FILTER, "        },"  );
		atdiObj.add( 67, JSON_FILTER, "        \"isolate\" : {"  );
		atdiObj.add( 68, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 69, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 70, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 71, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 72, JSON_FILTER, "        },"  );
		atdiObj.add( 73, JSON_FILTER, "        \"unit\" : {"  );
		atdiObj.add( 74, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 75, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 76, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 77, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 78, JSON_FILTER, "        },"  );
		atdiObj.add( 79, JSON_FILTER, "        \"unit.isolate\" : {"  );
		atdiObj.add( 80, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 81, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 82, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 83, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 84, JSON_FILTER, "        }"  );
		atdiObj.add( 85, JSON_FILTER, "      }"  );
		atdiObj.add( 86, JSON_FILTER, "    }"  );
		atdiObj.add( 87, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(88);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOOnly1On__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_setAssocTablesOOnly1On__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_setAssocTablesOOnly1On__", "setAssocTablesOOnly1On", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"TestHelloWorldMulti_1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 14, JSON_FILTER, "        }"  );
		atdiObj.add( 15, JSON_FILTER, "      }"  );
		atdiObj.add( 16, JSON_FILTER, "    },"  );
		atdiObj.add( 17, JSON_FILTER, "    \"TestHelloWorldMulti_2\" : {"  );
		atdiObj.add( 18, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 19, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 20, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 21, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 22, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 24, JSON_FILTER, "        },"  );
		atdiObj.add( 25, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 26, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 27, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 28, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 30, JSON_FILTER, "        },"  );
		atdiObj.add( 31, JSON_FILTER, "        \"hello.world\" : {"  );
		atdiObj.add( 32, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 33, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 34, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 35, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 36, JSON_FILTER, "        },"  );
		atdiObj.add( 37, JSON_FILTER, "        \"otherpck\" : {"  );
		atdiObj.add( 38, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 39, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 40, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 41, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 42, JSON_FILTER, "        }"  );
		atdiObj.add( 43, JSON_FILTER, "      }"  );
		atdiObj.add( 44, JSON_FILTER, "    },"  );
		atdiObj.add( 45, JSON_FILTER, "    \"TestHelloWorldMultiple\" : {"  );
		atdiObj.add( 46, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 47, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 48, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 49, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 50, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 51, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 52, JSON_FILTER, "        },"  );
		atdiObj.add( 53, JSON_FILTER, "        \"testhelloworldmultiple\" : {"  );
		atdiObj.add( 54, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 55, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 56, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 57, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 58, JSON_FILTER, "        }"  );
		atdiObj.add( 59, JSON_FILTER, "      },"  );
		atdiObj.add( 60, JSON_FILTER, "      \"Test Packages\" : {"  );
		atdiObj.add( 61, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 62, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 63, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 64, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 65, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 66, JSON_FILTER, "        },"  );
		atdiObj.add( 67, JSON_FILTER, "        \"isolate\" : {"  );
		atdiObj.add( 68, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 69, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 70, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 71, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 72, JSON_FILTER, "        },"  );
		atdiObj.add( 73, JSON_FILTER, "        \"unit\" : {"  );
		atdiObj.add( 74, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 75, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 76, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 77, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 78, JSON_FILTER, "        },"  );
		atdiObj.add( 79, JSON_FILTER, "        \"unit.isolate\" : {"  );
		atdiObj.add( 80, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 81, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 82, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 83, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 84, JSON_FILTER, "        }"  );
		atdiObj.add( 85, JSON_FILTER, "      }"  );
		atdiObj.add( 86, JSON_FILTER, "    }"  );
		atdiObj.add( 87, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(88);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTables1AssociateProjOnly__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_setAssocTables1AssociateProjOnly__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_setAssocTables1AssociateProjOnly__", "setAssocTables1AssociateProjOnly", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"TestHelloWorldMulti_1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 14, JSON_FILTER, "        }"  );
		atdiObj.add( 15, JSON_FILTER, "      }"  );
		atdiObj.add( 16, JSON_FILTER, "    },"  );
		atdiObj.add( 17, JSON_FILTER, "    \"TestHelloWorldMulti_2\" : {"  );
		atdiObj.add( 18, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 19, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 20, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 21, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 22, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 24, JSON_FILTER, "        },"  );
		atdiObj.add( 25, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 26, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 27, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 28, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 30, JSON_FILTER, "        },"  );
		atdiObj.add( 31, JSON_FILTER, "        \"hello.world\" : {"  );
		atdiObj.add( 32, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 33, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 34, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 35, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 36, JSON_FILTER, "        },"  );
		atdiObj.add( 37, JSON_FILTER, "        \"otherpck\" : {"  );
		atdiObj.add( 38, JSON_FILTER, "          \"on\" : true,"  );
		atdiObj.add( 39, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 40, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 41, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 42, JSON_FILTER, "        }"  );
		atdiObj.add( 43, JSON_FILTER, "      }"  );
		atdiObj.add( 44, JSON_FILTER, "    },"  );
		atdiObj.add( 45, JSON_FILTER, "    \"TestHelloWorldMultiple\" : {"  );
		atdiObj.add( 46, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 47, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 48, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 49, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 50, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 51, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 52, JSON_FILTER, "        },"  );
		atdiObj.add( 53, JSON_FILTER, "        \"testhelloworldmultiple\" : {"  );
		atdiObj.add( 54, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 55, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 56, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 57, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 58, JSON_FILTER, "        }"  );
		atdiObj.add( 59, JSON_FILTER, "      },"  );
		atdiObj.add( 60, JSON_FILTER, "      \"Test Packages\" : {"  );
		atdiObj.add( 61, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 62, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 63, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 64, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 65, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 66, JSON_FILTER, "        },"  );
		atdiObj.add( 67, JSON_FILTER, "        \"isolate\" : {"  );
		atdiObj.add( 68, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 69, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 70, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 71, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 72, JSON_FILTER, "        },"  );
		atdiObj.add( 73, JSON_FILTER, "        \"unit\" : {"  );
		atdiObj.add( 74, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 75, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 76, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 77, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 78, JSON_FILTER, "        },"  );
		atdiObj.add( 79, JSON_FILTER, "        \"unit.isolate\" : {"  );
		atdiObj.add( 80, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 81, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 82, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 83, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 84, JSON_FILTER, "        }"  );
		atdiObj.add( 85, JSON_FILTER, "      }"  );
		atdiObj.add( 86, JSON_FILTER, "    }"  );
		atdiObj.add( 87, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(88);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesNothingSet__
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static JsonValuesOtd _v0_setAssocTablesNothingSet__(JsonValuesOtd acfpiObj){
 
		JsonValuesOtd atdiObj = new JsonValuesOtd();
		atdiObj.methodData( "_v0_setAssocTablesNothingSet__", "setAssocTablesNothingSet", acfpiObj, EnumSet.of( JSON_FILTER) );
		atdiObj.add( 0, JSON_FILTER, "  \"pkgfltr.listing\" : {"  );
		atdiObj.add( 1, JSON_FILTER, "    \"TestHelloWorldMulti_1\" : {"  );
		atdiObj.add( 2, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 3, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 4, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 5, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 6, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 7, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 8, JSON_FILTER, "        },"  );
		atdiObj.add( 9, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 10, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 11, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 12, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 13, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 14, JSON_FILTER, "        }"  );
		atdiObj.add( 15, JSON_FILTER, "      }"  );
		atdiObj.add( 16, JSON_FILTER, "    },"  );
		atdiObj.add( 17, JSON_FILTER, "    \"TestHelloWorldMulti_2\" : {"  );
		atdiObj.add( 18, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 19, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 20, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 21, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 22, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 23, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 24, JSON_FILTER, "        },"  );
		atdiObj.add( 25, JSON_FILTER, "        \"hello\" : {"  );
		atdiObj.add( 26, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 27, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 28, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 29, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 30, JSON_FILTER, "        },"  );
		atdiObj.add( 31, JSON_FILTER, "        \"hello.world\" : {"  );
		atdiObj.add( 32, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 33, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 34, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 35, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 36, JSON_FILTER, "        },"  );
		atdiObj.add( 37, JSON_FILTER, "        \"otherpck\" : {"  );
		atdiObj.add( 38, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 39, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 40, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 41, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 42, JSON_FILTER, "        }"  );
		atdiObj.add( 43, JSON_FILTER, "      }"  );
		atdiObj.add( 44, JSON_FILTER, "    },"  );
		atdiObj.add( 45, JSON_FILTER, "    \"TestHelloWorldMultiple\" : {"  );
		atdiObj.add( 46, JSON_FILTER, "      \"Source Packages\" : {"  );
		atdiObj.add( 47, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 48, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 49, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 50, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 51, JSON_FILTER, "          \"pfCvr\" : \"COVER_NO\""  );
		atdiObj.add( 52, JSON_FILTER, "        },"  );
		atdiObj.add( 53, JSON_FILTER, "        \"testhelloworldmultiple\" : {"  );
		atdiObj.add( 54, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 55, JSON_FILTER, "          \"isTst\" : false,"  );
		atdiObj.add( 56, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 57, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 58, JSON_FILTER, "        }"  );
		atdiObj.add( 59, JSON_FILTER, "      },"  );
		atdiObj.add( 60, JSON_FILTER, "      \"Test Packages\" : {"  );
		atdiObj.add( 61, JSON_FILTER, "        \"\" : {"  );
		atdiObj.add( 62, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 63, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 64, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 65, JSON_FILTER, "          \"pfCvr\" : \"COVER_YES\""  );
		atdiObj.add( 66, JSON_FILTER, "        },"  );
		atdiObj.add( 67, JSON_FILTER, "        \"isolate\" : {"  );
		atdiObj.add( 68, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 69, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 70, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 71, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 72, JSON_FILTER, "        },"  );
		atdiObj.add( 73, JSON_FILTER, "        \"unit\" : {"  );
		atdiObj.add( 74, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 75, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 76, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 77, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 78, JSON_FILTER, "        },"  );
		atdiObj.add( 79, JSON_FILTER, "        \"unit.isolate\" : {"  );
		atdiObj.add( 80, JSON_FILTER, "          \"on\" : false,"  );
		atdiObj.add( 81, JSON_FILTER, "          \"isTst\" : true,"  );
		atdiObj.add( 82, JSON_FILTER, "          \"hasJava\" : true,"  );
		atdiObj.add( 83, JSON_FILTER, "          \"pfCvr\" : \"COVER_BY_PARENT\""  );
		atdiObj.add( 84, JSON_FILTER, "        }"  );
		atdiObj.add( 85, JSON_FILTER, "      }"  );
		atdiObj.add( 86, JSON_FILTER, "    }"  );
		atdiObj.add( 87, JSON_FILTER, "  },"  );
 
		atdiObj.methodDataCount(88);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
