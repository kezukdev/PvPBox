package dev.kezuk.flodoria.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {
	
	@EventHandler
	public void Motd(ServerListPingEvent event) {
		if (Bukkit.getServer().hasWhitelist()) {
			event.setMotd(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Flodoria\n" + ChatColor.WHITE + "Actuellement en maintenance!");
			event.setMaxPlayers(2022);
			return;
		}
		event.setMotd(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Flodoria\n" + ChatColor.RESET + ChatColor.WHITE + "Rejoignez-nous dès maintenant!");
		event.setMaxPlayers(event.getNumPlayers() + 1);
	}

	@EventHandler
	public void WeatherChange(WeatherChangeEvent event) {
		event.getWorld().setWeatherDuration(0);
		event.setCancelled(true);
	}
}
