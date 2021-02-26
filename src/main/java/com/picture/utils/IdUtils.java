package com.picture.utils;

public class IdUtils {
    /**
     * 单例对象
     */
    private volatile static IdUtils instance;

    /**
     * 起始的时间戳
     */
    private final static long twepoch = 1557825652094L;

    /**
     * 每一部分占用的位数
     */
    private final static long workerIdBits = 5L;
    private final static long datacenterIdBits = 5L;
    private final static long sequenceBits = 12L;

    /**
     * 每一部分的最大值
     */
    private final static long maxWorkerId = ~(-1L << workerIdBits);
    private final static long maxDatacenterId = ~(-1L << datacenterIdBits);
    private final static long maxSequence = ~(-1L << sequenceBits);

    /**
     * 每一部分向左的位移
     */
    private final static long workerIdShift = sequenceBits;
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    private final static long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;

    private static long datacenterId = 1; // 数据中心ID
    private static long workerId = 1; // 机器ID
    private static long sequence = 0L; // 序列号
    private static long lastTimestamp = -1L; // 上一次时间戳

    private IdUtils() {}

    public static IdUtils getInstance() {
        if (instance == null) {
            synchronized (IdUtils.class) {
                if (instance == null) {
                    instance = new IdUtils();
                }
            }
        }
        return instance;
    }
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0L) {
                timestamp = tilNextMillis();
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;

        return (timestamp - twepoch) << timestampShift // 时间戳部分
                | datacenterId << datacenterIdShift // 数据中心部分
                | workerId << workerIdShift // 机器标识部分
                | sequence; // 序列号部分
    }

    private long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
