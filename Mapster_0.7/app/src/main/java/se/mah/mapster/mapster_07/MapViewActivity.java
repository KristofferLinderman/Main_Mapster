package se.mah.mapster.mapster_07;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.File;

public class MapViewActivity extends AppCompatActivity {

    private final int radius = 10;
    private int[] dotPosition;
    private String[] searchQuery;
    private Button backBtn;
    private Bitmap bitmap, mutableBitmap;
    private Canvas canvas;
    private Paint paint;
    private String filename;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_view_activity);

        //get positions for the dot to indicate choosen room
        dotPosition = getIntent().getIntArrayExtra("Positions");
        //get the search made
        searchQuery = getIntent().getStringArrayExtra("Search");
        //Get filename
        filename = getIntent().getStringExtra("Filename");

        try {
            paint(dotPosition[0], dotPosition[1]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setTitle(createSearchString());
    }

    /**
     * Generates a string based on the search. String is generated in format "Building Level X - ZYQW"
     *
     * @return string of search
     */
    private String createSearchString() {
        String temp = "";

        if (searchQuery[0].equals("OR")) {
            temp += "Orkanen ";
        } else if (searchQuery[0].equals("NI")) {
            temp += "Niagara ";
            temp += searchQuery[0] + ":" + searchQuery[1] + searchQuery[2] + searchQuery[3];
            return temp;
        } else if (searchQuery[0].equals("G8")) {
            temp += "Gäddan ";
        }

        temp += "Level " + searchQuery[2] + " - ";

        temp += searchQuery[0] + ":" + searchQuery[1] + searchQuery[2] + searchQuery[3];

        return temp;
    }

    public void paint(int x, int y) {
        //Bitmap settings
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        myOptions.inPurgeable = true;

        image = new File(Environment.getExternalStorageDirectory() + File.separator + "Mapster", filename);
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), myOptions);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Create canvas from bitmap and draw
        canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x, y, 15, paint);

        TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(mutableBitmap);
        img.setBackgroundColor(Color.WHITE);
        img.setMaxZoom(4f);
        setContentView(img);
    }
}
