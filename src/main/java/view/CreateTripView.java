package view;

import interface_adapter.create_trip.CreateTripController;
import interface_adapter.create_trip.CreateTripState;
import interface_adapter.create_trip.CreateTripViewModel;
import interface_adapter.create_trip.CreateTripViewModelListener;
import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** The View for the Create Trip Use Case: a form for the details of a new trip. */
public class CreateTripView extends JPanel implements CreateTripViewModelListener {

  private static final int FIELD_WIDTH = 15;
  private static final int WIDE_FIELD_WIDTH = 30;

  private final CreateTripViewModel viewModel;
  private CreateTripController controller;

  private final JTextField nameField = new JTextField(FIELD_WIDTH);
  private final JTextField destinationField = new JTextField(FIELD_WIDTH);
  private final JTextField startDateField = new JTextField(FIELD_WIDTH);
  private final JTextField endDateField = new JTextField(FIELD_WIDTH);
  private final JTextField homeCurrencyField = new JTextField(FIELD_WIDTH);
  private final JTextField dailyBudgetField = new JTextField(FIELD_WIDTH);
  private final JTextField friendsField = new JTextField(WIDE_FIELD_WIDTH);
  private final JLabel errorLabel = new JLabel();

  /**
   * Builds the form.
   *
   * @param viewModel the state of the form
   */
  public CreateTripView(CreateTripViewModel viewModel) {
    this.viewModel = viewModel;
    viewModel.addListener(this);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    final JLabel title = new JLabel(CreateTripViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(title);

    add(row(CreateTripViewModel.NAME_LABEL, nameField));
    add(row(CreateTripViewModel.DESTINATION_LABEL, destinationField));
    add(row(CreateTripViewModel.START_DATE_LABEL, startDateField));
    add(row(CreateTripViewModel.END_DATE_LABEL, endDateField));
    add(row(CreateTripViewModel.HOME_CURRENCY_LABEL, homeCurrencyField));
    add(row(CreateTripViewModel.DAILY_BUDGET_LABEL, dailyBudgetField));
    add(row(CreateTripViewModel.FRIENDS_LABEL, friendsField));

    errorLabel.setForeground(Color.RED);
    errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(errorLabel);

    add(buttons());
    listenToFields();
  }

  private static LabelTextPanel row(String label, JTextField field) {
    return new LabelTextPanel(new JLabel(label), field);
  }

  private JPanel buttons() {
    final JPanel buttons = new JPanel();

    final JButton create = new JButton(CreateTripViewModel.CREATE_BUTTON_LABEL);
    create.addActionListener(
        evt -> {
          final CreateTripState state = viewModel.getState();
          controller.execute(
              state.getName(),
              state.getDestination(),
              state.getStartDate(),
              state.getEndDate(),
              state.getHomeCurrency(),
              state.getDailyBudget(),
              state.getFriends());
        });
    buttons.add(create);

    final JButton cancel = new JButton(CreateTripViewModel.CANCEL_BUTTON_LABEL);
    cancel.addActionListener(evt -> controller.switchToHomeView());
    buttons.add(cancel);

    return buttons;
  }

  /** Copies what the user types into the view model's state. */
  private void listenToFields() {
    onChange(nameField, text -> viewModel.getState().setName(text));
    onChange(destinationField, text -> viewModel.getState().setDestination(text));
    onChange(startDateField, text -> viewModel.getState().setStartDate(text));
    onChange(endDateField, text -> viewModel.getState().setEndDate(text));
    onChange(homeCurrencyField, text -> viewModel.getState().setHomeCurrency(text));
    onChange(dailyBudgetField, text -> viewModel.getState().setDailyBudget(text));
    onChange(friendsField, text -> viewModel.getState().setFriends(text));
  }

  private static void onChange(JTextField field, Consumer<String> update) {
    field
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                update.accept(field.getText());
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                update.accept(field.getText());
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                update.accept(field.getText());
              }
            });
  }

  /** The state changed: show the latest error, and clear the fields when the form was reset. */
  @Override
  public void stateChanged() {
    final CreateTripState state = viewModel.getState();
    show(nameField, state.getName());
    show(destinationField, state.getDestination());
    show(startDateField, state.getStartDate());
    show(endDateField, state.getEndDate());
    show(homeCurrencyField, state.getHomeCurrency());
    show(dailyBudgetField, state.getDailyBudget());
    show(friendsField, state.getFriends());
    errorLabel.setText(state.getErrorMessage());
  }

  private static void show(JTextField field, String text) {
    if (!field.getText().equals(text)) {
      field.setText(text);
    }
  }

  public void setCreateTripController(CreateTripController controller) {
    this.controller = controller;
  }
}
