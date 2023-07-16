package com.android.customlistview.Model;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
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
                "has_icc_card"
        };

        // For loop for iterating over the List
        for (String column : columns) {
            simInfoMap.put(column, null);
        }

        simInfoMap.put("sim_country_iso", telMgr.getSimCountryIso());

        simInfoMap.put("sim_operator_mcc_mnc", telMgr.getSimOperator());

        simInfoMap.put("sim_operator_name", telMgr.getSimOperatorName());

        simInfoMap.put("has_icc_card", telMgr.hasIccCard());

        return simInfoMap;
    }


}
