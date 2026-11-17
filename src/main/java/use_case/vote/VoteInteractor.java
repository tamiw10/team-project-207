package use_case.vote;

import entity.ActivityIdea;
import entity.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Vote Interactor. Each member has {@value #VOTES_PER_DAY} votes per day to spend on that day's
 * ideas, at most one per idea. Voting for an idea they already voted for takes the vote back.
 */
public class VoteInteractor implements VoteInputBoundary {

  /** How many votes each member gets for each day of the trip. */
  public static final int VOTES_PER_DAY = 3;

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
      } else if (countVotesUsed(dayIdeas, username) >= VOTES_PER_DAY) {
        // Extension 2b: nothing changes.
        presenter.prepareFailView("No votes left today. Take a vote back first.");
      } else {
        chosen.addVote(username);
        finish(trip, day, dayIdeas, chosen, username, VoteOutputData.Outcome.ADDED);
      }
    }
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

  private void finish(
      Trip trip,
      LocalDate day,
      List<ActivityIdea> dayIdeas,
      ActivityIdea changed,
      String username,
      VoteOutputData.Outcome outcome) {
    dataAccess.saveActivityIdea(changed);
    present(trip, day, dayIdeas, username, outcome);
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

  private List<ActivityIdea> ideasForDay(String tripId, LocalDate day) {
    final List<ActivityIdea> result = new ArrayList<>();
    for (ActivityIdea idea : dataAccess.getActivityIdeas(tripId)) {
      if (idea.getDay().equals(day)) {
        result.add(idea);
      }
    }
    return result;
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

  private static int countVotesUsed(List<ActivityIdea> dayIdeas, String username) {
    int used = 0;
    for (ActivityIdea idea : dayIdeas) {
      if (idea.hasVoted(username)) {
        used++;
      }
    }
    return used;
  }

  /** Ranks the day (ties go to the idea suggested first, extension 4a) and presents it. */
  private void present(
      Trip trip,
      LocalDate day,
      List<ActivityIdea> dayIdeas,
      String username,
      VoteOutputData.Outcome outcome) {
    final List<ActivityIdea> ranked = new ArrayList<>(dayIdeas);
    ranked.sort(ActivityIdea.BY_RANK);

    final List<RankedIdea> rows = new ArrayList<>();
    for (ActivityIdea idea : ranked) {
      rows.add(
          new RankedIdea(
              idea.getId(),
              idea.getTitle(),
              idea.getEstimatedCost(),
              idea.getVoteCount(),
              idea.hasVoted(username)));
    }
    final int votesLeft = Math.max(0, VOTES_PER_DAY - countVotesUsed(dayIdeas, username));
    presenter.prepareSuccessView(
        new VoteOutputData(
            trip.getName(),
            trip.getHomeCurrency(),
            day,
            trip.getDates(),
            rows,
            votesLeft,
            VOTES_PER_DAY,
            outcome));
  }
}
