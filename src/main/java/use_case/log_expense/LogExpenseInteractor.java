package use_case.log_expense;

import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * Interactor for Use Case 3: Log an expense. Validates expense inputs against the open {@link Trip},
 * converts foreign currencies via {@link ExchangeRateDataAccessInterface}, and saves the resulting
 * {@link Expense} through {@link LogExpenseDataAccessInterface}.
 */
public class LogExpenseInteractor implements LogExpenseInputBoundary {

  private static final double CENTS_PER_UNIT = 100.0;

  private final LogExpenseDataAccessInterface tripDataAccess;
  private final ExchangeRateDataAccessInterface exchangeRateDataAccess;
  private final LogExpenseOutputBoundary presenter;

  /**
   * Creates a {@link LogExpenseInteractor}.
   *
   * @param tripDataAccess data access for trips and expenses
   * @param exchangeRateDataAccess gateway for current exchange rates
   * @param presenter output boundary for presenting success or failure
   */
  public LogExpenseInteractor(
          LogExpenseDataAccessInterface tripDataAccess,
          ExchangeRateDataAccessInterface exchangeRateDataAccess,
          LogExpenseOutputBoundary presenter) {
    this.tripDataAccess = tripDataAccess;
    this.exchangeRateDataAccess = exchangeRateDataAccess;
    this.presenter = presenter;
  }

  /**
   * Executes Use Case 3: validates description, positive numeric amount, trip date range, payer
   * membership, and currency; converts to the trip's home currency if needed; and saves the
   * expense.
   *
   * @param inputData the raw expense fields submitted by the user
   */
  @Override
  public void execute(LogExpenseInputData inputData) {
    final Trip trip = findOpenTrip(inputData.getTripId());
    if (trip == null) {
      presenter.prepareFailView("No trip is currently open.");
      return;
    }

    if (isBlank(inputData.getDescription())) {
      presenter.prepareFailView("Description cannot be empty.");
      return;
    }

    final Double amount = parseAmount(inputData.getAmountText());
    final LocalDate date = amount == null ? null : parseDate(inputData.getDateText(), trip);
    final String payer = date == null ? null : checkPayer(inputData.getPayerUsername(), trip);
    final String currency = payer == null ? null : checkCurrency(inputData.getCurrency());
    if (currency == null) {
      return;
    }

    final Double amountInHomeCurrency =
            convertToHomeCurrency(amount, currency, trip.getHomeCurrency());
    if (amountInHomeCurrency == null) {
      return;
    }

    final ExpenseCategory category =
            inputData.getCategory() == null ? ExpenseCategory.OTHER : inputData.getCategory();
    final Expense expense =
            new Expense(
                    UUID.randomUUID().toString(),
                    trip.getId(),
                    payer,
                    inputData.getDescription().trim(),
                    amount,
                    currency,
                    amountInHomeCurrency,
                    category,
                    date);
    tripDataAccess.saveExpense(expense);

    final List<Expense> expenses = tripDataAccess.getExpenses(trip.getId());
    presenter.prepareSuccessView(
            new LogExpenseOutputData(trip.getId(), trip.getName(), trip.getHomeCurrency(), expenses));
  }

  private Trip findOpenTrip(String requestedTripId) {
    final String tripId =
            isBlank(requestedTripId) ? tripDataAccess.getCurrentTripId() : requestedTripId.trim();
    return isBlank(tripId) ? null : tripDataAccess.getTrip(tripId);
  }

  private Double parseAmount(String amountText) {
    if (isBlank(amountText)) {
      presenter.prepareFailView("Amount must be a number.");
      return null;
    }
    final double amount;
    try {
      amount = Double.parseDouble(amountText.trim());
    } catch (NumberFormatException ex) {
      presenter.prepareFailView("Amount must be a number.");
      return null;
    }
    if (!Double.isFinite(amount) || amount <= 0) {
      presenter.prepareFailView("Amount must be greater than 0.");
      return null;
    }
    return amount;
  }

  private LocalDate parseDate(String dateText, Trip trip) {
    if (isBlank(dateText)) {
      presenter.prepareFailView("Date must be in YYYY-MM-DD format.");
      return null;
    }
    final LocalDate date;
    try {
      date = LocalDate.parse(dateText.trim());
    } catch (DateTimeParseException ex) {
      presenter.prepareFailView("Date must be in YYYY-MM-DD format.");
      return null;
    }
    if (!trip.containsDate(date)) {
      presenter.prepareFailView("That date is not part of this trip.");
      return null;
    }
    return date;
  }

  private String checkPayer(String payerUsername, Trip trip) {
    final String payer = payerUsername == null ? "" : payerUsername.trim();
    if (payer.isEmpty()) {
      presenter.prepareFailView("Paid by cannot be empty.");
      return null;
    }
    if (!trip.hasMember(payer)) {
      presenter.prepareFailView(payer + " is not a member of this trip.");
      return null;
    }
    return payer;
  }

  private String checkCurrency(String rawCurrency) {
    if (isBlank(rawCurrency)) {
      presenter.prepareFailView("Currency cannot be empty.");
      return null;
    }
    final String currency = rawCurrency.trim().toUpperCase();
    try {
      Currency.getInstance(currency);
      return currency;
    } catch (IllegalArgumentException ex) {
      presenter.prepareFailView("Currency " + currency + " is not supported.");
      return null;
    }
  }

  private Double convertToHomeCurrency(double amount, String currency, String homeCurrency) {
    final double converted;
    if (currency.equals(homeCurrency)) {
      converted = roundToCent(amount);
    } else {
      final double rate;
      try {
        rate = exchangeRateDataAccess.getRate(currency, homeCurrency);
      } catch (ExchangeRateUnavailableException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("not supported")) {
          presenter.prepareFailView("Currency " + currency + " is not supported.");
        } else {
          presenter.prepareFailView("Couldn't get the exchange rate. Try again.");
        }
        return null;
      }
      converted = roundToCent(amount * rate);
    }
    if (converted <= 0) {
      presenter.prepareFailView("Converted amount must be greater than 0.");
      return null;
    }
    return converted;
  }

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  private static double roundToCent(double value) {
    return Math.round(value * CENTS_PER_UNIT) / CENTS_PER_UNIT;
  }
}