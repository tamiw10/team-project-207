package interface_adapter.suggest_idea;

import interface_adapter.vote.ActivitiesRows;
import interface_adapter.vote.ActivitiesState;
import interface_adapter.vote.ActivitiesViewModel;
import use_case.suggest_idea.SuggestIdeaOutputBoundary;
import use_case.suggest_idea.SuggestIdeaOutputData;

/** The Presenter for the Suggest Idea Use Case. It updates the activities screen's state. */
public class SuggestIdeaPresenter implements SuggestIdeaOutputBoundary {

  private final ActivitiesViewModel activitiesViewModel;

  /**
   * Creates the presenter.
   *
   * @param activitiesViewModel the view model of the activities screen
   */
  public SuggestIdeaPresenter(ActivitiesViewModel activitiesViewModel) {
    this.activitiesViewModel = activitiesViewModel;
  }

  @Override
  public void prepareSuccessView(SuggestIdeaOutputData outputData) {
    final ActivitiesState state = activitiesViewModel.getState();
    state.setDay(outputData.getDay());
    state.setRankedIdeas(ActivitiesRows.fromRanked(outputData.getIdeas()));
    state.setMessage("Idea added");
    state.setError("");
    activitiesViewModel.notifyStateChanged();
  }

  @Override
  public void prepareFailView(String errorMessage) {
    // The ideas on screen stay as they were: nothing was added.
    final ActivitiesState state = activitiesViewModel.getState();
    state.setMessage("");
    state.setError(errorMessage);
    activitiesViewModel.notifyStateChanged();
  }
}
