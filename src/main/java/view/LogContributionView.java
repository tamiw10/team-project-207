package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.log_contribution.LogContributionController;
import interface_adapter.log_contribution.LogContributionState;
import interface_adapter.log_contribution.LogContributionViewModel;
import interface_adapter.log_contribution.LogContributionViewModelListener;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;

/** View for Use Case 2: Log a Contribution */
public class LogContributionView extends JPanel implements LogContributionViewModelListener {

    // Setup
    private static final Color SUCCESS_COLOR = new Color(27, 127, 55);
    private static final Color ERROR_COLOR = new Color(198, 40, 40);
    private static final int PADDING = 16;
    private static final int GRID_GAP = 10;

    // variables
    private final LogContributionViewModel logContributionViewModel;
    private LogContributionController logContributionController;
    private ViewManagerModel viewManagerModel;

    private final JLabel headerLabel;
    private final JLabel statusLabel = new JLabel(" ");


    // text fields
    private final JTextField amountField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);
    private final JTextField myGoalField = new JTextField(10);
    private final JPanel myGoalPanel = labeledField("My goal", myGoalField);

    // button
    private final JButton logCButton = new JButton("Log Contribution");
    private final JButton backButton = new JButton("Back");

    // Labels
    private final JLabel myGoalDisplayLabel = new JLabel(); // displays goal budget after it is logged
    private final JLabel contributionLoggedLabel = new JLabel(); // confirmation message displayed after contribution is logged
    private final JLabel savingsProgressLabel = new JLabel(); // displays how much has been saved towards the goal
    private final JLabel weeksRemainingLabel = new JLabel(); // displays weeks remaining until the trip
    private final JLabel weeklyRequiredLabel = new JLabel(); // displays min amount user needs to save/week to meet their goal
    private final JLabel weeklyPaceLabel = new JLabel(); // displays how much the user has saved per week on avg
    private final JLabel onTrackLabel = new JLabel();
    private final JLabel groupProgressLabel = new JLabel(); // displays the total the group has saved towards the goal

    // progress bars
    private final JProgressBar personalProgressBar = new JProgressBar(0, 100);
    private final JProgressBar groupProgressBar = new JProgressBar(0, 100);

    // constructor
    public LogContributionView(LogContributionViewModel logContributionViewModel) {

        this.logContributionViewModel = logContributionViewModel;
        this.logContributionViewModel.addListener(this);

        LogContributionState state = logContributionViewModel.getState();
        headerLabel = new JLabel("Log Savings Contribution (in " + state.getHomeCurrency() + ")");

        // Add text to progress bars
        personalProgressBar.setStringPainted(true);
        groupProgressBar.setStringPainted(true);

        // layout
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        final JPanel topBar = new JPanel(new BorderLayout());
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 15f));
        topBar.add(headerLabel, BorderLayout.WEST);
        topBar.add(backButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        final JLabel sectionTitle = new JLabel("Log a savings contribution");
        sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 16f));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(10));

        final JPanel formGrid = new JPanel(new GridLayout(1, 3, GRID_GAP, GRID_GAP));
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        formGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        formGrid.add(labeledField("Amount", amountField));
        formGrid.add(labeledField("Date (YYYY-MM-DD)", dateField));
        formGrid.add(myGoalPanel);
        content.add(formGrid);
        content.add(Box.createVerticalStrut(12));

        final JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.add(logCButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(statusLabel);
        content.add(actionRow);
        content.add(Box.createVerticalStrut(14));

        final JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        content.add(separator);
        content.add(Box.createVerticalStrut(10));

        // Set subtitle fonts
        contributionLoggedLabel.setFont(contributionLoggedLabel.getFont().deriveFont(Font.BOLD, 16f));
        onTrackLabel.setFont(onTrackLabel.getFont().deriveFont(Font.BOLD, 16f));

        // Add components
        content.add(contributionLoggedLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(myGoalDisplayLabel);
        content.add(savingsProgressLabel);
        content.add(personalProgressBar);
        content.add(weeksRemainingLabel);
        content.add(weeklyRequiredLabel);
        content.add(weeklyPaceLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(onTrackLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(groupProgressLabel);
        content.add(groupProgressBar);

        add(content, BorderLayout.CENTER);

        // 2 cases; goal has already been logged, goal is being logged for first time
        logCButton.addActionListener(evt -> {
            if (logContributionController != null) {
                LogContributionState state2 = logContributionViewModel.getState();
                logContributionController.execute(
                        state2.getTripID(),
                        amountField.getText(),
                        dateField.getText(),
                        myGoalField.getText()
                );
            }
        });

        backButton.addActionListener(evt -> {
            if (viewManagerModel != null) {
                viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
                viewManagerModel.notifyViewChanged();
            }
        });
        updateView();

    }

    private static JPanel labeledField(String labelText, Component field) {
        final JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(new JLabel(labelText), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    public void setLogContributionController(LogContributionController logContributionController) {
        this.logContributionController = logContributionController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel){
        this.viewManagerModel = viewManagerModel;
    }


    @Override
    public void stateChanged() {
        updateView();
    }

    // after first contribution and goal is logged
    public void updateView() {
        LogContributionState state = logContributionViewModel.getState();

        myGoalPanel.setVisible(!state.isHasSavingsGoal());
        myGoalDisplayLabel.setVisible(state.isHasSavingsGoal());

        statusLabel.setText(state.getStatusMessage());

        myGoalDisplayLabel.setText("My goal: " + state.getSavingsGoal());

        contributionLoggedLabel.setText("Contributions Logged");

        savingsProgressLabel.setText("Saved " + state.getTotalSaved() + " of " + state.getSavingsGoal());

        weeksRemainingLabel.setText("weeks until the trip starts: " + state.getWeeksRemaining());

        onTrackLabel.setText(state.isOnTrack() ? "On track" : "Behind: save more each week to reach your goal");

        groupProgressLabel.setText("Saved " + state.getGroupSaved() + " of " + state.getGroupGoal());

        personalProgressBar.setValue((int) (Math.min(state.getUserProgress(), 1.0) * 100));

        groupProgressBar.setValue((int) (Math.min(state.getGroupProgress(), 1.0) * 100));

        if (!state.isUserGoalReached()) {
            weeklyRequiredLabel.setText("You need to save " + state.getWeeklyRequired() + " per week from now");
            weeklyPaceLabel.setText("You have been saving " + state.getWeeklyPace() + " per week so far");
        }

        // Clear fields only when requested by the presenter
        if (state.isClearForm()) {
            amountField.setText("");
            dateField.setText("");
            myGoalField.setText("");
        }

        // Show error or success message
        statusLabel.setForeground(
                state.isError() ? ERROR_COLOR : SUCCESS_COLOR
        );

        // Hide weekly information when the goal is reached
        weeklyRequiredLabel.setVisible(!state.isUserGoalReached());
        weeklyPaceLabel.setVisible(!state.isUserGoalReached());

        revalidate();
        repaint();
    }
}
