package nl.rens4000.bountyhunters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Sign;

import nl.rens4000.bountyhunters.game.Arena;

public class Events implements Listener {
	
	private Main main;
	
	public Events(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!main.getArenaManager().inGame(p))
			return;
		main.getArenaManager().getArena(p).leave(p);
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getType().equals(Material.SIGN) || e.getBlock().getType().equals(Material.SIGN_POST) || e.getBlock().getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) e.getBlock();
			if(!main.getConfigManager().getDataFile().contains("arenas." + s.getLine(3) + ".sign"))
				return;
			if(!e.getPlayer().hasPermission("BountyHunters.Admin"))
				e.setCancelled(true);
			main.getArenaManager().removeArena(s.getLine(3));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(!main.getArenaManager().inGame(p))
			return;
		
		if(p.getKiller() != null) {
			main.getArenaManager().getArena(p).sendMessage(p.getName() + " has been murdered by " + p.getKiller().getName() + " and has been eliminated"
					+ " from the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + (main.getArenaManager().getArena(p).getPlayers().size() - 1) + "/" + main.getArenaManager().getArena(p).getMax() + ChatColor.AQUA + ")");
			p.getKiller().getInventory().addItem(new ItemStack(Material.ARROW));
			if(!main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Kills")) {
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", 1);
				main.getConfigManager().save();
			} else {
				int kills = main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Kills");
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", kills + 1);
				main.getConfigManager().save();
			}
			
			if(!main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Kills")) {
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", 1);
				main.getConfigManager().save();
			} else {
				int kills = main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Kills");
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", kills + 1);
				main.getConfigManager().save();
			}
			
			if(!main.getConfigManager().getScoreFile().contains("Players." + p.getName() + ".Deaths")) {
				main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Deaths", 1);
				main.getConfigManager().save();
			} else {
				int kills = main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller() + ".Deaths");
				main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Deaths", kills + 1);
				main.getConfigManager().save();
			}
			if(!main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Score")) {
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", 1);
				main.getConfigManager().save();
			} else {
				int kills = main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Score");
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", kills + 1);
				main.getConfigManager().save();
			}
		} else {
			main.getArenaManager().getArena(p).sendMessage(p.getName() + " has been murdered and has been eliminated"
					+ " from the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + (main.getArenaManager().getArena(p).getPlayers().size() - 1) + "/" + main.getArenaManager().getArena(p).getMax() + ChatColor.AQUA + ")");
		}
		//Check if player was murderer's target
		if(main.getArenaManager().getArena(p).getTargets().get(p.getKiller()) == p.getName()) {
			p.getKiller().sendTitle(ChatColor.AQUA + "You killed your target!", ChatColor.GREEN + "Try to stay alive and kill everyone!");
			if(!main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Score")) {
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", 5);
				main.getConfigManager().save();
			} else {
				int kills = main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Score");
				main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", kills + 5);
				main.getConfigManager().save();
			}
			main.getArenaManager().getArena(p).getTargets().remove(p.getName());
		}
		
		p.spigot().respawn();
		p.getInventory().clear();
		
		Arena a = main.getArenaManager().getArena(p);
		main.getArenaManager().getArena(p).getPlayers().remove(p.getName());
			p.teleport(main.getConfigManager().getMainLobby());
			if(a.getPlayers().size() == 1) {
				a.end();
			return;
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(main.getArenaManager().inGame(p)) {
				if(!main.getArenaManager().getArena(p).canPVP()) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(main.getArenaManager().inGame(e.getPlayer()));
	}

}
