package data_access;

import entity.User;
import java.util.HashMap;
import java.util.Map;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.log_contribution.LogContributionUserDataAccessInterface;
import use_case.create_trip.CreateTripUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.log_contribution.LogContributionUserDataAccessInterface;


/**
 * In-memory implementation of the DAO for storing user data. This implementation does NOT persist
 * data between runs of the program.
 */
public class InMemoryUserDataAccessObject
    implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        LogContributionUserDataAccessInterface,
        CreateTripUserDataAccessInterface {

  private final Map<String, User> users = new HashMap<>();

  private String currentUsername;

  @Override
  public boolean existsByName(String identifier) {
    return users.containsKey(identifier);
  }

  @Override
  public void save(User user) {
    users.put(user.getName(), user);
  }

  @Override
  public User get(String username) {
    return users.get(username);
  }

  @Override
  public void setCurrentUsername(String name) {
    currentUsername = name;
  }

  @Override
  public String getCurrentUsername() {
    return currentUsername;
  }

  @Override
  public void changePassword(User user) {
    // Replace the old entry with the new password
    users.put(user.getName(), user);
  }
}
