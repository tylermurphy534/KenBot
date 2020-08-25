package net.tylermurphy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.tylermurphy.Config;

public class MariaDBConnection {
	private static final HikariConfig config = new HikariConfig();
	private static final HikariDataSource ds;
	
	static {

		config.setJdbcUrl("jdbc:mariadb://"+Config.DATABASE_HOST+":"+Config.DATABASE_PORT+"/kenbot");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("user", Config.DATABASE_USER);
		config.addDataSourceProperty("password", Config.DATABASE_PASSWORD);
		config.setDriverClassName("org.mariadb.jdbc.Driver");
		config.setAutoCommit(false);
		config.setMaximumPoolSize(20);
		ds = new HikariDataSource(config);
		
		try(final Statement statement = getConnection().createStatement()){
			statement.execute("CREATE TABLE IF NOT EXISTS GuildSettings (" +
					"GuildId VARCHAR(20) NOT NULL," +
					"SettingName VARCHAR(20) NOT NULL," +
					"SettingValue VARCHAR(150) NOT NULL," +
					"INDEX compound(GuildId, SettingName)"+
					");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(final Statement statement = getConnection().createStatement()){
			statement.execute("CREATE TABLE IF NOT EXISTS SocialStats (" +
					"UserFromID VARCHAR(20) NOT NULL," +
					"UserToID VARCHAR(20) NOT NULL," +
					"StatName VARCHAR(20) NOT NULL," +
					"StatValue INTEGER NOT NULL," +
					"INDEX compound(UserFromID, StatName)"+
					");");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try(final Statement statement = getConnection().createStatement()){
			statement.execute("CREATE TABLE IF NOT EXISTS UserSettings (" +
					"UserId VARCHAR(20) NOT NULL," +
					"GuildId VARCHAR(20) NOT NULL," +
					"SettingName VARCHAR(20) NOT NULL," +
					"SettingValue VARCHAR(20) NOT NULL," +
					"INDEX compound(UserId, SettingName)"+
					");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(final Statement statement = getConnection().createStatement()){
			statement.execute("CREATE TABLE IF NOT EXISTS SelfRoles (" +
					"LocationId VARCHAR(60) NOT NULL," +
					"RoleId VARCHAR(20) NOT NULL,"+
					"Reaction VARCHAR(20) NOT NULL,"+
					"INDEX compound(LocationId, Reaction)"+
					");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(final Statement statement = getConnection().createStatement()){
			statement.execute("ALTER TABLE SelfRoles CHANGE Reaction Reaction VARCHAR(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(final Statement statement = getConnection().createStatement()){
			statement.execute("CREATE TABLE IF NOT EXISTS Webhooks (" +
					"GuildId VARCHAR(20) NOT NULL,"+
					"AvatarURL TEXT NOT NULL,"+
					"Name VARCHAR(20) NOT NULL,"+
					"Prefix VARCHAR(20) NOT NULL,"+
					"INDEX compound(GuildId, Prefix)"+
					");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private MariaDBConnection() {}
	
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
