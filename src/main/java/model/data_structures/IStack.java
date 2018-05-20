package model.data_structures;

public interface IStack<E> {

	/** Push a new element at the top of the stack */
	public void push (E item);
	
	/** Pop the element at the top of the stack 
	 * @return the top element or null if it doesn't exist
	 * */
	public E pop();
	
	/** Evaluate if the stack is empty
	 * @return true if the stack is empty. false in other case. 
	 */
	public boolean isEmpty();
	
	/**
	 * Number of elements in the list
	 */
	public int size();
	
	E getCurrent();
	
	E next();
	
	void listing();

}
