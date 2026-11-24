package use_case.idea_ranking;

/** One activity idea as the user sees it in a day's ranking. */
public class RankedIdea {

  private final String id;
  private final String title;
  private final double estimatedCost;
  private final int voteCount;
  private final boolean votedByUser;

  /**
   * Creates a ranked idea.
   *
   * @param id the idea's id
   * @param title the activity's name
   * @param estimatedCost expected cost in the trip's home currency
   * @param voteCount how many members voted for it
   * @param votedByUser whether the user who is looking has voted for it
   */
  public RankedIdea(
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
