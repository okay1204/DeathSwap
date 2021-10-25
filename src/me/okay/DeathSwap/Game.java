package me.okay.DeathSwap;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Game implements Listener {
    private final int LOWEST_TP_COORD = -9999999;
    private final int HIGHEST_TP_COORD = 9999999;

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

        Bukkit.broadcastMessage(DeathSwap.toColorString("&6&oTeleporting players..."));
        spawnPlayers();
        Bukkit.broadcastMessage(DeathSwap.toColorString("&a&lDone! &6Game starts now, good luck!"));
    } 

    // Gets all players that are in survival mode
    public ArrayList<Player> getSurvivalPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Player player : deathSwap.getServer().getOnlinePlayers()) {
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                players.add(player);
            }
        }

        return players;
    }

    // teleports players to random locations
    public void spawnPlayers() {

        World world = Bukkit.getWorld("world");

        for (Player player : participants) {
            int xCoord = (int) Math.floor(Math.random() * (HIGHEST_TP_COORD - LOWEST_TP_COORD) + LOWEST_TP_COORD + 1);
            int zCoord = (int) Math.floor(Math.random() * (HIGHEST_TP_COORD - LOWEST_TP_COORD) + LOWEST_TP_COORD + 1);

            int yCoord = world.getHighestBlockYAt(xCoord, zCoord) + 1;
            Location teleportLocation = new Location(world, xCoord + 0.5, yCoord, zCoord + 0.5);
            
            while (teleportLocation.getBlock().getBiome().toString().contains("OCEAN")) {
                xCoord += 50;
                yCoord = world.getHighestBlockYAt(xCoord, zCoord) + 1;
                teleportLocation = new Location(world, xCoord + 0.5, yCoord, zCoord + 0.5);
            }


            player.teleport(teleportLocation);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (participants.contains(event.getPlayer())) {
            participants.remove(event.getPlayer());

            // TODO check if game is over
        }
    }
}
