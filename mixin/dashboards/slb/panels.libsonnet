local g = import '../g.libsonnet';
local base = import '../base/panels.libsonnet';

{
  timeSeries: base.timeSeries + {
    connsPS: self.short,
    count: self.short,
    dropConnPS: self.short,
    failConnPS: self.short,
    inActConnPS: self.short,
    inBitsPS: self.short,
    inDBitesPS: self.short,
    inDPktsPS: self.short,
    inPktsPS: self.short,
    maxConnsPs: self.short,
    outBitsPS: self.short,
    outDBitesPS: self.short,
    outDPktsPS: self.short,
    outPktsPS: self.short,
    sumInBytes: self.bytes,
    sumOutBytes: self.bytes,
  },
}
