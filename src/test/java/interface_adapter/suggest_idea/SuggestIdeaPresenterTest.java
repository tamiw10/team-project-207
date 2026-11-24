package interface_adapter.suggest_idea;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import interface_adapter.vote.ActivitiesViewModel;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import use_case.idea_ranking.RankedIdea;
import use_case.suggest_idea.SuggestIdeaOutputData;

class SuggestIdeaPresenterTest {

  private static final LocalDate DAY = LocalDate.of(2027, 2, 14);

  @Test
  void successReplacesTheIdeasAndSaysIdeaAdded() {
    final ActivitiesViewModel model = new ActivitiesViewModel();
    model.getState().setError("old error");
    model.getState().setVotesLeft(2);
    final int[] notified = {0};
    model.addListener(() -> notified[0]++);

    new SuggestIdeaPresenter(model)
        .prepareSuccessView(
            new SuggestIdeaOutputData(
                DAY,
                List.of(
                    new RankedIdea("a", "teamLab", 160, 2, true),
                    new RankedIdea("b", "Ramen", 20, 0, false))));

    assertEquals("Idea added", model.getState().getMessage());
    assertEquals("", model.getState().getError());
    assertEquals(DAY, model.getState().getDay());
    assertEquals(2, model.getState().getRankedIdeas().size());
    assertEquals("Ramen", model.getState().getRankedIdeas().get(1).getTitle());
    assertTrue(model.getState().getRankedIdeas().get(0).isVotedByUser());
    assertEquals(2, model.getState().getVotesLeft());
    assertEquals(1, notified[0]);
  }

  @Test
  void failureShowsErrorAndKeepsIdeas() {
    final ActivitiesViewModel model = new ActivitiesViewModel();
    model.getState().setMessage("Vote counted");
    new SuggestIdeaPresenter(model).prepareFailView("Please enter a title for the idea.");
    assertEquals("Please enter a title for the idea.", model.getState().getError());
    assertEquals("", model.getState().getMessage());
  }
}
