package dev.kezuk.flodoria.enums.game.effect;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public enum EffectsSound {
	
	THUNDER(ChatColor.GREEN, "Thunder", new ItemStack(Material.STICK), "flodoria.soundeffect.thunder", Sound.AMBIENCE_THUNDER, 10),
	BAT(ChatColor.GREEN, "Batman", new ItemStack(Material.CLAY), "flodoria.soundeffect.bat", Sound.BAT_DEATH, 25),
	ZOMBIE(ChatColor.GREEN, "Zombie", new ItemStack(Material.ROTTEN_FLESH), "flodoria.soundeffect.zombie", Sound.ZOMBIE_DEATH, 35),
	ANVIL(ChatColor.GREEN, "Stoneman", new ItemStack(Material.ANVIL), "flodoria.soundeffect.anvil", Sound.ANVIL_LAND, 75),
	ENDERDRAGON(ChatColor.GREEN, "Enderdragon", new ItemStack(Material.DRAGON_EGG), "flodoria.soundeffect.enderdragon", Sound.ENDERDRAGON_DEATH, 150);
	
	public ChatColor color;
	public String name;
	public ItemStack icon;
	public String permissions;
	public Sound sound;
	public int price;
	
	EffectsSound (ChatColor color, String name, ItemStack icon, String permissions, Sound sound, int price){
		this.color = color;
		this.name = name;
		this.icon = icon;
		this.permissions = permissions;
		this.sound = sound;
		this.price = price;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getPermissions() {
		return permissions;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public int getPrice() {
		return price;
	}
	
	public static EffectsSound getSoundFromDisplayName(String name) {
		for (EffectsSound sound : values()) {
			String displayName = sound.getColor() + sound.getName();
			if (displayName.toLowerCase().equals(name.toLowerCase())) {
				return sound;
			}
		} 
		return null;
	}

}
