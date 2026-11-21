package view;

import interface_adapter.log_contribution.LogContributionViewModel;
import interface_adapter.log_contribution.LogContributionViewModelListener;

import javax.swing.*;
import java.awt.event.ActionListener;

/** View when a user is logging a contribution (before trip) */
public class LogContributionView extends JPanel implements LogContributionViewModelListener {
    // add implements ActionListener, LogContributionViewModelListener in class definition
    // add log_contribution package to interface_adapter package
        // add LogContributionViewModelListener interface to that package

    // define variables
    private final LogContributionViewModel logContributionViewModel;

    // placeholder variable for what currency the user wants their goal to be in
    // I think this is set in the trip making use case and is typed in HomeCurrency text field (tamima's use case)
    public String trip_goal_currency;

    // create labels
    // title of frame will be the name of trip (pull from trip) -> or in Contributor classs?

    // title of page
    private final JLabel LogSCLabel = new JLabel("Log a saving contribution" + trip_goal_currency);
    private final JLabel AmountLabel = new JLabel("Amount");
    private final JLabel DateLabel = new JLabel("Date");
    private final JLabel MyGoalLabel = new JLabel("My Goal");

    // create text fields
    private final JTextField AmountField = new JTextField(10);
    private final JTextField DateField = new JTextField(10);
    private final JTextField MyGoalField = new JTextField(10);

    // create buttons
    private final JButton LogCButton = new JButton("Log Contribution");

    // create LabelTextPanels (panels containing labels and text) -> does this need to be defined in a method?
    final LabelTextPanel AmountPanel = new LabelTextPanel(AmountLabel, AmountField);
    final LabelTextPanel DatePanel = new LabelTextPanel(DateLabel, DateField);
    final LabelTextPanel MyGoalPanel = new LabelTextPanel(MyGoalLabel, MyGoalField);

    // constructor (parameter will be LogContributionViewModel)
    public LogContributionView(LogContributionViewModel logContributionViewModel){

        this.logContributionViewModel = logContributionViewModel;
    }

    // action listener methods
    // pressing LogCButton


}
