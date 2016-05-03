package se.mah.mapster.mapster_07;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
                if (previousSearchList.size() > 0)
                    mainActivity.search(previousSearchList.get(0));
                Log.d("EVAL", "Nothing to search, brah");
                break;

            case R.id.previous_search_2:
                if (previousSearchList.size() > 1)
                    mainActivity.search(previousSearchList.get(1));
                break;

            case R.id.previous_search_3:
                if (previousSearchList.size() > 2)
                    mainActivity.search(previousSearchList.get(2));
                break;

            case R.id.previous_search_4:
                if (previousSearchList.size() > 3)
                    mainActivity.search(previousSearchList.get(3));
                break;

            case R.id.previous_search_5:
                if (previousSearchList.size() > 4)
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

    public ArrayList<String> getStringList() {
        ArrayList<String> resultValue = new ArrayList<>();

        for (Search search : previousSearchList) {
            resultValue.add(search.toString());
        }

        return resultValue;
    }

    /**
     * Converts an ArrayList of strings to an ArrayList of Searches.
     *
     * @param input ArrayList with strings made by PreviousSearchListener.getStringList()
     * @return ArrayList with search objects.
     */
    private ArrayList<Search> convertList(ArrayList<String> input) {

        ArrayList<Search> convertedList = new ArrayList<>();

        for (String str : input) {

            String[] strArray = str.split(",");
            String[] searchArray = new String[4];

            for (int i = 0; i < 4; i++) {
                searchArray[i] = strArray[i];
            }

            int[] dotsPos = new int[2];
            dotsPos[0] = Integer.parseInt(strArray[4]);
            dotsPos[1] = Integer.parseInt(strArray[5]);

            convertedList.add(new Search(searchArray, dotsPos, strArray[6]));
        }

        return convertedList;
    }

    public void setList(ArrayList inputList) {
        this.previousSearchList = convertList(inputList);
        Log.d("EVAL", "List set");
    }
}
