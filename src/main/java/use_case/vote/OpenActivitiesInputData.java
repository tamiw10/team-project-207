package use_case.vote;

/** The Input Data for opening the activities screen. */
public class OpenActivitiesInputData {

  private final String username;

  /**
   * Creates the input data.
   *
   * @param username the member opening the screen
   */
  public OpenActivitiesInputData(String username) {
    this.username = username;
  }

  String getUsername() {
    return username;
  }
}
