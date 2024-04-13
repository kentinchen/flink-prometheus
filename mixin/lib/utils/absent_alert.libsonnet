{
  local absentAlert = self,
  componentName:: error 'must provide component name',
  selector:: error 'must provide selector for component',

  alert: '%s作业未启动' % absentAlert.componentName,
  expr: |||
    absent(up{%s} == 1)
  ||| % absentAlert.selector,
  'for': '5m',
  labels: {
    severity: 'critical',
  },
  annotations: {
    description: '%s has disappeared from Prometheus target discovery.' % absentAlert.componentName,
    summary: 'Target disappeared from Prometheus target discovery.',
  },
}
