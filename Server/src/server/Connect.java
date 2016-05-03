package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for connecting to a database and get information
 * Created by Gustav on 2016-04-06.
 */
public class Connect {

	private Connection conn;
	private Statement st;
	private ResultSet rs;
	private Room room;

	/**
	 * Connects to the given database
	 */
	public Connect() {

		try {//
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://10.2.13.227:3306/mapster", "Gustav1993", "password");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster", "Guest", "mapster");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster", "root", "");
			conn = DriverManager.getConnection("jdbc:mysql://84.219.169.69/mapster", "gustav", "1234");
			st = conn.createStatement();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	/**
	 * Prints all the data in the selected database
	 */
	public void printAllData() {

		try {
			String query = "select * from niagara";
			rs = st.executeQuery(query);
			System.out.println("Records from database:");
			while (rs.next()) {
				
				room = new Room(rs.getString(1), rs.getInt(2), 
						rs.getString(3), rs.getString(4));
				
				System.out.println("Floor: " + room.getFloor()+ " Name: " + room.getName() 
						+ " X/Y: " + room.getCoor() + " Path: " + room.getPath());
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Search for a room in the database with the given string, creates a room object
	 * from the values in the database and returns the room.
	 * @param searchFor
	 * @return room
     */
	public Room searchedRoom(String searchFor, String building) {

		String query = "select * FROM " + building + " WHERE name = '" + searchFor + "'";
		
		try {
			rs = st.executeQuery(query);
			rs.next();
			
			room = new Room(rs.getString(1), rs.getInt(2), 
					rs.getString(3), rs.getString(4));

//			System.out.println("#" + room.getId() + " Floor: " + room.getFloor()+ " Name: " + room.getName()
//				+ " X/Y: " + room.getCoor() + " Path: " + room.getPath());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return room;
	}

	public boolean searchExist(String searchFor, String building) {
		String query = "select * FROM " + building + " WHERE name = '" + searchFor + "'";
		try {
			rs = st.executeQuery(query);

			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}