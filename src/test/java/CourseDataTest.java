import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseDataTest {

    @Test
    void accessorsReturnConstructorValues() {
        CourseData course = new CourseData("U0001", (byte) 0, (byte) 1, (byte) 2,
                (byte) 10, (byte) 8, (byte) 1, (byte) 50, (byte) 0, (byte) 4);

        assertEquals("U0001", course.userId());
        assertEquals(0, course.experienceLevel());
        assertEquals(1, course.courseType());
        assertEquals(2, course.platform());
        assertEquals(10, course.hoursPerWeek());
        assertEquals(8, course.courseDuration());
        assertEquals(1, course.completionStatus());
        assertEquals(50, course.completionPercentage());
        assertEquals(0, course.dropoutReason());
        assertEquals(4, course.satisfactionScore());
    }

    @Test
    void nullUserIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CourseData(null,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
    }

    @Test
    void equalRecordsAreEqual() {
        CourseData a = new CourseData("U0001", (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1);
        CourseData b = new CourseData("U0001", (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
