package interface_adapter.log_contribution;

import use_case.log_contribution.LogContributionOutputBoundary;
import use_case.log_contribution.LogContributionOutputData;

import java.text.NumberFormat;

/** Presenter for Use Case 2: Log a Contribution */
public class LogContributionPresenter implements LogContributionOutputBoundary {

    private final LogContributionViewModel logContributionViewModel;

    public LogContributionPresenter(LogContributionViewModel logContributionViewModel) {
        this.logContributionViewModel = logContributionViewModel;
    }

    @Override
    public void prepareSuccessView(LogContributionOutputData outputData) {
        final LogContributionState state = logContributionViewModel.getState();
        state.setTripID(outputData.getTripID());
        state.setTripName(outputData.getTripName());
        state.setHomeCurrency(outputData.getHomeCurrency());
        state.setHasSavingsGoal(outputData.isHasSavingsGoal());

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        state.setTotalSaved("$" + formatter.format(outputData.getTotalSaved()));
        state.setSavingsGoal("$" + formatter.format(outputData.getSavingsGoal()));
        state.setOnTrack(outputData.isOnTrack());
        state.setUserProgress(outputData.getUserProgress());
        state.setUserGoalReached(outputData.isUserGoalReached());
        state.setWeeksRemaining(Double.toString(outputData.getWeeksRemaining()));

        if (outputData.isUserGoalReached()) {
            state.setWeeklyRequired("");
            state.setWeeklyPace("");
        } else {
            state.setWeeklyRequired("$" + formatter.format(outputData.getWeeklyRequired()));
            state.setWeeklyPace("$" + formatter.format(outputData.getWeeklyPace()));
        }

        state.setGroupSaved("$" + formatter.format(outputData.getGroupSaved()));
        state.setGroupGoal("$" + formatter.format(outputData.getGroupGoal()));
        state.setGroupProgress(outputData.getGroupProgress());
        state.setGroupGoalReached(outputData.isGroupGoalReached());
        state.setStatusMessage("Contribution Saved");
        state.setError(false);
        state.setClearForm(true);
        logContributionViewModel.notifyStateChanged();
        // Reset the flag for future updates
        state.setClearForm(false);

    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LogContributionState state = logContributionViewModel.getState();
        state.setStatusMessage(errorMessage);
        state.setError(true);
        state.setClearForm(false);
        logContributionViewModel.notifyStateChanged();
    }

}
