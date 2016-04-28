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
    private int counter = 0;

    public PreviousSearchListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        previousSearchList = new ArrayList<Search>();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.previous_search_1:
                mainActivity.search(previousSearchList.get(0));
                break;

            case R.id.previous_search_2:
                mainActivity.search(previousSearchList.get(1));
                break;

            case R.id.previous_search_3:
                mainActivity.search(previousSearchList.get(2));
                break;

            case R.id.previous_search_4:
                mainActivity.search(previousSearchList.get(3));
                break;

            case R.id.previous_search_5:
                mainActivity.search(previousSearchList.get(4));
                break;
        }
    }

    public void addPreviousSearch(String[] searchInput, int[] dotPosition) {
        String fileName = searchInput[0] + ":" + searchInput[2] + ".png";
        Log.d("PREV", fileName + " " + searchInput[2]);
        Search toAdd = new Search(searchInput, dotPosition, fileName);
        boolean add = true;

        //Check if the previous search ahs been made before in which case just move it to the top.
        //by deleting it and then adding it to index 0.
        for (int i = 0; i < previousSearchList.size(); i++) {
            if (previousSearchList.get(i).getSearchQuery().equals(toAdd.getSearchQuery())) {
                add = false;
                previousSearchList.remove(i);
                previousSearchList.add(0, new Search(searchInput, dotPosition, fileName));
            }
        }

        if (add) {
            previousSearchList.add(0, toAdd);
        }

        if (previousSearchList.size() > 5) {
            Log.d("EVAL", previousSearchList.remove(5).toString());
        }
    }

    public ArrayList<Search> getList() {
        return previousSearchList;
    }
}
