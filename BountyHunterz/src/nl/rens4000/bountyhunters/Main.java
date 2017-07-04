package nl.rens4000.bountyhunters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import nl.rens4000.bountyhunters.managers.ArenaManager;
import nl.rens4000.bountyhunters.managers.ConfigManager;

public class Main extends JavaPlugin {
	
	public static String PREFIX = ChatColor.GOLD + "Bounty" + ChatColor.AQUA + "Hunters " + ChatColor.RESET;
	public static String NOPERM = PREFIX + ChatColor.RED + "You don't have permissions for that command!";
	public static String NOPLAYER = PREFIX + ChatColor.RED + "You need to be a player to perform that command!";
	
	private static Main instance;
	private static ConfigManager configManager;
	private static ArenaManager am;

	public static Main getInstance() {
		return instance;
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static ArenaManager getArenaManager() {
		return am;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "+---------=BountyHunter=---------+");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "rens4000");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Version: 1.0");
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "+------------------------------+");
		this.instance = this;
		this.configManager = new ConfigManager();
		this.am = new ArenaManager();
		am.loadArenas();
		pm.registerEvents(new Events(), this);
	}
	
	@Override
	public void onDisable() {
		am.saveArenas();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("bh")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "+---------=BountyHunters=---------+");
				sender.sendMessage(ChatColor.GOLD + "BountyHunters");
				sender.sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "rens4000");
				sender.sendMessage(ChatColor.GOLD + "Do: /bh help for the command list.");
				sender.sendMessage(ChatColor.AQUA + "+---------------------------------+");
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
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
			if(args[0].equalsIgnoreCase("create")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh create <name>"); 
					return true;
				}
				if(ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Arena does already exists!");
					return true;
				}
				am.createArena(args[1]);
				sender.sendMessage(PREFIX + ChatColor.GREEN + "You've successfully created an arena!");
				sender.sendMessage(PREFIX + ChatColor.AQUA + "Things you can do now: ");
				sender.sendMessage(PREFIX + ChatColor.GOLD + "/bh setspawn " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the spawn of the arena.");
				sender.sendMessage(PREFIX + ChatColor.GOLD + "/bh setlobby " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Set the lobby of the arena.");
				sender.sendMessage(ChatColor.GOLD + "/bh toggle " + args[1] + " " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Toggle the arena to enabled or disabled.");
			}
			if(args[0].equalsIgnoreCase("remove")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh remove <name>"); 
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Arena doesn't exists!");
					return true;
				}
				if(am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
					return true;
				}
				am.removeArena(args[1]);
				sender.sendMessage(PREFIX + "Arena has been removed!");
			}
			if(args[0].equalsIgnoreCase("setspawn")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setspawn <name>"); 
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Arena doesn't exists!");
					return true;
				}
				if(am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
					return true;
				}
				Player p = (Player) sender;
				am.setSpawn(am.getArena(args[1]), p.getLocation());
				p.sendMessage(PREFIX + "Spawn for: " + args[1] + " has been set!");
			}
			if(args[0].equalsIgnoreCase("selectkit")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				Player p = (Player) sender;
				if(!ArenaManager.inGame(p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You are not in a game!");
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh selectkit <kit>"); 
					return true;
				}
				if(!args[1].equals("default") || !args[1].equals("warrior")) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That kit is incorrect. Kits you can choose from: default and warrior");
					return true;
				}
				ArenaManager.getArena(p).setKit(p, args[1]);
				p.sendMessage(PREFIX + "You choose the kit: " + args[1]);
				
			}
			if(args[0].equalsIgnoreCase("forcestart")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setlobby <name>"); 
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Arena doesn't exists!");
					return true;
				}
				if(!am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be enabled for this action!");
					return true;
				}
				getArenaManager().getArena(args[1]).start();
				sender.sendMessage(PREFIX + "Arena has been force started!");
			}
			if(args[0].equalsIgnoreCase("setlobby")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setlobby <name>"); 
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Arena doesn't exists!");
					return true;
				}
				if(am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
					return true;
				}
				Player p = (Player) sender;
				am.setLobby(am.getArena(args[1]), p.getLocation());
				p.sendMessage(PREFIX + "Lobby for: " + args[1] + " has been set!");
			}
			if(args[0].equalsIgnoreCase("join")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				Player p = (Player) sender;
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh join <name>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena doesn't exists!");
					return true;
				}
				if(ArenaManager.inGame(p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You are already in a game!");
					return true;
				}
				if(!ArenaManager.join(args[1], p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "This game is currently not joinable.");
				} else {
					sender.sendMessage(PREFIX + ChatColor.GOLD + "Joined game!");
				}
			}
			if(args[0].equalsIgnoreCase("leave")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				Player p = (Player) sender;
				if(!ArenaManager.inGame(p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You are not in a game!");
					return true;
				}
				ArenaManager.getArena(p).leave(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("setmainlobby")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				Player p = (Player) sender;
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				configManager.setMainLobby(p.getLocation());
				p.sendMessage(PREFIX + "Main lobby has been set!");
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh toggle <name>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena doesn't exists!");
					return true;
				}
				if(!configManager.mainLobbySet()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "There isn't a main lobby!");
					return true;
				}
				if(!am.getArena(args[1]).isEnabled()) {
					if(!am.ArenaReady(args[1])) {
						sender.sendMessage(PREFIX + ChatColor.RED + "That arena isn't ready yet!");
						return true;
					}
					am.setEnabled(args[1], true);
					sender.sendMessage(PREFIX + args[1] + " has been set to enabled!");
				} else {
					am.setEnabled(args[1], false);
					sender.sendMessage(PREFIX + args[1] + " has been set to disabled!");

				}
			}
			if(args[0].equalsIgnoreCase("setmin")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 3) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setmin <name> <min-players>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena doesn't exists!");
					return true;
				}
				if(am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
					return true;
				}
				if(Integer.parseInt(args[2]) < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The min has to be atleast 2"); 
					return false;
				}
				am.getArena(args[1]).setMin(Integer.parseInt(args[2]));
				configManager.setMin(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + "Minimum players has been set to: " + args[2]);
			}
			if(args[0].equalsIgnoreCase("setmax")) {
				if(!sender.hasPermission("BountyHunters.Admin")) {
					sender.sendMessage(NOPERM);
					return true;
				}
				if(args.length < 3) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Wrong usage of command! Do: /bh setmax <name> <max-players>");
					return true;
				}
				if(!ArenaManager.exists(args[1])) {
					sender.sendMessage(PREFIX + ChatColor.RED + "That arena doesn't exists!");
					return true;
				}
				if(am.getArena(args[1]).isEnabled()) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The arena needs to be disabled for this action!");
					return true;
				}
				if(Integer.parseInt(args[2]) < 2) {
					sender.sendMessage(PREFIX + ChatColor.RED + "The max has to be atleast 2"); 
					return false;
				}
				am.getArena(args[0]).setMax(Integer.parseInt(args[2]));
				configManager.setMax(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(PREFIX + "Maximum players has been set to: " + args[2]);
			}
		}
		return false;
	}

}
