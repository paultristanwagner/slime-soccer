package me.paultristanwagner.slimesoccer.arena;

import org.bukkit.Location;

public class Goal {
    
    private final Location position1;
    private final Location position2;
    
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private double maxZ;
    private double minZ;
    
    public Goal( Location position1, Location position2 ) {
        this.position1 = position1;
        this.position2 = position2;
        calculateBorder();
    }
    
    private void calculateBorder() {
        int x1 = position1.getBlockX();
        int x2 = position2.getBlockX();
        minX = Math.min( x1, x2 );
        maxX = Math.max( x1, x2 );
    
        int y1 = position1.getBlockY();
        int y2 = position2.getBlockY();
        minY = Math.min( y1, y2 );
        maxY = Math.max( y1, y2 );
    
        int z1 = position1.getBlockZ();
        int z2 = position2.getBlockZ();
        minZ = Math.min( z1, z2 );
        maxZ = Math.max( z1, z2 );
    }

    public boolean isInside( Location location ) {
        return  location.getX() <= maxX && location.getX() >= minX &&
                location.getY() <= maxY && location.getY() >= minY &&
                location.getZ() <= maxZ && location.getZ() >= minZ;
    }
}
