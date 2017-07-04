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
import nl.rens4000.bountyhunters.utils.CommandUtils;

public class Main extends JavaPlugin {
	
	public static final String PREFIX = ChatColor.GOLD + "Bounty" + ChatColor.AQUA + "Hunters " + ChatColor.RESET;
	public static final String NOPERM = PREFIX + ChatColor.RED + "You don't have permissions for that command!";
	public static final String NOPLAYER = PREFIX + ChatColor.RED + "You need to be a player to perform that command!";
	
	private static Main instance;
	private static ConfigManager configManager;
	private static ArenaManager am;
	private static CommandUtils cu;

	public static Main getInstance() {
		return instance;
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static ArenaManager getArenaManager() {
		return am;
	}
	
	public static CommandUtils getCommandUtils() {
		return cu;
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
		this.cu = new CommandUtils();
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
				cu.mainCommandMessage(sender);
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				cu.helpCommand(sender);
			}
			if(args[0].equalsIgnoreCase("create")) {
				if(cu.createCommand(sender, args)) sender.sendMessage(PREFIX + "ARENA HAS BEEN CREATED AND REGISTERED");
			}
			if(args[0].equalsIgnoreCase("remove")) {
				if(cu.removeCommand(sender, args)) sender.sendMessage(PREFIX + "Arena has been removed!");
			}
			if(args[0].equalsIgnoreCase("setspawn")) {
				if(cu.setSpawnCommand(sender, args)) sender.sendMessage(PREFIX + "Spawn for: " + args[1] + " has been set!");
			}
			if(args[0].equalsIgnoreCase("selectkit")) {
				if(cu.selectKitCommand(sender, args)) sender.sendMessage(Main.PREFIX + "You choose the kit: " + args[1]);
			}
			if(args[0].equalsIgnoreCase("setlobby")) {
				if(cu.setLobby(sender, args)) sender.sendMessage(PREFIX + "Lobby for: " + args[1] + " has been set!");		
			}
			if(args[0].equalsIgnoreCase("join")) {
				if(cu.joinGame(sender, args)) sender.sendMessage(Main.PREFIX + ChatColor.GOLD + "Joined game!");
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
				if(cu.toggle(sender, args)) sender.sendMessage(PREFIX + "Toggled the state");
			}
			if(args[0].equalsIgnoreCase("setmin")) {
				if(cu.setMin(sender, args))
				sender.sendMessage(PREFIX + "Minimum players has been set to: " + args[2]);
			}
			if(args[0].equalsIgnoreCase("setmax")) {
				if(cu.setMax(sender, args))
					sender.sendMessage(Main.PREFIX + "Maximum players has been set to: " + args[2]);
			}
		}
		return false;
	}

}
