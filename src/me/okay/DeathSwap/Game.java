package me.okay.DeathSwap;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Game implements Listener {
    private DeathSwap deathSwap;
    private ArrayList<Player> players;

    public Game(DeathSwap deathSwapClass) {
        deathSwap = deathSwapClass;
    }
}
