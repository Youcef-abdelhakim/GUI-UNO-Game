package components;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class TimeBot extends Timer {
    
    public TimeBot(int delay, ActionListener listener) {
        super(delay, listener);
        initialize();
    }
    
    private void initialize() {
        // Custom initialization
        this.setRepeats(false); // Default to single execution
        this.setCoalesce(true); // Combine multiple pending events
    }
    
    // Fluent API methods
    public TimeBot withDelay(int delay) {
        this.setDelay(delay);
        return this;
    }
    
    public TimeBot withRepeats(boolean repeats) {
        this.setRepeats(repeats);
        return this;
    }
    
    public TimeBot withAction(ActionListener listener) {
        for (ActionListener existing : this.getActionListeners()) {
            this.removeActionListener(existing);
        }
        this.addActionListener(listener);
        return this;
    }
    
    // Convenience method for common bot timer usage
    public static TimeBot createBotTimer(int delay, Runnable action) {
        return new TimeBot(delay, e -> action.run())
            .withRepeats(false);
    }
}