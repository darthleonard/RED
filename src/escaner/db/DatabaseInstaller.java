package escaner.db;

import java.sql.SQLException;

public class DatabaseInstaller {
	public void Install(DatabaseConnection connection) throws SQLException {
		createDeviceTable(connection);
	}
	
	private void createDeviceTable(DatabaseConnection connection) throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS Devices (\n"
				+ "	Id INTEGER NOT NULL DEFAULT 0,\n"
				+ "	IpAddress TEXT,\n"
				+ "	MacAddress TEXT,\n"
				+ "	Description TEXT,\n"
				+ "	PRIMARY KEY(Id AUTOINCREMENT)\n"
				+ ");";
		connection.ExecuteQuery(query);
		
	}
}
