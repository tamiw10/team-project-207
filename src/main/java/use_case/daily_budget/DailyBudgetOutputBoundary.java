package use_case.daily_budget;

import java.util.List;

/** Sends trip choices, calculated summaries, and errors to the presenter. */
public interface DailyBudgetOutputBoundary {
  /**
   * Shows the available trip choices when opening the budget screen.
   *
   * @param trips the signed-in member's trips, with the current trip first if available
   */
  void showTrips(List<DailyBudgetTripOption> trips);

  /**
   * Presents a calculated budget summary.
   *
   * @param summary the budget figures for the selected day
   * @param budgetUpdated whether this request saved a new daily budget
   */
  void showSummary(DailyBudgetOutputData summary, boolean budgetUpdated);

  /**
   * Presents a failure while retaining the previous summary.
   *
   * @param message the reason the request failed
   */
  void showError(String message);
}
