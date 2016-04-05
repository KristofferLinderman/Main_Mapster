package se.mah.mapster.clienttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button connect,send;
    private ClientThread clientThread;
    private EditText toSend;
    private TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clientThread = new ClientThread();
        clientThread.setMain(this);


        connect = (Button) findViewById(R.id.connectButton);
        send = (Button) findViewById(R.id.button);
        toSend = (EditText) findViewById(R.id.toSend);
        response = (TextView) findViewById(R.id.serverTV);

        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();
                clientThread.start();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clientThread.write(toSend.getText().toString());
            }
        });

    }

    public void display(String toDisplay){
        response.setText(toDisplay);
    }
}

