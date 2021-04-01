package net.tylermurphy.Kenbot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Warnings {

	public Map<Integer, String> getAll(long userId, long guildId) {
		String sql = "SELECT * FROM Warnings WHERE UserId = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
				Map<Integer,String> data = new HashMap<Integer,String>();
				while(resultSet.next()) {
    				data.put(resultSet.getInt("WarnId"), resultSet.getString("WarnMessage"));
    			}
    			connection.commit();
    			return data;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insert(long userId, long guildId, String warnMessage) {
		String sql = "INSERT INTO Warnings (UserId,GuildId,WarnMessage) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.setString(3, warnMessage);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void revoke(int warnId) {
		String sql = "DELETE FROM Warnings WHERE WarnId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setInt(1, warnId);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void revokeAll(long userId, long guildId) {
		String sql = "DELETE FROM Warnings WHERE UserId = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
