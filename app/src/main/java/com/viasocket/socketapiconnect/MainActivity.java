package com.viasocket.socketapiconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import rakshit.socketapiconnect.Argument;
import rakshit.socketapiconnect.SocketApiConnect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(1000);
                        SocketApiConnect socketApiConnect = new SocketApiConnect("YOUR_FLOW_ID", "YOUR_TEAM_AUTHKEY");
                        HashMap<String, Argument> map = new HashMap<>();
                        map.put("TEST_KEY", new Argument("data", "test_value"));
                        try {
                            socketApiConnect.call(map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
