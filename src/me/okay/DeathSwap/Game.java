package me.okay.DeathSwap;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Game implements Listener {
    private Main main;
    private ArrayList<Player> players;

    public Game(Main mainClass) {
        main = mainClass;
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}
