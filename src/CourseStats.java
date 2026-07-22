/**
 * Represents summary statistics calculated over a (possibly filtered) set of course records.
 *
 * @param recordCount               number of records the statistics were calculated over
 * @param avgHrsPerWeek             average hours spent per week
 * @param avgCourseDuration         average course duration, in weeks
 * @param avgCompletionPercent      average completion percentage
 * @param avgSatisfactionScore      average satisfaction score
 */
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
