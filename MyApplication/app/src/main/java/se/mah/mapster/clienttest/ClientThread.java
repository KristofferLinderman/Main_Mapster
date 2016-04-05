package se.mah.mapster.clienttest;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Kristoffer on 05/04/16.
 */
public class ClientThread extends Thread {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private MainActivity mainActivity;
    private String res;

    public void setMain(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void run() {
        while(true) {
            try {
                Log.d("EVAL", "1");
                socket = new Socket("10.2.15.25", 8080);
                Log.d("EVAL", "2");
                dos = new DataOutputStream(socket.getOutputStream());
                write("Hello Server");
                dos.flush();
                Log.d("EVAL", "3");
                dis = new DataInputStream(socket.getInputStream());
                res = dis.readUTF();
                display(res);
                Log.d("EVAL", res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String toSend) {
        try {
            dos.writeUTF(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void display(String toDisplay){
        mainActivity.display(toDisplay);
    }
}

