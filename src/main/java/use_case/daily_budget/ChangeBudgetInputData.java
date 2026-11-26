package use_case.daily_budget;

/** A new daily budget and the day whose summary should be refreshed. */
public final class ChangeBudgetInputData {
  private final DailyBudgetInputData day;
  private final String amount;

  /**
   * Creates a request to change the daily budget.
   *
   * @param day the trip and day whose summary will be refreshed
   * @param amount the new budget as entered by the user
   */
  public ChangeBudgetInputData(DailyBudgetInputData day, String amount) {
    this.day = day;
    this.amount = amount;
  }

  /**
   * Returns the trip and day whose summary will be refreshed.
   *
   * @return the trip and day whose summary will be refreshed
   */
  public DailyBudgetInputData getDay() {
    return day;
  }

  /**
   * Returns the new daily budget as entered by the user.
   *
   * @return the new daily budget as entered by the user
   */
  public String getAmount() {
    return amount;
  }
}
