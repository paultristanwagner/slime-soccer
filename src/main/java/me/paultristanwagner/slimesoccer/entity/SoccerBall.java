package me.paultristanwagner.slimesoccer.entity;

import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoccerBall extends EntitySlime {
    
    public SoccerBall( @NotNull Location location ) {
        super( EntityTypes.SLIME, ( (CraftWorld) Objects.requireNonNull( location.getWorld() ) ).getHandle() );
        
        setLocation( location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch() );
        setSize( 2, true );
        
        ( (CraftWorld) location.getWorld() ).getHandle().addEntity( this );
    }
    
    @Override
    protected void initPathfinder() {
    }
    
    @Override
    public void checkDespawn() {
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.5f;
    }
    
    @Override
    public void collide( Entity entity ) {
    
    }
    
    @Override
    public void die() {
        this.dead = true;
    }
}
