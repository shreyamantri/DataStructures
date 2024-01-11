package deque;

//import java.lang.reflect.Array;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comp = c;
    }

    public T max() {
        return max(comp);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T maximum = (T) this.get(0);
        for (int i = 0; i < this.size(); i++) {
            if (c.compare((T) this.get(i), maximum) > 0) {
                maximum = (T) this.get(i);
            }
        }
        return maximum;
    }
}
