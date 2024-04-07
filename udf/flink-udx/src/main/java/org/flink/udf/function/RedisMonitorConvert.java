package org.flink.udf.function;

import org.apache.flink.table.functions.ScalarFunction;
import org.flink.udf.enums.KvStoreEnum;

public class RedisMonitorConvert extends ScalarFunction {
    public String eval(String m) {
        return KvStoreEnum.getOldCode(m);
    }
}
