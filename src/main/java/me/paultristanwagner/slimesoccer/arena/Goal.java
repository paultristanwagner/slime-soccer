package me.paultristanwagner.slimesoccer.arena;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Goal implements ConfigurationSerializable {
    
    private final Location pos1;
    private final Location pos2;
    
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private double maxZ;
    private double minZ;
    
    public Goal( Location pos1, Location pos2 ) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        calculateBorder();
    }
    
    private void calculateBorder() {
        int x1 = pos1.getBlockX();
        int x2 = pos2.getBlockX();
        minX = Math.min( x1, x2 );
        maxX = Math.max( x1, x2 );
    
        int y1 = pos1.getBlockY();
        int y2 = pos2.getBlockY();
        minY = Math.min( y1, y2 );
        maxY = Math.max( y1, y2 );
    
        int z1 = pos1.getBlockZ();
        int z2 = pos2.getBlockZ();
        minZ = Math.min( z1, z2 );
        maxZ = Math.max( z1, z2 );
    }

    public boolean isInside( Location location ) {
        return  location.getX() <= maxX && location.getX() >= minX &&
                location.getY() <= maxY && location.getY() >= minY &&
                location.getZ() <= maxZ && location.getZ() >= minZ;
    }
    
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put( "pos1", pos1 );
        map.put( "pos2", pos2 );
        return map;
    }
    
    @NotNull
    public static Goal deserialize(@NotNull Map<String, Object> args) {
        Location pos1 = (Location) args.get( "pos1" );
        Location pos2 = (Location) args.get( "pos2" );
        Objects.requireNonNull(pos1);
        Objects.requireNonNull(pos2);
        return new Goal( pos1, pos2 );
    }
}
