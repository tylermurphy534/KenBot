package net.tylermurphy.Kenbot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Xp {

	public int get(long userId, long guildId) {
		String sql = "SELECT * FROM Xp WHERE UserId = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
    			if(resultSet.next()) {
					return resultSet.getInt("Xp");
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<String> getAll(long guildId) {
		String sql = "SELECT * FROM Xp WHERE GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
    			List<String> data = new ArrayList<String>();
				while(resultSet.next()) {
					String s = resultSet.getString("UserId") + ":" + resultSet.getString("Xp");
    				data.add(s);
    			}
    			return data;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public void set(long userId, long guildId, int xp) {
		String sql = "REPLACE INTO Xp (UserId,GuildId,Xp) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.setInt(3, xp);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(long userId, long guildId) {
		String sql = "DELETE FROM Xp WHERE UserId = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(userId));
			statement.setString(2, String.valueOf(guildId));
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
