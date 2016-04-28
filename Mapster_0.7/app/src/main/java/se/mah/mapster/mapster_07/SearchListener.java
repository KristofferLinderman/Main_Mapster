package se.mah.mapster.mapster_07;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;

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

    //    private String ip = "10.2.17.104"; //mah wifi
    //    private String ip = "192.168.0.104"; //gustav xps
    //    private String ip = "178.78.249.239";
    //    private String ip = "10.2.15.25"; //Kristoffer MAH
    private String ip = "192.168.0.2";//Kristoffer Hemma

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


    public void setBitmap(Bitmap map) {
        this.map = map;
    }

    public String getMapName() {
        return search[0] + search[2] + ".png";
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
            getSearchValues();

            activity.makeToast("Searching for " + getSearch());

            if (!mapOffline()) {
                Log.d("EVAL", "Map not offline, need to download");
                clientThread = new ClientThread(ip, 9999, this);
                clientThread.start();
            } else {
                Log.d("EVAL", "No download needed");
                activity.search(search, dotPosition, getFilename());
                return;
            }

//            clientThread = new ClientThread(ip, 9999, this);
//            clientThread.start();

            try {
                Thread.sleep(2000);
                Log.d("EVAL", "Sleep brah");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activity.search(search, dotPosition, getFilename());
        }
    }

    public String getSearch() {
        return search[0] + ":" + search[1] + search[2] + search[3];
    }

    /**
     * Check if the map the user searched for is already downloaded and stored localy
     *
     * @return True if the map is localy stored else false.
     */
    private boolean mapOffline() {
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + File.separator + "Mapster" + getFilename());

        if (myFile.exists()) {
            return false;
        } else {
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