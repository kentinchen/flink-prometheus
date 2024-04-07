package org.flink.udf.function;

import org.apache.flink.table.functions.ScalarFunction;
import org.flink.udf.function.utils.JSQLParseUtils;

public class SQLParseUdf extends ScalarFunction {
    public String eval(String sql, String type) {
        return JSQLParseUtils.parseToPlaceholder(sql);
    }
}
