package se.mah.mapster.mapster_06;

import android.view.View;

/**
 * Created by Kristoffer on 06/04/16.
 */
public class PreviousSearchListener implements View.OnClickListener {
    private MainActivity mainActivity;

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.previous_search_1:
                break;

            case R.id.previous_search_2:
                break;

            case R.id.previous_search_3:
                break;

            case R.id.previous_search_4:
                break;

            case R.id.previous_search_5:
                break;
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void addPreviousSearch(){

    }

}
