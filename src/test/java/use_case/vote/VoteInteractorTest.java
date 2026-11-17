package use_case.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryTripDataAccessObject;
import entity.ActivityIdea;
import entity.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoteInteractorTest {

  private static final String TRIP = "t1";
  private static final LocalDate START = LocalDate.of(2027, 2, 13);
  private static final LocalDate DAY = START.plusDays(1);
  private static final LocalDateTime NOON = LocalDateTime.of(2026, 11, 10, 12, 0);

  private InMemoryTripDataAccessObject dao;
  private VoteOutputData success;
  private String failure;
  private int successCount;
  private VoteInteractor interactor;

  private final VoteOutputBoundary presenter =
      new VoteOutputBoundary() {
        @Override
        public void prepareSuccessView(VoteOutputData outputData) {
          success = outputData;
          successCount++;
        }

        @Override
        public void prepareFailView(String errorMessage) {
          failure = errorMessage;
        }
      };

  @BeforeEach
  void setUp() {
    dao = new InMemoryTripDataAccessObject();
    final Trip trip =
        new Trip(TRIP, "Tokyo", "Tokyo, Japan", START, START.plusDays(2), "CAD", 100, "amelia");
    trip.addMember("emily");
    dao.saveTrip(trip);
    interactor = new VoteInteractor(dao, presenter);
  }

  private ActivityIdea idea(String id, LocalDate day, int minute, String... voters) {
    final ActivityIdea idea =
        new ActivityIdea(id, TRIP, day, "Idea " + id, 10, "emily", NOON.plusMinutes(minute));
    for (String voter : voters) {
      idea.addVote(voter);
    }
    dao.saveActivityIdea(idea);
    return idea;
  }

  private void vote(String ideaId, String user) {
    interactor.execute(new VoteInputData(TRIP, DAY, ideaId, user));
  }

  @Test
  void voteIsCountedOnceAndVotesLeftDrops() {
    idea("a", DAY, 0);
    vote("a", "amelia");
    assertNull(failure);
    assertEquals(VoteOutputData.Outcome.ADDED, success.getOutcome());
    assertEquals(2, success.getVotesLeft());
    assertEquals(3, success.getVotesPerDay());
    assertEquals(1, success.getRankedIdeas().get(0).getVoteCount());
    assertTrue(success.getRankedIdeas().get(0).isVotedByUser());
    assertEquals("Idea a", success.getRankedIdeas().get(0).getTitle());
    assertEquals(10.0, success.getRankedIdeas().get(0).getEstimatedCost());
    assertTrue(dao.getActivityIdea("a").hasVoted("amelia"));
    assertEquals("Tokyo", success.getTripName());
    assertEquals("CAD", success.getHomeCurrency());
    assertEquals(DAY, success.getDay());
    assertEquals(3, success.getTripDays().size());
  }

  @Test
  void votingAgainRemovesTheVote() {
    idea("a", DAY, 0, "amelia", "emily");
    vote("a", "amelia");
    assertEquals(VoteOutputData.Outcome.REMOVED, success.getOutcome());
    assertEquals(3, success.getVotesLeft());
    assertFalse(dao.getActivityIdea("a").hasVoted("amelia"));
    assertEquals(1, dao.getActivityIdea("a").getVoteCount());
    assertFalse(success.getRankedIdeas().get(0).isVotedByUser());
  }

  @Test
  void fourthVoteIsRejectedAndNothingChanges() {
    idea("a", DAY, 0, "amelia");
    idea("b", DAY, 1, "amelia");
    idea("c", DAY, 2, "amelia");
    idea("d", DAY, 3);
    vote("d", "amelia");
    assertEquals("No votes left today. Take a vote back first.", failure);
    assertEquals(0, successCount);
    assertEquals(0, dao.getActivityIdea("d").getVoteCount());
  }

  @Test
  void canStillTakeAVoteBackWhenAllVotesAreUsed() {
    idea("a", DAY, 0, "amelia");
    idea("b", DAY, 1, "amelia");
    idea("c", DAY, 2, "amelia");
    vote("c", "amelia");
    assertEquals(VoteOutputData.Outcome.REMOVED, success.getOutcome());
    assertEquals(1, success.getVotesLeft());
  }

  @Test
  void votesOnOtherDaysDoNotCount() {
    idea("a", DAY.plusDays(1), 0, "amelia");
    idea("b", DAY.plusDays(1), 1, "amelia");
    idea("c", DAY.plusDays(1), 2, "amelia");
    idea("d", DAY, 3);
    vote("d", "amelia");
    assertNull(failure);
    assertEquals(2, success.getVotesLeft());
  }

  @Test
  void otherMembersVotesDoNotUseMyVotes() {
    idea("a", DAY, 0, "emily");
    idea("b", DAY, 1, "emily");
    idea("c", DAY, 2, "emily");
    vote("a", "amelia");
    assertNull(failure);
    assertEquals(2, success.getVotesLeft());
    assertEquals(2, success.getRankedIdeas().get(0).getVoteCount());
  }

  @Test
  void ranksByVotesAndTiesGoToEarliestSuggested() {
    idea("late", DAY, 5, "emily");
    idea("early", DAY, 1);
    idea("top", DAY, 9, "emily", "amelia");
    vote("early", "amelia");
    final List<RankedIdea> ranked = success.getRankedIdeas();
    assertEquals("top", ranked.get(0).getId());
    assertEquals("early", ranked.get(1).getId());
    assertEquals("late", ranked.get(2).getId());
  }

  @Test
  void votingMovesAnIdeaUpTheRanking() {
    idea("a", DAY, 0, "emily");
    idea("b", DAY, 1, "emily");
    vote("b", "amelia");
    assertEquals("b", success.getRankedIdeas().get(0).getId());
  }

  @Test
  void unknownIdeaFails() {
    idea("a", DAY, 0);
    vote("missing", "amelia");
    assertEquals("That activity idea could not be found for this day.", failure);
  }

  @Test
  void ideaFromAnotherDayIsNotFound() {
    idea("a", DAY.plusDays(1), 0);
    vote("a", "amelia");
    assertNotNull(failure);
    assertEquals(0, successCount);
  }

  @Test
  void unknownTripFails() {
    interactor.execute(new VoteInputData("nope", DAY, "a", "amelia"));
    assertEquals("This trip could not be found.", failure);
  }

  @Test
  void nonMemberCannotVote() {
    idea("a", DAY, 0);
    vote("a", "stranger");
    assertEquals("Only members of this trip can vote.", failure);
    assertEquals(0, dao.getActivityIdea("a").getVoteCount());
  }

  @Test
  void loadDayShowsRankedIdeasWithoutChangingAnything() {
    idea("a", DAY, 0, "emily");
    idea("b", DAY, 1, "emily", "amelia");
    interactor.loadDay(new LoadDayInputData(TRIP, DAY, "amelia"));
    assertEquals(VoteOutputData.Outcome.NONE, success.getOutcome());
    assertEquals("b", success.getRankedIdeas().get(0).getId());
    assertEquals(2, success.getVotesLeft());
    assertEquals(1, dao.getActivityIdea("a").getVoteCount());
  }

  @Test
  void loadDayWithNoDayStartsOnFirstDayWithIdeas() {
    idea("a", START.plusDays(2), 0);
    idea("b", START.plusDays(1), 1);
    interactor.loadDay(new LoadDayInputData(TRIP, null, "amelia"));
    assertEquals(START.plusDays(1), success.getDay());
    assertEquals(1, success.getRankedIdeas().size());
  }

  @Test
  void loadDayWithNoIdeasAnywhereStartsOnFirstDay() {
    interactor.loadDay(new LoadDayInputData(TRIP, null, "amelia"));
    assertEquals(START, success.getDay());
    assertTrue(success.getRankedIdeas().isEmpty());
    assertEquals(3, success.getVotesLeft());
  }

  @Test
  void loadDayFailsForUnknownTripOrNonMember() {
    interactor.loadDay(new LoadDayInputData("nope", DAY, "amelia"));
    assertEquals("This trip could not be found.", failure);
    interactor.loadDay(new LoadDayInputData(TRIP, DAY, "stranger"));
    assertEquals("Only members of this trip can vote.", failure);
    assertEquals(0, successCount);
  }
}
