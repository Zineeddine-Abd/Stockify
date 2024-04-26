package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zaxxer.hikari.HikariDataSource;

import Components.Asset;
import Components.Hardware;
import Components.Software;


public class DB_Utilities {
	
	
	protected static Properties properties;
	protected static HikariDataSource dataSource;
	static {	
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/database.properties"));
			dataSource = new HikariDataSource();
			dataSource.setDriverClassName(properties.getProperty("driver.class.name"));
			dataSource.setJdbcUrl(properties.getProperty("db.url"));
			//tmp
			dataSource.setMinimumIdle(5);
			dataSource.setMaximumPoolSize(1000);
			
			dataSource.setLoginTimeout(2);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static HikariDataSource getDataSource() {
		return dataSource;
	}
	
	protected static void resetSequenceTo1(Connection con, String sequenceName) {
        try (Statement stmt = con.createStatement()){	
        	String alterSequenceQuery = "ALTER SEQUENCE " + sequenceName + " RESTART WITH 1";
			stmt.executeUpdate(alterSequenceQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected static boolean isTableEmpty(String table) {
		try (Connection con = dataSource.getConnection();
			    Statement stmt = con.createStatement()) {
			    String countQuery = "SELECT COUNT(*) FROM " + table;
			    try (ResultSet rs = stmt.executeQuery(countQuery)) {
			        rs.next();
			        int rowCount = rs.getInt(1);
			        if (rowCount == 0) {
			        	return true;
			        } else {
			            return false;
			        }
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		return false;
	}
	 
}
