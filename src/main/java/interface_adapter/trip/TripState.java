package interface_adapter.trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The state of the trip screen: the trip that is open, ready to display. */
public class TripState {

  private String tripId = "";
  private String title = "";
  private String details = "";
  private List<String> members = new ArrayList<>();

  public String getTripId() {
    return tripId;
  }

  public void setTripId(String tripId) {
    this.tripId = tripId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the one-line summary under the title, for example the destination, dates, currency and
   * daily budget.
   *
   * @return the summary line
   */
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public List<String> getMembers() {
    return Collections.unmodifiableList(members);
  }

  public void setMembers(List<String> members) {
    this.members = new ArrayList<>(members);
  }
}
