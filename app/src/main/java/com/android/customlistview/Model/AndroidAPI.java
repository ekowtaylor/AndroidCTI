package com.android.customlistview.Model;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AndroidAPI {
    private static final String TAG = "Android API";
    private final Context mContext;
    Map<String, Object> deviceInfoMap = new HashMap<String, Object>();
    Map<String, Object> cellInfoMap = new HashMap<String, Object>();
    Map<String, Object> cellSignalInfoMap = new HashMap<String, Object>();
    Map<String, Object> simInfoMap = new HashMap<String, Object>();
    Map<String, Object> operatorInfoMap = new HashMap<String, Object>();
    Map<String, Object> connectionInfoMap = new HashMap<String, Object>();
    private final TelephonyManager tm;


    public AndroidAPI(Context ctx) {
        mContext = ctx;
        tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
    }

    public String LoadText(int resourceId) {
        // The InputStream opens the resourceId and sends it to the buffer
        InputStream is = mContext.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            // While the BufferedReader readLine is not null
            while ((readLine = br.readLine()) != null) {
                Log.d("TEXT", readLine);
            }

            // Close the InputStream and BufferedReader
            is.close();
            br.close();

            return br.toString();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setSimInfo() {
        SimInfo si = new SimInfo(mContext,tm);
        simInfoMap = si.setSimInfo();
    }

    public void setDeviceInfo() {
        DeviceInfo di = new DeviceInfo(mContext,tm);
        deviceInfoMap = di.setDeviceInfo();
    }

    public void setCellInfo() {
        CellInfo ci = new CellInfo(mContext,tm);
        cellInfoMap = ci.setCellInfo();
    }

    public void setCellSignalInfo() {
        CellSignalInfo csi = new CellSignalInfo(mContext,tm);
        cellSignalInfoMap = csi.setCellSignalInfo();
    }

    public void setConnectionInfo() {
        ConnectionInfo cxi = new ConnectionInfo(mContext,tm);
        connectionInfoMap = cxi.setConnectionInfo();
    }

    /* GETTERS */
    /* DEVICE INFO */
    //Device manufacturer
    public Model get_device_manufacturer() {
        return new Model("", "", "cell_tower_info", "manufacturer", String.valueOf(deviceInfoMap.get("manufacturer")));
    }

    //os
    public Model get_os() {
        return new Model("", "", "cell_tower_info", "os", String.valueOf(deviceInfoMap.get("os")));
    }

    //Device OS version
    public Model get_os_version() {
        return new Model("", "", "cell_tower_info", "os_version", String.valueOf(deviceInfoMap.get("os_version")));
    }

    //device type
    public Model get_device_type() {
        /*String config_text = LoadText(R.raw.config);
        Yaml yaml = new Yaml();
        yaml.load(config_text);
        System.out.println(yaml.toString());*/

        return new Model("", "", "cell_tower_info", "device_type", String.valueOf(deviceInfoMap.get("device_type")));
    }



    //network_type
    public Model get_network_type() {
        return new Model("", "", "cell_tower_info", "network_type", String.valueOf(connectionInfoMap.get("network_type")));
    }

    //phone type
    public Model get_phone_type() {
        int phoneType = tm.getPhoneType();
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

        return new Model("", "", "cell_tower_info", "phone_type", phone_type);
    }

    //sim country iso
    public Model get_sim_country_iso() {
        String cn = tm.getSimCountryIso();
        return new Model("", "", "cell_tower_info", "sim_country_iso", cn);
    }

    //sim operator mcc mnc
    public Model get_sim_operator_mcc_mnc() {
        String mm = tm.getSimOperator();
        return new Model("", "", "cell_tower_info", "sim_operator_mcc_mnc", mm);
    }

    //sim operator name
    public Model get_sim_operator_name() {
        String name = tm.getSimOperatorName();
        return new Model("", "", "cell_tower_info", "sim_operator_name", name);
    }



    //has_icc_card
    public Model get_has_icc_card() {
        int ss = tm.getSimState();
        boolean ret = (ss != 0);
        return new Model("", "", "cell_tower_info", "has_icc_card", String.valueOf(ret));
    }


    // lte_rsrq
    public Model get_lte_rsrq(){
        return new Model("", "", "cell_tower_info", "lte_rsrq", String.valueOf(cellSignalInfoMap.get("lte_rsrq")));
    }

    // lte_rscp
    public Model get_lte_rscp(){
        return new Model("", "", "cell_tower_info", "lte_rscp", String.valueOf(cellSignalInfoMap.get("lte_rscp")));
    }



}
