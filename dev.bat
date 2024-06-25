.\gradlew clean
.\gradlew jar --rerun-tasks
.\gradlew shadow
docker/build-dev.bat
docker-compose up -d
docker-compose up -d -f deploy\dev\docker-compose.yml

docker exec -it jobmanager bash
./bin/sql-client.sh

.\gradlew tasks
.\gradlew help --task test
.\gradlew :connectors:flink-connector-socket:jar --info
.\gradlew :apps:prometheus:spotlessApply
.\gradlew spotbugsMain

nc -lk 9999
INSERT|Alice|12

sls查询
i-6qn059f007t6uy2yinea and vm.TcpCount

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

promtool check config prometheus.yml

http://localhost:8060/ui
curl -XPOST http://localhost:8060/-/reload

https://oapi.dingtalk.com/robot/send?access_token=9336388adedf15d84421a0a9347df939cdcaca77bc2970cd711e3140883bfb64
SECfefb5520fdfb065b30fcee229f2415c4d9b74af285107c4379ccd49bf6ebf273

https://oapi.dingtalk.com/robot/send?access_token=e342ee0d808536f5120b3370303710807594c43758f380fc565045149dd986d9
告警

curl -XPOST -d"{}" http://localhost:8060/dingtalk/webhook1/send
curl -XPOST -d"{}" http://10.81.200.185:8060/dingtalk/webhook1/send


curl -XPOST http://10.81.200.185:9093/-/reload
https://www.prometheus.io/webtools/alerting/routing-tree-editor/

#dos2unix转换目录下所有文件
find . -type f -exec dos2unix {} \;