package hr.fer.oprpp1.custom.collections;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable collection of objects that employs hashing algorithm for fast access and adding of elements.
 *
 * @param <K> key type of an entry
 * @param <V> value type of an entry
 * @Author Danijel Barišić
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

    /**
     * Internal array for storing entry slots.
     */
    private TableEntry<K, V>[] table;

    /**
     * Number of stored entries.
     */
    private int size;

    /**
     * Counts the number of times the collection has been modified.
     */
    private long modificationCount;


    /**
     * Hashtable entry model.
     *
     * @param <K> key
     * @param <V> value
     */
    public static class TableEntry<K, V> {

        /**
         * Key of the entry.
         */
        private K key;

        /**
         * Value of the entry.
         */
        private V value;

        /**
         * Reference to the next entry in the linked list that's created in case of slot overflow.
         */
        private TableEntry<K, V> next;

        /**
         * @return key of the entry
         */
        public K getKey() {
            return key;
        }

        /**
         * @return value of the entry
         */
        public V getValue() {
            return value;
        }

        /**
         * Set value of the entry.
         */
        public void setValue(V value) {
            this.value = value;
        }

    }

    /**
     * Iterator for hashtable.
     *
     * @Author Danijel Barišić
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

        /**
         * Index of the table entry.
         */
        private int index = 0;

        /**
         * Number of entries iterated by the iterator so far.
         */
        private int entriesFetched = 0;

        /**
         * Initial number of modifications at the time of creation of this iterator.
         */
        private long savedModificationCount;

        /**
         * Initial number of entries at the time of creation of this iterator.
         */
        private long savedSize;

        /**
         * Tells whether remove operation is eligible, which is true only once after each next() is called.
         */
        private boolean canRemove = false;

        /**
         * Previously fetched entry.
         */
        private TableEntry<K, V> prevEntry;

        /**
         * Initialises modification count and size.
         */
        public IteratorImpl() {
            this.savedModificationCount = modificationCount;
            this.savedSize = size;
        }

        /**
         * @return true if there are entries left to iterate, false otherwise
         * @throws ConcurrentModificationException if hash table was modified in the meantime
         */
        @Override
        public boolean hasNext() {

            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("Hash table was modified in the meantime.");
            }
            return entriesFetched < savedSize;
        }

        /**
         * Iterates over next entry.
         *
         * @return next entry in the table.
         * @throws ConcurrentModificationException if hash table was modified in the meantime
         * @throws NoSuchElementException          if there are no more elements to iterate over
         */
        @Override
        public SimpleHashtable.TableEntry<K, V> next() {

            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("Hash table was modified in the meantime.");
            }

            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate over.");
            }

            TableEntry<K, V> currEntry;
            if (prevEntry != null) {
                if (prevEntry.next != null) {
                    currEntry = prevEntry.next;
                } else {
                    while (index < table.length && table[index] == null) index++;
                    currEntry = table[index++];
                }
            } else {
                while (table[index] == null) index++;
                currEntry = table[index++];
            }

            prevEntry = currEntry;
            entriesFetched++;

            canRemove = true;

            return currEntry;
        }

        /**
         * Removes element in a way safe from ConcurrentModificationException
         *
         * @throws ConcurrentModificationException if hash table was modified in the meantime
         * @throws IllegalStateException           if next() method isn't called before next remove() call
         */
        @Override
        public void remove() {

            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("Hash table was modified in the meantime.");
            }

            if (canRemove) {
                SimpleHashtable.this.remove(prevEntry.getKey());

                modificationCount++;
                savedModificationCount = modificationCount;

                canRemove = false;
            } else {
                throw new IllegalStateException("Cannot call remove() method before next() method is called.");
            }
        }
    }

    /**
     * Allocates space for 16 slots in the hashtable.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable() {
        table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, 16);
    }

    /**
     * Allocates space for initialCapacity number of entry slots in the hashtable.
     *
     * @param initialCapacity initial number of slots to allocate
     * @throws IllegalArgumentException when the provided initial capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int initialCapacity) {

        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity cannot be less than 1");
        }

        int pow;
        for (pow = 1; pow <= initialCapacity; pow *= 2) ;

        table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, pow);

    }

    /**
     * Adds new element into the hashtable or changes an existing one,
     * and doubles the internal table capacity when size/numOfSlots ratio is >= 0.75.
     *
     * @param key   new or existing key
     * @param value new value
     * @return old value
     * @throws NullPointerException when key is null
     */
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {

        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int rawIndex = key.hashCode() % table.length;
        int index = (rawIndex >= 0 ? rawIndex : -rawIndex);

        TableEntry<K, V> currEntry = table[index];

        if (currEntry != null) {
            do {
                if (currEntry.key.equals(key)) {
                    V oldValue = currEntry.value;
                    currEntry.value = value;

                    return oldValue;
                }
            } while ((currEntry = currEntry.next) != null);
        }

        if (((double) size) / table.length >= 0.75d) {
            TableEntry<K, V>[] newTable = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class,
                    table.length * 2);

            TableEntry<K, V>[] oldTable = this.toArray();

            table = newTable;
            size = 0;

            for (int i = 0; i < oldTable.length; i++) {
                this.put(oldTable[i].key, oldTable[i].value);
            }

        }

        rawIndex = key.hashCode() % table.length;
        index = (rawIndex >= 0 ? rawIndex : -rawIndex);

        // At this point, entry with provided key doesn't previously exist
        TableEntry<K, V> newEntry = new TableEntry<>();
        newEntry.key = key;
        newEntry.value = value;
        newEntry.next = null;

        if (table[index] != null) {
            currEntry = table[index];

            while (currEntry.next != null) {
                currEntry = currEntry.next;
            }

            currEntry.next = newEntry;
        } else {
            table[index] = newEntry;
        }

        size++;
        modificationCount++;

        return null;
    }

    /**
     * Gets value of the entry with a provided key.
     *
     * @param key key of entry whose value to get
     * @return value of the entry with a given key, or null if it doesn't exist
     */
    public V get(Object key) {

        int rawIndex = key.hashCode() % table.length;
        int index = (rawIndex >= 0 ? rawIndex : -rawIndex);

        TableEntry<K, V> currEntry = table[index];
        if (currEntry != null) {
            do {
                if (currEntry.key.equals(key)) {
                    return currEntry.value;
                }
            } while ((currEntry = currEntry.next) != null);
        }

        return null;
    }

    /**
     * @return number of stored entries
     */
    public int size() {
        return size;
    }

    /**
     * @param key key whose existence to check
     * @return true if key exists in the hashtable, false otherwise
     */
    public boolean containsKey(Object key) {

        int rawIndex = key.hashCode() % table.length;
        int index = (rawIndex >= 0 ? rawIndex : -rawIndex);

        TableEntry<K, V> currEntry = table[index];
        if (currEntry != null) {
            do {
                if (currEntry.key.equals(key)) {
                    return true;
                }
            } while ((currEntry = currEntry.next) != null);
        }

        return false;
    }

    /**
     * @param value value whose existence to check
     * @return true if value exists in the hashtable, false otherwise
     */
    public boolean containsValue(Object value) {

        for (int i = 0; i < table.length; i++) {

            TableEntry<K, V> currEntry = table[i];
            if (currEntry == null) {
                continue;
            }

            do {
                if (currEntry.value.equals(value)) {
                    return true;
                }
            } while ((currEntry = currEntry.next) != null);
        }

        return false;
    }

    /**
     * Removes an entry from the hash table.
     *
     * @param key key of the entry to remove
     * @return value of the removed entry
     */
    public V remove(Object key) {

        int rawIndex = key.hashCode() % table.length;
        int index = (rawIndex >= 0 ? rawIndex : -rawIndex);

        TableEntry<K, V> currEntry = table[index];
        if (currEntry != null) {
            if (currEntry.key.equals(key)) {  // head element
                V value = currEntry.value;

                table[index] = currEntry.next;

                size--;
                modificationCount++;

                return value;
            }

            do {
                TableEntry<K, V> prevEntry = currEntry;
                currEntry = currEntry.next;

                if (currEntry.key.equals(key)) {
                    V value = currEntry.value;
                    prevEntry.next = null;
                    size--;

                    modificationCount++;
                    return value;
                }
            } while (currEntry.next != null);
        }

        return null;
    }

    /**
     * @return true if no entries are in hash table, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return formatted hash table
     */
    public String toString() {
        boolean firstEntry = true;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < table.length; i++) {

            TableEntry<K, V> currEntry = table[i];
            if (currEntry == null) {
                continue;
            }

            do {
                if (!firstEntry) {
                    sb.append(", ");
                }

                sb.append(currEntry.key).append("=").append(currEntry.value);

                firstEntry = false;
            } while ((currEntry = currEntry.next) != null);
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * @return array containing the entries
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] elements = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, size);

        for (int i = 0, j = 0; i < table.length; i++) {

            TableEntry<K, V> currEntry = table[i];
            if (currEntry == null) {
                continue;
            }

            do {
                elements[j++] = currEntry;
            } while ((currEntry = currEntry.next) != null);
        }

        return elements;
    }

    /**
     * Removes all entries but doesn't change capacity.
     */
    public void clear() {

        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        size = 0;
        modificationCount++;
    }

    /**
     * @return iterator instance
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

}
