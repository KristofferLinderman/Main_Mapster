package se.mah.mapster.mapster_06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Bitmap map;
    private String ip;
    private int port;
    private SearchListener searchListener;

    public ClientThread(String ip, int port, SearchListener searchListener) {
        this.ip = ip;
        this.port = port;
        this.searchListener = searchListener;
    }

    public void run() {
        try {
            Log.d("EVAL", "1");
            socket = new Socket(ip, port);
            Log.d("EVAL", "Connected");

            ois = new ObjectInputStream(socket.getInputStream());

            Log.d("EVAL", "3");
            map = receiveFile(ois, "map.png");
            searchListener.setBitmap(map);


            Log.d("EVAL", map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap receiveFile(InputStream is, String fileName) throws Exception {

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileInES = baseDir + File.separator + fileName;

        // read 4 bytes containing the file size
        byte[] bSize = new byte[4];
        int offset = 0;

        while (offset < bSize.length) {
            int bRead = is.read(bSize, offset, bSize.length - offset);
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
        FileOutputStream fos = new FileOutputStream(fileInES);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        while (fileSize > 0) {
            // make sure not to read more bytes than filesize
            if (fileSize > data.length) bToRead = data.length;
            else bToRead = fileSize;
            int bytesRead = is.read(data, 0, bToRead);
            if (bytesRead > 0) {
                bos.write(data, 0, bytesRead);
                fileSize -= bytesRead;
            }
        }
        bos.close();

        // Convert the received image to a Bitmap
        // If you do not want to return a bitmap comment/delete the folowing lines
        // and make the function to return void or whatever you prefer.
        Bitmap bmp = null;
        FileInputStream fis = new FileInputStream(fileInES);
        try {
            bmp = BitmapFactory.decodeStream(fis);
            return bmp;
        } finally {
            fis.close();
        }
    }
}