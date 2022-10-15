package dev.kezuk.flodoria.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.kezuk.flodoria.enums.FightStatus;
import dev.kezuk.flodoria.low.FightManager;
import dev.kezuk.flodoria.low.PlayerManager;

public class CombatTag extends BukkitRunnable {

	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() > 1) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				PlayerManager pm = PlayerManager.getPlayers().get(players.getUniqueId());
				if(!pm.isLastHitTimeActive()) {
					FightManager fm = FightManager.getInbox().get(players.getUniqueId());
					if (fm != null && fm.getFightState() == FightStatus.COMBAT) {
						FightManager fmOpponent = FightManager.getInbox().get(fm.getOpponent());
			            fm.setFightState(FightStatus.FREE);
			            fmOpponent.setFightState(FightStatus.FREE);
			            fm.setOpponent(null);
			            fmOpponent.setOpponent(null);
		                FightManager.setInFight(FightManager.getInFight() - 1);
					}
				}
			}
		}
	};
}
