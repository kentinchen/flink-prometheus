package org.flink.udf.function.cms;

import org.apache.flink.table.functions.ScalarFunction;
import org.flink.udf.enums.cms.ECSMetricEnum;

public class ECSMetricConvert extends ScalarFunction {

    //Row中的指标名转换成标准指标名
    public String eval(String m) {
        return ECSMetricEnum.of(m).name();
    }
}
