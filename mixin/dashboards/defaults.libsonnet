{
  local creMixin = self,
  local grafanaDashboards = super.grafanaDashboards,

  // Automatically add a uid to each dashboard based on the base64 encoding
  // of the file name and set the timezone to be 'default'.
  grafanaDashboards:: {
    [filename]: grafanaDashboards[filename] {
      uid: std.md5(filename),
      timezone: creMixin._config.grafana.grafanaTimezone,
      refresh: creMixin._config.grafana.refresh,
      tags: creMixin._config.grafana.dashboardTags,

      [if 'rows' in super then 'rows']: [
        row {
          panels: [
            panel {
              // Modify tooltip to only show a single value
              tooltip+: {
                shared: false,
              },
              // Modify legend to always show as table on right side
              legend+: {
                alignAsTable: true,
                rightSide: true,
              },
              // Set minimum time interval for all panels
              interval: creMixin._config.grafana.minimumTimeInterval,
            }
            for panel in super.panels
          ],
        }
        for row in super.rows
      ],

    }
    for filename in std.objectFields(grafanaDashboards)
  },
}
