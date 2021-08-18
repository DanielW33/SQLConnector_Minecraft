package me.MuchDan.SQLConnector.Util;

import me.MuchDan.SQLConnector.SQLConnector;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GetSQL {
    private SQLConnector plugin;
    public GetSQL(SQLConnector plugin){
        this.plugin = plugin;
    }
    public void CreateTable() throws SQLException {
        PreparedStatement preparedStatement; //all SQL is run through this preparedstatement.
        preparedStatement = plugin.getConnector().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getTable() + "(NAME VARCHAR(100), UUID VARCHAR(100), PRIMARY KEY (NAME))");
        preparedStatement.executeUpdate();
    }
    public void CreatePlayer(Player player) throws SQLException {
        UUID uuid = player.getUniqueId();
        if(!exists(uuid)){
            PreparedStatement preparedStatement1 = plugin.getConnector().getConnection().prepareStatement("INSERT INTO " + plugin.getTable() + " (NAME,UUID) VALUES (?,?)");
            preparedStatement1.setString(1, player.getName());
            preparedStatement1.setString(2,uuid.toString());
            preparedStatement1.executeUpdate();
            return;
        }
    }
    public void DeletePlayer(Player player) throws SQLException {
        if(exists(player.getUniqueId())){
            PreparedStatement preparedStatement = plugin.getConnector().getConnection().prepareStatement("DELETE FROM " + plugin.getTable() + " WHERE NAME=?");
            preparedStatement.setString(1,player.getName());
            preparedStatement.executeUpdate();
        }
    }
    public boolean exists(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = plugin.getConnector().getConnection().prepareStatement("SELECT * FROM " + plugin.getTable() + " Where UUID=?");
        preparedStatement.setString(1,uuid.toString());

        ResultSet results = preparedStatement.executeQuery();
        if(results.next()){
            return true;
        }
        return false;
    }

    //In order to set in SQL
    //PreparedStatement preparedstatement = plugin.getconnector().getConnection().prepareStatement("UPDATE " + plugin.getTable() + " SET " WHatever + "=? WHERE UUID=?");
    //preparedstatement.setWhateverType(1, Whateber)
    //preparedstatement.setWhateverType(2, Whatever);
    //preparedstatement.executeUpdate();

    public String getName(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = plugin.getConnector().getConnection().prepareStatement("SELECT NAME FROM " + plugin.getTable() + " WHERE UUID=?");
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        String Name = "";
        if(rs.next()){
            Name = rs.getString("NAME");
            return Name;
        }
        return "NULL";
    }
}
