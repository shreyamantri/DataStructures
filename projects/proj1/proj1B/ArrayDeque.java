import java.util.ArrayList;
import java.util.List;

// @author: Shreya Mantripragada
public class ArrayDeque<T> implements Deque<T> {

    private T[] values;
    private int newFirst, newLast, size;

    public ArrayDeque() {
        size = 0;
        values = (T[]) new Object[8];
        newFirst = 0;
        newLast = 1;
    }

    @Override
    public void addFirst(T x) {
        if (size == values.length) {
            resize(size * 2);

            int count = values.length - 1;
            int newLastIndex = 0;

            while (count >= 0) {
                if (values[count] == null) {
                    newLastIndex = count;
                    count = -1;
                } else {
                    count -= 1;
                }
            }
            newFirst = newLastIndex;
            values[newFirst] = x;
            newFirst -= 1;
            newLast = values.length / 2;
        } else {
            values[newFirst] = x;
            if (newFirst == 0) {
                newFirst = values.length - 1;
            } else {
                newFirst -= 1;
            }
        }

        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == values.length) {
            resize(size * 2);

            int count = values.length - 1;
            int newLastIndex = 0;

            while (count >= 0) {
                if (values[count] == null) {
                    newLastIndex = count;
                    count = -1;
                } else {
                    count -= 1;
                }
            }
            newFirst = newLastIndex;
            newLast = values.length / 2;
            values[newLast] = x;
            newLast += 1;
        } else {
            values[newLast] = x;
            if (newLast == values.length - 1) {
                newLast = 0;
            } else {
                newLast += 1;
            }

        }

        size += 1;
    }

    public void resize(int capacity) {
        System.out.println("capacity: " + capacity);
        T[] temp = (T[]) new Object[capacity];
        int counter = 0;

        for (int i = newFirst + 1; i < values.length; i++) {
            temp[counter] = values[i];
            counter += 1;
        }
        for (int j = 0; j < newFirst + 1; j++) {
            temp[counter] = values[j];
            counter += 1;
        }

        values = temp;

        int newFirstCount = values.length - 1, newLastCount = 0;
        int newFirstIndex = 0, newLastIndex  = 0;

        while (newFirstCount >= 0) {
            if (values[newFirstCount] == null) {
                newFirstIndex = newFirstCount;
                newFirstCount = -1;
            } else {
                newFirstCount -= 1;
            }
        }

        while (newLastCount < values.length) {
            if (values[newLastCount] == null) {
                newLastIndex = newLastCount;
                newLastCount = values.length;
            } else {
                newLastCount += 1;
            }
        }
        newFirst = newFirstIndex;
        newLast = newLastIndex;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();

        for (int j = newFirst + 1; j < values.length; j++) {
            if (values[j] != null) {
                returnList.add(values[j]);
            }
        }

        for (int i = 0; i < newFirst + 1; i++) {
            if (values[i] != null) {
                returnList.add(values[i]);
            }
        }

        return returnList;
    }

    @Override
    public T get(int index) {
        int counter = newFirst + 1, temp = 0;

        if (index < 0 || index > values.length) {
            return null;
        }

        if (counter >= values.length) {
            counter = 0;
        }

        while (temp < index) {
            if (counter == values.length - 1) {
                counter = 0;
            } else {
                counter += 1;
            }
            temp += 1;
        }
        return values[counter];
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        T origValue = null;

        if ((double) size / values.length < 0.25) {
            resizeDown(values.length / 2);
        }

        if (newFirst + 1 >= values.length) {
            newFirst = 0;
            origValue = values[newFirst];

            if (origValue != null) {
                values[newFirst] = null;
                size -= 1;
            }
            newFirst -= 1;

        } else {
            origValue = values[newFirst + 1];
            if (origValue != null) {
                values[newFirst + 1] = null;
                size -= 1;
            }
        }

        newFirst = newFirst + 1;

        return origValue;
    }

    @Override
    public T removeLast() {
        if ((double) size / values.length < 0.25) {
            resizeDown(values.length / 2);
        }

        newLast = newLast - 1;

        if (newLast < 0) {
            newLast = values.length - 1;
        }

        T origValue = values[newLast];

        if (origValue != null) {
            values[newLast] = null;
            size -= 1;
        }

        return origValue;
    }

    public void resizeDown(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int counter = 0;

        for (int i = newFirst + 1; i < values.length && counter < temp.length; i++) {
            System.out.println("counter: " + counter);
            temp[counter] = values[i];
            counter += 1;
        }
        for (int j = 0; j < newFirst + 1 && counter < temp.length; j++) {
            temp[counter] = values[j];
            counter += 1;
        }

        values = temp;

        int newFirstCount = values.length - 1, newLastCount = 0;
        int newFirstIndex = 0, newLastIndex  = 0;

        while (newFirstCount >= 0) {
            if (values[newFirstCount] == null) {
                newFirstIndex = newFirstCount;
                newFirstCount = -1;
            } else {
                newFirstCount -= 1;
            }
        }

        while (newLastCount < values.length) {
            if (values[newLastCount] == null) {
                newLastIndex = newLastCount;
                newLastCount = values.length;
            } else {
                newLastCount += 1;
            }
        }
        newFirst = newFirstIndex;
        newLast = newLastIndex;
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }
}
