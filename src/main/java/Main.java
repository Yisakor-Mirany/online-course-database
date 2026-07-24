import java.io.File;
import java.io.FileNotFoundException;

/** Application entry point; loads the course database and launches the filtering GUI. */
public class Main {

    /** default location of the course data file, used when none is given on the command line */
    private final static String DEFAULT_DB_FILE = "data/OnlineCourseDataset.csv";

    /** not meant to be instantiated */
    private Main() {
    }

    /**
     * Loads the course database and displays the filtering GUI.
     * @param args args[0], if present, is the path to the course data file to load;
     *             otherwise a default path is used
     * @throws FileNotFoundException if the course data file cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException {
        String path = args.length > 0 ? args[0] : DEFAULT_DB_FILE;
        new CourseDataFilteringGui(new OnlineCourseDatabase(new File(path)));
    }
}
