package model.data_structures;


import java.util.Iterator;

public class SeparateChainingHashMap<K extends Comparable<K>,V> implements IHashMap<K, V> {

	/**
	 * Default size of the array if the user doesn´t specifies it
	 */
	private static final int DEFAULT_CAPACITY = 31;

	/**
	 * Maximum charging factor allowed (numKeys / totalCapacity)
	 */
	public static final double MAX_CHARGING_FACTOR = 0.6;

	/**
	 * Size of the array
	 */
	private int capacity;

	/**
	 * Total number of key-value entries
	 */
	private int numEntries;

	/**
	 * Structure of the hash table
	 */
	private LinkedList<Entry<K, V>>[] entries;

	/**
	 * Constructs a Hash table with the defoult capacity
	 */
	public SeparateChainingHashMap() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructs a hash table with the specified capacity
	 * @param capacity
	 */
	public SeparateChainingHashMap(int capacity) {
		entries = (LinkedList<Entry<K,V>>[])new List[capacity];
		this.capacity = capacity;
		numEntries = 0;
		initializeLists();
	}

	/**
	 * Initializes all the linked lists within the array
	 */
	private void initializeLists() {
		for (int i = 0; i < capacity; i++) {
			entries[i] = new List<Entry<K,V>>();
		}
	}

	public int size() {
		return numEntries;
	}

	/**
	 * Returns the hash table
	 * @return entries
	 */
	public LinkedList<Entry<K, V>>[] getEntries(){
		return entries;
	}

	/**
	 * Sets the entries of the hash table
	 * @param entries
	 */
	public void setEntries(LinkedList<Entry<K, V>>[] entries){this.entries = entries;}

	/**
	 * Adds a new key-value entry to the hash table
	 * @param key
	 * @param value
	 * @throws Exception if the key is null
	 */
	@Override
	public void put(K key, V value) throws Exception{

		if(value == null){
			delete(key);
		}
		if(key != null) {
			LinkedList<Entry<K, V>> list = entries[getHash(key)];
			list.listing();
			Entry<K, V> e = new Entry(key, value);

			if (list.isEmpty()) {
				list.add(e);
				numEntries ++;
			} else {
				boolean found = false;
				for (int i = 0; i < list.size() && !found; i++) {
					Entry<K, V> curr = list.getCurrent();
					if (curr.getKey().equals(key)) {
						curr.setValue(value);
						found = true;
					}
					list.next();
				}
				if (!found) {
					list.add(e);
					numEntries++;
				}
			}
			if ( calculateChargingFactor() > MAX_CHARGING_FACTOR) {
				resize(2);
			}
		}else{
			throw new IllegalArgumentException("The key cannot be null");
		}
	}

	/**
	 * Calculates the current occupation percentage
	 * @return charging factor
	 */
	public double calculateChargingFactor(){
		return (numEntries + 0.0) / capacity;
	}

	/**
	 * Rehashes the table according to a given factor if the charging factor is surpassed
	 * @param factor factor to be multiplied by the capacity
	 * @throws Exception if the key is null
	 */
	private void resize(double factor) throws Exception{
		int newCapacity = (int)(capacity * factor);
		SeparateChainingHashMap<K,V> copy = new SeparateChainingHashMap<>(newCapacity);
		Iterator iter = keys();
		while (iter.hasNext()){
			K key = (K)iter.next();
			V val = get(key);
			copy.put(key, val);
		}
		capacity = newCapacity;
		setEntries(copy.getEntries());
	}

	/**
	 * Searches for the given key in the hash table
	 * @param key
	 * @return value
	 * @throws Exception if the key or the value are null
	 */
	@Override
	public V get(K key) throws Exception {
		V val = null;

		if(key != null){
			int location = getHash(key);
			LinkedList<Entry<K, V>> list = entries[location];
			list.listing();
			for (int i = 0; i < list.size() && val == null; i++) {
				Entry<K,V> curr = list.getCurrent();
				if (curr.getKey().equals(key)) {
					val = curr.getValue();
				}
				list.next();
			}
		}else{
			throw new IllegalArgumentException("The key cannot be null");
		}

		if(val == null){
			throw new Exception("The key doesn´t exist, value cannot be null");
		}

		return val;
	}

	public LinkedList<K> toList(){
		List<K> list = new List();
		for (int i = 0; i<entries.length; i++){
			entries[i].listing();
			for (int j = 0; j  < entries[i].size(); j++){
				Entry<K,V> entry = entries[i].getCurrent();
				list.add(entry.getKey());
				entries[i].next();
			}
		}
		list.listing();
		return list;
	}

	/**
	 * Returns an iterator for all the keys in the table
	 * @return iterator
	 */
	@Override
	public Iterator<K> keys() {
		return new ListIterator<>(toList());
	}

	/**
	 * Checks if the key exists in the hash table
	 * @param key
	 * @return true if the key exists, false otherwise
	 */
	@Override
	public boolean containsKey(K key) {
		try {
			return get(key) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Eliminates the element given by the key and returns it,
	 * @param key
	 * @return value
	 * @throws Exception if the key is null
	 */
	@Override
	public V delete(K key) throws Exception{
		
		LinkedList<Entry<K, V>> list = entries[getHash(key)];
		Entry<K, V> removed = list.delete(new Entry<K,V>(key, null));
		V value;
		if(removed != null){
			numEntries--;
			value = removed.getValue();
		}else{
			value = null;
		}

 		return value;
	}


	/**
	 * Returns the position in the array for a given key using a hash function
	 * @param key
	 * @return position in array
	 */
	public int getHash(K key) {

		return (key.hashCode() & 0x7fffffff) % capacity;
	}
	
	public IArrayList<K> getKeys(){
		IArrayList<K> arr = new ArrayList<>();
		for (int i = 0; i < entries.length; i++) {
			LinkedList<Entry<K, V>> l = entries[i];
			l.listing();
			for (int j = 0; j < l.size(); j++) {
				arr.add(l.getCurrent().getKey());
				l.next();
			}
		}
		return arr;
	}

	/**
	 * Checks if the table is empty
	 * @return true if it´s empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return entries.length == 0;
	}

}
