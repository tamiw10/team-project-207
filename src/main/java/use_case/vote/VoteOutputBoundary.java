package use_case.vote;

/** The output boundary for the Vote Use Case. */
public interface VoteOutputBoundary {

  /**
   * Prepares the success view: the day's ranked ideas and the user's remaining votes.
   *
   * @param outputData the output data
   */
  void prepareSuccessView(VoteOutputData outputData);

  /**
   * Prepares the failure view.
   *
   * @param errorMessage the explanation of the failure
   */
  void prepareFailView(String errorMessage);

  /** Switches to the activities screen. */
  void switchToActivitiesView();
}
