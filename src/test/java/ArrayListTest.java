import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayListTest {

    @Test
    void newListIsEmpty() {
        ArrayList<String> list = new ArrayList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void negativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayList<String>(-1));
    }

    @Test
    void addAndGet() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertFalse(list.isEmpty());
    }

    @Test
    void growsBeyondInitialCapacity() {
        ArrayList<Integer> list = new ArrayList<>(2);
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        assertEquals(50, list.size());
        for (int i = 0; i < 50; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void getOutOfBoundsThrows() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void indexOfAndContains() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        assertEquals(0, list.indexOf("a"));
        assertEquals(-1, list.indexOf("z"));
        assertTrue(list.contains("a"));
        assertFalse(list.contains("z"));
    }

    @Test
    void indexOfHandlesNullElements() {
        ArrayList<String> list = new ArrayList<>();
        list.add(null);
        list.add("b");
        assertEquals(0, list.indexOf(null));
        assertEquals(-1, list.indexOf("z"));
    }

    @Test
    void addAtIndexShiftsElements() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("c");
        list.add(1, "b");
        assertEquals("[a, b, c]", list.toString());
    }

    @Test
    void addAtInvalidIndexThrows() {
        ArrayList<String> list = new ArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "x"));
    }

    @Test
    void removeShiftsElements() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.remove(1);
        assertEquals("[a, c]", list.toString());
        assertEquals(2, list.size());
    }

    @Test
    void removeOutOfBoundsThrows() {
        ArrayList<String> list = new ArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void setReplacesElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.set(0, "z");
        assertEquals("z", list.get(0));
    }

    @Test
    void clearRemovesAllElements() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void addAllAppendsOtherList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        ArrayList<String> other = new ArrayList<>();
        other.add("b");
        other.add("c");
        list.addAll(other);
        assertEquals("[a, b, c]", list.toString());
    }

    @Test
    void addAllWithNullThrows() {
        ArrayList<String> list = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> list.addAll(null));
    }

    @Test
    void iteratorSupportsForEach() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        StringBuilder collected = new StringBuilder();
        for (String value : list) {
            collected.append(value);
        }
        assertEquals("abc", collected.toString());
    }

    @Test
    void iteratorThrowsWhenExhausted() {
        ArrayList<String> list = new ArrayList<>();
        Iterator<String> it = list.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorRemoveWithoutNextThrows() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        Iterator<String> it = list.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void iteratorRemoveDeletesLastReturnedElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Iterator<String> it = list.iterator();
        it.next();
        it.next();
        it.remove();
        assertEquals("[a, c]", list.toString());
    }

    @Test
    void toArrayGrowsWhenTargetTooSmall() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        String[] result = list.toArray(new String[0]);
        assertArrayEquals(new String[] {"a", "b"}, result);
    }

    @Test
    void toArrayNullTerminatesWhenTargetLarger() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        String[] target = new String[] {"x", "y"};
        String[] result = list.toArray(target);
        assertSame(target, result);
        assertEquals("a", result[0]);
        assertNull(result[1]);
    }

    @Test
    void checkIndexThrowsForInvalidIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkIndex(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkIndex(1));
    }
}
