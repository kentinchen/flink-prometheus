{
  local absentAlert = self,
  componentName:: error '必须提供产品名',
  metricName:: error '必须提供产品指标名',
  selector:: error '必须提供产品名选择器',

  alert: '%s作业未启动' % absentAlert.componentName,
  expr: |||
     absent(%s{product="%s"})
  ||| % [absentAlert.metricName, absentAlert.componentName],
  'for': '5m',
  labels: {
    severity: 'critical',
  },
  annotations: {
    description: '%s has disappeared from Prometheus target discovery.' % absentAlert.componentName,
    summary: 'Target disappeared from Prometheus target discovery.',
  },
}
