package use_case.daily_budget;

import entity.Expense;
import entity.Trip;
import java.util.List;

/** Reads the trips and expenses needed by the daily budget use case. */
public interface DailyBudgetDataAccessInterface {
  /**
   * Finds the trips that the given user belongs to.
   *
   * @param username the signed-in username
   * @return the trips that include the user as a member
   */
  List<Trip> getTripsForMember(String username);

  /**
   * Returns the trip currently open in the application.
   *
   * @return the current trip ID, or {@code null} if no trip is open
   */
  String getCurrentTripId();

  /**
   * Records which trip is open.
   *
   * @param tripId the trip ID, or {@code null} to clear the selection
   */
  void setCurrentTripId(String tripId);

  /**
   * Looks up a trip by ID.
   *
   * @param tripId the trip ID to look up
   * @return the matching trip, or {@code null} if it does not exist
   */
  Trip getTrip(String tripId);

  /**
   * Reads all expenses recorded for the trip.
   *
   * @param tripId the trip whose expenses to load
   * @return the trip's expenses, in the order they were logged
   */
  List<Expense> getExpenses(String tripId);

  /**
   * Saves a new daily budget for the trip.
   *
   * @param trip the trip to update
   * @param amount the finite, nonnegative amount in the trip's home currency
   * @return whether the budget was saved
   */
  boolean updateDailyBudget(Trip trip, double amount);
}
