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

    private final String LOG_TAG = "myLogs";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    MyTask mt;

/*
    public LocalService() {
    }
*/

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        return mBinder;
    }

    /** method for clients */

    public void requestCWData(String cn) {

        mt = new MyTask();
        mt.execute(cn);

        return;
    }

    public CWData getCWData() {

        CWData result = null;

        if (mt == null) return result;

        try {
            Log.d(LOG_TAG, "Try to get result");
//            result = mt.get(10, TimeUnit.SECONDS);
            result = mt.get();
            Log.d(LOG_TAG, "get returns " + result);
//            Toast.makeText(this, "get returns " + result, Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }


    public int getRandomNumber() {
//        return mGenerator.nextInt(100);

        mt = new MyTask();
        mt.execute("");

        int result = -1;

/*

//        if (mt == null) return;
//        int result = -1;

        try {
            Log.d(LOG_TAG, "Try to get result");
            result = mt.get(10, TimeUnit.SECONDS);
            Log.d(LOG_TAG, "get returns " + result);
//            Toast.makeText(this, "get returns " + result, Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.d(LOG_TAG, "get timeout, result = " + result);
            e.printStackTrace();
        }
*/
        return result;

    }


    class MyTask extends AsyncTask<String, Void, CWData> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            tvInfo.setText("Begin");
            Log.d(LOG_TAG, "Begin");
        }


        @Override
        protected CWData doInBackground(String... params) {

            CWData res = new CWFetcher().fetchItems(params[0]);
            Log.d(LOG_TAG, "Real result = " + res);
/*
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            return res;
        }

        @Override
        protected void onPostExecute(CWData result) {
            super.onPostExecute(result);
//            tvInfo.setText("End. Result = " + result);
            Log.d(LOG_TAG, "End. Result = " + result);

        }
    }

}



