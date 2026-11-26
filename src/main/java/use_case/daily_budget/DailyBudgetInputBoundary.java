package use_case.daily_budget;

/** Opens the budget screen, shows a day, or changes the daily budget. */
public interface DailyBudgetInputBoundary {
  /** Loads the trips available to the signed-in user. */
  void open();

  /**
   * Validates the selected trip and day, then presents the budget summary.
   *
   * @param input the trip identifier and selected date
   */
  void execute(DailyBudgetInputData input);

  /**
   * Validates and saves a new daily budget, then recalculates the selected day.
   *
   * @param input the selected day and the proposed daily budget
   */
  void changeBudget(ChangeBudgetInputData input);
}
