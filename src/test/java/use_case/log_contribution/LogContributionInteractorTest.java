package use_case.log_contribution;

import entity.Contribution;
import entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogContributionInteractorTest {

    /**
    private FakeDataAccessObject dataAccessObject;
    private FakePresenter presenter;
    private LogContributionInteractor interactor;

    private Trip trip;


    @BeforeEach
    void setUp() {

        // Create a trip that starts on February 15, 2027
        trip = new Trip(
                "trip1",
                "Japan Trip",
                "Japan",
                LocalDate.of(2027, 2, 15),
                LocalDate.of(2027, 2, 25),
                "CAD",
                100.0,
                "alice"
        );

        dataAccessObject = new FakeDataAccessObject(trip);
        presenter = new FakePresenter();

        interactor = new LogContributionInteractor(
                dataAccessObject,
                presenter
        );
    }

    // Fake DAO
    private static class FakeDataAccessObject
            implements LogContributionUserDataAccessInterface {

        private final Trip trip;
        private final List<Contribution> contributions =
                new ArrayList<>();

        FakeDataAccessObject(Trip trip) {
            this.trip = trip;
        }

        @Override
        public String getCurrentUsername() {
            return "alice";
        }

        @Override
        public Trip getTrip(String tripId) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
            return null;
        }

        @Override
        public void saveContribution(Contribution contribution) {
            contributions.add(contribution);
        }

        @Override
        public List<Contribution> getContributions(String tripId) {
            return new ArrayList<>(contributions);
        }

        @Override
        public List<Contribution> getContributions(String tripID, String username) {
            return new ArrayList<>(contributions);
        }
    }

    // Fake Presenter
    private static class FakePresenter
            implements LogContributionOutputBoundary {

        private LogContributionOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                LogContributionOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
        }
    }
    @Test
    void successFirstContribution() {

        LogContributionInputData inputData =
                new LogContributionInputData(
                        "trip1",
                        400.0,
                        LocalDate.of(2026, 11, 16),
                        2000.0
                );

        interactor.execute(inputData);

        // Check that the contribution was saved
        assertEquals(1, dataAccessObject.contributions.size());

        // Check that the contribution has the correct amount
        assertEquals(
                400.0,
                dataAccessObject.contributions.get(0).getAmount()
        );

        // Check that the savings goal was recorded
        assertTrue(trip.hasSavingsGoal("alice"));
        assertEquals(2000.0, trip.getSavingsGoal("alice"));

        // Check that the Interactor produced a success response
        assertNotNull(presenter.outputData);
        assertNull(presenter.errorMessage);
    }
    */
}
