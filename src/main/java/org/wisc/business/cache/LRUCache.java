package org.wisc.business.cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LRUCache<K, V> implements Cache<K, V>{
    private class CacheNode {
        CacheNode pre;
        CacheNode next;
        K k;
        V v;
        public CacheNode(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    // dummy head and tail are used to index real head and tail
    private CacheNode head;
    private CacheNode tail;
    private ConcurrentHashMap<K, CacheNode> map;
    private int maxSize;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        this.map = new ConcurrentHashMap<K, CacheNode>(maxSize);
        head = new CacheNode(null, null);
        tail = new CacheNode(null, null);
        head.next = tail;
        tail.pre = head;
    }

    private synchronized void unlink(CacheNode node) {
        node.next.pre = node.pre;
        node.pre.next = node.next;
        node.pre = null;
        node.next = null;
    }

    private synchronized void makeHead(CacheNode node) {
        CacheNode next = head.next;
        node.next = next;
        node.pre = head;
        next.pre = node;
        head.next = node;
    }

    private synchronized void removeTail() {
        CacheNode target = tail.pre;
        CacheNode pre = target.pre;
        tail.pre = pre;
        pre.next = tail;
        target.pre = null;
        target.next = null;
        map.remove(target.k);
    }

    @Override
    public V get(K key) {
        if (!map.containsKey(key))
            return null;
        CacheNode cacheNode = map.get(key);
        // unlink the node and make it head
        unlink(cacheNode);
        makeHead(cacheNode);
        return cacheNode.v;
    }

    @Override
    public void put(K key, V val) {
        if (map.containsKey(key)) {
            // take out the cachenode
            unlink(map.get(key));
        }
        CacheNode cacheNode = new CacheNode(key, val);
        makeHead(cacheNode);
        map.put(key, cacheNode);
        if (map.size() > maxSize) {
            removeTail();
        }
    }
}
