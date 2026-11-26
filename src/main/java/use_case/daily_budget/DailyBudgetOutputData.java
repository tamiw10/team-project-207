package use_case.daily_budget;

import entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Budget summary for one day, with all amounts in the trip's home currency. */
public final class DailyBudgetOutputData {
  private final LocalDate date;
  private final String currency;
  private final BigDecimal dailyBudget;
  private final BigDecimal carryOver;
  private final BigDecimal spent;
  private final Map<ExpenseCategory, BigDecimal> categoryTotals;
  private final BigDecimal projectedCost;
  private final BigDecimal totalBudget;

  /**
   * Creates a summary with amounts in the trip's home currency.
   *
   * @param date the selected trip day
   * @param currency the trip's home currency code
   * @param dailyBudget the shared budget per day
   * @param carryOver the balance from earlier days, negative if overspent
   * @param spent the amount spent on the selected day
   * @param categoryTotals spending on the selected day by category
   * @param projectedCost the estimated full trip cost based on spending through the selected day
   * @param totalBudget the daily budget multiplied by the number of trip days
   */
  public DailyBudgetOutputData(
      LocalDate date,
      String currency,
      BigDecimal dailyBudget,
      BigDecimal carryOver,
      BigDecimal spent,
      Map<ExpenseCategory, BigDecimal> categoryTotals,
      BigDecimal projectedCost,
      BigDecimal totalBudget) {
    this.date = date;
    this.currency = currency;
    this.dailyBudget = dailyBudget;
    this.carryOver = carryOver;
    this.spent = spent;
    final Map<ExpenseCategory, BigDecimal> categories = new EnumMap<>(ExpenseCategory.class);
    categories.putAll(categoryTotals);
    this.categoryTotals = Collections.unmodifiableMap(categories);
    this.projectedCost = projectedCost;
    this.totalBudget = totalBudget;
  }

  /**
   * Returns the day covered by the summary.
   *
   * @return the day covered by the summary
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Returns the trip's home currency code.
   *
   * @return the trip's home currency code
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * Returns the shared budget per day.
   *
   * @return the shared budget per day
   */
  public BigDecimal getDailyBudget() {
    return dailyBudget;
  }

  /**
   * Returns the balance from earlier days, negative if overspent.
   *
   * @return the balance from earlier days, negative if overspent
   */
  public BigDecimal getCarryOver() {
    return carryOver;
  }

  /**
   * Returns the daily budget adjusted by the balance from earlier days.
   *
   * @return the daily budget adjusted by the balance from earlier days
   */
  public BigDecimal getAllowance() {
    return dailyBudget.add(carryOver);
  }

  /**
   * Returns the amount spent on the selected day.
   *
   * @return the amount spent on the selected day
   */
  public BigDecimal getSpent() {
    return spent;
  }

  /**
   * Returns the allowance minus spending, negative if overspent.
   *
   * @return the allowance minus spending, negative if overspent
   */
  public BigDecimal getRemaining() {
    return getAllowance().subtract(spent);
  }

  /**
   * Returns the amount over the day's allowance, or zero if within it.
   *
   * @return the amount over the day's allowance, or zero if within it
   */
  public BigDecimal getDayOverage() {
    return getRemaining().negate().max(BigDecimal.ZERO);
  }

  /**
   * Returns a read-only map of spending by category for the selected day.
   *
   * @return a read-only map of spending by category for the selected day
   */
  public Map<ExpenseCategory, BigDecimal> getCategoryTotals() {
    return categoryTotals;
  }

  /**
   * Returns the trip cost projected from spending through the selected day.
   *
   * @return the trip cost projected from spending through the selected day
   */
  public BigDecimal getProjectedCost() {
    return projectedCost;
  }

  /**
   * Returns the budget for the full trip.
   *
   * @return the budget for the full trip
   */
  public BigDecimal getTotalBudget() {
    return totalBudget;
  }

  /**
   * Returns the projected amount over the trip budget, or zero.
   *
   * @return the projected amount over the trip budget, or zero
   */
  public BigDecimal getProjectedOverage() {
    return projectedCost.subtract(totalBudget).max(BigDecimal.ZERO);
  }
}
