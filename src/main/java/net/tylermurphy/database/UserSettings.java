package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSettings {

	public String get(long userId, long guildId, String settingName) {
		String sql = "SELECT * FROM UserSettings WHERE UserId = ? AND GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.setString(3, settingName);
			try(final ResultSet resultSet = statement.executeQuery()){
    			if(resultSet.next()) {
					return resultSet.getString("SettingValue");
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getAll(long guildId, String settingName) {
		String sql = "SELECT * FROM UserSettings WHERE GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, settingName);
			try(final ResultSet resultSet = statement.executeQuery()){
    			List<String> data = new ArrayList<String>();
				while(resultSet.next()) {
					String s = resultSet.getString("UserId") + ":" + resultSet.getString("SettingValue");
    				data.add(s);
    			}
    			return data;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public void set(long userId, long guildId, String settingName, String settingValue) {
		String sql = "REPLACE INTO UserSettings (UserId,GuildId,SettingName,SettingValue) VALUES (?,?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.setString(3, settingName);
			statement.setString(4, settingValue);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(long userId, long guildId, String settingName) {
		String sql = "DELETE FROM UserSettings WHERE UserId = ? AND GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.setString(3, settingName);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUserFromValue(long guildId, String settingName, String settingValue) {
		String sql = "SELECT * FROM UserSettings WHERE GuildId = ? AND SettingName = ? AND SettingValue = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, settingName);
			statement.setString(3, settingValue);
			try(final ResultSet resultSet = statement.executeQuery()){
    			while(resultSet.next()) {
    				if(resultSet.next()) {
    					return resultSet.getString("UserId");
    				}
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
