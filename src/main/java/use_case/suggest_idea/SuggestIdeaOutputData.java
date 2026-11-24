package use_case.suggest_idea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import use_case.idea_ranking.RankedIdea;

/** Output Data for the Suggest Idea Use Case: the day's ideas, ranked, with the new one in. */
public class SuggestIdeaOutputData {

  private final LocalDate day;
  private final List<RankedIdea> ideas;

  /**
   * Creates the output data.
   *
   * @param day the day the idea was added to
   * @param ideas that day's ideas, best ranked first
   */
  public SuggestIdeaOutputData(LocalDate day, List<RankedIdea> ideas) {
    this.day = day;
    this.ideas = new ArrayList<>(ideas);
  }

  public LocalDate getDay() {
    return day;
  }

  public List<RankedIdea> getIdeas() {
    return Collections.unmodifiableList(ideas);
  }
}
