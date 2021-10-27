package me.okay.DeathSwap;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Game implements Listener {
    private final int LOWEST_TP_COORD = -9999999;
    private final int HIGHEST_TP_COORD = 9999999;

    // Config settings
    private int minDelayMinutes;
    private int maxDelayMinutes;
    private int countdownTimer;
    private int totalLives;
    private boolean fallKills;
    private boolean fireDamage;
    private boolean netherEnabled;


    private DeathSwap deathSwap;
    private ArrayList<Player> participants;
    private ArrayList<Player> fallImmune = new ArrayList<Player>();
    private BukkitTask teleportDelayTask;

    // countdown
    private int currentTimer = 10;

    public Game(DeathSwap deathSwapClass) {
        deathSwap = deathSwapClass;

        // TODO set these to config values
        minDelayMinutes = 1;
        maxDelayMinutes = 2;
        countdownTimer = 10;
        totalLives = 1;

        fallKills = false;
        fireDamage = false;
        netherEnabled = false;
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
        Bukkit.broadcastMessage(DeathSwap.toColorString("&a&lDone! &6Game starts now, good luck."));

        if (!fireDamage) {
            for (Player player : participants) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            }
        }
        scheduleTeleportDelay();
    }

    private long getTeleportDelay() {
        return 20L * (long) (60 * (minDelayMinutes + (Math.random() * (maxDelayMinutes - minDelayMinutes))));
    }

    private void scheduleTeleportDelay() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        teleportDelayTask = scheduler.runTaskLater(deathSwap, () -> {
            teleportCountdown();
        }, getTeleportDelay());
    }

    public boolean getGameActive() {
        return teleportDelayTask != null;
    }

    public void stopGame() throws IllegalStateException {
        if (getGameActive()) {
            teleportDelayTask.cancel();
            teleportDelayTask = null;
        } else {
            throw new IllegalStateException("Game is not active.");
        }
    }

    private void teleportCountdown() {
        currentTimer = countdownTimer;
        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(deathSwap, task -> {
            if (currentTimer > 0 && getGameActive()) {
                // use different color codes depending on the timer
                char colorCode;
                if (currentTimer >= 6) {
                    colorCode = '2';
                } else if (currentTimer == 5) {
                    colorCode = 'a';
                } else if (currentTimer == 4) {
                    colorCode = 'e';
                } else if (currentTimer == 3) {
                    colorCode = '6';
                } else if (currentTimer == 2) {
                    colorCode = 'c';
                } else {
                    colorCode = '4';
                }

                for (Player player : deathSwap.getServer().getOnlinePlayers()) {
                    player.sendTitle(" ", DeathSwap.toColorString("&" + colorCode + currentTimer), 5, 40, 0);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
                }

                currentTimer--;
            } else {
                if (getGameActive()) {
                    swapPlayers();
                    for (Player player : deathSwap.getServer().getOnlinePlayers()) {
                        player.sendTitle(" ", DeathSwap.toColorString("&6" + "Swap!"), 5, 40, 0);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100, 1);
                    }
                }
                
                task.cancel();
            }
        }, 0L, 20L);
    }

    public void checkGameOver() {
        if (participants.size() <= 1) {
            Bukkit.broadcastMessage(DeathSwap.toColorString("&c&lGame over! &6" + participants.get(0).getName() + " &c&lwon!"));
            for (Player player : deathSwap.getServer().getOnlinePlayers()) {
                player.sendTitle(DeathSwap.toColorString("&c&lGame over!"), DeathSwap.toColorString("&6" + participants.get(0).getName() + " &c&lwon!"), 5, 20 * 5, 5);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.2f);
            }
            stopGame();
        }
    }

    // Swaps all players with each other
    private void swapPlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<>(participants);

        Collections.shuffle(alivePlayers);

        Location firstPlayerLocation = alivePlayers.get(0).getLocation();
        String firstPlayerName = alivePlayers.get(0).getName();

        Bukkit.broadcastMessage(DeathSwap.toColorString("&6&lSwap!"));
        for (int i = 0; i < alivePlayers.size(); i++) {
            Player player = alivePlayers.get(i);
            String swapMessage;

            if (i == alivePlayers.size() - 1) {
                player.teleport(firstPlayerLocation);
                swapMessage = "&6&o" + player.getName() + " &e&o-> &6&o" + firstPlayerName;
            }
            else {
                Player nextPlayer = alivePlayers.get(i + 1);

                player.teleport(nextPlayer.getLocation());
                swapMessage = "&6&o" + player.getName() + " &e&o-> &6&o" + nextPlayer.getName();
            }

            Bukkit.broadcastMessage(DeathSwap.toColorString(swapMessage));

            player.setFallDistance(0);
            
            if (!fallKills) {
                fallImmune.add(player);
            }
        }

        scheduleTeleportDelay();
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
    private void spawnPlayers() {

        World world = Bukkit.getWorld("world");

        for (Player player : participants) {
            int xCoord = (int) Math.floor(Math.random() * (HIGHEST_TP_COORD - LOWEST_TP_COORD) + LOWEST_TP_COORD);
            int zCoord = (int) Math.floor(Math.random() * (HIGHEST_TP_COORD - LOWEST_TP_COORD) + LOWEST_TP_COORD);

            int yCoord = world.getHighestBlockYAt(xCoord, zCoord) + 1;
            Location teleportLocation = new Location(world, xCoord + 0.5, yCoord, zCoord + 0.5);
            
            // avoids teleporting into an ocean
            while (teleportLocation.getBlock().getBiome().toString().contains("OCEAN")) {
                xCoord += 100;
                yCoord = world.getHighestBlockYAt(xCoord, zCoord) + 1;
                teleportLocation = new Location(world, xCoord + 0.5, yCoord, zCoord + 0.5);
            }


            player.teleport(teleportLocation);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.setHealth(20);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.getInventory().clear();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (getGameActive() && participants.contains(event.getPlayer())) {
            participants.remove(event.getPlayer());

            // TODO allow player to be disconnected for maximum of 1 minute as long as before swap
            checkGameOver();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (getGameActive() && participants.contains(player)) {
            participants.remove(player);
            checkGameOver();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (getGameActive()) {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (getGameActive() && !fallKills && fallImmune.contains(player)) {
            player.setFallDistance(0);

            // check if the player is on the ground
            if (((Entity) player).isOnGround()) {
                fallImmune.remove(player);
            }
        }
    }

    @EventHandler()
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        // keep fire res on player if fire damage is off
        if (getGameActive() && !fireDamage && event.getItem().getType().equals(Material.MILK_BUCKET) && participants.contains(player)) {
            BukkitScheduler scheduler = Bukkit.getScheduler();

            // add fire resistance back 1 tick after
            scheduler.runTaskLater(deathSwap, () -> {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            }, 1L);
        }
    }

    @EventHandler()
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        if (getGameActive() && !netherEnabled && participants.contains(player) && event.getTo().getWorld().getEnvironment().equals(Environment.NETHER)) {
            event.setCancelled(true);
            player.sendMessage(DeathSwap.toColorString("&cThe nether is disabled during the game. &7&oUse &8&o/deathswap nether on &7&oto enable it."));
        }
    }
}
