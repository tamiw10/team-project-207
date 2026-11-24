package interface_adapter.log_contribution;

import entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class LogContributionViewModel {

    private final Trip trip;

    // might need to change this down the line once branch is merged with others
    public LogContributionViewModel(Trip trip) {
        this.trip = trip;
    }
    private final List<LogContributionViewModelListener> listeners = new ArrayList<>();
    private LogContributionState state = new LogContributionState();

    public String getCurrency() { return trip.getHomeCurrency(); }

    public LogContributionState getState() { return state; }

    public void setState(LogContributionState state) { this.state = state; }

    public void addListener(LogContributionViewModelListener listener){
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (LogContributionViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }


}
