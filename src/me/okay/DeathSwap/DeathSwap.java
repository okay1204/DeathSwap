package me.okay.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    final public String PERMISSION_NAME = "deathswap.setup";
    private CommandListener commandListener;
    private Game game;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        commandListener = new CommandListener(this);
        getCommand("deathswap").setExecutor(commandListener);

        game = new Game(this);
        pluginManager.registerEvents(game, this);
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