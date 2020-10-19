package me.paultristanwagner.slimesoccer.game;

import me.paultristanwagner.slimesoccer.arena.Arena;
import me.paultristanwagner.slimesoccer.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {
    
    private static Game instance;
    
    private final Arena arena = Arena.getInstance();
    private final Team redTeam = new Team( TeamColor.RED, arena.getRedLocation() );
    private final Team blueTeam = new Team( TeamColor.BLUE, arena.getBlueLocation() );
    
    private Game() {
        setupTeams();
        I18n.broadcastMessage( "game_start" );
    }
    
    public void setupTeams() {
        Bukkit.getOnlinePlayers().forEach( this::addPlayer );
    }
    
    public void addPlayer( Player player ) {
        Team smallestTeam = smallestTeam();
        smallestTeam.add( player );
    }
    
    public void removePlayer( Player player ) {
        redTeam.remove( player );
        blueTeam.remove( player );
    }
    
    //todo use this to make players not hit their mates
    public Team getTeam( Player player ) {
        if ( redTeam.isMember( player ) ) {
            return redTeam;
        } else if ( blueTeam.isMember( player ) ) {
            return blueTeam;
        }
        throw new IllegalArgumentException( "Every player should be in a team" );
    }
    
    private Team smallestTeam() {
        int redSize = redTeam.getSize();
        int blueSize = blueTeam.getSize();
        if ( redSize < blueSize ) {
            return redTeam;
        } else if ( redSize > blueSize ) {
            return blueTeam;
        } else {
            return Math.random() < 0.5 ? blueTeam : redTeam;
        }
    }
    
    public static synchronized Game getInstance() {
        if ( instance == null ) {
            instance = new Game();
        }
        return instance;
    }
}
