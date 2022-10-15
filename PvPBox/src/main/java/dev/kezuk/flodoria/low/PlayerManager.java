package dev.kezuk.flodoria.low;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import com.google.common.collect.Maps;

import co.aikar.idb.DB;
import dev.kezuk.flodoria.PvPBox;
import dev.kezuk.flodoria.enums.Color;
import dev.kezuk.flodoria.enums.PlayerStatus;
import dev.kezuk.flodoria.enums.game.Kit;
import dev.kezuk.flodoria.enums.game.effect.EffectsSound;
import dev.kezuk.flodoria.enums.game.effect.ParticlesEffect;
import dev.kezuk.flodoria.utils.ItemBuilder;
import net.luckperms.api.cacheddata.CachedMetaData;

public class PlayerManager {
	public static Map<UUID, PlayerManager> players;
	public UUID uuid;
	public PlayerStatus playerState;
	public double exp;
	public double needExp;
	public int level;
	public int kill;
	public int death;
	public boolean build;
	public boolean french;
	public int killstreak;
	public boolean damageFall;
	public int coins;
	public String name;
	public Kit kit;
	public ParticlesEffect particles;
	public EffectsSound sound;
	private Long lastHitTime = 0L;
	
	public PlayerManager(UUID uuid) {
		this.uuid = uuid;
		this.playerState = PlayerStatus.SPAWN;
		this.exp = 0;
		this.needExp = 5;
		this.level = 1;
		this.death = 0;
		this.kill = 0;
		this.killstreak = 0;
		this.coins = 0;
		this.build = false;
		this.french = false;
		this.damageFall = false;
		this.name = Bukkit.getPlayer(uuid).getName();
		PlayerManager.players.putIfAbsent(uuid, this);
		this.update();
	}

	public void remove() {
		players.remove(this.uuid);
	}
	
	public void sendToSpawn() {
		Player player = Bukkit.getPlayer(uuid);
		this.setPlayerState(PlayerStatus.SPAWN);
		player.getInventory().clear();
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(player.getMaxHealth());
		player.getInventory().setArmorContents(null);
		player.setItemOnCursor(null);
		player.setSaturation(10000);
		player.setGameMode(GameMode.SURVIVAL);
		player.teleport(new Location(player.getLocation().getWorld(), player.getWorld().getSpawnLocation().getX(), player.getWorld().getSpawnLocation().getY(), player.getWorld().getSpawnLocation().getZ(), 178.0f, 1.0f));
		player.getInventory().setItem(0, ItemBuilder.createNewItemStackByMaterial(Material.EMERALD, ChatColor.GREEN + (this.isFrench() ? "Effets" : "Effects"), true));
		player.getInventory().setItem(4, ItemBuilder.createNewItemStackByMaterial(Material.CHEST, ChatColor.DARK_GREEN + (this.isFrench() ? "Choisissez votre kit!" : "Choose your kit!"), true));
		player.getInventory().setItem(8, ItemBuilder.createNewItemStackByMaterial(Material.NAME_TAG, ChatColor.GREEN + (this.isFrench() ? "Traduire" : "Translator"), true));
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		player.extinguish();
		player.updateInventory();
	}
	
	public void sendToBox(String kitName) {
		Player player = Bukkit.getPlayer(uuid);
		this.setPlayerState(PlayerStatus.SPAWNING);
		new FightManager(uuid);
		player.getInventory().clear();
		player.getActivePotionEffects().clear();
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.setItemOnCursor(null);
		player.setGameMode(GameMode.SURVIVAL);
		player.setSaturation(10000);
		Kit kit  = Kit.getKitFromName(kitName);
		setKit(kit);
	}
	
	public void clearData() {
		setLevel(1);
		setExp(0.0);
		setNeedExp(5.0);
		setDeath(0);
		setKill(0);
	}
	
	public void setSound(EffectsSound sound) {
		this.sound = sound;
	}
	
	public void setParticles(ParticlesEffect particles) {
		this.particles = particles;
	}
	
	public ParticlesEffect getParticles() {
		return particles;
	}
	
	public EffectsSound getSound() {
		return sound;
	}
	
	public void setKit(Kit kit) {
		this.kit = kit;
	}
	
	public boolean isLastHitTimeActive() {
		return this.lastHitTime > System.currentTimeMillis();
	}

	public long getLastHitTime() {
		return Math.max(0L, this.lastHitTime - System.currentTimeMillis());
	}

	public void applyLastHitTime() {
		this.lastHitTime = Long.valueOf(System.currentTimeMillis() + (60 * 1000));
	}

