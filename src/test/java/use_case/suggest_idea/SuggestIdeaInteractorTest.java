package use_case.suggest_idea;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryTripDataAccessObject;
import entity.ActivityIdea;
import entity.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuggestIdeaInteractorTest {

  private static final String TRIP = "t1";
  private static final LocalDate START = LocalDate.of(2027, 2, 13);
  private static final LocalDate DAY = START.plusDays(1);
  private static final LocalDateTime NOON = LocalDateTime.of(2026, 11, 10, 12, 0);

  private InMemoryTripDataAccessObject dao;
  private SuggestIdeaOutputData success;
  private String failure;
  private int successCount;
  private SuggestIdeaInteractor interactor;

  private final SuggestIdeaOutputBoundary presenter =
      new SuggestIdeaOutputBoundary() {
        @Override
        public void prepareSuccessView(SuggestIdeaOutputData outputData) {
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
    interactor = new SuggestIdeaInteractor(dao, presenter);
  }

  private void suggest(LocalDate day, String title, String cost, String user) {
    interactor.execute(new SuggestIdeaInputData(TRIP, day, title, cost, user));
  }

  private void existing(String id, LocalDate day, int minute, String... voters) {
    final ActivityIdea idea =
        new ActivityIdea(id, TRIP, day, "Idea " + id, 10, "emily", NOON.plusMinutes(minute));
    for (String voter : voters) {
      idea.addVote(voter);
    }
    dao.saveActivityIdea(idea);
  }

  @Test
  void suggestedIdeaIsSavedAndShownWithNoVotes() {
    suggest(DAY, "  Ramen night  ", "25.5", "amelia");
    assertNull(failure);
    assertEquals(DAY, success.getDay());
    assertEquals(1, success.getIdeas().size());
    final SuggestIdeaOutputData.IdeaRow shown = success.getIdeas().get(0);
    assertEquals("Ramen night", shown.getTitle());
    assertEquals(25.5, shown.getEstimatedCost());
    assertEquals(0, shown.getVoteCount());
    assertFalse(shown.isVotedByUser());
    final ActivityIdea saved = dao.getActivityIdeas(TRIP).get(0);
    assertEquals(shown.getId(), saved.getId());
    assertEquals("amelia", saved.getProposedBy());
    assertEquals(DAY, saved.getDay());
    assertEquals(0, saved.getVoteCount());
  }

  @Test
  void blankCostMeansFree() {
    suggest(DAY, "Park walk", "", "amelia");
    assertEquals(0.0, success.getIdeas().get(0).getEstimatedCost());
    suggest(DAY, "Park walk 2", null, "amelia");
    assertEquals(2, success.getIdeas().size());
  }

  @Test
  void newIdeaRanksBehindIdeasWithVotesAndShowsMyVotes() {
    existing("a", DAY, 0, "amelia", "emily");
    existing("other-day", DAY.plusDays(1), 1, "emily");
    suggest(DAY, "New", "0", "amelia");
    assertEquals(2, success.getIdeas().size());
    assertEquals("a", success.getIdeas().get(0).getId());
    assertTrue(success.getIdeas().get(0).isVotedByUser());
    assertEquals("New", success.getIdeas().get(1).getTitle());
  }

  @Test
  void rejectsBadInputAndSavesNothing() {
    suggest(DAY, "   ", "10", "amelia");
    assertEquals("Please enter a title for the idea.", failure);
    suggest(DAY, null, "10", "amelia");
    assertEquals("Please enter a title for the idea.", failure);
    suggest(DAY, "Museum", "abc", "amelia");
    assertEquals("Estimated cost must be a number.", failure);
    suggest(DAY, "Museum", "Infinity", "amelia");
    assertEquals("Estimated cost must be a number.", failure);
    suggest(DAY, "Museum", "-5", "amelia");
    assertEquals("Estimated cost cannot be negative.", failure);
    suggest(START.minusDays(1), "Museum", "5", "amelia");
    assertEquals("That day is not part of this trip.", failure);
    assertEquals(0, successCount);
    assertTrue(dao.getActivityIdeas(TRIP).isEmpty());
  }

  @Test
  void failsForUnknownTripOrNonMember() {
    interactor.execute(new SuggestIdeaInputData("nope", DAY, "Museum", "5", "amelia"));
    assertEquals("This trip could not be found.", failure);
    suggest(DAY, "Museum", "5", "stranger");
    assertEquals("Only members of this trip can suggest ideas.", failure);
    assertEquals(0, successCount);
  }
}
