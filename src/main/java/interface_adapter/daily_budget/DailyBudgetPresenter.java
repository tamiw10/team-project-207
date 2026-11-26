package interface_adapter.daily_budget;

import java.util.List;
import use_case.daily_budget.DailyBudgetOutputBoundary;
import use_case.daily_budget.DailyBudgetOutputData;
import use_case.daily_budget.DailyBudgetTripOption;

/** Updates the budget screen after a request succeeds or fails. */
public final class DailyBudgetPresenter implements DailyBudgetOutputBoundary {
  private final DailyBudgetViewModel model;

  /**
   * Creates a presenter for the budget screen.
   *
   * @param model the view model to update
   */
  public DailyBudgetPresenter(DailyBudgetViewModel model) {
    this.model = model;
  }

  /**
   * Resets the screen state with the available trip choices.
   *
   * @param trips the member's available trips
   */
  @Override
  public void showTrips(List<DailyBudgetTripOption> trips) {
    show(new DailyBudgetState(trips, null, "", false));
  }

  /**
   * Publishes a summary and confirms a saved update when applicable.
   *
   * @param summary the calculated budget figures
   * @param budgetUpdated whether a new daily budget was saved
   */
  @Override
  public void showSummary(DailyBudgetOutputData summary, boolean budgetUpdated) {
    final String message = budgetUpdated ? "Daily budget updated." : "";
    show(new DailyBudgetState(model.getState().getTrips(), summary, message, false));
  }

  /**
   * Displays an error while preserving the last valid summary.
   *
   * @param message the explanation to display
   */
  @Override
  public void showError(String message) {
    final DailyBudgetState previous = model.getState();
    show(new DailyBudgetState(previous.getTrips(), previous.getSummary(), message, true));
  }

  /** Replaces the view state and notifies its listeners. */
  private void show(DailyBudgetState state) {
    model.setState(state);
    model.notifyStateChanged();
  }
}
