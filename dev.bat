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

安装go-jsonnet及jb
https://github.com/google/go-jsonnet/releases
go install -a github.com/jsonnet-bundler/jsonnet-bundler/cmd/jb@latest

https://github.com/grafana/grafonnet
初始化目录并增加依赖
jb init
jb install github.com/grafana/grafonnet/gen/grafonnet-latest@main
编写文件并生成json
jsonnet -J vendor dashboard.jsonnet > dashboadd.json