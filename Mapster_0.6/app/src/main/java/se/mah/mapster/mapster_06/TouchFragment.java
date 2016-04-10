package se.mah.mapster.mapster_06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kristoffer on 08/04/16.
 */
public class TouchFragment extends Fragment {
    private Context context;
    private Bitmap bitmap;
    private Activity activity;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TouchImageView img = new TouchImageView(context);
        img.setImageBitmap(bitmap);
        img.setMaxZoom(4f);
        activity.setContentView(img);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.touch_fragment, container, false);
    }
}
