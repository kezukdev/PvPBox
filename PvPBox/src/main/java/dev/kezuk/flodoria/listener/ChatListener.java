package dev.kezuk.flodoria.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.kezuk.flodoria.PvPBox;
import dev.kezuk.flodoria.enums.Color;
import dev.kezuk.flodoria.low.PlayerManager;
import net.luckperms.api.cacheddata.CachedMetaData;

public class ChatListener implements Listener {
	
	private Pattern blacklistPattern = Pattern.compile("\b(e+z+|t+r+a+s+h+|l+o+s+e+r+|n+o+o+b+|k+y+s+)");
	
	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		final CachedMetaData metaData = PvPBox.getInstance().getLuckPerms().getPlayerAdapter(Player.class).getMetaData(player);
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		final int level = pm.getLevel();
		int value = 0;
        int[] tablo = {0, 5, 10, 20, 30, 40, 75, 76};
        for (int i = 0; i < (tablo.length - 1); i++) {
            if(level > tablo[i] && level <= tablo[i+1]) {
                value = tablo[i];
            }
        }
        if (level > 76) {
            value = 75;
	}
        String msg = event.getMessage();
        final Matcher blacklistMatcher = blacklistPattern.matcher(msg);
        if (blacklistMatcher.find()) {
        	while (blacklistMatcher.find()) {
        		msg = msg.replace(blacklistMatcher.group(), (pm.isFrench() ? "Bien joué" : "Good game"));
        	}
    		player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Certains mots ne sont pas tolérée!" : "Many word is not accepted!"));
        }
	if (msg != event.getMessage()) event.setMessage(msg);
	if (metaData.getPrefix() != null) {
		event.setFormat(ChatColor.translateAlternateColorCodes('&',ChatColor.GRAY + "(" + ChatColor.valueOf(Color.getColorByValue(value).toString()).toString() + level + ChatColor.GRAY + ") " + ChatColor.RESET + metaData.getPrefix() + " " + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s"));
		return;
	}
	event.setFormat(ChatColor.translateAlternateColorCodes('&',ChatColor.GRAY + "(" + ChatColor.valueOf(Color.getColorByValue(value).toString()).toString() + level + ChatColor.GRAY + ") " + ChatColor.GRAY + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s"));
	}
}
