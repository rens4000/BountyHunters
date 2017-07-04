package nl.rens4000.bountyhunters.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.rens4000.bountyhunters.Main;
import nl.rens4000.bountyhunters.game.Arena;

public class SignManager implements Listener{
	
	private Main main;
	
	public SignManager(Main main) {
		this.main = main;
	}

	public void updateSign(Location loc, Arena arena) {
		if(loc.getBlock().getState() instanceof Sign) {
			Sign s = (Sign) loc.getBlock().getState();
			s.setLine(2, arena.getState().getDisplayText().toString());
			s.setLine(3, ChatColor.GRAY.toString() + arena.getPlayers().size() + "/" + arena.getMax());
			s.update();
		}
 	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(!e.getLine(0).equalsIgnoreCase("[BountyHunters]")) return;
		Player p = e.getPlayer();
		if(!p.hasPermission("BountyHunters.Admin")) {
			p.sendMessage(main.PREFIX + ChatColor.RED + "You dont have permission to place BountyHunters signs!");
			e.setCancelled(true);
			return;
		}
		if(e.getLine(1).equalsIgnoreCase("leave")) {
			e.setLine(0, ChatColor.GOLD + "BountyHunters");
			e.setLine(1, ChatColor.DARK_RED + "Leave");
			p.sendMessage(main.PREFIX + ChatColor.GOLD + "Leave sign created.");
			return;
		}
		if(e.getLine(1).equalsIgnoreCase("join")) {
			if(e.getLine(2) == null) {
				p.sendMessage(main.PREFIX + ChatColor.RED + "Please enter an arena name on the second line.");
				e.setCancelled(true);
				return;
			}
			String name = e.getLine(2);
			if(!main.getArenaManager().exists(name)) {
				p.sendMessage(main.PREFIX + ChatColor.RED + "That arena doesn't exist.");
				e.setCancelled(true);
				return;
			}
			e.setLine(0, ChatColor.GOLD + "BountyHunters");
			e.setLine(1, ChatColor.GOLD + name);
			e.setLine(2, main.getArenaManager().getArena(name).getState().getDisplayText().toString());
			main.getArenaManager().setSign(e.getBlock().getLocation(), name);
			return;
		}
		e.setCancelled(true);
		p.sendMessage(main.PREFIX + ChatColor.RED + "Unknown option " + e.getLine(1));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock()) {
			if(e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				Sign s = (Sign) e.getClickedBlock().getState();
				if(s.getLine(0).equals(ChatColor.AQUA + "QuickCraft")) {
					if(s.getLine(1).equals(ChatColor.DARK_RED + "Leave")) {
						e.getPlayer().performCommand("qc leave");
						return;
					}
					String name = ChatColor.stripColor(s.getLine(1));
					e.getPlayer().performCommand("qc join " + name);
					updateSign(e.getClickedBlock().getLocation(), main.getArenaManager().getArena(name));
					main.getArenaManager().setSign(e.getClickedBlock().getLocation(), name);
				}
			}
		}
	}

}
