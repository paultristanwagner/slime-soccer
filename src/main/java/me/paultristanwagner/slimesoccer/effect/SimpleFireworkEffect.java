package me.paultristanwagner.slimesoccer.effect;

import me.paultristanwagner.slimesoccer.SlimeSoccerPlugin;
import me.paultristanwagner.slimesoccer.game.TeamColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Objects;

public class SimpleFireworkEffect {
    
    public static void spawn( Location location, TeamColor scorerColor ) {
        World world = location.getWorld();
        Objects.requireNonNull( world );
        Firework firework = (Firework) world.spawnEntity( location, EntityType.FIREWORK );
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect fireworkEffect = FireworkEffect.builder()
                .flicker( true )
                .withColor( Color.fromBGR( 0, 255, 0 ) )
                .with( FireworkEffect.Type.BALL_LARGE )
                .withFade( scorerColor.getBukkitColor() )
                .build();
        
        fireworkMeta.addEffect( fireworkEffect );
        firework.setFireworkMeta( fireworkMeta );
        
        Bukkit.getScheduler().runTask( SlimeSoccerPlugin.getInstance(), firework::detonate );
    }
}
