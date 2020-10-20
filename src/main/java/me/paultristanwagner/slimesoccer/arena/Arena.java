package me.paultristanwagner.slimesoccer.arena;

import me.paultristanwagner.slimesoccer.SlimeSoccerPlugin;
import me.paultristanwagner.slimesoccer.effect.SimpleFireworkEffect;
import me.paultristanwagner.slimesoccer.entity.SoccerBall;
import me.paultristanwagner.slimesoccer.game.TeamColor;
import me.paultristanwagner.slimesoccer.i18n.I18n;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Arena {
    
    private static Arena instance;
    
    private final Location position1;
    private final Location position2;
    private final Location redLocation;
    private final Location blueLocation;
    
    private final Goal redGoal;
    private final Goal blueGoal;
    
    private Location middle;
    
    private SoccerBall ball;
    
    private int minX;
    private int maxX;
    private int maxY;
    private int minZ;
    private int maxZ;
    
    private Arena( @NotNull Location position1, @NotNull Location position2,
                   @NotNull Location redLocation, @NotNull Location blueLocation,
                   @NotNull Location redGoalPosition1, @NotNull Location redGoalPosition2,
                   @NotNull Location blueGoalPosition1, @NotNull Location blueGoalPosition2 ) {
        this.position1 = position1;
        this.position2 = position2;
        this.redLocation = redLocation;
        this.blueLocation = blueLocation;
        this.redGoal = new Goal( redGoalPosition1, redGoalPosition2 );
        this.blueGoal = new Goal( blueGoalPosition1, blueGoalPosition2 );
        
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
        Bukkit.getScheduler().runTaskTimer( SlimeSoccerPlugin.getInstance(), this::tickBall, 1, 1 );
    }
    
    private void tickBall() {
        if ( !hasBall() ) {
            return;
        }
        
        double baseRepelSpeed = 0.3;
        
        Entity ballEntity = getBallEntity();
        Location ballLocation = ballEntity.getLocation();
        
        boolean goal = false;
        if ( redGoal.isInside( ballLocation ) ) {
            goal = true;
            SimpleFireworkEffect.spawn( ballLocation, TeamColor.BLUE );
            I18n.broadcastMessage( "blue_scored" );
        } else if ( blueGoal.isInside( ballLocation ) ) {
            goal = true;
            SimpleFireworkEffect.spawn( ballLocation, TeamColor.RED );
            I18n.broadcastMessage( "red_scored" );
        }
        
        if ( goal ) {
            ballEntity.remove();
            ball = null;
            
            Bukkit.getScheduler().runTaskLater( SlimeSoccerPlugin.getInstance(), this::spawnBall, 4 * 20L );
            return;
        }
        
        
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
            world.playSound( ballLocation, Sound.UI_BUTTON_CLICK, 0.75f, 2 );
        }
    }
    
    public void unregister() {
        if ( ball != null ) {
            ball.killEntity();
        }
    }
    
    public boolean hasBall() {
        return ball != null;
    }
    
    public Entity getBallEntity() {
        if ( hasBall() ) {
            return ball.getBukkitEntity();
        } else {
            throw new IllegalStateException( "The arena does not have a ball!" );
        }
    }
    
    public Location getRedLocation() {
        return redLocation;
    }
    
    public Location getBlueLocation() {
        return blueLocation;
    }
    
    public static synchronized Arena instantiate( @NotNull Location position1, @NotNull Location position2,
                                                  @NotNull Location redLocation, @NotNull Location blueLocation,
                                                  @NotNull Location goal1Position1, @NotNull Location goal1Position2,
                                                  @NotNull Location goal2Position1, @NotNull Location goal2Position2 ) {
        if ( instance != null ) {
            throw new IllegalStateException( "Arena should only be instantiated once!" );
        }
        
        return instance = new Arena( position1, position2, redLocation, blueLocation, goal1Position1, goal1Position2, goal2Position1, goal2Position2 );
    }
    
    public static synchronized Arena getInstance() {
        if ( instance == null ) {
            throw new IllegalStateException( "Instance should not be accessed before getting instantiated!" );
        }
        return instance;
    }
}
