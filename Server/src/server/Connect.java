package server;

import sun.applet.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {

	private Connection conn;
	private Statement st;
	private ResultSet rs;
	private Room room;

	public Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.2.13.227:3306/mapster", "Gustav1993", "password");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster", "Guest", "mapster");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapster", "root", "");
			st = conn.createStatement();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public void printAllData() {
		try {
			String query = "select * from niagara";
			
//			String query1 = "SELECT map FROM Orkanen WHERE ID = D313";
//			String query2 = "SELECT koordinates FROM Orkanen WHERE ID = D313";
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
	
	public Room searchedRoom(String searchFor) {
		String query = "select * FROM niagara WHERE name = '" + searchFor + "'";
		
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
}