package com.android.ctisandbox.Model;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;

public class SimInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    Map<String, Object> simInfoMap = new HashMap<String, Object>();


    public SimInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;

    }

    public Map setSimInfo() {
        // Set default values first
        String[] columns = {
                "sim_country_iso",
                "sim_operator_mcc_mnc",
                "sim_operator_name",
                "has_icc_card",
                "sim_carrier_id",
                "sim_carrier_id_name",
                "carrier_id_from_sim_mcc_mnc",
                "sim_specific_carrier_id",
                "sim_specific_carrier_id_name"
        };

        // For loop for iterating over the List
        for (String column : columns) {
            simInfoMap.put(column, null);
        }

        simInfoMap.put("sim_country_iso", telMgr.getSimCountryIso());

        simInfoMap.put("sim_operator_mcc_mnc", telMgr.getSimOperator());

        simInfoMap.put("sim_operator_name", telMgr.getSimOperatorName());

        simInfoMap.put("has_icc_card", telMgr.hasIccCard());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            simInfoMap.put("sim_carrier_id", telMgr.getSimCarrierId());
            simInfoMap.put("sim_carrier_id_name", telMgr.getSimCarrierIdName());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            simInfoMap.put("sim_specific_carrier_id", telMgr.getCarrierIdFromSimMccMnc());
            simInfoMap.put("sim_specific_carrier_id_name", telMgr.getSimSpecificCarrierIdName());
        }


        return simInfoMap;
    }


}
