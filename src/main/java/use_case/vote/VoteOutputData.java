package use_case.vote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Output Data for the Vote Use Case: the day's ranked ideas and the user's remaining votes. */
public class VoteOutputData {

  /** What the request did. */
  public enum Outcome {
    /** The day was only shown; no vote changed. */
    NONE,
    /** The user's vote was added. */
    ADDED,
    /** The user's vote was taken back. */
    REMOVED
  }

  private final String tripName;
  private final String homeCurrency;
  private final LocalDate day;
  private final List<LocalDate> tripDays;
  private final List<RankedIdea> rankedIdeas;
  private final int votesLeft;
  private final int votesPerDay;
  private final Outcome outcome;

  /**
   * Creates the output data.
   *
   * @param tripName the trip's name
   * @param homeCurrency the trip's home currency, used to show idea costs
   * @param day the day being shown
   * @param tripDays every day of the trip, for the day picker
   * @param rankedIdeas the day's ideas, best ranked first
   * @param votesLeft how many of the user's votes for this day are still unused
   * @param votesPerDay how many votes each member gets per day
   * @param outcome what the request did
   */
  public VoteOutputData(
      String tripName,
      String homeCurrency,
      LocalDate day,
      List<LocalDate> tripDays,
      List<RankedIdea> rankedIdeas,
      int votesLeft,
      int votesPerDay,
      Outcome outcome) {
    this.tripName = tripName;
    this.homeCurrency = homeCurrency;
    this.day = day;
    this.tripDays = new ArrayList<>(tripDays);
    this.rankedIdeas = new ArrayList<>(rankedIdeas);
    this.votesLeft = votesLeft;
    this.votesPerDay = votesPerDay;
    this.outcome = outcome;
  }

  public String getTripName() {
    return tripName;
  }

  public String getHomeCurrency() {
    return homeCurrency;
  }

  public LocalDate getDay() {
    return day;
  }

  public List<LocalDate> getTripDays() {
    return Collections.unmodifiableList(tripDays);
  }

  public List<RankedIdea> getRankedIdeas() {
    return Collections.unmodifiableList(rankedIdeas);
  }

  public int getVotesLeft() {
    return votesLeft;
  }

  public int getVotesPerDay() {
    return votesPerDay;
  }

  public Outcome getOutcome() {
    return outcome;
  }
}
