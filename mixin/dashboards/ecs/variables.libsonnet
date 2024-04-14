local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus'),

    instances:
      var.query.new('instances')
      + var.query.withDatasourceFromVariable(self.datasource)
      + var.query.queryTypes.withLabelValues(
        'instanceId',
        'vm_LoadAverage',
      )
      + var.query.selectionOptions.withMulti()
      + var.query.selectionOptions.withIncludeAll(),

    toArray: [
      self.datasource,
      self.targets,
    ],

    ecsSelector:
      'instanceid=~"$%s",job=~"pushgateway"' % [
        self.instances,
      ],
}