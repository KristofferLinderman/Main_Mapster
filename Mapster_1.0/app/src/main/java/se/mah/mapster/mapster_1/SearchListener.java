package se.mah.mapster.mapster_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Kristoffer on 02/04/16.
 */
public class SearchListener implements View.OnClickListener {
    private Context context;
    private NumberPicker buildingPicker, sectionPicker, levelPicker, roomPicker;
    private MainActivity activity;
    private String[] search = new String[4];
    private int[] dotPosition = new int[2];
    private ClientThread clientThread;
    private Bitmap map;

    private String ip = "84.219.169.69"; //MAIN
    private int port = 3450; //MAIN
    //    private String ip = "10.2.17.104"; //Gustav MAH
    //    private String ip = "192.168.0.104"; //gustav hemma
    //    private String ip = "192.168.0.106"; //gustav XPS
    //    private String ip = "178.78.249.239";
    //    private String ip = "10.2.15.25"; //Kristoffer MAH
    //    private String ip = "192.168.0.2";//Kristoffer Hemma

    public SearchListener(Context context, NumberPicker buildingPicker, NumberPicker sectionPicker, NumberPicker levelPicker, NumberPicker roomPicker) {
        this.context = context;
        this.buildingPicker = buildingPicker;
        this.sectionPicker = sectionPicker;
        this.levelPicker = levelPicker;
        this.roomPicker = roomPicker;
    }

    public void setMainActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void makeToast(String message) {
        activity.makeToast(message);
    }

    public void setX(int xPos) {
        dotPosition[0] = xPos;
    }

    public void setY(int yPos) {
        dotPosition[1] = yPos;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.search_Button) {
            search = new String[4];
            dotPosition = new int[2];
            getSearchValues();

            activity.makeToast("Searching for " + getSearch());

            clientThread = new ClientThread(ip, port, this);

            if (!mapOffline()) {
                clientThread.setAction(0);
                clientThread.start();
            } else {
                Log.d("EVAL", "No download needed");

                if (hashMapAvailable()) {
                    getCoordinatesFromHashMap();
                } else {
                    clientThread.setAction(1);
                    clientThread.start();
                }
            }
        }
    }

    public void search() {
        Log.d("EVAL", "" + dotPosition[0] + " " + dotPosition[1]);
        activity.search(search, dotPosition, getFilename());
    }

    private void getCoordinatesFromHashMap() {
        HashMap buildingHashMap = getHashMap();

        if (buildingHashMap != null) {
            String input = (String) buildingHashMap.get(getRoom());
            if (input != null) {
                String[] coordinates = input.split("\\.");

                setX(Integer.parseInt(coordinates[0]));
                setY(Integer.parseInt(coordinates[1]));

                search();
            }else {
                makeToast("Room doesn't exist");
            }
        }
    }

    private HashMap getHashMap() {
        File hashFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + "HashMap" + getbuilding());
        FileInputStream fileInputStreams = null;
        HashMap buildingHashMap;
        try {
            fileInputStreams = new FileInputStream(hashFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStreams);
            buildingHashMap = (HashMap) objectInputStream.readObject();
            Log.d("EVAL", "HashMap Found");
            return buildingHashMap;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSearch() {
        return search[0] + ":" + search[1] + search[2] + search[3];
    }

    private String getbuilding() {
        String temp = "";
        switch (search[0]) {
            case "NI":
                temp = "#niagara";
                break;
            case "OR":
                temp = "#orkanen";
                break;
        }
        return temp;
    }

    private String getRoom() {
        return search[1] + search[2] + search[3];
    }

    /**
     * Check if the map the user searched for is already downloaded and stored locally
     * @return True if the map is locally stored else false.
     */
    private boolean mapOffline() {
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + File.separator + "Mapster" + File.separator + getFilename());

        if (myFile.exists()) {
            Log.d("EVAL", "MapFile does exist");
            return true;
        } else {
            Log.d("EVAL", "MapFile does NOT exist");
            return false;
        }
    }

    private boolean hashMapAvailable() {
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + File.separator + "Mapster" + File.separator + "HashMap" + getbuilding());

        if (myFile.exists()) {
            Log.d("EVAL", "HashMap-file does exist");
            return true;
        } else {
            Log.d("EVAL", "HashMap-file does NOT exist");
            return false;
        }
    }

    public String getFilename() {
        return search[0] + ":" + search[2] + ".png";
    }

    // Gets the values from the pickers on MainActivity and adds them to a string[]
    private void getSearchValues() {
        String[] temp;
        temp = buildingPicker.getDisplayedValues();
        search[0] = temp[buildingPicker.getValue()];

        temp = sectionPicker.getDisplayedValues();
        //Om gäddan ta bort ABCD
        if (search[0].equals("G8")) {
            search[1] = "";
        } else {
            search[1] = temp[sectionPicker.getValue()];
        }

        //Lägg till 0 om niagara
        if (search[0].equals("NI")) {
            search[2] = "0" + levelPicker.getValue();
        } else {
            search[2] = "" + levelPicker.getValue();
        }

        int room = roomPicker.getValue();

        //Format for rooms are 0X for numbers under 10
        if (room > 9) {
            search[3] = "" + roomPicker.getValue();
        } else {
            search[3] = "0" + roomPicker.getValue();
        }
    }
}