package escaner.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import escaner.db.DatabaseConnection;

public class DeviceDataset extends ArrayList<DeviceRecord> {
	private static final long serialVersionUID = 1L;

	public DeviceDataset() {
	}

	public DeviceDataset(String filter) {
		load();
	}

	private void load() {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection connection = null;
		try {
			connection = databaseConnection.getConnection();
			Statement stmt = connection.createStatement();
			String query = "SELECT * FROM Devices";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				DeviceRecord device = new DeviceRecord();
				device.setId(rs.getInt("id"));
				device.setIpAddress(rs.getString("IpAddress"));
				device.setMacAddress(rs.getString("IpAddress"));
				device.setDescription(rs.getString("Description"));
				add(device);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
