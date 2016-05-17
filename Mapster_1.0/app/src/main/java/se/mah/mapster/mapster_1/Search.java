package se.mah.mapster.mapster_1;

import java.io.Serializable;

/**
 * Created by Kristoffer on 25/04/16.
 */
public class Search implements Serializable {
    private String[] search;
    private int[] dotPosition;
    private String fileName;

    public Search(String[] search, int[] dotPosition, String fileName) {
        this.search = search;
        this.dotPosition = dotPosition;
        this.fileName = fileName;
    }

    public String[] getSearch() {
        return search;
    }

    public int[] getDotPosition() {
        return dotPosition;
    }

    public String getFileName() {
        return fileName;
    }

    public String toString() {
        String str = new String();

        str += search[0] + "-" + search[1] + "-" + search[2] + "-" + search[3] + "-" + dotPosition[0] + "-" + dotPosition[1] + "-" + fileName;

        return str;
    }

    /**
     * Returns a string formated to display the seach
     *
     * @return String with suitable format
     */
    public String getSearchQuery() {
        if (search != null)
            return search[0] + ":" + search[1] + search[2] + search[3];
        else
            return " ";
    }
}
