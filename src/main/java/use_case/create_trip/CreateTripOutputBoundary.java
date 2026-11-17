package use_case.create_trip;

/** The output boundary for the Create Trip Use Case. */
public interface CreateTripOutputBoundary {

  /**
   * Prepares the success view: the new trip and its members.
   *
   * @param outputData the new trip
   */
  void prepareSuccessView(CreateTripOutputData outputData);

  /**
   * Prepares the failure view.
   *
   * @param errorMessage the explanation of the failure
   */
  void prepareFailView(String errorMessage);

  /** Shows the empty form for a new trip. */
  void switchToFormView();

  /** Shows the home screen. */
  void switchToHomeView();
}
