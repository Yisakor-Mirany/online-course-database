import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> {

    private final static int DEFAULT_CAPACITY = 10;

    private E[] elementData;
    private int size;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must not be negative");
        }
        elementData = (E[]) new Object[capacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public E get(int index) {
        checkIndex(index);
        return elementData[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elementData[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int indexOf(E value) {
        for (int i = 0; i < size; i++) {
            if (elementData[i] == null ? value == null : elementData[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    public void add(E value) {
        ensureCapacity(size + 1);
        elementData[size] = value;
        size++;
    }

    public void add(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[index] = value;
        size++;
    }

    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
        elementData[size - 1] = null;
        size--;
    }

    public void set(int index, E value) {
        checkIndex(index);
        elementData[index] = value;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    public void addAll(ArrayList<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    @SuppressWarnings("unchecked")
    public void ensureCapacity(int capacity) {
        if (capacity > elementData.length) {
            int newCapacity = Math.max(capacity, elementData.length * 2);
            E[] newElementData = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElementData[i] = elementData[i];
            }
            elementData = newElementData;
        }
    }

    public void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }

    private class ArrayListIterator implements Iterator<E> {

        private int position;
        private boolean removeOK;

        public ArrayListIterator() {
            position = 0;
            removeOK = false;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E value = elementData[position];
            position++;
            removeOK = true;
            return value;
        }

        @Override
        public void remove() {
            if (!removeOK) {
                throw new IllegalStateException("next() must be called before remove()");
            }
            position--;
            ArrayList.this.remove(position);
            removeOK = false;
        }
    }
}
