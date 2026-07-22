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

    public CourseData {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }

    @Override
    public String toString() {
        return "[" + userId + ", " + experienceLevel + ", " + courseType + ", " + platform + ", "
                + hoursPerWeek + ", " + courseDuration + ", " + completionStatus + ", "
                + completionPercentage + ", " + dropoutReason + ", " + satisfactionScore + "]";
    }
}
