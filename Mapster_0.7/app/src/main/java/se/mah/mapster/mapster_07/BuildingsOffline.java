package se.mah.mapster.mapster_07;

import android.content.Context;
import android.graphics.BitmapFactory;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Anton on 21/04/2016.
 */
public class BuildingsOffline {
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket socket;
    String ip = "localhost";
    int port = 3306;
    ClientThread cThread;
    HashMap<String, String> temporary = new HashMap<String, String>();
    HashMap<String, String> orkanen = new HashMap<String, String>();
    HashMap<String, String> niagara = new HashMap<String, String>();
    File directory;
    File fileInDir;
    int counter;
    String building;

    public BuildingsOffline() throws IOException {
        socket = new Socket(ip, port);
        if (directory.exists()) {
          //  readHashmap();
        }


    }

    public void requestBuilding(String building) {


        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeUTF(building);
            oos.flush();

            Object o = ois.readObject();
            temporary = (HashMap<String, String>) o;
            whichBuilding(temporary);

            while (o != -1) {

                o = ois.readObject();
                counter++;

               String floor =  Integer.toString(counter);

                receiveFile(directory.toString(), building.concat(floor));

            }

        } catch (IOException e) {
            e.toString();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void whichBuilding(HashMap values) {

        if (values.containsValue("OR:131")) {
            orkanen = values;
            values.clear();
            saveHashmap(orkanen, "Orkanen");

        } else if (values.containsValue("NIA0305")) {
            niagara = values;
            values.clear();
            saveHashmap(niagara, "Niagara");

        }


    }

    public void saveHashmap(HashMap<String, String> buildingHash, String building) {
        try {

          directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + building);
          fileInDir = new File(directory + File.separator + building);

            FileOutputStream fos = new FileOutputStream(fileInDir);
            ObjectOutputStream oos = new ObjectOutputStream((fos));
            oos.writeObject(buildingHash);
            oos.flush();




        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Bitmap receiveFile(String directory, String fileName) throws Exception {

        File fileInDir = new File(directory + File.separator + fileName);

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

    }


    public void offlineImage(String request){
        //get




    }

    public void readHashmap(String building){

        try{
            FileInputStream fin = new FileInputStream( directory + building + ".ser" );
            ObjectInputStream ois = new ObjectInputStream(fin);
            if(building == "orkanen"){
                orkanen  = (HashMap) ois.readObject();
            }
            else niagara = (HashMap) ois.readObject();
            ois.close();
            offlineImage();

        }catch(Exception ex){
            ex.printStackTrace();

        }
    }












































    File folder = new File(Environment.getExternalStorageDirectory() + "/TollCulator");
    boolean success = true;
    if (!folder.exists()) {
        //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
        success = folder.mkdir();
    }
    if (success) {
        Toast.makeText(MainActivity.this, "Directory Created", Toast.LENGTH_SHORT).show();
    } else {
        //Toast.makeText(MainActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
    }






//deletes the files in folder
public void deleteMaps(File fileOrDirectory){
        File dir=new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
        if(dir.isDirectory())
        {
        String[]children=dir.list();
        for(int i=0;i<children.length;i++)
        {
        new File(dir,children[i]).delete();
        }
        }
        }


public Bitmap load(){
        FileInputStream inputStream=null;
        try{
        inputStream=new FileInputStream(createFile());
        return BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
        e.printStackTrace();
        }finally{
        try{
        if(inputStream!=null){
        inputStream.close();
        }
        }catch(IOException e){
        e.printStackTrace();
        }
        }
        return null;
        }


public void save(Bitmap bitmapImage){
        FileOutputStream fileOutputStream=null;
        try{
        fileOutputStream=new FileOutputStream(createFile());
        bitmapImage.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        }catch(Exception e){
        e.printStackTrace();
        }finally{
        try{
        if(fileOutputStream!=null){
        fileOutputStream.close();
        }
        }catch(IOException e){
        e.printStackTrace();
        }
        }
        }


public void readHashMap(){

        File file= //your file
        try{
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String l;
        while((l=br.readLine())!=null){
        String[]args=l.split("[,]",2);
        if(args.length!=2)continue;
        String p=args[0].replaceAll(" ","");
        String b=args[1].replaceAll(" ","");
        if(b.equalsIgnoreCase("true"))hashmap.put(p,true);
        else hashmap.put(p,false);
        }
        br.close();
        }

        }
public void writeHashMap

        {
        HashMap<String, Boolean>hashmap=new HashMap<String, Boolean>();
        File file= //your file
        try{
        BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        for(String p:hashmap.keySet()){
        bw.write(p+","+hashmap.get(p));
        bw.newLine();
        }
        bw.flush();
        bw.close();
        }
        }


        }









