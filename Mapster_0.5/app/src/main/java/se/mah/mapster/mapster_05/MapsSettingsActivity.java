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
    }

    private void createCheckedTextView(){
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
                if (orkanenCheckTV.isChecked())
                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.checked_gaddan) {
                gaddanCheckTV.toggle();
                if (gaddanCheckTV.isChecked())
                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.checked_niagara) {
                niagaraCheckTV.toggle();
                if (niagaraCheckTV.isChecked())
                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.nav_dumb) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/anton.lagerlof.3?fref=ts"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
