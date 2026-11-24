package use_case.create_trip;

/** The input boundary for the Create Trip Use Case. */
public interface CreateTripInputBoundary {

  /**
   * Creates a trip from the details the user filled in.
   *
   * @param inputData the details of the new trip
   */
  void execute(CreateTripInputData inputData);

  /** Shows the empty form for a new trip. */
  void openForm();

  /** Leaves the form (or the new trip) and goes back to the home screen. */
  void switchToHomeView();
}
