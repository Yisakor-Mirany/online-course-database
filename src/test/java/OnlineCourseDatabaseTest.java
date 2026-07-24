import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OnlineCourseDatabaseTest {

    private static final String CSV_CONTENT = """
            User_ID,Experience_Level,Course_Type,Platform,Hours_Spent_Per_Week,Course_Duration_Weeks,Completion_Status,Completion_Percentage,Dropout_Reason,Satisfaction_Score
            U0001,Student,Tech,Coursera,10,8,Completed,90,No Dropout,5

            U0002,Fresher,Non-Tech,Udemy,5,4,Dropped,20,Too Difficult,2
            U0003,Student,Tech,Coursera,15,8,In Progress,60,No Dropout,4
            """;

    private OnlineCourseDatabase buildDatabase(Path tempDir) throws IOException {
        File file = tempDir.resolve("courses.csv").toFile();
        Files.writeString(file.toPath(), CSV_CONTENT);
        return new OnlineCourseDatabase(file);
    }

    private static int indexOf(String[] options, String value) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) {
                return i;
            }
        }
        throw new IllegalArgumentException(value + " not found");
    }

    @Test
    void nullFileThrows() {
        assertThrows(IllegalArgumentException.class, () -> new OnlineCourseDatabase(null));
    }

    @Test
    void missingFileThrowsFileNotFoundException() {
        assertThrows(FileNotFoundException.class,
                () -> new OnlineCourseDatabase(new File("does-not-exist.csv")));
    }

    @Test
    void sizeMatchesDataRows(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        assertEquals(3, db.size());
    }

    @Test
    void optionsAreSortedAndDeduplicated(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        assertArrayEquals(new String[] {"Fresher", "Student"}, db.getExperienceLevelOptions());
        assertArrayEquals(new String[] {"Non-Tech", "Tech"}, db.getCourseTypeOptions());
        assertArrayEquals(new String[] {"Coursera", "Udemy"}, db.getPlatformOptions());
        assertArrayEquals(new String[] {"Completed", "Dropped", "In Progress"}, db.getCompletionStatusOptions());
        assertArrayEquals(new String[] {"No Dropout", "Too Difficult"}, db.getDropoutReasonOptions());
    }

    @Test
    void getCourseRecordStringRendersDescriptiveValues(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        CourseData first = db.getCourseRecordAt(0);
        assertEquals("[U0001, Student, Tech, Coursera, 10, 8, Completed, 90, No Dropout, 5]",
                db.getCourseRecordString(first));
    }

    @Test
    void getCourseRecordStringWithNullThrows(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        assertThrows(IllegalArgumentException.class, () -> db.getCourseRecordString(null));
    }

    @Test
    void calcFilteredAveragesWithNoFilterCoversAllRecords(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        CourseStats stats = db.calcFilteredAverages((byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1);
        assertEquals(3, stats.recordCount());
        assertEquals((10 + 5 + 15) / 3.0, stats.avgHrsPerWeek(), 0.0001);
    }

    @Test
    void calcFilteredAveragesAppliesFilter(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte studentIndex = (byte) indexOf(db.getExperienceLevelOptions(), "Student");
        CourseStats stats = db.calcFilteredAverages(studentIndex, (byte) -1, (byte) -1, (byte) -1, (byte) -1);
        assertEquals(2, stats.recordCount());
    }

    @Test
    void calcFilteredAveragesWithNoMatchesReturnsSentinelStats(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte fresherIndex = (byte) indexOf(db.getExperienceLevelOptions(), "Fresher");
        byte completedIndex = (byte) indexOf(db.getCompletionStatusOptions(), "Completed");
        CourseStats stats = db.calcFilteredAverages(fresherIndex, (byte) -1, (byte) -1, completedIndex, (byte) -1);
        assertEquals(0, stats.recordCount());
        assertEquals(-1.0, stats.avgHrsPerWeek());
        assertEquals(-1.0, stats.avgCourseDuration());
        assertEquals(-1.0, stats.avgCompletionPercent());
        assertEquals(-1.0, stats.avgSatisfactionScore());
    }

    @Test
    void isIterableOverAllCourseRecords(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        int count = 0;
        for (CourseData course : db) {
            assertNotNull(course);
            count++;
        }
        assertEquals(db.size(), count);
    }

    @Test
    void emptyFileYieldsNoRecords(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("empty.csv").toFile();
        Files.writeString(file.toPath(), "");
        OnlineCourseDatabase db = new OnlineCourseDatabase(file);
        assertEquals(0, db.size());
    }

    @Test
    void calcFilteredAveragesAppliesCourseTypeFilter(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte techIndex = (byte) indexOf(db.getCourseTypeOptions(), "Tech");
        CourseStats stats = db.calcFilteredAverages((byte) -1, techIndex, (byte) -1, (byte) -1, (byte) -1);
        assertEquals(2, stats.recordCount());
    }

    @Test
    void calcFilteredAveragesAppliesPlatformFilter(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte courseraIndex = (byte) indexOf(db.getPlatformOptions(), "Coursera");
        CourseStats stats = db.calcFilteredAverages((byte) -1, (byte) -1, courseraIndex, (byte) -1, (byte) -1);
        assertEquals(2, stats.recordCount());
    }

    @Test
    void calcFilteredAveragesAppliesCompletionStatusFilter(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte completedIndex = (byte) indexOf(db.getCompletionStatusOptions(), "Completed");
        CourseStats stats = db.calcFilteredAverages((byte) -1, (byte) -1, (byte) -1, completedIndex, (byte) -1);
        assertEquals(1, stats.recordCount());
    }

    @Test
    void calcFilteredAveragesAppliesDropoutReasonFilter(@TempDir Path tempDir) throws IOException {
        OnlineCourseDatabase db = buildDatabase(tempDir);
        byte noDropoutIndex = (byte) indexOf(db.getDropoutReasonOptions(), "No Dropout");
        CourseStats stats = db.calcFilteredAverages((byte) -1, (byte) -1, (byte) -1, (byte) -1, noDropoutIndex);
        assertEquals(2, stats.recordCount());
    }
}
