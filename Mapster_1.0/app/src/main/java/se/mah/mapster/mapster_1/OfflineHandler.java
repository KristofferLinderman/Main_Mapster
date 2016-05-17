package se.mah.mapster.mapster_1;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
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
    private String ip = "10.2.17.104"; //Gustav MAH
    //    private String ip = "192.168.0.104"; //gustav hemma
    //    private String ip = "192.168.0.106"; //gustav XPS
    //    private String ip = "178.78.249.239";
    private int port = 9999;
    private String building;
    private File fileInDirBuildings;
    private File fileInDirHashMap;
    private File directory;

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
    private String fileString = "";

    // private Object[] maps;

>>>>>>> origin/master
    public OfflineHandler(String building) throws Exception {
        this.building = building;
        socket = new Socket(ip, port);
        activate();
    }

    public void activate() {
        try {
            requestBuilding();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestBuilding() throws Exception {
        try {
            oos = new DataOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeUTF(building);
            oos.flush();
            saveBuilding();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void whichBuilding() {

        switch (building) {

            case "#orkanen":
                fileString = "OR:0";
                break;

            case "#niagara":
                fileString = "NI:0";
                break;

            case "#gaddan":
                fileString = "G8:0";
                break;
        }

    }

    public void saveBuilding() {
        try {
            int nbrOfMaps = ois.readInt();
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            directory.mkdirs();

            Log.d("EVAL", "Building string: " + building);
            whichBuilding();
            Log.d("EVAL", "fileString: " + fileString);

            for (int i = 1; i <= nbrOfMaps; i++) {
                receiveFile(fileString + i + ".png");
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

        // read 4 bytes containing the file size
        byte[] bSize = new byte[4];
        int offset = 0;

        while (offset < bSize.length) {
            int bRead = ois.read(bSize, offset, bSize.length - offset); //här läses fil

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
        FileOutputStream fos = new FileOutputStream(fileInDirBuildings);
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

    }
}