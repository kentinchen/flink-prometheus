package com.github.mbode.flink_prometheus_example;

import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Histogram;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FlinkMetricsExposingMapFunctionTest {
    private static final Integer TEST_VALUE = 42;
    @InjectMocks
    private final FlinkMetricsExposingMapFunction flinkMetricsExposingMapFunction =
            new FlinkMetricsExposingMapFunction();
    @Mock
    private Counter eventCounter;
    @Mock
    private Histogram valueHistogram;

    @Test
    void mapActsAsIdentity() {
        assertThat(flinkMetricsExposingMapFunction.map(TEST_VALUE)).isEqualTo(TEST_VALUE);
    }

    @Test
    void eventsAreCounted() {
        flinkMetricsExposingMapFunction.map(TEST_VALUE);
        Mockito.verify(eventCounter).inc();
    }

    @Test
    void valueIsReportedToHistogram() {
        flinkMetricsExposingMapFunction.map(TEST_VALUE);
        Mockito.verify(valueHistogram).update(TEST_VALUE);
    }
}
