package se.mah.mapster.mapster_07;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Kristoffer on 06/04/16.
 */
public class PreviousSearchListener implements View.OnClickListener {
    private MainActivity mainActivity;
    private String[] search;
    private int[] dotPosition;
    private ArrayList<Search> previousSearchList;

    public PreviousSearchListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        previousSearchList = new ArrayList<Search>();

        for (int i = 0; i < 5; i++) {
            previousSearchList.add(new Search(search, dotPosition, ""));
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

//        switch (id) {
//            case R.id.previous_search_1:
//                mainActivity.search(previousSearchList[0]);
//                break;
//
//            case R.id.previous_search_2:
//                mainActivity.search(previousSearchList[1]);
//                break;
//
//            case R.id.previous_search_3:
//                mainActivity.search(previousSearchList[2]);
//                break;
//
//            case R.id.previous_search_4:
//                mainActivity.search(previousSearchList[3]);
//                break;
//
//            case R.id.previous_search_5:
//                mainActivity.search(previousSearchList[4]);
//                break;
//        }
    }

    public void addPreviousSearch(String[] search, int[] dotPosition) {
        String mapDir = "";

        mapDir = search[0] + ":" + search[2] + ".png";
        previousSearchList.add(0, new Search(search, dotPosition, mapDir));

        if (previousSearchList.size() > 5) {
            previousSearchList.remove(5);
        }
    }

    public ArrayList<Search> getList() {
        return previousSearchList;
    }
}
