package use_case.log_contribution;

import entity.Contribution;
import entity.Trip;

import java.util.List;

/**
 * Trip DAO for Log Contribution Use Case
 * implemented by {@link data_access.InMemoryTripDataAccessObject} and
 * {@link data_access.JsonFileTripDataAccessObject}. (not true yet)
 */
public interface LogContributionTripDataAccessInterface {
    /**
     * Returns the trip with the given id.
     *
     * @param tripId the trip
     * @return the trip, or null if there is none
     */
    Trip getTrip(String tripId);

    /**
     * Returns the id of the trip
     *
     * @return the id of the trip
     */
    String getCurrentTripId();

    /**
     * Saves a new contribution or replaces the stored contribution with the same id.
     *
     * @param contribution the contribution to save
     */
    void saveContribution(Contribution contribution);

    /**
     * Returns every contribution logged for a trip, in the order they were logged.
     *
     * @param tripId the trip
     * @return the trip's contribution
     */
    List<Contribution> getContributions(String tripId);

    /**
     * Returns every contribution logged for a trip by a specific user, in the order they were logged.
     *
     * @param tripId the trip
     * @param username the user
     * @return the trip's contributions by the user
     */
    List<Contribution> getContributions(String tripId, String username);
}
