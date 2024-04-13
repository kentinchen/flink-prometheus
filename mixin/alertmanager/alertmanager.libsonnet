local alertmanagerConfig = import 'github.com/crdsonnet/alertmanager-libsonnet/alertmanagerConfig/main.libsonnet';
local alertmanagerKube = import 'github.com/crdsonnet/alertmanager-libsonnet/alertmanagerKube/main.libsonnet';

local testReceiver =
  alertmanagerConfig.receivers.new('test')
  + alertmanagerConfig.receivers.withSlackConfigs([
    alertmanagerConfig.receivers.slack_configs.new('#general'),
  ])
  + alertmanagerConfig.receivers.withWebhookConfigs([
    alertmanagerConfig.receivers.webhook_configs.new('http://localhost/hot/new/webhook'),
  ]);

local config =
  alertmanagerConfig.withRoute([
    alertmanagerConfig.route.withReceiver(testReceiver.name),
  ])
  + alertmanagerConfig.withReceivers([
    testReceiver,
  ]);

alertmanagerKube.new()
+ alertmanagerKube.withConfig(config)