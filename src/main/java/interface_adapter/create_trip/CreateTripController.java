package interface_adapter.create_trip;

import java.util.ArrayList;
import java.util.List;
import use_case.create_trip.CreateTripInputBoundary;
import use_case.create_trip.CreateTripInputData;

/** The Controller for the Create Trip Use Case. */
public class CreateTripController {

  private final CreateTripInputBoundary interactor;

  public CreateTripController(CreateTripInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Asks the use case to create a trip.
   *
   * @param name the trip name
   * @param destination where the group is going
   * @param startDate the first day, as typed
   * @param endDate the last day, as typed
   * @param homeCurrency the home currency code, as typed
   * @param dailyBudget the daily budget, as typed
   * @param friends the usernames to invite, separated by commas (can be empty)
   */
  public void execute(
      String name,
      String destination,
      String startDate,
      String endDate,
      String homeCurrency,
      String dailyBudget,
      String friends) {
    interactor.execute(
        new CreateTripInputData(
            name,
            destination,
            startDate,
            endDate,
            homeCurrency,
            dailyBudget,
            splitUsernames(friends)));
  }

  /** Asks the use case to show the empty form. */
  public void openForm() {
    interactor.openForm();
  }

  /** Asks the use case to go back to the home screen. */
  public void switchToHomeView() {
    interactor.switchToHomeView();
  }

  /** Turns "amelia, jadyn" into ["amelia", "jadyn"]. Empty entries are dropped. */
  static List<String> splitUsernames(String friends) {
    final List<String> usernames = new ArrayList<>();
    if (friends != null) {
      for (String piece : friends.split(",")) {
        if (!piece.isBlank()) {
          usernames.add(piece.trim());
        }
      }
    }
    return usernames;
  }
}
