{
    _config+:: {
      ecsSelector: error '必须提供ecs产品选择器',
      ecsmetricName: 'vm_CPUUtilization',
      ecsAlertAlertTime: '30s',
      cpuThrottlingPercent: 70,
      cpuThrottling75Percent: 75,
      cpuThrottling80Percent: 80,
      memThrottlingPercent: 70,
      memThrottling75Percent: 75,
      memThrottling80Percent: 80,
      diskThrottling70Percent: 70,
      diskThrottlingPercent: 80,
      inodeThrottlingPercent: 60,
      tcpCountThrottlingPercentMax: 10000,
      tcpCountThrottlingPercent: 6000,
      processCountThrottlingPercentMax: 5000,
      processCountThrottlingPercent: 1500,
      internetNetworkRXThrottlingPercent: 300000,
      internetNetworkTXThrottlingPercent: 300000,
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'ecs告警规则集',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'ecs',
                    metricName::  $._config.ecsmetricName,
                    selector:: $._config.ecsSelector,
                    labels+: {
                        category: '平台运维',
                        notify_identity: 'OPS',
                        product: 'ecs',
                    },
                    annotations+: {
                        message: "ecs作业未启动",
                    }
                  },
                  {
                     alert: 'CPU使用率告警',
                     expr: |||
                       vm_CPUUtilization {product="ecs",instanceId != "i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i|i-4fo01o4le2mk2k46qxa4"}> %(cpuThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} CPU使用率为: {{ $value }}%',
                       summary: 'CPU使用率太高',
                     },
                  },
                  {
                     alert: 'CPU使用率告警',
                     expr: |||
                       vm_CPUUtilization {product="ecs",instanceId = "i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i"}> %(cpuThrottling80Percent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} CPU使用率为: {{ $value }}%',
                       summary: 'CPU使用率太高',
                     },
                  },
                  {
                     alert: 'CPU使用率告警',
                     expr: |||
                       vm_CPUUtilization {product="ecs",instanceId = "i-4fo01o4le2mk2k46qxa4"}> %(cpuThrottling75Percent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} CPU使用率为: {{ $value }}%',
                       summary: 'CPU使用率太高',
                     },
                  },
                  {
                     alert: '内存使用率告警',
                     expr: |||
                       vm_MemoryUtilization {product="ecs",instanceId != "i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i|i-4fo01o4le2mk2k46qxa4"}> %(memThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} 内存使用率为: {{ $value }}%',
                       summary: '内存使用率太高',
                     },
                  },
                  {
                     alert: '内存使用率告警',
                     expr: |||
                       vm_MemoryUtilization {product="ecs",instanceId = "i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i"}> %(memThrottling80Percent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} 内存使用率为: {{ $value }}%',
                       summary: '内存使用率太高',
                     },
                  },
                  {
                     alert: '内存使用率告警',
                     expr: |||
                       vm_MemoryUtilization {product="ecs",instanceId = "i-4fo01o4le2mk2k46qxa4"}> %(memThrottling75Percent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} 内存使用率为: {{ $value }}%',
                       summary: '内存使用率太高',
                     },
                  },
                  {
                     alert: '磁盘使用率告警',
                     expr: |||
                       vm_DiskUtilization {product="ecs",instanceId != "i-4fo01h7fs2f4irqk6i6d|i-4fo01h7fs2f4irqk6i64|i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i|i-4fo01h7fs2dopnst7a1d"}> %(diskThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }} 分区:{{$labels.mountpoint}} 使用率为: {{ $value }}%',
                       summary: '磁盘使用率太高',
                     },
                  },
                  {
                      alert: '磁盘使用率告警',
                      expr: |||
                        vm_DiskUtilization {product="ecs" ,instanceId = "i-4fo01h7fs2f4irqk6i6d|i-4fo01h7fs2f4irqk6i64|i-4fo01h7fs2dopprubc0k|i-4fo01h7fs2dopprubc0j|i-4fo01h7fs2dopprubc0i|i-4fo01h7fs2dopnst7a1d"}> %(diskThrottling70Percent)d
                      ||| % $._config,
                      'for': '5m',
                      labels+: {
                        severity: 'critical',
                        category: '平台运维',
                        notify_identity: 'OPS',
                      },
                      annotations: {
                        description: 'ecs: {{ $labels.instanceId }} 分区:{{$labels.mountpoint}} 使用率为: {{ $value }}%',
                        summary: '磁盘使用率太高',
                      },
                  },
                  {
                     alert: 'Inode过高告警',
                     expr: |||
                       vm_DiskIusedUtilization {product="ecs"}> %(inodeThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }}  使用率为: {{ $value }}%',
                       summary: 'Ionde过高',
                     },
                  },
                  {
                     alert: '连接数过高告警',
                     expr: |||
                       vm_TcpCount {product="ecs",instanceId = "i-4fo01h7fs2dopprubc0k"}> %(tcpCountThrottlingPercentMax)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }}  连接数为: {{ $value }}',
                       summary: 'tcp连接数过高',
                     },
                  },
                  {
                     alert: '连接数过高告警',
                     expr: |||
                       vm_TcpCount {product="ecs",instanceId != "i-4fo01h7fs2dopprubc0k"}> %(tcpCountThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }}  连接数为: {{ $value }}',
                       summary: 'tcp连接数过高',
                     },
                  },
                  {
                     alert: '进程数过高告警',
                     expr: |||
                       vm_ProcessCount {product="ecs",instanceId = "i-4fo01h7fs2dopprubc0k"}> %(processCountThrottlingPercentMax)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }}  进程数为: {{ $value }}',
                       summary: '进程数数过高',
                     },
                  },
                  {
                     alert: '进程数过高告警',
                     expr: |||
                       vm_ProcessCount {product="ecs",instanceId != "i-4fo01h7fs2dopprubc0k"}> %(processCountThrottlingPercent)d
                     ||| % $._config,
                     'for': '5m',
                     labels+: {
                       severity: 'critical',
                       category: '平台运维',
                       notify_identity: 'OPS',
                     },
                     annotations: {
                       description: 'ecs: {{ $labels.instanceId }}  进程数为: {{ $value }}',
                       summary: '进程数数过高',
                     },
                  },
                ],
              },
        ],
    }
}