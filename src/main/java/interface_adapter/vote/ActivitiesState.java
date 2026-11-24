package interface_adapter.vote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** The state of the activities screen: the day shown, its ranked ideas and the user's votes. */
public class ActivitiesState {

  private String username = "";
  private String tripId = "";
  private String tripName = "";
  private String homeCurrency = "";
  private LocalDate day;
  private List<LocalDate> tripDays = new ArrayList<>();
  private List<IdeaRow> rankedIdeas = new ArrayList<>();
  private int votesLeft;
  private int votesPerDay;
  private String message = "";
  private String error = "";

  /**
   * Returns the member using the screen.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the member using the screen.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the trip being shown.
   *
   * @return the trip's id
   */
  public String getTripId() {
    return tripId;
  }

  /**
   * Sets the trip being shown.
   *
   * @param tripId the trip's id
   */
  public void setTripId(String tripId) {
    this.tripId = tripId;
  }

  /**
   * Returns the name of the trip being shown.
   *
   * @return the trip's name
   */
  public String getTripName() {
    return tripName;
  }

  /**
   * Sets the name of the trip being shown.
   *
   * @param tripName the trip's name
   */
  public void setTripName(String tripName) {
    this.tripName = tripName;
  }

  /**
   * Returns the currency the idea costs are shown in.
   *
   * @return the trip's home currency code
   */
  public String getHomeCurrency() {
    return homeCurrency;
  }

  /**
   * Sets the currency the idea costs are shown in.
   *
   * @param homeCurrency the trip's home currency code
   */
  public void setHomeCurrency(String homeCurrency) {
    this.homeCurrency = homeCurrency;
  }

  /**
   * Returns the day whose ideas are shown.
   *
   * @return the day
   */
  public LocalDate getDay() {
    return day;
  }

  /**
   * Sets the day whose ideas are shown.
   *
   * @param day the day
   */
  public void setDay(LocalDate day) {
    this.day = day;
  }

  /**
   * Returns every day of the trip, for the day picker.
   *
   * @return the trip's days
   */
  public List<LocalDate> getTripDays() {
    return tripDays;
  }

  /**
   * Sets every day of the trip, for the day picker.
   *
   * @param tripDays the trip's days
   */
  public void setTripDays(List<LocalDate> tripDays) {
    this.tripDays = new ArrayList<>(tripDays);
  }

  /**
   * Returns the day's ideas, best ranked first.
   *
   * @return the rows to show
   */
  public List<IdeaRow> getRankedIdeas() {
    return rankedIdeas;
  }

  /**
   * Sets the day's ideas, best ranked first.
   *
   * @param rankedIdeas the rows to show
   */
  public void setRankedIdeas(List<IdeaRow> rankedIdeas) {
    this.rankedIdeas = new ArrayList<>(rankedIdeas);
  }

  /**
   * Returns how many of the user's votes for the day are unused.
   *
   * @return the votes left
   */
  public int getVotesLeft() {
    return votesLeft;
  }

  /**
   * Sets how many of the user's votes for the day are unused.
   *
   * @param votesLeft the votes left
   */
  public void setVotesLeft(int votesLeft) {
    this.votesLeft = votesLeft;
  }

  /**
   * Returns how many votes each member gets per day.
   *
   * @return the votes per day
   */
  public int getVotesPerDay() {
    return votesPerDay;
  }

  /**
   * Sets how many votes each member gets per day.
   *
   * @param votesPerDay the votes per day
   */
  public void setVotesPerDay(int votesPerDay) {
    this.votesPerDay = votesPerDay;
  }

  /**
   * Returns the green confirmation text, such as "Vote counted".
   *
   * @return the message, or an empty string if there is none
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the green confirmation text.
   *
   * @param message the message, or an empty string for none
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Returns the red error text, such as "No votes left today. Take a vote back first.".
   *
   * @return the error, or an empty string if there is none
   */
  public String getError() {
    return error;
  }

  /**
   * Sets the red error text.
   *
   * @param error the error, or an empty string for none
   */
  public void setError(String error) {
    this.error = error;
  }

  /** One idea as the activities screen shows it. */
  public static class IdeaRow {

    private final String id;
    private final String title;
    private final double estimatedCost;
    private final int voteCount;
    private final boolean votedByUser;

    /**
     * Creates a row.
     *
     * @param id the idea's id
     * @param title the activity's name
     * @param estimatedCost expected cost in the trip's home currency
     * @param voteCount how many members voted for it
     * @param votedByUser whether the user using the screen has voted for it
     */
    public IdeaRow(
        String id, String title, double estimatedCost, int voteCount, boolean votedByUser) {
      this.id = id;
      this.title = title;
      this.estimatedCost = estimatedCost;
      this.voteCount = voteCount;
      this.votedByUser = votedByUser;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public double getEstimatedCost() {
      return estimatedCost;
    }

    public int getVoteCount() {
      return voteCount;
    }

    public boolean isVotedByUser() {
      return votedByUser;
    }
  }
}
