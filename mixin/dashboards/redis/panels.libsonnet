local g = import '../g.libsonnet';
local base = import '../base/panels.libsonnet';

{
  timeSeries: base.timeSeries + {
    connectionUsage: self.long,
    cpuUsage: self.long,
    failedCount: self.short,
    intranetIn: self.long,
    intranetInRatio: self.long,
    intranetOut: self.long,
    intranetOutRatio: self.long,
    memoryUsage: self.long,
    qpsUsage: self.long,
    quotaBandWidth: self.short,
    quotaConnection: self.short,
    quotaMemory: self.short,
    quotaQPS: self.short,
    usedConnection: self.short,
    usedMemory: self.short,
    usedQPS: self.short,
  },
}
