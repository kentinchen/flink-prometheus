local g = import '../g.libsonnet';
local base = import '../base/panels.libsonnet';

{
  timeSeries: base.timeSeries + {
    storage: self.bytes,
    recv_size: self.short,
    send_size: self.short,
    error_403: self.short,
    error_404: self.short,
    error_408: self.short,
    error_499: self.short,
    error_4xx: self.short,
    error_503: self.short,
    error_5xx: self.short,
    success_203: self.short,
    success_2xx: self.short,
    success_3xx: self.short,
    intran_recv_size: self.short,
    intran_send_size: self.short,
  },
}
