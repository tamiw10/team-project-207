package view;

import interface_adapter.create_trip.CreateTripController;
import interface_adapter.trip.TripState;
import interface_adapter.trip.TripViewModel;
import interface_adapter.trip.TripViewModelListener;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The View for an open trip: its name, summary and members. Created by the Create Trip Use Case.
 * The other use cases add their buttons (savings, expenses, daily budget, activities) here.
 */
public class TripView extends JPanel implements TripViewModelListener {

  private final TripViewModel viewModel;

  private final JLabel title = new JLabel();
  private final JLabel details = new JLabel();
  private final JPanel members = new JPanel();
  private CreateTripController createTripController;

  /**
   * Builds the trip screen.
   *
   * @param viewModel the state of the open trip
   */
  public TripView(TripViewModel viewModel) {
    this.viewModel = viewModel;
    viewModel.addListener(this);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    details.setAlignmentX(Component.CENTER_ALIGNMENT);
    members.setLayout(new BoxLayout(members, BoxLayout.Y_AXIS));

    final JLabel membersLabel = new JLabel(TripViewModel.MEMBERS_LABEL);
    membersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    final JButton back = new JButton(TripViewModel.BACK_BUTTON_LABEL);
    back.setAlignmentX(Component.CENTER_ALIGNMENT);
    back.addActionListener(evt -> createTripController.switchToHomeView());

    add(title);
    add(details);
    add(membersLabel);
    add(members);
    add(back);
  }

  /** The open trip changed: show its name, summary and members. */
  @Override
  public void stateChanged() {
    final TripState state = viewModel.getState();
    title.setText(state.getTitle());
    details.setText(state.getDetails());
    members.removeAll();
    final String creator = state.getMembers().isEmpty() ? "" : state.getMembers().get(0);
    for (String member : state.getMembers()) {
      final JLabel label = new JLabel(member.equals(creator) ? member + " (creator)" : member);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      members.add(label);
    }
    members.revalidate();
    members.repaint();
  }

  public void setCreateTripController(CreateTripController controller) {
    this.createTripController = controller;
  }
}
