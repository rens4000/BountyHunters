package nl.rens4000.bountyhunters.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.rens4000.bountyhunters.Main;
import nl.rens4000.bountyhunters.managers.ArenaManager;

public class CommandUtils {
	
	public CommandUtils() {  }
	
	public final void mainCommandMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "+---------=BountyHunters=---------+");
		sender.sendMessage(ChatColor.GOLD + "BountyHunters");
		sender.sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "rens4000");
		sender.sendMessage(ChatColor.GOLD + "Do: /bh help for the command list.");
		sender.sendMessage(ChatColor.AQUA + "+---------------------------------+");
	}
	
	public final void helpCommand(CommandSender sender) {
		if(sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(ChatColor.AQUA + "+----------=Commands=----------+");
			sender.sendMessage(ChatColor.GOLD + "/bh" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Main command of the minigame.");
			sender.sendMessage(ChatColor.GOLD + "/bh create <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Create an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh remove <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Remove an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh setspawn <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the spawn of the arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh setlobby <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the lobby of the arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh setmainlobby" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the main lobby spawn of the server.");
			sender.sendMessage(ChatColor.GOLD + "/bh toggle <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Toggle the arena to enabled or disabled.");
			sender.sendMessage(ChatColor.GOLD + "/bh join <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Join an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh leave" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Leave an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh setmin <name> <min-players>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the minimum players.");
			sender.sendMessage(ChatColor.GOLD + "/bh setmax <name> <max-players>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the maximum players.");
		} else {
			sender.sendMessage(ChatColor.AQUA + "+----------=Commands=----------+");
			sender.sendMessage(ChatColor.GOLD + "/bh join <name>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Join an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh leave" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Leave an arena.");
			sender.sendMessage(ChatColor.GOLD + "/bh selectkit <kit>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Select a kit for the game!");

		}
	}
	
	public final boolean createCommand(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(Main.NOPERM);
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh create <name>"); 
			return true;
		}
		if(ArenaManager.exists(args[1])) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Arena does already exists!");
			return true;
		}
		Main.getArenaManager().createArena(args[1]);
		sender.sendMessage(Main.PREFIX + ChatColor.GREEN + "You've successfully created an arena!");
		sender.sendMessage(Main.PREFIX + ChatColor.AQUA + "Things you can do now: ");
		sender.sendMessage(Main.PREFIX + ChatColor.GOLD + "/bh setspawn " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the spawn of the arena.");
		sender.sendMessage(Main.PREFIX + ChatColor.GOLD + "/bh setlobby " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the lobby of the arena.");
		sender.sendMessage(ChatColor.GOLD + "/bh toggle " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Toggle the arena to enabled or disabled.");
		return true;
	}
	
	public final boolean removeCommand(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(Main.NOPERM);
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh remove <name>"); 
			return true;
		}
		if(!ArenaManager.exists(args[1])) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Arena doesn't exists!");
			return true;
		}
		if(Main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return true;
		}
		Main.getArenaManager().removeArena(args[1]);
		return true;
	}
	
	public final boolean setSpawnCommand(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(Main.NOPERM);
			return true;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.NOPLAYER);
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setspawn <name>"); 
			return true;
		}
		if(!ArenaManager.exists(args[1])) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "Arena doesn't exists!");
			return true;
		}
		if(Main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return true;
		}
		Player p = (Player) sender;
		Main.getArenaManager().setSpawn(Main.getArenaManager().getArena(args[1]), p.getLocation());
		return true;
	}

}
