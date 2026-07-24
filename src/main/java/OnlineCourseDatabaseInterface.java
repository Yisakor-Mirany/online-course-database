/**
 * Expresses method requirements for online course database implementers.
 * Do not alter this file in any way.  If your code doesn't work with the project-provided
 *      version of this interface, your work will be rejected and returned for rework.
 */
public interface OnlineCourseDatabaseInterface {

    /**
     * Retrieves the course record at the specified index in the database
     * @param index     the index of the desired rouse record
     * @return          the course record at the specified position
     */
    public CourseData getCourseRecordAt(int index);

    /**
     * Retrieves a course record with all student option indexes turned into descriptive strings.
     *      For example, with the last row of data in the sample file, this string would be returned:
     *      "[U0500, Working Professional, Non-Tech, Coursera, 6, 22, In Progress, 85, No Dropout, 5]"
     *
     * @param course    online course record to render
     * @return          human-readable, complete representation of the course record
     */
    public String getCourseRecordString(CourseData course);

    /**
     * Returns the number of records managed by the system
     * @return      number of course records managed by the system
     */
    public int size();

    /**
     * Creates and returns an array representing all possible experience level options
     * @return      array containing options
     */
    public String[] getExperienceLevelOptions();

    /**
     * Creates and returns an array representing all possible course type options
     * @return      array containing options
     */
    public String[] getCourseTypeOptions();

    /**
     * Creates and returns an array representing all possible platform options
     * @return      array containing options
     */
    public String[] getPlatformOptions();

    /**
     * Creates and returns an array representing all possible completion status options
     * @return      array containing options
     */
    public String[] getCompletionStatusOptions();

    /**
     * Creates and returns an array representing all dropout options
     * @return      array containing options
     */
    public String[] getDropoutReasonOptions();

    /**
     * Calculates and returns statistics based on filtered results
     * @param experienceLevelIndex      index of the experience level to filter, or -1 for no filtering on this field
     * @param courseTypeIndex           index of the course type to filter, or -1 for no filtering on this field
     * @param platformIndex             index of the platform to filter, or -1 for no filtering on this field
     * @param completionStatusIndex     index of the completion status to filter, or -1 for no filtering on this field
     * @param dropoutReasonIndex        index of the dropout reason to filter, or -1 for no filtering on this field
     * @return  statistics for the results of filtering operation.  If filtering results in no records, will contain
     *          a count of 0 and other stats set to -1.0
     */
    public CourseStats calcFilteredAverages(byte experienceLevelIndex,
                                           byte courseTypeIndex,
                                           byte platformIndex,
                                           byte completionStatusIndex,
                                           byte dropoutReasonIndex);

}
