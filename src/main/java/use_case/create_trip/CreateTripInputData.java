package use_case.create_trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The input data for the Create Trip Use Case: exactly what the user typed into the form. Dates and
 * the budget are still text here, because deciding whether they are valid is the interactor's job.
 */
public class CreateTripInputData {

  private final String name;
  private final String destination;
  private final String startDate;
  private final String endDate;
  private final String homeCurrency;
  private final String dailyBudget;
  private final List<String> friendUsernames;

  /**
   * Creates the input data.
   *
   * @param name the trip name
   * @param destination where the group is going
   * @param startDate the first day, as typed (yyyy-mm-dd)
   * @param endDate the last day, as typed (yyyy-mm-dd)
   * @param homeCurrency the home currency code, as typed
   * @param dailyBudget the daily budget, as typed
   * @param friendUsernames the usernames of the friends to invite (can be empty)
   */
  public CreateTripInputData(
      String name,
      String destination,
      String startDate,
      String endDate,
      String homeCurrency,
      String dailyBudget,
      List<String> friendUsernames) {
    this.name = name;
    this.destination = destination;
    this.startDate = startDate;
    this.endDate = endDate;
    this.homeCurrency = homeCurrency;
    this.dailyBudget = dailyBudget;
    this.friendUsernames = new ArrayList<>(friendUsernames);
  }

  String getName() {
    return name;
  }

  String getDestination() {
    return destination;
  }

  String getStartDate() {
    return startDate;
  }

  String getEndDate() {
    return endDate;
  }

  String getHomeCurrency() {
    return homeCurrency;
  }

  String getDailyBudget() {
    return dailyBudget;
  }

  List<String> getFriendUsernames() {
    return Collections.unmodifiableList(friendUsernames);
  }
}
