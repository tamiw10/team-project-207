package interface_adapter.vote;

import java.util.ArrayList;
import java.util.List;

/** The View Model for the Activities View. */
public class ActivitiesViewModel {

  public static final String VIEW_NAME = "activities";

  private final List<ActivitiesViewModelListener> listeners = new ArrayList<>();

  private ActivitiesState state = new ActivitiesState();

  public ActivitiesState getState() {
    return this.state;
  }

  public void setState(ActivitiesState state) {
    this.state = state;
  }

  public void addListener(ActivitiesViewModelListener listener) {
    listeners.add(listener);
  }

  /** Tells the view the state changed. */
  public void notifyStateChanged() {
    for (ActivitiesViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
