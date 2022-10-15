package dev.kezuk.flodoria.board;

import dev.kezuk.flodoria.*;
import org.bukkit.entity.*;
import com.bizarrealex.aether.scoreboard.*;
import com.bizarrealex.aether.scoreboard.cooldown.*;
import java.util.*;
import org.bukkit.*;
import dev.kezuk.flodoria.low.*;

public class PvPBoxBoard implements BoardAdapter
{
    private final PvPBox plugin;
    
    public PvPBoxBoard() {
        this.plugin = PvPBox.getInstance();
    }
    
    @Override
    public String getTitle(final Player player) {
        return ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "PvP " + ChatColor.WHITE + "♦" + ChatColor.GREEN + " Box";
    }
    
    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final PlayerManager playerData = PlayerManager.getPlayers().get(player.getUniqueId());
        if (playerData == null) {
            this.plugin.getLogger().warning(String.valueOf(player.getName()) + "'s player data is null");
            return null;
        }
        switch (playerData.getPlayerState()) {
            case SPAWN: {
                return this.getLobbyBoard(player);
            }
            case SPAWNING:
            	return this.getGameBoard(player);
            case GAME: {
                return this.getGameBoard(player);
            }
            default: {
                return null;
            }
        }
    }
    
    private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
        board.add(String.valueOf(ChatColor.GRAY.toString()) + ChatColor.STRIKETHROUGH + "-»---------------«-");
        board.add(ChatColor.WHITE + (pm.isFrench() ? "Connectée" : "Online") + ChatColor.WHITE + " » " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
        String sizeInFight = ChatColor.GREEN.toString() + FightManager.getInbox().size();
        board.add(ChatColor.WHITE + (pm.isFrench() ? "En jeu" : "In Game") + ChatColor.WHITE + " » "  + sizeInFight);
        board.add("");
        board.add(ChatColor.DARK_GRAY + " • " + ChatColor.DARK_GREEN + (pm.isFrench() ? " Statistiques" : " Statistics") + ChatColor.DARK_GRAY + " • ");
        board.add("");
        board.add(ChatColor.WHITE + " FlodoCoins" + ChatColor.WHITE + " ► "  + ChatColor.GREEN + pm.getCoins());
        board.add(ChatColor.WHITE + (pm.isFrench() ? " Niveau" : " Level") + ChatColor.WHITE + " ► "  + ChatColor.GREEN + pm.getLevel());
        board.add(String.valueOf(ChatColor.GRAY.toString()) + ChatColor.STRIKETHROUGH + "-»---------------«-");
        board.add(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.ITALIC + "play.flodoria.com");
        return board;
    }
    
    private List<String> getGameBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
        board.add(String.valueOf(ChatColor.GRAY.toString()) + ChatColor.STRIKETHROUGH + "-»---------------«-");
        int withYou = FightManager.getInbox().size() - 1;
        board.add(ChatColor.DARK_GRAY + " • " + ChatColor.GREEN + "Informations" + ChatColor.DARK_GRAY + " • ");
        final FightManager fm = FightManager.getInbox().get(player.getUniqueId());
        if (fm != null && fm.getOpponent() != null) {
            board.add(ChatColor.WHITE + (pm.isFrench() ? " Ennemi" : " Opponent") + ChatColor.WHITE + " ► "  + ChatColor.RED + Bukkit.getPlayer(fm.getOpponent()).getName());
            board.add(ChatColor.WHITE + (pm.isFrench() ? " Son ping" : " Their ping") + ChatColor.WHITE + " ► "  + ChatColor.RED + Bukkit.getPlayer(fm.getOpponent()).getPing());
        }
        if (fm != null && fm.getOpponent() == null) {
        	board.add(ChatColor.WHITE + (pm.isFrench() ? " Avec vous" : " With you") + ChatColor.WHITE + " ► "  + ChatColor.GREEN + withYou);
        }
        board.add("");
        board.add(ChatColor.DARK_GRAY + " • " + ChatColor.DARK_GREEN + (pm.isFrench() ? " Statistiques" : " Statistics") + ChatColor.DARK_GRAY + " • ");
        board.add(ChatColor.WHITE + (pm.isFrench() ? " Tuer" : " Kill") + ChatColor.WHITE + " ► " + ChatColor.GREEN + pm.getKill());
        board.add(ChatColor.WHITE + (pm.isFrench() ? " Mort" : " Death") + ChatColor.WHITE + " ► " + ChatColor.GREEN + pm.getDeath());
        board.add(ChatColor.WHITE + " EXP" + ChatColor.WHITE + " ► "  + ChatColor.GREEN + pm.getExp());
        board.add(String.valueOf(ChatColor.GRAY.toString()) + ChatColor.STRIKETHROUGH + "-»---------------«-");
        board.add(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.ITALIC + "play.flodoria.com");
        return board;
    }
}
