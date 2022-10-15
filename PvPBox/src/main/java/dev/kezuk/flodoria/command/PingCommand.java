package dev.kezuk.flodoria.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.low.PlayerManager;

//BY NOKSIO
public class PingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if(args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /ping <player>");
			return false;
		}
		Player player = (Player) sender;
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (args.length == 0) {
			player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Votre ping: " : "Your ping: ") + ChatColor.WHITE + player.getPing() + "ms");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
				
		if (target == null) {
			player.sendMessage(ChatColor.RED + "This player is not online.");
			return false;
		}
		if (target == player) {
			player.sendMessage(ChatColor.GREEN +  (pm.isFrench() ? "Votre ping: " : "Your ping: ") + ChatColor.WHITE + player.getPing() + "ms");
			return true;
		}
		player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GREEN + "'s ping: " + ChatColor.WHITE + target.getPing() + "ms");
		player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Différence de ping: " : "Ping difference: ") + ChatColor.WHITE + Math.abs(player.getPing() - target.getPing()) + "ms");
		return true;
	}
}