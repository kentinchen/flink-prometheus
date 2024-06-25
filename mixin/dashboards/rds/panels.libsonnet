local g = import '../g.libsonnet';
local base = import '../base/panels.libsonnet';

{
  timeSeries: base.timeSeries + {
    active_session+: self.long,
    Queries_ps: self.short,
    Slow_queries_ps: self.long,
    TPS: self.short,
    QPS: self.short,
    CpuUsage: self.long,
    DiskUsage: self.long,
    IOPSUsage: self.long,
    MemoryUsage: self.long,
    Key_usage_ratio: self.long,
    Key_read_hit_ratio: self.long,
    Key_write_hit_ratio: self.long,
  }
}
