#!/bin/bash
cd /users/dut/info/$USER/git/projet_s2/
export JAVA_HOME="/users/dut/info/demmanuv/git/jdk-11.0.1"
JAVA_HOME="/users/dut/info/demmanuv/git/jdk-11.0.1" mvn package
$JAVE_HOME/bin/java -jar target/project-armstrong-0.0.1-SNAPSHOT-jar-with-dependencies.jar gui
