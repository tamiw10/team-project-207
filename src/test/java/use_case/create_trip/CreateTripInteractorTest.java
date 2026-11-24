package use_case.create_trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryTripDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entity.Trip;
import entity.UserFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

class CreateTripInteractorTest {

  private InMemoryUserDataAccessObject users;
  private InMemoryTripDataAccessObject trips;
  private RecordingPresenter presenter;
  private boolean exchangeRatesDown;

  @BeforeEach
  void setUp() {
    final UserFactory factory = new UserFactory();
    users = new InMemoryUserDataAccessObject();
    users.save(factory.create("tamima", "pw"));
    users.save(factory.create("amelia", "pw"));
    users.save(factory.create("jadyn", "pw"));
    users.setCurrentUsername("tamima");
    trips = new InMemoryTripDataAccessObject();
    presenter = new RecordingPresenter();
    exchangeRatesDown = false;
  }

  private CreateTripInteractor interactor() {
    final ExchangeRateDataAccessInterface rates =
        new ExchangeRateDataAccessInterface() {
          @Override
          public double getRate(String from, String to) {
            return 1.0;
          }

          @Override
          public List<String> getSupportedCurrencies() throws ExchangeRateUnavailableException {
            if (exchangeRatesDown) {
              throw new ExchangeRateUnavailableException("offline");
            }
            return Arrays.asList("CAD", "JPY", "USD");
          }
        };
    return new CreateTripInteractor(users, trips, rates, presenter);
  }

  private static CreateTripInputData input(
      String name,
      String destination,
      String start,
      String end,
      String currency,
      String budget,
      String... friends) {
    return new CreateTripInputData(
        name, destination, start, end, currency, budget, Arrays.asList(friends));
  }

  private static CreateTripInputData validInput(String... friends) {
    return input(
        "Tokyo Reading Week", "Tokyo, Japan", "2027-02-13", "2027-02-19", "CAD", "250", friends);
  }

  private void assertRejected(String expectedMessage) {
    assertEquals(expectedMessage, presenter.error);
    assertNull(presenter.success);
    assertTrue(trips.getTripsForMember("tamima").isEmpty());
    assertNull(trips.getCurrentTripId());
  }

  @Test
  void createsTripWithInvitedFriendsAsMembers() {
    interactor().execute(validInput("amelia", "jadyn"));

    assertNull(presenter.error);
    final CreateTripOutputData out = presenter.success;
    assertNotNull(out);
    assertEquals("Tokyo Reading Week", out.getName());
    assertEquals("Tokyo, Japan", out.getDestination());
    assertEquals(LocalDate.of(2027, 2, 13), out.getStartDate());
    assertEquals(LocalDate.of(2027, 2, 19), out.getEndDate());
    assertEquals("CAD", out.getHomeCurrency());
    assertEquals(250.0, out.getDailyBudget());
    assertEquals(Arrays.asList("tamima", "amelia", "jadyn"), out.getMemberUsernames());

    final Trip saved = trips.getTrip(out.getTripId());
    assertNotNull(saved);
    assertEquals(Arrays.asList("tamima", "amelia", "jadyn"), saved.getMemberUsernames());
    assertEquals(out.getTripId(), trips.getCurrentTripId());
    // Each invited friend sees the trip in their own list of trips.
    assertEquals(1, trips.getTripsForMember("amelia").size());
    assertEquals(1, trips.getTripsForMember("jadyn").size());
  }

  @Test
  void createsTripWithOnlyTheCreatorWhenNoFriendsAreNamed() {
    interactor().execute(validInput());

    assertEquals(Collections.singletonList("tamima"), presenter.success.getMemberUsernames());
  }

  @Test
  void countsEachPersonOnceAndIgnoresTheCreatorsOwnName() {
    interactor().execute(validInput("amelia", " amelia ", "tamima", "", "jadyn", "amelia"));

    assertEquals(
        Arrays.asList("tamima", "amelia", "jadyn"), presenter.success.getMemberUsernames());
  }

  @Test
  void acceptsTripOfOneDayAndTripAlreadyUnderway() {
    interactor().execute(input("Day trip", "Niagara", "2020-05-01", "2020-05-01", "USD", "0"));

    assertNull(presenter.error);
    final Trip saved = trips.getTrip(presenter.success.getTripId());
    assertEquals(1, saved.getNumberOfDays());
    assertEquals(0.0, saved.getDailyBudget());
  }

