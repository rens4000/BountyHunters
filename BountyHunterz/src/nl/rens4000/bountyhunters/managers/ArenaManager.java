package nl.rens4000.bountyhunters.managers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import nl.rens4000.bountyhunters.Main;
import nl.rens4000.bountyhunters.game.Arena;

public class ArenaManager {
	
	public  ArrayList<Arena> arenas = new ArrayList<>();
	
	private  FileConfiguration data;
	
	private  ArenaManager am;
	
	private Main main;
	
	public ArenaManager(Main main) {
		this.main = main;
		data = main.getConfigManager().getDataFile();
		am = this;
	}
	
	public  Arena getArena(Player p) {
		for(Arena a : arenas) {
			if(a.inGame(p)) {
				return a;
			}
		}
		return null;
	}
	
	public void setSign(Location loc, String name) {
		data.set("arenas." + name + ".sign.world", loc.getWorld().getName());
		data.set("arenas." + name + ".sign.x", loc.getBlockX());
		data.set("arenas." + name + ".sign.y", loc.getBlockY());
		data.set("arenas." + name + ".sign.z", loc.getBlockZ());
		main.getConfigManager().save();
	}
	
	public void removeSign(String name) {
		data.set("arenas." + name + ".sign.world", null);
		data.set("arenas." + name + ".sign.x", null);
		data.set("arenas." + name + ".sign.y", null);
		data.set("arenas." + name + ".sign.z", null);
		main.getConfigManager().save();
	}
	
	public Location getSign(String name) {
		return new Location(Bukkit.getWorld(data.getString("arenas." + name + ".sign.world")), data.getInt("arenas." + name + ".sign.x"), data.getInt("arenas." + name + ".sign.y"), data.getInt("arenas." + name + ".sign.z"));
	}
	
	public boolean signCreated(String name) {
		return data.contains("arenas." + name + ".sign");
	}
	
	public void setSpawn(Arena a, Location loc) {
		a.setSpawn(loc);
		saveArenas();
	}
	
	public Arena getArena(String name) {
		for(Arena a : arenas) {
			if(a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	
	public  boolean inGame(Player p) {
		for(Arena a : arenas) {
			if(a.getPlayers().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public  boolean exists(String name) {
		Arena a = am.getArena(name);
		return arenas.contains(a);
	}
	
	public void createArena(String name) {
		Arena a = new Arena(name, 2, 8, false, null, null, main);
		arenas.add(a);
		saveArenas();
	}
	
	public void removeArena(String name) {
		Arena a = getArena(name);
		if(a.getSpawn() != null) {
			data.set("Arenas." + a.getName() + ".spawn.world", null);
			data.set("Arenas." + a.getName() + ".spawn.x", null);
			data.set("Arenas." + a.getName() + ".spawn.y", null);
			data.set("Arenas." + a.getName() + ".spawn.z", null);
		}
		if(a.getLobby() != null) {
			data.set("Arenas." + a.getName() + ".lobby.world", null);
			data.set("Arenas." + a.getName() + ".lobby.x", null);
			data.set("Arenas." + a.getName() + ".lobby.y", null);
			data.set("Arenas." + a.getName() + ".lobby.z", null);
		}
		data.set("Arenas." + a.getName() + ".min", null);
		data.set("Arenas." + a.getName() + ".max", null);
		data.set("Arenas." + a.getName() + ".enabled", null);
		main.getConfigManager().save();
		arenas.remove(a);
	}
	
	public  void loadArenas() {
		if(!data.contains("Arenas"))
			return;
		
		for(String key : data.getConfigurationSection("Arenas").getKeys(false)) {
			if(data.getString("Arenas." + key + ".lobby.world") != null) {
				Location lobbyLoc = new Location(Bukkit.getWorld(data.getString("Arenas." + key + ".lobby.world")), data.getInt("Arenas." + key + ".lobby.x"), data.getInt("Arenas." + key + ".lobby.y"), data.getInt("Arenas." + key + ".lobby.z"));
				if(data.getString("Arenas." + key + ".spawn.world") != null) {
					Location spawnLoc = new Location(Bukkit.getWorld(data.getString("Arenas." + key + ".spawn.world")), data.getInt("Arenas." + key + ".spawn.x"), data.getInt("Arenas." + key + ".spawn.y"), data.getInt("Arenas." + key + ".spawn.z"));
					Arena arena = new Arena(key, data.getInt("Arenas." + key + ".min"), data.getInt("Arenas." + key + ".max"), data.getBoolean("Arenas." + key + ".enabled"), lobbyLoc, spawnLoc, main);
					arenas.add(arena);
					return;
				}
			}
			Arena arena = new Arena(key, data.getInt("Arenas." + key + ".min"), data.getInt("Arenas." + key + ".max"), data.getBoolean("Arenas." + key + ".enabled"), null, null, main);
			arenas.add(arena);
			Bukkit.getConsoleSender().sendMessage(main.PREFIX + "Loaded arena: " + arena.getName());
		}
}

	public void saveArenas() {
		for(Arena a : arenas) {
			if(a.getSpawn() != null) {
				data.set("Arenas." + a.getName() + ".spawn.world", a.getSpawn().getWorld().getName());
				data.set("Arenas." + a.getName() + ".spawn.x", a.getSpawn().getX());
				data.set("Arenas." + a.getName() + ".spawn.y", a.getSpawn().getY());
				data.set("Arenas." + a.getName() + ".spawn.z", a.getSpawn().getZ());
			}
			if(a.getLobby() != null) {
				data.set("Arenas." + a.getName() + ".lobby.world", a.getLobby().getWorld().getName());
				data.set("Arenas." + a.getName() + ".lobby.x", a.getLobby().getX());
				data.set("Arenas." + a.getName() + ".lobby.y", a.getLobby().getY());
				data.set("Arenas." + a.getName() + ".lobby.z", a.getLobby().getZ());
			}
			data.set("Arenas." + a.getName() + ".min", a.getMin());
			data.set("Arenas." + a.getName() + ".max", a.getMax());
			data.set("Arenas." + a.getName() + ".enabled", a.isEnabled());
			main.getConfigManager().save();
		}
	}

	public void setLobby(Arena a, Location loc) {
		a.setLobby(loc);
		saveArenas();
	}
	
	public boolean join(String name, Player p) {
		Arena arena = am.getArena(name);
		
		if(arena == null) return false;
		
		return arena.join(p);
}

	public boolean ArenaReady(String arena) {
		Location spawn = getArena(arena).getSpawn();
		Location lobby = getArena(arena).getLobby();
		return spawn != null && lobby != null;
	}

	public void setEnabled(String name, boolean enabled) {
		data.set("Arenas." + name + ".enabled", enabled);
		Arena arena = getArena(name);
		arena.setEnabled(enabled);
		for (int i = 0; i < arena.getPlayers().size(); i++) {
			Player p = Bukkit.getPlayer(arena.getPlayers().get(i));
			p.sendMessage(main.PREFIX + ChatColor.RED + "This arena has been changed by an admin.");
			arena.leave(p);
		}
		main.getConfigManager().save();
		
	}

}
