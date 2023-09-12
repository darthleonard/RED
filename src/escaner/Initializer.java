package escaner;

import java.sql.SQLException;

import escaner.db.DatabaseConnection;
import escaner.db.DatabaseInstaller;

public class Initializer {
	public void Init() throws SQLException {
		DatabaseConnection connection = new DatabaseConnection();
		System.out.println("Validating database");
		if(!connection.DatabaseInstalled()) {
			System.out.println("Installing database");
			DatabaseInstaller dbi = new DatabaseInstaller();
			dbi.Install(connection);
			System.out.println("Database succesfully installed");
		}
		System.out.println("Starting software");
		//connection.Insert("192.168.1.64", "some mac", "description of device");
		connection.SelectAll();
	}
}
