package org.flink.connectors.hitsdb.sink;

import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import com.aliyun.hitsdb.client.value.request.Point;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HiTSDBSinkExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<MyData> dataStreamSource = env.fromCollection(Arrays.asList(
                new MyData("a", System.currentTimeMillis(), 3, "v1"),
                new MyData("b", System.currentTimeMillis(), 5, "v2")));
        dataStreamSource.addSink(new MyHiTSDBSinkFunction("h1"));
        env.execute("example");
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

    static class MyHiTSDBSinkFunction extends HiTSDBBaseSinkFunction<MyData> {
        public MyHiTSDBSinkFunction(String host) {
            super(host);
        }

        AbstractPoint constructPoint(MyData myData) {
            Map<String, String> tagsMap = new HashMap<>();
            tagsMap.put("tagK1", myData.tagV1);
            Point point = Point.metric(myData.metric).timestamp(myData.timeStamp).value(Integer.valueOf(myData.value)).tag(tagsMap).build(true);
            return point;
        }
    }
}
