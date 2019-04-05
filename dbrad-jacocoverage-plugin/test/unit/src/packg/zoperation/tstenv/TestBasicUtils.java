/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.zoperation.tstenv;

import java.io.PrintStream;
import org.openide.util.Exceptions;

/**
 * Class for some basic static methods.
 *
 * @author dbradley
 */
public class TestBasicUtils {

    /**
     * Print a dbrad-jacoco testing message for progress information.
     *
     * @param prtStream the stream System.out or System.err
     * @param message   the message to print (needs to include any new-lines)
     */
    public static void printTestingMsg(PrintStream prtStream, String message) {
        prtStream.printf("# dbrad-jacoco test ## %s", message);
    }

    /**
     * Print a dbrad-jacoco testing message for progress information using
     * a provided format-string and array of objects as format argument objects.
     *
     * @param prtStream the stream System.out or System.err
     * @param formatString  the message to be formatted into
     * @param objArr list of arguments for the format-string
     */
    public static void printTestingMsg(PrintStream prtStream, String formatString, Object... objArr) {

        formatString = String.format("# dbrad-jacoco test ## %s", formatString);
        prtStream.printf(formatString, objArr);
    }

    /**
     * Pause the execution of this thread for a period of time.
     *
     * @param milliseconds integer value of milliseconds
     */
    public static void pauseMs(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
