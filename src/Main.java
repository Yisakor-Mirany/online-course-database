import java.io.File;

public class Main {

    private final static String DEFAULT_DB_FILE = "data/OnlineCourseDataset.csv";

    private OnlineCourseDatabase db;
    private CourseDataFilteringGui gui;

    private Main(File dbFile) {
        db = new OnlineCourseDatabase(dbFile);
        gui = new CourseDataFilteringGui(db);
    }

    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : DEFAULT_DB_FILE;
        new Main(new File(path));
    }
}
