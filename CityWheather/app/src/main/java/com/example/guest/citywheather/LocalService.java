package com.example.guest.citywheather;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LocalService extends Service {

    private final String LOG_TAG = "LocalService";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;

    // task to get CW data in background mode
    MyTask mt;


    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "START: onCreate()..." );

        return;
    }


    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "START: onDestroy()..." );

        return;
    }


    public void setCallbacks(ServiceCallbacks callbacks) {
        Log.i(LOG_TAG, "START: setCallbacks()..." );

        serviceCallbacks = callbacks;

        Log.i(LOG_TAG, "serviceCallbacks = " + callbacks);
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            Log.i(LOG_TAG, "START: getService()..." );

            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "START: onBind()..." );

        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        return mBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(LOG_TAG, "START: onUnbind()..." );

        // to cancel async task if disconnect happens
        if (mt != null) {
            mt.cancel(true);
        }

        return false;
    }


    /** methods for clients */

    public void requestCWData(String cn) {
        Log.i(LOG_TAG, "START: requestCWData()..." );

        mt = new MyTask();
        mt.execute(cn);
        return;
    }


    class MyTask extends AsyncTask<String, Void, CWData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i(LOG_TAG, "START: onPreExecute()..." );
        }

        @Override
        protected CWData doInBackground(String... params) {
            Log.i(LOG_TAG, "START: doInBackground()..." + "   Input param = " + params[0]);

            CWData res = new CWFetcher().fetchItems(params[0]);
/*
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            return res;
        }

        @Override
        protected void onPostExecute(CWData result) {
            super.onPostExecute(result);
            Log.i(LOG_TAG, "START: onPostExecute()..." + "   Final result (CW data) = " + result);

            if (serviceCallbacks != null) {
                serviceCallbacks.updateCWData(result);
            }

        }
    }

}



