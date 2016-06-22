package com.example.guest.citywheather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] mCityNames = { "Nizhny Novgorod", "Moscow", "Vladimir", "Kostroma", "Kiev", "Mozdok" };

    private Button mBtnRequest;
    private Button mBtnGet;
    private TextView mTVCityName;

    private TextView mTVCountry;
    private TextView mTVWeGen;
    private TextView mTVWeDesc;
    private TextView mTVTemper;
    private TextView mTVWePressure;
    private TextView mTVWeHumidity;
    private TextView mTVWeWindSpeed;

    LocalService mService;
    boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // находим список
        ListView lvMain = (ListView) findViewById(R.id.lvCities);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCityNames);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        mTVCityName = (TextView)findViewById(R.id.tvCityName);
        mTVCountry = (TextView)findViewById(R.id.tvCountry);
        mTVWeGen = (TextView)findViewById(R.id.tvWeGen);
        mTVWeDesc = (TextView)findViewById(R.id.tvWeDesc);
        mTVTemper = (TextView)findViewById(R.id.tvTemper);
        mTVWePressure = (TextView)findViewById(R.id.tvPressure);
        mTVWeHumidity = (TextView)findViewById(R.id.tvHumidity);
        mTVWeWindSpeed = (TextView)findViewById(R.id.tvWeWindSpeed);

        mBtnRequest = (Button)findViewById(R.id.bRequest);
        mBtnRequest.setOnClickListener(new View.OnClickListener() {
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
//                    int num = mService.getRandomNumber();
//                    Toast.makeText(MainActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
                    mService.requestCWData();

                    mTVCityName.setText("");
                    mTVCountry.setText("");
                    mTVWeGen.setText("");
                    mTVWeDesc.setText("");
                    mTVTemper.setText("");
                    mTVWePressure.setText("");
                    mTVWeHumidity.setText("");
                    mTVWeWindSpeed.setText("");

                    Toast.makeText(MainActivity.this, "Request CWData...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBtnGet = (Button)findViewById(R.id.bGet);
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
//                    int num = mService.getRandomNumber();
//                    Toast.makeText(MainActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
                    CWData gotRes = mService.getCWData();
                    if (gotRes != null) {
                        mTVCityName.setText(gotRes.mName);
                        mTVCountry.setText(gotRes.mCountry);
                        mTVWeGen.setText(gotRes.mWeGen);
                        mTVWeDesc.setText(gotRes.mWeDesc);
                        mTVTemper.setText(Double.toString(gotRes.mWeTemp));
                        mTVWePressure.setText(Integer.toString(gotRes.mWePressure));
                        mTVWeHumidity.setText(Integer.toString(gotRes.mWeHumidity));
                        mTVWeWindSpeed.setText(Double.toString(gotRes.mWeWindSpeed));
                    }
                    Toast.makeText(MainActivity.this, "Get CWData...", Toast.LENGTH_SHORT).show();

                }



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
