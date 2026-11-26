package interface_adapter.daily_budget;

import java.util.List;
import use_case.daily_budget.DailyBudgetOutputData;
import use_case.daily_budget.DailyBudgetTripOption;

/** The visible trip choices, last valid summary, and feedback. */
public final class DailyBudgetState {
  private final List<DailyBudgetTripOption> trips;
  private final DailyBudgetOutputData summary;
  private final String message;
  private final boolean error;

  /**
   * Captures one state of the budget screen.
   *
   * @param trips the available trip choices
   * @param summary the last valid summary, or {@code null} if no day was loaded
   * @param message the feedback to display, or an empty string
   * @param error whether the feedback describes a failure
   */
  DailyBudgetState(
      List<DailyBudgetTripOption> trips, DailyBudgetOutputData summary,
      String message, boolean error) {
    this.trips = List.copyOf(trips);
    this.summary = summary;
    this.message = message;
    this.error = error;
  }

  /**
   * Returns the available trip choices.
   *
   * @return the available trip choices
   */
  public List<DailyBudgetTripOption> getTrips() {
    return trips;
  }

  /**
   * Returns the last valid summary, or {@code null} if none was loaded.
   *
   * @return the last valid summary, or {@code null} if none was loaded
   */
  public DailyBudgetOutputData getSummary() {
    return summary;
  }

  /**
   * Returns the feedback text, or an empty string if there is no feedback.
   *
   * @return the feedback text, or an empty string if there is no feedback
   */
  public String getMessage() {
    return message;
  }

  /**
   * Reports whether the feedback describes an error.
   *
   * @return whether the feedback describes an error
   */
  public boolean hasError() {
    return error;
  }
}
