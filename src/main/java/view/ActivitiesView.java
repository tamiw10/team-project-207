package view;

import interface_adapter.suggest_idea.SuggestIdeaController;
import interface_adapter.vote.ActivitiesState;
import interface_adapter.vote.ActivitiesViewModel;
import interface_adapter.vote.ActivitiesViewModelListener;
import interface_adapter.vote.VoteController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import use_case.vote.RankedIdea;

/**
 * The View for voting on a day's activity ideas: pick a day, see its ideas ranked by votes, and
 * click Vote or Unvote on an idea. Each member has a few votes per day.
 */
public class ActivitiesView extends JPanel implements ActivitiesViewModelListener {

  private static final Color SUCCESS_GREEN = new Color(0, 110, 40);
  private static final Color ERROR_RED = new Color(200, 30, 30);
  private static final int GAP = 8;
  private static final int ROW_HEIGHT = 56;
  private static final int LIST_WIDTH = 520;

  private final ActivitiesViewModel activitiesViewModel;
  private VoteController voteController;
  private SuggestIdeaController suggestIdeaController;
  private Runnable backAction = () -> {};

  private final JLabel title = new JLabel("Activities");
  private final JComboBox<LocalDate> dayPicker = new JComboBox<>();
  private final JLabel votesLeft = new JLabel();
  private final JLabel status = new JLabel(" ");
  private final JPanel ideaList = new JPanel();
  private final JButton back = new JButton("Back");
  private final JTextField newTitle = new JTextField(14);
  private final JTextField newCost = new JTextField(6);
  private final JLabel costLabel = new JLabel("Cost");
  private final JButton addIdea = new JButton("Add idea");

  /** True while the view fills the day picker, so that doing so does not count as a user pick. */
  private boolean updating;

  public ActivitiesView(ActivitiesViewModel activitiesViewModel) {
    this.activitiesViewModel = activitiesViewModel;
    this.activitiesViewModel.addListener(this);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(GAP * 2, GAP * 2, GAP * 2, GAP * 2));

    final JLabel heading = new JLabel("Vote on activities");
    heading.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JPanel dayRow = new JPanel(new BorderLayout());
    final JPanel dayChooser = new JPanel(new FlowLayout(FlowLayout.LEFT));
    dayChooser.add(new JLabel("Day"));
    dayChooser.add(dayPicker);
    dayRow.add(dayChooser, BorderLayout.WEST);
    dayRow.add(votesLeft, BorderLayout.EAST);
    dayRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    dayRow.setMaximumSize(new Dimension(LIST_WIDTH, dayRow.getPreferredSize().height));

    status.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JLabel rankedLabel = new JLabel("Ranked by votes");
    rankedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    ideaList.setLayout(new BoxLayout(ideaList, BoxLayout.Y_AXIS));
    ideaList.setAlignmentX(Component.LEFT_ALIGNMENT);

    back.setAlignmentX(Component.LEFT_ALIGNMENT);
    back.addActionListener(evt -> backAction.run());

    dayPicker.addActionListener(
        evt -> {
          if (!updating && dayPicker.getSelectedItem() != null) {
            final ActivitiesState state = activitiesViewModel.getState();
            voteController.showDay(
                state.getTripId(), (LocalDate) dayPicker.getSelectedItem(), state.getUsername());
          }
        });

    add(title);
    add(Box.createVerticalStrut(GAP));
    add(heading);
    add(dayRow);
    add(status);
    add(Box.createVerticalStrut(GAP));
    add(rankedLabel);
    add(ideaList);
    add(Box.createVerticalStrut(GAP));
    add(buildSuggestPanel());
    add(Box.createVerticalStrut(GAP));
    add(back);
  }

  private JPanel buildSuggestPanel() {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP / 2, 0));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(LIST_WIDTH, ROW_HEIGHT));
    panel.add(new JLabel("Suggest an idea:"));
    panel.add(newTitle);
    panel.add(costLabel);
    panel.add(newCost);
    panel.add(addIdea);
    addIdea.addActionListener(
        evt -> {
          final ActivitiesState state = activitiesViewModel.getState();
          suggestIdeaController.execute(
              state.getTripId(),
              (LocalDate) dayPicker.getSelectedItem(),
              newTitle.getText(),
              newCost.getText(),
              state.getUsername());
          // The use case runs right away, so the state already says whether it worked.
          if (activitiesViewModel.getState().getError().isEmpty()) {
            newTitle.setText("");
            newCost.setText("");
          }
        });
    return panel;
  }

  public void setVoteController(VoteController voteController) {
    this.voteController = voteController;
  }

  public void setSuggestIdeaController(SuggestIdeaController suggestIdeaController) {
    this.suggestIdeaController = suggestIdeaController;
  }

  /**
   * Sets what the Back button does.
   *
   * @param backAction runs when the user clicks Back
   */
  public void setBackAction(Runnable backAction) {
    this.backAction = backAction;
  }

  @Override
  public void stateChanged() {
    final ActivitiesState state = activitiesViewModel.getState();
    title.setText("Activities - " + state.getTripName());
    costLabel.setText("Cost (" + state.getHomeCurrency() + ")");
    votesLeft.setText(
        "My votes left today: " + state.getVotesLeft() + " of " + state.getVotesPerDay());

    updating = true;
    dayPicker.removeAllItems();
    for (LocalDate tripDay : state.getTripDays()) {
      dayPicker.addItem(tripDay);
    }
    dayPicker.setSelectedItem(state.getDay());
    updating = false;

    if (!state.getError().isEmpty()) {
      status.setForeground(ERROR_RED);
      status.setText(state.getError());
    } else if (!state.getMessage().isEmpty()) {
      status.setForeground(SUCCESS_GREEN);
      status.setText(state.getMessage());
    } else {
      status.setText(" ");
    }

    ideaList.removeAll();
    int rank = 1;
    for (RankedIdea idea : state.getRankedIdeas()) {
      ideaList.add(buildRow(rank, idea, state));
      ideaList.add(Box.createVerticalStrut(GAP / 2));
      rank++;
    }
    ideaList.revalidate();
    ideaList.repaint();
  }

  private JPanel buildRow(int rank, RankedIdea idea, ActivitiesState state) {
    final JPanel row = new JPanel(new BorderLayout(GAP, 0));
    row.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(GAP / 2, GAP, GAP / 2, GAP)));
    row.setAlignmentX(Component.LEFT_ALIGNMENT);
    row.setMaximumSize(new Dimension(LIST_WIDTH, ROW_HEIGHT));
    row.setPreferredSize(new Dimension(LIST_WIDTH, ROW_HEIGHT));

    final JPanel text = new JPanel();
    text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
    text.add(new JLabel(rank + ". " + idea.getTitle()));
    text.add(
        new JLabel(String.format("%.2f %s", idea.getEstimatedCost(), state.getHomeCurrency())));

    final int count = idea.getVoteCount();
    final JLabel votes = new JLabel(count + (count == 1 ? " vote" : " votes"));

    final JButton button = new JButton(idea.isVotedByUser() ? "Unvote" : "Vote");
    button.addActionListener(
        evt ->
            voteController.vote(
                state.getTripId(), state.getDay(), idea.getId(), state.getUsername()));

    final JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, GAP, 0));
    right.add(votes);
    right.add(button);

    row.add(text, BorderLayout.CENTER);
    row.add(right, BorderLayout.EAST);
    return row;
  }
}
