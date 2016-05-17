package se.mah.mapster.mapster_1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NumberPicker.OnValueChangeListener {

    private NumberPicker buildingPicker, sectionPicker, levelPicker, roomPicker;
    private SearchListener searchListener;
    private PreviousSearchListener previousSearchListener;
    private Button searchButton, previousSearch1Button, previousSearch2Button,
            previousSearch3Button, previousSearch4Button, previousSearch5Button;
    private ArrayList<Button> previousSearchBtnList;
    private File fileInDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster" + File.separator + "previousSearches");
    private SharedPreferences prevSearches;
    private SharedPreferences.Editor prevSearchesEditor;
    private boolean saveState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if the folder Mapster is availible or if the external storage isn't mounted.
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("MAPSTER", "No SDCARD");
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            directory.mkdirs();
        }

        SharedPreferences sp = getSharedPreferences("FirstBoot", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (!sp.getBoolean("first", false)) {
            editor.putBoolean("first", true);
            editor.commit();
            Intent intent = new Intent(this, MyIntro.class);
            startActivity(intent);
        }

//        Intent intent = new Intent(this, MyIntro.class);
//        startActivity(intent);


        prevSearches = getSharedPreferences("Previous Searches", Context.MODE_PRIVATE);
        prevSearchesEditor = prevSearches.edit();

        setContentView(R.layout.main_activity);
        setTitle("Mapster");
        verifyStoragePermissions();

        addBuildingPicker();
        addLevelPicker();
        addRoomPicker();
        addSectionPicker();

        initiatePreviousSearch();
        initiateSearchButton();
        initiateNavigationDrawer();
    }

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

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Gson gson = new Gson();
//        String json = gson.toJson(previousSearchListener.getList());
//        prevSearchesEditor.putString("Previous Search List", json);
//        prevSearchesEditor.commit();
//
//        saveState = true;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Gson gson = new Gson();
//        String json = prevSearches.getString("Previous Search List", "");
////        ArrayList<Search> temp = gson.fromJson(json, ArrayList.class);
////        if (temp != null)
////            for (Search s : temp) {
////                Log.d("RESUME", s.toString());
////            }
//
//        if (saveState) {
//            HashMap<String, Search> featuresFromJson = new Gson().fromJson(json, new TypeToken<Map<String, Search>>() {
//            }.getType());
//            ArrayList<Search> temp = new ArrayList<>();
//
//            for (Map.Entry<String, Search> entry : featuresFromJson.entrySet()) {
//                Search search = entry.getValue();
//                temp.add(search);
//            }
//            previousSearchListener.setList(temp);
//        }
//    }

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
        setDividerColor(buildingPicker);
        buildingPicker.setOnValueChangedListener(this);

        //Makes it not ediable
        buildingPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addSectionPicker() {
        sectionPicker = (NumberPicker) findViewById(R.id.sectionPicker);
        sectionPicker.setMinValue(0);
        sectionPicker.setMaxValue(5);
        sectionPicker.setDisplayedValues(new String[]{"A", "B", "C", "D", "E", "F"});
        sectionPicker.setWrapSelectorWheel(false);
        setDividerColor(sectionPicker);

        //Makes it not ediable
        sectionPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addLevelPicker() {
        levelPicker = (NumberPicker) findViewById(R.id.levelPicker);
        levelPicker.setMinValue(1);
        levelPicker.setMaxValue(6);
        levelPicker.setWrapSelectorWheel(false);
        setDividerColor(levelPicker);

        //Makes it not ediable
        levelPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void addRoomPicker() {
        roomPicker = (NumberPicker) findViewById(R.id.roomPicker);
        roomPicker.setWrapSelectorWheel(false);
        roomPicker.setMinValue(1);
        roomPicker.setMaxValue(41);
        roomPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        setDividerColor(roomPicker);

        //Makes it not ediable
        roomPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void niagaraValues() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sectionPicker.setDisplayedValues(new String[]{"A", "B", "C"});
            }
        });
    }

    private void startPickerMonotor() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String[] temp;
                    temp = buildingPicker.getDisplayedValues();
                    if (temp[buildingPicker.getValue()] == "NI") {
                        niagaraValues();
                    } else if (temp[buildingPicker.getValue()] == "G8") {
                        levelPicker.setEnabled(false);
                    }

                    temp = sectionPicker.getDisplayedValues();

//                    //Lägg till 0 om niagara
//                    if (search[0].equals("NI")) {
//                        search[2] = "0" + levelPicker.getValue();
//                    } else {
//                        search[2] = "" + levelPicker.getValue();
//                    }
//
//                    int room = roomPicker.getValue();
//
//                    //Format for rooms are 0X for numbers under 10
//                    if (room > 9) {
//                        search[3] = "" + roomPicker.getValue();
//                    } else {
//                        search[3] = "0" + roomPicker.getValue();
//                    }

                }
            }
        }).start();
    }


    private void setDividerColor(NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
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
        } else if (id == R.id.maps_settings) {
            startActivity(new Intent("Mapster.MapsSettingsActivity"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Change the values depending on the selected building
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        String[] temp;
        temp = picker.getDisplayedValues();

        if (temp[picker.getValue()] == "NI") {
            sectionPicker.setValue(0);
            sectionPicker.setMaxValue(2);
            sectionPicker.setDisplayedValues(new String[]{"A", "B", "C"});
            sectionPicker.setEnabled(true);

            levelPicker.setValue(0);
            levelPicker.setMaxValue(6);
        } else if (temp[picker.getValue()] == "OR") {
            sectionPicker.setValue(0);
            sectionPicker.setMaxValue(5);
            sectionPicker.setDisplayedValues(new String[]{"A", "B", "C", "D", "E", "F"});
            sectionPicker.setEnabled(true);

            levelPicker.setValue(0);
            levelPicker.setMaxValue(5);
        } else if (temp[picker.getValue()] == "G8") {
            sectionPicker.setValue(0);
            sectionPicker.setDisplayedValues(new String[]{" ", " "});
            sectionPicker.setEnabled(false);

            levelPicker.setValue(0);
            levelPicker.setMaxValue(5);
        }
    }
}