package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarnActions {
	
	public final String DefaultValue = "false";
	public String get(long guildId, int warnAmount) {
		String sql = "SELECT * FROM GuildSettings WHERE GuildId = ? AND WarnAmount = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setInt(2, warnAmount);
			try(final ResultSet resultSet = statement.executeQuery()){
				connection.commit();
				if(resultSet.next()) {
    				return resultSet.getString("WarnAction");
    			}else {
    				return null;
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return DefaultValue;
	}
	
	public void insert(long guildId, int warnAmount) {
		String sql = "INSERT INTO GuildSettings (GuildId,WarnAmount,WarnAction) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setInt(2, warnAmount);
			statement.setString(3, DefaultValue);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void set(long guildId, String settingName, String settingValue) {
		String sql = "UPDATE GuildSettings SET SettingValue = ? WHERE GuildId = ? AND SettingName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, settingValue);
			statement.setString(2, String.valueOf(guildId));
			statement.setString(3, settingName);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
