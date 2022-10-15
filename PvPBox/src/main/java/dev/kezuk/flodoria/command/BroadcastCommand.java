package dev.kezuk.flodoria.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.low.PlayerManager;

public class BroadcastCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {return false;}
		Player player = (Player) sender;
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (!player.hasPermission("flodaria.admin")) {
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Vous n'avez pas la permissions requise!" : "You don't have the required permission!"));
			player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Permission requis: " : "Required permission: ") + "flodaria.admin");
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			return false;
		}
		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "/broadcast <announce>");
			return false;
		}
        final StringBuilder str = new StringBuilder();
        for (int j = 0; j < args.length; ++j) {
            str.append(args[j] + " ");
        }
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(" ");
			players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			players.sendMessage(ChatColor.DARK_GREEN + (pm.isFrench() ? "Annonces" : "Broadcast") + ChatColor.GRAY + ":");
			players.sendMessage(ChatColor.RED + str.toString());
			players.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			players.sendMessage(" ");
		}
		return false;
	}

}
