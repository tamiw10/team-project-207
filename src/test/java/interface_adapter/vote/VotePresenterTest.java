package interface_adapter.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.Test;
import use_case.idea_ranking.RankedIdea;
import use_case.vote.VoteOutputData;

class VotePresenterTest {

  private static final LocalDate DAY = LocalDate.of(2027, 2, 14);

  private VoteOutputData output(VoteOutputData.Outcome outcome) {
    return new VoteOutputData(
        "t1",
        "amelia",
        "Tokyo",
        "CAD",
        DAY,
        List.of(DAY),
        List.of(new RankedIdea("a", "teamLab", 160, 2, true)),
        1,
        3,
        outcome);
  }

  @Test
  void successFillsStateAndNotifies() {
    final ActivitiesViewModel model = new ActivitiesViewModel();
    final int[] notified = {0};
    model.addListener(() -> notified[0]++);
    final VotePresenter presenter = new VotePresenter(new ViewManagerModel(), model);

    presenter.prepareSuccessView(output(VoteOutputData.Outcome.ADDED));
    assertEquals("Vote counted", model.getState().getMessage());
    assertEquals(1, model.getState().getVotesLeft());
    assertEquals(3, model.getState().getVotesPerDay());
    assertEquals(1, model.getState().getRankedIdeas().size());
    assertEquals("Tokyo", model.getState().getTripName());
    assertEquals("t1", model.getState().getTripId());
    assertEquals("amelia", model.getState().getUsername());

    presenter.prepareSuccessView(output(VoteOutputData.Outcome.REMOVED));
    assertEquals("Vote removed", model.getState().getMessage());

    presenter.prepareSuccessView(output(VoteOutputData.Outcome.NONE));
    assertEquals("", model.getState().getMessage());
    assertEquals(3, notified[0]);
  }

  @Test
  void failureKeepsIdeasAndShowsError() {
    final ActivitiesViewModel model = new ActivitiesViewModel();
    final VotePresenter presenter = new VotePresenter(new ViewManagerModel(), model);
    presenter.prepareSuccessView(output(VoteOutputData.Outcome.ADDED));
    presenter.prepareFailView("No votes left today. Take a vote back first.");
    assertEquals("No votes left today. Take a vote back first.", model.getState().getError());
    assertEquals("", model.getState().getMessage());
    assertEquals(1, model.getState().getRankedIdeas().size());
    presenter.prepareSuccessView(output(VoteOutputData.Outcome.NONE));
    assertEquals("", model.getState().getError());
  }

  @Test
  void switchingShowsTheActivitiesView() {
    final ViewManagerModel views = new ViewManagerModel();
    new VotePresenter(views, new ActivitiesViewModel()).switchToActivitiesView();
    assertEquals(ActivitiesViewModel.VIEW_NAME, views.getCurrentViewName());
  }
}
