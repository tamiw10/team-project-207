package use_case.create_trip;

import entity.Trip;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * The Create Trip Interactor. It checks the details of a new trip, checks that every invited friend
 * is a user, and saves the trip with the creator and the friends as members.
 */
public class CreateTripInteractor implements CreateTripInputBoundary {

  private final CreateTripUserDataAccessInterface userDataAccess;
  private final CreateTripDataAccessInterface tripDataAccess;
  private final ExchangeRateDataAccessInterface exchangeRates;
  private final CreateTripOutputBoundary presenter;

  /**
   * Creates the interactor.
   *
   * @param userDataAccess where users are looked up
   * @param tripDataAccess where the new trip is saved
   * @param exchangeRates where the list of supported currencies comes from
   * @param presenter where results are sent
   */
  public CreateTripInteractor(
      CreateTripUserDataAccessInterface userDataAccess,
      CreateTripDataAccessInterface tripDataAccess,
      ExchangeRateDataAccessInterface exchangeRates,
      CreateTripOutputBoundary presenter) {
    this.userDataAccess = userDataAccess;
    this.tripDataAccess = tripDataAccess;
    this.exchangeRates = exchangeRates;
    this.presenter = presenter;
  }

  @Override
  public void execute(CreateTripInputData inputData) {
    final String creator = userDataAccess.getCurrentUsername();
    final String missing = firstMissingDetail(inputData);
    if (creator == null || creator.isBlank()) {
      presenter.prepareFailView("You must be signed in to create a trip.");
    } else if (missing != null) {
      presenter.prepareFailView(missing + " cannot be empty");
    } else {
      validateAndCreate(inputData, creator);
    }
  }

  @Override
  public void openForm() {
    presenter.switchToFormView();
  }

  @Override
  public void switchToHomeView() {
    presenter.switchToHomeView();
  }

  private void validateAndCreate(CreateTripInputData inputData, String creator) {
    final LocalDate start = parseDate(inputData.getStartDate());
    final LocalDate end = parseDate(inputData.getEndDate());
    final Double budget = parseBudget(inputData.getDailyBudget());
    final String currency = inputData.getHomeCurrency().trim().toUpperCase();

    final String error;
    if (start == null) {
      error = "Start date must be a real date written like 2027-02-13";
    } else if (end == null) {
      error = "End date must be a real date written like 2027-02-19";
    } else if (end.isBefore(start)) {
      error = "End date cannot be before the start date";
    } else if (budget == null) {
      error = "Daily budget must be a number";
    } else if (budget < 0) {
      error = "Daily budget cannot be negative";
    } else {
      error = currencyError(currency);
    }

    if (error != null) {
      presenter.prepareFailView(error);
      return;
    }

    final List<String> friends = distinctFriends(inputData.getFriendUsernames(), creator);
    final List<String> unknown = unknownUsers(friends);
    if (unknown.isEmpty()) {
      createTrip(inputData, creator, friends, new TripDates(start, end), currency, budget);
    } else {
      presenter.prepareFailView(unknownUsersMessage(unknown));
    }
  }

  private void createTrip(
      CreateTripInputData inputData,
      String creator,
      List<String> friends,
      TripDates dates,
      String currency,
      double budget) {
    final Trip trip =
        new Trip(
            UUID.randomUUID().toString(),
            inputData.getName(),
            inputData.getDestination(),
            dates.start,
            dates.end,
            currency,
            budget,
            creator);
    for (String friend : friends) {
      trip.addMember(friend);
    }
    tripDataAccess.saveTrip(trip);
    tripDataAccess.setCurrentTripId(trip.getId());

    presenter.prepareSuccessView(
        new CreateTripOutputData(
            trip.getId(),
            trip.getName(),
            trip.getDestination(),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getHomeCurrency(),
            trip.getDailyBudget(),
            trip.getMemberUsernames()));
  }

  /** Returns the name of the first detail that was left empty, or null if none was. */
  private static String firstMissingDetail(CreateTripInputData inputData) {
    final String missing;
    if (isBlank(inputData.getName())) {
      missing = "Trip name";
    } else if (isBlank(inputData.getDestination())) {
      missing = "Destination";
    } else if (isBlank(inputData.getStartDate())) {
      missing = "Start date";
    } else if (isBlank(inputData.getEndDate())) {
      missing = "End date";
    } else if (isBlank(inputData.getHomeCurrency())) {
      missing = "Home currency";
    } else if (isBlank(inputData.getDailyBudget())) {
      missing = "Daily budget";
    } else {
      missing = null;
    }
    return missing;
  }

  private static boolean isBlank(String text) {
    return text == null || text.isBlank();
  }

  /** Returns the date, or null if the text is not a real date written as yyyy-mm-dd. */
  private static LocalDate parseDate(String text) {
    LocalDate date;
    try {
      date = LocalDate.parse(text.trim());
    } catch (DateTimeParseException ex) {
      date = null;
    }
    return date;
  }

  /** Returns the budget, or null if the text is not a finite number. */
  private static Double parseBudget(String text) {
    Double budget;
    try {
      budget = Double.valueOf(text.trim());
      if (budget.isNaN() || budget.isInfinite()) {
        budget = null;
      }
    } catch (NumberFormatException ex) {
      budget = null;
    }
    return budget;
  }

  /** Returns why the currency cannot be used, or null if it is supported. */
  private String currencyError(String currency) {
    String error = null;
    try {
      if (!exchangeRates.getSupportedCurrencies().contains(currency)) {
        error = "Home currency is not supported";
      }
    } catch (ExchangeRateUnavailableException ex) {
      error =
          "Could not check the home currency right now. "
              + "Check your internet connection and try again.";
    }
    return error;
  }

  /** Counts each friend once, in the order given, and leaves out the creator. */
  private static List<String> distinctFriends(List<String> typed, String creator) {
    final Set<String> friends = new LinkedHashSet<>();
    for (String username : typed) {
      final String cleaned = username == null ? "" : username.trim();
      if (!cleaned.isEmpty() && !cleaned.equals(creator)) {
        friends.add(cleaned);
      }
    }
    return new ArrayList<>(friends);
  }

  private List<String> unknownUsers(List<String> usernames) {
    final List<String> unknown = new ArrayList<>();
    for (String username : usernames) {
      if (!userDataAccess.existsByName(username)) {
        unknown.add(username);
      }
    }
    return unknown;
  }

  private static String unknownUsersMessage(List<String> unknown) {
    final String noun = unknown.size() == 1 ? "No user named " : "No users named ";
    return noun + String.join(", ", unknown) + ". Trip not created";
  }

  /** The first and last day of a trip. */
  private static final class TripDates {
    private final LocalDate start;
    private final LocalDate end;

    private TripDates(LocalDate start, LocalDate end) {
      this.start = start;
      this.end = end;
    }
  }
}
