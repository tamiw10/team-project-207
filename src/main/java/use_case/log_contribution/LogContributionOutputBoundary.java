package use_case.log_contribution;

public interface LogContributionOutputBoundary {
    void prepareSuccessView(LogContributionOutputData outputData);

    void prepareFailView(String error);
}
