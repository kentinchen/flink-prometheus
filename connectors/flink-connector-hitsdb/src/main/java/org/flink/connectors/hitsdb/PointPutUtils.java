package org.flink.connectors.hitsdb;

import com.aliyun.hitsdb.client.TSDB;
import com.aliyun.hitsdb.client.exception.http.HttpUnknowStatusException;
import com.aliyun.hitsdb.client.value.request.AbstractPoint;
import com.aliyun.hitsdb.client.value.request.MultiFieldPoint;
import com.aliyun.hitsdb.client.value.request.Point;
import com.aliyun.hitsdb.client.value.response.batch.IgnoreErrorsResult;
import com.aliyun.hitsdb.client.value.response.batch.MultiFieldIgnoreErrorsResult;

public class PointPutUtils {
    public static void putPoint(TSDB tsdb, AbstractPoint point, boolean sync, boolean ignoreWriteError) {
        if (point == null)
            return;
        try {
            switch (point.getPointType()) {
                case SINGLE_VALUE:
                    if (sync) {
                        if (ignoreWriteError) {
                            tsdb.putSync(IgnoreErrorsResult.class, (Point) point);
                        } else {
                            tsdb.putSync((Point) point);
                        }
                    } else {
                        tsdb.put((Point) point);
                    }
                    return;
                case MULTI_FIELD:
                    if (sync) {
                        if (ignoreWriteError) {
                            tsdb.multiFieldPutSync((MultiFieldPoint) point, MultiFieldIgnoreErrorsResult.class);
                        } else {
                            tsdb.multiFieldPutSync((MultiFieldPoint) point);
                        }
                    } else {
                        tsdb.multiFieldPut((MultiFieldPoint) point);
                    }
                    return;
            }
            throw new IllegalStateException("unsupported point type: " + point.getPointType());
        } catch (HttpUnknowStatusException e) {
            if (!ignoreWriteError)
                throw e;
        }
    }
}
