import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * File-backed {@link OnlineCourseDatabaseInterface} implementation. Loads course records from a
 * comma-separated file, encoding categorical fields as indexes into option lists derived from the
 * data itself. Also iterable over its {@link CourseData} records.
 */
public class OnlineCourseDatabase implements OnlineCourseDatabaseInterface, Iterable<CourseData> {

    /** column index, within a data row, of the experience level field */
    private final static int COL_EXPERIENCE_LEVEL = 1;
    /** column index, within a data row, of the course type field */
    private final static int COL_COURSE_TYPE = 2;
    /** column index, within a data row, of the platform field */
    private final static int COL_PLATFORM = 3;
    /** column index, within a data row, of the completion status field */
    private final static int COL_COMPLETION_STATUS = 6;
    /** column index, within a data row, of the dropout reason field */
    private final static int COL_DROPOUT_REASON = 8;

    /** all course records managed by this database */
    private ArrayList<CourseData> courseData;
    /** distinct experience level values, sorted alphabetically */
    private ArrayList<String> experienceLevelList;
    /** distinct course type values, sorted alphabetically */
    private ArrayList<String> courseTypeList;
    /** distinct platform values, sorted alphabetically */
    private ArrayList<String> platformList;
    /** distinct completion status values, sorted alphabetically */
    private ArrayList<String> completionStatusList;
    /** distinct dropout reason values, sorted alphabetically */
    private ArrayList<String> dropoutReasonList;

    /**
     * Constructor; loads course records from the given file.
     * @param dbFile comma-separated file containing a header row followed by course records;
     *               must not be null
     * @throws FileNotFoundException if dbFile does not exist or cannot be opened
     */
    public OnlineCourseDatabase(File dbFile) throws FileNotFoundException {
        throwIfNull(dbFile, "dbFile");

        courseData = new ArrayList<>();
        experienceLevelList = new ArrayList<>();
        courseTypeList = new ArrayList<>();
        platformList = new ArrayList<>();
        completionStatusList = new ArrayList<>();
        dropoutReasonList = new ArrayList<>();

        loadData(dbFile);
    }

    /**
     * Reads the given file, populating the option lists and course records.
     * @param dbFile comma-separated file containing a header row followed by course records
     * @throws FileNotFoundException if dbFile does not exist or cannot be opened
     */
    private void loadData(File dbFile) throws FileNotFoundException {
        ArrayList<String[]> rawRows = new ArrayList<>();

        Scanner scanner = new Scanner(dbFile);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isBlank()) {
                String[] fields = line.split(",");
                rawRows.add(fields);
                insertSorted(experienceLevelList, fields[COL_EXPERIENCE_LEVEL].trim());
                insertSorted(courseTypeList, fields[COL_COURSE_TYPE].trim());
                insertSorted(platformList, fields[COL_PLATFORM].trim());
                insertSorted(completionStatusList, fields[COL_COMPLETION_STATUS].trim());
                insertSorted(dropoutReasonList, fields[COL_DROPOUT_REASON].trim());
            }
        }
        scanner.close();

        for (int i = 0; i < rawRows.size(); i++) {
            String[] f = rawRows.get(i);
            CourseData course = new CourseData(
                    f[0].trim(),
                    (byte) experienceLevelList.indexOf(f[COL_EXPERIENCE_LEVEL].trim()),
                    (byte) courseTypeList.indexOf(f[COL_COURSE_TYPE].trim()),
                    (byte) platformList.indexOf(f[COL_PLATFORM].trim()),
                    Byte.parseByte(f[4].trim()),
                    Byte.parseByte(f[5].trim()),
                    (byte) completionStatusList.indexOf(f[COL_COMPLETION_STATUS].trim()),
                    Byte.parseByte(f[7].trim()),
                    (byte) dropoutReasonList.indexOf(f[COL_DROPOUT_REASON].trim()),
                    Byte.parseByte(f[9].trim()));
            courseData.add(course);
        }
    }

    /**
     * Inserts the given value into the given list in alphabetical (case-insensitive) order,
     * if not already present.
     * @param list  list to insert into, assumed to already be sorted
     * @param value value to insert
     */
    private static void insertSorted(ArrayList<String> list, String value) {
        if (list.contains(value)) {
            return;
        }
        int index = 0;
        while (index < list.size() && list.get(index).compareToIgnoreCase(value) < 0) {
            index++;
        }
        list.add(index, value);
    }

    @Override
    public CourseData getCourseRecordAt(int index) {
        return courseData.get(index);
    }

    @Override
    public String getCourseRecordString(CourseData course) {
        throwIfNull(course, "course");
        return String.format("[%s, %s, %s, %s, %d, %d, %s, %d, %s, %d]",
                course.userId(),
                experienceLevelList.get(course.experienceLevel()),
                courseTypeList.get(course.courseType()),
                platformList.get(course.platform()),
                course.hoursPerWeek(),
                course.courseDuration(),
                completionStatusList.get(course.completionStatus()),
                course.completionPercentage(),
                dropoutReasonList.get(course.dropoutReason()),
                course.satisfactionScore());
    }

    @Override
    public int size() {
        return courseData.size();
    }

    @Override
    public String[] getExperienceLevelOptions() {
        return experienceLevelList.toArray(new String[0]);
    }

    @Override
    public String[] getCourseTypeOptions() {
        return courseTypeList.toArray(new String[0]);
    }

    @Override
    public String[] getPlatformOptions() {
        return platformList.toArray(new String[0]);
    }

    @Override
    public String[] getCompletionStatusOptions() {
        return completionStatusList.toArray(new String[0]);
    }

    @Override
    public String[] getDropoutReasonOptions() {
        return dropoutReasonList.toArray(new String[0]);
    }

    @Override
    public CourseStats calcFilteredAverages(byte experienceLevelIndex,
                                             byte courseTypeIndex,
                                             byte platformIndex,
                                             byte completionStatusIndex,
                                             byte dropoutReasonIndex) {
        int count = 0;
        long hoursSum = 0;
        long durationSum = 0;
        long completionSum = 0;
        long satisfactionSum = 0;

        for (int i = 0; i < courseData.size(); i++) {
            CourseData c = courseData.get(i);
            boolean matches = (experienceLevelIndex == -1 || c.experienceLevel() == experienceLevelIndex)
                    && (courseTypeIndex == -1 || c.courseType() == courseTypeIndex)
                    && (platformIndex == -1 || c.platform() == platformIndex)
                    && (completionStatusIndex == -1 || c.completionStatus() == completionStatusIndex)
                    && (dropoutReasonIndex == -1 || c.dropoutReason() == dropoutReasonIndex);

            if (matches) {
                count++;
                hoursSum += c.hoursPerWeek();
                durationSum += c.courseDuration();
                completionSum += c.completionPercentage();
                satisfactionSum += c.satisfactionScore();
            }
        }

        if (count == 0) {
            return new CourseStats(0, -1.0, -1.0, -1.0, -1.0);
        }

        return new CourseStats(count,
                (double) hoursSum / count,
                (double) durationSum / count,
                (double) completionSum / count,
                (double) satisfactionSum / count);
    }

    @Override
    public Iterator<CourseData> iterator() {
        return courseData.iterator();
    }

    /**
     * Throws an exception if the given object reference is null.
     * @param obj     object reference to check
     * @param context variable name, used to construct an exception message
     */
    private static void throwIfNull(Object obj, String context) {
        if (obj == null) {
            throw new IllegalArgumentException(context + " must not be null");
        }
    }
}
