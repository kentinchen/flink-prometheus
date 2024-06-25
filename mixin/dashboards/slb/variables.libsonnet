local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus'),

    instances:
             var.query.new('instances','select slb_id from monitor.slb_info')
             + var.query.withDatasource(
                            type='mysql',
                            uid='MySQL',
                          )
             + var.query.selectionOptions.withMulti()
             + var.query.selectionOptions.withIncludeAll(),

    vports:
      var.query.new('vports')
      + var.query.withDatasourceFromVariable(self.datasource)
      + var.query.queryTypes.withLabelValues(
        'vport',
        'inBitsPS',
      )
      + var.query.selectionOptions.withMulti()
      + var.query.selectionOptions.withIncludeAll(),

    toArray: [
      self.datasource,
      self.instances,
    ],
}