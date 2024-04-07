package org.flink.udf.enums;

import org.flink.udf.constant.Constants;

public enum RdsEnum {
    CPU(Constants.cpuusage, Constants.CpuUsage),
    MEM(Constants.memusage, Constants.MemoryUsage),
    DISK(Constants.disk_usage, Constants.DiskUsage),
    IOPS(Constants.iops_usage, Constants.IOPSUsage),
    CONN(Constants.conn_usage, Constants.ConnectionUsage),
    PG_DISK("local_fs_size_usage", "DiskUsage");

    private String newCode;
    private String oldCode;

    RdsEnum(String newCode, String oldCode) {
        this.newCode = newCode;
        this.oldCode = oldCode;
    }

    public static String getOldCode(String m) {
        for (RdsEnum e : values()) {
            if (e.getNewCode().equalsIgnoreCase(m))
                return e.getOldCode();
        }
        return m;
    }

    public String getNewCode() {
        return this.newCode;
    }

    public String getOldCode() {
        return this.oldCode;
    }
}
