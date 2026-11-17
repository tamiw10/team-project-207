package interface_adapter.trip;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the trip screen. The Create Trip Use Case fills it in when a trip is created.
 * The other use cases add their own buttons to the trip screen.
 */
public class TripViewModel {

  public static final String VIEW_NAME = "trip";

  public static final String MEMBERS_LABEL = "Members";
  public static final String BACK_BUTTON_LABEL = "Back to home";

  private final List<TripViewModelListener> listeners = new ArrayList<>();

  private TripState state = new TripState();

  public TripState getState() {
    return state;
  }

  public void setState(TripState state) {
    this.state = state;
  }

  public void addListener(TripViewModelListener listener) {
    listeners.add(listener);
  }

  /** Tells every listener that the state changed. */
  public void notifyStateChanged() {
    for (TripViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
