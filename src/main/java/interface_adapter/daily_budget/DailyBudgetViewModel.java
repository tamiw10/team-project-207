package interface_adapter.daily_budget;

import java.util.ArrayList;
import java.util.List;

/** Holds the budget screen's state and notifies its Swing view. */
public final class DailyBudgetViewModel {
  /** Card layout identifier for the budget screen. */
  public static final String VIEW_NAME = "daily budget";

  private final List<DailyBudgetViewModelListener> listeners = new ArrayList<>();
  private DailyBudgetState state = new DailyBudgetState(List.of(), null, "", false);

  /**
   * Returns the state currently shown on the budget screen.
   *
   * @return the available trip choices, latest summary, and feedback
   */
  public DailyBudgetState getState() {
    return state;
  }

  /** Replaces the state before listeners are notified. */
  void setState(DailyBudgetState state) {
    this.state = state;
  }

  /**
   * Registers a listener for changes to the budget screen state.
   *
   * @param listener the listener to notify
   */
  public void addListener(DailyBudgetViewModelListener listener) {
    listeners.add(listener);
  }

  /** Notifies registered listeners that the screen state has changed. */
  void notifyStateChanged() {
    for (DailyBudgetViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
