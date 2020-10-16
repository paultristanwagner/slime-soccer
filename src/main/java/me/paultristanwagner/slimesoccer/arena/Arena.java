package me.paultristanwagner.slimesoccer.arena;

import me.paultristanwagner.slimesoccer.SlimeSoccerPlugin;
import me.paultristanwagner.slimesoccer.entity.SoccerBall;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Arena {
    
    private final Location position1;
    private final Location position2;
    
    private Location middle;
    
    private SoccerBall ball;
    
    private int minX;
    private int maxX;
    private int maxY;
    private int minZ;
    private int maxZ;
    
    public Arena( @NotNull Location position1, @NotNull Location position2 ) {
        this.position1 = position1;
        this.position2 = position2;
        
        calculateBorders();
        spawnBall();
        startTicking();
    }
    
    private void calculateBorders() {
        int x1 = position1.getBlockX();
        int x2 = position2.getBlockX();
        minX = Math.min( x1, x2 );
        maxX = Math.max( x1, x2 );
        
        int y1 = position1.getBlockY();
        int y2 = position2.getBlockY();
        maxY = Math.max( y1, y2 );
        
        int z1 = position1.getBlockZ();
        int z2 = position2.getBlockZ();
        minZ = Math.min( z1, z2 );
        maxZ = Math.max( z1, z2 );
        
        double middleX = ( x1 + x2 ) / 2.0;
        double middleZ = ( z1 + z2 ) / 2.0;
        middle = new Location( position1.getWorld(), middleX, position1.getY() + 5, middleZ );
    }
    
    private void spawnBall() {
        ball = new SoccerBall( middle );
    }
    
    private void startTicking() {
        Bukkit.getScheduler().runTaskTimer( SlimeSoccerPlugin.getInstance(), this::tick, 1, 1 );
    }
    
    private void tick() {
        double baseRepelSpeed = 0.3;
        
        Entity ballEntity = ball.getBukkitEntity();
        Location ballLocation = ballEntity.getLocation();
        Vector vector = ballEntity.getVelocity();
        
        boolean update = false;
        if ( ballLocation.getX() > maxX && vector.getX() > 0 ) {
            vector.setX( Math.min( -baseRepelSpeed, -vector.getX() ) );
            update = true;
        } else if ( ballLocation.getX() < minX && vector.getX() < 0 ) {
            vector.setX( Math.max( baseRepelSpeed, -vector.getX() ) );
            update = true;
        }
        
        if ( ballLocation.getZ() > maxZ && vector.getZ() > 0 ) {
            vector.setZ( Math.min( -baseRepelSpeed, -vector.getZ() ) );
            update = true;
        } else if ( ballLocation.getZ() < minZ && vector.getZ() < 0 ) {
            vector.setZ( Math.max( baseRepelSpeed, -vector.getZ() ) );
            update = true;
        }
        
        if ( ballLocation.getY() > maxY && vector.getY() > 0 ) {
            vector.setY( Math.min( -baseRepelSpeed, -vector.getY() ) );
            update = true;
        }
        
        if ( update ) {
            ballEntity.setVelocity( vector );
            
            World world = ballLocation.getWorld();
            Objects.requireNonNull( world );
            
            world.spawnParticle( Particle.DRAGON_BREATH, ballLocation, 30, 0.2, 0.2, 0.2, 0.1 );
            world.playSound( ballLocation, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.75f, 2 );
        }
    }
    
    public void unregister() {
        ball.killEntity();
    }
}
