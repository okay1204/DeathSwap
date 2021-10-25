package me.okay.DeathSwap;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Game implements Listener {
    private DeathSwap deathSwap;
    private ArrayList<Player> participants;

    public Game(DeathSwap deathSwapClass) {
        deathSwap = deathSwapClass;
    }

    // Starts the game with all players in survival mode
    public void startGame() throws IllegalArgumentException {
        startGame(getSurvivalPlayers());
    }

    // Starts the game with a given list of players
    public void startGame(ArrayList<Player> players) throws IllegalArgumentException {
        if (players.size() < 2) {
            throw new IllegalArgumentException("Not enough players to start game.");
        }

        participants = players;
    } 

    // Gets all players that are in survival mode
    public ArrayList<Player> getSurvivalPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Player player : deathSwap.getServer().getOnlinePlayers()) {
            if (player.getGameMode().equals(org.bukkit.GameMode.SURVIVAL)) {
                players.add(player);
            }
        }

        return players;
    }
}
