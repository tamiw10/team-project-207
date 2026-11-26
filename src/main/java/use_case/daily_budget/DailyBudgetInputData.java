package use_case.daily_budget;

import java.time.LocalDate;

/** Identifies the trip and day for a budget summary. */
public final class DailyBudgetInputData {
  private final String tripId;
  private final LocalDate date;

  /**
   * Creates a request for a trip day's budget.
   *
   * @param tripId the selected trip ID
   * @param date the selected day
   */
  public DailyBudgetInputData(String tripId, LocalDate date) {
    this.tripId = tripId;
    this.date = date;
  }

  /**
   * Returns the selected trip ID.
   *
   * @return the selected trip ID
   */
  public String getTripId() {
    return tripId;
  }

  /**
   * Returns the selected day.
   *
   * @return the selected day
   */
  public LocalDate getDate() {
    return date;
  }
}
