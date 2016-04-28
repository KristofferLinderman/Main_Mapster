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
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Environment;
import android.view.View;

/**
 * Created by Anton on 21/04/2016.
 */
public class BuildingsOffline{
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket socket;
    String ip = "";
    int port;
    ClientThread cThread;
    HashMap<String, String> values = new HashMap<String, String>();
    public BuildingsOffline() throws IOException{
        socket = new Socket(ip, port);

    }

 public void requestBuilding(String building){

     oos = new ObjectOutputStream(socket.getOutputStream());
     ois = new ObjectInputStream(socket.getInputStream());

     try{
         oos.writeUTF(building);
         oos.flush();

           Object o = ois.readObject();
           values = (HashMap<String, String>) o;
           whichBuilding(values);

            while(o != -1){

                o = ois.readObject();
                import java.io.File;
                File folder = new File(Environment.getExternalStorageDirectory() + "/TollCulator");
                boolean success = true;
                if (!folder.exists()) {
                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                    success = folder.mkdir();
                }
                if (success) {
                    //Toast.makeText(MainActivity.this, "Directory Created", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
                }

            }





     }catch(IOException e){
         e.toString();
     }

    }

    public void whichBuilding(HashMap values){

        if(values.containsValue("OR:131")){
            saveHashmap(values);

        }

        else if (values.containsValue("NIA0305")) {
            //do something
        }


    }



    public void saveHashmap(HashMap<String, String> values){}

















    try
    {
        FileOutputStream fos = context.openFileOutput("YourInfomration.ser", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(myHashMap);
        oos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
} catch (IOException e) {
        e.printStackTrace();
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









