package dev.kezuk.flodoria.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.enums.PlayerStatus;
import dev.kezuk.flodoria.low.PlayerManager;

public class BuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { return false; }
		Player player = (Player) sender;
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (!player.hasPermission("flodaria.admin")) {
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Vous n'avez pas la permissions requise!" : "You don't have the required permission!"));
			player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Permission requis: " : "Required permission: ") + "flodaria.admin");
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
			return false;
		}
		if (pm.getPlayerState() != PlayerStatus.SPAWN) {
			player.sendMessage("");
			return false;
		}
		if (pm.isBuild()) {
			pm.setBuild(false);
			player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Vous pouvez plus construire dès à présent!" : "You can't build now!"));
			pm.sendToSpawn();
			return false;
		}
		player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Vous pouvez dès à présent construire!" : "You can build now!"));
		pm.setBuild(true);
		player.setGameMode(GameMode.CREATIVE);
		return false;
	}

}
