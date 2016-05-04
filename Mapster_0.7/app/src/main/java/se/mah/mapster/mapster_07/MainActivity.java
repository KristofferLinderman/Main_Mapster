package se.mah.mapster.mapster_07;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NumberPicker buildingPicker, sectionPicker, levelPicker, roomPicker;
    private SearchListener searchListener;
    private PreviousSearchListener previousSearchListener;
    private Button searchButton, previousSearch1Button, previousSearch2Button,
            previousSearch3Button, previousSearch4Button, previousSearch5Button;
    private ArrayList<Button> previousSearchBtnList;
    private File fileInDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + "previousSearches");
    private SharedPreferences prefs;
    private Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("MAPSTER", "No SDCARD");
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            directory.mkdirs();
        }
        prefs = this.getSharedPreferences("savedPreviousSearches", Context.MODE_PRIVATE);
        edit = prefs.edit();

        setContentView(R.layout.main_activity);
        setTitle("");
        verifyStoragePermissions();

        addBuildingPicker();
        addLevelPicker();
        addRoomPicker();
        addSectionPicker();

        initiatePreviousSearch();
        initiateSearchButton();
        initiateNavigationDrawer();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        ArrayList<String> toBeSaved = previousSearchListener.getStringList(); // fetch the data
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putStringSet("SAVEDATA", new HashSet<String>(toBeSaved));
//        edit.commit();
//
////
////        ArrayList<String> tempStringList = previousSearchListener.getStringList();
////
////        if (tempStringList != null) {
////            Set<String> set = new HashSet<String>();
////            set.addAll(tempStringList);
////
////            edit.putStringSet("previousSearch", set);
////            edit.commit();
////        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        ArrayList<String> retrieved = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));
//        Log.d("EVAL", retrieved.toString());
//        previousSearchListener.setList(retrieved);
//        Log.d("EVAL","list set");
//        updatePreviousSearch();
//        Log.d("EVAL","list up");
////        Set<String> set = prefs.getStringSet("previousSearch", null);
////        if (set != null) {
////            ArrayList<String> temp = new ArrayList<String>(set);
////
////            previousSearchListener.setList(temp);
////            updatePreviousSearch();
////            Log.d("EVAL", "Resumed");
////        }
//    }

    private void initiateNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(3);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Storage Permissions for API 23+
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage (API 23+)
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    private void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initiatePreviousSearch() {
        previousSearch1Button = (Button) findViewById(R.id.previous_search_1);
        previousSearch2Button = (Button) findViewById(R.id.previous_search_2);
        previousSearch3Button = (Button) findViewById(R.id.previous_search_3);
        previousSearch4Button = (Button) findViewById(R.id.previous_search_4);
        previousSearch5Button = (Button) findViewById(R.id.previous_search_5);

        previousSearchListener = new PreviousSearchListener(this);

        previousSearchBtnList = new ArrayList<>();
        previousSearchBtnList.add(previousSearch1Button);
        previousSearchBtnList.add(previousSearch2Button);
        previousSearchBtnList.add(previousSearch3Button);
        previousSearchBtnList.add(previousSearch4Button);
        previousSearchBtnList.add(previousSearch5Button);

        previousSearch1Button.setOnClickListener(previousSearchListener);
        previousSearch2Button.setOnClickListener(previousSearchListener);
        previousSearch3Button.setOnClickListener(previousSearchListener);
        previousSearch4Button.setOnClickListener(previousSearchListener);
        previousSearch5Button.setOnClickListener(previousSearchListener);
    }

    private void initiateSearchButton() {
        searchListener = new SearchListener(this, buildingPicker, sectionPicker, levelPicker, roomPicker);
        searchListener.setMainActivity(this);

        searchButton = (Button) findViewById(R.id.search_Button);
        searchButton.setOnClickListener(searchListener);
    }

    public void search(String[] search, int[] dotPosition, String fileName) {
        previousSearchListener.addPreviousSearch(search, dotPosition);
        updatePreviousSearch();

        Intent mapViewIntent = new Intent(getApplicationContext(), MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mapViewIntent.putExtra("Positions", dotPosition);
        mapViewIntent.putExtra("Search", search);
        mapViewIntent.putExtra("Filename", fileName);

        startActivity(mapViewIntent);
    }

    public void search(Search search) {
        int[] temp = search.getDotPosition();
        Log.d("EVAL", "Previous Search coordinates: " + temp[0] + " " + temp[1]);
        search(search.getSearch(), temp, search.getFileName());
    }

    private void updatePreviousSearch() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Search> listOfPrevSearches = previousSearchListener.getList();
                for (int i = 0; i < listOfPrevSearches.size(); i++) {
                    previousSearchBtnList.get(i).setText(listOfPrevSearches.get(i).getSearchQuery());
                }
            }
        });
    }

    public void makeToast(String messageToDisplay) {
        final String message = messageToDisplay;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBuildingPicker() {
        buildingPicker = (NumberPicker) findViewById(R.id.buildingPicker);
        buildingPicker.setMinValue(0);
        buildingPicker.setMaxValue(2);
        buildingPicker.setDisplayedValues(new String[]{"OR", "NI", "G8"});

        //Makes it not ediable
        buildingPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addSectionPicker() {
        sectionPicker = (NumberPicker) findViewById(R.id.sectionPicker);
        sectionPicker.setMinValue(0);
        sectionPicker.setMaxValue(5);
        sectionPicker.setDisplayedValues(new String[]{"A", "B", "C", "D", "E", "F"});
        sectionPicker.setWrapSelectorWheel(false);

        //Makes it not ediable
        sectionPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addLevelPicker() {
        levelPicker = (NumberPicker) findViewById(R.id.levelPicker);
        levelPicker.setMinValue(1);
        levelPicker.setMaxValue(6);
        levelPicker.setWrapSelectorWheel(false);

        //Makes it not ediable
        levelPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addRoomPicker() {
        roomPicker = (NumberPicker) findViewById(R.id.roomPicker);
        roomPicker.setWrapSelectorWheel(false);
        roomPicker.setMinValue(1);
        roomPicker.setMaxValue(41);

//        roomPicker.setDisplayedValues(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
//                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
//                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "72"});

        //Makes it not ediable
        roomPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            startActivity(new Intent("Mapster.AboutActivity"));
        } else if (id == R.id.nav_find) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mah.se/kartor-mah"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_dumb) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/anton.lagerlof.3?fref=ts"));
            startActivity(browserIntent);
        } else if (id == R.id.maps_settings) {
            startActivity(new Intent("Mapster.MapsSettingsActivity"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}