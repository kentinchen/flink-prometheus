import os
import time
from aliyun.log import LogClient, ListLogstoresRequest, GetLogsRequest, GetProjectLogsRequest
from datetime import datetime


def list_logstores(client, project):
    request = ListLogstoresRequest(project)
    response = client.list_logstores(request)
    response.log_print()


def get_logs(client, project, logstore):
    topic = ''
    # 秒
    From = int(time.time()) - 3600
    To = int(time.time())
    # test formatted time
    # From = datetime.fromtimestamp(From).strftime('%Y-%m-%d %H:%M:%S')
    # To = datetime.fromtimestamp(To).strftime('%Y-%m-%d %H:%M:%S')
    request = GetLogsRequest(project, logstore, From, To, topic)
    response = client.get_logs(request)
    response.log_print()

    res = client.get_log(project, logstore, From, To, topic)
    res.log_print()

    res = client.get_log_all(project, logstore, From, To, topic)
    for x in res:
        x.log_print()


# 统计接口审计日志
def count_api_log(client, project='ascm-asapi', logstore='api-audit'):
    from_dt = datetime.fromtimestamp(int(time.time()) - 3600 * 24).strftime('%Y-%m-%d %H:%M:%S')
    to_dt = datetime.fromtimestamp(int(time.time())).strftime('%Y-%m-%d %H:%M:%S')
    req = GetProjectLogsRequest(project, "select productName,apiName, count(1) as count from %s "
                                         "where __date__ >'%s' and __date__ < '%s' and status='success' "
                                         "group by productName,apiName "
                                         "order by count desc"
                                % (logstore, from_dt, to_dt))
    res = client.get_project_logs(req)
    # res.log_print()
    print("*************接口审计日志调用API:%s" % len(res.logs))
    for x in res.logs:
        print(x.contents['productName'], x.contents['apiName'], x.contents['count'])


# 统计操作日志
def count_op_log(client, project='ascm-logger', logstore='op-logger'):
    from_dt = datetime.fromtimestamp(int(time.time()) - 60 * 1).strftime('%Y-%m-%d %H:%M:%S')
    to_dt = datetime.fromtimestamp(int(time.time())).strftime('%Y-%m-%d %H:%M:%S')
    # req = GetProjectLogsRequest(project, "select apiName, count(1) as count from %s "
    #                                      "where __date__ >'%s' and __date__ < '%s' "
    #                                      "group by apiName "
    #                                      "order by count desc"
    req = GetProjectLogsRequest(project, "select * from %s "
                                         "where __date__ >'%s' and __date__ < '%s'  limit 100"
                                % (logstore, from_dt, to_dt))
    res = client.get_project_logs(req)
    for x in res.logs:
        print(x.contents['apiName'], x.contents['count'])


def exec_sql_log(client, project, sql):
    from_dt = datetime.fromtimestamp(int(time.time()) - 3600 * 1).strftime('%Y-%m-%d %H:%M:%S')
    to_dt = datetime.fromtimestamp(int(time.time())).strftime('%Y-%m-%d %H:%M:%S')
    # req = GetProjectLogsRequest(project, sql % (from_dt, to_dt))
    req = GetProjectLogsRequest(project,
                                "select * from rivermaster where __date__ >'%s' and __date__ < '%s' limit 100" % (
                                    from_dt, to_dt))
    res = client.get_project_logs(req)
    for x in res.logs:
        x.log_print()


def main():
    endpoint = os.environ.get('ALIYUN_LOG_SAMPLE_ENDPOINT', 'data.cn-yaan-sczwhlw-d01.sls.res.inter-sctyun.com')
    accessKeyId = os.environ.get('ALIYUN_LOG_SAMPLE_ACCESSID', 'OLIDBuPuCROXJt52')
    accessKey = os.environ.get('ALIYUN_LOG_SAMPLE_ACCESSKEY', '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI')
    client = LogClient(endpoint, accessKeyId, accessKey, "", region='cn-yaan-sczwhlw-d01')

    # endpoint = os.environ.get('ALIYUN_LOG_SAMPLE_ENDPOINT', 'data.cn-yaan-sczwzw-d01.sls.res.intra-sctyun.com')
    # accessKeyId = os.environ.get('ALIYUN_LOG_SAMPLE_ACCESSID', 'OLIDBuPuCROXJt52')
    # accessKey = os.environ.get('ALIYUN_LOG_SAMPLE_ACCESSKEY', '9LoXpXIN5by9M8Gzz2CWgSIkGIbxyI')
    # client = LogClient(endpoint, accessKeyId, accessKey, "", region='cn-yaan-sczwzw-d01')
    # env = ZWW()

    # test list project
    ret = client.list_project(size=-1)
    print("**project count:", ret.get_count())

    # list_logstores(client, project)
    # get_logs(client, project, logstore)

    # 统计审计日志
    print("*************统计审计日志")
    count_api_log(client)

    # 统计操作日志
    print("*************统计操作日志")
    # count_op_log(client)

    # sql = "select t.cluster as cluster,t.serverrole as serverrole,t.host as host, value(t._fuxi_cnt) as _fuxi_cnt,
    # value(t._server_black_cnt) as _server_black_cnt,value(t._server_discon_cnt) as _server_discon_cnt,
    # value(t._server_normal_cnt) as _server_normal_cnt,value(t._river_cluster_switch) as _river_cluster_switch,
    # value(t._cluster_cnt) as _cluster_cnt,value(t._river_version) as _river_version,
    # value(t._river_master_switch) as _river_master_switch,value(t._master_cnt) as _master_cnt " \
    #       "from rivermaster t " \
    #       "where t.__date__ >'%s' and t.__date__ < '%s' and t.scriptname='ecs_ag_storage-check_river_service'"
    # exec_sql_log(client, 'tjm-ecsrivermaster', sql)


if __name__ == '__main__':
    main()
