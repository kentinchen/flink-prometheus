package org.flink.udf.enums;

public enum KvStoreEnum {
    AVGRT("AvgRt", "AvgRT"),
    HITRATE("HitRate", "hit_rate");

    private final String newCode;
    private final String oldCode;

    KvStoreEnum(String newCode, String oldCode) {
        this.newCode = newCode;
        this.oldCode = oldCode;
    }

    public static String getOldCode(String m) {
        for (KvStoreEnum e : values()) {
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
