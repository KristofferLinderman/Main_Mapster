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
    private String building,level,room;
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

    private void openView() {
        Intent i = new Intent(context,MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Positions", dotPosition);
        activity.startActivity(i);
//        activity.startActivity(new Intent(context, MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void getSearch() {
    }

    private void makeToast(String messageToDisplay) {
        Toast.makeText(context, messageToDisplay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.search_Button) {
            try {

                //Get X/Y
                dotPosition[0] = activity.getX();
                dotPosition[1] = activity.getY();
                dotPosition[2] = activity.getImageID();

            } catch(NumberFormatException numberFormatException) {
                makeToast("Input X/Y value");
            }
            getSearch();
            makeToast("Searching for " + building + ":" + level + room);
            openView();
        }
    }

    /*
    parent is the spinner where the selection was made and the if/else identifies in which spinner the selection was made
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == buildingSpinner) {
            building = parent.getItemAtPosition(position).toString();
        } else if (parent == levelSpinner) {
            level = parent.getItemAtPosition(position).toString();
        } else if (parent == roomSpinner) {
            room = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}