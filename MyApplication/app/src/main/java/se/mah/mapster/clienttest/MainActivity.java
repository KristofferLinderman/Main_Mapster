package se.mah.mapster.clienttest;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button connect;
    private ClientThread clientThread = new ClientThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button) findViewById(R.id.connectButton);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();
                new ClientThread().start();
            }
        });
    }

    private class ClientThread extends Thread {
        private Socket socket;
        ObjectInputStream ois;

//        public ClientThread(){
//            Toast.makeText(getApplicationContext(),"Thread Started",Toast.LENGTH_SHORT).show();
//        }

        public void run() {
            try {
                Log.d("EVAL","1");
                socket = new Socket("localhost", 9999);
                Log.d("EVAL", "2");
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                ois = new ObjectInputStream(socket.getInputStream());
                Log.d("EVAL","3");

                while (true) {
                    Toast.makeText(getApplicationContext(), ois.readUTF(), Toast.LENGTH_SHORT).show();
                    Log.d("EVAL", "4");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


