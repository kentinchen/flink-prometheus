package com.github.mbode.flink_prometheus_example;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

class PrometheusIT {
    private static final String PROMETHEUS_URL =
            "http://" + System.getProperty("prometheus.host") + ":" + Integer.getInteger("prometheus.tcp.9090") + "/";

    private static boolean dataIsAvailableInPrometheusFor(String metricsName)
            throws UnirestException {
        final JSONArray resultArray =
                Unirest.get(PROMETHEUS_URL + "api/v1/query?query=" + metricsName)
                        .asJson()
                        .getBody()
                        .getObject()
                        .getJSONObject("data")
                        .getJSONArray("result");
        return resultArray.length() > 0;
    }

    @Test
    void genericFlinkMetricsAreAvailable() {
        await().until(() -> dataIsAvailableInPrometheusFor("flink_jobmanager_job_uptime"));
    }

    @Test
    void exampleJobMetricsAreAvailable() {
        await().atMost(1, TimeUnit.MINUTES).until(
                () -> dataIsAvailableInPrometheusFor("flink_taskmanager_job_task_operator_events")
                        && dataIsAvailableInPrometheusFor("flink_taskmanager_job_task_operator_value_histogram"));
    }
}
