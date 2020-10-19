package me.paultristanwagner.slimesoccer.util;

import com.google.common.base.Suppliers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A simple expandable class to build ItemStack.
 *
 * @author Overload
 * @version 1.0
 * @since 2.0
 */
public class ItemBuilders {
    
    private static final Supplier<Field> skullOwnerField = Suppliers.memoize( () -> {
        try {
            ItemStack itemStack = new ItemStack( Material.PLAYER_HEAD ); // hack to get the class because it is protected
            ItemMeta meta = itemStack.getItemMeta();
            Objects.requireNonNull( meta );
            Class<?> clazz = meta.getClass();
            Field field = clazz.getDeclaredField( "profile" );
            field.setAccessible( true );
            return field;
        } catch ( Throwable t ) {
            throw new RuntimeException( t );
        }
    } );
    
    public static abstract class ItemBuilder<T extends ItemMeta> {
        
        protected final ItemStack itemStack;
        
        private Material material;
        private int amount;
        private short durability;
        protected final T itemMeta;
        
        @SuppressWarnings("unchecked")
        protected ItemBuilder( ItemStack itemStack, Class<T> typeOfT ) throws IllegalArgumentException {
            ItemMeta meta = itemStack.getItemMeta();
            Objects.requireNonNull( meta );
            if ( !typeOfT.isAssignableFrom( meta.getClass() ) ) {
                throw new IllegalArgumentException( "ItemStack's meta is not of the right type." );
            }
            
            this.itemStack = itemStack;
            
            this.material = itemStack.getType();
            this.amount = itemStack.getAmount();
            this.durability = itemStack.getDurability();
            this.itemMeta = (T) itemStack.getItemMeta();
        }
        
        public ItemStack apply() {
            apply( itemStack );
            return itemStack;
        }
        
        public ItemStack build() {
            ItemStack builtItemStack = itemStack.clone();
            apply( builtItemStack );
            return builtItemStack;
        }
        
        protected void apply( ItemStack targetItemStack ) {
            targetItemStack.setType( material );
            targetItemStack.setAmount( amount );
            targetItemStack.setDurability( durability );
            targetItemStack.setItemMeta( itemMeta );
        }
        
        public ItemBuilder<T> material( Material material ) {
            this.material = material;
            return this;
        }
        
        public Material material() {
            return material;
        }
        
        public ItemBuilder<T> amount( int amount ) {
            this.amount = amount;
            return this;
        }
        
        public int amount() {
            return amount;
        }
        
        public ItemBuilder<T> durability( short durability ) {
            this.durability = durability;
            return this;
        }
        
        public short durability() {
            return durability;
        }
        
        public ItemBuilder<T> name( String displayName ) {
            itemMeta.setDisplayName( displayName );
            return this;
        }
        
        public String name() {
            return itemMeta.getDisplayName();
        }
        
        public ItemBuilder<T> lore( List<String> lore ) {
            itemMeta.setLore( lore );
            return this;
        }
        
        public List<String> lore() {
            return itemMeta.getLore();
        }
        
        public ItemBuilder<T> lore( String... lore ) {
            return lore( Arrays.asList( lore ) );
        }
        
        public ItemBuilder<T> enchant( Enchantment enchantment, int level ) {
            itemMeta.addEnchant( enchantment, level, true );
            return this;
        }
        
        public ItemBuilder<T> hide( ItemFlag... hiddenFlags ) {
            itemMeta.addItemFlags( hiddenFlags );
            return this;
        }
        
        public ItemBuilder<T> unbreakable( boolean unbreakable ) {
            itemMeta.setUnbreakable( unbreakable );
            return this;
        }
        
        public ItemBuilder<T> unbreakable() {
            return unbreakable( true );
        }
        
    }
    
    public static class NormalItemBuilder extends ItemBuilder<ItemMeta> {
        
        protected NormalItemBuilder( ItemStack itemStack ) {
            super( itemStack, ItemMeta.class );
        }
        
        protected NormalItemBuilder( Material material ) {
            this( new ItemStack( material ) );
        }
        
    }
    
    public static class FireworkEffectItemBuilder extends ItemBuilder<FireworkEffectMeta> {
        
        protected FireworkEffectItemBuilder( ItemStack itemStack ) throws IllegalArgumentException {
            super( itemStack, FireworkEffectMeta.class );
        }
        
        protected FireworkEffectItemBuilder() {
            this( new ItemStack( Material.FIREWORK_ROCKET ) );
        }
        
        public FireworkEffectItemBuilder effect( FireworkEffect fireworkEffect ) {
            itemMeta.setEffect( fireworkEffect );
            return this;
        }
        
    }
    
    /**
     * For backwards comparability
     */
    @Deprecated
    public static class FireworkItemBuilder extends FireworkEffectItemBuilder {
    }
    
    public static class PotionItemBuilder extends ItemBuilder<PotionMeta> {
        
        protected PotionItemBuilder( ItemStack itemStack ) throws IllegalArgumentException {
            super( itemStack, PotionMeta.class );
        }
        
        protected PotionItemBuilder() {
            this( new ItemStack( Material.POTION ) );
        }
        
        public PotionItemBuilder effect( Potion potion ) {
            potion.apply( itemStack );
            return this;
        }
        
        public PotionItemBuilder effect( PotionEffect potionEffect, boolean overwrite ) {
            itemMeta.addCustomEffect( potionEffect, overwrite );
            return this;
        }
        
