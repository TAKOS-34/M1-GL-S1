package org.test.rmi;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests {
    @Test
    public void orderedDictionaryTest() {
        OrderedDictionary d = new OrderedDictionary();

        assertTrue(d.isEmpty());
        d.put("A", "A");
        d.put("B", "B");
        d.put("C", "C");
        assertFalse(d.isEmpty());
        assertEquals(3, d.size());
        assertEquals(d.get("A"), d.get("A"));
        assertTrue(d.containsKey("A"));
        assertFalse(d.containsKey("test"));
        assertEquals(0, d.indexOf("A"));
        assertEquals(3, d.newIndexOf("D"));
    }

    @Test
    public void fastDictionaryTest() {
        FastDictionary d = new FastDictionary();

        assertTrue(d.isEmpty());
        d.put("A", "A");
        assertEquals(2, d.keys.length);
        d.put("C", "C");
        assertEquals(4, d.keys.length);
        d.put("D", "D");
        assertEquals(4, d.keys.length);
        d.put("E", "E");
        assertFalse(d.isEmpty());
        assertEquals(8, d.keys.length);
        assertEquals(4, d.size());
        assertEquals(d.get("A"), d.get("A"));
        assertTrue(d.containsKey("A"));
        assertFalse(d.containsKey("test"));
    }

    @Test
    public void sortedDictionaryTest() {
        SortedDictionary d = new SortedDictionary();

        assertTrue(d.isEmpty());
        d.put("A", "A");
        d.put("C", "C");
        d.put("D", "D");
        assertFalse(d.isEmpty());
        assertEquals(3, d.size());
        assertEquals(d.get("A"), d.get("A"));
        assertTrue(d.containsKey("A"));
        assertFalse(d.containsKey("test"));
        assertEquals(0, d.indexOf("A"));
        d.put("B", "B");
        assertEquals(1, d.indexOf("B"));
        assertEquals(4, d.size());
    }
}