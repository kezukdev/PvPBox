package dev.kezuk.flodoria.rapid;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.low.PlayerManager;

public class LevelUp {
	
	public LevelUp(Player killer) {
		PlayerManager pmTarget = PlayerManager.getPlayers().get(killer.getUniqueId());
		if (pmTarget.getExp() >= pmTarget.getNeedExp()) {
			this.multiplicatedNeedExp(killer.getUniqueId());
			pmTarget.setLevel(pmTarget.getLevel() + 1);
			pmTarget.setTab(killer);
			killer.sendMessage(ChatColor.DARK_GREEN + (pmTarget.isFrench() ? "Vous avez augmentez d'un niveau, vous êtes désormais niveau " : "You have been upgrade your level, you'r level is now ") + pmTarget.getLevel() + "!");
			return;
		}
	}
	
	public void multiplicatedNeedExp(final UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		pm.setNeedExp(pm.getNeedExp() * 1.5D);
	}

}
