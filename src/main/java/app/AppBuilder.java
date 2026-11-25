package app;

import data_access.FileUserDataAccessObject;
import data_access.FrankfurterExchangeRateGateway;
import data_access.JsonFileTripDataAccessObject;
import entity.Contribution;
import entity.Trip;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.log_contribution.LogContributionState;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import interface_adapter.log_contribution.LogContributionController;
import interface_adapter.log_contribution.LogContributionPresenter;
import interface_adapter.log_contribution.LogContributionViewModel;
import use_case.log_contribution.LogContributionInputBoundary;
import use_case.log_contribution.LogContributionInteractor;
import use_case.log_contribution.LogContributionOutputBoundary;
import view.LogContributionView;
import use_case.log_contribution.LogContributionUserDataAccessInterface;
import use_case.log_contribution.LogContributionTripDataAccessInterface;

import static java.lang.Math.max;

public class AppBuilder {
  private final JPanel cardPanel = new JPanel();
  private final CardLayout cardLayout = new CardLayout();
  final UserFactory userFactory = new UserFactory();
  final ViewManagerModel viewManagerModel = new ViewManagerModel();
  ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

  // for Use Case 2:
  private LogContributionViewModel logContributionViewModel;
  private LogContributionView logContributionView;
  private LogContributionController logContributionController;

  // ----- Shared data access objects -----
  // Users are saved to users.csv (course starter code).
  final FileUserDataAccessObject userDataAccessObject =
      new FileUserDataAccessObject("users.csv", userFactory);
  // Trips, expenses, contributions and activity ideas are saved to trips.json.
  // Add "implements <YourUseCase>DataAccessInterface" to InMemoryTripDataAccessObject
  // and pass this object to your interactor.
  final JsonFileTripDataAccessObject tripDataAccessObject =
      new JsonFileTripDataAccessObject("trips.json");
  // Live exchange rates from the Frankfurter API.
  final FrankfurterExchangeRateGateway exchangeRateGateway = new FrankfurterExchangeRateGateway();

  private SignupView signupView;
  private SignupViewModel signupViewModel;
  private LoginViewModel loginViewModel;
  private LoggedInViewModel loggedInViewModel;
  private LoggedInView loggedInView;
  private LoginView loginView;

  public AppBuilder() {
    cardPanel.setLayout(cardLayout);
  }

  public AppBuilder addSignupView() {
    signupViewModel = new SignupViewModel();
    signupView = new SignupView(signupViewModel);
    cardPanel.add(signupView, SignupViewModel.VIEW_NAME);
    return this;
  }

  public AppBuilder addLoginView() {
    loginViewModel = new LoginViewModel();
    loginView = new LoginView(loginViewModel);
    cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
    return this;
  }

  public AppBuilder addLoggedInView() {
    loggedInViewModel = new LoggedInViewModel();
    loggedInView = new LoggedInView(loggedInViewModel);
    cardPanel.add(loggedInView, LoggedInViewModel.VIEW_NAME);
    return this;
  }

  public AppBuilder addLogContributionView() {
    logContributionViewModel = new LogContributionViewModel();
    logContributionView = new LogContributionView(logContributionViewModel);
    logContributionView.setViewManagerModel(viewManagerModel);
    cardPanel.add(logContributionView, logContributionViewModel.VIEW_NAME);
    return this;
  }

  public AppBuilder addSignupUseCase() {
    final SignupOutputBoundary signupOutputBoundary =
        new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

    final SignupInputBoundary userSignupInteractor =
        new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

    SignupController controller = new SignupController(userSignupInteractor);
    signupView.setSignupController(controller);
    return this;
  }

  public AppBuilder addLoginUseCase() {
    final LoginOutputBoundary loginOutputBoundary =
        new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
    final LoginInputBoundary loginInteractor =
        new LoginInteractor(userDataAccessObject, loginOutputBoundary);

    LoginController loginController = new LoginController(loginInteractor);
    loginView.setLoginController(loginController);
    return this;
  }

  public AppBuilder addChangePasswordUseCase() {
    final ChangePasswordOutputBoundary changePasswordOutputBoundary =
        new ChangePasswordPresenter(loggedInViewModel);

    final ChangePasswordInputBoundary changePasswordInteractor =
        new ChangePasswordInteractor(
            userDataAccessObject, changePasswordOutputBoundary, userFactory);

    ChangePasswordController changePasswordController =
        new ChangePasswordController(changePasswordInteractor);
    loggedInView.setChangePasswordController(changePasswordController);
    return this;
  }

  /**
   * Adds the Logout Use Case to the application.
   *
   * @return this builder
   */
  public AppBuilder addLogoutUseCase() {
    final LogoutOutputBoundary logoutOutputBoundary =
        new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

    final LogoutInputBoundary logoutInteractor =
        new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

    final LogoutController logoutController = new LogoutController(logoutInteractor);
    loggedInView.setLogoutController(logoutController);
    return this;
  }

  /**
   * Adds Use Case 2: Log Contribution to the application
   * @return this builder
   */
  public AppBuilder addLogContributionUseCase() {
    final LogContributionOutputBoundary logContributionOutputBoundary =
            new LogContributionPresenter(logContributionViewModel);

    final LogContributionInputBoundary logContributionInteractor =
            new LogContributionInteractor(userDataAccessObject, tripDataAccessObject, logContributionOutputBoundary);

    final LogContributionController logContributionController =
            new LogContributionController(logContributionInteractor);

    logContributionView.setLogContributionController(logContributionController);

    if (loggedInView != null) {
      loggedInView.setOnOpenLogContribution(this::openLogContributionForCurrentUser);
    }
    return this;

  }

