package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SocialStats {
	
	private int DefaultValue = 0;

	public int get(long UserFromID, long UserToID, String statName) {
		String sql = "SELECT * FROM SocialStats WHERE UserFromID = ? AND StatName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(UserFromID));
			statement.setString(2, statName);
			try(final ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()) {
    				if(resultSet.getString("UserToID").equals(String.valueOf(UserToID))) {
    					return resultSet.getInt("StatValue");
    				}
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return DefaultValue;
	}
	
	public HashMap<String,Integer> getAll(long UserFromID, long UserToID){
		String sql = "SELECT * FROM SocialStats WHERE UserFromID = ? AND UserToID = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(UserFromID));
			statement.setString(2, String.valueOf(UserToID));
			try(final ResultSet resultSet = statement.executeQuery()){
				HashMap<String,Integer> map = new HashMap<String,Integer>();
				while(resultSet.next()) {
					map.put(resultSet.getString("StatName"), resultSet.getInt("StatValue"));
    			}
				return map;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new HashMap<String,Integer>();
	}
	
//	public void insert(long UserFromID, long UserToID, String statName, int value) {
//		String sql = "INSERT INTO SocialStats (UserFromID,UserToID,StatName,StatValue) VALUES (?,?,?,?)";
//		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
//			statement.setString(1, String.valueOf(UserFromID));
//			statement.setString(2, String.valueOf(UserToID));
//			statement.setString(3, statName);
//			statement.setInt(4, value);
//			statement.execute();
//			connection.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void set(long UserFromID, long UserToID, String statName, int statValue) {
		String sql = "REPLACE SocialStats SET StatValue = ? WHERE UserFromID = ? AND UserToID = ? AND StatName = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setInt(1, statValue);
			statement.setString(2, String.valueOf(UserFromID));
			statement.setString(3, String.valueOf(UserToID));
			statement.setString(4, statName);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
