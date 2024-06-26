local utils = import '../lib/utils/utils.libsonnet';

local lower(x) =
  local cp(c) = std.codepoint(c);
  local lowerLetter(c) =
    if cp(c) >= 65 && cp(c) < 91
    then std.char(cp(c) + 32)
    else c;
  std.join('', std.map(lowerLetter, std.stringChars(x)));

{
  _config+:: {
    runbookURLPattern: 'https://github.com',
  },

  prometheusAlerts+::
    local addRunbookURL(rule) = rule {
      [if 'alert' in rule && !('runbook_url' in rule.annotations) then 'annotations']+: {
        runbook_url: $._config.runbookURLPattern,
      },
    };
    utils.mapRuleGroups(addRunbookURL),
}
