package interface_adapter.vote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import use_case.vote.RankedIdea;

/** The state of the activities screen: the day shown, its ranked ideas and the user's votes. */
public class ActivitiesState {

  private String username = "";
  private String tripId = "";
  private String tripName = "";
  private String homeCurrency = "";
  private LocalDate day;
  private List<LocalDate> tripDays = new ArrayList<>();
  private List<RankedIdea> rankedIdeas = new ArrayList<>();
  private int votesLeft;
  private int votesPerDay;
  private String message = "";
  private String error = "";

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTripId() {
    return tripId;
  }

  public void setTripId(String tripId) {
    this.tripId = tripId;
  }

  public String getTripName() {
    return tripName;
  }

  public void setTripName(String tripName) {
    this.tripName = tripName;
  }

  public String getHomeCurrency() {
    return homeCurrency;
  }

  public void setHomeCurrency(String homeCurrency) {
    this.homeCurrency = homeCurrency;
  }

  public LocalDate getDay() {
    return day;
  }

  public void setDay(LocalDate day) {
    this.day = day;
  }

  public List<LocalDate> getTripDays() {
    return tripDays;
  }

  public void setTripDays(List<LocalDate> tripDays) {
    this.tripDays = new ArrayList<>(tripDays);
  }

  public List<RankedIdea> getRankedIdeas() {
    return rankedIdeas;
  }

  public void setRankedIdeas(List<RankedIdea> rankedIdeas) {
    this.rankedIdeas = new ArrayList<>(rankedIdeas);
  }

  public int getVotesLeft() {
    return votesLeft;
  }

  public void setVotesLeft(int votesLeft) {
    this.votesLeft = votesLeft;
  }

  public int getVotesPerDay() {
    return votesPerDay;
  }

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

  public void setError(String error) {
    this.error = error;
  }
}
