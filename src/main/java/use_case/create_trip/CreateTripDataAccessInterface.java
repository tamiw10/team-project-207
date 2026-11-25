package use_case.create_trip;

import entity.Trip;

/** The part of the trip storage the Create Trip Use Case needs. */
public interface CreateTripDataAccessInterface {

  /**
   * Saves the new trip.
   *
   * @param trip the trip to save
   */
  void saveTrip(Trip trip);

  /**
   * Records which trip the user has open.
   *
   * @param tripId the id of the open trip
   */
  void setCurrentTripId(String tripId);
}
