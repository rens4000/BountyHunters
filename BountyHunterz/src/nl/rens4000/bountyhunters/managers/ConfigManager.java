package nl.rens4000.bountyhunters.managers;



import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.rens4000.bountyhunters.Main;

public class ConfigManager {
	
	private File configFile;
	private FileConfiguration config;
	
	private File dataFile;
	private FileConfiguration data;
	
	private File scoreFile;
	private FileConfiguration score;
	
	
	public ConfigManager(Main main) {
		main = new Main();
		
		dataFile = new File(main.getDataFolder(), "data.yml");
		data = YamlConfiguration.loadConfiguration(dataFile);
		
		configFile = new File(main.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		scoreFile = new File(main.getDataFolder(), "scores.yml");
		score = YamlConfiguration.loadConfiguration(scoreFile);
	}
	
	public void save() {
		try {
			config.save(configFile);
			data.save(dataFile);
			score.save(scoreFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getConfig() {
		return configFile;
	}
	
	public File getData() {
		return dataFile;
	}
	
	public File getScore() {
		return scoreFile;
	}
	
	public FileConfiguration getConfigFile() {
		return config;
	}
	
	public FileConfiguration getDataFile() {
		return data;
	}
	
	public FileConfiguration getScoreFile() {
		return score;
	}
	
	public void setMin(String arena, int min) {
		data.set("Arenas." + arena + ".min", min);
		save();
	}

	public void setMainLobby(Location loc) {
		data.set("lobby.world", loc.getWorld().getName());
		data.set("lobby.x", loc.getBlockX());
		data.set("lobby.y", loc.getBlockY());
		data.set("lobby.z", loc.getBlockZ());
		save();
	}
	
	public boolean mainLobbySet() {
		return data.contains("lobby");
	}
	
	public Location getMainLobby() {
		return new Location(Bukkit.getWorld(data.getString("lobby.world")), data.getInt("lobby.x"), data.getInt("lobby.y"), data.getInt("lobby.z"));
	}

	public void setMax(String string, int max) {
		data.set("Arenas." + string + ".max", max);
		save();
	}
}
