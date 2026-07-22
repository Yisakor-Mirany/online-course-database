import java.io.File;

/**
 * Application entry point; loads the course database and launches the filtering GUI.
 */
public class Main {

    /** default location of the course data file, used when none is specified on the command line */
    private final static String DEFAULT_DB_FILE = "data/OnlineCourseDataset.csv";

    /** the online course database backing this application */
    private OnlineCourseDatabase db;
    /** the GUI presenting and filtering the database's data */
    private CourseDataFilteringGui gui;

    /**
     * Constructor; loads the database and displays the GUI
     * @param dbFile    comma-separated file containing course records; must not be null
     */
    private Main(File dbFile) {
        db = new OnlineCourseDatabase(dbFile);
        gui = new CourseDataFilteringGui(db);
    }

    /**
     * Application entry point
     * @param args      command line arguments; args[0], if present, is the path to the course
     *                  data file to load, otherwise a default path is used
     */
    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : DEFAULT_DB_FILE;
        new Main(new File(path));
    }
}
