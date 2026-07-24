import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseStatsTest {

    @Test
    void accessorsReturnConstructorValues() {
        CourseStats stats = new CourseStats(10, 5.5, 12.25, 60.0, 4.2);
        assertEquals(10, stats.recordCount());
        assertEquals(5.5, stats.avgHrsPerWeek());
        assertEquals(12.25, stats.avgCourseDuration());
        assertEquals(60.0, stats.avgCompletionPercent());
        assertEquals(4.2, stats.avgSatisfactionScore());
    }

    @Test
    void toStringContainsAllFields() {
        CourseStats stats = new CourseStats(3, 1.0, 2.0, 3.0, 4.0);
        String rendered = stats.toString();
        assertTrue(rendered.contains("recordCount=3"));
        assertTrue(rendered.contains("avgHrsPerWeek=1.00"));
        assertTrue(rendered.contains("avgCourseDuration=2.00"));
        assertTrue(rendered.contains("avgCompletionPercent=3.00"));
        assertTrue(rendered.contains("avgSatisfactionScore=4.00"));
    }
}
