package se.mah.mapster.mapster_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

/**
 * Created by Kristoffer on 31/03/16.
 */
public class MapsSettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CheckedTextView orkanenCheckTV, niagaraCheckTV, gaddanCheckTV;
    private Listener listener;
    private SharedPreferences mPreferences;
    private boolean nigaraChecked, orkanenChecked, gaddanChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listener = new Listener();
        createCheckedTextView();

        mPreferences = getSharedPreferences("BuildingsToggles",
                MODE_PRIVATE);
        getCheckBoxState();
    }

    private void getCheckBoxState() {
        nigaraChecked = mPreferences.getBoolean("NiagaraChecked", false);
        orkanenChecked = mPreferences.getBoolean("OrkanenChecked", false);
        gaddanChecked = mPreferences.getBoolean("GaddanChecked", false);

        Log.d("EVAL", "Niagara: " + nigaraChecked);
        Log.d("EVAL", "Orkanen: " + orkanenChecked);
        Log.d("EVAL", "Gäddan: " + gaddanChecked);

        niagaraCheckTV.setChecked(nigaraChecked);
        orkanenCheckTV.setChecked(orkanenChecked);
        gaddanCheckTV.setChecked(gaddanChecked);
    }

    private void createCheckedTextView() {
        orkanenCheckTV = (CheckedTextView) findViewById(R.id.checked_orkanen);
        niagaraCheckTV = (CheckedTextView) findViewById(R.id.checked_niagara);
        gaddanCheckTV = (CheckedTextView) findViewById(R.id.checked_gaddan);

        orkanenCheckTV.setOnClickListener(listener);
        niagaraCheckTV.setOnClickListener(listener);
        gaddanCheckTV.setOnClickListener(listener);
    }

    private class Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.checked_orkanen) {
                orkanenCheckTV.toggle();
                if (orkanenCheckTV.isChecked()) {
                    orkanenChecked = true;
                    Toast.makeText(getApplicationContext(), "Downloading Maps for Orkanen", Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //The string sent to server
                                new OfflineHandler("#gaddan");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //Saves the state of the toggle to shared Preferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("OrkanenChecked", orkanenChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the Orkanenstate: " + orkanenChecked);
                } else {
                    orkanenChecked = false;

                    Toast.makeText(getApplicationContext(), "Deleting Maps for Orkanen", Toast.LENGTH_SHORT).show();

                    //Saves the state of the toggle to shared Preferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("OrkanenChecked", orkanenChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the Orkanenstate: " + orkanenChecked);
                }

            } else if (id == R.id.checked_gaddan) {
                gaddanCheckTV.toggle();
                if (gaddanCheckTV.isChecked()) {
                    gaddanChecked = true;
                    Toast.makeText(getApplicationContext(), "Downloading Maps for Gäddan", Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //The string sent to server
                                new OfflineHandler("#gaddan");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //Saves the state of the toggle to shared Preferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("GaddanChecked", gaddanChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the Gäddanstate: " + gaddanChecked);
                } else {
                    gaddanChecked = false;

                    Toast.makeText(getApplicationContext(), "Deleting Maps for Gäddan", Toast.LENGTH_SHORT).show();

                    //Saves the state of the toggle to shared Preferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("GaddanChecked", gaddanChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the Gäddanstate: " + gaddanChecked);
                }

            } else if (id == R.id.checked_niagara) {
                niagaraCheckTV.toggle();
                if (niagaraCheckTV.isChecked()) {
                    nigaraChecked = true;
                    Toast.makeText(getApplicationContext(), "Downloading Maps for Niagara", Toast.LENGTH_SHORT).show();

                    Log.d("EVAL", "Pre Building offline");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //The string sent to server
                                new OfflineHandler("#niagara");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Log.d("EVAL", "Buildingoffline initiated");

                    //Saves the state of the toggle to shared Preferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("NiagaraChecked", nigaraChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the state: " + nigaraChecked);
                    Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                } else {
                    //Saves the state of the toggle to shared Preferences
                    nigaraChecked = false;

                    Toast.makeText(getApplicationContext(), "Deleting Maps for Niagara", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("NiagaraChecked", nigaraChecked);
                    editor.commit();
                    Log.d("EVAL", "Saved the state: " + nigaraChecked);
                }
            }
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

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_find) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mah.se/kartor-mah"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}