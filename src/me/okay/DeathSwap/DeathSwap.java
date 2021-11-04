package me.okay.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    public final String PERMISSION_NAME = "deathswap.setup";

    private CommandListener commandListener;
    private Game game;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        
        // set up config
        saveDefaultConfig();
        config = getConfig();
        
        int minDelayMinutes = config.getInt("swapTimer.minMinutes");
        int maxDelayMinutes = config.getInt("swapTimer.maxMinutes");
        int countdownTimer = config.getInt("countdownTimer");
        boolean fallKills = config.getBoolean("fallKills");
        boolean netherEnabled = config.getBoolean("netherEnabled");
        boolean fireDamage = config.getBoolean("fireDamage");
        int totalLives = config.getInt("lives");
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();

        commandListener = new CommandListener(this);
        getCommand("deathswap").setExecutor(commandListener);
        game = new Game(this, minDelayMinutes, maxDelayMinutes, countdownTimer, totalLives, fallKills, fireDamage, netherEnabled);
        pm.registerEvents(game, this);

        // set up permissions
        Permission permission = pm.getPermission(PERMISSION_NAME);
        if (permission == null) {
            permission = new Permission(PERMISSION_NAME);
            pm.addPermission(permission);
        }
    }
    
    @Override
    public void onDisable() {
        
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public Game getGame() {
        return game;
    }

    // returns the string colored by &
    public static String toColorString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}