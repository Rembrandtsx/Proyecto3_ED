package model.data_structures;

/**
 * Abstract Data Type for a linked list of generic objects
 * This ADT should contain the basic operations to manage a list
 * add: add a new element T 
 * delete: delete the given element T 
 * get: get the given element T (null if it doesn't exist in the list)
 * size: return the the number of elements
 * get: get an element T by position (the first position has the value 0) 
 * listing: set the listing of elements at the firt element
 * getCurrent: return the current element T in the listing (return null if it doesn�t exists)
 * next: advance to next element in the listing (return if it exists)
 * @param <T>
 */
public interface LinkedList <T extends Comparable<T>>  extends Comparable
{

	/**
	 * Adds an element to the list
	 * @param e Element to be added
	 */
	void add(T e);

	/**
	 * Deletes the corresponding element given by parameter
	 * @param e Element to be deleted.
	 */
	T delete(T e);

	/**
	 * Returns the size of the list
	 * @return size
	 */
	int size();

	/**
	 * Gets the element at the given position
	 * @param pos Position in the list
	 * @return the asked element
	 */
	T get(int pos) throws  Exception;

	/**
	 * Searches for the given element
	 */
	T get(T e);

	/**
	 * Sets the listing of elements at the root of the list
	 */
	void listing();

	/**
	 * Returns the current element
	 * @return current
	 */
	T getCurrent();

	/**
	 * Sets the current element at the next position
	 * @return the current element
	 */
	T next();
	
	/**
	 * Checks if the list is empty
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * Gets the root of the list
	 * @return
	 */
	T getRoot();
	
	/**
	 * Adds an element before the current element
	 * @param e
	 */
	void addCurrentNode(T e);

	boolean hasNext();

	public void deleteCurrent();

	public LinkedList<T> getCopy();
}
