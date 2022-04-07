#!/bin/bash

./gradlew sonarqube \
  -Dsonar.projectKey=VPrecaAndroid \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=b1fa23358a9621408d437e9f0b7ea2444da9ca47