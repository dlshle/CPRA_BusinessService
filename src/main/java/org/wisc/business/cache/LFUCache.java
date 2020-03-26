package org.wisc.business.cache;

import java.util.HashMap;
import java.util.Map;

public class LFUCache<K, V> implements Cache<K, V> {

    private Map<K, Node> map;
    private int capacity;

    // dummy nodes
    private FreqNode head;
    private FreqNode tail;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);

        this.head = new FreqNode();
        this.tail = new FreqNode();
        this.head.next = tail;
        this.tail.prev = head;
    }

    public V get(K key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }

        increaseFreq(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node node = map.get(key);

        if (node != null) {
            node.value = value;
            increaseFreq(node);
        } else if (capacity == 0) {
        } else {
            // evict
            if (map.size() >= capacity) {
                map.remove(this.head.next.removeLRU().key);
            }

            FreqNode freqNode = getOrBuildInitialFreqNode();
            node = new Node(key, value);
            freqNode.add(node);
            map.put(key, node);
        }
    }

    private FreqNode getOrBuildInitialFreqNode() {
        if (this.head.next.freq == 1) {
            return this.head.next;
        }

        return new FreqNode(1, head);
    }

    private void increaseFreq(Node node) {
        FreqNode nextFreqNode;
        if (node.freqNode.next.freq == node.freqNode.freq + 1) {
            nextFreqNode = node.freqNode.next;
        } else {
            FreqNode prev = node.freqNode;
            nextFreqNode = new FreqNode(prev.freq + 1, prev);
        }

        node.freqNode.remove(node);
        removeIfNecessary(node.freqNode);
        nextFreqNode.add(node);
    }

    private void removeIfNecessary(FreqNode node) {
        if (node.size <= 0) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    private class FreqNode {
        FreqNode prev;
        FreqNode next;
        int freq;
        int size;

        // LRU dummy nodes
        Node head;
        Node tail;

        FreqNode() {}

        FreqNode(int freq, FreqNode prev) {
            this.freq = freq;
            head = new Node();
            tail = new Node();

            head.next = tail;
            tail.prev = head;

            this.next = prev.next;
            prev.next = this;
            this.next.prev = this;
            this.prev = prev;
        }

        private void add(Node node) {
            node.freqNode = this;

            // add to the head of the list
            node.prev = head;
            node.next = head.next;

            head.next = node;
            node.next.prev = node;

            size++;
        }

        private void remove(Node node) {
            node.remove();
            size--;
        }

        private Node removeLRU() {
            Node toRemove = tail.prev;
            remove(toRemove);
            return toRemove;
        }
    }

    private class Node {
        K key;
        V value;
        Node prev;
        Node next;
        FreqNode freqNode;

        Node() {}

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private void remove() {
            prev.next = next;
            next.prev = prev;
        }
    }
}
