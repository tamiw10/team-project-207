package use_case.vote;

/** Input Boundary for actions related to voting on a day's activity ideas. */
public interface VoteInputBoundary {

  /**
   * Executes the vote use case: votes for an idea, or takes the vote back if the user had already
   * voted for it.
   *
   * @param voteInputData the input data
   */
  void execute(VoteInputData voteInputData);

  /**
   * Opens the activities screen for the trip the user has open, or for the first trip they belong
   * to when none is open, starting on the first day that has ideas.
   *
   * @param openActivitiesInputData the input data
   */
  void open(OpenActivitiesInputData openActivitiesInputData);

  /**
   * Shows a day's ideas ranked by votes without changing anything. Used when the user opens the
   * activities screen or picks another day.
   *
   * @param loadDayInputData the input data
   */
  void loadDay(LoadDayInputData loadDayInputData);
}
