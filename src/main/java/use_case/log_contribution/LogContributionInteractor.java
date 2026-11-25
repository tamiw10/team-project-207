package use_case.log_contribution;

import entity.Trip;
import entity.Contribution;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.*;

/** Interactor for Use Case 2: Log a Contribution
 * Validates contribution inputs against the open {@link Trip},
 * performs calculations to retrieve output data information,
 * saves resulting {@link Contribution} through {@link LogContributionUserDataAccessInterface} and
 * {@link LogContributionTripDataAccessInterface}
 */
public class LogContributionInteractor implements LogContributionInputBoundary {

    // creates output data object and passes it through output boundary
    private final LogContributionUserDataAccessInterface userDataAccessObject;
    private final LogContributionTripDataAccessInterface tripDataAccessObject;
    private final LogContributionOutputBoundary logContributionPresenter;

    public LogContributionInteractor(LogContributionUserDataAccessInterface userDataAccessObject,
                                     LogContributionTripDataAccessInterface tripDataAccessObject,
                                     LogContributionOutputBoundary logContributionPresenter) {

        this.userDataAccessObject = userDataAccessObject;
        this.tripDataAccessObject = tripDataAccessObject;
        this.logContributionPresenter = logContributionPresenter;
    }

    @Override
    public void execute(LogContributionInputData inputData) {
        // Check if trip exists
        final String tripID = inputData.getTripId().isBlank()
                            ? tripDataAccessObject.getCurrentTripId()
                            : inputData.getTripId();
        final Trip trip = tripID.isBlank()
                        ? null
                        : tripDataAccessObject.getTrip(tripID);
        if (trip == null) {
            logContributionPresenter.prepareFailView("No trip is currently open");
            return;
        }
        // Retrieve trip information
        final String tripName = trip.getName();
        final String homeCurrency = trip.getHomeCurrency();
        final String username = userDataAccessObject.getCurrentUsername();
        final String amountText = inputData.getAmount();
        final String dateText = inputData.getDate();
        final String savingsGoalText = inputData.getSavingsGoal();
        final boolean hasGoal = trip.hasSavingsGoal(username);

        // Extensions:
        // 2a. The amount is not a number, or is zero or negative.
        final double amount;
        try {
            if (amountText == null || amountText.isBlank()) {
                logContributionPresenter.prepareFailView("Enter an amount first");
                return;
            }
            amount = Double.parseDouble(inputData.getAmount().trim());
        } catch (NumberFormatException ex) {
            logContributionPresenter.prepareFailView("Amount must be a number");
            return;
        }
        if (amount <= 0 || !Double.isFinite(amount)) {
            logContributionPresenter.prepareFailView("Amount must be greater than 0");
            return;
        }

        // 2c. The date is on or after the trip's start date.
        // might want to add a check that user doesn't enter a date earlier than past contributions
        final LocalDate date;
        try {
            if (dateText == null || dateText.isBlank()) {
                logContributionPresenter.prepareFailView("Enter a date first");
                return;
            }
            date = LocalDate.parse(inputData.getDate().trim());
        } catch (DateTimeParseException ex) {
            logContributionPresenter.prepareFailView("Date must be in YYYY-MM-DD format");
            return;
        }
        if (!date.isBefore(trip.getStartDate())) {
            logContributionPresenter.prepareFailView("Savings are for before the trip starts");
            return;
        }

        // 2b. The user has no savings goal and did not provide a valid one.
        final double savingsGoal;
        if (!hasGoal) {
            try {
                if (savingsGoalText == null || savingsGoalText.isBlank()) {
                    logContributionPresenter.prepareFailView("Enter a savings goal first");
                    return;
                }
                savingsGoal = Double.parseDouble(inputData.getSavingsGoal().trim());
            } catch (NumberFormatException ex) {
                logContributionPresenter.prepareFailView("Savings goal must be a number");
                return;
            }
            if (savingsGoal <= 0 || !Double.isFinite(savingsGoal)) {
                logContributionPresenter.prepareFailView("Savings goal must be greater than 0");
                return;
            }
            // 3. The system records the goal
            trip.setSavingsGoal(username, savingsGoal);
        } else {
            savingsGoal = trip.getSavingsGoal(username);
        }


        // 3. the system records the contribution
        final Contribution contribution = new Contribution(UUID.randomUUID().toString(), tripID, username, amount, date);
        tripDataAccessObject.saveContribution(contribution);

        // 4. The system works out how much the user has saved in total, how much is left to reach the goal,
        //    and how many weeks there are from the contribution's date to the trip's start date.
        // 5. The system works out how much the user needs to save per week from now to reach the goal in time, and how
        //    much per week they have saved so far.
        final List<Contribution> groupContributions = tripDataAccessObject.getContributions(tripID);
        final List<Contribution> userContributions = tripDataAccessObject.getContributions(tripID, username);
        // Total user savings
        double tSaved = 0;
        for (Contribution userContribution : userContributions) {
            tSaved += userContribution.getAmount();
        }
        final double totalSaved = tSaved;
        // User progress bar completion percentage
        final double userProgress = totalSaved / savingsGoal;

        // Group savings: sum member total savings
        double gSaved = 0;
        for (Contribution groupContribution : groupContributions) {
            gSaved += groupContribution.getAmount();
        }
        final double groupSaved = gSaved;
        // Group goal: sum member goals
        double groupGoal = trip.getTotalSavingsGoal();
        // Group progress bar completion percentage
        final double groupProgress = groupSaved / groupGoal;

        // Goal reached
        final boolean userGoalReached = totalSaved >= savingsGoal;
        final boolean groupGoalReached = groupSaved >= groupGoal;

        // Weeks remaining until trip
        final long daysRemainingLong = ChronoUnit.DAYS.between(date, trip.getStartDate());
        final int daysRemaining = Math.toIntExact(daysRemainingLong);
        final int weeksRemaining = max(1, Math.ceilDiv(daysRemaining, 7));

        // 4a. The user's total savings are now equal to or more than their goal.
        if (userGoalReached) {
            final double amountRemaining = 0;
            final boolean onTrack = true;

            // Could null work instead?
            final double weeklyRequired = 0;
            final double weeklyPace = 0; // set as 0 for now

            // Prepare output
            LogContributionOutputData outputData =
                    new LogContributionOutputData(tripID,
                            tripName,
                            homeCurrency,
                            hasGoal,
                            savingsGoal,
                            totalSaved,
                            amountRemaining,
                            weeksRemaining,
                            weeklyRequired,
                            weeklyPace,
                            onTrack,
                            groupSaved,
                            groupGoal,
                            userGoalReached,
                            groupGoalReached,
                            userProgress,
                            groupProgress);

            logContributionPresenter.prepareSuccessView(outputData);
        }

        if (!userGoalReached) {
            // Remaining amount
            final double amountRemaining = savingsGoal - totalSaved;

            // Required weekly savings to reach goal
            final BigDecimal weeklyRequiredBig = BigDecimal.valueOf(amountRemaining / weeksRemaining)
                                                .setScale(2, RoundingMode.HALF_UP);
            final double weeklyRequired = weeklyRequiredBig.doubleValue();

            // Current pace
            final LocalDate firstContributionDate = userContributions.get(0).getDate();
            final long daysElapsedLong = ChronoUnit.DAYS.between(firstContributionDate, date);
            final int daysElapsed = Math.toIntExact(daysElapsedLong);
            final int weeksElapsed = max(1, Math.ceilDiv(daysElapsed, 7));
            final BigDecimal weeklyPaceBig = BigDecimal.valueOf(totalSaved / weeksElapsed)
                                            .setScale(2, RoundingMode.HALF_UP);
            final double weeklyPace = weeklyPaceBig.doubleValue();

            // On/behind track
            final boolean onTrack = weeklyPace >= weeklyRequired;

            // Prepare output
            LogContributionOutputData outputData =
                    new LogContributionOutputData(tripID,
                            tripName,
                            homeCurrency,
                            hasGoal,
                            savingsGoal,
                            totalSaved,
                            amountRemaining,
                            weeksRemaining,
                            weeklyRequired,
                            weeklyPace,
                            onTrack,
                            groupSaved,
                            groupGoal,
                            userGoalReached,
                            groupGoalReached,
                            userProgress,
                            groupProgress);

            logContributionPresenter.prepareSuccessView(outputData);
        }
    }

    // does trip need to be resaved?
}