package se.mah.mapster.mapster_06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;


/**
 * Created by Kristoffer on 01/04/16.
 */
public class MapViewActivity extends AppCompatActivity {

    //    private ImageView imageView;
    private Button button;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private final int radius = 10;
    private int[] dotPosition;
    private String[] searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_view_activity);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        dotPosition = getIntent().getIntArrayExtra("Positions");
        paint(dotPosition[0], dotPosition[1], dotPosition[2]);

        searchQuery = getIntent().getStringArrayExtra("Search");
        String search = createSearchString();
        setTitle(search);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private String createSearchString() {
        String temp = "";

        if (searchQuery[0].equals("OR")) {
            temp += "Orkanen ";
        } else if (searchQuery[0].equals("NI")) {
            temp += "Niagara ";
        } else if (searchQuery[0].equals("G8")) {
            temp += "Gäddan ";
        }

        temp += "Level " + searchQuery[2] + " - ";

        temp += searchQuery[0] + searchQuery[1] + searchQuery[2] + searchQuery[3];


        return temp;
    }

    public void paint(int x, int y, int id) {
        //Bitmap settings
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        myOptions.inPurgeable = true;

        if (id == 0)
            id = R.drawable.orkanenhigh;

        //Paint and create bitmap from image
        bitmap = BitmapFactory.decodeResource(getResources(), id, myOptions);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Create canvas from bitmap and draw
//        canvas = new Canvas(mutableBitmap);
//        canvas.drawCircle(x, y, 15, paint);
//
//        TouchImageView img = new TouchImageView(this);
//        img.setImageBitmap(mutableBitmap);
//        img.setMaxZoom(4f);
//        setContentView(img);

        //animation
        canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x, y, radius, paint);

//        TouchFragment fragment = new TouchFragment();
//        fragment.setActivity(this);
//        fragment.setBitmap(mutableBitmap);
//        fragment.setContext(getApplicationContext());

        TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(mutableBitmap);
        img.setMaxZoom(4f);
        setContentView(img);

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        } else if (id == R.id.nav_find) {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mah.se/kartor-mah"));
//            startActivity(browserIntent);
//        } else if (id == R.id.nav_dumb) {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/anton.lagerlof.3?fref=ts"));
//            startActivity(browserIntent);
//        } else if (id == R.id.nav_about) {
//            startActivity(new Intent(this, AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        }else if(id == R.id.maps_settings){
//            startActivity(new Intent(this, MapsSettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

}