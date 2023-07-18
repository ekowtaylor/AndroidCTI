package com.android.ctisandbox.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
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
                "hardware_address",
                "is_multi_sim_supported",
                "manufacturer_code",
                "phone_type",
                "device_tac",
                "service_state",
                "country"

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


        // is_multi_sim_supported
        // Method 1
        //For API >=17:
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        // Get information about all radio modules on device board
        // and check what you need by calling #getCellIdentity.

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
        }
        else {
            final List<CellInfo> allCellInfo = manager.getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                    //TODO Use cellIdentity to check MCC/MNC code, for instance.
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (cellInfo instanceof CellInfoWcdma) {
                        CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                    }
                }
            }
        }


        // Method 2
        //For API >=22:
        SubscriptionManager subscriptionManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = SubscriptionManager.from(mContext);
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        }
        assert activeSubscriptionInfoList != null;
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                final CharSequence carrierName = subscriptionInfo.getCarrierName();
                final CharSequence displayName = subscriptionInfo.getDisplayName();
                final int mcc = subscriptionInfo.getMcc();
                final int mnc = subscriptionInfo.getMnc();
                final String subscriptionInfoNumber = subscriptionInfo.getNumber();
            }

        }

        // Method 3
        //For API >=23. To just check if phone is dual/triple/many sim:
        boolean d_sim = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (telMgr.getActiveModemCount() == 2) { //getPhoneCount()
                // Dual sim
                d_sim = true;
            }
        }

        // Method 4 - Reflection
        //TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(mContext);

        /*String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();*/

        deviceInfoMap.put("is_multi_sim_supported", d_sim);

        // manufacturer_code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                deviceInfoMap.put("manufacturer_code", telMgr.getMeid());
            }
            catch (Exception e) {
                // Todo
                // log some error to serial
            }
        }


        // phone_type
        int phoneType = telMgr.getPhoneType();
        String phone_type = "unknown";
        switch (phoneType)
        {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                phone_type = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                phone_type = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                phone_type = "NONE";
                break;
        }
        deviceInfoMap.put("phone_type", phone_type);

        // device_tac
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceInfoMap.put("device_tac", telMgr.getTypeAllocationCode());
        }

        // service_state
        ServiceState serviceState = new ServiceState();
        System.out.println("Service State:" + serviceState);
        deviceInfoMap.put("service_state", serviceState);

        // country
        // Method 1
        String country = telMgr.getSimCountryIso();

        // Method 2
        //String country = telMgr.getNetworkCountryIso()

        deviceInfoMap.put("country", country);

        return deviceInfoMap;
    }


}
