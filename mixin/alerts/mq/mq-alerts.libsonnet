{
    _config+:: {
      mqSelector: error '必须提供mq产品选择器',
      mqMetricName: 'online',
      okTpsCriticalValue: 2000,
      totalDiffCriticalValue: 1500,
      delayTimeCriticalValue: 300,
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'mq告警规则集',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'MQ',
                    metricName::  $._config.mqMetricName,
                    selector:: $._config.mqSelector,
                    labels+: {
                        category: '平台运维',
                        notify_identity: 'OPS',
                        product: 'MQ',
                    },
                    annotations+: {
                        message: "mq作业未启动",
                    }
                  },
                  {
                     alert: '消费速度告警',
                     expr: |||
                       okTps {product="MQ"} > %(okTpsCriticalValue)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'MQ: {{ $labels.instance_id }} 消费速度为: {{ $value }} 条/s',
                       summary: '消费速度太高',
                     },
                  },
                  {
                     alert: '消息堆积量告警',
                     expr: |||
                       totalDiff {product="MQ"}> %(totalDiffCriticalValue)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'MQ: {{ $labels.instance_id }} 消息堆积量为: {{ $value }} 条',
                       summary: '消息堆积量太高',
                     },
                  },
                  {
                     alert: '消息延时时间告警',
                     expr: |||
                       delayTime {product="MQ"}> %(delayTimeCriticalValue)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'MQ: {{ $labels.instance_id }} 消息延时时间为: {{ $value }} s',
                       summary: '消息延时时间太长',
                     },
                  },
                ],
              },
        ],
    }
}