package org.flink.connectors.hitsdb;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

public final class HiTSDBOptions {
    public static final ConfigOption<Boolean> IGNORE_WRITE_ERROR = ConfigOptions.key("ignoreWriteError".toLowerCase()).defaultValue(Boolean.valueOf(false));

    public static final ConfigOption<Boolean> PRINT_POINT_DETAILS = ConfigOptions.key("printPointDetails".toLowerCase()).defaultValue(Boolean.valueOf(false));

    public static final ConfigOption<String> USERNAME = ConfigOptions.key("username".toLowerCase()).noDefaultValue();

    public static final ConfigOption<String> PASSWORD = ConfigOptions.key("password".toLowerCase()).noDefaultValue();

    public static final ConfigOption<String> DATABASE = ConfigOptions.key("database".toLowerCase()).noDefaultValue();

    public static final ConfigOption<Boolean> USE_PARTITION_MODE = ConfigOptions.key("usePartitionMode".toLowerCase()).defaultValue(Boolean.valueOf(false));

    public static final ConfigOption<String> PARTITION_KEY = ConfigOptions.key("partitionKey".toLowerCase()).defaultValue("");

    public static final ConfigOption<String> PARTITION_HOSTS = ConfigOptions.key("partitionHosts".toLowerCase()).defaultValue("");
}
