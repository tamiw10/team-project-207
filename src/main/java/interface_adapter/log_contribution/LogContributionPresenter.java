package interface_adapter.log_contribution;

import interface_adapter.ViewManagerModel;
import use_case.log_contribution.LogContributionOutputBoundary;
import use_case.log_contribution.LogContributionOutputData;

/** Presenter for Use Case 2: Log a Contribution */
public class LogContributionPresenter implements LogContributionOutputBoundary {

    private final LogContributionViewModel logContributionViewModel;
    // is ViewManagerModel necessary? just copying signup presenter
    private final ViewManagerModel viewManagerModel;

    public LogContributionPresenter(LogContributionViewModel logContributionViewModel,
                                    ViewManagerModel viewManagerModel) {
        this.logContributionViewModel = logContributionViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LogContributionOutputData outputData) {
        final LogContributionState state = logContributionViewModel.getState();
        state.setTripID(outputData.getTripID());
        state.setTripName(outputData.getTripName());
        state.setHomeCurrency(outputData.getHomeCurrency());
        state.setTotalSaved("$" + Double.toString(outputData.getTotalSaved()));
        state.setAmountRemaining("$" + Double.toString(outputData.getAmountRemaining()));
        state.setSavingsGoal("$" + Double.toString(outputData.getSavingsGoal()));
        state.setOnTrack(outputData.isOnTrack());
        state.setUserProgress(outputData.getUserProgress());
        state.setUserGoalReached(outputData.isUserGoalReached());
        state.setGroupGoalReached(outputData.isGroupGoalReached());
        state.setWeeksRemaining(Double.toString(outputData.getWeeksRemaining()));
        state.setWeeklyRequired("$" + Double.toString(outputData.getWeeklyRequired()));
        state.setWeeklyPace("$" + Double.toString(outputData.getWeeklyPace()));
        state.setGroupSaved("$" + Double.toString(outputData.getGroupSaved()));
        state.setGroupGoal("$" + Double.toString(outputData.getGroupGoal()));
        state.setStatusMessage("Contribution Saved");
        state.setError(false);
        state.setClearForm(false);
        logContributionViewModel.notifyStateChanged();

    }

    @Override
    public void prepareFailView(String errorMessage) {
        //...
    }

}
