package se.mah.mapster.mapster_07;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


/**
 * Created by Anton on 29/04/2016.
 */
public class OfflineHandler extends Thread {
    private HashMap<String, String> buildingHashMap = new HashMap<String, String>();
    private DataOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
//    private String ip = "10.2.13.227";  //"10.2.17.104"
    //    private String ip = "10.2.17.104"; //Gustav MAH
//        private String ip = "192.168.0.104"; //gustav hemma
        private String ip = "192.168.0.106"; //gustav XPS
    //    private String ip = "178.78.249.239";
    //    private String ip = "10.2.15.25"; //Kristoffer MAH
    //    private String ip = "192.168.0.2";//Kristoffer Hemma
    private int port = 9999;
    private String building;
    private File fileInDirBuildings;
    private File fileInDirHashMap;
    private File directory;

    // private Object[] maps;

    public OfflineHandler(String building) throws Exception {
        this.building = building;
        Log.d("TESTER", "before creating the socket");
        socket = new Socket(ip, port);
        Log.d("TESTER", "socket is now created");
        activate();
        Log.d("TESTER", " requestBuilding() started");

        //requestBuilding(); //for testing
    }


    public void activate() {
        try {
            requestBuilding();
            Log.d("TESTER", "Requested Buildings");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void requestBuilding() throws Exception {
        try {
            oos = new DataOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            Log.d("TESTER", "streams created ");
            oos.writeUTF(building);
            oos.flush();
            Log.d("TESTER", "Written to server");
            saveBuilding();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBuilding() {
        try {
            //   Object o = ois.read();
            Log.d("TESTER", "SaveBuilding started");
            //  buildingHashMap = (HashMap) o;
            int nbrOfMaps = ois.readInt();
            Log.d("TESTERMAJESTER", Integer.toString(nbrOfMaps));
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            directory.mkdirs();

            for (int i = 1; i <= nbrOfMaps; i++) {
                Log.d("LOOP", "NI:0" + i);
                receiveFile("NI:0" + i + ".png");
                System.out.println("after recieveFile");
            }
            saveHashMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveHashMap() throws ClassNotFoundException {
        try {
            fileInDirHashMap = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + "HashMap" + building);
            buildingHashMap = (HashMap) ois.readObject();
            FileOutputStream fos = new FileOutputStream(fileInDirHashMap);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(buildingHashMap);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String fileName) throws Exception {

        fileInDirBuildings = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + fileName);


        Log.d("Logg", "after fileInDir is created" + fileInDirBuildings);


        // read 4 bytes containing the file size
        byte[] bSize = new byte[4];
        int offset = 0;

        while (offset < bSize.length) {
            int bRead = ois.read(bSize, offset, bSize.length - offset); //här läses fil
            Log.d("Logg", "reading from OIS" + bRead);

            offset += bRead;
        }

        // Convert the 4 bytes to an int
        int fileSize;
        fileSize = (int) (bSize[0] & 0xff) << 24
                | (int) (bSize[1] & 0xff) << 16
                | (int) (bSize[2] & 0xff) << 8
                | (int) (bSize[3] & 0xff);
        Log.d("Logg", "fileSize");
        // buffer to read from the socket
        // 8k buffer is good enough
        byte[] data = new byte[8 * 1024];
        Log.d("Logg", " after fileSize");
        int bToRead;
        FileOutputStream fos = new FileOutputStream(fileInDirBuildings);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        Log.d("Logg", " after FileOutputStream");

        while (fileSize > 0) {
            Log.d("Logg", " while (fileSize > 0)" + fileSize);
            // make sure not to read more bytes than filesize
            if (fileSize > data.length) {
                bToRead = data.length;
            } else {
                bToRead = fileSize;
            }
            Log.d("Logg", "before writing to file");
            int bytesRead = ois.read(data, 0, bToRead);

            if (bytesRead > 0) {
                bos.write(data, 0, bytesRead);
                fileSize -= bytesRead;
                Log.d("Logg", "after writing to file " + bytesRead);
            }
        }
        bos.close();

    }
}