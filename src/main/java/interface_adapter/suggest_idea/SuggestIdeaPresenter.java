package interface_adapter.suggest_idea;

import interface_adapter.vote.ActivitiesState;
import interface_adapter.vote.ActivitiesViewModel;
import java.util.ArrayList;
import java.util.List;
import use_case.suggest_idea.SuggestIdeaOutputBoundary;
import use_case.suggest_idea.SuggestIdeaOutputData;
import use_case.vote.RankedIdea;

/** The Presenter for the Suggest Idea Use Case. It updates the activities screen's state. */
public class SuggestIdeaPresenter implements SuggestIdeaOutputBoundary {

  private final ActivitiesViewModel activitiesViewModel;

  public SuggestIdeaPresenter(ActivitiesViewModel activitiesViewModel) {
    this.activitiesViewModel = activitiesViewModel;
  }

  @Override
  public void prepareSuccessView(SuggestIdeaOutputData outputData) {
    final List<RankedIdea> rows = new ArrayList<>();
    for (SuggestIdeaOutputData.IdeaRow idea : outputData.getIdeas()) {
      rows.add(
          new RankedIdea(
              idea.getId(),
              idea.getTitle(),
              idea.getEstimatedCost(),
              idea.getVoteCount(),
              idea.isVotedByUser()));
    }
    final ActivitiesState state = activitiesViewModel.getState();
    state.setDay(outputData.getDay());
    state.setRankedIdeas(rows);
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
