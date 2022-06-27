## Important for security check
Web did implement SSL Pinning for 2 api:
- vpreca api mock server
- google vision api 
here is expiration date of those api:
- vpreca api mock server: 
    + file: app/src/main/res/xml/network_security_config.xml
    + file: app/src/main/java/com/lifecard/vpreca/data/api/ApiServiceFactory.kt
    + expiration: Jan 17 2038
- Google Vision: 
    + file: app/src/main/res/xml/network_security_config.xml
    + file: app/src/main/java/com/lifecard/vpreca/data/api/ApiServiceFactory.kt
    + expiration: June 22 2036

Before those certificate has been expired, app must be update new certificate and submit to store

## Setup

Download file at 
- https://smart-idea.backlog.jp/file/VPRECAAPP/keystore.properties
- https://smart-idea.backlog.jp/file/VPRECAAPP/secure.properties
- https://smart-idea.backlog.jp/file/VPRECAAPP/rsa_key/private_key.pem

save those file to root of project to build and run

## Some cli
- clear android : adb shell pm clear com.lifecard.vpreca 
- clean and build release: ./gradlew clean && ./gradlew assembleRelease

## Release:
- file path: ./app/build/outputs/apk/release/app-release.apk
- cli to deploy to deploygate: sh deploygate-submit.sh

## Deploygate link:
- android dev: https://dply.me/jmfhf5


## Generate debug keystore
get sha1 from debug keystore: 

keytool -list -v -keystore ./app/debugKeystore/debug.keystore -alias androiddebugkey -storepass android -keypass android

keytool -list -v -keystore ./app/keystore/key.jks -alias vpreca


- show all signing report
./gradlew signingReport

## Get SHA1 fingerprint for API vision config
debug key: 
keytool -list -v -keystore ./app/debugKeystore/debug.keystore -alias androiddebugkey -storepass android -keypass android