  private void openLogContributionForCurrentUser() {
    final String username = loggedInViewModel.getState().getUsername();
    Trip trip = null;
    if (tripDataAccessObject.getCurrentTripId() != null) {
      trip = tripDataAccessObject.getTrip(tripDataAccessObject.getCurrentTripId());
    }
    if (trip == null && username != null && !username.isBlank()) {
      final List<Trip> memberTrips = tripDataAccessObject.getTripsForMember(username);
      if (!memberTrips.isEmpty()) {
        trip = memberTrips.get(0);
      }
    }
    if (trip == null) {
      trip = tripDataAccessObject.getTrip(SampleDataSeeder.SAMPLE_TRIP_ID);
    }

    final LogContributionState state = logContributionViewModel.getState();
    state.setStatusMessage("");
    state.setError(false);
    state.setClearForm(false);

    if (trip != null) {
      tripDataAccessObject.setCurrentTripId(trip.getId());
      state.setTripID(trip.getId());
      state.setTripName(trip.getName());
      state.setHomeCurrency(trip.getHomeCurrency());
      state.setHasSavingsGoal(trip.hasSavingsGoal(username));

      // Calculations
      final List<Contribution> groupContributions = tripDataAccessObject.getContributions(trip.getId());
      final List<Contribution> userContributions = tripDataAccessObject.getContributions(trip.getId(), username);
      final LocalDate today = LocalDate.now();

      // Weeks Remaining
      final long daysRemainingLong = ChronoUnit.DAYS.between(today, trip.getStartDate());
      final int daysRemaining = Math.toIntExact(daysRemainingLong);
      final int weeksRemaining = max(1, Math.ceilDiv(daysRemaining, 7));
      state.setWeeksRemaining(Double.toString(weeksRemaining));

      // User goal
      if (trip.hasSavingsGoal(username)) {
        double tSaved = 0;
        for (Contribution userContribution : userContributions) {
          tSaved += userContribution.getAmount();
        }
        final double totalSaved = tSaved;
        state.setTotalSaved(Double.toString(totalSaved));
        state.setSavingsGoal(Double.toString(trip.getSavingsGoal(username)));
        state.setUserProgress(totalSaved / trip.getSavingsGoal(username));
        state.setUserGoalReached(totalSaved >= trip.getSavingsGoal(username));

        //Weekly Required
        final double amountRemaining = trip.getSavingsGoal(username) - totalSaved;
        final BigDecimal weeklyRequiredBig = BigDecimal.valueOf(amountRemaining / weeksRemaining)
                .setScale(2, RoundingMode.HALF_UP);
        final double weeklyRequired = weeklyRequiredBig.doubleValue();
        state.setWeeklyRequired(Double.toString(weeklyRequired));

        if (userContributions.isEmpty()) {
          state.setWeeklyPace("");
          // On Track
          state.setOnTrack(false);

        } else {
          final LocalDate firstContributionDate = userContributions.get(0).getDate();
          final long daysElapsedLong = ChronoUnit.DAYS.between(firstContributionDate, today);
          final int daysElapsed = Math.toIntExact(daysElapsedLong);
          final int weeksElapsed = max(1, Math.ceilDiv(daysElapsed, 7));
          final BigDecimal weeklyPaceBig = BigDecimal.valueOf(totalSaved / weeksElapsed)
                  .setScale(2, RoundingMode.HALF_UP);
          final double weeklyPace = weeklyPaceBig.doubleValue();
          state.setWeeklyPace(Double.toString(weeklyPace));
          // On Track
          state.setOnTrack(weeklyPace >= weeklyRequired);
        }

      } else {
        state.setSavingsGoal("");
        state.setTotalSaved("$0.00");
        state.setUserProgress(0.0);
        state.setUserGoalReached(false);
        state.setWeeklyRequired("");
        state.setWeeklyPace("");
        state.setOnTrack(false);
      }

      // Group
      if (trip.getSavingsGoals().isEmpty()) {
        state.setGroupSaved("$0.00");
        state.setGroupGoal("");
        state.setGroupProgress(0.0);
        state.setGroupGoalReached(false);

      } else {
        double gSaved = 0;
        for (Contribution groupContribution : groupContributions) {
          gSaved += groupContribution.getAmount();
        }
        final double groupSaved = gSaved;
        state.setGroupSaved(Double.toString(groupSaved));
        state.setGroupGoal(Double.toString(trip.getTotalSavingsGoal()));
        state.setGroupProgress(groupSaved / trip.getTotalSavingsGoal());
        state.setGroupGoalReached(groupSaved >= trip.getTotalSavingsGoal());
      }


    } else {
      state.setTripID("");
      state.setTripName("");
      state.setStatusMessage("No trip found. Run SampleDataSeeder first");
      state.setError(true);
    }
    logContributionViewModel.notifyStateChanged();
    viewManagerModel.setState(LogContributionViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();
  }

  public JFrame build() {
    final JFrame application = new JFrame("Trip Planner & Budgeter");
    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    application.add(cardPanel);

    viewManagerModel.setState(SignupViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();

    return application;
  }
}
