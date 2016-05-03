package se.mah.mapster.mapster_07;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.BufferedOutputStream;
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
public class BuildingsOfflineGeneric extends Thread {
    HashMap<String, String> buildingHashMap = new HashMap<String, String>();
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket socket;
    String ip = "localhost";
    int port = 3306;
    String building;
    Object[] maps;

    public BuildingsOfflineGeneric(String building) throws Exception{
        this.building = building;
        socket = new Socket(ip, port);
        start();

    }

        public void run(){
            try {
                requestBuilding();
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveBuilding();
         //   saveHashMap();


        }

        public void requestBuilding() throws Exception {
            try{
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeUTF(building);
                oos.flush();

            }catch(IOException e ){
                e.printStackTrace();
            }
        }

    public void saveBuilding(){
        try{
            Object o = ois.read();
          //  buildingHashMap = (HashMap) o;
            int nbrOfMaps = ois.readInt();
            //must be changed for test

            //(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            String directory = "C:/Users/Anton/Desktop/TestResult";

             for(int i = 1; i<nbrOfMaps; i++){
                 receiveFile(directory, building + i);
        }

    }catch(Exception e){
        e.printStackTrace();}
    }


    public void saveHashMap(){
        try {
            File  directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + building);
            File fileInDir = new File(directory + File.separator + building);

            FileOutputStream fos = new FileOutputStream(fileInDir);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(buildingHashMap);
            oos.flush();

        }catch(IOException e ){
            e.printStackTrace();
        }

    }


    private void receiveFile(String directory, String fileName) throws Exception {

        File fileInDir = new File(directory + File.separator + fileName);

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

    }




    public static void main(String[] args) throws Exception {
        BuildingsOfflineGeneric test = new BuildingsOfflineGeneric("#orkanen");

    }

}


