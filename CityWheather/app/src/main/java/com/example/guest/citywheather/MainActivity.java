package com.example.guest.citywheather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mCity1Button;
    private Button mCity2Button;

    LocalService mService;
    boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCity1Button = (Button)findViewById(R.id.bCity1);
        mCity1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                // TO DO!
                Toast.makeText(MainActivity.this,
                        R.string.city_name_1,
                        Toast.LENGTH_SHORT).show();
*/
                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    int num = mService.getRandomNumber();
                    Toast.makeText(MainActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
                }

            }
        });

        mCity2Button = (Button)findViewById(R.id.bCity2);
        mCity2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TO DO!
                Toast.makeText(MainActivity.this,
                        R.string.city_name_2,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
