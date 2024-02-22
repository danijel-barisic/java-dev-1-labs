package hr.fer.oprpp1.custom.collections;

/**
 * Adapter class that adapts ArrayIndexedCollection into a map-like data structure.
 *
 * @param <K> key type of an entry
 * @param <V> value type of an entry
 * @Author Danijel Barišić
 */
public class Dictionary<K, V> {

    /**
     * Dictionary entry model.
     *
     * @param <K> key
     * @param <V> value
     */
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Internal entry array.
     */
    private ArrayIndexedCollection<Entry<K, V>> entries;

    /**
     * Initialises entry array.
     */
    public Dictionary() {
        entries = new ArrayIndexedCollection<>();
    }

    /**
     * @return true if no entries are in dictionary, false otherwise
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * @return number of entries in the dictionary
     */
    public int size() {
        return entries.size();
    }

    /**
     * Removes all elements from the dictionary.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Adds an entry into a dictionary or changes an existing one.
     *
     * @param key   key of the entry
     * @param value value of the entry
     * @return old value of the entry with the given key
     * @throws NullPointerException when provided key is null
     */
    public V put(K key, V value) {

        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        ElementsGetter<Entry<K, V>> eg = entries.createElementsGetter();

        Entry<K, V> e;
        while (eg.hasNextElement()) {
            e = eg.getNextElement();

            if (e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;

                return oldValue;
            }
        }

        entries.add(new Entry<>(key, value));
        return null;
    }

    /**
     * Gets the entry with the provided key.
     *
     * @param key key of entry to get
     * @return value of the entry with the given key
     */
    public V get(Object key) {

        ElementsGetter<Entry<K, V>> eg = entries.createElementsGetter();

        Entry<K, V> e;
        while (eg.hasNextElement()) {
            e = eg.getNextElement();

            if (e.key.equals(key)) {
                return e.value;
            }
        }

        return null;
    }

    /**
     * Removes the entry with the given key
     *
     * @param key key of entry to remove
     * @return value of the removed entry
     */
    public V remove(K key) {

        Entry<K, V> e;
        for (int i = 0; i < entries.size(); i++) {
            e = entries.get(i);

            if (e.key.equals(key)) {
                entries.remove(e);
                return e.value;
            }
        }
        return null;
    }

}
