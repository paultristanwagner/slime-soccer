package me.paultristanwagner.slimesoccer.game;

import org.bukkit.Color;

public enum TeamColor {

    RED( Color.RED ),
    BLUE( Color.BLUE );
    
    private final Color color;
    
    TeamColor( Color color ) {
        this.color = color;
    }
    
    public Color getBukkitColor() {
        return color;
    }
}
