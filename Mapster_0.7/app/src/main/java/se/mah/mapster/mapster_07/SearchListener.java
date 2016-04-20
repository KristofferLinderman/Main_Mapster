package se.mah.mapster.mapster_07;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Created by Kristoffer on 02/04/16.
 */
public class SearchListener implements View.OnClickListener {
    private Context context;
    private NumberPicker buildingPicker, sectionPicker, levelPicker, roomPicker;
    private MainActivity activity;
    private String[] search = new String[4];
    private int[] dotPosition = new int[3];
    private ClientThread clientThread;
    private Bitmap map;


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

    private void makeToast(String messageToDisplay) {
        Toast.makeText(context, messageToDisplay, Toast.LENGTH_SHORT).show();
    }

    public void setBitmap(Bitmap map) {
        this.map = map;
    }

    public String getMapName() {
        return search[0] + search[2] + ".png";
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.search_Button) {

            //Get X/Y
            dotPosition[0] = activity.getX();
            dotPosition[1] = activity.getY();
            dotPosition[2] = activity.getImageID();

            getSearch();
            makeToast("Searching for " + search[0] + ":" + search[1] + search[2] + search[3]);

            clientThread = new ClientThread("10.2.15.25", 9999, this);
            clientThread.start();

            try {
                Log.d("EVAL", "Sleep brah");
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activity.search(search, dotPosition, map);
        }
    }

    // Gets the values from the pickers on MainActivity and adds them to a string[]
    private void getSearch() {
        String[] temp;
        temp = buildingPicker.getDisplayedValues();
        search[0] = temp[buildingPicker.getValue()];

        temp = sectionPicker.getDisplayedValues();
        search[1] = temp[sectionPicker.getValue()];

        search[2] = "" + levelPicker.getValue();

        int room = roomPicker.getValue();

        //Format for rooms are 0X for numbers under 10
        if (room > 9) {
            search[3] = "" + roomPicker.getValue();
        } else {
            search[3] = "0" + roomPicker.getValue();
        }
    }
}