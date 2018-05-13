package model.data_structures;

public interface IQueue <T> 
{
	/**
	 * Agregar un item al tope de la cola
	 */
	void enqueue(T item);

	/**
	 * Elimina el elemento al tope de la cola
	 */
	T dequeue();

	/**
	 * Indica si la cola esta vacia
	 */
	boolean isEmpty();

	/**
	 * Numeros de elementos en la lista
	 */
	int size();
	
	T getCurrent();
	
	T next();
	
	void listing();
}
