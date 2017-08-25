#!/bin/bash

SCRIPT_PATH=${0%/*}
if [ "$0" != "$SCRIPT_PATH" ] && [ "$SCRIPT_PATH" != "" ]; then
    cd $SCRIPT_PATH
fi

cd ../../
git clone https://github.com/AIMLang/aiml-bots.git
cd aiml-java-interpreter/app-core/
ln -s ../../aiml-bots/ ./