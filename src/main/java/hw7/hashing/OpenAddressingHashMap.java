package hw7.hashing;

import static java.util.Objects.hash;

import hw7.Map;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {
    // Maximum load factor permitted before rehashing
    private static final double THRESHOLD = 0.6;

    // Types of probing
    // 1: Linear Probing
    // 2: Quadratic Probing
    // 3: Double Probing
    private static final int PROBE_MODE = 1;

    // List of prime numbers for updating capacity in rehashing
    private static final int[] PRIMES_LIST = {2, 5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203,
            6421, 12853, 25717, 51437, 102877, 205759, 411527, 823117, 1646237, 3292489, 6584983, 13169977};

    // Array of Nodes holding map data
    private Node<K, V>[] hashTable;

    // Number of key/value pairs in the map
    private int numElements;

    // Index of which prime number in PRIMES_LIST we are using for the current capacity
    private int capacityIndex;

    // Maximum number of key/value pairs our map can hold
    private int capacity;

    /**
     * Instantiate an OpenAddressing HashMap.
     */
    public OpenAddressingHashMap() {
        // Capacity set to the first prime number
        capacityIndex = 0;
        capacity = PRIMES_LIST[capacityIndex];

        // Instantiate empty array of Nodes
        hashTable = (Node<K, V>[]) Array.newInstance(Node.class, capacity);
        numElements = 0;
    }

    @Override
    public void insert(K k, V v) throws IllegalArgumentException {
        // Check if key is either null or not present in the map
        if (k == null || has(k)) {
            throw new IllegalArgumentException();
        }

        Node<K, V> newNode = new Node<>(k, v);

        // Pick the index to try to place Node
        // Probe until there is a free place to insert
        int index = findPositiveHashCode(k) % capacity;

        for (int i = 0; i < capacity; i++) {
            Node<K, V> entry = hashTable[index];
            if (entry == null || entry.isTombstone) {
                hashTable[index] = newNode;
                numElements++;
                rehash();
                return;
            }
            index = (index + findProbeInterval(i, k, PROBE_MODE)) % capacity;
        }
    }

    @Override
    public V remove(K k) throws IllegalArgumentException {
        // Check if the key is null
        if (k == null) {
            throw new IllegalArgumentException();
        }


        Node<K, V> targetNode = findNodeByKey(k);

        // If a matching Node is found, set it to a tombstone
        if (targetNode != null) {
            targetNode.isTombstone = true;
            numElements--;
            return targetNode.value;
        }

        throw new IllegalArgumentException();
    }


    @Override
    public void put(K k, V v) throws IllegalArgumentException {
        // Check if the key is null
        if (k == null) {
            throw new IllegalArgumentException();
        }

        Node<K, V> targetNode = findNodeByKey(k);

        // If a matching Node is found, edit its value
        if (targetNode != null) {
            targetNode.value = v;
            return;
        }

        throw new IllegalArgumentException();

    }

    @Override
    public V get(K k) throws IllegalArgumentException {
        // Check if the key is null
        if (k == null) {
            throw new IllegalArgumentException();
        }

        Node<K, V> targetNode = findNodeByKey(k);

        // If a matching Node is found, return its value
        if (targetNode != null) {
            return targetNode.value;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public boolean has(K k) {
        return (k != null && findNodeByKey(k) != null);
    }

    // Search for a Node with a matching key in the hash table
    // Return the Node if it can be found, or return null
    private Node<K, V> findNodeByKey(K key) {
        // Pick the index to search for the key
        // Probe until there is a matching Node
        int index = findPositiveHashCode(key) % capacity;

        for (int i = 0; i < capacity; i++) {
            Node<K, V> entry = hashTable[index];
            if (entry != null && !entry.isTombstone) {
                if (((hashTable[index].key).equals(key))) {
                    return entry;
                }
            }
            index = (index + findProbeInterval(i, key, PROBE_MODE)) % capacity;
        }

        // Return null if there is no match
        return null;

    }

    // Rehash the array of nodes into a larger array when it becomes
    // too full
    private void rehash() {
        // Check if our load factor exceeds the threshold
        double loadFactor = (double) numElements / capacity;
        if (loadFactor >= THRESHOLD) {
            int oldCapacity = capacity;

            // If there are additional unused primes, go to the next one
            // Otherwise, multiplies the existing capacity by 2
            if (capacityIndex < PRIMES_LIST.length - 1) {
                capacityIndex++;
                capacity = PRIMES_LIST[capacityIndex];
            } else {
                capacity *= 2;
            }

            // Create a new array with the new capacity
            // Copy the old data into the new array
            Node<K, V>[] tmp = (Node<K, V>[]) Array.newInstance(Node.class, capacity);
            for (int i = 0; i < oldCapacity; i++) {
                Node<K, V> entry = hashTable[i];
                if (entry != null && !entry.isTombstone) {
                    tmp[i] = entry;
                }
            }
            hashTable = tmp;
        }
    }

    // Calculate the step interval to the next index
    // Take the current index, the key, and the probing setting
    private int findProbeInterval(int curIndex, K key, int mode) {
        switch (mode) {
            case 1:
                return 1;
            case 2:
                return curIndex * curIndex;
            default:
                return hash(key) * curIndex;
        }
    }

    // Return a non-negative hash code for a key object
    private int findPositiveHashCode(K key) {
        return Math.abs(key.hashCode());
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {
        // Current bucket index
        private int index;

        // Number of key/value pairs already visited
        private int count;

        HashMapIterator() {
            index = 0;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            // Return whether there are unvisited Nodes
            return (count < numElements);
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Advance until the next valid bucket
            while (hashTable[index] == null || hashTable[index].isTombstone) {
                index++;
            }

            // Get the key value of the current Node
            K key = hashTable[index].key;

            // Advance to the next bucket
            index++;
            count++;
            return key;
        }
    }

    // Class to hold key/value pairs
    private static class Node<K, V> {
        K key;
        V value;

        // Whether the key/value pair is a tombstone (i.e. no longer valid)
        boolean isTombstone;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            isTombstone = false;
        }
    }
}
