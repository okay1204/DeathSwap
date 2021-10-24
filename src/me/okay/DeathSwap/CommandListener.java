package me.okay.DeathSwap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CommandListener implements CommandExecutor, TabCompleter {
    private Main main;
    private String helpMessage;

    public CommandListener(Main mainClass) {
        main = mainClass;
        main.getCommand("manhunt").setExecutor(this);

        // setting helpMessage
        Map<String, String> commands = new HashMap<>();
        commands.put("start", "Starts the game of DeathSwap, where everyone currently in survival mode will be part of the game.");
        commands.put("stop", "Ends the game of DeathSwap");
        commands.put("settings swaptime <number>-<number>", "Sets the amount of time between swaps. The time will be a random time between the selected range.");
        commands.put("settings fallkills <on|off>", "If on, players will be immune to fall damage after a teleport until they hit the ground.");
        commands.put("settings lives <number>", "Sets the amount of lives each player has before they are eliminated.");
        commands.put("help", "Displays this help menu.");
        
        helpMessage = "&7------[&6Deathswap&7]------\n";
    
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            helpMessage += "&7- &6/manhunt " + entry.getKey() + " &7- " + entry.getValue() + ".\n";
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(helpMessage);
        }
        else if (args[0].equalsIgnoreCase("start")) {
            ArrayList<Player> players = new ArrayList<>();

            for (Player player : main.getServer().getOnlinePlayers()) {
                if (player.getGameMode() == org.bukkit.GameMode.SURVIVAL) {
                    players.add(player);
                }
            }

            if (players.size() > 1) {
                // NOTE start game here
            }
            else {
                sender.sendMessage("&cThere are not enough players to start the game. Make sure participants are in survival!");
            }
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("start", "stop", "stop", "settings", "help"), new ArrayList<>());
        }

        return null;
    }
}