package app;

import data_access.FileUserDataAccessObject;
import data_access.FrankfurterExchangeRateGateway;
import data_access.JsonFileTripDataAccessObject;
import entity.Contribution;
import entity.Trip;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.log_contribution.LogContributionState;
import entity.Trip;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_expense.AddExpenseState;
import interface_adapter.add_expense.AddExpenseViewModel;
import interface_adapter.add_expense.LogExpenseController;
import interface_adapter.add_expense.LogExpensePresenter;
import interface_adapter.create_trip.CreateTripController;
import interface_adapter.create_trip.CreateTripPresenter;
import interface_adapter.create_trip.CreateTripViewModel;
import interface_adapter.daily_budget.DailyBudgetController;
import interface_adapter.daily_budget.DailyBudgetPresenter;
import interface_adapter.daily_budget.DailyBudgetViewModel;
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
import interface_adapter.suggest_idea.SuggestIdeaController;
import interface_adapter.suggest_idea.SuggestIdeaPresenter;
import interface_adapter.trip.TripViewModel;
import interface_adapter.vote.ActivitiesViewModel;
import interface_adapter.vote.VoteController;
import interface_adapter.vote.VotePresenter;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.create_trip.CreateTripInputBoundary;
import use_case.create_trip.CreateTripInteractor;
import use_case.create_trip.CreateTripOutputBoundary;
import use_case.daily_budget.DailyBudgetInputBoundary;
import use_case.daily_budget.DailyBudgetInteractor;
import use_case.daily_budget.DailyBudgetOutputBoundary;
import use_case.log_expense.LogExpenseInputBoundary;
import use_case.log_expense.LogExpenseInteractor;
import use_case.log_expense.LogExpenseOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.suggest_idea.SuggestIdeaInputBoundary;
import use_case.suggest_idea.SuggestIdeaInteractor;
import use_case.suggest_idea.SuggestIdeaOutputBoundary;
import use_case.vote.VoteInputBoundary;
import use_case.vote.VoteInteractor;
import use_case.vote.VoteOutputBoundary;
import view.ActivitiesView;
import view.AddExpenseView;
import view.CreateTripView;
import view.DailyBudgetView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.TripView;
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
  private AddExpenseViewModel addExpenseViewModel;
  private AddExpenseView addExpenseView;
  private CreateTripViewModel createTripViewModel;
  private CreateTripView createTripView;
  private TripViewModel tripViewModel;
  private TripView tripView;
  private DailyBudgetViewModel dailyBudgetViewModel;
  private DailyBudgetView dailyBudgetView;
  private ActivitiesViewModel activitiesViewModel;
  private ActivitiesView activitiesView;

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
  /**
   * Adds the form for creating a trip.
   *
   * @return this builder
   */
  public AppBuilder addCreateTripView() {
    createTripViewModel = new CreateTripViewModel();
    createTripView = new CreateTripView(createTripViewModel);
    cardPanel.add(createTripView, CreateTripViewModel.VIEW_NAME);
    return this;
  }

  /**
   * Adds the screen that shows an open trip.
   *
   * @return this builder
   */
  public AppBuilder addTripView() {
    tripViewModel = new TripViewModel();
    tripView = new TripView(tripViewModel);
    cardPanel.add(tripView, TripViewModel.VIEW_NAME);
    return this;
  }

  /**
   * Adds the screen for viewing and updating a trip's budget.
   *
   * @return this builder
   */
  public AppBuilder addDailyBudgetView() {
    dailyBudgetViewModel = new DailyBudgetViewModel();
    dailyBudgetView = new DailyBudgetView(dailyBudgetViewModel);
    cardPanel.add(dailyBudgetView, DailyBudgetViewModel.VIEW_NAME);
    return this;
  }

  /**
   * Adds the screen for voting on activity ideas.
   *
   * @return this builder
   */
  public AppBuilder addActivitiesView() {
    activitiesViewModel = new ActivitiesViewModel();
    activitiesView = new ActivitiesView(activitiesViewModel);
    cardPanel.add(activitiesView, ActivitiesViewModel.VIEW_NAME);
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
      final int weeksRemaining = max(1, (daysRemaining + 6) / 7);
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
          final int weeksElapsed = max(1, (daysElapsed + 6) / 7);
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
  /**
   *Adds the Create Trip Use Case to the application.
   *
   * @return this builder
   */
  public AppBuilder addCreateTripUseCase() {
    final CreateTripOutputBoundary createTripOutputBoundary =
        new CreateTripPresenter(viewManagerModel, createTripViewModel, tripViewModel);

    final CreateTripInputBoundary createTripInteractor =
        new CreateTripInteractor(
            userDataAccessObject,
            tripDataAccessObject,
            exchangeRateGateway,
            createTripOutputBoundary);

    final CreateTripController createTripController =
        new CreateTripController(createTripInteractor);
    createTripView.setCreateTripController(createTripController);
    tripView.setCreateTripController(createTripController);
    loggedInView.setCreateTripController(createTripController);
    return this;
  }

  /**
   * Adds the Vote on Activity Ideas Use Case to the application. Needs the logged-in and
   * activities views to have been added first.
   *
   * @return this builder
   */
  public AppBuilder addVoteUseCase() {
    final VoteOutputBoundary voteOutputBoundary =
        new VotePresenter(viewManagerModel, activitiesViewModel);
    final VoteInputBoundary voteInteractor =
        new VoteInteractor(tripDataAccessObject, voteOutputBoundary);
    final VoteController voteController = new VoteController(voteInteractor);
    activitiesView.setVoteController(voteController);
    activitiesView.setBackAction(() -> showView(LoggedInViewModel.VIEW_NAME));
    loggedInView.setOpenActivitiesAction(
        () -> voteController.open(loggedInViewModel.getState().getUsername()));
    return this;
  }

  /**
   * Adds the Suggest Activity Idea Use Case to the application. Needs the activities view to have
   * been added first.
   *
   * @return this builder
   */
  public AppBuilder addSuggestIdeaUseCase() {
    final SuggestIdeaOutputBoundary suggestIdeaOutputBoundary =
        new SuggestIdeaPresenter(activitiesViewModel);
    final SuggestIdeaInputBoundary suggestIdeaInteractor =
        new SuggestIdeaInteractor(
            tripDataAccessObject, suggestIdeaOutputBoundary, LocalDateTime::now);
    activitiesView.setSuggestIdeaController(new SuggestIdeaController(suggestIdeaInteractor));
    return this;
  }

  public JFrame build() {
    final JFrame application = new JFrame("Trip Planner & Budgeter");
    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    application.add(cardPanel);

    viewManagerModel.setState(SignupViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();

    return application;
  }

  /**
   * Wires the daily budget use case to the signed-in screen.
   *
   * @return this builder
   */
  public AppBuilder addDailyBudgetUseCase() {
    final DailyBudgetOutputBoundary presenter = new DailyBudgetPresenter(dailyBudgetViewModel);
    final DailyBudgetInputBoundary interactor = new DailyBudgetInteractor(
        tripDataAccessObject, userDataAccessObject::getCurrentUsername, presenter);
    final DailyBudgetController controller = new DailyBudgetController(interactor);
    dailyBudgetView.setDailyBudgetController(controller);
    dailyBudgetView.setBackAction(() -> showView(LoggedInViewModel.VIEW_NAME));
    loggedInView.setOpenDailyBudgetAction(() -> {
      controller.open();
      showView(DailyBudgetViewModel.VIEW_NAME);
    });
    return this;
  }

  /** Switches the visible card to the named view. */
  private void showView(String viewName) {
    viewManagerModel.setState(viewName);
    viewManagerModel.notifyViewChanged();
  }

  /**
   * Creates and registers {@link AddExpenseView} and {@link AddExpenseViewModel} in the card panel.
   *
   * @return this builder
   */
  public AppBuilder addAddExpenseView() {
    addExpenseViewModel = new AddExpenseViewModel();
    addExpenseView = new AddExpenseView(addExpenseViewModel);
    addExpenseView.setViewManagerModel(viewManagerModel);
    cardPanel.add(addExpenseView, AddExpenseViewModel.VIEW_NAME);
    return this;
  }

  /**
   * Wires the Log Expense use case ({@link LogExpenseInteractor}, {@link LogExpensePresenter}, and
   * {@link LogExpenseController}) and connects the Log Expense button on {@link LoggedInView}.
   *
   * @return this builder
   */
  public AppBuilder addLogExpenseUseCase() {
    final LogExpenseOutputBoundary logExpenseOutputBoundary =
        new LogExpensePresenter(addExpenseViewModel);

    final LogExpenseInputBoundary logExpenseInteractor =
        new LogExpenseInteractor(
            tripDataAccessObject, exchangeRateGateway, logExpenseOutputBoundary);

    final LogExpenseController logExpenseController =
        new LogExpenseController(logExpenseInteractor);
    addExpenseView.setLogExpenseController(logExpenseController);

    if (loggedInView != null) {
      loggedInView.setOnOpenLogExpense(this::openLogExpenseForCurrentUser);
    }
    return this;
  }

  private void openLogExpenseForCurrentUser() {
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

    final AddExpenseState state = addExpenseViewModel.getState();
    state.setStatusMessage("");
    state.setError(false);
    state.setClearForm(false);
    if (trip != null) {
      tripDataAccessObject.setCurrentTripId(trip.getId());
      state.setTripId(trip.getId());
      state.setTripName(trip.getName());
      state.setHomeCurrency(trip.getHomeCurrency());
      state.setExpenses(
          LogExpensePresenter.formatExpenses(
              tripDataAccessObject.getExpenses(trip.getId()), trip.getHomeCurrency()));
    } else {
      state.setTripId("");
      state.setTripName("");
      state.setStatusMessage("No trip found. Run SampleDataSeeder first.");
      state.setError(true);
    }
    addExpenseViewModel.notifyStateChanged();
    viewManagerModel.setState(AddExpenseViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();
  }
}
