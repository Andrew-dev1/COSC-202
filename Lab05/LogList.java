import java.util.*;

public class LogList<E> {
    
    public LogList() {

    }

    public LogList(Collection<E> items) {

    }

    public int size() {
        return 0;
    }

    public void insert(int index, E item) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(index);
    }

    public E get(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(index);

        return null;
    }

    public E delete(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(index);

        return null;
    }

    public String toString() {
        return null;
    }

}
