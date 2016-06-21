package com.example.guest.citywheather;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The class receives weather data for indicated city in JSON format
 * and parses the data into special structure to be used by other classes
 */

public class CWFetcher {

    public static final String TAG = "CWFetcher";

    //    weather
    private static final String ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String PARAM_API_KEY = "appid";
    private static final String API_KEY = "a18c87f9856cddffb1f2c079f18eb202";
    private static final String PARAM_CITY = "q";
    private static final String CITY = "Moscow";





    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    public String fetchItems() {
        String items = "";

        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter(PARAM_CITY, CITY)
                    .appendQueryParameter(PARAM_API_KEY, API_KEY)
                    .build().toString();

            Log.i(TAG, "url: " + url);

            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);

            items = xmlString;

            CWData CWResult = new CWData();

            try {
                JSONObject jsonObject = new JSONObject(items);
                CWResult.mCod = jsonObject.getInt("cod");
                CWResult.mName = jsonObject.getString("name");
//                CWResult.mDescription = jsonObject.getString("weather.description");
//                CWResult.mTemp = jsonObject.getDouble("temp");
                JSONObject joTemp = jsonObject.getJSONObject("main");
                CWResult.mTemp = joTemp.getDouble("temp");

                Log.e(TAG, "CWResult: " + CWResult.mCod + CWResult.mName + CWResult.mTemp + jsonObject.has("main") + jsonObject.has("main.temp"));

            } catch (JSONException e) {
                Log.e(TAG, "Failed to json: ", e);

            }


/*
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items, parser);
*/

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
//        } catch (XmlPullParserException xppe) {
//            Log.e(TAG, "Failed to parse items", xppe);
        }

        return items;
    }



/*
    public ArrayList<GalleryItem> fetchItems() {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("key", API_KEY)
                    .appendQueryParameter(PARAM_FORMAT, EXTRA_FORMAT_XML)
                    .appendQueryParameter(PARAM_PAGE_SIZE, EXTRA_PAGE_SIZE)
                    .appendQueryParameter(PARAM_PAGE_NUM, EXTRA_PAGE_NUM)
                    .appendQueryParameter("method", METHOD_GET_READ)
                    .build().toString();

            Log.i(TAG, "url: " + url);


            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items, parser);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Failed to parse items", xppe);
        }

        return items;
    }
*/

/*
    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
//            Log.e(TAG, "eventType parser.getName() ->" + eventType + ", " + parser.getName());
//            Log.e(TAG, "parser.getText() ->" + parser.getText());
            GalleryItem item = new GalleryItem();
            item.setCaption(parser.getText());
            item.setEngCaption(parser.getText());
            items.add(item);

            eventType = parser.next();
        }
    }
*/

}
