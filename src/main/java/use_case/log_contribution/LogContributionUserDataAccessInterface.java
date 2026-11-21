package use_case.log_contribution;

import entity.Contribution;
import entity.Trip;

import java.util.List;

public interface LogContributionUserDataAccessInterface {
    String getCurrentUsername();

    Trip getTrip(String tripId);

    void saveContribution(Contribution contribution);

    List<Contribution> getContributionsByTrip(String tripId);
}
