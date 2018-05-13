package model.data_structures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Entry <K extends Comparable<K>, V> implements Comparable <Entry<K, V>>{

	private K key;
	
	private V value;
	
	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public Entry(){

	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}
	public String saveJson() {
		String rta = "";
		Gson gson = new GsonBuilder().create();
		rta= gson.toJson(this);
		return rta;
		
	}
	
	@Override
	public boolean equals(Object o) {
		Entry e = (Entry)o;
		return this.key.equals(e.getKey());
	}

	@Override
	public int compareTo(Entry e) {
		return e.getKey().compareTo(key);

	}

}
