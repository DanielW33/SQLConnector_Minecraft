package me.MuchDan.SQLConnector;

import me.MuchDan.SQLConnector.Commands.OnTestCommand;
import me.MuchDan.SQLConnector.ConfigManagers.UtilManager;
import me.MuchDan.SQLConnector.Util.CreateSQLConnection;
import me.MuchDan.SQLConnector.Util.GetSQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class SQLConnector extends JavaPlugin {
    private UtilManager UtilData;
    private CreateSQLConnection Connector;
    private GetSQL SQLData;
    @Override
    public void onEnable(){
        this.UtilData = new UtilManager(this);
        this.UtilData.getConfig().options().copyDefaults(false);
        this.UtilData.saveDefaultConfig();

        Connector = new CreateSQLConnection(this);
        SQLData = new GetSQL(this);

        try {
            if(!Connector.SQLSetup()){
                this.getServer().getPluginManager().disablePlugin(this);
                this.getLogger().log(Level.SEVERE, "Failed to connect to MYSQL database.");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            this.getLogger().log(Level.SEVERE, "Failed to connect to MYSQL database.");
        }
        if(Connector.isConnected()){
            this.getLogger().log(Level.INFO, "Successfully connected to MySql database.");
            try {
                SQLData.CreateTable();
            } catch (SQLException throwables) {
                this.getLogger().log(Level.SEVERE, "Failed to create table.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }

        this.getCommand("SQLTest").setExecutor(new OnTestCommand(this));
    }
    @Override
    public void onDisable(){
        try {
            getConnector().disconnect();
        } catch (SQLException throwables) {
            this.getServer().getLogger().log(Level.SEVERE, "Failed to Disconnect from SQL Database.");
        }
    }
    public UtilManager getUtilData(){
        return UtilData;
    }
    public CreateSQLConnection getConnector(){
        return Connector;
    }
    public String getTable(){
        return UtilData.getConfig().getString("SQL.table");
    }
    public GetSQL getSQLData(){
        return SQLData;
    }
}
