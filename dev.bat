.\gradlew tasks
.\gradlew clean
.\gradlew build
.\gradlew shadow
.\gradlew :connectors:flink-connector-socket:jar --info
.\gradlew :apps:prometheus:spotlessApply

del libs/*.jar
copy apps/*/build/libs/*.jar libs
copy connectors/*/build/libs/*.jar libs
docker/build-dev.bat

nc -lk 9999