	public void removeLastHitTime() {
		this.lastHitTime = 0L;
	}
	
	public Kit getKit() {
		return kit;
	}
	
	public boolean isDamageFall() {
		return damageFall;
	}
	
	public void setDamageFall(boolean damageFall) {
		this.damageFall = damageFall;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}
	
	public static Map<UUID, PlayerManager> getPlayers() {
		return players;
	}
	
	public void setNeedExp(double needExp) {
		this.needExp = needExp;
	}
	
	public void setFrench(boolean french) {
		this.french = french;
	}
	
	public boolean isFrench() {
		return french;
	}
	
	public void setBuild(boolean build) {
		this.build = build;
	}
	
	public double getNeedExp() {
		return needExp;
	}
	
	public boolean isBuild() {
		return build;
	} 
	
	public void setPlayerState(PlayerStatus playerState) {
		this.playerState = playerState;
	}
	
	public PlayerStatus getPlayerState() {
		return playerState;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}
	
	public void setExp(double exp) {
		this.exp = exp;
	}
	
	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getDeath() {
		return death;
	}
	
	public double getExp() {
		return exp;
	}
	
	public int getKill() {
		return kill;
	}
	
	public int getLevel() {
		return level;
	}
	
	private boolean existPlayer() {
		return PvPBox.getInstance().practiceDB.existPlayerManager(this.uuid);
	}
	    
	private void update() {
		if (!this.existPlayer()) {
			PvPBox.getInstance().practiceDB.createPlayerManager(this.uuid,this.name);
		}
		else {
			PvPBox.getInstance().practiceDB.updatePlayerManager(this.name, this.uuid);
			this.load();
		}
	}
	    
	private void load() {
		try {
			this.kill = DB.getFirstRow("SELECT kills FROM playersdata WHERE name=?", this.name).getInt("kills");
			this.death = DB.getFirstRow("SELECT deaths FROM playersdata WHERE name=?", this.name).getInt("deaths");
			this.exp = DB.getFirstRow("SELECT exps FROM playersdata WHERE name=?", this.name).getDbl("exps");
			this.needExp = DB.getFirstRow("SELECT needexp FROM playersdata WHERE name=?", this.name).getDbl("needexp");
			this.level = DB.getFirstRow("SELECT levels FROM playersdata WHERE name=?", this.name).getInt("levels");
			this.coins = DB.getFirstRow("SELECT coins FROM playersdata WHERE name=?", this.name).getInt("coins");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
		   
	private void save() {
		try {
			DB.executeUpdate("UPDATE playersdata SET kills=? WHERE name=?", this.kill, this.name);
			DB.executeUpdate("UPDATE playersdata SET deaths=? WHERE name=?", this.death, this.name);
			DB.executeUpdate("UPDATE playersdata SET exps=? WHERE name=?", this.exp, this.name);
			DB.executeUpdate("UPDATE playersdata SET needexp=? WHERE name=?", this.needExp, this.name);
			DB.executeUpdate("UPDATE playersdata SET levels=? WHERE name=?", this.level, this.name);
			DB.executeUpdate("UPDATE playersdata SET coins=? WHERE name=?", this.coins, this.name);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public void disconnect(final Player player) {
    	save();
    	final Player p = player;
    	players.remove(player.getUniqueId());
    	p.kickPlayer(ChatColor.WHITE + "Thanks for playing on " + ChatColor.DARK_GREEN + "Flodoria");
    }
	
	public void setTab(Player player) {
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		final CachedMetaData metaData = PvPBox.getInstance().getLuckPerms().getPlayerAdapter(Player.class).getMetaData(player);
		final int level = pm.getLevel();
		int bracket = 0;
        int[] tablo = {0, 5, 10, 20, 30, 40, 75, 76};
        for (int i = 0; i < (tablo.length - 1); i++) {
            if(level > tablo[i] && level <= tablo[i+1]) {
                bracket = tablo[i];
            }
        }
        if (level > 76) {
            bracket = 75;
        }
        if (metaData.getSuffix() != null) {
        	player.setPlayerListName(ChatColor.valueOf(Color.getColorByValue(bracket).toString()).toString() + level + " " + metaData.getSuffix() + player.getName());
        	return;
        }
		player.setPlayerListName(ChatColor.valueOf(Color.getColorByValue(bracket).toString()).toString() + level + " " + ChatColor.GRAY + player.getName());
	}
	
    static {
        PlayerManager.players = Maps.newConcurrentMap();
    }

}
