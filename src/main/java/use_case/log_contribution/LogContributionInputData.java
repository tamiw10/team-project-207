package use_case.log_contribution;

import java.time.LocalDate;

public class LogContributionInputData {
    private final String tripId;
    private final double amount;
    private final LocalDate date;
    private final Double savingsGoal;

    public LogContributionInputData(
            String tripId,
            double amount,
            LocalDate date,
            Double savingsGoal) {

        this.tripId = tripId;
        this.amount = amount;
        this.date = date;
        this.savingsGoal = savingsGoal;
    }

    public String getTripId() {
        return tripId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getSavingsGoal() {
        return savingsGoal;
    }
}
