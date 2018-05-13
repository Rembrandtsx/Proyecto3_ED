package model.data_structures;

import java.util.Comparator;

public interface IArrayList<T> {

	void add(T e);

	T get(int index);
	
	T remove(int index);
		
	int size();
	
	boolean isEmpty();
	
	void sort(Comparator<T> c, String order);
}
