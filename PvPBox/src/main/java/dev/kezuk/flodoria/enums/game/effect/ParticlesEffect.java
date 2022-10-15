package dev.kezuk.flodoria.enums.game.effect;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ParticlesEffect {
	
	SMOKE(ChatColor.GREEN, "Smoke", new ItemStack(Material.STRING), "flodoria.particles.smoke", "largesmoke", 100),
	EXPLODE(ChatColor.GREEN, "Explosion", new ItemStack(Material.TNT), "flodoria.particles.explosion", "hugeexplosion", 125);
	
	public ChatColor color;
	public String name;
	public ItemStack icon;
	public String permissions;
	public String particleName;
	public int price;
	
	ParticlesEffect(ChatColor color, String name, ItemStack icon, String permissions,String particleName, int price) {
		this.color = color;
		this.name = name;
		this.icon = icon;
		this.permissions = permissions;
		this.particleName = particleName;
		this.price = price;
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
	
	public String getParticleName() {
		return particleName;
	}
	
	public int getPrice() {
		return price;
	}
	
	public static ParticlesEffect getParticleFromDisplayName(String name) {
		for (ParticlesEffect particles : values()) {
			String displayName = particles.getColor() + particles.getName();
			if (displayName.toLowerCase().equals(name.toLowerCase())) {
				return particles;
			}
		} 
		return null;
	}
}
