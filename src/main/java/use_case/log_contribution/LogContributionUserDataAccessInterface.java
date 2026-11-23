package use_case.log_contribution;

import entity.Contribution;
import entity.Trip;

import java.util.List;


/** DAO for Log Contribution Use Case
 * implemented by {@link data_access.InMemoryUserDataAccessObject}
 */
public interface LogContributionUserDataAccessInterface {

    /**
     * Returns username of current user
     *
     * @return username of current user
     */
    String getCurrentUsername();
}
