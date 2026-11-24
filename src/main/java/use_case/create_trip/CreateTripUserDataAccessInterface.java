package use_case.create_trip;

/** The part of the user data the Create Trip Use Case needs. */
public interface CreateTripUserDataAccessInterface {

  /**
   * Checks whether a user with this exact username exists.
   *
   * @param username the username to look for
   * @return true if the user exists
   */
  boolean existsByName(String username);

  /**
   * Returns the username of the signed-in user.
   *
   * @return the signed-in username, or null if nobody is signed in
   */
  String getCurrentUsername();
}
