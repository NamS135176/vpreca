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