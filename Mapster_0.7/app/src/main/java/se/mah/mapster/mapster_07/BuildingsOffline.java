package se.mah.mapster.mapster_07;

import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Environment;
import android.view.View;

/**
 * Created by Anton on 21/04/2016.
 */
public class BuildingsOffline extends Thread {
    ClientThread clientThread;
    private HashMap<String, String > buildings = new HashMap<String, String>();


    public BuildingsOffline(ClientThread clientThread) throws IOException{
        this.clientThread = clientThread;

    }

    public void requestBuilding(String building){
        try{
        clientThread.oos.writeChars(building);
        clientThread.oos.flush();
            start();

        } catch(IOException e ){
            e.printStackTrace();
        }}


    public void run(){
        buildings = ClientThread.ois.readObject();
    }







        //deletes the files in folder
       public  void deleteMaps(File fileOrDirectory) {
           File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
           if (dir.isDirectory())
           {
               String[] children = dir.list();
               for (int i = 0; i < children.length; i++)
               {
                   new File(dir, children[i]).delete();
               }
           }
    }




    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void readHashMap() {
        HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
        File file = //your file
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String l;
            while ((l = br.readLine()) != null) {
                String[] args = l.split("[,]", 2);
                if (args.length != 2) continue;
                String p = args[0].replaceAll(" ", "");
                String b = args[1].replaceAll(" ", "");
                if (b.equalsIgnoreCase("true")) hashmap.put(p, true);
                else hashmap.put(p, false);
            }
            br.close();
        }

    }
            public void writeHashMap

    {
        HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
        File file = //your file
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String p : hashmap.keySet()) {
                bw.write(p + "," + hashmap.get(p));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
    }



}









