package it.polito.ezshop.classes;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteUpdateListener;

public class EZShopDB {
	private SQLiteConnection connection = null;
	private String dbUrl = "jdbc:sqlite:ezshop.db";
	
	public void createConnection() {
		try {
			// create database connection
			this.connection = (SQLiteConnection) DriverManager.getConnection(dbUrl);
			
			//This method will create a listener that will monitor updates in the db in order to 
			//automatically update the data structures 
			connection.addUpdateListener(new SQLiteUpdateListener() {
				@Override
				public void onUpdate(Type type, String db, String table, long rowId) {
					System.out.println("OnUpdate: " + type + " " + db + " " + table + " " + rowId);
					//Update data here
				}
			});
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
