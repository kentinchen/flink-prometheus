package org.flink.udf.function;

import org.apache.flink.table.functions.ScalarFunction;

public class RedisInsIdUdf extends ScalarFunction {
    public String eval(String str) {
        if (str == null)
            return "XIAOMAO";
        if (!str.contains("-db-"))
            return str;
        return str.substring(0, str.indexOf("-db-"));
    }
}
