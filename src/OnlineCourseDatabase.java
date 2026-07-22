import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OnlineCourseDatabase implements OnlineCourseDatabaseInterface {

    private final static int COL_EXPERIENCE_LEVEL = 1;
    private final static int COL_COURSE_TYPE = 2;
    private final static int COL_PLATFORM = 3;
    private final static int COL_COMPLETION_STATUS = 6;
    private final static int COL_DROPOUT_REASON = 8;

    private ArrayList<CourseData> courseData;
    private ArrayList<String> experienceLevelList;
    private ArrayList<String> courseTypeList;
    private ArrayList<String> platformList;
    private ArrayList<String> completionStatusList;
    private ArrayList<String> dropoutReasonList;

    public OnlineCourseDatabase(File dbFile) {
        throwIfNull(dbFile, "dbFile");

        courseData = new ArrayList<>();
        experienceLevelList = new ArrayList<>();
        courseTypeList = new ArrayList<>();
        platformList = new ArrayList<>();
        completionStatusList = new ArrayList<>();
        dropoutReasonList = new ArrayList<>();

        loadData(dbFile);
    }

    private void loadData(File dbFile) {
        ArrayList<String[]> rawRows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(",");
                rawRows.add(fields);
                insertSorted(experienceLevelList, fields[COL_EXPERIENCE_LEVEL].trim());
                insertSorted(courseTypeList, fields[COL_COURSE_TYPE].trim());
                insertSorted(platformList, fields[COL_PLATFORM].trim());
                insertSorted(completionStatusList, fields[COL_COMPLETION_STATUS].trim());
                insertSorted(dropoutReasonList, fields[COL_DROPOUT_REASON].trim());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read database file: " + dbFile, e);
        }

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

    private static String[] toArray(ArrayList<String> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
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
        return toArray(experienceLevelList);
    }

    @Override
    public String[] getCourseTypeOptions() {
        return toArray(courseTypeList);
    }

    @Override
    public String[] getPlatformOptions() {
        return toArray(platformList);
    }

    @Override
    public String[] getCompletionStatusOptions() {
        return toArray(completionStatusList);
    }

    @Override
    public String[] getDropoutReasonOptions() {
        return toArray(dropoutReasonList);
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
            if (experienceLevelIndex != -1 && c.experienceLevel() != experienceLevelIndex) {
                continue;
            }
            if (courseTypeIndex != -1 && c.courseType() != courseTypeIndex) {
                continue;
            }
            if (platformIndex != -1 && c.platform() != platformIndex) {
                continue;
            }
            if (completionStatusIndex != -1 && c.completionStatus() != completionStatusIndex) {
                continue;
            }
            if (dropoutReasonIndex != -1 && c.dropoutReason() != dropoutReasonIndex) {
                continue;
            }

            count++;
            hoursSum += c.hoursPerWeek();
            durationSum += c.courseDuration();
            completionSum += c.completionPercentage();
            satisfactionSum += c.satisfactionScore();
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

    private static void throwIfNull(Object obj, String context) {
        if (obj == null) {
            throw new IllegalArgumentException(context + " must not be null");
        }
    }
}
