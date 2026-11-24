package interface_adapter.vote;

import java.util.ArrayList;
import java.util.List;
import use_case.idea_ranking.RankedIdea;

/** Turns the use cases' ranked ideas into the rows the activities screen shows. */
public final class ActivitiesRows {

  private ActivitiesRows() {}

  /**
   * Converts ranked ideas into screen rows.
   *
   * @param ranked the ideas from a use case, best ranked first
   * @return the same ideas as rows for the activities screen
   */
  public static List<ActivitiesState.IdeaRow> fromRanked(List<RankedIdea> ranked) {
    final List<ActivitiesState.IdeaRow> rows = new ArrayList<>();
    for (RankedIdea idea : ranked) {
      rows.add(
          new ActivitiesState.IdeaRow(
              idea.getId(),
              idea.getTitle(),
              idea.getEstimatedCost(),
              idea.getVoteCount(),
              idea.isVotedByUser()));
    }
    return rows;
  }
}
