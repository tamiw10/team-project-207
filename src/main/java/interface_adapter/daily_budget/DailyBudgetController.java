package interface_adapter.daily_budget;

import java.time.LocalDate;
import use_case.daily_budget.ChangeBudgetInputData;
import use_case.daily_budget.DailyBudgetInputBoundary;
import use_case.daily_budget.DailyBudgetInputData;

/** Converts choices from the budget screen into use case requests. */
public final class DailyBudgetController {
  private final DailyBudgetInputBoundary interactor;

  /**
   * Creates a controller for the daily budget use case.
   *
   * @param interactor the input boundary called by the controller
   */
  public DailyBudgetController(DailyBudgetInputBoundary interactor) {
    this.interactor = interactor;
  }

  /** Loads the signed-in user's trip choices for the budget screen. */
  public void open() {
    interactor.open();
  }

  /**
   * Requests a budget summary for the selected day.
   *
   * @param tripId the selected trip ID
   * @param date the selected trip day
   */
  public void showDay(String tripId, LocalDate date) {
    interactor.execute(new DailyBudgetInputData(tripId, date));
  }

  /**
   * Submits a budget change for the selected trip.
   *
   * @param tripId the selected trip ID
   * @param date the day whose summary will be refreshed
   * @param amount the new daily budget as entered in the form
   */
  public void changeBudget(String tripId, LocalDate date, String amount) {
    final DailyBudgetInputData day = new DailyBudgetInputData(tripId, date);
    interactor.changeBudget(new ChangeBudgetInputData(day, amount));
  }
}
