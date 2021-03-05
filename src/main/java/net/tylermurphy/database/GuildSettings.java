package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuildSettings {

	public String get(long guildId, String settingName) {
		String sql = "SELECT * FROM GuildSettings WHERE GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, settingName);
			try(final ResultSet resultSet = statement.executeQuery()){
				connection.commit();
				if(resultSet.next()) {
    				return resultSet.getString("SettingValue");
    			}else {
    				return null;
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(long guildId, String settingName, String settingValue) {
		String sql = "REPLACE INTO GuildSettings (GuildId,SettingName,SettingValue) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, settingName);
			statement.setString(3, settingValue);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getAllWithValue(String settingName, String settingValue) {
		String sql = "SELECT * FROM GuildSettings WHERE SettingName = ? AND SettingValue = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, settingName);
			statement.setString(2, settingValue);
			try(final ResultSet resultSet = statement.executeQuery()){
    			List<String> data = new ArrayList<String>();
				while(resultSet.next()) {
    				data.add(resultSet.getString("GuildId"));
    			}
    			return data;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void remove(long guildId, String settingName) {
		String sql = "DELETE FROM GuildSettings WHERE GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, settingName);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
