package interface_adapter.vote;

import use_case.vote.VoteOutputBoundary;
import use_case.vote.VoteOutputData;

/** The Presenter for the Vote Use Case. */
public class VotePresenter implements VoteOutputBoundary {

  private final ActivitiesViewModel activitiesViewModel;

  public VotePresenter(ActivitiesViewModel activitiesViewModel) {
    this.activitiesViewModel = activitiesViewModel;
  }

  @Override
  public void prepareSuccessView(VoteOutputData outputData) {
    final ActivitiesState state = activitiesViewModel.getState();
    state.setTripName(outputData.getTripName());
    state.setHomeCurrency(outputData.getHomeCurrency());
    state.setDay(outputData.getDay());
    state.setTripDays(outputData.getTripDays());
    state.setRankedIdeas(outputData.getRankedIdeas());
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
}
