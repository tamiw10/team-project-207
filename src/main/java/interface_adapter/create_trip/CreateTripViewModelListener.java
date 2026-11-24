package interface_adapter.create_trip;

/** Something that wants to know when the Create Trip form state changes. */
public interface CreateTripViewModelListener {

  /** The state of the form was updated. */
  void stateChanged();
}
