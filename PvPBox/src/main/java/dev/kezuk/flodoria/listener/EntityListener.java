package dev.kezuk.flodoria.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import dev.kezuk.flodoria.PvPBox;
import dev.kezuk.flodoria.enums.FightStatus;
import dev.kezuk.flodoria.enums.PlayerStatus;
import dev.kezuk.flodoria.enums.game.Kit;
import dev.kezuk.flodoria.low.FightManager;
import dev.kezuk.flodoria.low.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class EntityListener implements Listener {
	
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
            return;
        }
            final Player player = (Player)e.getEntity();
            final PlayerManager playerData = PlayerManager.getPlayers().get(player.getUniqueId());
            if (playerData.getPlayerState() == PlayerStatus.SPAWN) {
            	e.setCancelled(true);
            	return;
            }
            if (!playerData.isDamageFall()) {
            	if (player.getLocation().getY() <= 139.0D) {
                    if (e.getCause() == DamageCause.FALL) {
                    	playerData.setDamageFall(true);
                    	e.setCancelled(true);
                    	player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.25f);
                		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
                		if (pm.getKit() == Kit.DRUIDE) {
    						int pourcentage = new Random().nextInt(100);
    					  	ItemStack pot = new ItemStack(Material.POTION, 1, (short)16453);
    						if (pourcentage <= 15) {
    							ItemStack potspeed = new ItemStack(Material.POTION, 1, (short)16386);
    							player.getInventory().setItem(7, pot);
    							player.getInventory().setItem(6, potspeed);
    						}
    						if (pourcentage <= 50) {
    							player.getInventory().setItem(7, pot);
    						}
                		}
                		player.sendMessage(ChatColor.DARK_GREEN + (pm.isFrench() ? "Niveaux du retardateur: " : "Delayer level: ") + ChatColor.WHITE + FightManager.getInbox().get(player.getUniqueId()).getDelayerStatus().toString());
                    	player.sendMessage(ChatColor.WHITE + (pm.isFrench() ? "Vous pouvez attaquez d'autres joueurs dans " : "You can attack another player in ") + FightManager.getInbox().get(player.getUniqueId()).getDelayerStatus().getTime() + (pm.isFrench() ? " secondes!" : " seconds!"));
                        Bukkit.getScheduler().runTaskLater(PvPBox.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                            	pm.setPlayerState(PlayerStatus.GAME);
                            	player.sendMessage(ChatColor.DARK_GREEN + (pm.isFrench() ? "Bonne " : "Good ") + ChatColor.WHITE + (pm.isFrench() ? "Chance!" : "Luck!"));
                            }
                        }, FightManager.getInbox().get(player.getUniqueId()).getDelayerStatus().getLitre());
                    	return;
                    }
            	}
            }
            e.setCancelled(false);
    }
	
    @EventHandler
	public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
    	if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player)) {
          	 event.setCancelled(true);
           	return;
       	}
		Player damaged = (Player) event.getEntity();
		if (damaged.isDead()) return;
		Player attacker = (Player) event.getDamager();
		
		FightManager fmDamaged = FightManager.getInbox().get(damaged.getUniqueId());
		FightManager fmAttacker = FightManager.getInbox().get(attacker.getUniqueId());
		
		if (fmAttacker != null && fmAttacker.getFightState() == FightStatus.COMBAT) {
			return;
		}
		
		PlayerManager pmDamaged = PlayerManager.getPlayers().get(damaged.getUniqueId());
		PlayerManager pmAttacker = PlayerManager.getPlayers().get(attacker.getUniqueId());
		
		if (pmDamaged.getPlayerState() == PlayerStatus.SPAWNING || pmAttacker.getPlayerState() == PlayerStatus.SPAWNING) {
			event.setCancelled(true);
			return;
		}
		
		if (pmDamaged.getPlayerState() == PlayerStatus.SPAWN || pmAttacker.getPlayerState() == PlayerStatus.SPAWN || pmDamaged.getPlayerState() == PlayerStatus.SPAWN && pmAttacker.getPlayerState() == PlayerStatus.SPAWN) {
			event.setCancelled(true);
			return;
		}
		if (pmDamaged.getPlayerState() == PlayerStatus.GAME) {
			if (pmAttacker.getKit().equals(Kit.SANSUE)) {
				int pourcentage = new Random().nextInt(100);
	  	        if (pourcentage <= 25) {
		        	attacker.setHealth(attacker.getHealth() + 1);
		        	attacker.sendMessage(ChatColor.GREEN + (pmAttacker.isFrench() ? "Vous avez gagnez un coeur graçe à votre kit!" : "You've won one heart with your kit!"));
		        	return;
		        }
			}
			
			if (fmDamaged != null && fmAttacker != null) {
		        if (fmDamaged.getFightState().equals(FightStatus.FREE) && fmAttacker.getFightState().equals(FightStatus.FREE)) {
		        	pmAttacker.applyLastHitTime();
		        	pmDamaged.applyLastHitTime();
		            fmAttacker.setFightState(FightStatus.COMBAT);
		            fmDamaged.setFightState(FightStatus.COMBAT);
		            fmAttacker.setOpponent(damaged.getUniqueId());
		            fmDamaged.setOpponent(attacker.getUniqueId());
		        }
			}
		}
	}

}
