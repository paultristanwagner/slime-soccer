package me.paultristanwagner.slimesoccer;

import me.paultristanwagner.slimesoccer.arena.Arena;
import me.paultristanwagner.slimesoccer.command.BallExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SlimeSoccerPlugin extends JavaPlugin {
    
    private Arena arena;
    
    @Override
    public void onEnable() {
        PluginCommand ballCommand = getCommand( "ball" );
        Objects.requireNonNull( ballCommand );
        
        ballCommand.setExecutor( new BallExecutor() );
        
        setupArena();
    }
    
    @Override
    public void onDisable() {
        arena.unregister();
    }
    
    private void setupArena() {
        World world = Bukkit.getWorld( "world" );
        Location pos1 = new Location( world, -72.5, 90, -180.5 );
        Location pos2 = new Location( world, -40.5, 100, -202.5 );
        
        arena = new Arena( pos1, pos2 );
    }
    
    public static SlimeSoccerPlugin getInstance() {
        return JavaPlugin.getPlugin( SlimeSoccerPlugin.class );
    }
}
