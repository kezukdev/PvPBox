package dev.kezuk.flodoria.rapid;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.low.PlayerManager;

public class Killstreak {
	
	public int[] ks = {5, 10, 15, 25, 40};
	
	public Killstreak(Player player) {
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		pm.setKillstreak(pm.getKillstreak() + 1);
		for (int kss : ks) {
			if (kss == pm.getKillstreak()) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					PlayerManager pms = PlayerManager.getPlayers().get(players.getUniqueId());
					players.sendMessage(" ");
					players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
					players.sendMessage(ChatColor.DARK_GREEN + "Killstreak" + ChatColor.GRAY + ":");
					players.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.WHITE + (pms.isFrench() ? " est sur un killstreak de " : " has on a killstreak of ") + pm.getKillstreak());
					players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
					players.sendMessage(" ");
				}
			}
		}
	}
}
