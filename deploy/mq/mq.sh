#! /bin/bash
# 用配置文件中配置启动rocketmq-exporter
nohup java -jar -Dspring.config.location=./mq1-application.yml ./rocketmq-exporter-0.0.3-SNAPSHOT.jar &

# 每15秒抽取exporter指标一次并向pushgateway推送
n=1
while (( $n <= 100000 ))
do
   curl -s http://localhost:5557/metrics | curl --data-binary @- http://10.81.69.186:9091/metrics/job/mq_job/instance/Bj3IscUQ
   #(( n++ ))
   sleep 15
done