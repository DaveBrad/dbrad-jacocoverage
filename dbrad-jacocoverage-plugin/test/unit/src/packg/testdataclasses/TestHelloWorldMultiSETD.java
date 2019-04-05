package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
import packg.appfunc.otdextensions.TableOtd;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_NO;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.NOT_AFFECTED;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_YES;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTING_OTHERS;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_BY_PARENT;
import static dbrad.jacocofpm.mgr.com.PackageAffectingState.AFFECTED_BY_A_SIBLING;

/**
 * Class code for an application-class-file-processor-instance (ACFPI) for
 * an object-test-data generated test data class. The static methods within
 * the ACFPI file represent application-test-data-instances (ATDI) which hold
 * test-data-element-group (TDEG) data. (Except 'getResetDataFor' which the 
 * framework uses for reset-data-for capabilities.)
 */
public final class TestHelloWorldMultiSETD {
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
	 * ATDI method _v0_associatedProjectEnabled_TestHelloWorldMultiple
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_associatedProjectEnabled_TestHelloWorldMultiple(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_associatedProjectEnabled_TestHelloWorldMultiple", "TestHelloWorldMultiple", acfpiObj );
		atdiObj.add( 0, true, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, true, false, COVER_YES, false, "Source Packages", "testhelloworldmultiple", NOT_AFFECTED  );
		atdiObj.add( 2, true, true, COVER_YES, true, "Test Packages", "", AFFECTING_OTHERS  );
		atdiObj.add( 3, true, true, COVER_BY_PARENT, true, "Test Packages", "isolate", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 4, true, true, COVER_BY_PARENT, true, "Test Packages", "unit", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 5, "*"  );
 
		atdiObj.methodDataCount(6);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_associatedProjectEnabled_TestHelloWorldMulti_1
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_associatedProjectEnabled_TestHelloWorldMulti_1(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_associatedProjectEnabled_TestHelloWorldMulti_1", "TestHelloWorldMulti_1", acfpiObj );
		atdiObj.add( 0, false, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_associatedProjectEnabled_TestHelloWorldMulti_2
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_associatedProjectEnabled_TestHelloWorldMulti_2(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_associatedProjectEnabled_TestHelloWorldMulti_2", "TestHelloWorldMulti_2", acfpiObj );
		atdiObj.add( 0, false, false, COVER_YES, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOn_TestHelloWorldMultiple
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOn_TestHelloWorldMultiple(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOn_TestHelloWorldMultiple", "TestHelloWorldMultiple", acfpiObj );
		atdiObj.add( 0, true, true, COVER_NO, false, "Source Packages", "", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 1, true, true, COVER_YES, false, "Source Packages", "testhelloworldmultiple", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, true, false, COVER_YES, true, "Test Packages", "", NOT_AFFECTED  );
		atdiObj.add( 3, "*"  );
 
		atdiObj.methodDataCount(4);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOn_TestHelloWorldMulti_1
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOn_TestHelloWorldMulti_1(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOn_TestHelloWorldMulti_1", "TestHelloWorldMulti_1", acfpiObj );
		atdiObj.add( 0, false, true, COVER_NO, false, "Source Packages", "", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 1, false, true, COVER_YES, false, "Source Packages", "hello", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, "*"  );
 
		atdiObj.methodDataCount(3);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOn_TestHelloWorldMulti_2
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOn_TestHelloWorldMulti_2(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOn_TestHelloWorldMulti_2", "TestHelloWorldMulti_2", acfpiObj );
		atdiObj.add( 0, false, true, COVER_YES, false, "Source Packages", "", AFFECTING_OTHERS  );
		atdiObj.add( 1, false, true, COVER_BY_PARENT, false, "Source Packages", "hello", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, false, true, COVER_BY_PARENT, false, "Source Packages", "hello.world", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 3, false, true, COVER_BY_PARENT, false, "Source Packages", "otherpck", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 4, "*"  );
 
		atdiObj.methodDataCount(5);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOOnly1On_TestHelloWorldMultiple
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOOnly1On_TestHelloWorldMultiple(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOOnly1On_TestHelloWorldMultiple", "TestHelloWorldMultiple", acfpiObj );
		atdiObj.add( 0, true, true, COVER_NO, false, "Source Packages", "", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 1, true, true, COVER_YES, false, "Source Packages", "testhelloworldmultiple", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, true, false, COVER_YES, true, "Test Packages", "", NOT_AFFECTED  );
		atdiObj.add( 3, "*"  );
 
		atdiObj.methodDataCount(4);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOOnly1On_TestHelloWorldMulti_1
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOOnly1On_TestHelloWorldMulti_1(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOOnly1On_TestHelloWorldMulti_1", "TestHelloWorldMulti_1", acfpiObj );
		atdiObj.add( 0, false, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesOOnly1On_TestHelloWorldMulti_2
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesOOnly1On_TestHelloWorldMulti_2(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesOOnly1On_TestHelloWorldMulti_2", "TestHelloWorldMulti_2", acfpiObj );
		atdiObj.add( 0, false, true, COVER_YES, false, "Source Packages", "", AFFECTING_OTHERS  );
		atdiObj.add( 1, false, true, COVER_BY_PARENT, false, "Source Packages", "hello", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, false, true, COVER_BY_PARENT, false, "Source Packages", "hello.world", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 3, false, true, COVER_BY_PARENT, false, "Source Packages", "otherpck", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 4, "*"  );
 
		atdiObj.methodDataCount(5);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMultiple
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMultiple(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTables1AssociateProjOnly_TestHelloWorldMultiple", "TestHelloWorldMultiple", acfpiObj );
		atdiObj.add( 0, true, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, true, false, COVER_YES, false, "Source Packages", "testhelloworldmultiple", NOT_AFFECTED  );
		atdiObj.add( 2, "*"  );
 
		atdiObj.methodDataCount(3);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_1
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_1(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_1", "TestHelloWorldMulti_1", acfpiObj );
		atdiObj.add( 0, false, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_2
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_2(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTables1AssociateProjOnly_TestHelloWorldMulti_2", "TestHelloWorldMulti_2", acfpiObj );
		atdiObj.add( 0, false, true, COVER_YES, false, "Source Packages", "", AFFECTING_OTHERS  );
		atdiObj.add( 1, false, true, COVER_BY_PARENT, false, "Source Packages", "hello", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, false, true, COVER_BY_PARENT, false, "Source Packages", "hello.world", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 3, false, true, COVER_BY_PARENT, false, "Source Packages", "otherpck", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 4, "*"  );
 
		atdiObj.methodDataCount(5);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesNothingSet_TestHelloWorldMultiple
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesNothingSet_TestHelloWorldMultiple(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesNothingSet_TestHelloWorldMultiple", "TestHelloWorldMultiple", acfpiObj );
		atdiObj.add( 0, true, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, true, false, COVER_YES, false, "Source Packages", "testhelloworldmultiple", NOT_AFFECTED  );
		atdiObj.add( 2, "*"  );
 
		atdiObj.methodDataCount(3);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesNothingSet_TestHelloWorldMulti_1
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesNothingSet_TestHelloWorldMulti_1(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesNothingSet_TestHelloWorldMulti_1", "TestHelloWorldMulti_1", acfpiObj );
		atdiObj.add( 0, false, false, COVER_NO, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

	 /**
	 * ATDI method _v0_setAssocTablesNothingSet_TestHelloWorldMulti_2
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_setAssocTablesNothingSet_TestHelloWorldMulti_2(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_setAssocTablesNothingSet_TestHelloWorldMulti_2", "TestHelloWorldMulti_2", acfpiObj );
		atdiObj.add( 0, false, false, COVER_YES, false, "Source Packages", "", NOT_AFFECTED  );
		atdiObj.add( 1, "*"  );
 
		atdiObj.methodDataCount(2);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
