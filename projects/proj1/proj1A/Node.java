import java.util.List;
public class Node<T> {

    public T item;
    public Node<T> next;
    public Node<T> prev;

    public Node(T value, Node<T> prevValue, Node<T> nextValue) {
        this.item = value;
        this.next = nextValue;
        this.prev = prevValue;

    }

}
