local alertmanagerConfig = import "github.com/crdsonnet/alertmanager-libsonnet/alertmanagerConfig/main.libsonnet";

local testReceiver =
  alertmanagerConfig.receiver.new('test')
  + alertmanagerConfig.receiver.withSlackConfigs([
    alertmanagerConfig.receiver.slack.new('#general'),
  ])
  + alertmanagerConfig.receiver.withWebhookConfigs([
    alertmanagerConfig.receiver.webhook.new('http://localhost/hot/new/webhook'),
  ]);

alertmanagerConfig.withRoute([
  alertmanagerConfig.route.withReceiver(testReceiver.name),
])
+ alertmanagerConfig.withReceivers([
  testReceiver,
])
