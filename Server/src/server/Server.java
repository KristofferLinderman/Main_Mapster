package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Starts a server that receives a room and returns an image (map) Created by
 * Gustav on 2016-04-05.
 */
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Thread serverThread;
	private Connect connect = new Connect();
	private FileFunctions fh = new FileFunctions();
	private ArrayList<String> recievedBuildings = new ArrayList<String>();

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			serverThread = new Thread(this);
			serverThread.start();
		} catch (IOException e) {
		}
	}

	/**
	 * Creates a new thread (ClientListener) for each accepted connection made
	 */
	public void run() {
		System.out.println("Server started");

		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected! ["
						+ socket.getInetAddress().getHostName() + "]");
				new ClientListener(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A tread that listens for requests without closing the connection between
	 * requests.
	 */
	private class ClientListener extends Thread {
		private Socket socket;
		private ObjectOutputStream outputStream;
		private DataInputStream inputStream;

		public ClientListener(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Receives room-string from android client, search room via connect
		 * class and gets info from database, returns the path of the file
		 * stored on the server.
		 */
		public void run() {
			try {
				inputStream = new DataInputStream(socket.getInputStream());
				String request = inputStream.readUTF();

				if (!request.contains(":")) {
					connect.whichBuilding(request);
					recievedBuildings = connect.getDistinctFloors();

					for (int i = 0; i < recievedBuildings.size(); i++) {
						fh.sendFile(outputStream, recievedBuildings.get(i));
					}

					outputStream.writeObject(connect.getHashMap());
					outputStream.flush();

				}

				fh.splitRoom(request);
				System.out.println("Input from client: " + fh.getRoomString()
						+ " " + fh.getBuildingString());

				Room room = connect.searchedRoom(fh.getRoomString(),
						fh.getBuildingString());
				String strImage = room.getPath();

				// Send file to clientx
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				fh.sendFile(outputStream, strImage);
				System.out.println("Picture sent to " + socket.getInetAddress()
						+ "!" + " [" + strImage + "]");

				// Get coordinates
				fh.splitCoor(room.getCoor());
				System.out.println("X: " + fh.getX() + " Y: " + fh.getY());
				outputStream.writeInt(fh.getX());
				outputStream.writeInt(fh.getY());
				outputStream.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}