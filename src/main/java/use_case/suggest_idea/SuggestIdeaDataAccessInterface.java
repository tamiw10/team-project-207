package use_case.suggest_idea;

import entity.ActivityIdea;
import entity.Trip;
import java.util.List;

/** The data the Suggest Activity Idea use case needs from storage. */
public interface SuggestIdeaDataAccessInterface {

  /**
   * Returns the trip with the given id.
   *
   * @param tripId the id to look up
   * @return the trip, or null if there is none
   */
  Trip getTrip(String tripId);

  /**
   * Returns every activity idea for a trip, in the order they were proposed.
   *
   * @param tripId the trip
   * @return the trip's activity ideas
   */
  List<ActivityIdea> getActivityIdeas(String tripId);

  /**
   * Saves a new activity idea.
   *
   * @param idea the idea to save
   */
  void saveActivityIdea(ActivityIdea idea);
}
