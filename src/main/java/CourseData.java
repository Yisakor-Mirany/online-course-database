/**
 * A single, byte-encoded online course record. The categorical fields (experienceLevel,
 * courseType, platform, completionStatus, dropoutReason) store indexes into the corresponding
 * option lists maintained by the database, rather than descriptive text.
 *
 * @param userId               unique identifier of the student
 * @param experienceLevel      index into the experience level options
 * @param courseType           index into the course type options
 * @param platform             index into the platform options
 * @param hoursPerWeek         hours spent per week on the course
 * @param courseDuration       duration of the course, in weeks
 * @param completionStatus     index into the completion status options
 * @param completionPercentage percentage of the course completed
 * @param dropoutReason        index into the dropout reason options
 * @param satisfactionScore    satisfaction score reported by the student
 */
public record CourseData(String userId,
                          byte experienceLevel,
                          byte courseType,
                          byte platform,
                          byte hoursPerWeek,
                          byte courseDuration,
                          byte completionStatus,
                          byte completionPercentage,
                          byte dropoutReason,
                          byte satisfactionScore) {

    /** Validates that userId is non-null. */
    public CourseData {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }
}
