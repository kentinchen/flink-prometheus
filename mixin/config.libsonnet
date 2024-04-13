{
    _config+:: {
        // Config for the Grafana dashboards in the Kubernetes Mixin
        grafana: {
          dashboardNamePrefix: 'CRE / ',
          dashboardTags: ['CRE Dashboard'],

          // For links between grafana dashboards, you need to tell us if your grafana
          // servers under some non-root path.
          linkPrefix: '',

          // The default refresh time for all dashboards, default to 10s
          refresh: '10s',
          minimumTimeInterval: '1m',

          // Timezone for Grafana dashboards:: UTC, browser, ...
          grafanaTimezone: 'Asia/Shanghai',
        },
    }
}