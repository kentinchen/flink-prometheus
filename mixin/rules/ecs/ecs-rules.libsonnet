{
  _config+:: {
    kubeletSelector: 'job="kubelet"',
    clusterLabel: ''
  },

  prometheusRules+:: {
    groups+: [
      {
        name: 'kubelet.rules',
        rules: [
          {
            record: 'node_quantile:kubelet_pleg_relist_duration_seconds:histogram_quantile',
            expr: |||
              histogram_quantile(%(quantile)s, sum(rate(kubelet_pleg_relist_duration_seconds_bucket{%(kubeletSelector)s}[5m])) by (cluster, instance, le) * on(cluster, instance) group_left(node) kubelet_node_name{job="kubelet"})
            ||| % ({ quantile: quantile } + $._config),
            labels: {
              quantile: quantile,
            },
          }
          for quantile in ['0.99', '0.9', '0.5']
        ],
      },
    ],
  },
}
