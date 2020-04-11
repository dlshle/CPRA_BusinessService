package org.wisc.business.cache;

@Deprecated
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V val);
}
