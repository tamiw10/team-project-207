package use_case.suggest_idea;

import entity.ActivityIdea;
import entity.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import use_case.idea_ranking.IdeaRanking;
import use_case.idea_ranking.RankedIdea;

/** The Suggest Idea Interactor: a member adds an activity idea for a day of the trip. */
public class SuggestIdeaInteractor implements SuggestIdeaInputBoundary {

  private final SuggestIdeaDataAccessInterface dataAccess;
  private final SuggestIdeaOutputBoundary presenter;
  private final Supplier<LocalDateTime> clock;

  /**
   * Creates the interactor.
   *
   * @param dataAccess where trips and activity ideas are stored
   * @param presenter where results are sent
   * @param clock tells the current time, used to record when an idea was suggested (ties between
   *     equally voted ideas go to the one suggested first)
   */
  public SuggestIdeaInteractor(
      SuggestIdeaDataAccessInterface dataAccess,
      SuggestIdeaOutputBoundary presenter,
      Supplier<LocalDateTime> clock) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
    this.clock = clock;
  }

  @Override
  public void execute(SuggestIdeaInputData inputData) {
    final String username = inputData.getUsername();
    final Trip trip = dataAccess.getTrip(inputData.getTripId());
    final LocalDate day = inputData.getDay();
    final String title = inputData.getTitle() == null ? "" : inputData.getTitle().trim();
    final String costText =
        inputData.getEstimatedCost() == null ? "" : inputData.getEstimatedCost().trim();
    final double cost = parseCost(costText);

    if (trip == null) {
      presenter.prepareFailView("This trip could not be found.");
    } else if (!trip.hasMember(username)) {
      presenter.prepareFailView("Only members of this trip can suggest ideas.");
    } else if (!trip.containsDate(day)) {
      presenter.prepareFailView("That day is not part of this trip.");
    } else if (title.isEmpty()) {
      presenter.prepareFailView("Please enter a title for the idea.");
    } else if (Double.isNaN(cost)) {
      presenter.prepareFailView("Estimated cost must be a number.");
    } else if (cost < 0) {
      presenter.prepareFailView("Estimated cost cannot be negative.");
    } else {
      dataAccess.saveActivityIdea(
          new ActivityIdea(
              UUID.randomUUID().toString(),
              trip.getId(),
              day,
              title,
              cost,
              username,
              clock.get()));
      final List<RankedIdea> rows =
          IdeaRanking.rank(
              IdeaRanking.ideasForDay(dataAccess.getActivityIdeas(trip.getId()), day), username);
      presenter.prepareSuccessView(new SuggestIdeaOutputData(day, rows));
    }
  }

  /** Reads the typed cost: blank means free, and anything that is not a finite number is NaN. */
  private static double parseCost(String text) {
    double result = 0;
    if (!text.isEmpty()) {
      try {
        result = Double.parseDouble(text);
      } catch (NumberFormatException ex) {
        result = Double.NaN;
      }
      if (Double.isInfinite(result)) {
        result = Double.NaN;
      }
    }
    return result;
  }
}
