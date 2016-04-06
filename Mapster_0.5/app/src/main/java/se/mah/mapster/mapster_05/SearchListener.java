package se.mah.mapster.mapster_05;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Kristoffer on 02/04/16.
 */
public class SearchListener implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Context context;
    private Spinner buildingSpinner, levelSpinner, roomSpinner;
    private MainActivity activity;
    private String[] search = new String[3];
    private int[] dotPosition = new int[3];


    public SearchListener(Context context, Spinner building, Spinner level, Spinner room) {
        this.context = context;
        buildingSpinner = building;
        levelSpinner = level;
        roomSpinner = room;

        buildingSpinner.setOnItemSelectedListener(this);
        levelSpinner.setOnItemSelectedListener(this);
        roomSpinner.setOnItemSelectedListener(this);
    }

    public void setMainActivity(MainActivity activity) {
        this.activity = activity;
    }

    private void makeToast(String messageToDisplay) {
        Toast.makeText(context, messageToDisplay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.search_Button) {

                //Get X/Y
                dotPosition[0] = activity.getX();
                dotPosition[1] = activity.getY();
                dotPosition[2] = activity.getImageID();

            makeToast("Searching for " + search[0] + ":" + search[1] + search[2]);
            activity.search(search, dotPosition);
        }
    }

    /*
    parent is the spinner where the selection was made and the if/else identifies in which spinner the selection was made
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == buildingSpinner) {
            search[0] = parent.getItemAtPosition(position).toString();
        } else if (parent == levelSpinner) {
            search[1] = parent.getItemAtPosition(position).toString();
        } else if (parent == roomSpinner) {
            search[2] = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}