package use_case.log_contribution;

/**
 * Output boundary for Use Case 2: Log a Contribution
 * Implemented by {@link interface_adapter.log_contribution.LogContributionPresenter}.
 */
public interface LogContributionOutputBoundary {
    /**
     * Prepares the view model when the contribution has been validated, converted and saved
     *
     * @param outputData the {@link LogContributionOutputData} containing the updated data from the new contribution
     */
    void prepareSuccessView(LogContributionOutputData outputData);

    /**
     * Prepares the view model when the validation of the contribution fails
     *
     * @param errorMessage a user-facing message explaining why the contribution was not recorded
     */
    void prepareFailView(String errorMessage);
}
