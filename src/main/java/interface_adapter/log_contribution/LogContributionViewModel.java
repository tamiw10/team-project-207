package interface_adapter.log_contribution;

import java.util.ArrayList;
import java.util.List;

public class LogContributionViewModel {

    public static String VIEW_NAME = "log a contribution";

    private final List<LogContributionViewModelListener> listeners = new ArrayList<>();
    private LogContributionState state = new LogContributionState();

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
