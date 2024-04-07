package org.flink.udf.function;

import org.apache.flink.table.functions.ScalarFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateFormatT08 extends ScalarFunction {
    public String eval(String uptime) {
        String ts;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS+0800");
            Date date = simpleDateFormat.parse(uptime);
            ts = String.valueOf(date.getTime());
        } catch (ParseException e) {
            ts = String.valueOf(Instant.now().toEpochMilli());
        }
        return ts;
    }
}
