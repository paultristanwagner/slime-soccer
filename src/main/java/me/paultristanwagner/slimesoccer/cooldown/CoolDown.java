package me.paultristanwagner.slimesoccer.cooldown;

public class CoolDown {
    
    private final long milliseconds;
    private long lastActivated;
    
    public CoolDown( long milliseconds ) {
        this.milliseconds = milliseconds;
    }
    
    /**
     * Tests if the cooldown is running
     * @return true if the cooldown is running
     */
    public boolean isRunning() {
        return System.currentTimeMillis() - lastActivated < milliseconds;
    }
    
    /**
     * Tests if the cooldown is running. If it is not it resets the cooldown.
     * @return true if the cooldown is running
     */
    public boolean isRunningOrReset() {
        boolean running = isRunning();
        if ( !running ) {
            lastActivated = System.currentTimeMillis();
        }
        return running;
    }
}
