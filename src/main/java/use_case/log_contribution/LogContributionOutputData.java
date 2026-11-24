package use_case.log_contribution;

/** Output data for Use Case 2: Log a Contribution */
public class LogContributionOutputData {
    private final String tripID;
    private final String tripName;
    private final String homeCurrency;
    private final double savingsGoal;
    private final double totalSaved;
    private final double amountRemaining;
    private final int weeksRemaining;
    private final Double weeklyRequired;
    private final Double weeklyPace;
    private final boolean onTrack;
    private final double groupSaved;
    private final double groupGoal;
    private final boolean userGoalReached;
    private final boolean groupGoalReached;
    private final double userProgress;
    private final double groupProgress;

    public LogContributionOutputData(String tripID,
                                     String tripName,
                                     String homeCurrency,
                                     double savingsGoal,
                                     double totalSaved,
                                     double amountRemaining,
                                     int weeksRemaining,
                                     Double weeklyRequired,
                                     Double weeklyPace,
                                     boolean onTrack,
                                     double groupSaved,
                                     double groupGoal,
                                     boolean userGoalReached,
                                     boolean groupGoalReached,
                                     double userProgress,
                                     double groupProgress) {
        this.tripID = tripID;
        this.tripName = tripName;
        this.homeCurrency = homeCurrency;
        this.savingsGoal = savingsGoal;
        this.totalSaved = totalSaved;
        this.amountRemaining = amountRemaining;
        this.weeksRemaining = weeksRemaining;
        this.weeklyRequired = weeklyRequired;
        this.weeklyPace = weeklyPace;
        this.onTrack = onTrack;
        this.groupSaved = groupSaved;
        this.groupGoal = groupGoal;
        this.userGoalReached = userGoalReached;
        this.groupGoalReached = groupGoalReached;
        this.userProgress = userProgress;
        this.groupProgress = groupProgress;
    }

    public String getTripID() { return tripID; }

    public String getTripName() { return tripName; }

    public String getHomeCurrency() { return homeCurrency; }

    public double getSavingsGoal() {
        return savingsGoal;
    }

    public double getTotalSaved() {
        return totalSaved;
    }

    public double getAmountRemaining() {
        return amountRemaining;
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

    public boolean isUserGoalReached() {
        return userGoalReached;
    }

    public boolean isGroupGoalReached() { return groupGoalReached; }

    public double getUserProgress() { return userProgress; }

    public double getGroupProgress() { return groupProgress; }
}
