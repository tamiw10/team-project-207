package interface_adapter.log_contribution;

/**
 * View state for Use Case 2: Log a Contribution
 * held by {@link LogContributionViewModel} and rendered by {@link view.LogContributionView}
 */
public class LogContributionState {

    private String tripID = "";
    private String tripName = "";
    // does this mean CAD is the default?
    private String homeCurrency = "CAD";

    // User savings information
    private String totalSaved = "";
    // amountRemaining is not displayed information so might not be necessary
    private String amountRemaining = "";
    private boolean hasSavingsGoal = false;
    private String savingsGoal = "";
    private boolean onTrack = false;
    private double userProgress = 0.0;
    private boolean userGoalReached = false;

    // Weekly savings information
    private String weeksRemaining = "";
    private String weeklyRequired = "";
    private String weeklyPace = "";

    // Groups savings information
    private String groupSaved = "";
    private String groupGoal = "";
    private double groupProgress = 0.0;
    private boolean groupGoalReached = false;

    // Status information
    private String statusMessage = "";
    private boolean error = false;
    private boolean clearForm = false;

    // Getters and setters
    public String getTripID() { return tripID; }
    public void setTripID(String tripID) {
        this.tripID = tripID;
    }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    public String getHomeCurrency() { return homeCurrency; }
    public void setHomeCurrency(String homeCurrency) {
        this.homeCurrency = homeCurrency;
    }

    public String getTotalSaved() { return totalSaved; }
    public void setTotalSaved(String totalSaved) {
        this.totalSaved = totalSaved;
    }
    public String getAmountRemaining() { return amountRemaining; }
    public void setAmountRemaining(String amountRemaining) {
        this.amountRemaining = amountRemaining;
    }
    public String getSavingsGoal() { return savingsGoal; }
    public void setSavingsGoal(String savingsGoal) {
        this.savingsGoal = savingsGoal;
    }
    public boolean isHasSavingsGoal() { return hasSavingsGoal; }
    public void setHasSavingsGoal(boolean hasSavingsGoal) {
        this.hasSavingsGoal = hasSavingsGoal;
    }

    public boolean isOnTrack() { return onTrack; }
    public void setOnTrack(boolean onTrack) {
        this.onTrack = onTrack;
    }
    public double getUserProgress() { return userProgress; }
    public void setUserProgress(double userProgress) {
        this.userProgress = userProgress;
    }
    public boolean isUserGoalReached() { return userGoalReached; }
    public void setUserGoalReached(boolean userGoalReached) {
        this.userGoalReached = userGoalReached;
    }

    public String getWeeksRemaining() { return weeksRemaining; }
    public void setWeeksRemaining(String weeksRemaining) {
        this.weeksRemaining = weeksRemaining;
    }
    public String getWeeklyRequired() { return weeklyRequired; }
    public void setWeeklyRequired(String weeklyRequired) {
        this.weeklyRequired = weeklyRequired;
    }
    public String getWeeklyPace() { return weeklyPace; }
    public void setWeeklyPace(String weeklyPace) {
        this.weeklyPace = weeklyPace;
    }

    public String getGroupSaved() { return groupSaved; }
    public void setGroupSaved(String groupSaved) {
        this.groupSaved = groupSaved;
    }
    public String getGroupGoal() { return groupGoal; }
    public void setGroupGoal(String groupGoal) {
        this.groupGoal = groupGoal;
    }
    public double getGroupProgress() { return groupProgress; }
    public void setGroupProgress(double groupProgress) {
        this.groupProgress = groupProgress;
    }
    public boolean isGroupGoalReached() { return groupGoalReached; }
    public void setGroupGoalReached(boolean groupGoalReached) {
        this.groupGoalReached = groupGoalReached;
    }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    public boolean isError() { return error; }
    public void setError(boolean error) {
        this.error = error;
    }
    public boolean isClearForm() { return clearForm; }
    public void setClearForm(boolean clearForm) {
        this.clearForm = clearForm;
    }
}
