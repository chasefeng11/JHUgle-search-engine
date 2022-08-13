package hw7.hashing;

import hw7.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {
  // Number of buckets
  private static final int NUM_ROWS = 3;

  // Array of buckets holding the map data
  private ArrayList<Node<K, V>>[] hashTable;

  // Number of key/value pairs in the map
  private int numElements;

  /**
   * Instantiate a Chaining HashMap.
   */
  public ChainingHashMap() {
    // Instantiate empty array of ArrayLists
    hashTable = (ArrayList<Node<K, V>>[]) new ArrayList[NUM_ROWS];
    for (int i = 0; i < NUM_ROWS; i++) {
      hashTable[i] = new ArrayList<>();
    }
    numElements = 0;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    // Checks if key is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    // Picks bucket to place Node
    int index = findPositiveHashCode(k) % NUM_ROWS;

    ArrayList<Node<K, V>> targetList = hashTable[index];
    Node<K, V> targetNode = findNodeByKey(targetList, k);

    // Checks if Node with a matching key already exists in bucket
    // Otherwise, adds the new one
    if (targetNode == null) {
      targetList.add(new Node<>(k, v));
      numElements++;
    } else {
      throw new IllegalArgumentException();
    }

  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    // Checks if key is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    // Picks bucket to look for Node
    int index = findPositiveHashCode(k) % NUM_ROWS;

    ArrayList<Node<K, V>> targetList = hashTable[index];
    Node<K, V> targetNode = findNodeByKey(targetList, k);

    // Removes Node if there is one with the specified key
    if (targetNode != null) {
      targetList.remove(targetNode);
      numElements--;
      return targetNode.value;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // Checks if key is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    // Picks bucket to look for Node
    int index = findPositiveHashCode(k) % NUM_ROWS;

    ArrayList<Node<K, V>> targetList = hashTable[index];
    Node<K, V> targetNode = findNodeByKey(targetList, k);

    // Edits Node if there is one with the specified key
    if (targetNode != null) {
      targetNode.value = v;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    // Checks if key is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    // Picks bucket to look for Node
    int index = findPositiveHashCode(k) % NUM_ROWS;

    ArrayList<Node<K, V>> targetList = hashTable[index];
    Node<K, V> targetNode = findNodeByKey(targetList, k);

    // Gets Node if there is one with the specified key
    if (targetNode != null) {
      return targetNode.value;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean has(K k) {
    // Checks if key is null
    if (k == null) {
      return false;
    }

    // Picks bucket to look for Node
    int index = findPositiveHashCode(k) % NUM_ROWS;
    ArrayList<Node<K, V>> targetList = hashTable[index];

    // Returns if there is a Node with a matching key in the bucket
    return (findNodeByKey(targetList, k) != null);
  }


  // Searches for a Node with the given key value in an ArrayList of Nodes
  // If there is a match, returns the given Node
  // Otherwise, returns null
  private Node<K, V> findNodeByKey(ArrayList<Node<K, V>> list, K key) {
    // Checks if the ArrayList is empty
    if (list.isEmpty()) {
      return null;
    }

    // Performs a linear search for the desired key
    for (Node<K, V> curNode : list) {
      if (key.equals(curNode.key)) {
        return curNode;
      }
    }

    return null;
  }

  // Returns a non-negative hash code for a key object
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
    // Current bucket number
    private int listIndex;

    // Current ArrayList index within the bucket
    private int subIndex;

    // Number of key/value pairs visited so far
    private int count;

    HashMapIterator() {
      listIndex = 0;
      subIndex = 0;
      count = 0;
    }

    @Override
    public boolean hasNext() {
      // Returns whether there are unvisited Nodes
      return (count < numElements);
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      // Advances to the next non-empty bucket
      if (hashTable[listIndex].isEmpty()) {
        listIndex++;
      }

      // Gets the key value of the current Node
      K curKey = hashTable[listIndex].get(subIndex).key;

      // Either advances to the next Node in the bucket or
      // advances to the next bucket if we are the final Node of the bucket
      if (subIndex < hashTable[listIndex].size() - 1) {
        subIndex++;
      } else {
        listIndex++;
        subIndex = 0;
      }

      count++;
      return curKey;
    }
  }

  // Class to hold key/value pairs
  private static class Node<K, V> {
    K key;
    V value;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
