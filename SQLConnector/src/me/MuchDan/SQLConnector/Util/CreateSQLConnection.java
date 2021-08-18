package me.MuchDan.SQLConnector.Util;

import me.MuchDan.SQLConnector.ConfigManagers.UtilManager;
import me.MuchDan.SQLConnector.SQLConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class CreateSQLConnection {
    private SQLConnector plugin;
    private UtilManager UtilData;
    private Connection connection;
    private String host, database, username, password, table, ssl;
    private int port;

    public CreateSQLConnection(SQLConnector plugin) {
        this.plugin = plugin;
        UtilData = this.plugin.getUtilData();
        this.port = 0;
    }

    public boolean SQLSetup() throws SQLException, ClassNotFoundException {
        if (UtilData.getConfig().getBoolean("SQL.isEnabled")) {
            host = UtilData.getConfig().getString("SQL.host");
            database = UtilData.getConfig().getString("SQL.database");
            username = UtilData.getConfig().getString("SQL.username");
            password = UtilData.getConfig().getString("SQL.password");
            port = UtilData.getConfig().getInt("SQL.port");
            table = UtilData.getConfig().getString("SQL.table");
            ssl = UtilData.getConfig().getString("SQL.ssl");
            System.out.println(host + " " + database + " " + username + " " + password + " " + port + " " + table);
            if (host == null || database == null || username == null || password == null || port == 0) {
                plugin.getLogger().log(Level.SEVERE, "Failed to pull mysql data from config: UtilConfig.yml. Please check for config errors and try again.");
                return false;
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl,username,password);
        }
        return true;
    }
    public boolean isConnected(){
        return (connection == null ? false : true);
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }
    public void disconnect() throws SQLException {
        if(isConnected()){
            connection.close();
        }
    }
}
