package dev.kezuk.flodoria.listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.kezuk.flodoria.PvPBox;
import dev.kezuk.flodoria.enums.FightStatus;
import dev.kezuk.flodoria.enums.PlayerStatus;
import dev.kezuk.flodoria.enums.game.Kit;
import dev.kezuk.flodoria.enums.game.effect.EffectsSound;
import dev.kezuk.flodoria.enums.game.effect.ParticlesEffect;
import dev.kezuk.flodoria.low.FightManager;
import dev.kezuk.flodoria.low.PlayerManager;
import dev.kezuk.flodoria.rapid.Killstreak;
import dev.kezuk.flodoria.rapid.LevelUp;
import dev.kezuk.flodoria.utils.ItemBuilder;
import dev.kezuk.flodoria.utils.ParticleAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		PlayerManager pm = new PlayerManager(player.getUniqueId());
		pm.sendToSpawn();
		pm.setTab(player);
		this.welcomeMessage(player);
		
	}

	@EventHandler
	public void PlayerLeft(PlayerQuitEvent event) {
		event.setQuitMessage("");
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (FightManager.getInbox().containsKey(uuid)) {
			FightManager fm = FightManager.getInbox().get(uuid);
			if (fm.getOpponent() != null) {
				Player opponent = Bukkit.getPlayer(fm.getOpponent());
				PlayerManager pmOpponent = PlayerManager.getPlayers().get(opponent.getUniqueId());
				pmOpponent.setKill(pmOpponent.getKill() + 1);
				FightManager.getInbox().get(opponent.getUniqueId()).setFightState(FightStatus.FREE);
				for (Player players : Bukkit.getOnlinePlayers()) {
					opponent.showPlayer(players);
					if (FightManager.getInbox().get(players.getUniqueId()) != null && FightManager.getInbox().get(players.getUniqueId()).getFightState() == FightStatus.COMBAT) {
						continue;
					}
					players.showPlayer(opponent);
				}
				FightManager.getInbox().get(opponent.getUniqueId()).setOpponent(null);
				PlayerManager.getPlayers().get(uuid).setDeath(PlayerManager.getPlayers().get(uuid).getDeath() + 1);
				opponent.sendMessage(ChatColor.GREEN + (pmOpponent.isFrench() ? "Vous avez tuez " : "You have killed ") + Bukkit.getPlayer(uuid).getName() + (pmOpponent.isFrench() ? " car il s'est déconnecter en combat!" : " because he have disconnect in fight!"));
				opponent.setHealth(player.getMaxHealth());
				pmOpponent.removeLastHitTime();
				FightManager.setInFight(FightManager.getInFight() - 1);
			}
			FightManager.getInbox().remove(uuid);
		}
		pm.disconnect(player);
	}
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack current = event.getItem();
        if(current == null) return;
        Action action = event.getAction();
        Player player = event.getPlayer();
        if(current.hasItemMeta()) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                PlayerManager playerManager = PlayerManager.getPlayers().get(player.getUniqueId());
                if(playerManager.getPlayerState() == PlayerStatus.SPAWN) {
                    event.setCancelled(true);
                    if(current.getType() == Material.CHEST) {
                    	PvPBox.getInstance().getInventoryManager().setKit(player);
                    	player.openInventory(PvPBox.getInstance().getInventoryManager().getKitInventory());
                    }
                    if(current.getType() == Material.NAME_TAG) {
                    	if (playerManager.isFrench()) {
                    		playerManager.setFrench(false);
                    		player.sendMessage(ChatColor.GREEN + "You've been set the lang to english!");
                    		playerManager.sendToSpawn();
                    		return;
                    	}
                		playerManager.setFrench(true);
                		player.sendMessage(ChatColor.GREEN + "Vous avez défini la langue à française!");
                		playerManager.sendToSpawn();
                    }
                    if (current.getType() == Material.EMERALD) {
                    	player.openInventory(PvPBox.getInstance().getInventoryManager().getShopInventory());
                    	return;
                    }
                }
            }
        }
	}
	
	@EventHandler
	public void PlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.isBuild()) {
			event.setCancelled(false);
			return;
		}
		if (pm.getPlayerState() == PlayerStatus.SPAWN || pm.getPlayerState() == PlayerStatus.GAME) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
	
	@EventHandler
	public void DeathEvent(final PlayerDeathEvent event) {
		event.setDeathMessage(null);
		event.getDrops().clear();	
		final Player player = event.getEntity().getPlayer();
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (event.getEntity().getKiller() instanceof Player) {
			final Player killer = event.getEntity().getKiller();
			d(killer);
			killer.setHealth(killer.getMaxHealth());
			for (Player players : Bukkit.getOnlinePlayers()) {
        			PlayerManager pms = PlayerManager.getPlayers().get(players.getUniqueId());
				if (FightManager.getInbox().get(players.getUniqueId()) != null && FightManager.getInbox().get(players.getUniqueId()).getFightState() == FightStatus.FREE){
					players.showPlayer(player);
            				players.showPlayer(killer);
				}
           		 	killer.showPlayer(players);
            			player.showPlayer(players);
        		players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
        		players.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + (pms.isFrench() ? " à été tuer par " : " as been killed by " ) + ChatColor.DARK_GREEN + killer.getName());
        		players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
            		}
			FightManager.getInbox().get(player.getUniqueId()).setOpponent(null);
			player.sendMessage(ChatColor.DARK_GREEN + (pm.isFrench() ? "Vous avez était tuer par " : "Your killer is ") + ChatColor.WHITE + killer.getName());
		}
        	pm.setDeath(pm.getDeath() + 1);
        	pm.setKillstreak(0);
        	player.setLevel(0);
        	FightManager.getInbox().get(player.getUniqueId()).setFightState(FightStatus.FREE);
		if (FightManager.getInbox().get(player.getUniqueId()).getOpponent() != null) {
			final UUID uuid = FightManager.getInbox().get(player.getUniqueId()).getOpponent();
			final Player killer = Bukkit.getPlayer(uuid);
			d(killer);
			killer.setHealth(killer.getMaxHealth());
			for (Player players : Bukkit.getOnlinePlayers()) {
        		PlayerManager pms = PlayerManager.getPlayers().get(players.getUniqueId());
        		players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
        		players.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + (pms.isFrench() ? " à été tuer par " : " as been killed by " ) + ChatColor.DARK_GREEN + killer.getName());
        		players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
            }
			FightManager.getInbox().get(player.getUniqueId()).setOpponent(null);
			player.sendMessage(ChatColor.DARK_GREEN + (pm.isFrench() ? "Vous avez était tuer par " : "Your killer is ") + ChatColor.WHITE + killer.getName());
		}
        	FightManager.getInbox().remove(player.getUniqueId());
        	pm.setDamageFall(false);
        	if (pm.getExp() > 0.25D) {
        		pm.setExp(pm.getExp() - 0.25D);
        	}
         	new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
	                final Object nmsPlayer = event.getEntity().getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(event.getEntity(), new Object[0]);
	                final Object con = nmsPlayer.getClass().getDeclaredField("playerConnection").get(nmsPlayer);
	                final Class<?> EntityPlayer = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EntityPlayer");
	                final Field minecraftServer = con.getClass().getDeclaredField("minecraftServer");
	                minecraftServer.setAccessible(true);
	                final Object mcserver = minecraftServer.get(con);
	                final Object playerlist = mcserver.getClass().getDeclaredMethod("getPlayerList", (Class<?>[])new Class[0]).invoke(mcserver, new Object[0]);
	                final Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", EntityPlayer, Integer.TYPE, Boolean.TYPE);
	                moveToWorld.invoke(playerlist, nmsPlayer, 0, false);
					pm.sendToSpawn();	
				}
				  catch (Exception ex) {
					  	player.spigot().respawn();
					  	pm.sendToSpawn();
	                    ex.printStackTrace();
	                }
			}
		}.runTaskLater(PvPBox.getInstance(), 2L);
	}
						    
	private void d(Player killer){
			final PlayerManager pmTarget = PlayerManager.getPlayers().get(killer.getUniqueId());
			pmTarget.setExp(pmTarget.getExp() + 1.0D);
			pmTarget.setKill(pmTarget.getKill() + 1);
			pmTarget.setCoins(pmTarget.getCoins() + 3);
			pmTarget.removeLastHitTime();
			if (pmTarget.getParticles() != null) {
				ParticleAPI particle = new ParticleAPI(pmTarget.getParticles().getParticleName(), killer.getLocation(), 0.1f, 0.1f, 0.1f, 0.08f, 40);
				particle.sendToPlayer(killer);
			}
			if (pmTarget.getSound() != null) {
				killer.playSound(killer.getLocation(), pmTarget.getSound().getSound(), 1.0f, 1.25f);
			}
			if (pmTarget.getKit() == Kit.DRUIDE) {
				int pourcentage = new Random().nextInt(100);
				ItemStack pot = new ItemStack(Material.POTION, 1, (short)16453);
				if (pourcentage <= 15) {
					ItemStack potspeed = new ItemStack(Material.POTION, 1, (short)16386);
					killer.getInventory().setItem(7, pot);
					killer.getInventory().setItem(6, potspeed);
				}
				if (pourcentage <= 50) {
					killer.getInventory().setItem(7, pot);
				}
				killer.getInventory().setItem(8, pot);
			}
			FightManager.getInbox().get(killer.getUniqueId()).setFightState(FightStatus.FREE);
			FightManager.getInbox().get(killer.getUniqueId()).setOpponent(null);
			new LevelUp(killer);
        		new Killstreak(killer);
         		FightManager.setInFight(FightManager.getInFight() - 1);
	}
	
	
	@EventHandler
	public void onBreaking(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.isBuild()) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBuilder(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.isBuild()) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        PlayerManager playerManager = PlayerManager.getPlayers().get(player.getUniqueId());
        if (playerManager.isBuild()) {
        	event.setCancelled(false);
        	return;
        }
        if (playerManager.getPlayerState() == PlayerStatus.SPAWN) {
        	User user = PvPBox.getInstance().getLuckPerms().getUserManager().getUser(player.getUniqueId());
            event.setCancelled(true);
			final ItemStack item = event.getCurrentItem();
            if (current.hasItemMeta()) {
            	if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Kits:")) {
    				player.closeInventory();
    				Kit kit = Kit.getKitFromName(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
    				if (kit != null && playerManager.getLevel() >= kit.getLevel() || player.isOp()) {
    	           			playerManager.setKit(kit);
    	           			player.sendMessage(ChatColor.GREEN + "You've selected the " + kit.getName() + " kit");
    	           			player.closeInventory();
                			return;
    				}
    				player.sendMessage(ChatColor.RED + (playerManager.isFrench() ? "Vous ne possedez pas ce kit!" : "You don't have this kit!"));
            	}
            	if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Manage:")) {
            		if (current.getType() == Material.NOTE_BLOCK) {
            			PvPBox.getInstance().getInventoryManager().setShopSoundInventory(player);
            			player.openInventory(PvPBox.getInstance().getInventoryManager().getShopSoundInventory());
            		}
            		if (current.getType() == Material.FIREWORK) {
            			PvPBox.getInstance().getInventoryManager().setShopKillEffect(player);
            			player.openInventory(PvPBox.getInstance().getInventoryManager().getShopKillEffectInventory());
            		}
            	}
            	if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Kill Effects:")) {
            		ParticlesEffect particles = ParticlesEffect.getParticleFromDisplayName(item.getItemMeta().getDisplayName());
            		player.closeInventory();
            		if (player.hasPermission(particles.getPermissions())) {
            			playerManager.setParticles(particles);
            			player.sendMessage(ChatColor.GREEN + (playerManager.isFrench() ? "Vous avez bien défini l'effet " : "You've been defined the effect ") + ChatColor.GRAY + "► " + particles.getColor() + particles.getName());
            			return;
            		}
            		if (playerManager.getCoins() >= particles.getPrice()) {
            			user.data().add(Node.builder(particles.getPermissions()).build());
            			playerManager.setCoins(playerManager.getCoins() - 50);
            			playerManager.setParticles(particles);
            			player.sendMessage(ChatColor.GREEN + (playerManager.isFrench() ? "L'éffet à était bien acheter et vous à était mis!" : "The effect as been bought and him as set by default"));
            			return;
            		}
        			player.sendMessage(ChatColor.RED + (playerManager.isFrench() ? "Votre solde n'est pas suffisant!" : "You'r sold is empty!"));
            	}
            	if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Sound Effects:")) {
            		EffectsSound sound = EffectsSound.getSoundFromDisplayName(item.getItemMeta().getDisplayName());
            		player.closeInventory();
            		if (player.hasPermission(sound.getPermissions())) {
            			playerManager.setSound(sound);
            			player.sendMessage(ChatColor.GREEN + (playerManager.isFrench() ? "Vous avez bien défini le son " : "You've been defined the sound ") + ChatColor.GRAY + "► " + sound.getColor() + sound.getName());
            			return;
            		}
            		if (playerManager.getCoins() >= sound.getPrice()) {
            			user.data().add(Node.builder(sound.getPermissions()).build());
            			playerManager.setCoins(playerManager.getCoins() - 50);
            			playerManager.setSound(sound);
            			player.sendMessage(ChatColor.GREEN + (playerManager.isFrench() ? "L'éffet à était bien acheter et vous à était mis!" : "The effect as been bought and him as set by default"));
            			return;
            		}
        			player.sendMessage(ChatColor.RED + (playerManager.isFrench() ? "Votre solde n'est pas suffisant!" : "You'r sold is empty!"));
            	}
            }
        }
	}
	
	@EventHandler()
	public void playerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if(!pm.isDamageFall()) {
			if (player.getLocation().getY() <= 139.0D) {
	            if (pm.getPlayerState() == PlayerStatus.SPAWN) {
	            	if (pm.getKit() != null) {
	            		pm.sendToBox(pm.getKit().getName());
	            		return;
	            	}
	            	pm.sendToBox("Warrior");
	            }
	            Kit kit = pm.getKit();
	            switch (kit) {
					case WARRIOR: {
						ItemStack helmet = ItemBuilder.createNewItemStackByMaterial(Material.IRON_HELMET, null, true);
						ItemStack chestplate = ItemBuilder.createNewItemStackByMaterial(Material.DIAMOND_CHESTPLATE, null, true);
						ItemStack leggings = ItemBuilder.createNewItemStackByMaterial(Material.IRON_LEGGINGS, null, true);
						ItemStack boots = ItemBuilder.createNewItemStackByMaterial(Material.DIAMOND_BOOTS, null, true);
						player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
						player.getInventory().setItem(0, ItemBuilder.createNewItemStackByMaterial(Material.IRON_SWORD, null, true));
						break;
					}
					case TANK: {
						ItemStack helmet = ItemBuilder.createNewItemStackByMaterial(Material.IRON_HELMET, null, true);
						ItemStack chestplate = ItemBuilder.createNewItemStackByMaterial(Material.DIAMOND_CHESTPLATE, null, true);
						ItemStack leggings = ItemBuilder.createNewItemStackByMaterial(Material.IRON_LEGGINGS, null, true);
						ItemStack boots = ItemBuilder.createNewItemStackByMaterial(Material.DIAMOND_BOOTS, null, true);
						player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
						ItemStack attack = ItemBuilder.createNewItemStackByMaterial(Material.WOOD_SWORD, null, true);
						attack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						player.getInventory().setItem(0, attack);
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 0));
						player.setMaxHealth(23.0D);
						player.setHealth(player.getMaxHealth());
						break;
					}
					case NINJA: {
						ItemStack helmet = ItemBuilder.createNewItemStackByMaterial(Material.IRON_HELMET, null, true);
						ItemStack chestplate = ItemBuilder.createNewItemStackByMaterial(Material.CHAINMAIL_CHESTPLATE, null, true);
						ItemStack leggings = ItemBuilder.createNewItemStackByMaterial(Material.IRON_LEGGINGS, null, true);
						ItemStack boots = ItemBuilder.createNewItemStackByMaterial(Material.CHAINMAIL_BOOTS, null, true);
						player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
						ItemStack attack = ItemBuilder.createNewItemStackByMaterial(Material.DIAMOND_SWORD, null, true);
						attack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						player.getInventory().setItem(0, attack);
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
						return;
					}
					case SANSUE: {
						ItemStack helmet = ItemBuilder.createNewItemStackByMaterial(Material.IRON_HELMET, null, true);
						ItemStack chestplate = ItemBuilder.createNewItemStackByMaterial(Material.IRON_CHESTPLATE, null, true);
						ItemStack leggings = ItemBuilder.createNewItemStackByMaterial(Material.IRON_LEGGINGS, null, true);
						ItemStack boots = ItemBuilder.createNewItemStackByMaterial(Material.IRON_BOOTS, null, true);
						player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
						player.getInventory().setItem(0, ItemBuilder.createNewItemStackByMaterial(Material.IRON_SWORD, null, true));
						break;
					}
					case DRUIDE: {
					  	ItemStack pot = new ItemStack(Material.POTION, 1, (short)16453);
						ItemStack helmet = ItemBuilder.createNewItemStackByMaterial(Material.IRON_HELMET, null, true);
						ItemStack chestplate = ItemBuilder.createNewItemStackByMaterial(Material.CHAINMAIL_CHESTPLATE, null, true);
						ItemStack leggings = ItemBuilder.createNewItemStackByMaterial(Material.IRON_LEGGINGS, null, true);
						ItemStack boots = ItemBuilder.createNewItemStackByMaterial(Material.IRON_BOOTS, null, true);
						player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
						player.getInventory().setItem(0, ItemBuilder.createNewItemStackByMaterial(Material.IRON_SWORD, null, true));
						player.getInventory().setItem(8, pot);
						break;
					}
	            }
			}
		}
	}
	
	private void welcomeMessage(Player player) {
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
		player.sendMessage(ChatColor.WHITE + (pm.isFrench() ? "Bienvenue sur le PvPBox de " : "Welcome on the PvPBox of ") + ChatColor.DARK_GREEN + "Flodoria");
		player.sendMessage("");
		player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Nous sommes heureux de vous acceuillir!" : "We are happy to welcome you!"));
		player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Notre discord: " : "Us discord: ") + ChatColor.WHITE + "http://discord.flodoria.com/");
		player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
	}
}
