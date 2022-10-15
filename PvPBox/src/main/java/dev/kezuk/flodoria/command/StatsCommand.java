package dev.kezuk.flodoria.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kezuk.flodoria.low.PlayerManager;

public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {return false;}
		Player player = (Player) sender;
		PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (args.length == 0) {
			player.sendMessage(" ");
			player.sendMessage(ChatColor.DARK_GRAY + "          • " + ChatColor.DARK_GREEN + (pm.isFrench() ? "Statistiques" : "Statistics") + ChatColor.DARK_GRAY + " •  ");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.DARK_GRAY + " ♠ " + ChatColor.WHITE + (pm.isFrench() ? "Tuer" : "Kill") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getKill());
			player.sendMessage(ChatColor.DARK_GRAY + " ♣ " + ChatColor.WHITE + (pm.isFrench() ? "Mort" : "Death") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getDeath());
			player.sendMessage(" ");
			player.sendMessage(ChatColor.DARK_GRAY + " ♦ " + ChatColor.WHITE + "EXP" + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getExp() + ChatColor.GRAY + "/" + ChatColor.RED + pm.getNeedExp());
			player.sendMessage(" ");
			return false;
		}
		if (args.length <= 2) {
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == player) {
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + "          • " + ChatColor.DARK_GREEN + (pm.isFrench() ? "Statistiques" : "Statistics") + ChatColor.DARK_GRAY + " •  ");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + " ♠ " + ChatColor.WHITE + (pm.isFrench() ? "Tuer" : "Kill") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getKill());
					player.sendMessage(ChatColor.DARK_GRAY + " ♣ " + ChatColor.WHITE + (pm.isFrench() ? "Mort" : "Death") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getDeath());
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + " ♦ " + ChatColor.WHITE + "EXP" + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getExp() + ChatColor.GRAY + "/" + ChatColor.RED + pm.getNeedExp());
					player.sendMessage(" ");
					return false;
				}
				if (target == null) {
					player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Ce joueur n'est pas en ligne!" : "This player is offline!"));
					return false;
				}
				else {
					PlayerManager pmTarget = PlayerManager.getPlayers().get(target.getUniqueId());
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + "          • " + ChatColor.DARK_GREEN + (pm.isFrench() ? "Statistiques de " : "Statistics of ") + ChatColor.WHITE + target.getName() + ChatColor.DARK_GRAY + " •  ");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + " ♠ " + ChatColor.WHITE + (pm.isFrench() ? "Tuer" : "Kill") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pmTarget.getKill());
					player.sendMessage(ChatColor.DARK_GRAY + " ♣ " + ChatColor.WHITE + (pm.isFrench() ? "Mort" : "Death") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pmTarget.getDeath());
					player.sendMessage(" ");
					player.sendMessage(ChatColor.DARK_GRAY + " ♦ " + ChatColor.WHITE + "EXP" + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pmTarget.getExp() + ChatColor.GRAY + "/" + ChatColor.RED + pmTarget.getNeedExp());
					player.sendMessage(" ");
				}
			}
			if (args[0].equalsIgnoreCase("reset")) {
				if (args.length == 2) {
					if (!player.hasPermission("flodoria.admin")) {
						player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
						player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Vous n'avez pas la permissions requise!" : "You don't have the required permission!"));
						player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Permission requis: " : "Required permission: ") + "flodaria.admin");
						player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "-------------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
						return false;
					}
					Player target = Bukkit.getServer().getPlayer(args[1]);
					if (target == null) {
						player.sendMessage(ChatColor.RED + (pm.isFrench() ? "Ce joueur n'est pas en ligne!" : "This player is offline!"));
						return false;
					}
					else {
						PlayerManager pmTarget = PlayerManager.getPlayers().get(target.getUniqueId());
						player.sendMessage(ChatColor.GREEN + (pm.isFrench() ? "Vous avez reinitialiser les données du joueur " : "You have clear the data of ") + ChatColor.WHITE + target.getName());
						pmTarget.clearData();	
					}
				}
				player.sendMessage(ChatColor.DARK_GRAY + " » " + ChatColor.RED + "/stats reset <player>");
				return false;
			}
			return false;
		}
		player.sendMessage(" ");
		player.sendMessage(ChatColor.DARK_GRAY + "          • " + ChatColor.DARK_GREEN + (pm.isFrench() ? "Statistiques" : "Statistics") + ChatColor.DARK_GRAY + " •  ");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.DARK_GRAY + " ♠ " + ChatColor.WHITE + (pm.isFrench() ? "Tuer" : "Kill") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getKill());
		player.sendMessage(ChatColor.DARK_GRAY + " ♣ " + ChatColor.WHITE + (pm.isFrench() ? "Mort" : "Death") + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getDeath());
		player.sendMessage(" ");
		player.sendMessage(ChatColor.DARK_GRAY + " ♦ " + ChatColor.WHITE + "EXP" + ChatColor.DARK_GRAY + " ► " + ChatColor.GREEN + pm.getExp() + ChatColor.GRAY + "/" + ChatColor.RED + pm.getNeedExp());
		player.sendMessage(" ");
		return false;
	}

}
