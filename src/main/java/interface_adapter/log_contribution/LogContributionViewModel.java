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

    public void addListener(LogContributionViewModelListener listener){
        listeners.add(listener);
    }
    public String getCurrency() {
        return trip.getHomeCurrency();
    }

}
