package nl.rens4000.bountyhunters.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import nl.rens4000.bountyhunters.Main;
import nl.rens4000.bountyhunters.managers.ConfigManager;

public class Arena {
	
	private ArrayList<String> players = new ArrayList<>();
	private HashMap<String, String> kit = new HashMap<>();
	private HashMap<String, String> targets = new HashMap<>();
	
	private String name;
	private int min;
	private int max;
	private int countdown = 30;
	private Location lobby;
	private Location spawn;
	private boolean enabled;
	private boolean pvp;
	private Random randomGenerator;
	
	private GameState state;

	private ConfigManager configManager;
	
	public Arena(String name, int min, int max, boolean enabled, Location lobby, Location spawn) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.spawn = spawn;
		this.lobby = lobby;
		this.enabled = enabled;
		this.state = GameState.WAITING;
		this.configManager = Main.getConfigManager();
		this.pvp = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean b) {
		enabled = b;
	}

	public boolean join(Player p) {
		if (!state.isJoinable())
			return false;
		if (!enabled)
			return false;
		if (players.size() == max)
			return false;
		players.add(p.getName());
		kit.put(p.getName(), "default");
		p.teleport(lobby);
		sendMessage(p.getName() + " has joined the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + players.size() + "/" + max + ChatColor.AQUA + ")");
		p.setHealth(20.0);
		p.setFoodLevel(20);
		
		if (state == GameState.WAITING) {
			if (players.size() >= min) {
				state = GameState.STARTING;
				sendMessage("Starting countdown!");
				start();
			}
		}		
		p.getInventory().clear();
		return true;
	}
	
	public String getRandomPlayer() {
		int index = randomGenerator.nextInt(players.size());
		return players.get(index);
	}
	
	public boolean isAlreadyTarget(String s) {
		for(String st : targets.values()) {
			if(st == s) {
				return true;
			}
		}
		return false;
	}

	public void start() {
		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(state != GameState.WAITING) {
						for (int i = 0; i < players.size(); i++) {
							Player p = Bukkit.getPlayer(players.get(i));
							p.setLevel(countdown - 1);
						}
				if(countdown == 0) {
					//Start the game
					state = GameState.IN_GAME;
					for (int i = 0; i < players.size(); i++) {
						Player p = Bukkit.getPlayer(players.get(i));
						//Set target
						String victim = getRandomPlayer();
						if(victim == p.getName() || isAlreadyTarget(victim)) {
							String victim2 = getRandomPlayer();
							targets.put(p.getName(), victim2);
							p.sendTitle(victim2 + "Is your target.", "Try to kill him!");
						} else {
							targets.put(p.getName(), victim);
							p.sendTitle(victim + "Is your target.", "Try to kill him!");
						}
						
						//Basic stuff
						p.teleport(spawn);
						if(kit.get(p.getName()) == "default") {
						p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
						p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
						ItemStack item = new ItemStack(Material.WOOD_SWORD);
						item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
						ItemStack item2 = new ItemStack(Material.BOW);
						item2.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
						p.getInventory().addItem(item);
						p.getInventory().addItem(item2);
						p.getInventory().addItem(new ItemStack(Material.ARROW, 6));
						}
						if(kit.get(p.getName()) == "warrior") {
							p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
							p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
							
							ItemStack item2 = new ItemStack(Material.BOW);
							ItemStack item = new ItemStack(Material.STONE_SWORD);
							item.addEnchantment(Enchantment.DAMAGE_ALL, 3);
							p.getInventory().addItem(item);
							p.getInventory().addItem(item2);
							p.getInventory().addItem(new ItemStack(Material.ARROW, 2));
							}
					}
					startPVP();
					sendMessage("PVP will be enabled in 5 seconds!");
					this.cancel();
					return;
				}
				countdown--;
				if(countdown == 30 || countdown == 20 || countdown == 15 || countdown == 10 || countdown == 5 || countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {
					sendMessage(ChatColor.AQUA + "" + countdown + ChatColor.WHITE + " seconds until the game starts!");
					sendTitle(ChatColor.RED + "" + countdown + ChatColor.WHITE + " seconds", ChatColor.AQUA + "Until the game starts!");
				}
				} else {
					countdown = 30;
					this.cancel();
				}
			}

			private void startPVP() {
				new BukkitRunnable() {

					@Override
					public void run() {
						pvp = true;
						sendMessage(ChatColor.AQUA + "PVP has been enabled! LAST MAN STANDING WINS!");
					}
					
				}.runTaskLater(Main.getInstance(), 100);
				
			}
			
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
	}

	public void sendMessage(String string) {
		for(String s : players) {
			Player p = Bukkit.getPlayer(s);
			p.sendMessage(Main.PREFIX + string);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void sendTitle(String string, String string2) {
		for(String s : players) {
			Player p = Bukkit.getPlayer(s);
			p.sendTitle(string, string2);
		}
	}

	public void leave(Player p) {
		p.teleport(Main.getConfigManager().getMainLobby());
		p.sendMessage(Main.PREFIX + "You have left the game!");
		players.remove(p.getName());
		kit.remove(p.getName());
		p.getInventory().clear();
		sendMessage(p.getName() + " has left the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + players.size() + "/" + max + ChatColor.AQUA + ")");


		if (players.size() < min && state == GameState.STARTING) {
			state = GameState.WAITING;

			sendMessage(ChatColor.RED + "Not enough players. Countdown stopped!");

		}
		if(players.size() == 0) {
			reset();
		}
		if (players.size() == 1 && state == GameState.IN_GAME) {
			end();
		}
	}
	
	public void leaveLast(Player p) {
		p.teleport(Main.getConfigManager().getMainLobby());
		players.remove(p.getName());
		kit.remove(p.getName());
		p.getInventory().clear();
			reset();	
	}

	private void reset() {
		state = GameState.RESETTING;
		for (int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			p.teleport(configManager.getMainLobby());
			p.getInventory().clear();
			if(targets.containsKey(p.getName()))
			targets.remove(p.getName());
		}
		pvp = false;
		countdown = 30;
		
		state = GameState.WAITING;
	}

	public void end() {
		if(players.size() == 0) {
			reset();
			return;
		}
		for (int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			p.sendMessage(Main.PREFIX + "You have won the game. Congratz!");
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getName() + ".Wins")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Wins", 1);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller() + ".Wins");
				Main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Wins", kills + 1);
				Main.getConfigManager().save();
			}
			leaveLast(p);
		}
	}

	public boolean getPVP() {
		return pvp;
	}

	public boolean inGame(Player p) {
				return players.contains(p.getName());
			}

	public void setKit(Player p, String string) {
		kit.remove(p.getName());
		kit.put(p.getName(), string);
	}

	public HashMap<String, String> getTargets() {
		// TODO Auto-generated method stub
		return targets;
	}

}
