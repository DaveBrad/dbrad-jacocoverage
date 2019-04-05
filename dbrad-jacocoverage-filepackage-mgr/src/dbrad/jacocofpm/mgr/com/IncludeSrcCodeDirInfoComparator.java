/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.util.Comparator;

/**
 *
 * @author dbradley
 */
public class IncludeSrcCodeDirInfoComparator implements Comparator<IncludeSrcCodeDirInfo>{

    @Override
    public int compare(IncludeSrcCodeDirInfo o1, IncludeSrcCodeDirInfo o2) {
        // comparator is only on the string parts
        
        return o1.packageFormat.compareTo(o2.packageFormat);
    }
    
}
