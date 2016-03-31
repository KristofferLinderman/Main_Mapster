package se.mah.mapster.mapster_02;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Spinner buildingSpinner, levelSpinner, roomSpinner;
    private SearchListener listener;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addBuildingSpinner();
        addLevelSpinner();
        addRoomSpinner();

        listener = new SearchListener();

        searchButton = (Button) findViewById(R.id.search_Button);
        searchButton.setOnClickListener(listener);

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

    //@Override
    //Removes the three dots in the top right corner
  /*  public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);
        return false;
    }*/

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
        } else if (id == R.id.maps) {
            startActivity(new Intent("Mapster_0.22.MapsActivity"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SearchListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if(id == R.id.search_Button){
                Toast.makeText(getApplicationContext(),"Searching...",Toast.LENGTH_SHORT).show();
            }
        }
    }
}