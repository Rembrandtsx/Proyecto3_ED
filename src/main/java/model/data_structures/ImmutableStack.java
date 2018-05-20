package model.data_structures;

public class ImmutableStack<T extends Comparable<T>> implements Comparable<ImmutableStack<T>> {

    private static ImmutableStack emptyStack;

    private T head;

    private ImmutableStack<T> tail;

    public ImmutableStack() {
        head = null;
        tail = null;
    }

    public T peek(){
        return head;
    }

    public ImmutableStack<T> push(T newHead){
        ImmutableStack<T> result = new ImmutableStack<>();
        result.head = newHead;
        result.tail = this;
        return result;
    }

    public ImmutableStack<T> pop(){
        return tail;
    }

    public boolean isEmpty(){
        return head == null && tail == null;
    }

    public LinkedList<T> toList(){
        ImmutableStack<T> temp = this;
        LinkedList<T> list = new List<>();
        while (!temp.isEmpty()){
            list.add(temp.peek());
            temp = temp.pop();
        }
        return list;
    }

    @Override
    public int compareTo(ImmutableStack<T> o) {
        return 0;
    }
}
