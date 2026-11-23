package use_case.log_contribution;

import java.time.LocalDate;

/** Input data for the Use Case 2: Log a Contribution */
public class LogContributionInputData {
    private final String tripId;
    private final String amountText;
    private final String dateText;
    private final String savingsGoalText;

    public LogContributionInputData(String tripId, String amountText, String dateText,
                                    String savingsGoalText) {
        this.tripId = tripId;
        this.amountText = amountText;
        this.dateText = dateText;
        this.savingsGoalText = savingsGoalText;
    }

    public String getTripId() {
        return tripId;
    }

    public String getAmount() {
        return amountText;
    }

    public String getDate() {
        return dateText;
    }

    public String getSavingsGoal() {
        return savingsGoalText;
    }
}
