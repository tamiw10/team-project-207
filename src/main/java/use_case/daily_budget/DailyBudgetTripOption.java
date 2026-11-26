package use_case.daily_budget;

import java.time.LocalDate;

/** The information needed to choose a trip and enter one of its dates. */
public final class DailyBudgetTripOption {
  private final String id;
  private final String label;
  private final LocalDate start;
  private final LocalDate end;

  /**
   * Creates a trip option for the budget screen.
   *
   * @param id the trip ID
   * @param label the text shown in the trip picker
   * @param start the first day of the trip
   * @param end the last day of the trip, inclusive
   */
  public DailyBudgetTripOption(String id, String label, LocalDate start, LocalDate end) {
    this.id = id;
    this.label = label;
    this.start = start;
    this.end = end;
  }

  /**
   * Returns the trip ID.
   *
   * @return the trip ID
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the first day of the trip.
   *
   * @return the first day of the trip
   */
  public LocalDate getStart() {
    return start;
  }

  /**
   * Returns the last day of the trip.
   *
   * @return the last day of the trip
   */
  public LocalDate getEnd() {
    return end;
  }

  /**
   * Returns the label displayed in the trip picker.
   *
   * @return the label displayed in the trip picker
   */
  @Override
  public String toString() {
    return label;
  }
}
