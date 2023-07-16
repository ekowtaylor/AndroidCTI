package com.android.customlistview.Model;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    Map<String, Object> deviceInfoMap = new HashMap<String, Object>();


    public DeviceInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;

    }

    public Map setDeviceInfo() {
        // Set default values first
        String[] columns = {
                "manufacturer",
                "device_type", // actually model
                "os",
                "os_version",
                "appid",
                "appver",
                "hardware_address"

        };

        // For loop for iterating over the List
        for (String column : columns) {
            deviceInfoMap.put(column, null);
        }

        deviceInfoMap.put("device_type", Build.MODEL);

        deviceInfoMap.put("os", "Android"); // Hard coded, obviously

        deviceInfoMap.put("os_version", Build.VERSION.RELEASE);  //Build.VERSION.SDK_INT  -- API level

        deviceInfoMap.put("manufacturer", Build.MANUFACTURER);

        deviceInfoMap.put("hardware_address", Build.MANUFACTURER);

        return deviceInfoMap;
    }


}
