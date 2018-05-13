package model.data_structures;

import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.util.Iterator;

public class LinearProbingHashMap<K extends Comparable<K>,V> implements IHashMap<K,V>{

    private static final int DEFAULT_CAPACITY = 31;

    private static final double MAX_CHARGING_FACTOR = 0.5;

    private int capacity;

    private int numEntries;

    private Entry<K,V>[] entries;

    public LinearProbingHashMap(K defaultK, V defaultV){
        this(DEFAULT_CAPACITY);
    }

    public LinearProbingHashMap(int capacity){
        this.capacity = capacity;
        numEntries = 0;
        entries = (Entry<K,V>[])new Entry[capacity];
    }


    @Override
    public void put(K key, V value) {

        if (value == null){
            delete(key);
            return;
        }

        if (calculateChargingFactor() > MAX_CHARGING_FACTOR){
            resize(2);
        }

        if(key != null){
            int i = getHash(key);
            while (entries[i] != null){
                if (entries[i].getKey().equals(key)){
                    entries[i].setValue(value);
                    numEntries++;
                    return;
                }
                i = (i + 1)%capacity;
            }
            entries[i] = new Entry<>(key, value);
            numEntries++;
        }

        else{
            throw new IllegalArgumentException("The key cannot be null");
        }
    }

    public double calculateChargingFactor(){
        return (numEntries + 0.0) / capacity;
    }

    @Override
    public V get(K key) throws Exception{
        if(key != null){
            for (int i = getHash(key); entries[i] != null; i = (i + 1)%capacity){
                if(entries[i].getKey() == null){

                }
                if(entries[i].getKey().equals(key)){
                    return entries[i].getValue();
                }
            }
        }else{
            throw new IllegalArgumentException("The key cannot be null");
        }
        throw  new Exception("The key doesn´t exists, the value shouldn´t be null");
    }

    @Override
    public V delete(K key) {
        V deleted = null;
        if(key != null && containsKey(key)){
            //find location
            int location = getHash(key);
            for (; !entries[location].getKey().equals(key); ){
                location = (location + 1) % capacity;
            }
            deleted = entries[location].getValue();
            entries[location] = null;
            numEntries--;

            //rearrange
            location = (location + 1) % capacity;
            for (; entries[location] != null; location = (location + 1)%capacity){
                K tempKey = entries[location].getKey();
                V tempValue = entries[location].getValue();
                entries[location] = null;
                numEntries--;
                put(tempKey, tempValue);
            }

        }else throw new IllegalArgumentException("The key shouldn't be null");
        return deleted;
    }

    @Override
    public Iterator<K> keys() {
        List<K> list = new List();
        for (int i = 0; i<entries.length; i++){
            if(entries[i] != null){
                list.add(entries[i].getKey());
            }
        }
        return new ListIterator<>(list);
    }

    @Override
    public boolean containsKey(K key) {
        try {
            get(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    public int size(){
        return entries.length;
    }

    public int getHash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    private void resize(double factor) {
        int size = entries.length;
        capacity *= factor;
        LinearProbingHashMap<K,V> copy = new LinearProbingHashMap<>(capacity);
        for (int i = 0; i < size; i++) {
            copy.getEntries()[i] = entries[i];
        }

        setEntries(copy.getEntries());
    }

    public void setEntries(Entry<K,V>[] entries){
        this.entries = entries;
    }

    public Entry<K,V>[] getEntries(){
        return entries;
    }
}
