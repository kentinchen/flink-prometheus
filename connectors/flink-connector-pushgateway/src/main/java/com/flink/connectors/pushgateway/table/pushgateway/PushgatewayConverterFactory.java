package com.flink.connectors.pushgateway.table.pushgateway;

import com.flink.connectors.pushgateway.config.NotEnoughParamsException;
import com.flink.connectors.pushgateway.sink.pushgateway.PushgatewayGaugeEntity;
import com.flink.connectors.pushgateway.table.SchemaLifecycleAwareElementConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;

import java.util.*;

import static com.flink.connectors.pushgateway.config.ConnectorConfigConstants.*;
import static com.flink.connectors.pushgateway.table.pushgateway.PushgatewayDynamicSinkConnectorOptions.*;

@Slf4j
public class PushgatewayConverterFactory {

    public static SchemaLifecycleAwareElementConverter<RowData, ArrayList<PushgatewayGaugeEntity>> create(ReadableConfig tableOptions, DataType consumedDataType, SerializationSchema<RowData> serializationSchema) {
        RowType physicalRowType = (RowType) consumedDataType.getLogicalType();
        List<RowType.RowField> fields = physicalRowType.getFields();
        Map<Integer, String> tagsIdxMap = new HashMap<>();
        String jobName = tableOptions.getOptional(PROM_JOB).orElse(CONST_FLINK);
        String debugOpt = tableOptions.getOptional(PROM_DEBUG).orElse(CONST_F);
        boolean debug = debugOpt.equalsIgnoreCase(CONST_T);
        String modeOpt = tableOptions.getOptional(PROM_MODE).orElse(CONST_ROW);
        if (modeOpt.equals(CONST_ROW)) {
            return getSerializationSchemaElementRowConverter(tableOptions, serializationSchema, physicalRowType, fields, tagsIdxMap, jobName, debug);
        } else {
            return getSerializationSchemaElementColumnConverter(tableOptions, serializationSchema, physicalRowType, fields, tagsIdxMap, jobName, debug);
        }
    }

    // 行模式，指标名在metric列中
    private static SchemaLifecycleAwareElementConverter<RowData, ArrayList<PushgatewayGaugeEntity>> getSerializationSchemaElementRowConverter
    (ReadableConfig tableOptions, SerializationSchema<RowData> serializationSchema, RowType physicalRowType, List<RowType.RowField> fields,
     Map<Integer, String> tagsIdxMap, String jobName, boolean debug) {
        int nameIdx = -1;
        int helpIdx = -1;
        int valueIdx = -1;
        int tsIdx = -1;
        String metricName = tableOptions.getOptional(PROM_M_NAME).orElse(CONST_M);
        String metricValue = tableOptions.getOptional(PROM_V_NAME).orElse(CONST_V);
        if (debug) {
            log.info("PROM_M_NAME:" + metricName + " PROM_V_NAME:" + metricValue);
        }
        for (int i = 0; i < physicalRowType.getFieldCount(); i++) {
            String fieldName = fields.get(i).getName();
            if (debug) {
                log.info("i:" + i + " fieldName:" + fieldName + " type:" + fields.get(i).getType());
            }
            if (fieldName.equals(metricName)) {
                nameIdx = i;
            } else if (fieldName.equals(tableOptions.getOptional(PROM_H_NAME).orElse(CONST_H))) {
                helpIdx = i;
            } else if (fieldName.equals(metricValue)) {
                valueIdx = i;
            } else if (fieldName.equals(tableOptions.getOptional(PROM_TS_NAME).orElse(CONST_TS))) {
                tsIdx = i;
            } else {
                tagsIdxMap.put(i, fieldName);
            }
        }
        if (nameIdx < 0 || valueIdx < 0) {
            throw new NotEnoughParamsException("指标名(默认m)及指标值字段(默认v)不存在");
        }
        return new SerializationSchemaElementRowConverter(jobName, nameIdx, helpIdx, valueIdx, tsIdx, tagsIdxMap, serializationSchema);
    }


    // 列模式，指标名为列名
    private static SchemaLifecycleAwareElementConverter<RowData, ArrayList<PushgatewayGaugeEntity>> getSerializationSchemaElementColumnConverter
    (ReadableConfig tableOptions, SerializationSchema<RowData> serializationSchema, RowType physicalRowType, List<RowType.RowField> fields,
     Map<Integer, String> tagsIdxMap, String jobName, boolean debug) {
        int tsIdx = -1;
        String metricName = tableOptions.getOptional(PROM_M_NAME).orElse("");
        List<String> metricList = Arrays.asList(metricName.replaceAll("\\\\| +|\r|\n", "").split(","));
        Map<Integer, String> metricIdxMap = new HashMap<>();
        for (int i = 0; i < physicalRowType.getFieldCount(); i++) {
            String fieldName = fields.get(i).getName();
            if (debug) {
                log.info("i:" + i + " fieldName:" + fieldName + " type:" + fields.get(i).getType());
            }
            if (isColumnExist(metricList, fieldName)) {
                metricName = fieldName.replaceAll("-", "_").replaceAll("\\.", "_");
                metricIdxMap.put(i, metricName);
            } else if (fieldName.equals(tableOptions.getOptional(PROM_TS_NAME).orElse(CONST_TS))) {
                tsIdx = i;
            } else {
                tagsIdxMap.put(i, fieldName);
            }
        }
        if (metricIdxMap.isEmpty()) {
            throw new NotEnoughParamsException("列模式下，指标列不能为空");
        }
        if (debug) {
            for (Map.Entry<Integer, String> entry : metricIdxMap.entrySet()) {
                log.info("metricIdx:" + entry.getKey() + " metricName:" + entry.getValue());
            }
            for (Map.Entry<Integer, String> entry : tagsIdxMap.entrySet()) {
                log.info("tagsIdx:" + entry.getKey() + " tagName:" + entry.getValue());
            }
        }
        return new SerializationSchemaElementColumnConverter(jobName, tsIdx, metricIdxMap, tagsIdxMap, serializationSchema);
    }

    private static boolean isColumnExist(List<String> metricList, String column) {
        for (String elm : metricList) {
            if (elm.trim().equals(column)) {
                return true;
            }
        }
        return false;
    }
}
