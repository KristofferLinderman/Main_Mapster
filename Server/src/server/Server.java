package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


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

	public void run() {
		System.out.println("Server B started");

		// creates a new thread (ClientListener) for each accepted connection
		// made
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

	// A tread that listens for requests without closing the connection between
	// requests.
	private class ClientListener extends Thread {
		private Socket socket;
		private ObjectOutputStream outputStream;

		public ClientListener(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				
//				String request = fromAndroidGetRoomName
				String request = "A0312";

				// String strImage = "/home/gustav/Downloads/beluga.jpg";
				// String strImage =
				// "D:/Gustav/Dokument/Dropbox/Dropbox/Dropbox/Skola/ASO8c.jpg";
				String strImage = connect.searchedRoom(request).getPath();


				outputStream = new ObjectOutputStream(socket.getOutputStream());
				// inputStream = new ObjectInputStream(socket.getInputStream());

				fh.sendFile(outputStream, strImage);
				System.out.println("Picture sent to " + socket.getInetAddress() + "!" +  " [" + strImage + "]");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Server(9999);
	}
}