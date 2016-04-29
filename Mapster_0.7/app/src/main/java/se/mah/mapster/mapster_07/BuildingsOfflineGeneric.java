package se.mah.mapster.mapster_07;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Anton on 29/04/2016.
 */
public class BuildingsOfflineGeneric {
    HashMap<String, String> buildingHashMap = new HashMap<String, String>();
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket socket;
    String ip;
    int port;
    String building;

    public BuildingsOfflineGeneric(String building) throws IOException{
        this.building = building;
        socket = new Socket(ip, port);

    }

        public void activate(){
            requestBuilding();
            saveHashMap();
            floorSaver(); //itererar över recieveFile för sparning av bilder

        }

        public void requestBuilding(){
            try{
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeUTF(building);
                oos.flush();

                Object o = ois.read();
                buildingHashMap = (HashMap) o;


                try{
                    int counter = 0;
                while(o != null){
                    o = ois.readObject();
                    counter++;
                    
                }}catch(ClassNotFoundException e){
                    e.printStackTrace();
                }

            }catch(IOException e ){
                e.printStackTrace();
            }
        }




}


