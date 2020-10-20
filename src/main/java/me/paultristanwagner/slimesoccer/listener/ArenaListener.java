package me.paultristanwagner.slimesoccer.listener;

import me.paultristanwagner.slimesoccer.arena.Arena;
import me.paultristanwagner.slimesoccer.cooldown.PlayerSpecificCoolDown;
import me.paultristanwagner.slimesoccer.game.Game;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ArenaListener implements Listener {
    
    private final Arena arena = Arena.getInstance();
    private final Game game = Game.getInstance();
    private final PlayerSpecificCoolDown coolDown = new PlayerSpecificCoolDown( 60 );
    
    private static final double KICK_DISTANCE_SQUARED = Math.pow( 1, 2 );
    
    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event ) {
        Player player = event.getPlayer();
        game.addPlayer( player );
        event.setJoinMessage( null );
    }
    
    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        Player player = event.getPlayer();
        game.removePlayer( player );
        event.setQuitMessage( null );
    }
    
    @EventHandler
    public void onEntityDamageByEntity( EntityDamageByEntityEvent event ) {
        EntityType damagerType = event.getDamager().getType();
        if ( damagerType.equals( EntityType.SLIME ) || damagerType.equals( EntityType.FIREWORK ) ) {
            event.setCancelled( true );
        } else {
            event.setDamage( 0 );
        }
    }
    
    @EventHandler
    public void onKickBall( PlayerMoveEvent event ) {
        if ( !arena.hasBall() ) {
            return;
        }
        
        Location to = event.getTo();
        Objects.requireNonNull( to );
        Entity ballEntity = arena.getBallEntity();
        Location ballLocation = ballEntity.getLocation();
        
        if ( to.distanceSquared( ballLocation ) > KICK_DISTANCE_SQUARED ) {
            return;
        }
        
        Vector playerVector = to.toVector();
        Vector ballVector = ballLocation.toVector();
        
        Vector direction = ballVector.clone().subtract( playerVector ).normalize().multiply( 2 ).setY( 0.65 );
        
        try {
            direction.checkFinite();
            ballEntity.setVelocity( direction );
            
            Player player = event.getPlayer();
            if ( coolDown.isRunningOrReset( player ) ) {
                return;
            }
            
            World world = to.getWorld();
            Objects.requireNonNull( world );
            
            world.playSound( to, Sound.ENTITY_ARROW_SHOOT, 1, 2 );
        } catch ( IllegalArgumentException ignored ) {
        }
    }
    
    @EventHandler
    public void onEntityDamage( EntityDamageEvent event ) {
        if ( event.getEntity().getType().equals( EntityType.SLIME ) &&
                event.getCause() == EntityDamageEvent.DamageCause.FALL ) {
            event.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onPlayerDropItem( PlayerDropItemEvent event ) {
        event.setCancelled( true );
    }
    
    @EventHandler
    public void onInventoryClick( InventoryClickEvent event ) {
        event.setCancelled( true );
    }
    
    @EventHandler
    public void onFoodLevelChange( FoodLevelChangeEvent event ) {
        event.setCancelled( true );
    }
}
