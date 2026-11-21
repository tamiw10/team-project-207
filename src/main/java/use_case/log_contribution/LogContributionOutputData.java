package use_case.log_contribution;

public class LogContributionOutputData {
    private final double savingsGoal;
    private final double totalSaved;
    private final double remaining;
    private final int weeksRemaining;
    private final Double weeklyRequired;
    private final Double weeklyPace;
    private final boolean onTrack;
    private final double groupSaved;
    private final double groupGoal;
    private final boolean goalReached;

    public LogContributionOutputData(
            double savingsGoal,
            double totalSaved,
            double remaining,
            int weeksRemaining,
            Double weeklyRequired,
            Double weeklyPace,
            boolean onTrack,
            double groupSaved,
            double groupGoal,
            boolean goalReached) {

        this.savingsGoal = savingsGoal;
        this.totalSaved = totalSaved;
        this.remaining = remaining;
        this.weeksRemaining = weeksRemaining;
        this.weeklyRequired = weeklyRequired;
        this.weeklyPace = weeklyPace;
        this.onTrack = onTrack;
        this.groupSaved = groupSaved;
        this.groupGoal = groupGoal;
        this.goalReached = goalReached;
    }

    public double getSavingsGoal() {
        return savingsGoal;
    }

    public double getTotalSaved() {
        return totalSaved;
    }

    public double getRemaining() {
        return remaining;
    }

    public int getWeeksRemaining() {
        return weeksRemaining;
    }

    public Double getWeeklyRequired() {
        return weeklyRequired;
    }

    public Double getWeeklyPace() {
        return weeklyPace;
    }

    public boolean isOnTrack() {
        return onTrack;
    }

    public double getGroupSaved() {
        return groupSaved;
    }

    public double getGroupGoal() {
        return groupGoal;
    }

    public boolean isGoalReached() {
        return goalReached;
    }
}
