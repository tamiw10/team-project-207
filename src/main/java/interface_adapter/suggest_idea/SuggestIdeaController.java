package interface_adapter.suggest_idea;

import java.time.LocalDate;
import use_case.suggest_idea.SuggestIdeaInputBoundary;
import use_case.suggest_idea.SuggestIdeaInputData;

/** The controller for the Suggest Idea Use Case. */
public class SuggestIdeaController {

  private final SuggestIdeaInputBoundary suggestIdeaInteractor;

  public SuggestIdeaController(SuggestIdeaInputBoundary suggestIdeaInteractor) {
    this.suggestIdeaInteractor = suggestIdeaInteractor;
  }

  /**
   * Adds a new activity idea for a day.
   *
   * @param tripId the trip being viewed
   * @param day the day the idea is for
   * @param title short name of the activity
   * @param estimatedCost expected cost as typed; blank means free
   * @param username the member suggesting the idea
   */
  public void execute(
      String tripId, LocalDate day, String title, String estimatedCost, String username) {
    suggestIdeaInteractor.execute(
        new SuggestIdeaInputData(tripId, day, title, estimatedCost, username));
  }
}
