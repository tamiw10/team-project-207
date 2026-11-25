package use_case.suggest_idea;

import java.time.LocalDate;

/** The Input Data for suggesting a new activity idea for a day of the trip. */
public class SuggestIdeaInputData {

  private final String tripId;
  private final LocalDate day;
  private final String title;
  private final String estimatedCost;
  private final String username;

  /**
   * Creates the input data.
   *
   * @param tripId the trip being viewed
   * @param day the day the idea is for
   * @param title short name of the activity
   * @param estimatedCost expected cost in the trip's home currency, as typed; blank means free
   * @param username the member suggesting the idea
   */
  public SuggestIdeaInputData(
      String tripId, LocalDate day, String title, String estimatedCost, String username) {
    this.tripId = tripId;
    this.day = day;
    this.title = title;
    this.estimatedCost = estimatedCost;
    this.username = username;
  }

  String getTripId() {
    return tripId;
  }

  LocalDate getDay() {
    return day;
  }

  String getTitle() {
    return title;
  }

  String getEstimatedCost() {
    return estimatedCost;
  }

  String getUsername() {
    return username;
  }
}
