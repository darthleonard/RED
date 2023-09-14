package escaner.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import escaner.db.DatabaseConnection;

public class DeviceRecord {
	private int id;
	private String ipAddress;
	private String macAddress;
	private String description;
	
	public DeviceRecord() { }
	
	public DeviceRecord(int id) throws SQLException {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection connection = databaseConnection.getConnection();
		executeSelect(connection, id);
		connection.close();
	}
	
	public void Save() throws SQLException {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection connection = databaseConnection.getConnection();
		if(id == 0) {
			executeInsert(connection);
		} else {
			executeUpdate(connection);
		}
		connection.close();
	}
	
	public void Delete() throws SQLException {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection connection = databaseConnection.getConnection();
		executeDelete(connection);
		connection.close();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private void executeSelect(Connection connection, int id) throws SQLException {
		String query = "SELECT * FROM Devices WHERE id=?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			this.id = rs.getInt("id");
			ipAddress = rs.getString("IpAddress");
			macAddress = rs.getString("MacAddress");
			description = rs.getString("Description");
		}
	}
	
	private void executeInsert(Connection connection) throws SQLException {
		String query = "INSERT INTO Devices (IpAddress, MacAddress, Description) VALUES(?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, ipAddress);
		pstmt.setString(2, macAddress);
		pstmt.setString(3, description);
		pstmt.executeUpdate();
		
		Statement stmt = connection.createStatement();
		String getIdQuery = "SELECT seq FROM sqlite_sequence WHERE name='Devices'";
		ResultSet rs = stmt.executeQuery(getIdQuery);
		if(rs.next()) {
			id = rs.getInt(1);
		} else {
			throw new SQLException("Save Failed");
		}
	}
	
	private void executeUpdate(Connection connection) throws SQLException {
		String query = "UPDATE Devices SET IpAddress=?, MacAddress=?, Description=? WHERE id=?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, ipAddress);
		pstmt.setString(2, macAddress);
		pstmt.setString(3, description);
		pstmt.setInt(4, id);
		pstmt.executeUpdate();
	}
	
	private void executeDelete(Connection connection) throws SQLException {
		String query = "DELETE FROM Devices WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}
}
