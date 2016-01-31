Build projects, run unit tests and publish to local repository
--------------------------------------------------------------
cd openmsx-launcher-build
mvn clean install


Assemble release JAR
--------------------
cd openmsx-launcher-assembly
mvn assembly:single

resulting JAR is in openmsx-launcher-assembly/target


Update Eclipse projects
-----------------------
mvn eclipse:eclipse
then on eclipse: maven->update project