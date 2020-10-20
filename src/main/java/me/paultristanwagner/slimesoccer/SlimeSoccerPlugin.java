package me.paultristanwagner.slimesoccer;

import me.paultristanwagner.slimesoccer.arena.Arena;
import me.paultristanwagner.slimesoccer.arena.Goal;
import me.paultristanwagner.slimesoccer.i18n.I18n;
import me.paultristanwagner.slimesoccer.listener.ArenaListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class SlimeSoccerPlugin extends JavaPlugin {
    
    private Arena arena;
    
    @Override
    public void onLoad() {
        setupConfig();
    }
    
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
    
    private void setupConfig() {
        ConfigurationSerialization.registerClass( Goal.class );
        saveDefaultConfig();
    }
    
    private void setupArena() {
        FileConfiguration config = getConfig();
        Location defaultLocation = Bukkit.getWorlds().get( 0 ).getSpawnLocation();
        Goal defaultGoal = new Goal( defaultLocation, defaultLocation );
        
        arena = Arena.instantiate(
                Objects.requireNonNull( config.getLocation( "arena.pos1", defaultLocation ) ),
                Objects.requireNonNull( config.getLocation( "arena.pos2", defaultLocation ) ),
                Objects.requireNonNull( config.getLocation( "arena.red-spawn", defaultLocation ) ),
                Objects.requireNonNull( config.getLocation( "arena.blue-spawn", defaultLocation ) ),
                Objects.requireNonNull( config.getObject( "arena.red-goal", Goal.class, defaultGoal ) ),
                Objects.requireNonNull( config.getObject( "arena.blue-goal", Goal.class, defaultGoal ) )
        );
    }
    
    public static SlimeSoccerPlugin getInstance() {
        return JavaPlugin.getPlugin( SlimeSoccerPlugin.class );
    }
}
