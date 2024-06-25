package org.flink.udf.enums.cms;

import static org.flink.udf.constant.cms.ECSMetricConstant.*;

public enum ECSMetricEnum {
    /*ECS基础监控项，基础监控数据，无需安装插件即可查询监控数据。*/
    InternetInRate("InternetInRate", "公网流入带宽", "bit/s"),
    InternetOutRate("InternetOutRate", "公网流入带宽", "bit/s"),
    IntranetInRate("IntranetInRate", "私网流入带宽", "bit/s"),
    IntranetOutRate("IntranetOutRate", "私网流出带宽", "bit/s"),
    DiskReadBPS("DiskReadBPS", "系统磁盘总读BPS", "Bps"),
    DiskWriteBPS("DiskWriteBPS", "系统磁盘总写BPS", "Bps"),
    DiskReadIOPS("DiskReadIOPS", "系统磁盘读IOPS", "Count/Second"),
    DiskWriteIOPS("DiskWriteIOPS", "系统磁盘写IOPS", "Count/Second"),

    /*操作系统级别监控项*/
    cpu_total("cpu_total", "当前消耗的总CPU百分比", "%"),
    memory_usedutilization("memory_usedutilization", "内存使用率", "%"),
    load_1m("load_1m", "过去1分钟的系统平均负载，Windows操作系统没有此指标使用率", ""),
    load_5m("load_5m", "过去5分钟的系统平均负载，Windows操作系统没有此指标使用率", "%"),
    load_15m("load_15m", "过去15分钟的系统平均负载，Windows操作系统没有此指标使用率", "%"),
    diskusage_utilization("diskusage_utilization", "磁盘使用率", "%"),
    disk_readbytes("disk_readbytes", "磁盘每秒读取的字节数", "byte/s"),
    disk_writebytes("disk_writebytes", "磁盘每秒写入的字节数", "byte/s"),
    disk_readiops("disk_readiops", "磁盘每秒的读请求数量", "次/秒"),
    disk_writeiops("disk_writeiops", "磁盘每秒的写请求数量", "次/秒"),
    fs_inodeutilization("fs_inodeutilization", "inode使用率", "%"),

    /*错误监控项*/
    InvalidMetric("InvalidMetric", "无法解析的指标", "");

    ECSMetricEnum(String m, String h, String unit) {
    }

    public static ECSMetricEnum of(String org) {
        switch (org) {
            case VM_INTERNET_NETWORK_RX:
                return InternetInRate;
            case VM_INTERNET_NETWORK_TX:
                return InternetOutRate;
            case VM_MEMORY_UTILIZATION:
                return memory_usedutilization;
            case VM_DISK_UTILIZATION:
                return diskusage_utilization;
            case VM_DISK_IUSED_UTILIZATION:
                return fs_inodeutilization;
            case VM_DISK_IO_READ:
                return disk_readbytes;
            case VM_DISK_IO_WRITE:
                return disk_writebytes;
            case VM_TCP_COUNT_TOTAL:
            case VM_PROCESS_COUNT:
                return InvalidMetric; //TODO
            case VM_CPU_UTILIZATION:
                return cpu_total;
            case VM_LOAD_AVERAGE1:
                return load_1m;
            case VM_LOAD_AVERAGE5:
                return load_5m;
        }
        return InvalidMetric;
    }
}
