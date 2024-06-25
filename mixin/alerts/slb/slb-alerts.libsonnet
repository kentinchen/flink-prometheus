{
    _config+:: {
      slbSelector: error '必须提供slb产品选择器',
      slbmetricName: 'sumOutBytes',
      dropConnThrottlingPercent: 10,
      failConnThrottlingPercent: 10,
      maxConnsPsThrottlingPercent: 30000,
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'slb作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'slb',
                    metricName::  $._config.slbmetricName,
                    selector:: $._config.slbSelector,
                    labels+: {
                          category: '平台运维',
                          notify_identity: 'OPS',
                          product: 'slb',
                    },
                    annotations+: {
                          message: "slb作业未启动",
                    }
                  },{
                       alert: '丢失连接数',
                       expr: |||
                         dropConnPS {product="slb"} > %(dropConnThrottlingPercent)d
                       ||| % $._config,
                       'for': '5m',
                       labels+: {
                            severity: 'critical',
                            category: '平台运维',
                            notify_identity: 'OPS',
                       },
                       annotations: {
                            description: 'slb: {{ $labels.instanceId }} 丢失连接数为: {{ $value }}',
                            summary: '丢失连接数太高了'
                       }
                    },
                    {
                       alert: '建联失败数',
                       expr: |||
                         failConnPS {product="slb"}> %(failConnThrottlingPercent)d
                       ||| % $._config,
                       'for': '5m',
                       labels+: {
                         severity: 'critical',
                         category: '平台运维',
                         notify_identity: 'OPS',
                       },
                       annotations: {
                         description: 'slb: {{ $labels.instanceId }} 建联失败数为: {{ $value }}',
                         summary: '建联失败数太高了'
                       }
                   },
                    {
                       alert: '最大并发连接数过高告警',
                       expr: |||
                         maxConnsPs {product="slb"}> %(maxConnsPsThrottlingPercent)d
                       ||| % $._config,
                       'for': '5m',
                       labels+: {
                         severity: 'critical',
                         category: '平台运维',
                         notify_identity: 'OPS',
                       },
                       annotations: {
                         description: 'slb: {{ $labels.instanceId }} 建联失败数为: {{ $value }}',
                         summary: '最大并发连接数过高告警'
                       }
                   },
                ],
              },
        ],
    }
}