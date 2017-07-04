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

import nl.rens4000.bountyhunters.game.Arena;
import nl.rens4000.bountyhunters.managers.ArenaManager;

public class Events implements Listener {
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!ArenaManager.inGame(p))
			return;
		ArenaManager.getArena(p).leave(p);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(!ArenaManager.inGame(p))
			return;
		
		if(p.getKiller() != null) {
			ArenaManager.getArena(p).sendMessage(p.getName() + " has been murdered by " + p.getKiller().getName() + " and has been eliminated"
					+ " from the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + (ArenaManager.getArena(p).getPlayers().size() - 1) + "/" + ArenaManager.getArena(p).getMax() + ChatColor.AQUA + ")");
			p.getKiller().getInventory().addItem(new ItemStack(Material.ARROW));
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Kills")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", 1);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Kills");
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", kills + 1);
				Main.getConfigManager().save();
			}
			
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Kills")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", 1);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Kills");
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Kills", kills + 1);
				Main.getConfigManager().save();
			}
			
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getName() + ".Deaths")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Deaths", 1);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller() + ".Deaths");
				Main.getConfigManager().getScoreFile().set("Players." + p.getName() + ".Deaths", kills + 1);
				Main.getConfigManager().save();
			}
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Score")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", 1);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Score");
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", kills + 1);
				Main.getConfigManager().save();
			}
		} else {
			ArenaManager.getArena(p).sendMessage(p.getName() + " has been murdered and has been eliminated"
					+ " from the game! " + ChatColor.AQUA + "(" + ChatColor.WHITE + (ArenaManager.getArena(p).getPlayers().size() - 1) + "/" + ArenaManager.getArena(p).getMax() + ChatColor.AQUA + ")");
		}
		//Check if player was murderer's target
		if(ArenaManager.getArena(p).getTargets().get(p.getKiller()) == p.getName()) {
			p.getKiller().sendTitle(ChatColor.AQUA + "You killed your target!", ChatColor.GREEN + "Try to stay alive and kill everyone!");
			if(!Main.getConfigManager().getScoreFile().contains("Players." + p.getKiller().getName() + ".Score")) {
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", 5);
				Main.getConfigManager().save();
			} else {
				int kills = Main.getConfigManager().getScoreFile().getInt("Players." + p.getKiller().getName() + ".Score");
				Main.getConfigManager().getScoreFile().set("Players." + p.getKiller().getName() + ".Score", kills + 5);
				Main.getConfigManager().save();
			}
			ArenaManager.getArena(p).getTargets().remove(p.getName());
		}
		
		p.spigot().respawn();
		p.getInventory().clear();
		
		Arena a = ArenaManager.getArena(p);
		ArenaManager.getArena(p).getPlayers().remove(p.getName());
			p.teleport(Main.getConfigManager().getMainLobby());
			if(a.getPlayers().size() == 1) {
				a.end();
			return;
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(ArenaManager.inGame(p)) {
				if(!ArenaManager.getArena(p).canPVP()) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(ArenaManager.inGame(e.getPlayer()));
	}

}
