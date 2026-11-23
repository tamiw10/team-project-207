package use_case.log_contribution;

/**
 * Input Boundary for Use Case 2: Log a Contribution
 * Implemented by {@link LogContributionInteractor}
 */
public interface LogContributionInputBoundary {

    /**
     * Executes the log contribution use case
     *
     * @param inputData the input data
     */
    void execute(LogContributionInputData inputData);
}
