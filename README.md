# online-course-database

Java Swing application for filtering online course records and viewing summary statistics.

## Project structure

- `src/` — source files (default package)
  - `ArrayList.java` — custom generic, resizable list implementation
  - `CourseData.java` — record representing a single, byte-encoded course entry
  - `CourseStats.java` — record representing summary statistics
  - `OnlineCourseDatabaseInterface.java` — provided database contract (unmodified)
  - `OnlineCourseDatabase.java` — loads course data from a file and implements the interface
  - `CourseDataFilteringGui.java` — provided filtering/stats GUI (unmodified)
  - `Main.java` — application entry point
- `data/OnlineCourseDataset.csv` — sample course data

## Build and run

```
javac -d out src/*.java
java -cp out Main data/OnlineCourseDataset.csv
```

If no file path is given, `Main` defaults to `data/OnlineCourseDataset.csv`.
