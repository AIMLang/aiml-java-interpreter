#!/bin/bash

SCRIPT_PATH=${0%/*}
if [ "$0" != "$SCRIPT_PATH" ] && [ "$SCRIPT_PATH" != "" ]; then
    cd $SCRIPT_PATH
fi

cd ../
mvn clean package
java -jar ./target/aiml-java-interpreter-1.0.0-SNAPSHOT.jar