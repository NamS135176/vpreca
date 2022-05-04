## Setup

Download file at https://smart-idea.backlog.jp/file/VPRECAAPP/keystore.properties and https://smart-idea.backlog.jp/file/VPRECAAPP/secure.properties
save those file to root of project to build and run

## Some cli
- clear android : adb shell pm clear com.lifecard.vpreca 
- clean and build release: ./gradlew clean && ./gradlew assembleRelease

## release file path:
./app/build/outputs/apk/release/app-release.apk