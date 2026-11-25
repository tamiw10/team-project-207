package use_case.vote;

import entity.ActivityIdea;
import entity.Trip;
import java.time.LocalDate;
import java.util.List;
import use_case.idea_ranking.IdeaRanking;

/**
 * The Vote Interactor. Each member has {@value #VOTES_PER_DAY} votes per day to spend on that day's
 * ideas, at most one per idea. Voting for an idea they already voted for takes the vote back.
 */
public class VoteInteractor implements VoteInputBoundary {

  /** How many votes each member gets for each day of the trip. */
  public static final int VOTES_PER_DAY = 3;

  private static final String SAVE_FAILED = "Could not save your vote. Please try again.";

  private final VoteDataAccessInterface dataAccess;
  private final VoteOutputBoundary presenter;

  /**
   * Creates the interactor.
   *
   * @param dataAccess where trips and activity ideas are stored
   * @param presenter where results are sent
   */
  public VoteInteractor(VoteDataAccessInterface dataAccess, VoteOutputBoundary presenter) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
  }

  @Override
  public void execute(VoteInputData inputData) {
    final String username = inputData.getUsername();
    final LocalDate day = inputData.getDay();
    final Trip trip = findTripForMember(inputData.getTripId(), username);
    if (trip != null) {
      final List<ActivityIdea> dayIdeas = ideasForDay(trip.getId(), day);
      final ActivityIdea chosen = findIdea(dayIdeas, inputData.getIdeaId());
      if (chosen == null) {
        presenter.prepareFailView("That activity idea could not be found for this day.");
      } else if (chosen.hasVoted(username)) {
        // Extension 2a: voting again takes the vote back.
        chosen.removeVote(username);
        finish(trip, day, dayIdeas, chosen, username, VoteOutputData.Outcome.REMOVED);
      } else if (IdeaRanking.countVotesUsed(dayIdeas, username) >= VOTES_PER_DAY) {
        // Extension 2b: nothing changes.
        presenter.prepareFailView("No votes left today. Take a vote back first.");
      } else {
        chosen.addVote(username);
        finish(trip, day, dayIdeas, chosen, username, VoteOutputData.Outcome.ADDED);
      }
    }
  }

  @Override
  public void open(OpenActivitiesInputData inputData) {
    final String username = inputData.getUsername();
    final List<Trip> trips = dataAccess.getTripsForMember(username);
    if (trips.isEmpty()) {
      presenter.prepareFailView("You are not a member of any trip yet.");
    } else {
      final Trip trip = pickTrip(trips);
      dataAccess.setCurrentTripId(trip.getId());
      final LocalDate day = firstDayWithIdeas(trip);
      present(trip, day, ideasForDay(trip.getId(), day), username, VoteOutputData.Outcome.NONE);
    }
    presenter.switchToActivitiesView();
  }

  @Override
  public void loadDay(LoadDayInputData inputData) {
    final String username = inputData.getUsername();
    final Trip trip = findTripForMember(inputData.getTripId(), username);
    if (trip != null) {
      LocalDate day = inputData.getDay();
      if (day == null) {
        day = firstDayWithIdeas(trip);
      }
      final List<ActivityIdea> dayIdeas = ideasForDay(trip.getId(), day);
      present(trip, day, dayIdeas, username, VoteOutputData.Outcome.NONE);
    }
  }

  /**
   * Saves the changed idea and shows the result. If it cannot be saved, the vote is put back as it
   * was, so what is in memory matches what is stored.
   */
  private void finish(
      Trip trip,
      LocalDate day,
      List<ActivityIdea> dayIdeas,
      ActivityIdea changed,
      String username,
      VoteOutputData.Outcome outcome) {
    if (dataAccess.saveVote(changed)) {
      present(trip, day, dayIdeas, username, outcome);
    } else {
      if (outcome == VoteOutputData.Outcome.ADDED) {
        changed.removeVote(username);
      } else {
        changed.addVote(username);
      }
      presenter.prepareFailView(SAVE_FAILED);
    }
  }

  /** Looks up the trip and reports a failure if it is missing or the user is not a member. */
  private Trip findTripForMember(String tripId, String username) {
    final Trip trip = dataAccess.getTrip(tripId);
    Trip result = null;
    if (trip == null) {
      presenter.prepareFailView("This trip could not be found.");
    } else if (!trip.hasMember(username)) {
      presenter.prepareFailView("Only members of this trip can vote.");
    } else {
      result = trip;
    }
    return result;
  }

  /** The trip the user has open, or the first trip they belong to if none is open. */
  private Trip pickTrip(List<Trip> trips) {
    final String currentTripId = dataAccess.getCurrentTripId();
    Trip result = trips.get(0);
    for (Trip candidate : trips) {
      if (candidate.getId().equals(currentTripId)) {
        result = candidate;
      }
    }
    return result;
  }

  private List<ActivityIdea> ideasForDay(String tripId, LocalDate day) {
    return IdeaRanking.ideasForDay(dataAccess.getActivityIdeas(tripId), day);
  }

  private LocalDate firstDayWithIdeas(Trip trip) {
    final List<ActivityIdea> ideas = dataAccess.getActivityIdeas(trip.getId());
    LocalDate result = trip.getStartDate();
    boolean found = false;
    for (LocalDate day : trip.getDates()) {
      for (ActivityIdea idea : ideas) {
        if (!found && idea.getDay().equals(day)) {
          result = day;
          found = true;
        }
      }
    }
    return result;
  }

  private static ActivityIdea findIdea(List<ActivityIdea> ideas, String ideaId) {
    ActivityIdea result = null;
    for (ActivityIdea idea : ideas) {
      if (idea.getId().equals(ideaId)) {
        result = idea;
      }
    }
    return result;
  }

  /** Ranks the day (ties go to the idea suggested first, extension 4a) and presents it. */
  private void present(
      Trip trip,
      LocalDate day,
      List<ActivityIdea> dayIdeas,
      String username,
      VoteOutputData.Outcome outcome) {
    final int votesLeft =
        Math.max(0, VOTES_PER_DAY - IdeaRanking.countVotesUsed(dayIdeas, username));
    presenter.prepareSuccessView(
        new VoteOutputData(
            trip.getId(),
            username,
            trip.getName(),
            trip.getHomeCurrency(),
            day,
            trip.getDates(),
            IdeaRanking.rank(dayIdeas, username),
            votesLeft,
            VOTES_PER_DAY,
            outcome));
  }
}
