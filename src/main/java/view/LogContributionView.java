package view;

import interface_adapter.log_contribution.LogContributionController;
import interface_adapter.log_contribution.LogContributionState;
import interface_adapter.log_contribution.LogContributionViewModel;
import interface_adapter.log_contribution.LogContributionViewModelListener;

import javax.swing.*;

/** View for Use Case 2: Log a Contribution */
public class LogContributionView extends JPanel implements LogContributionViewModelListener {

    // variables
    private final LogContributionViewModel logContributionViewModel;
    private final LogContributionController logContributionController;

    // labels
    // title of frame will be the name of trip (pull from trip)?
    private final JLabel myGoalDisplayLabel = new JLabel(); // displays goal budget after it is logged
    private final JLabel confirmationLabel = new JLabel(); // confirmation message displayed after contribution is logged
    private final JLabel savingsProgressLabel = new JLabel(); // displays how much has been saved towards the goal
    private final JLabel weeksRemainingLabel = new JLabel(); // displays weeks remaining until the trip
    private final JLabel weeklyRequiredLabel = new JLabel(); // displays min amount user needs to save/week to meet their goal
    private final JLabel weeklyPaceLabel = new JLabel(); // displays how much the user has saved per week on avg
    private final JLabel onTrackLabel = new JLabel();
    private final JLabel groupProgressLabel = new JLabel(); // displays the total the group has saved towards the goal

    // title of page
    private final JLabel logSCLabel;
    private final JLabel amountLabel = new JLabel("Amount");
    private final JLabel dateLabel = new JLabel("Date");
    private final JLabel myGoalLabel = new JLabel("My Goal");

    // text fields
    private final JTextField amountField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);
    private final JTextField myGoalField = new JTextField(10);

    // button
    private final JButton logCButton = new JButton("Log Contribution");

    // progress bars
    private final JProgressBar personalProgressBar = new JProgressBar(0, 100);
    private final JProgressBar groupProgressBar = new JProgressBar(0, 100);

    // constructor
    public LogContributionView(LogContributionViewModel logContributionViewModel,
                               LogContributionController logContributionController) {

        this.logContributionViewModel = logContributionViewModel;
        this.logContributionController = logContributionController;
        this.logContributionViewModel.addListener(this);
        // relies on view model to have trip object stored
        this.logSCLabel = new JLabel("Log a saving contribution" + logContributionViewModel.getCurrency());

        // LabelTextPanels (panels containing labels and text)
        final LabelTextPanel amountPanel = new LabelTextPanel(amountLabel, amountField);
        final LabelTextPanel datePanel = new LabelTextPanel(dateLabel, dateField);
        final LabelTextPanel myGoalPanel = new LabelTextPanel(myGoalLabel, myGoalField);

        // layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add components
        this.add(logSCLabel);
        this.add(amountPanel);
        this.add(datePanel);
        this.add(myGoalPanel);
        this.add(logCButton);
        this.add(confirmationLabel);
        this.add(myGoalDisplayLabel);
        this.add(savingsProgressLabel);
        this.add(personalProgressBar);
        this.add(weeksRemainingLabel);
        this.add(weeklyRequiredLabel);
        this.add(weeklyPaceLabel);
        this.add(onTrackLabel);
        this.add(groupProgressLabel);
        this.add(groupProgressBar);

        // 2 cases; goal has already been logged, goal is being logged for first time
        logCButton.addActionListener(evt -> {
            String amountText = amountField.getText();
            String dateText = dateField.getText();
            String myGoalText = myGoalField.getText();
            String tripID = logContributionViewModel.getTripId();

            logContributionController.execute(tripID, amountText, dateText, myGoalText);
        });

        // Initialize the view using the current state
        updateView();

    }

    @Override
    public void stateChanged() {
        updateView();
    }

    // after first contribution and goal is logged
    public void updateView() {
        LogContributionState state = logContributionViewModel.getState();

        myGoalField.setVisible(!state.isHasSavingsGoal());
        myGoalLabel.setVisible(!state.isHasSavingsGoal());
        myGoalDisplayLabel.setVisible(state.isHasSavingsGoal());

        confirmationLabel.setText(state.getStatusMessage());

        myGoalDisplayLabel.setText("My goal: " + state.getSavingsGoal());

        savingsProgressLabel.setText(
                state.getTotalSaved() + " saved"
        );

        weeksRemainingLabel.setText(
                state.getWeeksRemaining() + " weeks remaining"
        );

        onTrackLabel.setText(
                state.isOnTrack() ? "On track" : "Not on track"
        );

        groupProgressLabel.setText(
                state.getGroupSaved() + " / " + state.getGroupGoal()
        );

        personalProgressBar.setValue(
                (int) (Math.min(state.getUserProgress(), 1.0) * 100)
        );

        groupProgressBar.setValue(
                (int) (Math.min(state.getGroupProgress(), 1.0) * 100)
        );

        weeklyRequiredLabel.setText(
                "Required per week: " + state.getWeeklyRequired()
        );

        weeklyPaceLabel.setText(
                "Current weekly pace: " + state.getWeeklyPace()
        );

        // Clear fields only when requested by the presenter
        if (state.isClearForm()) {
            amountField.setText("");
            dateField.setText("");
            myGoalField.setText("");
        }

        // Show error or success message
        confirmationLabel.setForeground(
                state.isError() ? java.awt.Color.RED : java.awt.Color.BLACK
        );

        // Hide weekly information when the goal is reached
        weeklyRequiredLabel.setVisible(!state.isUserGoalReached());
        weeklyPaceLabel.setVisible(!state.isUserGoalReached());
    }
}
