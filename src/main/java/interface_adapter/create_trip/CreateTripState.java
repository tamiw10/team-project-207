package interface_adapter.create_trip;

/** The state of the Create Trip form: what has been typed so far and the latest error. */
public class CreateTripState {

  private String name = "";
  private String destination = "";
  private String startDate = "";
  private String endDate = "";
  private String homeCurrency = "";
  private String dailyBudget = "";
  private String friends = "";
  private String errorMessage = "";

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getHomeCurrency() {
    return homeCurrency;
  }

  public void setHomeCurrency(String homeCurrency) {
    this.homeCurrency = homeCurrency;
  }

  public String getDailyBudget() {
    return dailyBudget;
  }

  public void setDailyBudget(String dailyBudget) {
    this.dailyBudget = dailyBudget;
  }

  public String getFriends() {
    return friends;
  }

  public void setFriends(String friends) {
    this.friends = friends;
  }

  /**
   * Returns the message to show under the form.
   *
   * @return the error, or an empty string if there is none
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
