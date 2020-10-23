package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarnActions {
	
	public String get(long guildId, int warnAmount) {
		String sql = "SELECT * FROM WarnActions WHERE GuildId = ? AND WarnAmount = ?";
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
		return null;
	}
	
//	public void insert(long guildId, int warnAmount, String warnAction) {
//		String sql = "INSERT INTO WarnActions (GuildId,WarnAmount,WarnAction) VALUES (?,?,?)";
//		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
//			statement.setString(1, String.valueOf(guildId));
//			statement.setInt(2, warnAmount);
//			statement.setString(3, warnAction);
//			statement.execute();
//			connection.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void set(long guildId, int warnAmount, String warnAction) {
//		if(get(guildId,warnAmount) == null) { insert(guildId,warnAmount,warnAction); return; }
		String sql = "REPLACE INTO WarnActions (GuildId,WarnAmount,WarnAction) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setInt(2, warnAmount);
			statement.setString(3, warnAction);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void remove(long guildId, int warnAmount) {
		String sql = "DELETE FROM WarnActions WHERE GuildId = ? AND WarnAmount = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, String.valueOf(guildId));
			statement.setInt(2, warnAmount);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
