package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts a server that receives a room and returns an image (map)
 * Created by Gustav on 2016-04-05.
 */
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Thread serverThread;
	private Connect connect = new Connect();
	private FileFunctions fh = new FileFunctions();

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
				System.out.println("Client connected! [" + socket.getInetAddress().getHostName() + "]");
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

		public ClientListener(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Receives room-string from android client, search room via connect class and get info
		 * from database, returns the path om the file stored on the server.
		 */
		public void run() {
			try {
//				String request = fromAndroidGetRoomName
				String request = "A0312";

				// String strImage = "/home/gustav/Downloads/beluga.jpg";
				// "D:/Gustav/Dokument/Dropbox/Dropbox/Dropbox/Skola/ASO8c.jpg";
				String strImage = connect.searchedRoom(request).getPath();


				outputStream = new ObjectOutputStream(socket.getOutputStream());
				// inputStream = new ObjectInputStream(socket.getInputStream());

				fh.sendFile(outputStream, strImage);
				System.out.println("Picture sent to " + socket.getInetAddress() + "!" +  " [" + strImage + "]");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}