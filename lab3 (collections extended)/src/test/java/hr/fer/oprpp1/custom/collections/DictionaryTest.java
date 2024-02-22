package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {

    Dictionary<String, Integer> dummyDict;

    @BeforeEach
    public void initDummyDict() {
        dummyDict = new Dictionary<>();
        dummyDict.put("Marko", 4);
        dummyDict.put("Marija", 5);
        dummyDict.put("Danko", 3);
    }

    @Test
    public void testContainsNumberOneUnderNameIvan() {
        Dictionary<String, Integer> grades = new Dictionary<>();
        grades.put("Ivan", 1);
        assertEquals((Integer) 1, grades.get("Ivan"));
    }

    @Test
    public void testNothingIsRemovedForNonexistentKey() {
        dummyDict.remove("Nocko");
        assertEquals(3, dummyDict.size());
    }

    @Test
    public void testSizeIsTwoAfterRemove() {
        dummyDict.remove("Danko");
        assertEquals(2, dummyDict.size());
    }

    @Test
    public void testMarijaHasGradeFive() {
        assertEquals((Integer)5, dummyDict.get("Marija"));
    }

}
