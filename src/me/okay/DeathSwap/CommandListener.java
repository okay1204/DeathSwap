package me.okay.DeathSwap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CommandListener implements CommandExecutor, TabCompleter {
    private DeathSwap deathSwap;
    private String helpMessage;
    private Map<String, String> commands = new TreeMap<String, String>();

    public CommandListener(DeathSwap deathSwapClass) {
        deathSwap = deathSwapClass;

        // setting helpMessage
        commands.put("start", "Starts the game of DeathSwap, where everyone currently in survival mode will be part of the game.");
        commands.put("stop", "Ends the game of DeathSwap.");
        commands.put("swaptime <minimum> <maximum>", "Sets the amount of minutes between swaps. The time will be a random time between the selected range.");
        commands.put("countdown <seconds>", "Sets the amount of seconds the countdown timer will tick down before a swap.");
        commands.put("fallkills <on|off>", "If off, players will be immune to fall damage after a swap until they hit the ground.");
        commands.put("firedamage <on|off>", "If on, players will have permanent fire resistance.");
        commands.put("allownether <on|off>", "If off, players will not be able to travel to the nether.");
        commands.put("lives <amount>", "Sets the amount of lives each player has before they are eliminated.");
        commands.put("settings", "Displays all settings currently configured, as well as defaults.");
        commands.put("help", "Displays this help menu.");
        
        helpMessage = "&7------[&6Deathswap&7]------\n";
    
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            helpMessage += "&7- &6/deathswap " + entry.getKey() + " &7- " + entry.getValue() + "\n";
        }

        helpMessage = DeathSwap.toColorString(helpMessage);
    }

    private String usageMessage(String arg1) throws NoSuchElementException {
        for (String fullCommand : commands.keySet()) {
            String command = fullCommand.split(" ")[0];

            if (command.equalsIgnoreCase(arg1)) {
                return DeathSwap.toColorString("&cUsage: /deathswap " + fullCommand);
            }
        }

        throw new NoSuchElementException("Command not found.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Game game = deathSwap.getGame();

        if (args.length == 0) {
            sender.sendMessage(helpMessage);
        }
        else if (args[0].equalsIgnoreCase("start")) {
            if (args.length == 1) {
                ArrayList<Player> players = game.getSurvivalPlayers();
                
                if (game.getGameActive()) {
                    sender.sendMessage(DeathSwap.toColorString("&cThe game is already active."));
                } else if (players.size() < 2) {
                    sender.sendMessage(DeathSwap.toColorString("&cAt least 2 players are required to start the game. Make sure participants are in survival!"));
                } else {
                    game.startGame(players);
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("stop")) {
            if (args.length == 1) {
                if (game.getGameActive()) {
                    game.stopGame();
                } else {
                    sender.sendMessage(DeathSwap.toColorString("&cThe game is not active."));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("swaptime")) {
            if (args.length == 3) {
                try {
                    int min = Integer.parseInt(args[1]);
                    int max = Integer.parseInt(args[2]);

                    if (min < 0 || max < 0) {
                        sender.sendMessage(DeathSwap.toColorString("&cThe minimum and maximum swap times must be positive integers."));
                    } else if (min > max) {
                        sender.sendMessage(DeathSwap.toColorString("&cThe minimum swap time must be less than the maximum swap time."));
                    } else {
                        game.setSwapTime(min, max);
                        sender.sendMessage(DeathSwap.toColorString("&aSet minimum time to &2" + min + "&a and maximum time to &2" + max + "&a."));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(DeathSwap.toColorString("&cThe minimum and maximum swap times must be positive integers."));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("countdown")) {
            if (args.length == 2) {
                try {
                    int seconds = Integer.parseInt(args[1]);

                    if (seconds < 0) {
                        sender.sendMessage(DeathSwap.toColorString("&cThe countdown timer must be a positive integer."));
                    } else {
                        game.setCountdownTimer(seconds);
                        sender.sendMessage(DeathSwap.toColorString("&aSet countdown timer to &2" + seconds + "&a."));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(DeathSwap.toColorString("&cThe countdown timer must be a positive integer."));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("fallkills")) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("on")) {
                    game.setFallKills(true);
                    sender.sendMessage(DeathSwap.toColorString("&aFall kills are now &2enabled&a."));
                } else if (args[1].equalsIgnoreCase("off")) {
                    game.setFallKills(false);
                    sender.sendMessage(DeathSwap.toColorString("&aFall kills are now &cdisabled&a."));
                } else {
                    sender.sendMessage(usageMessage(args[0]));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("firedamage")) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("on")) {
                    game.setFireDamage(true);
                    sender.sendMessage(DeathSwap.toColorString("&aFire damage is now &2enabled&a."));
                } else if (args[1].equalsIgnoreCase("off")) {
                    game.setFireDamage(false);
                    sender.sendMessage(DeathSwap.toColorString("&aFire damage is now &cdisabled&a."));
                } else {
                    sender.sendMessage(usageMessage(args[0]));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("allownether")) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("on")) {
                    game.setNetherEnabled(true);
                    sender.sendMessage(DeathSwap.toColorString("&aNether is now &2enabled&a."));
                } else if (args[1].equalsIgnoreCase("off")) {
                    game.setNetherEnabled(false);
                    sender.sendMessage(DeathSwap.toColorString("&aNether is now &cdisabled&a."));
                } else {
                    sender.sendMessage(usageMessage(args[0]));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("lives")) {
            if (args.length == 2) {
                try {
                    int lives = Integer.parseInt(args[1]);

                    if (lives < 0) {
                        sender.sendMessage(DeathSwap.toColorString("&cThe amount of lives must be a positive integer."));
                    } else {
                        game.setLives(lives);
                        sender.sendMessage(DeathSwap.toColorString("&aSet amount of lives to &2" + lives + "&a."));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(DeathSwap.toColorString("&cThe amount of lives must be a positive integer."));
                }
            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else if (args[0].equalsIgnoreCase("settings")) {
            if (args.length == 1) {
                sender.sendMessage(DeathSwap.toColorString("&aSettings:"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aSwap Time:&2 " + game.getMinDelayMinutes() + " &ato &2" + game.getMaxDelayMinutes() + " &aminutes &7(Default: 5 to 10 minutes)"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aCountdown Timer:&2 " + game.getCountdownTimer() + " &aseconds &7(Default: 10 seconds)"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aFall Kills: " + (game.getFallKills() ? "&2enabled" : "&cdisabled") + " &7(Default: enabled)"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aNether: " + (game.getNetherEnabled() ? "&2enabled" : "&cdisabled") + " &7(Default: enabled)"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aFire Damage: " + (game.getFireDamage() ? "&2enabled" : "&cdisabled") + " &7(Default: enabled)"));
                sender.sendMessage(DeathSwap.toColorString("&7- &aLives: &2" + game.getLives() + " &alives &7(Default: 1 life)"));

            } else {
                sender.sendMessage(usageMessage(args[0]));
            }
        }
        else {
            sender.sendMessage(helpMessage);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("start", "stop", "stop", "swaptime", "countdown", "fallkills", "firedamage", "allownether", "lives", "settings", "help"), new ArrayList<>());
        } else if (args.length == 2) {
            if (List.of("fallkills", "firedamage", "allownether").contains(args[0])) {
                return StringUtil.copyPartialMatches(args[1], List.of("on", "off"), new ArrayList<>());
            }
        }

        return new ArrayList<>();
    }
}
