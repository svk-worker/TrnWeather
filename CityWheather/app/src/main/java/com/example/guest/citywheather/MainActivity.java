package com.example.guest.citywheather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {

    private final String LOG_TAG = "MainActivity";

    // for save in Bundle
    private static final String KEY_SELECTED_CITY = "SelectedCity";
    private static final String KEY_CW_DATA = "CWData";
    private static final String KEY_IS_REQ_INPROGRESS = "IsReqInprogress";

    String[] mCityNames = { "Nizhny Novgorod", "Moscow", "Vladimir", "Kostroma", "Kiev", "Mozdok" };

    private String mSelectedCity = null;
    private CWData mCWDataItem = null;
    private boolean mIsReqInprogress = false;     // true if request to get CW data is in progress


    private Button mBtnRequest;
    private TextView mTVCityName;

    // weather interface
    private TextView mTVCountry;
    private TextView mTVWeGen;
    private TextView mTVWeDesc;
    private TextView mTVTemper;
    private TextView mTVWePressure;
    private TextView mTVWeHumidity;
    private TextView mTVWeWindSpeed;

    // Service data
    LocalService mService;
    boolean mBound = false;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(LOG_TAG, "START: onSaveInstanceState()...");
        savedInstanceState.putString(KEY_SELECTED_CITY, mSelectedCity);
        savedInstanceState.putSerializable(KEY_CW_DATA, mCWDataItem);
        savedInstanceState.putBoolean(KEY_IS_REQ_INPROGRESS, mIsReqInprogress);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "START: onCreate()...");

        if (savedInstanceState != null) {
            mSelectedCity = savedInstanceState.getString(KEY_SELECTED_CITY);
            mCWDataItem = (CWData)savedInstanceState.getSerializable(KEY_CW_DATA);
            mIsReqInprogress = savedInstanceState.getBoolean(KEY_IS_REQ_INPROGRESS);
        }

        // find list
        final ListView lvMain = (ListView) findViewById(R.id.lvCities);

        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCityNames);

        // set adapter to list
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedCity = lvMain.getAdapter().getItem(position).toString();
                Log.d(LOG_TAG, "itemClick: position = " + position + ",  city = " + mSelectedCity);
            }
        });

        mBtnRequest = (Button)findViewById(R.id.bRequest);
        mBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    if (mSelectedCity == null) {
                        Toast.makeText(MainActivity.this, "Please select concrete city to get weather!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // clear weather data from previous request
                        mCWDataItem = null;
                        updateCWDataUI();

                        // Disable request button and request data from Service
                        mIsReqInprogress = true;
                        mBtnRequest.setEnabled(false);
                        mService.requestCWData(mSelectedCity);
                    }
                }
            }
        });

        mTVCityName = (TextView)findViewById(R.id.tvCityName);
        mTVCountry = (TextView)findViewById(R.id.tvCountry);
        mTVWeGen = (TextView)findViewById(R.id.tvWeGen);
        mTVWeDesc = (TextView)findViewById(R.id.tvWeDesc);
        mTVTemper = (TextView)findViewById(R.id.tvTemper);
        mTVWePressure = (TextView)findViewById(R.id.tvPressure);
        mTVWeHumidity = (TextView)findViewById(R.id.tvHumidity);
        mTVWeWindSpeed = (TextView)findViewById(R.id.tvWeWindSpeed);

        updateCWDataUI();
    }


    protected void updateCWDataUI() {
        Log.i(LOG_TAG, "START: updateCWDataUI()...");

        if (mCWDataItem == null) {
            // clear weather data
            mTVCityName.setText("");
            mTVCountry.setText("");
            mTVWeGen.setText("");
            mTVWeDesc.setText("");
            mTVTemper.setText("");
            mTVWePressure.setText("");
            mTVWeHumidity.setText("");
            mTVWeWindSpeed.setText("");
        } else {
            mTVCityName.setText(mCWDataItem.mName);
            mTVCountry.setText(mCWDataItem.mCountry);
            mTVWeGen.setText(mCWDataItem.mWeGen);
            mTVWeDesc.setText(mCWDataItem.mWeDesc);
            mTVTemper.setText(Double.toString(mCWDataItem.mWeTemp));
            mTVWePressure.setText(Integer.toString(mCWDataItem.mWePressure));
            mTVWeHumidity.setText(Integer.toString(mCWDataItem.mWeHumidity));
            mTVWeWindSpeed.setText(Double.toString(mCWDataItem.mWeWindSpeed));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "START: onStart()...");

        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "START: onStop()...");

        // Unbind from the service
        if (mBound) {
            mService.setCallbacks(null); // unregister
            unbindService(mConnection);
            mBound = false;
        }
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(LOG_TAG, "START: onServiceConnected()...");

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();

            Log.i(LOG_TAG, "onServiceConnected = " + mService);

            mBound = true;
            mService.setCallbacks(MainActivity.this); // register
            // resend request if previous one was not completed
            if (mIsReqInprogress) {
                Log.i(LOG_TAG, "Resend request for city = " + mSelectedCity);
                mBtnRequest.setEnabled(false);
                mService.requestCWData(mSelectedCity);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i(LOG_TAG, "START: onServiceDisconnected()...");

            mBound = false;
        }
    };


    /* Defined by ServiceCallbacks interface */
    @Override
    public void updateCWData(CWData gotRes) {
        Log.i(LOG_TAG, "START: updateCWData()..." + "   gotRes = " + gotRes);

        // Enable request button and refresh interface per received data from Service
        mIsReqInprogress = false;
        mBtnRequest.setEnabled(true);
        mCWDataItem = gotRes;
        updateCWDataUI();
        if (gotRes == null) {
            Toast.makeText(MainActivity.this, "Failed to receive weather data from service!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
