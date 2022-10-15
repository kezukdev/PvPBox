package dev.kezuk.flodoria.low;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import dev.kezuk.flodoria.enums.game.Kit;
import dev.kezuk.flodoria.enums.game.effect.EffectsSound;
import dev.kezuk.flodoria.enums.game.effect.ParticlesEffect;
import dev.kezuk.flodoria.utils.ItemBuilder;

public class InventoryManager {
	
	public Inventory shopInventory;
	public Inventory shopSoundInventory;
	public Inventory shopKillEffectInventory;
	public Inventory kitInventory;
	
	public InventoryManager() {
		this.shopInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Manage:");
		this.shopSoundInventory = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Sound Effects:");
		this.shopKillEffectInventory = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Kill Effects:");
		this.kitInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Kits:");
		this.setShopInventory();
	}
	
	public void setKit(Player player) {
		this.kitInventory.clear();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		for (Kit kit : Kit.values()) {
			ItemStack item = ItemBuilder.createNewItemStack(kit.getIcon(),kit.getColor() + kit.getName(), Lists.newArrayList(ChatColor.GRAY + (pm.getLevel() >= kit.getLevel() ?  ChatColor.GRAY + (pm.isFrench() ? "Vous posséder ce kit!" : "You have this kit!") : (pm.isFrench() ? "Niveau requis:" : "Required level: ") + ChatColor.DARK_AQUA + kit.getLevel())));
			this.kitInventory.addItem(item);
		}
	}

	public void setShopKillEffect(Player player) {
		this.shopKillEffectInventory.clear();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		for (ParticlesEffect effect : ParticlesEffect.values()) {
			ItemStack item = ItemBuilder.createNewItemStack(effect.getIcon(), effect.getColor() + effect.getName(), Lists.newArrayList((player.hasPermission(effect.getPermissions()) ? ChatColor.GRAY + (pm.isFrench() ? "Cliquer pour activer cette effet!" : "Click here for enable the effect!") : (pm.isFrench() ? ChatColor.GRAY + "Prix: " : ChatColor.GRAY + "Price: ") + ChatColor.DARK_AQUA + effect.getPrice())));
			this.shopKillEffectInventory.addItem(item);
		}
	}

	public void setShopSoundInventory(Player player) {
		this.shopSoundInventory.clear();
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		for (EffectsSound effect : EffectsSound.values()) {
			ItemStack item = ItemBuilder.createNewItemStack(effect.getIcon(), effect.getColor() + effect.getName(), Lists.newArrayList((player.hasPermission(effect.getPermissions()) ? ChatColor.GRAY + (pm.isFrench() ? "Cliquer pour activer cette effet!" : "Click here for enable the effect!") : (pm.isFrench() ? ChatColor.GRAY + "Prix: " : ChatColor.GRAY + "Price: ") + ChatColor.DARK_AQUA + effect.getPrice())));
			this.shopSoundInventory.addItem(item);
		}
	}

	public void setShopInventory() {
		ItemStack sound = ItemBuilder.createNewItemStack(new ItemStack(Material.NOTE_BLOCK), ChatColor.GREEN + "Sound", Lists.newArrayList(ChatColor.DARK_AQUA + "Some sound effect at kill opponent!\n"));
		ItemStack killEffect = ItemBuilder.createNewItemStack(new ItemStack(Material.FIREWORK), ChatColor.GREEN + "Kill-Effect", Lists.newArrayList(ChatColor.DARK_AQUA + "Many particle summon at kill!\n"));
		this.shopInventory.addItem(sound);
		this.shopInventory.addItem(killEffect);
	}
	
	public Inventory getShopInventory() {
		return shopInventory;
	}
	
	public Inventory getShopKillEffectInventory() {
		return shopKillEffectInventory;
	}
	
	public Inventory getShopSoundInventory() {
		return shopSoundInventory;
	}
	
	public Inventory getKitInventory() {
		return kitInventory;
	}
}
