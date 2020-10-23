package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelfRoles {

	public String get(String locationId, String reaction) {
		String sql = "SELECT * FROM SelfRoles WHERE LocationId = ? AND Reaction = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, locationId);
			statement.setString(2, reaction);
			try(final ResultSet resultSet = statement.executeQuery()){
    			if(resultSet.next()) {
    				connection.commit();
    				return resultSet.getString("RoleId");
    			}
    			return null;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getAll(String locationId) {
		String sql = "SELECT * FROM SelfRoles WHERE LocationId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, locationId);
			try(final ResultSet resultSet = statement.executeQuery()){
				List<String> roles = new ArrayList<String>();
    			while(resultSet.next()) {
    				roles.add(resultSet.getString("RoleId"));
    			}
    			connection.commit();
    			return roles;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public void set(String locationId, long roleId, String reaction) {
		String sql = "REPLACE INTO SelfRoles (LocationId,RoleId,Reaction) VALUES (?,?,?)";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, locationId);
			statement.setString(2, String.valueOf(roleId));
			statement.setString(3, reaction);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void del(String locationId) {
		String sql = "DELETE FROM SelfRoles WHERE LocationId = ?";
		try( Connection connection = MariaDBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ){
			statement.setString(1, locationId);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
