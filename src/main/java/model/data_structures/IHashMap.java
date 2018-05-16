package model.data_structures;

import java.io.Serializable;
import java.util.Iterator;

public interface IHashMap<K extends Comparable<K>,V> extends Serializable{

	/**
	 * Adds a new key-value entry to the hash table
	 * @param key
	 * @param value
	 * @throws Exception if the key is null
	 */
	void put(K key, V value) throws Exception;

	/**
	 * Searches for the given key in the hash table
	 * @param key
	 * @return value
	 * @throws Exception if the key or the value are null
	 */
	V get(K key) throws Exception;

	/**
	 * Eliminates the element given by the key and returns it,
	 * @param key
	 * @return value
	 * @throws Exception if the key is null
	 */
	V delete(K key) throws Exception;

	/**
	 * Returns an iterator for all the keys in the table
	 * @return iterator
	 */
	Iterator<K> keys();

	LinkedList<K> toList();

	/**
	 * Checks if the key exists in the hash table
	 * @param key
	 * @return true if the key exists, false otherwise
	 */
	boolean containsKey(K key);

	/**
	 * Checks if the table is empty
	 * @return true if it´s empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the number of entries in the hash map
	 * @return size
	 */
	int size();
	
}
