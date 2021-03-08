package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Twitch {

	public String get(long guildId, String settingName) {
		String sql = "SELECT * FROM Twitch WHERE GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
				connection.commit();
				if(resultSet.next()) {
    				return resultSet.getString(settingName);
    			}else {
    				return null;
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, String> getStack(long guildId) {
		String sql = "SELECT * FROM Twitch WHERE GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
				connection.commit();
				if(resultSet.next()) {
					Map<String, String> data = new HashMap<String, String>();
					data.put("GuildId",resultSet.getString("GuildId"));
					data.put("Status",resultSet.getString("Status"));
					data.put("WebhookId",resultSet.getString("WebhookId"));
					data.put("Login",resultSet.getString("Login"));
					data.put("UserId",resultSet.getString("UserId"));
					data.put("ChannelId",resultSet.getString("ChannelId"));
					data.put("RoleId",resultSet.getString("RoleId"));
					return data;
    			}else {
    				return null;
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String,String>> getAllWithSetting(String channelId, String setting) {
		String sql = "SELECT * FROM Twitch WHERE "+setting+" = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, channelId);
			try(final ResultSet resultSet = statement.executeQuery()){
				connection.commit();
				List<Map<String,String>> store = new ArrayList<Map<String,String>>();
				while(resultSet.next()) {
					Map<String, String> data = new HashMap<String, String>();
					data.put("GuildId",resultSet.getString("GuildId"));
					data.put("Status",resultSet.getString("Status"));
					data.put("WebhookId",resultSet.getString("WebhookId"));
					data.put("Login",resultSet.getString("Login"));
					data.put("UserId",resultSet.getString("UserId"));
					data.put("ChannelId",resultSet.getString("ChannelId"));
					data.put("RoleId",resultSet.getString("RoleId"));
					store.add(data);
    			}
				return store;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Map<String,String> map) {
		String sql = "REPLACE INTO Twitch (GuildId,Status,WebhookId,Login,UserId,ChannelId,RoleId) VALUES (?,?,?,?,?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, map.get("GuildId"));
			statement.setString(2, map.get("Status"));
			statement.setString(3, map.get("WebhookId"));
			statement.setString(4, map.get("Login"));
			statement.setString(5, map.get("UserId"));
			statement.setString(6, map.get("ChannelId"));
			statement.setString(7, map.get("RoleId"));
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void remove(long guildId) {
		String sql = "DELETE FROM Twitch WHERE GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
