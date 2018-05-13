package model.data_structures;

public class Node <T> {

	/**
	 * Represents the next node
	 */
	private Node<T> next;

	/**
	 * Represents the previous node
	 */
	private Node<T> prev;

	/**
	 * Represents the contained element in the node
	 */
	private T element;


	/**
	 * Instantiates a node initializing next and prev to null,
	 * and allocates the element inside the node
	 * @param element Element to be contained in the node
	 */
	public Node(T element) {
		this.element = element;
		this.next = null;
		this.prev = null;
	}

	/**
	 * Returns the contained element
	 * @return element
	 */
	public T getElement() {
		return element;
	}

	/**
	 * Returns the next node
	 * @return next
	 */
	public Node<T> getNext() {
		return next;
	}

	/**
	 * Retruns the previous node
	 * @return prev
	 */
	public Node<T> getPrev() {
		return prev;
	}

	/**
	 * Sets the reference to the next node
	 * @param e next node
	 */
	public void setNext(Node<T> e) {
		this.next = e;
	}

	/**
	 * Sets the reference to the previous node
	 * @param e previous node
	 */
	public void setPrev(Node<T> e) {
		this.prev = e;
	}
}