        public PotionItemBuilder mainEffect( PotionEffectType potionEffectType ) {
            itemMeta.setMainEffect( potionEffectType );
            return this;
        }
        
    }
    
    public static class LeatherArmorItemBuilder extends ItemBuilder<LeatherArmorMeta> {
        
        protected LeatherArmorItemBuilder( ItemStack itemStack ) throws IllegalArgumentException {
            super( itemStack, LeatherArmorMeta.class );
        }
        
        protected LeatherArmorItemBuilder( ArmorElement armorElement ) {
            this( new ItemStack( armorElement.getMaterial() ) );
        }
        
        public LeatherArmorItemBuilder dye( Color color ) {
            itemMeta.setColor( color );
            return this;
        }
        
        public enum ArmorElement {
            HELMET( Material.LEATHER_HELMET ),
            CHESTPLATE( Material.LEATHER_CHESTPLATE ),
            LEGGINGS( Material.LEATHER_LEGGINGS ),
            BOOTS( Material.LEATHER_BOOTS );
            
            private final Material material;
            
            ArmorElement( Material material ) {
                this.material = material;
            }
            
            public Material getMaterial() {
                return material;
            }
        }
        
    }
    
    public static class SkullItemBuilder extends ItemBuilder<SkullMeta> {
        
        protected SkullItemBuilder( ItemStack itemStack ) {
            super( itemStack, SkullMeta.class );
        }
        
        protected SkullItemBuilder() {
            this( new ItemStack( Material.PLAYER_HEAD ) );
        }
        
        /**
         * Sets the skull owner of this skull. Deprecated: Use @link{SkullItemBuilder:owner(GameProfile)} useless to prevent Name->UUID resolving
         *
         * @param owner The game profile of the owner of the skull
         * @return The builder instance for fluent access
         */
        @Deprecated
        public SkullItemBuilder owner( String owner ) {
            // Change durability to make player skulls possible
            durability( (short) SkullType.PLAYER.ordinal() );
            
            itemMeta.setOwner( owner );
            return this;
        }
        
        public SkullItemBuilder owner( GameProfile profile ) {
            durability( (short) SkullType.PLAYER.ordinal() );
            try {
                skullOwnerField.get().set( itemMeta, profile );
            } catch ( IllegalAccessException e ) {
                e.printStackTrace();
            }
            return this;
        }
        
        /**
         * Sets the textures of this skull
         *
         * @param textureUrlBase64 The base64 encoded json structure containing the urls to skins and cape
         * @return The builder instance for fluent access
         */
        public SkullItemBuilder texture( String textureUrlBase64 ) {
            GameProfile profile = new GameProfile( UUID.randomUUID(), null );
            profile.getProperties().put( "textures", new Property( "textures", textureUrlBase64 ) );
            return owner( profile );
        }
        
        public SkullItemBuilder type( SkullType skullType ) {
            durability( (short) skullType.ordinal() );
            return this;
        }
    }
    
    public static class BookItemBuilder extends ItemBuilder<BookMeta> {
        
        protected BookItemBuilder( ItemStack itemStack ) {
            super( itemStack, BookMeta.class );
        }
        
        protected BookItemBuilder( boolean writable ) {
            this( new ItemStack( writable ? Material.BOOK : Material.WRITTEN_BOOK ) );
        }
        
        public BookItemBuilder title( String title ) {
            itemMeta.setTitle( title );
            return this;
        }
        
        public BookItemBuilder author( String author ) {
            itemMeta.setAuthor( author );
            return this;
        }
        
        public BookItemBuilder page( String... data ) {
            itemMeta.addPage( data );
            return this;
        }
        
        public BookItemBuilder page( int page, String data ) {
            itemMeta.setPage( page, data );
            return this;
        }
        
        public BookItemBuilder pages( List<String> pages ) {
            itemMeta.setPages( pages );
            return this;
        }
        
        public BookItemBuilder pages( String... pages ) {
            itemMeta.setPages( pages );
            return this;
        }
        
    }
    
    public static ItemStack air() {
        return new ItemStack( Material.AIR );
    }
    
    public static ItemBuilder<? extends ItemMeta> wrap( ItemStack itemStack ) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        if ( itemMeta instanceof FireworkEffectMeta ) {
            return new FireworkEffectItemBuilder( itemStack );
        } else if ( itemMeta instanceof PotionMeta ) {
            return new PotionItemBuilder( itemStack );
        } else if ( itemMeta instanceof LeatherArmorMeta ) {
            return new LeatherArmorItemBuilder( itemStack );
        } else if ( itemMeta instanceof SkullMeta ) {
            return new SkullItemBuilder( itemStack );
        } else if ( itemMeta instanceof BookMeta ) {
            return new BookItemBuilder( itemStack );
        }
        
        return new NormalItemBuilder( itemStack );
    }
    
    public static ItemBuilder<ItemMeta> normal( Material material ) {
        return new NormalItemBuilder( material );
    }
    
    public static FireworkEffectItemBuilder firework() {
        return new FireworkEffectItemBuilder();
    }
    
    public static PotionItemBuilder potion() {
        return new PotionItemBuilder();
    }
    
    public static LeatherArmorItemBuilder armor( LeatherArmorItemBuilder.ArmorElement armorElement ) {
        return new LeatherArmorItemBuilder( armorElement );
    }
    
    public static SkullItemBuilder skull() {
        return new SkullItemBuilder();
    }
    
    public static BookItemBuilder book( boolean writable ) {
        return new BookItemBuilder( writable );
    }
    
    public static BookItemBuilder book() {
        return book( false );
    }
    
}
