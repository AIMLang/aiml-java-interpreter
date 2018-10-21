# Aiml Java Interpreter
[![Build Status](https://travis-ci.org/AIMLang/aiml-java-interpreter.svg?branch=master)](https://travis-ci.org/AIMLang/aiml-java-interpreter)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

AIML 2.0 Interpreter for Java

It is not contains full implementation of specification, basically it is still pet project for aimlang spec implementation.
So please keep it in mind.

## Build with Maven
`./checkout.sh && mvn clean package`

## Run
`./scripts/run.sh`

or

`mvn clean package && java -jar ./target/aiml-java-interpreter-1.0.0-SNAPSHOT.jar`

## Bots
https://github.com/AIMLang/aiml-bots.git

## Dependencies
- Spring Boot (2.0.6.RELEASE)
- Logback (1.2.3)
- Slf4j (1.7.25)
- JUnit (4.12)

## Contacts
anton@batiaev.com