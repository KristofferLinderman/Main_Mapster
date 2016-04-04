package se.mah.mapster.mapster_02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
    private Activity activity;
    private String building,level,room;

    public SearchListener(Context context, Spinner building, Spinner level, Spinner room) {
        this.context = context;
        buildingSpinner = building;
        levelSpinner = level;
        roomSpinner = room;

        buildingSpinner.setOnItemSelectedListener(this);
        levelSpinner.setOnItemSelectedListener(this);
        roomSpinner.setOnItemSelectedListener(this);
    }

    public void setMainActivity(Activity activity) {
        this.activity = activity;
    }

    private void openView() {
        activity.startActivity(new Intent(context, MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
