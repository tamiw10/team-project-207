package use_case.vote;

import java.time.LocalDate;

/** The Input Data for the Vote Use Case. */
public class VoteInputData {

  private final String tripId;
  private final LocalDate day;
  private final String ideaId;
  private final String username;

  /**
   * Creates the input data.
   *
   * @param tripId the trip being viewed
   * @param day the day of the trip whose ideas are shown
   * @param ideaId the idea the user clicked Vote or Unvote on
   * @param username the member voting
   */
  public VoteInputData(String tripId, LocalDate day, String ideaId, String username) {
    this.tripId = tripId;
    this.day = day;
    this.ideaId = ideaId;
    this.username = username;
  }

  String getTripId() {
    return tripId;
  }

  LocalDate getDay() {
    return day;
  }

  String getIdeaId() {
    return ideaId;
  }

  String getUsername() {
    return username;
  }
}
