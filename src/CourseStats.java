public record CourseStats(int recordCount,
                           double avgHrsPerWeek,
                           double avgCourseDuration,
                           double avgCompletionPercent,
                           double avgSatisfactionScore) {

    @Override
    public String toString() {
        return String.format("CourseStats[recordCount=%d, avgHrsPerWeek=%.2f, avgCourseDuration=%.2f, "
                        + "avgCompletionPercent=%.2f, avgSatisfactionScore=%.2f]",
                recordCount, avgHrsPerWeek, avgCourseDuration, avgCompletionPercent, avgSatisfactionScore);
    }
}
