package me.paultristanwagner.slimesoccer.util;

import me.paultristanwagner.slimesoccer.game.Team;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Item {
    
    public static final ItemStack KNOCKBACK_STICK = ItemBuilders.normal( Material.STICK )
            .name("§5Baton")
            .enchant( Enchantment.KNOCKBACK, 2 )
            .hide( ItemFlag.HIDE_ENCHANTS )
            .build();
    
    public static ItemStack getTeamArmor( ItemBuilders.LeatherArmorItemBuilder.ArmorElement armorElement, Team team ) {
        return ItemBuilders.armor( armorElement ).dye( team.getColor().getBukkitColor() ).build();
    }
}
