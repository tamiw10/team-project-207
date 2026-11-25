package use_case.idea_ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.ActivityIdea;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class IdeaRankingTest {

  private static final LocalDate DAY = LocalDate.of(2027, 2, 14);
  private static final LocalDateTime NOON = LocalDateTime.of(2026, 11, 10, 12, 0);

  private ActivityIdea idea(String id, LocalDate day, int minute, String... voters) {
    final ActivityIdea idea =
        new ActivityIdea(id, "t1", day, "Idea " + id, 10, "emily", NOON.plusMinutes(minute));
    for (String voter : voters) {
      idea.addVote(voter);
    }
    return idea;
  }

  @Test
  void ideasForDayKeepsOnlyThatDay() {
    final List<ActivityIdea> all =
        List.of(idea("a", DAY, 0), idea("b", DAY.plusDays(1), 1), idea("c", DAY, 2));
    final List<ActivityIdea> result = IdeaRanking.ideasForDay(all, DAY);
    assertEquals(2, result.size());
    assertEquals("a", result.get(0).getId());
    assertEquals("c", result.get(1).getId());
  }

  @Test
  void countVotesUsedCountsOnlyThatMember() {
    final List<ActivityIdea> ideas =
        List.of(idea("a", DAY, 0, "amelia"), idea("b", DAY, 1, "emily"), idea("c", DAY, 2));
    assertEquals(1, IdeaRanking.countVotesUsed(ideas, "amelia"));
    assertEquals(0, IdeaRanking.countVotesUsed(ideas, "nobody"));
  }

  @Test
  void rankPutsMostVotesFirstAndTiesToEarliest() {
    final List<ActivityIdea> ideas =
        List.of(
            idea("late", DAY, 5, "emily"),
            idea("early", DAY, 1, "emily"),
            idea("top", DAY, 9, "emily", "amelia"));
    final List<RankedIdea> rows = IdeaRanking.rank(ideas, "amelia");
    assertEquals("top", rows.get(0).getId());
    assertEquals("early", rows.get(1).getId());
    assertEquals("late", rows.get(2).getId());
    assertTrue(rows.get(0).isVotedByUser());
    assertFalse(rows.get(1).isVotedByUser());
    assertEquals(2, rows.get(0).getVoteCount());
    assertEquals("Idea top", rows.get(0).getTitle());
    assertEquals(10.0, rows.get(0).getEstimatedCost());
  }
}
