package se.mah.mapster.clienttest;

import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
        try {
            Log.d("EVAL", "1");
            socket = new Socket("192.168.0.2", 8080);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                Log.d("EVAL", "2");
                dos = new DataOutputStream(socket.getOutputStream());
                write("Hello Server");
                dos.flush();
                Log.d("EVAL", "3");
                dis = new DataInputStream(socket.getInputStream());
                res = dis.readUTF();
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
}

