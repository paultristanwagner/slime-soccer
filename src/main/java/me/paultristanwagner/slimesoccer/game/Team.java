package me.paultristanwagner.slimesoccer.game;

import me.paultristanwagner.slimesoccer.i18n.I18n;
import me.paultristanwagner.slimesoccer.util.Item;
import me.paultristanwagner.slimesoccer.util.ItemBuilders;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    
    private final TeamColor color;
    private final Location spawnLocation;
    private final List<Player> playerList = new ArrayList<>();
    
    public Team( TeamColor color, Location spawnLocation ) {
        this.color = color;
        this.spawnLocation = spawnLocation;
    }
    
    public void add( Player player ) {
        playerList.add( player );
        setupPlayer( player );
        
        if ( color == TeamColor.RED ) {
            I18n.sendMessage( player, "join_team_red" );
        } else if ( color == TeamColor.BLUE ) {
            I18n.sendMessage( player, "join_team_blue" );
        }
    }
    
    public boolean remove( Player player ) {
        return playerList.remove( player );
    }
    
    public boolean isMember( Player player ) {
        return playerList.contains( player );
    }
    
    public int getSize() {
        return playerList.size();
    }
    
    private void setupPlayer( Player player ) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem( 0, Item.KNOCKBACK_STICK );
        inventory.setHelmet( Item.getTeamArmor( ItemBuilders.LeatherArmorItemBuilder.ArmorElement.HELMET, this ) );
        inventory.setChestplate( Item.getTeamArmor( ItemBuilders.LeatherArmorItemBuilder.ArmorElement.CHESTPLATE, this ) );
        inventory.setLeggings( Item.getTeamArmor( ItemBuilders.LeatherArmorItemBuilder.ArmorElement.LEGGINGS, this ) );
        inventory.setBoots( Item.getTeamArmor( ItemBuilders.LeatherArmorItemBuilder.ArmorElement.BOOTS, this ) );
        
        player.setGameMode( GameMode.ADVENTURE );
        player.setFoodLevel( 20 );
        player.setHealth( Objects.requireNonNull( player.getAttribute( Attribute.GENERIC_MAX_HEALTH ) ).getValue() );
        player.teleport( spawnLocation );
    }
    
    public TeamColor getColor() {
        return color;
    }
}
