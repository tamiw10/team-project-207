package interface_adapter.create_trip;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.trip.TripState;
import interface_adapter.trip.TripViewModel;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import use_case.create_trip.CreateTripOutputBoundary;
import use_case.create_trip.CreateTripOutputData;

/** The Presenter for the Create Trip Use Case. */
public class CreateTripPresenter implements CreateTripOutputBoundary {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

  private final ViewManagerModel viewManagerModel;
  private final CreateTripViewModel createTripViewModel;
  private final TripViewModel tripViewModel;

  /**
   * Creates the presenter.
   *
   * @param viewManagerModel decides which screen is shown
   * @param createTripViewModel the state of the form
   * @param tripViewModel the state of the trip screen
   */
  public CreateTripPresenter(
      ViewManagerModel viewManagerModel,
      CreateTripViewModel createTripViewModel,
      TripViewModel tripViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.createTripViewModel = createTripViewModel;
    this.tripViewModel = tripViewModel;
  }

  @Override
  public void prepareSuccessView(CreateTripOutputData outputData) {
    final TripState tripState = new TripState();
    tripState.setTripId(outputData.getTripId());
    tripState.setTitle(outputData.getName());
    tripState.setDetails(describe(outputData));
    tripState.setMembers(outputData.getMemberUsernames());
    tripViewModel.setState(tripState);
    tripViewModel.notifyStateChanged();

    clearForm();
    showView(TripViewModel.VIEW_NAME);
  }

  @Override
  public void prepareFailView(String errorMessage) {
    final CreateTripState state = createTripViewModel.getState();
    state.setErrorMessage(errorMessage);
    createTripViewModel.notifyStateChanged();
  }

  @Override
  public void switchToFormView() {
    clearForm();
    showView(CreateTripViewModel.VIEW_NAME);
  }

  @Override
  public void switchToHomeView() {
    clearForm();
    showView(LoggedInViewModel.VIEW_NAME);
  }

  private void clearForm() {
    createTripViewModel.setState(new CreateTripState());
    createTripViewModel.notifyStateChanged();
  }

  private void showView(String viewName) {
    viewManagerModel.setState(viewName);
    viewManagerModel.notifyViewChanged();
  }

  /** Builds the one-line summary, for example "Tokyo, Japan | Feb 13, 2027 to ... | CAD 250.00". */
  private static String describe(CreateTripOutputData outputData) {
    return outputData.getDestination()
        + " | "
        + DATE_FORMAT.format(outputData.getStartDate())
        + " to "
        + DATE_FORMAT.format(outputData.getEndDate())
        + " | "
        + outputData.getHomeCurrency()
        + " "
        + String.format(Locale.ENGLISH, "%.2f", outputData.getDailyBudget())
        + " per day";
  }
}
