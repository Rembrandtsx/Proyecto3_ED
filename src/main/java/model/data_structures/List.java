package model.data_structures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class List<T extends Comparable<T>>
	implements LinkedList<T>, IStack<T>, IQueue<T> {

	/**
	 * The size of the list
	 */
	protected int size;

	/**
	 * The current node
	 */
	protected Node<T> current;

	/**
	 * The first element of the list
	 */
	protected Node<T> root;
	
	/**
	 * The last element of the list
	 */
	protected Node<T> end;

	/**
	 * Instantiates a list initializing the size in 0 and the root to null
	 */
	public List() {

		root = null;
		end = null;
		listing();
		size = 0;

	}
	
	

	/**
	 * Returns the first element of the list
	 * @return root
	 */
	public T getRoot(){
		return root == null ? null: root.getElement();
	}

	/**
	 * Adds an element at the beginning of the list
	 * @param e Element to be added
	 */
	@Override
	public void add(T e) {
		listing();
		addCurrentNode(e);
	}

	/**
	 * Deletes the corresponding element given by parameter
	 * @param e Element to be deleted.
	 */
	@Override
	public T delete(T e) {
		T deleted = null;
		if(size == 0) {
			return deleted;
		}
		else {
			Node<T> prev = null;
			Node<T> curr = root;
			Node<T> next = root.getNext();
			
			while(curr != null && deleted == null) {
				if(e.equals(curr.getElement())) {
					if(prev != null) {
						prev.setNext(next);
					}else {
						root = next;
					}
					if(next != null) {
						next.setPrev(prev);
					}else {
						end = end.getPrev();
					}
					deleted = curr.getElement();
				}else {
					prev = curr;
					curr = next;
					if(curr != null) {
						next = curr.getNext();
					}
				}
			}
			size --;
			listing();
			return deleted;
		}
	}

	/**
	 * Returns the size of the list
	 * @return size
 	 */
	@Override
	public int size() {
		return size;
	}
	public String saveJson() {
		String rta = "";
		Gson gson = new GsonBuilder().create();
		rta = gson.toJson(this);
		return rta;
	}
	/**
	 * Gets the element at the given position
	 * @param pos Position in the list
	 * @return the asked element
	 */
	@Override
	public T get(int pos) throws Exception{
		if(pos >= size) {
			throw  new Exception("the given position is bigger than the size of the list");
		}else {
			int currPos = 0;
			Node<T> currNode = root;
			while(currPos < pos) {
				currPos ++;
				currNode = currNode.getNext();
			}
			return currNode.getElement();
		}
	}

	/**
	 * Searches for the given element
	 */
	@Override
	public T get(T e) {

		Node<T> curr = root;
		while(curr != null){
			if(e.equals(curr.getElement())){
				return curr.getElement();
			}else {
				curr = curr.getNext();
			}
		}
		return curr != null ? curr.getElement(): null;
	}

	/**
	 * Sets the listing of elements at the root of the list
	 */
	@Override
	public void listing() {
		current = root;
	}


	/**
	 * Returns the current element
	 * @return current
	 */
	@Override
	public T getCurrent() {
		if(current != null) {
			return current.getElement();
		}else {
			return null;
		}
	}


	@Override
	public boolean hasNext() {
		return current != null;
	}

	/**
	 * Sets the current element at the next position
	 * @return the current element
	 */
	@Override
	public T next() {
		if(current != null) {
			current = current.getNext();
			if(current != null) {
				return current.getElement();
			}else {
				return null;
			}
		}else {
			return null;
		}
	}

	@Override
	public void enqueue(T item) {
		add(item);
	}

	@Override
	public T dequeue() {
		Node<T> e = end;
		if(e != null) {
			end = e.getPrev();
			e.setPrev(null);
			if(end != null) {
				end.setNext(null);
			}
			size --;
			return e.getElement();
		}

		return null;
	}

	@Override
	public void push(T item) {
		add(item);
		
	}

	@Override
	public T pop() {
		Node<T> e = root;
		if(e != null) {
			root = e.getNext();
			e.setNext(null);
			if(root != null) {
				root.setPrev(null);
			}
			size --;
			return e.getElement();
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0 ? true: false;
	}
	

	/**
	 * Adds an element before the current element
	 * @param e
	 */
	@Override
	public void addCurrentNode(T e) {
		Node<T> n = new Node(e);
		if(current != null) {
			Node<T> prev = current.getPrev();
			if(prev != null) {
				prev.setNext(n);
			}
			n.setPrev(prev);
			if(current == root) {
				root = n;
			}
			current.setPrev(n);
			n.setNext(current);
			current = n;
		}else {
			if(!isEmpty()) {
				end.setNext(n);
				n.setPrev(end);
				end = n;
			}else {
				root = n;
				end = n;
				listing();
			}
		}
		size ++;
		
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public void deleteCurrent(){
		if(current != null){
			Node prev = current.getPrev();
			Node next = current.getNext();

			prev.setNext(next);
			next.setPrev(prev);
			current.setPrev(null);
			current.setNext(null);
			current = prev;
			size--;
		}
	}

}
