package server;

/**
 * Room class that stores information about a room
 * Created by Gustav on 2016-04-08.
 */
public class Room {

	private int floor;
	private String name, coor, path;

	/**
	 * Sets values for the room object
	 * @param name
	 * @param floor
	 * @param coor
     * @param path
     */
	public Room(String name, int floor, String coor, String path) {
		this.floor = floor;
		this.name = name;
		this.coor = coor;
		this.path = path;
	}

	/**
	 * Returns the name of the room
	 * @return name
     */
	public String getName() {
		return name;
	}

	/**
	 * Returns the floor of the room
	 * @return floor
     */
	public int getFloor() {
		return floor;
	}

	/**
	 * Returns the coordinates of the room in the format of XXXX.YYYY
	 * @return coordinates
     */
	public String getCoor() {
		return coor;
	}

	/**
	 * Returns the filepath of the room located on the server
	 * @return path
     */
	public String getPath() {
		return path;
	}
}