package packg.testdataclasses;
import java.util.HashMap;
import org.jtestdb.objecttestdata.OtdAddXyz;
import org.jtestdb.objecttestdata.AddOS;
import org.jtestdb.objecttestdata.AddHost;
import packg.appfunc.otdextensions.TableOtd;
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
public final class PoorPackagesSETD {
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
	 * ATDI method _v0_validatePkgFilterTable_TestModelPoorPackaging
	 * @param acfpiObj the parent OTD object
	 * @return an ATDI OTD object
	 */
	public static TableOtd _v0_validatePkgFilterTable_TestModelPoorPackaging(TableOtd acfpiObj){
 
		TableOtd atdiObj = new TableOtd();
 
		atdiObj.methodData( "_v0_validatePkgFilterTable_TestModelPoorPackaging", "TestModelPoorPackaging", acfpiObj );
		atdiObj.add( 0, true, true, COVER_YES, false, "Source Packages", "", AFFECTING_OTHERS  );
		atdiObj.add( 1, true, true, COVER_BY_PARENT, false, "Source Packages", "package1", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 2, true, true, COVER_BY_PARENT, false, "Source Packages", "package1.inner1", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 3, true, true, COVER_BY_PARENT, false, "Source Packages", "package1_1", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 4, true, true, COVER_BY_PARENT, false, "Source Packages", "package1_1.inner1", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 5, true, true, COVER_BY_PARENT, false, "Source Packages", "package2", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 6, true, true, COVER_BY_PARENT, false, "Source Packages", "package2.inner2", AFFECTED_BY_A_SIBLING  );
		atdiObj.add( 7, "*"  );
 
		atdiObj.methodDataCount(8);
 
		return atdiObj;
	}

 
} // end class, do not edit or change this line of code
