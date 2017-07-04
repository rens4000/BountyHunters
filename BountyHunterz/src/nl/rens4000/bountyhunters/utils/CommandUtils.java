package nl.rens4000.bountyhunters.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.primitives.Ints;

import nl.rens4000.bountyhunters.Main;


public class CommandUtils {
	
	private final Main main;
	
	public CommandUtils(Main main) { 
		this.main = main;
	}
	
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
			sender.sendMessage(ChatColor.GOLD + "/bh" + ChatColor.GRAY + " - " + ChatColor.AQUA + "main command of the minigame.");
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
			sender.sendMessage(main.getInstance().NOPERM);
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh create <name>"); 
			return false;
		}
		if(main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Arena does already exists!");
			return false;
		}
		main.getArenaManager().createArena(args[1]);
		sender.sendMessage(main.PREFIX + ChatColor.GREEN + "You've successfully created an arena!");
		sender.sendMessage(main.PREFIX + ChatColor.AQUA + "Things you can do now: ");
		sender.sendMessage(main.PREFIX + ChatColor.GOLD + "/bh setspawn " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the spawn of the arena.");
		sender.sendMessage(main.PREFIX + ChatColor.GOLD + "/bh setlobby " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the lobby of the arena.");
		sender.sendMessage(ChatColor.GOLD + "/bh toggle " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Toggle the arena to enabled or disabled.");
		return true;
	}
	
	public final boolean removeCommand(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh remove <name>"); 
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Arena doesn't exists!");
			return false;
		}
		if(main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return false;
		}
		main.getArenaManager().removeArena(args[1]);
		return true;
	}
	
	public final boolean setSpawnCommand(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.NOPLAYER);
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setspawn <name>"); 
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Arena doesn't exists!");
			return false;
		}
		if(main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return false;
		}
		Player p = (Player) sender;
		main.getArenaManager().setSpawn(main.getArenaManager().getArena(args[1]), p.getLocation());
		return true;
	}
	
	public final boolean selectKitCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.NOPLAYER);
			return false;
		}
		Player p = (Player) sender;
		if(!main.getArenaManager().inGame(p)) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "You are not in a game!");
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh selectkit <kit>"); 
			return false;
		}
		if(!args[1].equals("default") || !args[1].equals("warrior")) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That kit is incorrect. Kits you can choose from: default and warrior");
			return false;
		}
		main.getArenaManager().getArena(p).setKit(p, args[1]);
		
		return true;
	}
	
	public final boolean setLobby(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.NOPLAYER);
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setlobby <name>"); 
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Arena doesn't exists!");
			return false;
		}
		if(main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return false;
		}
		Player p = (Player) sender;
		main.getArenaManager().setLobby(main.getArenaManager().getArena(args[1]), p.getLocation());
		return true;
	}
	
	public final boolean joinGame(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.NOPLAYER);
			return false;
		}
		Player p = (Player) sender;
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh join <name>");
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That arena doesn't exists!");
			return false;
		}
		if(main.getArenaManager().inGame(p)) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "You are already in a game!");
			return false;
		}
		if(!main.getArenaManager().join(args[1], p)) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "This game is currently not joinable.");
			return false;
		} 
		return true;
	}
	
	public final boolean toggle(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(args.length < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh toggle <name>");
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That arena doesn't exists!");
			return false;
		}
		if(!main.getConfigManager().mainLobbySet()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "There isn't a main lobby!");
			return false;
		}
		if(!main.getArenaManager().getArena(args[1]).isEnabled()) {
			if(!main.getArenaManager().ArenaReady(args[1])) {
				sender.sendMessage(main.PREFIX + ChatColor.RED + "That arena isn't ready yet!");
				return false;
			}
			main.getArenaManager().setEnabled(args[1], true);
			sender.sendMessage(main.PREFIX + args[1] + " has been set to enabled!");
			return true;
		} else {
			main.getArenaManager().setEnabled(args[1], false);
			sender.sendMessage(main.PREFIX + args[1] + " has been set to disabled!");
			return true;
		}
	}
	
	public final boolean setMin(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(args.length < 3) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setmin <name> <min-players>");
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That arena doesn't exists!");
			return false;
		}
		
		if(!isInteger(args[2])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That's not a number!");
			return false;
		}
		final Integer min = Ints.tryParse(args[2]);
		if(main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return false;
		}
		if(min < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The min has to be atleast 2"); 
			return false;
		}
		main.getArenaManager().getArena(args[1]).setMin(min);
		main.getConfigManager().setMin(args[1], min);
		return true;
	}
	
	public final boolean setMax(CommandSender sender, String[] args) {
		if(!sender.hasPermission("BountyHunters.Admin")) {
			sender.sendMessage(main.NOPERM);
			return false;
		}
		if(args.length < 3) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setmax <name> <max-players>");
			return false;
		}
		if(!main.getArenaManager().exists(args[1])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That arena doesn't exists!");
			return false;
		}
		if(!isInteger(args[2])) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "That's not a number!");
			return false;
		}
		if(main.getArenaManager().getArena(args[1]).isEnabled()) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
			return false;
		}
		if(Integer.parseInt(args[2]) < 2) {
			sender.sendMessage(main.PREFIX + ChatColor.RED + "The max has to be atleast 2"); 
			return false;
		}
		main.getArenaManager().getArena(args[0]).setMax(Integer.parseInt(args[2]));
		main.getConfigManager().setMax(args[1], Integer.parseInt(args[2]));
		return true;
	}
	
	public static boolean isInteger(String min) {
		return Ints.tryParse(min) != null;
	}

}
