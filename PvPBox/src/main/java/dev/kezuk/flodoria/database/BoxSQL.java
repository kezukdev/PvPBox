package dev.kezuk.flodoria.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import co.aikar.idb.DB;
import co.aikar.idb.DbStatement;
import dev.kezuk.flodoria.PvPBox;

public class BoxSQL {

    public boolean createPlayerManagerTable() {
        return DB.createTransaction(stm -> createPlayerManagerTable(stm));
    }

    private boolean createPlayerManagerTable(DbStatement stm) {
        String player_manager = "CREATE TABLE playersdata ("
                + "ID INT(64) NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(16) NOT NULL,"
                + "uuid VARCHAR(64) NOT NULL,"
                + "kills INT(40) DEFAULT '0',"
                + "deaths INT(40) DEFAULT '0',"
                + "exps DECIMAL(50,30) DEFAULT '0.0',"
                + "needexp DECIMAL(50,30) DEFAULT '5.0',"
                + "levels INT(64) DEFAULT '1',"
                + "coins INT(64) DEFAULT '0',"
                + "french BOOLEAN DEFAULT '0',"
                + "PRIMARY KEY (`ID`))";
        try {
            DatabaseMetaData dbm = PvPBox.getInstance().getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "playersdata", null);
            if (tables.next()) {
                //table exist
                return false;
            } else {
                //table doesn't exist
                stm.executeUpdateQuery(player_manager);
                System.out.println("SUCESS create playersdata table.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("ERROR while creating playersdata table.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean createPlayerManager(UUID uuid, String name)
    {
        return DB.createTransaction(stm -> createPlayerManager(uuid, name, stm));
    }

    private boolean createPlayerManager(UUID uuid, String name, DbStatement stm) {
        String query = "INSERT INTO playersdata (uuid, name) " +
                "VALUES (?, ?)";
        try {
            return stm.executeUpdateQuery(query, uuid.toString(), name) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePlayerManager(String name, UUID uuid)
    {
        return DB.createTransaction(stm -> updatePlayerManager(name, uuid, stm));
    }

    private boolean updatePlayerManager(String name, UUID uuid, DbStatement stm) {
        String query = "UPDATE playersdata SET name=? WHERE uuid=?";
        try {
            return stm.executeUpdateQuery(query, name, uuid.toString()) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existPlayerManager(UUID uuid)
    {
        return DB.createTransaction(stm -> existPlayerManager(uuid, stm));
    }

    private boolean existPlayerManager(UUID uuid, DbStatement stm) {
        String query = "SELECT * FROM playersdata WHERE uuid=?";
        try {
            return stm.executeQueryGetFirstRow(query, uuid.toString()) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}