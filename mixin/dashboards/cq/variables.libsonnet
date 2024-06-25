local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus')
      + var.datasource.generalOptions.showOnDashboard.withNothing(),

    instances:
      var.query.new('instances','select vm_id from monitor.ecs_info')
      + var.query.withDatasource(
                     type='mysql',
                     uid='MySQL',
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