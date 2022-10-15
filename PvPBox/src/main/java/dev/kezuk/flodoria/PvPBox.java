package dev.kezuk.flodoria;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.bizarrealex.aether.Aether;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import dev.kezuk.flodoria.board.PvPBoxBoard;
import dev.kezuk.flodoria.command.BroadcastCommand;
import dev.kezuk.flodoria.command.BuildCommand;
import dev.kezuk.flodoria.command.PingCommand;
import dev.kezuk.flodoria.command.StatsCommand;
import dev.kezuk.flodoria.database.BoxSQL;
import dev.kezuk.flodoria.listener.ChatListener;
import dev.kezuk.flodoria.listener.EntityListener;
import dev.kezuk.flodoria.listener.PlayerListener;
import dev.kezuk.flodoria.listener.ServerListener;
import dev.kezuk.flodoria.low.InventoryManager;
import dev.kezuk.flodoria.low.PlayerManager;
import dev.kezuk.flodoria.tasks.CombatTag;
import net.luckperms.api.LuckPerms;

public class PvPBox extends JavaPlugin
{
    public static PvPBox instance;
    private String configPath;
    public LuckPerms luckPerms;
    public Connection connection;
    public BoxSQL practiceDB = new BoxSQL();
    public InventoryManager inventoryManager;
    
    public PvPBox() {
        this.inventoryManager = new InventoryManager();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void onEnable() {
        PvPBox.instance = this;
        this.luckPerms = (LuckPerms)this.getServer().getServicesManager().load((Class)LuckPerms.class);
        this.registerRessource();
		this.setupHikariCP();
		this.setupDatabase();
	    this.registerListeners();
        new Aether(this, new PvPBoxBoard());
        this.registerCommand();
        this.getServer().getScheduler().runTaskTimer((Plugin)this, (Runnable)new CombatTag(), (10 * 20), (10 * 20));
    }
    
    private void registerCommand() {
        this.getCommand("build").setExecutor((CommandExecutor)new BuildCommand());
        this.getCommand("stats").setExecutor((CommandExecutor)new StatsCommand());
        this.getCommand("ping").setExecutor((CommandExecutor)new PingCommand());
        this.getCommand("broadcast").setExecutor((CommandExecutor)new BroadcastCommand());
    }
    
    public void onDisable() {
        for (final Player players : Bukkit.getOnlinePlayers()) {
            final PlayerManager pm = PlayerManager.getPlayers().get(players.getUniqueId());
            pm.disconnect(players);
        }
    }
    
    private void registerListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new PlayerListener(), (Plugin)this);
        pm.registerEvents((Listener)new EntityListener(), (Plugin)this);
        pm.registerEvents((Listener)new ServerListener(), (Plugin)this);
        pm.registerEvents((Listener)new ChatListener(), (Plugin)this);
    }
    
    private void registerRessource() {
        this.configPath = this.getDataFolder() + "/hikari.properties";
        this.saveResource("hikari.properties", false);
    }
    
    private void setupDatabase() {
        if (this.connection != null) {
            this.practiceDB.createPlayerManagerTable();
            return;
        }
        System.out.println("WARNING enter valid database information (" + this.configPath + ") \n You will not be able to access many features");
    }
    
    private void setupHikariCP() {
        try {
            final HikariConfig config = new HikariConfig(this.configPath);
            @SuppressWarnings("resource")
			final HikariDataSource ds = new HikariDataSource(config);
            final String passwd = (config.getDataSourceProperties().getProperty("password") == null) ? "" : config.getDataSourceProperties().getProperty("password");
            final Database db = BukkitDB.createHikariDatabase(this, config.getDataSourceProperties().getProperty("user"), passwd, config.getDataSourceProperties().getProperty("databaseName"), config.getDataSourceProperties().getProperty("serverName") + ":" + config.getDataSourceProperties().getProperty("portNumber"));
            DB.setGlobalDatabase(db);
            this.connection = ds.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Error could not connect to SQL database.");
            e.printStackTrace();
        }
        System.out.println("Successfully connected to the SQL database.");
    }
    
    public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
	
	public Connection getConnection() {
		return connection;
	}
    
    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }
    
    public static PvPBox getInstance() {
        return PvPBox.instance;
    }
}
