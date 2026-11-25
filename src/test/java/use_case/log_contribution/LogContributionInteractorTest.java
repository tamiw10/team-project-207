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
    private FakeUserDataAccessObject userDataAccessObject;
    private FakeTripDataAccessObject tripDataAccessObject;
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

        tripDataAccessObject = new FakeTripDataAccessObject(trip);
        userDataAccessObject = new FakeUserDataAccessObject(trip);
        presenter = new FakePresenter();

        interactor = new LogContributionInteractor(
                userDataAccessObject,
                tripDataAccessObject,
                presenter
        );
    }

    // Fake DAOs
    private static class FakeUserDataAccessObject
            implements LogContributionUserDataAccessInterface {

        private final Trip trip;

        FakeUserDataAccessObject(Trip trip) {
            this.trip = trip;
        }


        @Override
        public String getCurrentUsername() {
            return "alice";
        }
    }
    private static class FakeTripDataAccessObject
            implements LogContributionTripDataAccessInterface {

        private final Trip trip;
        private final List<Contribution> contributions =
                new ArrayList<>();

        FakeTripDataAccessObject(Trip trip) {
            this.trip = trip;
        }

        @Override
        public Trip getTrip(String tripId) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
            return null;
        }

        @Override
        public String getCurrentTripId() {
            return "";
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
                        "400.0",
                        "2026-11-16",
                        "2000.0"
                );

        interactor.execute(inputData);

        // Check that the contribution was saved
        assertEquals(1, tripDataAccessObject.contributions.size());

        // Check that the contribution has the correct amount
        assertEquals(
                400.0,
                tripDataAccessObject.contributions.get(0).getAmount()
        );

        // Check that the savings goal was recorded
        assertTrue(trip.hasSavingsGoal("alice"));
        assertEquals(2000.0, trip.getSavingsGoal("alice"));

        // Check some calculated output values
        assertEquals(400.0, presenter.outputData.getTotalSaved());
        assertEquals(2000.0, presenter.outputData.getSavingsGoal());
        assertFalse(presenter.outputData.isUserGoalReached());

        // Check that the Interactor produced a success response
        assertNotNull(presenter.outputData);
        assertNull(presenter.errorMessage);
    }
}