  @Test
  void acceptsLowerCaseCurrencyAndStoresItUpperCase() {
    interactor().execute(input("Trip", "Osaka", "2027-02-13", "2027-02-19", " jpy ", "100"));

    assertEquals("JPY", presenter.success.getHomeCurrency());
  }

  @Test
  void rejectsEmptyTripName() {
    interactor().execute(input("  ", "Tokyo, Japan", "2027-02-13", "2027-02-19", "CAD", "250"));

    assertRejected("Trip name cannot be empty");
  }

  @Test
  void rejectsEachOtherMissingDetail() {
    interactor().execute(input("Trip", "", "2027-02-13", "2027-02-19", "CAD", "250"));
    assertEquals("Destination cannot be empty", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "", "2027-02-19", "CAD", "250"));
    assertEquals("Start date cannot be empty", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "2027-02-13", null, "CAD", "250"));
    assertEquals("End date cannot be empty", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "", "250"));
    assertEquals("Home currency cannot be empty", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "CAD", " "));
    assertRejected("Daily budget cannot be empty");
  }

  @Test
  void rejectsEndDateBeforeStartDate() {
    interactor().execute(input("Trip", "Tokyo", "2027-02-19", "2027-02-13", "CAD", "250"));

    assertRejected("End date cannot be before the start date");
  }

  @Test
  void rejectsDatesThatAreNotRealDates() {
    interactor().execute(input("Trip", "Tokyo", "13/02/2027", "2027-02-19", "CAD", "250"));
    assertEquals("Start date must be a real date written like 2027-02-13", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-30", "CAD", "250"));
    assertRejected("End date must be a real date written like 2027-02-19");
  }

  @Test
  void rejectsBudgetThatIsNotANumber() {
    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "CAD", "lots"));
    assertEquals("Daily budget must be a number", presenter.error);

    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "CAD", "NaN"));
    assertRejected("Daily budget must be a number");
  }

  @Test
  void rejectsNegativeBudget() {
    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "CAD", "-5"));

    assertRejected("Daily budget cannot be negative");
  }

  @Test
  void rejectsUnsupportedCurrency() {
    interactor().execute(input("Trip", "Tokyo", "2027-02-13", "2027-02-19", "XYZ", "250"));

    assertRejected("Home currency is not supported");
  }

  @Test
  void rejectsTripWhenCurrencyCannotBeChecked() {
    exchangeRatesDown = true;

    interactor().execute(validInput());

    assertRejected(
        "Could not check the home currency right now. "
            + "Check your internet connection and try again.");
  }

  @Test
  void rejectsOneUnknownUsernameAndCreatesNothing() {
    interactor().execute(validInput("amelia", "ameliaa"));

    assertRejected("No user named ameliaa. Trip not created");
  }

  @Test
  void listsEveryUnknownUsername() {
    interactor().execute(validInput("bob", "amelia", "carol"));

    assertRejected("No users named bob, carol. Trip not created");
  }

  @Test
  void usernamesMustMatchCapitalLettersExactly() {
    interactor().execute(validInput("Amelia"));

    assertRejected("No user named Amelia. Trip not created");
  }

  @Test
  void rejectsWhenNobodyIsSignedIn() {
    users.setCurrentUsername(null);

    interactor().execute(validInput());

    assertEquals("You must be signed in to create a trip.", presenter.error);
    assertNull(presenter.success);
  }

  @Test
  void showsFormAndGoesHome() {
    final CreateTripInteractor interactor = interactor();

    interactor.openForm();
    assertTrue(presenter.formShown);
    assertFalse(presenter.homeShown);

    interactor.switchToHomeView();
    assertTrue(presenter.homeShown);
  }

  /** Remembers what the interactor told the presenter. */
  private static final class RecordingPresenter implements CreateTripOutputBoundary {
    private CreateTripOutputData success;
    private String error;
    private boolean formShown;
    private boolean homeShown;

    @Override
    public void prepareSuccessView(CreateTripOutputData outputData) {
      success = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
      error = errorMessage;
    }

    @Override
    public void switchToFormView() {
      formShown = true;
    }

    @Override
    public void switchToHomeView() {
      homeShown = true;
    }
  }
}
