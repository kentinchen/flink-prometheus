{
    _config+:: {
      rdsSelector: error '必须提供rds产品选择器',
      rdsmetricName: 'MemoryUsage',
      rdsAlertAlertTime: '30s',
      cpuThrottlingPercent: 70,
      memThrottlingPercent: 70,
      diskThrottlingPercent: 70,
      slowSQLTimeCriticalValue: 10,
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'rds作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'rds',
                    metricName::  $._config.rdsmetricName,
                    selector:: $._config.rdsSelector,
                    labels+: {
                              category: '平台运维',
                              product: 'rds',
                              notify_identity: 'OPS',
                    },
                    annotations+: {
                        message: "rds作业未启动",
                    }
                  },
                  {
                     alert: 'CPU使用率告警',
                     expr: |||
                       CpuUsage {product="rds"}> %(cpuThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'rds: {{ $labels.instanceId }} CPU使用率为: {{ $value }}%',
                       summary: 'CPU使用率太高',
                     },
                  },
                  {
                     alert: '内存使用率告警',
                     expr: |||
                       MemoryUsage {product="rds"}> %(memThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'rds: {{ $labels.instanceId }} 内存使用率为: {{ $value }}%',
                       summary: '内存使用率太高',
                     },
                  },
                  {
                     alert: '磁盘使用率告警',
                     expr: |||
                       DiskUsage {product="rds"}> %(diskThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'rds: {{ $labels.instanceId }} 分区:{{$labels.mountpoint}} 磁盘使用率为: {{ $value }}%',
                       summary: '磁盘使用率太高',
                     },
                  },
                  {
                     alert: '慢SQL告警',
                     expr: |||
                       (slow_sql {product="slow_sql"})/1000000 > %(slowSQLTimeCriticalValue)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: '慢SQL: {{ $labels.sql_rendered }}  执行时间为: {{ $value | humanizeDuration }}',
                       summary: '慢SQL执行时间太长',
                     },
                  },
                ],
              },
        ],
    }
}