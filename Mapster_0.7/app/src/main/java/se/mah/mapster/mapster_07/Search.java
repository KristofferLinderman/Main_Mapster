package se.mah.mapster.mapster_07;

/**
 * Created by Kristoffer on 25/04/16.
 */
public class Search {
    private String[] search;
    private int[] dotPosition;
    private String mapDicrectory;

    public Search(String[] search, int[] dotPosition,String mapDirectory) {
        this.search = search;
        this.dotPosition = dotPosition;
        this.mapDicrectory = mapDirectory;
    }

    public String[] getSearch() {
        return search;
    }

    public int[] getDotPosition() {
        return dotPosition;
    }

    public String getMapDirectory() {
        return mapDicrectory;
    }

    /**
     * Returns a string formated to display the seach
     * @return String with suitable format
     */
    public String getSearchQuery(){
        if(search!=null)
            return search[0] + ":" + search[1] + search[2] + search[3];
        else
            return " ";
    }
}
