package view;

import interface_adapter.log_contribution.LogContributionViewModel;
import interface_adapter.log_contribution.LogContributionViewModelListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** View when a user is logging a contribution (before trip) */
public class LogContributionView extends JPanel implements LogContributionViewModelListener {
    // add implements ActionListener, LogContributionViewModelListener in class definition
    // add log_contribution package to interface_adapter package
        // add LogContributionViewModelListener interface to that package

    // define variables
    private final LogContributionViewModel logContributionViewModel;

    // create labels
    // title of frame will be the name of trip (pull from trip) -> or in Contributor classs?

    private final JLabel myGoalDisplayLabel = new JLabel();
    private final JLabel confirmationLabel = new JLabel();
    private final JLabel savingsProgressLabel = new JLabel();
    private final JLabel weeksRemainingLabel = new JLabel();
    private final JLabel weeklyRequiredLabel = new JLabel();
    private final JLabel weeklyPaceLabel = new JLabel();
    private final JLabel onTrackLabel = new JLabel();
    private final JLabel groupProgressLabel = new JLabel();

    // title of page
    private final JLabel LogSCLabel;
    private final JLabel AmountLabel = new JLabel("Amount");
    private final JLabel DateLabel = new JLabel("Date");
    private final JLabel MyGoalLabel = new JLabel("My Goal");

    // text fields
    private final JTextField AmountField = new JTextField(10);
    private final JTextField DateField = new JTextField(10);
    private final JTextField MyGoalField = new JTextField(10);

    // button
    private final JButton LogCButton = new JButton("Log Contribution");

    // progress bars
    private final JProgressBar personalProgressBar = new JProgressBar(0, 100);
    private final JProgressBar groupProgressBar = new JProgressBar(0, 100);

    // constructor
    public LogContributionView(LogContributionViewModel logContributionViewModel){

        this.logContributionViewModel = logContributionViewModel;
        this.logContributionViewModel.addListener(this);
        // relies on view model to have trip object stored
        this.LogSCLabel = new JLabel("Log a saving contribution" + logContributionViewModel.getCurrency());

        // create LabelTextPanels (panels containing labels and text)
        final LabelTextPanel AmountPanel = new LabelTextPanel(AmountLabel, AmountField);
        final LabelTextPanel DatePanel = new LabelTextPanel(DateLabel, DateField);
        final LabelTextPanel MyGoalPanel = new LabelTextPanel(MyGoalLabel, MyGoalField);

        // set layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add components
        this.add(LogSCLabel);
        this.add(AmountPanel);
        this.add(DatePanel);
        this.add(MyGoalPanel);
        this.add(LogCButton);

        LogCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amount = AmountField.getText();
                String date = DateField.getText();
                String myGoal = MyGoalField.getText();
                JOptionPane.showMessageDialog(null, "Contribution Logged");

            }
        });

    }

    // after first contribution and goal is logged
    public void updateView(){
        // Clear the amount and date fields
        AmountField.setText("");
        DateField.setText("");

        // Hide the goal input field
        MyGoalField.setVisible(false);

        // Display the existing goal
        // myGoalDisplayLabel.setText("My goal: " + myGoal);

        // Display confirmation
        confirmationLabel.setText("Contribution logged!");

        // Update savings progress
        // Update weekly savings information
        // Update group progress
    }

}
