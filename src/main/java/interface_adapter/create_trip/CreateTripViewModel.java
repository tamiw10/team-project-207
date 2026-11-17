package interface_adapter.create_trip;

import java.util.ArrayList;
import java.util.List;

/** The View Model for the Create Trip form. */
public class CreateTripViewModel {

  public static final String VIEW_NAME = "create trip";

  public static final String TITLE_LABEL = "Create Trip";
  public static final String NAME_LABEL = "Trip name";
  public static final String DESTINATION_LABEL = "Destination";
  public static final String START_DATE_LABEL = "Start date (yyyy-mm-dd)";
  public static final String END_DATE_LABEL = "End date (yyyy-mm-dd)";
  public static final String HOME_CURRENCY_LABEL = "Home currency";
  public static final String DAILY_BUDGET_LABEL = "Daily budget";
  public static final String FRIENDS_LABEL =
      "Friends to invite (usernames, separated by commas, optional)";
  public static final String CREATE_BUTTON_LABEL = "Create";
  public static final String CANCEL_BUTTON_LABEL = "Cancel";

  private final List<CreateTripViewModelListener> listeners = new ArrayList<>();

  private CreateTripState state = new CreateTripState();

  public CreateTripState getState() {
    return state;
  }

  public void setState(CreateTripState state) {
    this.state = state;
  }

  public void addListener(CreateTripViewModelListener listener) {
    listeners.add(listener);
  }

  /** Tells every listener that the state changed. */
  public void notifyStateChanged() {
    for (CreateTripViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
