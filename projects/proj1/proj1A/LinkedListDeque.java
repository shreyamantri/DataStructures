import java.util.List;
import java.util.ArrayList;

// @author: Shreya Mantripragda
public class LinkedListDeque<T> implements Deque<T> {
    private Node<T> sentinel;
    private int size;


    public class Node<T> {

        private T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T value, Node<T> prevValue, Node<T> nextValue) {
            this.item = value;
            this.next = nextValue;
            this.prev = prevValue;

        }

    }

    public LinkedListDeque() {
        this.sentinel = new Node<T>(null, null, null);
        this.sentinel.prev = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;

    }

    @Override
    public void addFirst(T x) {
        Node<T> nodeBeginning = new Node<T>(x, sentinel, sentinel.next);
        if (sentinel.next == null)  {
            sentinel.next = nodeBeginning;
            nodeBeginning.prev = sentinel;
        } else {
            Node<T> oldFront = sentinel.next;
            sentinel.next = nodeBeginning;
            nodeBeginning.prev = sentinel;
            nodeBeginning.next = oldFront;
            oldFront.prev = nodeBeginning;
        }

        this.size += 1;
    }

    @Override
    public void addLast(T x) {
        Node<T> nodeEnd = new Node<T>(x, null, null);
        if (sentinel.prev == null) {
            sentinel.next = nodeEnd;
            nodeEnd.prev = sentinel;
        } else {
            Node<T> oldEnd = sentinel.prev;
            nodeEnd.prev = sentinel.prev;
            sentinel.prev = nodeEnd;
            nodeEnd.next = sentinel;
            oldEnd.next = nodeEnd;
        }

        this.size += 1;

    }

    @Override
    public List<T> toList() {
        ArrayList<T> dequeValues = new ArrayList<T>();
        Node<T> pointer = sentinel.next;

        while (pointer.item != null) {
            dequeValues.add(pointer.item);
            pointer = pointer.next;
        }
        return dequeValues;
    }

    @Override
    public boolean isEmpty() {
        return this.size() <= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (sentinel.next == null || size == 0) {
            return null;
        }
        Node<T> origFirst = sentinel.next;
        Node<T> newFirst = sentinel.next.next;


        sentinel.next = newFirst;
        newFirst.prev = sentinel;

        this.size -= 1;

        return origFirst.item;

    }

    @Override
    public T removeLast() {
        if (sentinel.next == null || size == 0) {
            return null;
        }
        Node<T> origLast = sentinel.prev;
        Node<T> newLast = sentinel.prev.prev;

        newLast.next = sentinel;
        sentinel.prev = newLast;

        this.size -= 1;

        return origLast.item;

    }

    @Override
    public T get(int index) {
        if (index < 0 || index > this.size()) {
            return null;
        }

        Node<T> pointer = sentinel.next;
        int counter = 0;

        while (pointer.item != null) {
            if (counter == index) {
                return pointer.item;
            }
            pointer = pointer.next;
            counter += 1;
        }
        return null;
    }

    @Override
    public T getRecursive(int index) {
        int counter = 0;

        if (index < 0 || index > this.size()) {
            return null;
        }
        return recursiveFunc(sentinel.next, counter, index);
    }

    public T recursiveFunc(Node<T> list, int count, int indexVal) {
        if (count == indexVal) {
            return list.item;
        } else if (list.item == null) {
            return null;
        }

        return recursiveFunc(list.next, count + 1, indexVal);

    }

}
