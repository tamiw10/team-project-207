package interface_adapter.trip;

/** Something that wants to know when the open trip changes. */
public interface TripViewModelListener {

  /** The state of the trip screen was updated. */
  void stateChanged();
}
