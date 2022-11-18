package com.jwt.demo.util.cache;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className Pair.java
 * @description
 * @createTime 2022年08月31日 10:42
 */
public class Pair<K, V> {
    public K first;
    public V second;

    public Pair() {
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }
}
