package com.example.guest.citywheather;

/**
 * Class to store City weather data
 */

public class CWData {
    int mCod = 0;                       // "cod":200
    String mName = null;                // "name":"Moscow"
    String mCountry = null;             // "sys"."country":"RU"
    String mWeGen = null;               // "weather":[0]."main":"Clouds"
    String mWeDesc = null;              // "weather":[0]."description":"few clouds"
    double mWeTemp = 0;                 // "main"."temp":301.12         (Kelvin)
    int mWePressure = 0;                // "main"."pressure":1023       (kPa)
    int mWeHumidity = 0;                // "main"."humidity":48         (%)
    double mWeWindSpeed = 0;            // "wind"."speed":6.68          (m/s)
}



            /*
            items:
            {"coord":{"lon":37.62,"lat":55.75},
            "weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02d"}],
            "base":"cmc stations",
            "main":{"temp":301.12,"pressure":1023,"humidity":48,"temp_min":300.37,"temp_max":302.04},
            "wind":{"speed":6.68,"deg":180,"gust":6.68},
            "clouds":{"all":20},"dt":1466509585,
            "sys":{"type":3,"id":37754,"message":0.0033,"country":"RU","sunrise":1466469886,"sunset":1466533090},
            "id":524901,"name":"Moscow","cod":200}
             */
