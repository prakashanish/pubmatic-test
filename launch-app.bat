@ECHO OFF
echo "cleaning target folder"
cd bin
echo y | rmdir com /S
cd ..
echo "Building project"
javac -d bin -sourcepath src/main/java/ src/main/java/com/pubmatic/launcher/AppLauncher.java
echo "Copy resources"
xcopy .\src\main\resources\*.* .\bin\ /E

echo "setting class path"
set classpath=./bin/
set classpath=%classpath%;./jar/httpclient-osgi-4.3.jar;./jar/apache-httpcomponents-httpclient.jar;./jar/apache-httpcomponents-httpcore.jar;./jar/jackson-all-1.9.0.jar;./jar/junit.jar;./jar/hamcrest-core-1.2.jar;./jar/httpcore-4.3.jar;./jar/org-apache-commons-logging.jar;./jar/easymock-3.0.jar;./jar/cglib-2.1.3.jar;./jar/asm-3.1.jar;./jar/log4j.jar

echo "Starting the app"
java com.pubmatic.launcher.AppLauncher
