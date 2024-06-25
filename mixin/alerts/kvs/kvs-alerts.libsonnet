{
    _config+:: {
      kvSelector: error '必须提供kvs产品选择器',
      kvMetricName: 'CpuUsage',
      cpuUsageThrottlingPercent: 70,
      failedCountThrottlingPercent: 10,
      memoryUsageThrottlingPercent: 70,
      connectionUsageThrottlingPercent: 80,
     },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'kv作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'kv',
                    metricName::  $._config.kvMetricName,
                    selector:: $._config.kvSelector,
                    labels+: {
                          category: '平台运维',
                          notify_identity: 'OPS',
                          product: 'kv',
                    },
                    annotations+: {
                          message: "kv作业未启动",
                    }
                  },
                   { alert: 'CPU使用率告警',
                     expr: |||
                       CpuUsage {product="kv"}> %(cpuUsageThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'redis: {{ $labels.instanceId }} CPU使用率为: {{ $value }}%',
                       summary: 'CPU使用率太高',
                     },
                  },
                  { alert: '连接失败数告警',
                     expr: |||
                       FailedCount {product="kv"}> %(failedCountThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'redis: {{ $labels.instanceId }} 连接失败数为: {{ $value }}',
                       summary: '连接失败数太高',
                     },
                  },
                  { alert: '内存使用率告警',
                     expr: |||
                       MemoryUsage {product="kv"}> %(memoryUsageThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'redis: {{ $labels.instanceId }} 内存使用率为: {{ $value }}%',
                       summary: '内存使用率太高了',
                     },
                  },
                  { alert: '连接使用率告警',
                     expr: |||
                       ConnectionUsage {product="kv"}> %(connectionUsageThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'redis: {{ $labels.instanceId }} 使用率为: {{ $value }}%',
                       summary: '连接使用率太高了',
                     },
                  },
                ],
              },
        ],
    }
}