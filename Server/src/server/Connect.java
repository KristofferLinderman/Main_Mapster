package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for connecting to a database and get information Created by Gustav on
 * 2016-04-06.
 */
public class Connect {

	private Connection conn;
	private Statement st;
	private ResultSet rs;
	private Room room;
	private HashMap<String, String> combo = new HashMap<String, String>();
	private ArrayList<String> distinctFloors = new ArrayList<String>();

	/**
	 * Connects to the given database
	 */
	public Connect() {

		try {//
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://10.2.13.227:3306/mapster", "Gustav1993",
					"password");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster",
			// "Guest", "mapster");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster",
			// "root", "");
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

				room = new Room(rs.getString(1), rs.getInt(2), rs.getString(3),
						rs.getString(4));

				System.out.println("Floor: " + room.getFloor() + " Name: "
						+ room.getName() + " X/Y: " + room.getCoor()
						+ " Path: " + room.getPath());
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public HashMap<String, String> getHashMap() {
		return combo;

	}

	public ArrayList<String> getDistinctFloors() {
		return distinctFloors;
	}

	public void whichBuilding(String building) throws SQLException {

		switch (building) {

		case "orkanen":
			getBuilding("orkanen");
			break;

		case "niagara":
			getBuilding("niagara");
			break;

		case "gaddan":
			getBuilding("gaddan");
			break;
		}

	}

	// Retrieves name and coordinates for all rooms of a building
	public void getBuilding(String building) throws SQLException {
		String name, coordinates;

		String query = "SELECT name, coordinates FROM " + building;
		rs = st.executeQuery(query);
		combo.clear();
		while (rs.next()) {
			name = rs.getString("name");
			coordinates = rs.getString("coordinates");
			combo.put(name, coordinates);

//			File file = new File(building);
//			try {
//				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//				for (String p : combo.keySet()) {
//					bw.write(p + "," + combo.get(p));
//					bw.newLine();
//				}
//				bw.flush();
//				bw.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		}
		requestFloors(building);
	}

	public void requestFloors(String building) {
		String query = null;

		switch (building) {

		case "orkanen":
			query = "SELECT floors FROM downloadable LIMIT 0, 5;";
			break;

		case "niagara":
			query = "SELECT floors FROM downloadable LIMIT 5, 6;";
			break;
		case "gaddan":
			query = "SELECT floors FROM downloadable LIMIT 11, 4;";
			break;

		}

		try {
			rs = st.executeQuery(query);
			distinctFloors.clear();
			while (rs.next()) {
				distinctFloors.add(rs.getString("path"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Search for a room in the database with the given string, creates a room
	 * object from the values in the database and returns the room.
	 * 
	 * @param searchFor
	 * @return room
	 */
	public Room searchedRoom(String searchFor, String building) {

		String query = "select * FROM " + building + " WHERE name = '"
				+ searchFor + "'";

		try {
			rs = st.executeQuery(query);
			rs.next();

			room = new Room(rs.getString(1), rs.getInt(2), rs.getString(3),
					rs.getString(4));

			// System.out.println("#" + room.getId() + " Floor: " +
			// room.getFloor()+ " Name: " + room.getName()
			// + " X/Y: " + room.getCoor() + " Path: " + room.getPath());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return room;
	}
}