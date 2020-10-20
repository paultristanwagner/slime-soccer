package me.paultristanwagner.slimesoccer.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSpecificCoolDown {
    
    private final long milliseconds;
    
    private final Map<UUID, CoolDown> coolDownMap = new HashMap<>();
    
    public PlayerSpecificCoolDown( long milliseconds ) {
        this.milliseconds = milliseconds;
    }
    
    /**
     * Tests if the cooldown is running.
     *
     * @return true if the cooldown is running
     */
    public boolean isRunning( Player player ) {
        CoolDown coolDown = coolDownMap.get( player.getUniqueId() );
        if ( coolDown == null ) {
            return false;
        } else {
            return coolDown.isRunning();
        }
    }
    
    /**
     * Tests if the cooldown is running. If it is not it resets the cooldown.
     *
     * @return true if the cooldown is running
     */
    public boolean isRunningOrReset( Player player ) {
        UUID uuid = player.getUniqueId();
        CoolDown coolDown = coolDownMap.getOrDefault( uuid, new CoolDown( milliseconds ) );
        coolDownMap.put( uuid, coolDown );
        return coolDown.isRunningOrReset();
    }
}
