package se.mah.mapster.wheelnumbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    private NumberPicker wheel, picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wheel = (NumberPicker) findViewById(R.id.secondScroll);
        wheel.setMinValue(0);
        wheel.setMaxValue(6);

        picker = (NumberPicker) findViewById(R.id.firstScroll);
        picker.setMinValue(0);
        picker.setMaxValue(2);
        picker.setDisplayedValues(new String[]{"G8", "Ni", "Or"});

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (picker.getValue() == 1) {
                        disableWheel();
                    }
                }
            }
        }).start();
    }

    public void disableWheel() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wheel.setEnabled(false);
            }
        });
    }
}
