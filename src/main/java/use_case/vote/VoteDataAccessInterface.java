package use_case.vote;

import entity.ActivityIdea;
import entity.Trip;
import java.util.List;

/** The data the Vote on Activity Ideas use case needs from storage. */
public interface VoteDataAccessInterface {

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
   * Returns the trips a member belongs to.
   *
   * @param username the member
   * @return their trips, in the order they were created; empty if they belong to none
   */
  List<Trip> getTripsForMember(String username);

  /**
   * Returns the id of the trip the user has open.
   *
   * @return the open trip's id, or null if no trip is open
   */
  String getCurrentTripId();

  /**
   * Remembers which trip the user has open.
   *
   * @param tripId the id of the open trip
   */
  void setCurrentTripId(String tripId);

  /**
   * Saves an activity idea after its votes changed, replacing the stored idea with the same id.
   *
   * @param idea the idea to save
   * @return true if it was saved, false if it could not be saved
   */
  boolean saveVote(ActivityIdea idea);
}
