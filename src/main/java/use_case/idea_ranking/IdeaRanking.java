package use_case.idea_ranking;

import entity.ActivityIdea;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Works out which ideas belong to a day, how many votes a member has used and how the ideas are
 * ranked. The use cases that show a day's ideas share it, so the ranking rule lives in one place:
 * most votes first, ties to the idea suggested first.
 */
public final class IdeaRanking {

  private IdeaRanking() {}

  /**
   * Picks out the ideas suggested for one day.
   *
   * @param ideas all the ideas of a trip
   * @param day the day to keep
   * @return the ideas for that day, in the order they were given
   */
  public static List<ActivityIdea> ideasForDay(List<ActivityIdea> ideas, LocalDate day) {
    final List<ActivityIdea> result = new ArrayList<>();
    for (ActivityIdea idea : ideas) {
      if (idea.getDay().equals(day)) {
        result.add(idea);
      }
    }
    return result;
  }

  /**
   * Counts how many ideas a member has voted for.
   *
   * @param dayIdeas the ideas of one day
   * @param username the member
   * @return the number of votes the member has used on those ideas
   */
  public static int countVotesUsed(List<ActivityIdea> dayIdeas, String username) {
    int used = 0;
    for (ActivityIdea idea : dayIdeas) {
      if (idea.hasVoted(username)) {
        used++;
      }
    }
    return used;
  }

  /**
   * Ranks a day's ideas: most votes first, ties to the idea suggested first.
   *
   * @param dayIdeas the ideas of one day
   * @param username the member looking, so each row can say whether they voted for it
   * @return the ideas as rows, best ranked first
   */
  public static List<RankedIdea> rank(List<ActivityIdea> dayIdeas, String username) {
    final List<ActivityIdea> sorted = new ArrayList<>(dayIdeas);
    sorted.sort(ActivityIdea.BY_RANK);

    final List<RankedIdea> rows = new ArrayList<>();
    for (ActivityIdea idea : sorted) {
      rows.add(
          new RankedIdea(
              idea.getId(),
              idea.getTitle(),
              idea.getEstimatedCost(),
              idea.getVoteCount(),
              idea.hasVoted(username)));
    }
    return rows;
  }
}
