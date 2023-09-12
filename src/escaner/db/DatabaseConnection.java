package escaner.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	private final String URL = "jdbc:sqlite:red.db";
	private final String DRIVER = "org.sqlite.JDBC";
	
	public void SelectAll() throws SQLException {
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
		String query = "SELECT * FROM Devices";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			System.out.println(rs.getInt("id") + "\t" +
					rs.getString("IpAddress") + "\t" +
					rs.getString("IpAddress") + "\t" +
					rs.getString("Description"));
		}

		connection.close();
	}
	
	public boolean DatabaseInstalled() throws SQLException {
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
		String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='Devices'";
		ResultSet rs = stmt.executeQuery(query);;
		boolean tableExist = rs.next();
		connection.close();
		return tableExist;
	} 
	
	public void Insert(String ipAddress, String macAddress, String Description) throws SQLException {
		Connection connection = getConnection();
		String query = "INSERT INTO Devices (IpAddress, MacAddress, Description) VALUES(?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, ipAddress);
		pstmt.setString(2, macAddress);
		pstmt.setString(3, Description);
		pstmt.executeUpdate();
		connection.close();
	}
	
	public void ExecuteQuery(String query) throws SQLException {
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
		stmt.execute(query);
		connection.close();
	}
	
	private Connection getConnection() throws SQLException {
		Connection c = null;
	    try {
	    	Class.forName(DRIVER).newInstance();
	    	c = DriverManager.getConnection(URL);
	    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
	    	throw new SQLException(e.getMessage());
	    }
	    
	    return c;
	}
}
