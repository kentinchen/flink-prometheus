del libs/*.jar
.\gradlew clean
.\gradlew jar
.\gradlew shadow
docker/build-dev.bat
docker-compose up -d

docker exec -it jobmanager bash
./bin/sql-client.sh

.\gradlew jar
docker/build-dev.bat
docker-compose up -d

.\gradlew tasks
.\gradlew help --task test
.\gradlew :connectors:flink-connector-socket:jar --info
.\gradlew :apps:prometheus:spotlessApply
.\gradlew spotbugsMain

nc -lk 9999
INSERT|Alice|12

https://mirrors.cloud.tencent.com/gradle/

for %%cntr in ("jobmanager" "taskmanager") do (
  echo %%cntr
  docker exec %%cntr mkdir -p /opt/flink/scripts
  docker exec %%cntr mkdir -p /opt/flink/data;
)

cntrs = ("jobmanager,taskmanager1,taskmanager2")
foreach ($cntr in $cntrs) {
    echo $cntr
    docker exec $cntr mkdir -p /opt/flink/scripts
    docker exec $cntr mkdir -p /opt/flink/data;
}

