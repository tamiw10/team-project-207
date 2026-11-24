package interface_adapter.log_contribution;

import use_case.log_contribution.LogContributionInputBoundary;
import use_case.log_contribution.LogContributionInputData;

import java.time.LocalDate;

/** Controller for Use Case 2: Log a Contribution*/
public class LogContributionController {
    // receives raw user data from view, creates input data object
    private final LogContributionInputBoundary logContributionUseCaseInteractor;


    public LogContributionController(LogContributionInputBoundary logContributionUseCaseInteractor) {
        this.logContributionUseCaseInteractor = logContributionUseCaseInteractor;
    }

    /** Executes Log Contribution use case
     *
     * @param tripID the id of the trip the contribution is for
     * @param amountText the amount contributed towards the goal as entered
     * @param dateText the date logged as entered
     * @param savingsGoalText the user's savings goal as entered
     */
    public void execute(String tripID, String amountText, String dateText, String savingsGoalText) {
        final LogContributionInputData inputData =
                new LogContributionInputData(tripID, amountText, dateText, savingsGoalText);

        logContributionUseCaseInteractor.execute(inputData);
    }
}
