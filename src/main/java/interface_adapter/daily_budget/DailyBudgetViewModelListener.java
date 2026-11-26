package interface_adapter.daily_budget;

/** Listens for changes to the daily budget screen's state. */
public interface DailyBudgetViewModelListener {
  /** Refreshes the view after the budget model publishes a new state. */
  void stateChanged();
}
