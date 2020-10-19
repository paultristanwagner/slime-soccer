package me.paultristanwagner.slimesoccer;

import me.paultristanwagner.slimesoccer.arena.Arena;
import me.paultristanwagner.slimesoccer.i18n.I18n;
import me.paultristanwagner.slimesoccer.listener.ArenaListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;

public class SlimeSoccerPlugin extends JavaPlugin {
    
    private Arena arena;
    
    @Override
    public void onEnable() {
        try {
            I18n.setup();
        } catch ( IOException | URISyntaxException e ) {
            e.printStackTrace();
        }
        
        setupArena();
        registerListeners();
    }
    
    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents( new ArenaListener(), this );
    }
    
    @Override
    public void onDisable() {
        arena.unregister();
    }
    
    private void setupArena() {
        World world = Bukkit.getWorld( "world" );
        Location pos1 = new Location( world, -73.0, 90.0, -180.0 );
        Location pos2 = new Location( world, -40.0, 100, -203.0 );
        
        Location redLocation = new Location( world, -44.5, 91.0, -191.5, 90, 0 );
        Location blueLocation = new Location( world, -68.5, 91.0, -191.5, -90, 0 );
        
        Location goal1Pos1 = new Location( world, -40.0, 91.0, -188.0 );
        Location goal1Pos2 = new Location( world, -39.0, 95.0, -195.0 );
        Location goal2Pos1 = new Location( world, -73.0, 91.0, -188.0 );
        Location goal2Pos2 = new Location( world, -74.0, 95.0, -195.0 );
        
        arena = Arena.instantiate( pos1, pos2, redLocation, blueLocation, goal1Pos1, goal1Pos2, goal2Pos1, goal2Pos2 );
    }
    
    public static SlimeSoccerPlugin getInstance() {
        return JavaPlugin.getPlugin( SlimeSoccerPlugin.class );
    }
}
