package se.mah.mapster.mapster_07;

import android.util.Log;
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
    private Search[] prevSearches;
    private int counter = 0;

    public PreviousSearchListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        prevSearches = new Search[5];

        previousSearchList = new ArrayList<Search>();
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

    public void addPreviousSearch(String[] searchInput, int[] dotPosition) {
        String mapDir = searchInput[0] + ":" + searchInput[2] + ".png";
        Search temp = new Search(searchInput, dotPosition, mapDir);
        Log.d("PREV", mapDir + " " + searchInput[2]);

        previousSearchList.add(0, temp);

        for (Search s : previousSearchList) {
            Log.d("PREV", s.getSearchQuery());
        }

        if (previousSearchList.size() > 5) {
            Log.d("EVAL", previousSearchList.remove(5).toString());
        }
    }

    public ArrayList<Search> getList() {
        return previousSearchList;
    }
}
