package model.data_structures;

import java.util.Comparator;

public class ArrayList <T> implements IArrayList<T> {
	
	private static final int DEFAULT_CAPACITY = 16;
	
	public static final String ASCENDING = "ASCENDING";
	
	public static final String DESCENDING = "DESCENDING";
	
	private int maxSize = 0;
	
	private int size = 0;
	
	private Object[] arr;
	
	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayList(int capacity) {
		arr = new Object[capacity];
		size = 0;
		maxSize = capacity;
	}

	@Override
	public void add(Object e) {
		if (size >= maxSize) {
			resize();
		}
		arr[size] = e;
		size ++;		
	}

	private void resize() {
		maxSize *= 2;
		Object[] copy = new Object[size];
		for (int i = 0; i < size; i++) {
			copy[i] = (T) arr[i];
		}
		
		arr = new Object[maxSize];
		for (int i = 0; i < copy.length; i++) {
			arr[i] = (T) copy[i];
		}
	}

	@Override
	public T get(int index) {
		if (index > size) {
			throw new IndexOutOfBoundsException();
		}		
		return (T) arr[index];
	}

	@Override
	public T remove(int index) {
		if (index > size) {
			throw new IndexOutOfBoundsException();
		}
		T e = (T)arr[index];
		for (int i = index; i < size - 1; i++) {
			arr[i] = arr[i + 1];
		}

		arr[size -1] = null;
		size--;
		return e;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public void sort(Comparator<T> c, String order) {


		//sort(c, order, copy, 0, arr.length - 1);
		quickSort(c,0, size - 1, order);


	}
	
	private void sort(Comparator<T> c, String order, Object[] copy, int start, int end) {
		
		if(start > end) {
			return;
		}
		int mid = start + (end - start) / 2;
		sort(c, order, copy, start, mid);
		sort(c, order, copy, mid + 1, end);
		merge(copy, start, mid, end, c, order);
	}
	
	private void merge(Object[] copy, int start, int mid, int end, Comparator<T> c, String order) {
		int s = start;
		int t = mid + 1;
		
		for (int i = start; i < end; i++) {
			
			
	
			if(s > mid ) {
				arr[i] = copy[t];
				t++;
			}else if(t > end) {
				arr[i] = copy[s];
				s++;
			
			}
			else if((order.equals(ASCENDING) && c.compare((T)arr[t],(T) arr[s]) < 0) ||
					(order.equals(DESCENDING) && c.compare((T) arr[t],(T) arr[s]) > 0)) {
				arr[i] = copy[t];
				t++;
			}else {
				arr[i] = copy[s];
				s++;
			}
		}
	}

	private void swap(int i,int j)
	{
		T t = (T) arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}

	/* This function is same in both iterative and
       recursive*/
	private int partition (Comparator<T> c, int l, int h, String order)
	{
		T x = (T) arr[h];
		int i = (l - 1);

		for (int j = l; j <= h- 1; j++)
		{
			if ((order.equals(ASCENDING) && c.compare((T)arr[j], x)<= 0) || (order.equals(DESCENDING) && c.compare((T)arr[j], x)>= 0) )
			{
				i++;
				// swap arr[i] and arr[j]
				swap(i,j);
			}
		}
		// swap arr[i+1] and arr[h]
		swap(i+1,h);
		return (i + 1);
	}

	// Sorts arr[l..h] using iterative QuickSort
	private void quickSort(Comparator<T> c, int l, int h, String order)
	{
		// create auxiliary stack
		int stack[] = new int[h-l+1];

		// initialize top of stack
		int top = -1;

		// push initial values in the stack
		stack[++top] = l;
		stack[++top] = h;

		// keep popping elements until stack is not empty
		while (top >= 0)
		{
			// pop h and l
			h = stack[top--];
			l = stack[top--];

			// set pivot element at it's proper position
			int p = partition(c, l, h, order);

			// If there are elements on left side of pivot,
			// then push left side to stack
			if ( p-1 > l )
			{
				stack[ ++top ] = l;
				stack[ ++top ] = p - 1;
			}

			// If there are elements on right side of pivot,
			// then push right side to stack
			if ( p+1 < h )
			{
				stack[ ++top ] = p + 1;
				stack[ ++top ] = h;
			}
		}
	}
	
}
