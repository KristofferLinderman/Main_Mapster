package se.mah.mapster.mapster_07;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NumberPicker buildingPicker, sectionPicker, levelPicker, roomPicker;
    private SearchListener searchListener;
    private PreviousSearchListener previousSearchListener;
    private Button searchButton, previousSearch1Button, previousSearch2Button, previousSearch3Button, previousSearch4Button, previousSearch5Button;
    private ClientThread clientThread;
    private Bitmap map;
    /*
    TODO Edit X/Y pos
       Make these get information from server/Database instead
        */
    private int xPosition = 350;
    private int yPosition = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("MAPSTER", "No SDCARD");
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster");
            directory.mkdirs();
        }

        Toast.makeText(MainActivity.this, "Welcome to the first Mapster", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.main_activity);

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

        previousSearchListener = new PreviousSearchListener();
        previousSearchListener.setMainActivity(this);

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

    public void search(String[] search, int[] dotPosition, Bitmap map) {
        MapViewActivity.setMain(this);
        this.map = map;

        Intent i = new Intent(getApplicationContext(), MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Positions", dotPosition);
        i.putExtra("Search", search);
        startActivity(i);
    }

    public Bitmap getBitmap() {
        return map;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public int getImageID() {
        return R.drawable.niggafive;
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
        roomPicker.setMaxValue(35);

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
            startActivity(new Intent("Mapster_0.22.AboutActivity"));
        } else if (id == R.id.nav_find) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mah.se/kartor-mah"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_dumb) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/anton.lagerlof.3?fref=ts"));
            startActivity(browserIntent);
        } else if (id == R.id.maps_settings) {
            startActivity(new Intent("Mapster_0.22.MapsSettingsActivity"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }








}