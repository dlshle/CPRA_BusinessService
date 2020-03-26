package org.wisc.business;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wisc.business.cache.LRUCache;

import java.util.LinkedList;
import java.util.List;

public class LRUCacheTest {
    LRUCache cache;
    List<Integer> keySequence;
    List<Integer> valSequence;

    @BeforeClass
    public void prepareSequences() {
        keySequence = new LinkedList<>();
    }

    @Test
    public void testCapacity() {
        cache = new LRUCache(3);

    }

}
