package se.mah.mapster.mapster_05;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Spinner buildingSpinner, sectionSpinner, levelSpinner, roomSpinner;
    private SearchListener searchListener;
    private PreviousSearchListener previousSearchListener;
    private Button searchButton, previousSearch1Button, previousSearch2Button, previousSearch3Button, previousSearch4Button, previousSearch5Button;
    private int[] dotPosition;

    /*TODO Edit X/Y pos
       Make these get information from server/Database instead
        */
    private int xPosition = 350;
    private int yPosition = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addBuildingSpinner();
        addLevelSpinner();
        addRoomSpinner();
        addSectionSpinner();

        initiatePreviousSearch();
        initiateSearchButton();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        searchListener = new SearchListener(this, buildingSpinner, levelSpinner, roomSpinner);
        searchListener.setMainActivity(this);

        searchButton = (Button) findViewById(R.id.search_Button);
        searchButton.setOnClickListener(searchListener);
    }

    public void search(String[] search, int[] dotPosition) {
        Intent i = new Intent(getApplicationContext(), MapViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Positions", dotPosition);
        startActivity(i);
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public int getImageID() {
        return R.drawable.orkanenhigh;
    }

    private void addLevelSpinner() {
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

        //Create a ArrayAdapter using the StringArray "level_list" in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        levelSpinner.setAdapter(adapter);
    }

    private void addRoomSpinner() {
        roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.room_list, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        roomSpinner.setAdapter(adapter);
    }

    private void addBuildingSpinner() {
        buildingSpinner = (Spinner) findViewById(R.id.buildingSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.building_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        buildingSpinner.setAdapter(adapter);
    }

    private void addSectionSpinner() {
        sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.section_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sectionSpinner.setAdapter(adapter);
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