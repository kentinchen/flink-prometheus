package com.flink.table.connectors.pushgateway;

import com.flink.connectors.pushgateway.sink.PushgatewayGaugeSinkEntity;
import com.flink.connectors.pushgateway.sink.function.PushgatewayBaseSinkFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.TreeMap;

import static com.flink.connectors.pushgateway.table.sink.PushgatewyaDynamicSinkConnectorOptions.PUSHGATEWAY;

public class PushgatewayFunctionTest {
    public static void main(String[] args) throws Exception {
        String PUSHGATEWAY = "pushgateway:9091";
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<MyData> dataStreamSource = env.fromCollection(Arrays.asList(
                new MyData("a", System.currentTimeMillis(), 3, "v1"),
                new MyData("b", System.currentTimeMillis(), 5, "v2")));
        dataStreamSource.addSink(new MyPushgatewaySinkFunction(PUSHGATEWAY));
        env.execute("example");
    }

    static class MyPushgatewaySinkFunction extends PushgatewayBaseSinkFunction<MyData> {
        public MyPushgatewaySinkFunction(String pushgateway) {
            super(pushgateway);
        }

        public void constructPoint(MyData myData) {
            TreeMap<String, String> tagsMap = new TreeMap<>();
            tagsMap.put("tagK1", myData.tagV1);
            PushgatewayGaugeSinkEntity gaugeSinkEntity = new PushgatewayGaugeSinkEntity("job",
                    myData.metric, (double) myData.value, tagsMap);
            pushGauge(gaugeSinkEntity);
        }
    }

    static class MyData implements Serializable {
        private final String metric;
        private final long timeStamp;
        private final int value;
        private final String tagV1;

        public MyData(String metric, long timeStamp, int value, String tagV1) {
            this.metric = metric;
            this.timeStamp = timeStamp;
            this.value = value;
            this.tagV1 = tagV1;
        }

        public String getMetric() {
            return this.metric;
        }

        public long getTimeStamp() {
            return this.timeStamp;
        }

        public int getValue() {
            return this.value;
        }

        public String getTagV1() {
            return this.tagV1;
        }
    }
}
