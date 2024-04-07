package org.flink.udf.function;

import org.apache.flink.table.functions.ScalarFunction;
import org.flink.udf.enums.RdsEnum;

public class RdsMonitorConvert extends ScalarFunction {
    public String eval(String m) {
        return RdsEnum.getOldCode(m);
    }
}
