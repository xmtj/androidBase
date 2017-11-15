#!/bin/bash
./gradlew  clean
./gradlew  tinkerPatchRelease
./gradlew  assembleReleaseChannels
./gradlew  copyBaseApk
