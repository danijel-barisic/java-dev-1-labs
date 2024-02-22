package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest {

    ArrayIndexedCollection dummyArray;

    @BeforeEach
    public void initArray() {

        dummyArray = new ArrayIndexedCollection();
        dummyArray.add(4);
        dummyArray.add(17);
        dummyArray.add(3);
    }

    @Test
    public void testSuccessfulInit() {
        Integer[] expected = {4, 17, 3};
        assertArrayEquals(expected, dummyArray.toArray());
    }

    @Test
    public void testDefaultConstructorShouldCreateCollectionOfSizeZero() {

        ArrayIndexedCollection newArray = new ArrayIndexedCollection();
        assertEquals(0, newArray.size());
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForInitialCapacityLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
    }

    @Test
    public void testConstructorShouldCopyCollection() {

        ArrayIndexedCollection copyOfArray = new ArrayIndexedCollection(dummyArray);
        assertArrayEquals(dummyArray.toArray(), copyOfArray.toArray());
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionWhenNullIsPassedForOtherCollection() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 16));
    }

    @Test
    public void testAddMethodShouldAddNewElement() {

        dummyArray.add(5);
        Integer[] expected = {4, 17, 3, 5};

        assertArrayEquals(expected, dummyArray.toArray());
    }

    @Test
    public void testAddMethodThrowsNullPointerExceptionIfNullIsPassed() {
        assertThrows(NullPointerException.class, () -> dummyArray.add(null));
    }

    @Test
    public void testGetMethodShouldReturnElementAtSpecifiedIndex() {
        assertEquals(17, dummyArray.get(1));
    }

    @Test
    public void testGetMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsLessThanZero() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.get(-1));
    }

    @Test
    public void testGetMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsGreaterThanSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.get(dummyArray.size() + 1));
    }

    @Test
    public void testClearMethodSetsArraySizeToZero() {

        dummyArray.clear();
        assertEquals(0, dummyArray.size());
    }

    @Test
    public void testInsertMethodInsertsElementAtSpecifiedIndex() {

        dummyArray.insert(25, 2);
        Integer[] expected = {4, 17, 25, 3};
        assertArrayEquals(expected, dummyArray.toArray());
    }

    @Test
    public void testInsertMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsLessThanZero() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.insert(25, -1));
    }

    @Test
    public void testInsertMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsGreaterThanSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.insert(25, dummyArray.size() + 1));
    }

    @Test
    public void testIndexOfMethodDoesNotFindNullElement() {
        assertEquals(-1, dummyArray.indexOf(null));
    }

    @Test
    public void testIndexOfMethodReturnsIndexOfSpecifiedElement() {
        assertEquals(1, dummyArray.indexOf(17));
    }

    @Test
    public void testRemoveMethodRemovesElementAtSpecifiedIndex() {

        dummyArray.remove(1);
        Integer[] expected = {4, 3};
        assertArrayEquals(expected, dummyArray.toArray());
    }

    @Test
    public void testRemoveMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsLessThanZero() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.remove(-1));
    }

    @Test
    public void testRemoveMethodThrowsIndexOutOfBoundsExceptionWhenIndexIsGreaterThanSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> dummyArray.remove(dummyArray.size() + 1));
    }

    @Test
    public void testRemoveMethodRemovesSpecifiedObject() {
        dummyArray.remove((Integer) 17);
        Integer[] expected = {4, 3};
        assertArrayEquals(expected, dummyArray.toArray());
    }

}
