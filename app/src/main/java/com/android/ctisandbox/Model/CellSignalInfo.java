package com.android.ctisandbox.Model;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.os.Build;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellSignalInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    Map<String, Object> cellSignalInfoMap = new HashMap<String, Object>();
    String TAG = "CellInfo";


    public CellSignalInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;

    }

    public Map setCellSignalInfo() {
        try {
            // Set default values first
            String[] columns = {
                    "lte_rsrq",
                    "lte_rssi",
                    "lte_rssnr",
                    "nr_csi_rsrp",
                    "nr_csi_rsrq",
                    "nr_csi_sinr",
                    "nr_ss_rsrp",
                    "nr_ss_rsrq",
                    "nr_ss_sinr",
                    "signal_level",
                    "signal_asu_level",
                    "signal_dbm",
                    "is_nr_nsa_signal_strength"
            };

            // For loop for iterating over the List
            for (String column : columns) {
                cellSignalInfoMap.put(column, null);
            }

            // Start setting CellInfo
            TelephonyManager tm;
            List<CellSignalStrength> cellSignalInfo;

            tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellSignalInfo = tm.getSignalStrength().getCellSignalStrengths();

                for (CellSignalStrength cellInfo : cellSignalInfo) {
                    cellSignalInfoMap.put("signal_level", cellInfo.getLevel());
                    cellSignalInfoMap.put("signal_asu_level", cellInfo.getAsuLevel());
                    cellSignalInfoMap.put("signal_dbm", cellInfo.getDbm());

                    if (cellInfo instanceof CellSignalStrengthLte) {
                        System.out.println("CellSignalStrengthLte");
                        cellSignalInfoMap.put("lte_rsrp", ((CellSignalStrengthLte) cellInfo).getRsrp());
                        cellSignalInfoMap.put("lte_rsrq", ((CellSignalStrengthLte) cellInfo).getRsrq());
                        cellSignalInfoMap.put("lte_snr", ((CellSignalStrengthLte) cellInfo).getRssnr());
                    } else if (cellInfo instanceof CellSignalStrengthWcdma) {
                        System.out.println("CellSignalStrengthWcdma");
                    } else if (cellInfo instanceof CellSignalStrengthGsm) {
                        System.out.println("CellSignalStrengthGsm");
                    } else if (cellInfo instanceof CellSignalStrengthCdma) {
                        System.out.println("CellSignalStrengthCdma");
                    } else if (cellInfo instanceof CellSignalStrengthNr) {
                        System.out.println("CellSignalStrengthNr");

                        cellSignalInfoMap.put("nr_csi_rsrp", ((CellSignalStrengthNr) cellInfo).getCsiRsrp());
                        cellSignalInfoMap.put("nr_csi_rsrq", ((CellSignalStrengthNr) cellInfo).getCsiRsrq());
                        cellSignalInfoMap.put("nr_csi_sinr", ((CellSignalStrengthNr) cellInfo).getCsiSinr());
                        cellSignalInfoMap.put("nr_ss_rsrp", ((CellSignalStrengthNr) cellInfo).getSsRsrp());
                        cellSignalInfoMap.put("nr_ss_rsrq", ((CellSignalStrengthNr) cellInfo).getSsRsrq());
                        cellSignalInfoMap.put("nr_ss_sinr", ((CellSignalStrengthNr) cellInfo).getSsSinr());

                        cellSignalInfoMap.put("is_nr_nsa_signal_strength", true);
                    } else {
                        System.out.println("Other");
                    }


                }
            }




        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return cellSignalInfoMap;
    }


}
