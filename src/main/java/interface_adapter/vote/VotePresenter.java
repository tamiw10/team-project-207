package interface_adapter.vote;

import interface_adapter.ViewManagerModel;
import use_case.vote.VoteOutputBoundary;
import use_case.vote.VoteOutputData;

/** The Presenter for the Vote Use Case. */
public class VotePresenter implements VoteOutputBoundary {

  private final ViewManagerModel viewManagerModel;
  private final ActivitiesViewModel activitiesViewModel;

  /**
   * Creates the presenter.
   *
   * @param viewManagerModel used to switch to the activities screen
   * @param activitiesViewModel the view model of the activities screen
   */
  public VotePresenter(ViewManagerModel viewManagerModel, ActivitiesViewModel activitiesViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.activitiesViewModel = activitiesViewModel;
  }

  @Override
  public void prepareSuccessView(VoteOutputData outputData) {
    final ActivitiesState state = activitiesViewModel.getState();
    state.setTripId(outputData.getTripId());
    state.setUsername(outputData.getUsername());
    state.setTripName(outputData.getTripName());
    state.setHomeCurrency(outputData.getHomeCurrency());
    state.setDay(outputData.getDay());
    state.setTripDays(outputData.getTripDays());
    state.setRankedIdeas(ActivitiesRows.fromRanked(outputData.getRankedIdeas()));
    state.setVotesLeft(outputData.getVotesLeft());
    state.setVotesPerDay(outputData.getVotesPerDay());
    state.setError("");
    if (outputData.getOutcome() == VoteOutputData.Outcome.ADDED) {
      state.setMessage("Vote counted");
    } else if (outputData.getOutcome() == VoteOutputData.Outcome.REMOVED) {
      state.setMessage("Vote removed");
    } else {
      state.setMessage("");
    }
    activitiesViewModel.notifyStateChanged();
  }

  @Override
  public void prepareFailView(String errorMessage) {
    // The ideas and the vote counts on screen stay as they were: nothing changed.
    final ActivitiesState state = activitiesViewModel.getState();
    state.setMessage("");
    state.setError(errorMessage);
    activitiesViewModel.notifyStateChanged();
  }

  @Override
  public void switchToActivitiesView() {
    viewManagerModel.setState(ActivitiesViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();
  }
}
