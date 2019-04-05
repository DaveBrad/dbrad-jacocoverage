/*
 * Copyright (c) 2017 dbradley.
 * Sub-licensed 'dbrad_license.html'.
 *
 * Copyright (c) 2016 Jonathan Lermitage.
 * Contains original code from tikione-coverage authors under 'MIT_tikione-license.html'.

 * (see in: dbrad/jacocoverage/_license).
 *
 */
package dbrad.jacocofpm.util;

/**
 * NetBeans project types supported.
 *
 * @author Jonathan Lermitage
 * @author dbradley
 */
public enum NBProjectTypeEnum {

    /** One of the project types supported by dbrad-jacocoverage */
    J2SE,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    NBMODULE,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    NBMODULE_SUITE,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    J2EE_WEB,
    /** One of the
     * project types supported by dbrad-jacocoverage */
    J2EE,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    J2EE_EAR,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    J2EE_EJB,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    MAVEN,
    /**
     * One of the project types supported by dbrad-jacocoverage */
    MAVEN__ALL;

    /**
     * query name for the type of project supported which is an
     * org.netbeans.yxyxyxyxyx package setting.
     *
     * @return a string representing a NB project type package setting
     */
    public String qname() {
        String qname;
        switch (this) {
            case J2SE:
                qname = "org.netbeans.modules.java.j2seproject.J2SEProject";
                break;
            case NBMODULE:
                qname = "org.netbeans.modules.apisupport.project.NbModuleProject";
                break;
            case NBMODULE_SUITE:
                qname = "org.netbeans.modules.apisupport.project.suite.SuiteProject";
                break;
            case J2EE:
                qname = "org-netbeans-modules-j2ee-archiveproject";
                break;
            case J2EE_EAR:
                qname = "org-netbeans-modules-j2ee-earproject";
                break;
            case J2EE_EJB:
                qname = "org-netbeans-modules-j2ee-ejbjarproject";
                break;
            case J2EE_WEB:
                qname = "org.netbeans.modules.web.project.WebProject";
                break;
            case MAVEN:
                qname = "org.netbeans.modules.maven.NbMavenProjectImpl";
                break;
            case MAVEN__ALL:
                qname = "org.netbeans.modules.maven";
                break;
            default:
                qname = "n/a";
        }
        return qname;
    }

    /**
     * If true, check the entire project definition qname, otherwise check if
     * the project definition qname starts with the given project type.
     *
     * @return strict level.
     */
    public boolean isStrict() {
        boolean isStrict;
        switch (this) {
            case J2SE:
            case NBMODULE:
            case J2EE:
            case J2EE_EAR:
            case J2EE_EJB:
            case J2EE_WEB:
            case MAVEN:
                isStrict = true;
                break;
            case MAVEN__ALL:
                isStrict = false;
                break;
            default:
                isStrict = true;
        }
        return isStrict;
    }
}
