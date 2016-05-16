package se.mah.mapster.mapster_07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Kristoffer on 11/05/16.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final ImageView iv = (ImageView) findViewById(R.id.spalshIV);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.pulse);

        iv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent main = new Intent(getBaseContext(), MainActivity.class);
                startActivity(main);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
