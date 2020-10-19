package me.paultristanwagner.slimesoccer.i18n;

import me.paultristanwagner.slimesoccer.SlimeSoccerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class I18n {
    
    private static final String BASE_NAME = "messages";
    private static final String LANGUAGE_DIRECTORY = "languages";
    private static final String FILE_EXTENSION = ".properties";
    private static final String DEFAULT_BUNDLE = BASE_NAME + "_default";
    private static final String DEFAULT_FILE = DEFAULT_BUNDLE + FILE_EXTENSION;
    
    private static ResourceBundle defaultBundle;
    private static final Map<Locale, ResourceBundle> bundleMap = new HashMap<>();
    
    public static void broadcastMessage( String key, String... replace ) {
        broadcastMessage( Bukkit.getOnlinePlayers(), key, replace );
    }
    
    public static void broadcastMessage( Collection<? extends Player> playerList, String key, String... replace ) {
        playerList.forEach( player -> sendMessage( player, key, replace ) );
    }
    
    public static void sendMessage( Player player, String key, String... replace ) {
        ResourceBundle bundle = getBundle( player );
        String message = bundle.getString( key );
        String formatted = String.format( message, (Object[]) replace );
        player.sendMessage( formatted );
    }
    
    private static ResourceBundle getBundle( Player player ) {
        String localeTag = player.getLocale().replace( "_", "-" );
        Locale locale = Locale.forLanguageTag( localeTag );
        if ( locale.toLanguageTag().equals( "" ) ) {
            System.out.println( "Unknown locale '" + localeTag + "' for player " + player.getName() );
            return defaultBundle;
        }
        return bundleMap.getOrDefault( locale, defaultBundle );
    }
    
    public static void setup() throws IOException, URISyntaxException {
        saveDefaultResources();
        loadLanguages();
    }
    
    private static void saveDefaultResources() throws URISyntaxException, IOException {
        String jarPath = I18n.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        
        List<Path> result;
        URI uri2 = URI.create( "jar:file:" + jarPath );
        try ( FileSystem fs = FileSystems.newFileSystem( uri2, Collections.emptyMap() ) ) {
            result = Files.walk( fs.getPath( LANGUAGE_DIRECTORY ) )
                    .filter( Files::isRegularFile )
                    .collect( Collectors.toList() );
        }
        
        for ( Path path : result ) {
            saveDefault( path );
        }
    }
    
    
    private static void saveDefault( Path path ) throws IOException {
        JavaPlugin plugin = SlimeSoccerPlugin.getInstance();
        InputStream is = plugin.getResource( path.toString() );
        Objects.requireNonNull( is );
        
        String fileName = path.getFileName().toString();
        
        File languageFolder = new File( plugin.getDataFolder(), LANGUAGE_DIRECTORY );
        if ( !languageFolder.exists() ) {
            boolean result = languageFolder.mkdirs();
            if ( !result ) {
                throw new RuntimeException( "Could not create the language directory!" );
            }
        }
        
        File file = new File( languageFolder, fileName );
        if ( !file.exists() ) {
            Files.copy( is, file.toPath() );
        }
    }
    
    private static void loadLanguages() throws MalformedURLException {
        JavaPlugin plugin = SlimeSoccerPlugin.getInstance();
        File languageFolder = new File( plugin.getDataFolder(), LANGUAGE_DIRECTORY );
        
        File[] subFiles = languageFolder.listFiles();
        Objects.requireNonNull( subFiles );
        
        for ( File file : subFiles ) {
            String fileName = file.getName();
            if ( fileName.startsWith( BASE_NAME ) && fileName.endsWith( FILE_EXTENSION ) ) {
                loadLanguage( file );
            }
        }
    }
    
    private static void loadLanguage( File file ) throws MalformedURLException {
        File languageFolder = file.getParentFile();
        URL[] urls = { languageFolder.toURI().toURL() };
        ClassLoader loader = new URLClassLoader( urls );
        
        String fileName = file.getName();
        
        if ( fileName.equals( DEFAULT_FILE ) ) {
            defaultBundle = ResourceBundle.getBundle( DEFAULT_BUNDLE, Locale.getDefault(), loader );
            System.out.println( "Loaded default resource bundle" );
            return;
        }
        
        Locale locale = resolveLocale( fileName );
        ResourceBundle resourceBundle = ResourceBundle.getBundle( BASE_NAME, locale, loader );
        bundleMap.put( locale, resourceBundle );
        
        System.out.println( "Loaded resource bundle for locale " + locale.toLanguageTag() );
    }
    
    private static Locale resolveLocale( String fileName ) {
        String baseNameWithLocale = fileName.substring( 0, fileName.length() - 11 );
        
        String localeTag = fileName.substring( BASE_NAME.length() + 1, baseNameWithLocale.length() ).replace( "_", "-" );
        Locale locale = Locale.forLanguageTag( localeTag );
        
        if ( locale.getCountry().equals( "" ) ) {
            throw new IllformedLocaleException( "The locale tag of '" + fileName + "' could not be parsed" );
        }
        return locale;
    }
}
