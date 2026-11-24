package use_case.create_trip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The output data for the Create Trip Use Case: the trip that was just created. */
public class CreateTripOutputData {

  private final String tripId;
  private final String name;
  private final String destination;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String homeCurrency;
  private final double dailyBudget;
  private final List<String> memberUsernames;

  /**
   * Creates the output data.
   *
   * @param tripId the id of the new trip
   * @param name the trip name
   * @param destination where the group is going
   * @param startDate the first day
   * @param endDate the last day
   * @param homeCurrency the home currency code
   * @param dailyBudget the daily budget in the home currency
   * @param memberUsernames the members, creator first
   */
  public CreateTripOutputData(
      String tripId,
      String name,
      String destination,
      LocalDate startDate,
      LocalDate endDate,
      String homeCurrency,
      double dailyBudget,
      List<String> memberUsernames) {
    this.tripId = tripId;
    this.name = name;
    this.destination = destination;
    this.startDate = startDate;
    this.endDate = endDate;
    this.homeCurrency = homeCurrency;
    this.dailyBudget = dailyBudget;
    this.memberUsernames = new ArrayList<>(memberUsernames);
  }

  public String getTripId() {
    return tripId;
  }

  public String getName() {
    return name;
  }

  public String getDestination() {
    return destination;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public String getHomeCurrency() {
    return homeCurrency;
  }

  public double getDailyBudget() {
    return dailyBudget;
  }

  public List<String> getMemberUsernames() {
    return Collections.unmodifiableList(memberUsernames);
  }
}
