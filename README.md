# Aiml Java Interpreter
[![Build Status](https://travis-ci.org/AIMLang/aiml-java-interpreter.svg?branch=master)](https://travis-ci.org/AIMLang/aiml-java-interpreter)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

AIML 2.0 Interpreter for Java

It is not contains full implementation of specification, basically it is still pet project for aimlang spec implementation.
So please keep it in mind.

## 1. Checkout bots and add symlink at homedir
Bots repo - https://github.com/AIMLang/aiml-bots.git

`./checkout.sh`

## 2. Build
### 2.1 Using Maven
`mvn clean package`
## 2. Using Gradle
`gradle clean fatJar`

## 3.Run
1. `./scripts/run.sh`
2. `java -jar ./target/aiml.jar`
3. `java -jar ./target/aiml.jar russian`

## Dependencies
- Logback (1.2.3)
- Slf4j (1.7.25)
- JUnit (4.12)

## Contacts
anton@batiaev.com