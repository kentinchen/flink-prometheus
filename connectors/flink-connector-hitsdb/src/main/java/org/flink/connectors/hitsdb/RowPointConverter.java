package org.flink.connectors.hitsdb;

import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import org.apache.flink.types.Row;

import java.io.Serializable;

public interface RowPointConverter extends Serializable {
    AbstractPoint convert(Row paramRow);
}
