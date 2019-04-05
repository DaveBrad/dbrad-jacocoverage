Information

dbrad-jacocoverage is a Netbeans 8.2 module for code coverage using JaCoCo and is a significant upgrade from a fork of https://github.com/jonathanlermitage/tikione-jacocoverage (which project was concluded May 12th, 2016).

[dbrad-jacocoverage basic documentation](https://DaveBrad.github.io/prjdoc/db-jacoco/doc/intro.html)

The NBM files are contained in NBM-files folder, they work for NB 8.2 and seems to work for NB 10 (but as aplha).

Due to the transfer to Apache of Netbeans and its incubation, the dbrad-jacocoverage project was placed on hold. The code
can be downloaded as a NB project and built and tested on Netbeans 8.2. However, Apache Netbeans 10 the code and be built, but the 
testing testcases will not run due to missing support in NB 10 (as yet).

Prelimary document can be viewed at dbrad-jacocoverage-plugin dbrad.jacocoverage._licenseanddocs intro.html. (Download just the
_licenseanddocs folder.)

--------------------------------------------------------------------------------------------------
About dbrad-jacocoverage

The different name allows use of dbrad-jacocoverage and tikione-jacoco as separate modules. Thus issues are independent 
and should prevent confusion for users, while providing a choice of a code coverage tool in Netbeans.

dbrad-jacocoverage came about due to development of an application and needing an ability to get code coverage 
from testing over multiple associated projects in a Netbeans IDE. Also needed better control of includes and 
excludes arguments that JaCoCo agent supports.

Capabilities

Projects
* base and associated projects are gathered for code coverage 
  * (associate projects are sub-projects or compile/runtime dependent projects in a Netbeans project properties)

Reports
* will be placed in a dbradcoverage directory
  * (different from tikione-jacocoverage)
* may be placed into time-stamped directories for independent ongoing progress analysis (e.g. quality metrics)
  * also provided is automatic delete of time-stamped reports based on a retain setting
* an explicit directory location for reports may be specified

JaCoCo usage
* includes
  * each project (base and associated projects) may be individually managed for includes gathering of coverage
  * Netbeans source-code and test-source-code folders are individually manageable (thus code coverage on test-cases is possible)
* excludes
  * explicit exclude package definitions are manageable 

Development

* The code is a significant modification of tikione-jacocoverage code.
  * All packaging was changed from fr.tikione.* to dbrad.* so as supported as a separate module.
  * Many more classes created to represent new/modified capabilites/features/function.
  * Test-case scripts have been added to perform functional test of the GUI sections.

License & copyright
As at Oct/Nov2017 a license for dbrad-jacocoverage remains undecided due to Netbeans Apache incubation.

History

* 2019/04/03: Software was uploaded 
* 2017/10/29: creation of repository with refining edits for the README.md

  
