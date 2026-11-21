package use_case.log_contribution;

import entity.Trip;
import entity.Contribution;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class LogContributionInteractor implements LogContributionInputBoundary {

    private final LogContributionDataAccessInterface dataAccessObject;
    private final LogContributionOutputBoundary presenter;

    public LogContributionInteractor(
            LogContributionDataAccessInterface dataAccessObject,
            LogContributionOutputBoundary presenter) {

        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(LogContributionInputData inputData) {

        // Retrieve current user and trip
        String username = dataAccessObject.getCurrentUsername();
        Trip trip = dataAccessObject.getTrip(inputData.getTripId());

        if (username == null || trip == null) {
            presenter.prepareFailView("User or trip could not be found.");
            return;
        }

        // Check that the user belongs to the trip
        if (!trip.hasMember(username)) {
            presenter.prepareFailView("You are not a member of this trip.");
            return;
        }

        // Validate amount
        if (inputData.getAmount() <= 0 ||
                !Double.isFinite(inputData.getAmount())) {
            presenter.prepareFailView("Contribution amount must be positive.");
            return;
        }

        // Validate date
        LocalDate date = inputData.getDate();

        if (date == null) {
            presenter.prepareFailView("Please provide a date.");
            return;
        }

        if (!date.isBefore(trip.getStartDate())) {
            presenter.prepareFailView(
                    "Contributions must be made before the trip starts.");
            return;
        }

        // Check whether the user already has a savings goal
        boolean hasGoal = trip.hasSavingsGoal(username);

        if (!hasGoal) {

            Double goal = inputData.getSavingsGoal();

            if (goal == null || !Double.isFinite(goal) || goal <= 0) {
                presenter.prepareFailView(
                        "Please provide a valid savings goal.");
                return;
            }

            trip.setSavingsGoal(username, goal);
        }

        // Retrieve existing contributions
        List<Contribution> contributions =
                dataAccessObject.getContributionsByTrip(trip.getId());

        // Calculate the user's existing savings
        double totalSaved = 0;
        LocalDate firstContributionDate = date;

        for (Contribution contribution : contributions) {

            if (contribution.getMemberUsername().equals(username)) {

                totalSaved += contribution.getAmount();

                if (contribution.getDate().isBefore(firstContributionDate)) {
                    firstContributionDate = contribution.getDate();
                }
            }
        }

        // Create and save the new contribution
        Contribution newContribution = new Contribution(
                UUID.randomUUID().toString(),
                trip.getId(),
                username,
                inputData.getAmount(),
                date
        );

        dataAccessObject.saveContribution(newContribution);

        // Include the new contribution in the total
        totalSaved += inputData.getAmount();

        // Retrieve the user's savings goal
        double savingsGoal = trip.getSavingsGoal(username);

        // Calculate group savings
        double groupSaved = 0;

        for (Contribution contribution : contributions) {
            groupSaved += contribution.getAmount();
        }

        groupSaved += inputData.getAmount();

        double groupGoal = trip.getTotalSavingsGoal();

        // Check whether the user has reached their goal
        boolean goalReached = totalSaved >= savingsGoal;

        double remaining = Math.max(0, savingsGoal - totalSaved);

        // Calculate weeks remaining until the trip
        long daysRemaining =
                ChronoUnit.DAYS.between(date, trip.getStartDate());

        int weeksRemaining = Math.max(
                1,
                (int) Math.ceil(daysRemaining / 7.0)
        );

        Double weeklyRequired = null;
        Double weeklyPace = null;
        boolean onTrack = false;

        if (!goalReached) {

            // Calculate required weekly savings
            weeklyRequired = remaining / weeksRemaining;

            // Calculate current weekly savings pace
            long daysElapsed =
                    ChronoUnit.DAYS.between(firstContributionDate, date);

            int weeksElapsed = Math.max(
                    1,
                    (int) Math.ceil(daysElapsed / 7.0)
            );

            weeklyPace = totalSaved / weeksElapsed;

            // Determine whether the user is on track
            onTrack = weeklyPace >= weeklyRequired;
        }

        // Prepare output
        LogContributionOutputData outputData =
                new LogContributionOutputData(
                        savingsGoal,
                        totalSaved,
                        remaining,
                        weeksRemaining,
                        weeklyRequired,
                        weeklyPace,
                        onTrack,
                        groupSaved,
                        groupGoal,
                        goalReached
                );

        presenter.prepareSuccessView(outputData);
    }

}