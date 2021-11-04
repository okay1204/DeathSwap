# DeathSwap (Spigot plugin for version 1.17.1)
This plugin recreates the well-known game of Death Swap, and has a bunch of extra features to make the game more balanced!

This spigot plugin provides the tools needed to play Death Swap with you and your friends. When the game starts, all players will be teleported to completely random locations, millions of blocks away. Then, players can cook food, gear up, and most importantly, put themselves in a dangerous situation. Every so often, all players will swap locations with another player. You can use the swap to kill other players that get teleported to your location. When a player runs out of lives, they lose. The last one standing wins the game!

## Installation
Simply download the .jar file from the releases page and drag and drop it into your Spigot server's plugin folder.

## Setup
Setup is quite easy:
1. **Optional**: Configure any settings you wish to change using `/deathswap <setting> <on|off|number>`. _More information below in the commands section._
2. Next, make sure everyone is ready to start the game and in survival mode. Run `/deathswap start`. This will clear everyone's inventory, health, and hunger and start the game.

## Commands
- `deathswap start`: Starts the game of DeathSwap, where everyone currently in survival mode will be part of the game.
- `deathswap stop`: Ends the game of DeathSwap.
- `deathswap swaptime <minimum> <maximum>`: Sets the amount of minutes between swaps. The time will be a random time between the selected range.
- `deathswap countdown <seconds>`: Sets the amount of seconds the countdown timer will tick down before a swap.
- `deathswap fallkills <on|off>`: If off, players will be immune to fall damage after a swap until they hit the ground.
- `deathswap firedamage <on|off>`: If off, players will have permanent fire resistance.
- `deathswap allownether <on|off>`: If off, players will not be able to travel to the nether.
- `deathswap lives <amount>`: Sets the amount of lives each player has before they are eliminated.
- `deathswap settings`: Displays all settings currently configured, as well as defaults.
- `deathswap help`: Displays this help menu.

_All these commands require the permission `deathswap.setup` to use._

### Config file (nerdy stuff)
You can also change the tracking settings in the config file located at `YourServer/plugins/DeathSwap/config.yml`.
Default config:
```yml
# swapTimer: Random delay time between each swap
# countdownTimer: Number of seconds in the countdown before a swap
# fallKills: If false, players will be immune to fall damage until they hit the ground after a swap
# netherEnabled: If false, players cannot portal to the nether
# fireDamage: If false, players will have permanent fire resistance
# lives: Sets the amount of lives each player has before they lose
swapTimer:
  minMinutes: 5
  maxMinutes: 10
countdownTimer: 10
fallKills: true
netherEnabled: true
fireDamage: true
lives: 1
```

_Note: If settings are changed using the `/deathswap` commands, it will automatically update the config file_


## FAQ

Q: What happens when a player dies when they still have lives remaining?
A: They will keep their inventory and respawn in a new random location.

_More will be added if needed_



**Please reach out to me if you have any issues through the GitHub Issues page or on discord at okay#2996, thank you :)**