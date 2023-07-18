package com.android.ctisandbox.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

public class DeviceLocationInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    Map<String, Object> deviceLocationInfoMap = new HashMap<String, Object>();

    public Criteria criteria;
    public String bestProvider;


    public DeviceLocationInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;

    }

    public Map setDeviceLocationInfo() {
        // Set default values first
        String[] columns = {
                "country",
                "device_lat",
                "device_long",
                "device_altitude",
                "device_acc",
                "device_altitude_acc"
        };

        // For loop for iterating over the List
        for (String column : columns) {
            deviceLocationInfoMap.put(column, null);
        }

        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
        }

        //criteria = new Criteria();
        //bestProvider = String.valueOf(lm.getBestProvider(criteria, true)).toString();

        //You can still do this if you like, you might get lucky:
        //Location location = lm.getLastKnownLocation(bestProvider);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.e("TAG", "GPS is on");

            deviceLocationInfoMap.put("device_lat", location.getLatitude());

            deviceLocationInfoMap.put("device_long", location.getLongitude());

            deviceLocationInfoMap.put("device_altitude", location.getAltitude());

            deviceLocationInfoMap.put("device_acc", location.getAccuracy());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceLocationInfoMap.put("device_altitude_acc", location.getVerticalAccuracyMeters());
            }


        }
        else{
            //This is what you need:
            //lm.requestLocationUpdates(bestProvider, 1000, 0, this);
        }
        return deviceLocationInfoMap;
    }
}
