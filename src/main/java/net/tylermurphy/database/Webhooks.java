package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Webhooks {

	public HashMap<String,String> get(long guildId, String Prefix) {
		String sql = "SELECT * FROM Webhooks WHERE Prefix = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, Prefix);
			statement.setString(2, String.valueOf(guildId));
			try(final ResultSet resultSet = statement.executeQuery()){
    			if(resultSet.next()) {
    				HashMap<String,String> data = new HashMap<String,String>();
    				data.put("AvatarURL", resultSet.getString("AvatarURL"));
    				data.put("Name", resultSet.getString("Name"));
    				connection.commit();
    				return data;
    			}
    			return null;
    		}
		} catch (SQLException e) {

		}
		return null;
	}
	
//	public void insert(long guildId, String AvatarURL, String Name, String Prefix) {
//		String sql = "INSERT INTO Webhooks (GuildId,AvatarURL,Name,Prefix) VALUES (?,?,?,?)";
//		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
//			statement.setString(1, String.valueOf(guildId));
//			statement.setString(2, AvatarURL);
//			statement.setString(3, Name);
//			statement.setString(4, Prefix);
//			statement.execute();
//			connection.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void del(long guildId, String Prefix) {
		String sql = "DELETE FROM Webhooks WHERE Prefix = ? AND GuildId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, Prefix);
			statement.setString(2, String.valueOf(guildId));
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void set(long guildId, String AvatarURL, String Name, String Prefix) {
		String sql = "REPLACE INTO Webhooks (GuildId,AvatarURL,Name,Prefix) VALUES (?,?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setString(2, AvatarURL);
			statement.setString(3, Name);
			statement.setString(4, Prefix);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
