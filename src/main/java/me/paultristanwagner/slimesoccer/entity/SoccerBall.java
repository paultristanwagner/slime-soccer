package me.paultristanwagner.slimesoccer.entity;

import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoccerBall extends EntitySlime {
    
    public SoccerBall( @NotNull Location location ) {
        super( EntityTypes.SLIME, ( (CraftWorld) Objects.requireNonNull( location.getWorld() ) ).getHandle() );
        
        setLocation( location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch() );
        setSize( 2, true );
        setInvulnerable( true );
        
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
        if ( !( entity instanceof EntityPlayer ) ) {
            return;
        }
        
        Vec3D kickerVector = entity.getPositionVector();
        Vec3D ballVector = getPositionVector();
        
        Vec3D direction = kickerVector.a( ballVector );
        Vector vector = new Vector( direction.getX(), direction.getY(), direction.getZ() ).normalize().multiply( 2 ).setY( 0.4 );
        
        try {
            vector.checkFinite();
            org.bukkit.entity.Entity bukkitEntity = getBukkitEntity();
            bukkitEntity.setVelocity( vector );
            Location location = bukkitEntity.getLocation();
            World world = location.getWorld();
            Objects.requireNonNull( world );
            
            world.playSound( location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 2 );
        } catch ( IllegalArgumentException ignored ) {
        }
    }
    
    @Override
    public void die() {
        this.dead = true;
    }
}
