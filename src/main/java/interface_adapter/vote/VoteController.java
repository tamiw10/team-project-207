package interface_adapter.vote;

import java.time.LocalDate;
import use_case.vote.LoadDayInputData;
import use_case.vote.OpenActivitiesInputData;
import use_case.vote.VoteInputBoundary;
import use_case.vote.VoteInputData;

/** The controller for the Vote Use Case. */
public class VoteController {

  private final VoteInputBoundary voteInteractor;

  /**
   * Creates the controller.
   *
   * @param voteInteractor the use case this controller starts
   */
  public VoteController(VoteInputBoundary voteInteractor) {
    this.voteInteractor = voteInteractor;
  }

  /**
   * Votes for an idea, or takes the vote back if the user already voted for it.
   *
   * @param tripId the trip being viewed
   * @param day the day whose ideas are shown
   * @param ideaId the idea the user clicked Vote or Unvote on
   * @param username the member voting
   */
  public void vote(String tripId, LocalDate day, String ideaId, String username) {
    voteInteractor.execute(new VoteInputData(tripId, day, ideaId, username));
  }

  /**
   * Opens the activities screen for the trip the user has open.
   *
   * @param username the member opening the screen
   */
  public void open(String username) {
    voteInteractor.open(new OpenActivitiesInputData(username));
  }

  /**
   * Shows a day's ideas.
   *
   * @param tripId the trip being viewed
   * @param day the day to show, or null for the first day that has ideas
   * @param username the member looking at the ideas
   */
  public void showDay(String tripId, LocalDate day, String username) {
    voteInteractor.loadDay(new LoadDayInputData(tripId, day, username));
  }
}
