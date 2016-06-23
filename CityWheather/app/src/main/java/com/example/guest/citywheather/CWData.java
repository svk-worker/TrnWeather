package com.example.guest.citywheather;

import java.io.Serializable;

/**
 * Class to store City weather data
 */

public class CWData implements Serializable {
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
