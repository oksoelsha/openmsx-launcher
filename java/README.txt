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

Build the MacOS app
-------------------
cd openmsx-launcher-package-mac

to update the local repository with the appbundle jar (this step is needed only once):
mvn install:install-file -Dfile=lib/appbundler-1.0.jar -DgroupId=com.oracle.appbundler -DartifactId=appbundler -Dversion=1.0 -Dpackaging=jar

to create the app:
mvn package

resulting app is in openmsx-launcher-package-mac/target