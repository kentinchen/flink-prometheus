local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus'),

    instances:
      var.query.new('instances','select redis_id from monitor.kv_info')
      + var.query.withDatasource(
                     type='mysql',
                     uid='MySQL',
                   )
      + var.query.selectionOptions.withMulti()
      + var.query.selectionOptions.withIncludeAll(),


    toArray: [
      self.datasource,
      self.instances,
    ],
}