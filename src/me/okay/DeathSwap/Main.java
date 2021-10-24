package me.okay.DeathSwap;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    final public String PERMISSION_NAME = "deathswap.setup";
    private CommandListener commandListener = new CommandListener(this);
    private Game game = new Game(this);

    @Override
    public void onEnable() {
        
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
}