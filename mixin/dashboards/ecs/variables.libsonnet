local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus'),

    instanceid:
      var.query.new('targets')
      + var.query.withDatasourceFromVariable(self.datasource)
      + var.query.queryTypes.withLabelValues(
        'instanceid',
        'vm_LoadAverage',
      ),

    toArray: [
      self.datasource,
      self.targets,
    ],

    selector:
      'instanceid="$%s"' % [
        self.targets,
      ],
}