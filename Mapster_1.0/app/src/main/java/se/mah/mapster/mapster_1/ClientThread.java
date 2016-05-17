package se.mah.mapster.mapster_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private DataOutputStream dos;
    private Bitmap map, btm;
    private String ip;
    private int port;
    private SearchListener searchListener;
    private String search;
    private int x, y;
    private int choice;

    private File directory;
    private File fileInDir;

    public ClientThread(String ip, int port, SearchListener searchListener) {
        this.ip = ip;
        this.port = port;
        this.searchListener = searchListener;
    }

    /**
     * Select which type of searh to be made.
     *
     * @param choice 0 for search Map and Coordinates, 1 for search of only coordinate
     */
    public void setAction(int choice) {
        this.choice = choice;
    }

    public void run() {
        try {
            search = searchListener.getSearch();
            Log.d("EVAL", "Got search string " + search);
            socket = new Socket(ip, port);
            Log.d("EVAL", "Connected to server " + socket.getInetAddress());

            dos = new DataOutputStream(socket.getOutputStream());
            Log.d("EVAL", "Got OutputStream");

            ois = new ObjectInputStream(socket.getInputStream());
            Log.d("EVAL", "Got InputStream");

            switch (choice) {
                case 0:
                    searchMapAndCoordinates();
                    break;
                case 1:
                    searchCoordinates();
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int[] searchCoordinates() {
        int[] result = new int[2];
        try {
            dos.writeUTF("&" + search);
            Log.d("EVAL", "Search : " + search);
            Log.d("EVAL", "Wrote search to server");
            dos.flush();

//            ois = new ObjectInputStream(socket.getInputStream());
//            Log.d("EVAL", "Got InputStream");

            if (ois.readBoolean()) {

                Log.d("EVAL", "Room exists");

                result[0] = ois.readInt();
                result[1] = ois.readInt();

                searchListener.setX(result[0]);
                searchListener.setY(result[1]);

                Log.d("EVAL", "Coordinates; X: " + result[0] + ", Y: " + result[1]);

                searchListener.search();
            } else {
                Log.d("EVAL", "Room doesn't exists!");
                searchListener.makeToast("Room doesn't exist");
            }

            dos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void searchMapAndCoordinates() {
        try {
            dos.writeUTF(search);
            Log.d("EVAL", "Search : " + search);
            Log.d("EVAL", "Wrote search to server");
            dos.flush();

//            ois = new ObjectInputStream(socket.getInputStream());
//            Log.d("EVAL", "Got InputStream");

            if (ois.readBoolean()) {

                Log.d("EVAL", "Loading file...");

                String filename = searchListener.getFilename();
                Log.d("EVAL", "Room exists");
                btm = receiveFile(ois, filename);
                Log.d("EVAL", "Image received!");

                x = ois.readInt();
                y = ois.readInt();

                searchListener.setX(x);
                searchListener.setY(y);
                Log.d("EVAL", "Coordinates; X: " + x + ", Y: " + y);
                searchListener.search();
            } else {
                Log.d("EVAL", "Room doesn't exists!");

                searchListener.makeToast("Room doesn't exist");
            }

            dos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap receiveFile(ObjectInputStream ois, String fileName) throws Exception {
//        directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
        fileInDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + fileName);

        // read 4 bytes containing the file size
        byte[] bSize = new byte[4];
        int offset = 0;

        while (offset < bSize.length) {
            int bRead = ois.read(bSize, offset, bSize.length - offset);
            offset += bRead;
        }

        // Convert the 4 bytes to an int
        int fileSize;
        fileSize = (int) (bSize[0] & 0xff) << 24
                | (int) (bSize[1] & 0xff) << 16
                | (int) (bSize[2] & 0xff) << 8
                | (int) (bSize[3] & 0xff);

        // buffer to read from the socket
        // 8k buffer is good enough
        byte[] data = new byte[8 * 1024];

        int bToRead;
        FileOutputStream fos = new FileOutputStream(fileInDir);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        while (fileSize > 0) {
            // make sure not to read more bytes than filesize
            if (fileSize > data.length) {
                bToRead = data.length;
            } else {
                bToRead = fileSize;
            }

            int bytesRead = ois.read(data, 0, bToRead);
            if (bytesRead > 0) {
                bos.write(data, 0, bytesRead);
                fileSize -= bytesRead;
            }
        }

        bos.close();

        // Convert the received image to a Bitmap
        Bitmap bmp = null;
        FileInputStream fis = new FileInputStream(fileInDir);
        try {
            bmp = BitmapFactory.decodeStream(fis);
            return bmp;
        } finally {
            fis.close();
        }
    }

}