package use_case.suggest_idea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Output Data for the Suggest Idea Use Case: the day's ideas, ranked, with the new one in. */
public class SuggestIdeaOutputData {

  private final LocalDate day;
  private final List<IdeaRow> ideas;

  /**
   * Creates the output data.
   *
   * @param day the day the idea was added to
   * @param ideas that day's ideas, best ranked first
   */
  public SuggestIdeaOutputData(LocalDate day, List<IdeaRow> ideas) {
    this.day = day;
    this.ideas = new ArrayList<>(ideas);
  }

  public LocalDate getDay() {
    return day;
  }

  public List<IdeaRow> getIdeas() {
    return Collections.unmodifiableList(ideas);
  }

  /** One idea in the day's list. */
  public static class IdeaRow {

    private final String id;
    private final String title;
    private final double estimatedCost;
    private final int voteCount;
    private final boolean votedByUser;

    /**
     * Creates a row.
     *
     * @param id the idea's id
     * @param title the activity's name
     * @param estimatedCost expected cost in the trip's home currency
     * @param voteCount how many members voted for it
     * @param votedByUser whether the user who suggested the new idea has voted for this one
     */
    public IdeaRow(
        String id, String title, double estimatedCost, int voteCount, boolean votedByUser) {
      this.id = id;
      this.title = title;
      this.estimatedCost = estimatedCost;
      this.voteCount = voteCount;
      this.votedByUser = votedByUser;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public double getEstimatedCost() {
      return estimatedCost;
    }

    public int getVoteCount() {
      return voteCount;
    }

    public boolean isVotedByUser() {
      return votedByUser;
    }
  }
}
