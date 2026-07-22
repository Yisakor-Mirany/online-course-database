import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom, resizable generic list implementation backed by an array.
 * @param <E>   type of elements stored in the list
 */
public class ArrayList<E> {

    /** default capacity used when none is specified */
    private final static int DEFAULT_CAPACITY = 10;

    /** backing array holding the list's elements */
    private E[] elementData;
    /** number of elements currently stored in the list */
    private int size;

    /**
     * Creates an empty list with a default initial capacity
     */
    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty list with the specified initial capacity
     * @param capacity  initial capacity of the list; must not be negative
     */
    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must not be negative");
        }
        elementData = (E[]) new Object[capacity];
        size = 0;
    }

    /**
     * Returns the number of elements in the list
     * @return  number of elements in the list
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified index
     * @param index     index of the desired element
     * @return          element at the specified index
     */
    public E get(int index) {
        checkIndex(index);
        return elementData[index];
    }

    /**
     * Returns a string representation of the list, e.g. "[a, b, c]"
     * @return  string representation of the list
     */
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

    /**
     * Returns the index of the first occurrence of the specified value
     * @param value     value to search for
     * @return          index of the first occurrence of value, or -1 if not found
     */
    public int indexOf(E value) {
        for (int i = 0; i < size; i++) {
            if (elementData[i] == null ? value == null : elementData[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns whether the list contains no elements
     * @return  true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns whether the list contains the specified value
     * @param value     value to search for
     * @return          true if the list contains value, false otherwise
     */
    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    /**
     * Appends the specified value to the end of the list
     * @param value     value to add
     */
    public void add(E value) {
        ensureCapacity(size + 1);
        elementData[size] = value;
        size++;
    }

    /**
     * Inserts the specified value at the specified index, shifting subsequent elements to the right
     * @param index     index at which to insert; must be between 0 and size (inclusive)
     * @param value     value to insert
     */
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

    /**
     * Removes the element at the specified index, shifting subsequent elements to the left
     * @param index     index of the element to remove
     */
    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
        elementData[size - 1] = null;
        size--;
    }

    /**
     * Replaces the element at the specified index with the specified value
     * @param index     index of the element to replace
     * @param value     new value to store at the specified index
     */
    public void set(int index, E value) {
        checkIndex(index);
        elementData[index] = value;
    }

    /**
     * Removes all elements from the list
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    /**
     * Appends all elements of the specified list to the end of this list, in order
     * @param other     list whose elements are to be added; must not be null
     */
    public void addAll(ArrayList<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    /**
     * Returns an iterator over the elements in the list, in order
     * @return  iterator over the list's elements
     */
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    /**
     * Ensures that the backing array can hold at least the specified number of elements,
     * growing it if necessary
     * @param capacity  minimum capacity required
     */
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

    /**
     * Throws an exception if the specified index is not a valid index into the list
     * @param index     index to check
     */
    public void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }

    /**
     * Iterator implementation over the elements of the enclosing list
     */
    private class ArrayListIterator implements Iterator<E> {

        /** index of the element that will be returned by the next call to next() */
        private int position;
        /** whether remove() may currently be called */
        private boolean removeOK;

        /**
         * Creates an iterator positioned before the first element of the list
         */
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
