local g = import '../g.libsonnet';
local var = g.dashboard.variable;

{
    datasource:
      var.datasource.new('datasource', 'prometheus'),

    buckets:
      var.query.new('buckets','select bucket_id from monitor.oss_info')
      + var.query.withDatasource(
                     type='mysql',
                     uid='MySQL',
                   )
      + var.query.selectionOptions.withMulti()
      + var.query.selectionOptions.withIncludeAll(),

    toArray: [
      self.datasource,
      self.buckets,
    ],
}