package dev.kezuk.flodoria.enums.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Kit {
	
	WARRIOR(ChatColor.GREEN, "Warrior", new ItemStack(Material.IRON_SWORD), "flodoria.kit.warrior", 1),
	TANK(ChatColor.GREEN, "Tank", new ItemStack(Material.DIAMOND_CHESTPLATE), "flodoria.kit.tank", 10),
	NINJA(ChatColor.GREEN, "Ninja", new ItemStack(Material.FEATHER), "flodoria.kit.ninja", 15),
	SANSUE(ChatColor.GREEN, "Sansue", new ItemStack(Material.ROTTEN_FLESH), "flodoria.kit.sansue", 25),
	DRUIDE(ChatColor.GREEN, "Druide", new ItemStack(Material.POTION), "flodoria.kit.druide", 30);
	
	public ChatColor color;
	public String name;
	public ItemStack icon;
	public String permissions;
	public int level;
	
	Kit(ChatColor color, String name, ItemStack icon, String permissions, int level){
		this.color = color;
		this.name = name;
		this.icon = icon;
		this.permissions = permissions;
		this.level = level;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPermissions() {
		return permissions;
	}

	public int getLevel() {
		return level;
	}
	
	public static Kit getKitFromName(String name) {
		for (Kit classes : values()) {
			if (classes.getName().toLowerCase().equals(name.toLowerCase())) {
				return classes;
			}
		} 
		return null;
	}
}
