package app;

import data_access.FileUserDataAccessObject;
import data_access.FrankfurterExchangeRateGateway;
import data_access.JsonFileTripDataAccessObject;
import entity.Trip;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
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
import interface_adapter.vote.ActivitiesViewModel;
import interface_adapter.vote.VoteController;
import interface_adapter.vote.VotePresenter;
import java.awt.*;
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
import use_case.suggest_idea.SuggestIdeaInputBoundary;
import use_case.suggest_idea.SuggestIdeaInteractor;
import use_case.suggest_idea.SuggestIdeaOutputBoundary;
import use_case.vote.VoteInputBoundary;
import use_case.vote.VoteInteractor;
import use_case.vote.VoteOutputBoundary;
import view.ActivitiesView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

public class AppBuilder {
  private final JPanel cardPanel = new JPanel();
  private final CardLayout cardLayout = new CardLayout();
  final UserFactory userFactory = new UserFactory();
  final ViewManagerModel viewManagerModel = new ViewManagerModel();
  ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

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
  private ActivitiesViewModel activitiesViewModel;
  private ActivitiesView activitiesView;
  private VoteController voteController;

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
   * Adds the Vote on Activity Ideas Use Case to the application. Needs the logged-in and
   * activities views to have been added first.
   *
   * @return this builder
   */
  public AppBuilder addVoteUseCase() {
    final VoteOutputBoundary voteOutputBoundary = new VotePresenter(activitiesViewModel);
    final VoteInputBoundary voteInteractor =
        new VoteInteractor(tripDataAccessObject, voteOutputBoundary);
    voteController = new VoteController(voteInteractor);
    activitiesView.setVoteController(voteController);

    activitiesView.setBackAction(
        () -> {
          viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
          viewManagerModel.notifyViewChanged();
        });
    loggedInView.setOpenActivitiesAction(this::openActivities);
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
        new SuggestIdeaInteractor(tripDataAccessObject, suggestIdeaOutputBoundary);
    activitiesView.setSuggestIdeaController(new SuggestIdeaController(suggestIdeaInteractor));
    return this;
  }

  /**
   * Opens the activities screen for the trip the user has open. Until the trip list screen exists
   * and sets the current trip, this falls back to the first trip the user belongs to.
   */
  private void openActivities() {
    final String username = loggedInViewModel.getState().getUsername();
    final List<Trip> trips = tripDataAccessObject.getTripsForMember(username);
    if (trips.isEmpty()) {
      JOptionPane.showMessageDialog(cardPanel, "You are not a member of any trip yet.");
    } else {
      final String currentTripId = tripDataAccessObject.getCurrentTripId();
      Trip trip = trips.get(0);
      for (Trip candidate : trips) {
        if (candidate.getId().equals(currentTripId)) {
          trip = candidate;
        }
      }
      tripDataAccessObject.setCurrentTripId(trip.getId());
      final interface_adapter.vote.ActivitiesState state = activitiesViewModel.getState();
      state.setUsername(username);
      state.setTripId(trip.getId());
      state.setMessage("");
      state.setError("");
      voteController.showDay(trip.getId(), null, username);
      viewManagerModel.setState(ActivitiesViewModel.VIEW_NAME);
      viewManagerModel.notifyViewChanged();
    }
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
