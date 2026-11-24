package use_case.vote;

import java.time.LocalDate;

/** The Input Data for showing one day's activity ideas. */
public class LoadDayInputData {

  private final String tripId;
  private final LocalDate day;
  private final String username;

  /**
   * Creates the input data.
   *
   * @param tripId the trip being viewed
   * @param day the day to show, or null to start on the first day that has ideas
   * @param username the member looking at the ideas
   */
  public LoadDayInputData(String tripId, LocalDate day, String username) {
    this.tripId = tripId;
    this.day = day;
    this.username = username;
  }

  String getTripId() {
    return tripId;
  }

  LocalDate getDay() {
    return day;
  }

  String getUsername() {
    return username;
  }
}
