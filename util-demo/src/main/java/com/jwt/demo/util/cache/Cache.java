package com.jwt.demo.util.cache;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className Cache.java
 * @description
 * @createTime 2022年08月31日 10:40
 */

public class Cache<K, V> {
    private static final Logger LOG = Logger.getLogger(Cache.class.getName());
    private final ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<>();
    private final DelayQueue<DelayItem<Pair<K, V>>> delayQueue = new DelayQueue<>();
    private static final ExecutorService SINGLE_EXECUTE = ThreadUtil.newSingleExecutor();

    public Cache() {
        SINGLE_EXECUTE.execute(this::daemonCheck);
    }

    /**
     * 超时对象处理
     */
    private void daemonCheck() {
        for (; ; ) {
            try {
                DelayItem<Pair<K, V>> delayItem = delayQueue.take();
                // 超时对象处理
                Pair<K, V> pair = delayItem.getItem();
                cacheObjMap.remove(pair.first, pair.second);
            } catch (InterruptedException e) {
                if (LOG.isLoggable(Level.SEVERE)) {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
                break;
            }
        }
    }

    /**
     * 添加缓存对象
     *
     * @param key   key值
     * @param value value值
     * @param time  时间
     * @param unit  时间单位
     */
    public void put(K key, V value, long time, TimeUnit unit) {
        V oldValue = cacheObjMap.put(key, value);
        if (oldValue != null) {
            delayQueue.remove(key);
        }
        long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
        delayQueue.put(new DelayItem<>(new Pair<>(key, value), nanoTime));
    }

    public V get(K key) {
        return cacheObjMap.get(key);
    }
}
