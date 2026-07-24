import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable, array-backed generic list.
 * @param <E> type of elements stored in the list
 */
public class ArrayList<E> implements Iterable<E> {

    /** capacity used when none is specified */
    private final static int DEFAULT_CAPACITY = 10;

    /** backing array */
    private E[] elementData;
    /** number of elements currently stored */
    private int size;

    /** Creates an empty list with the default capacity. */
    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty list with the given initial capacity.
     * @param capacity initial capacity; must not be negative
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
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the given index.
     * @param index index of the desired element
     * @return element at the given index
     */
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

    /**
     * Returns the index of the first occurrence of the given value.
     * @param value value to search for
     * @return index of the first occurrence, or -1 if not found
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
     * Returns whether the list has no elements.
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns whether the list contains the given value.
     * @param value value to search for
     * @return true if the list contains value
     */
    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    /**
     * Appends the given value to the end of the list.
     * @param value value to add
     */
    public void add(E value) {
        ensureCapacity(size + 1);
        elementData[size] = value;
        size++;
    }

    /**
     * Inserts the given value at the given index, shifting later elements right.
     * @param index index to insert at; must be between 0 and size (inclusive)
     * @param value value to insert
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
     * Removes the element at the given index, shifting later elements left.
     * @param index index of the element to remove
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
     * Replaces the element at the given index with the given value.
     * @param index index of the element to replace
     * @param value new value
     */
    public void set(int index, E value) {
        checkIndex(index);
        elementData[index] = value;
    }

    /** Removes all elements from the list. */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    /**
     * Appends all elements of the given list to the end of this list, in order.
     * @param other list whose elements are added; must not be null
     */
    public void addAll(ArrayList<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    /**
     * Copies this list's elements into the given array, following the same contract as
     * {@link java.util.Collection#toArray(Object[])}.
     * @param target array to fill, or to use as a size/type template if too small
     * @return array containing this list's elements
     */
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] target) {
        if (target.length < size) {
            target = (E[]) Array.newInstance(target.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            target[i] = elementData[i];
        }
        if (target.length > size) {
            target[size] = null;
        }
        return target;
    }

    /**
     * Grows the backing array if needed so it can hold at least the given capacity.
     * @param capacity minimum capacity required
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
     * Throws an exception if the given index is not valid for this list.
     * @param index index to check
     */
    public void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }

    /** Iterator over the elements of the enclosing list. */
    private class ArrayListIterator implements Iterator<E> {

        /** index of the element that will be returned by the next call to next() */
        private int position;
        /** whether remove() may currently be called */
        private boolean removeOK;

        /** Creates an iterator positioned before the first element. */
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
