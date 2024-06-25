{
    _config+:: {
      ossSelector: error '必须提供oss产品选择器',
      ossmetricName: 'recv_size',
      error5xxThrottlingPercent: 10,
      error4xxThrottlingPercent: 10,
      storageThrottlingPercent: 8,
      contrastStorageThrottlingPercent: -10,

    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'oss作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'oss',
                    metricName::  $._config.ossmetricName,
                    selector:: $._config.ossSelector,
                    labels+: {
                          category: '平台运维',
                          notify_identity: 'OPS',
                          product: 'oss',
                    },
                    annotations+: {
                          message: "oss作业未启动",
                    }
                  },
                  { alert: '5xx错误数告警',
                     expr: |||
                       error_5xx {product="oss"}> %(error5xxThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'oss: {{ $labels.instanceId }} 5xx错误数为: {{ $value }}',
                       summary: '5xx错误数太高了',
                     },
                  },
                  { alert: '4xx错误数告警',
                     expr: |||
                       error_4xx {product="oss"} > %(error4xxThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'oss: {{ $labels.instanceId }} 4xx错误数为: {{ $value }}',
                       summary: '4xx错误数太高了',
                     },
                  },
                  { alert: '用量告警',
                     expr: |||
                       (storage {product="oss"})/1099511627776 > %(storageThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'oss: {{ $labels.instanceId }} 已用内存为: {{ $value }} (TB)',
                       summary: '剩余内存告警',
                     },
                  },
                  { alert: '用量环比告警(与昨天同时刻比较)',
                     expr: |||
                       100 * (( storage {product="oss"} - (storage {product="oss"} offset 1d)) / storage {product="oss"}) <= %(contrastStorageThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'oss: {{ $labels.instanceId }} 已用内存为: {{ $value }} (byte)',
                       summary: '用量波动幅度告警',
                     },
                  },
                ],
              },
        ],
    }
}