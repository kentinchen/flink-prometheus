.\gradlew clean
.\gradlew jar --rerun-tasks
.\gradlew shadow
docker/build-dev.bat
docker-compose up -d

docker exec -it jobmanager bash
./bin/sql-client.sh

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

http://localhost:8060/ui
curl -XPOST http://localhost:8060/-/reload

https://oapi.dingtalk.com/robot/send?access_token=9336388adedf15d84421a0a9347df939cdcaca77bc2970cd711e3140883bfb64
SECfefb5520fdfb065b30fcee229f2415c4d9b74af285107c4379ccd49bf6ebf273

https://oapi.dingtalk.com/robot/send?access_token=e342ee0d808536f5120b3370303710807594c43758f380fc565045149dd986d9
告警

curl -XPOST -d"{}" http://localhost:8060/dingtalk/webhook1/send