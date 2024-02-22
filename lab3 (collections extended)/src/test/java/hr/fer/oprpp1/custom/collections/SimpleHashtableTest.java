package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleHashtableTest {

    SimpleHashtable<String, Integer> dummyHashtable;

    @BeforeEach
    public void initDummyHashtable() {
        dummyHashtable = new SimpleHashtable<>();
        dummyHashtable.put("Marko", 4);
        dummyHashtable.put("Marija", 5);
        dummyHashtable.put("Danko", 3);
    }

    @Test
    public void testConstructorThrowsIllegalArgExceptionForCapLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, Integer>(0));
    }

    @Test
    public void testHashtableContainsNumberOneUnderNameIvan() {
        SimpleHashtable<String, Integer> grades = new SimpleHashtable<>();
        grades.put("Ivan", 1);
        assertEquals((Integer) 1, grades.get("Ivan"));
    }

    @Test
    public void testContainsDanko() {
        assertTrue(dummyHashtable.containsKey("Danko"));
    }

    @Test
    public void testDankoIsRemoved() {
        dummyHashtable.remove("Danko");
        assertTrue(!dummyHashtable.containsKey("Danko"));
    }

    @Test
    public void testIterate() {
        int size = 0;
        for (SimpleHashtable.TableEntry<String, Integer> entry : dummyHashtable) {
            size++;
        }
        assertEquals(3, size);
    }

}
