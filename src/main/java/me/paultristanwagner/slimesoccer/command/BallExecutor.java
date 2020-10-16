package me.paultristanwagner.slimesoccer.command;

import me.paultristanwagner.slimesoccer.entity.SoccerBall;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class BallExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args ) {
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( "Only players can execute this command." );
            return true;
        }
        
        Player player = (Player) sender;
        Location location = player.getLocation();
        SoccerBall ball = new SoccerBall( location );
        player.sendMessage( "Ball spawned!" );
        return true;
    }
}
