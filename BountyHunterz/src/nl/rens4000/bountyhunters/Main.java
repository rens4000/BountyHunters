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
import nl.rens4000.bountyhunters.managers.SignManager;
import nl.rens4000.bountyhunters.utils.CommandUtils;

public class Main extends JavaPlugin {
	
	//The variables of the Main class
	public final String PREFIX = ChatColor.GOLD + "Bounty" + ChatColor.AQUA + "Hunters " + ChatColor.RESET;
	public final String NOPERM = PREFIX + ChatColor.RED + "You don't have permissions for that command!";
	public final String NOPLAYER = PREFIX + ChatColor.RED + "You need to be a player to perform that command!";
	
	private ConfigManager configManager;
	private ArenaManager arenaManager;
	private CommandUtils commandUtils;
	private SignManager signManager;
	
	//Getters
	
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public CommandUtils getCommandUtils() {
		return commandUtils;
	}
	
	public SignManager getSignManager() {
		return signManager;
	}
	
	//Enables the plugin and does certain things
	@Override
	public void onEnable() {
		//Registers the plugin manager
		PluginManager pm = Bukkit.getPluginManager();
		//Says a message
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "+---------=BountyHunter=---------+");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Made by " + ChatColor.DARK_AQUA + "rens4000");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Version: 1.0");
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "+------------------------------+");
		//Initializes some variables
		configManager = new ConfigManager(this);
		arenaManager = new ArenaManager(this);
		commandUtils = new CommandUtils(this);
		signManager = new SignManager(this);
		arenaManager.loadArenas();
		pm.registerEvents(new Events(this), this);
	}
	
	@Override
	public void onDisable() {
		arenaManager.saveArenas();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("bh")) {
			if(args.length == 0) {
				commandUtils.mainCommandMessage(sender);
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				commandUtils.helpCommand(sender);
			} else
			if(args[0].equalsIgnoreCase("create")) {
				if(commandUtils.createCommand(sender, args)) sender.sendMessage(PREFIX + "ARENA HAS BEEN CREATED AND REGISTERED");
			}else
			if(args[0].equalsIgnoreCase("remove")) {
				if(commandUtils.removeCommand(sender, args)) sender.sendMessage(PREFIX + "Arena has been removed!");
			}else
			if(args[0].equalsIgnoreCase("setspawn")) {
				if(commandUtils.setSpawnCommand(sender, args)) sender.sendMessage(PREFIX + "Spawn for: " + args[1] + " has been set!");
			}else
			if(args[0].equalsIgnoreCase("selectkit")) {
				if(commandUtils.selectKitCommand(sender, args)) sender.sendMessage(PREFIX + "You choose the kit: " + args[1]);
			}else
			if(args[0].equalsIgnoreCase("setlobby")) {
				if(commandUtils.setLobby(sender, args)) sender.sendMessage(PREFIX + "Lobby for: " + args[1] + " has been set!");		
			}else
			if(args[0].equalsIgnoreCase("join")) {
				if(commandUtils.joinGame(sender, args)) sender.sendMessage(PREFIX + ChatColor.GOLD + "Joined game!");
			}else
			if(args[0].equalsIgnoreCase("leave")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(NOPLAYER);
					return true;
				}
				Player p = (Player) sender;
				if(!arenaManager.inGame(p)) {
					sender.sendMessage(PREFIX + ChatColor.RED + "You are not in a game!");
					return true;
				}
				arenaManager.getArena(p).leave(p);
				return true;
			}else
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
			}else
			if(args[0].equalsIgnoreCase("toggle")) {
				if(commandUtils.toggle(sender, args)) sender.sendMessage(PREFIX + "Toggled the state");
			}else
			if(args[0].equalsIgnoreCase("setmin")) {
				if(commandUtils.setMin(sender, args))
				sender.sendMessage(PREFIX + "Minimum players has been set to: " + args[2]);
			}else
			if(args[0].equalsIgnoreCase("setmax")) {
				if(commandUtils.setMax(sender, args))
					sender.sendMessage(PREFIX + "Maximum players has been set to: " + args[2]);
			}else {
				sender.sendMessage(PREFIX + "That's not a valid command! Do: /bh help to see which command you can use!");
				return true;
			}
		}
		return false;
	}

}
