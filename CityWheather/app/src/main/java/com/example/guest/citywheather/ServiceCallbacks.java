package com.example.guest.citywheather;

/**
 * Created by guest on 22.06.16.
 */
public interface ServiceCallbacks {

    // callback function to update interface per received CW data from service
    void updateCWData(CWData resultCW);
}
