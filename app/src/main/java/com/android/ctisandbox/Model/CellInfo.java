package com.android.ctisandbox.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    Map<String, Object> cellInfoMap = new HashMap<String, Object>();
    String TAG = "CellInfo";


    public CellInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;

    }

    public Map setCellInfo() {
        try {
            // Set default values first
            String[] columns = {
                    "gsm_arfcn",
                    "gsm_bsic",
                    "gsm_cid",
                    "gsm_lac",
                    "gsm_mcc",
                    "gsm_mnc",
                    "gsm_psc",
                    "wcdma_cid",
                    "wcdma_lac",
                    "wcdma_mcc",
                    "wcdma_mnc",
                    "wcdma_psc",
                    "wcdma_uarfcn",
                    "lte_bandwidth",
                    "lte_ci",
                    "lte_earfcn",
                    "lte_mcc",
                    "lte_mnc",
                    "lte_pci",
                    "lte_tac",
                    "nr_mcc",
                    "nr_mnc",
                    "nr_nci",
                    "nr_nrarfcn",
                    "nr_pci",
                    "nr_tac",
                    "cdma_base_station_id",
                    "cdma_base_station_latitude",
                    "cdma_base_station_longitude",
                    "cdma_network_id",
                    "cdma_system_id",
                    "operator_alpha_long",
                    "operator_alpha_short"
            };

            // For loop for iterating over the List
            for (String column : columns) {
                cellInfoMap.put(column, null);
            }

            // Start setting CellInfo

            List<android.telephony.CellInfo> cellInfoList = null;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //return;
            } else {
                cellInfoList = telMgr.getAllCellInfo();
                List<CellSignalStrength> cellSignalInfo;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    for (android.telephony.CellInfo cellInfo : cellInfoList) {
                        if (cellInfo instanceof CellInfoGsm) {
                            //2G
                            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                            CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();

                            cellInfoMap.put("gsm_arfcn", cellIdentityGsm.getArfcn());
                            cellInfoMap.put("gsm_bsic", cellIdentityGsm.getBsic());
                            cellInfoMap.put("gsm_cid", cellIdentityGsm.getCid());
                            cellInfoMap.put("gsm_lac", cellIdentityGsm.getLac());
                            cellInfoMap.put("gsm_mcc", cellIdentityGsm.getMccString());
                            cellInfoMap.put("gsm_mnc", cellIdentityGsm.getMncString());
                            cellInfoMap.put("gsm_psc", cellIdentityGsm.getPsc());

                            if (cellInfoGsm.getCellSignalStrength() != null) {
                                /*
                                baseStation.setAsuLevel(cellInfoGsm.getCellSignalStrength().getAsuLevel());
                                baseStation.setSignalLevel(cellInfoGsm.getCellSignalStrength().getLevel());
                                baseStation.setDbm(cellInfoGsm.getCellSignalStrength().getDbm());
                                */
                            }
                        } else if (cellInfo instanceof CellInfoCdma) {
                            //CDMA
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                            CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();

                            cellInfoMap.put("cdma_base_station_id", cellIdentityCdma.getBasestationId());
                            cellInfoMap.put("cdma_base_station_latitude", cellIdentityCdma.getLatitude());
                            cellInfoMap.put("cdma_base_station_longitude", cellIdentityCdma.getLongitude());
                            cellInfoMap.put("cdma_network_id", cellIdentityCdma.getNetworkId());
                            cellInfoMap.put("cdma_system_id", cellIdentityCdma.getSystemId());

                            if (cellInfoCdma.getCellSignalStrength() != null) {
                                /*
                                baseStation.setAsuLevel(cellInfoGsm.getCellSignalStrength().getAsuLevel());
                                baseStation.setSignalLevel(cellInfoGsm.getCellSignalStrength().getLevel());
                                baseStation.setDbm(cellInfoGsm.getCellSignalStrength().getDbm());
                                */
                            }
                        } else if (cellInfo instanceof CellInfoWcdma) {
                            //联通3G
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                            CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();

                            cellInfoMap.put("wcdma_cid", cellIdentityWcdma.getCid());
                            cellInfoMap.put("wcdma_lac", cellIdentityWcdma.getLac());
                            cellInfoMap.put("wcdma_mcc", cellIdentityWcdma.getMccString());
                            cellInfoMap.put("wcdma_mnc", cellIdentityWcdma.getMncString());
                            cellInfoMap.put("wcdma_psc", cellIdentityWcdma.getPsc());
                            cellInfoMap.put("wcdma_uarfcn", cellIdentityWcdma.getUarfcn());

                            if (cellInfoWcdma.getCellSignalStrength() != null) {
                                /*
                                baseStation.setAsuLevel(cellInfoWcdma.getCellSignalStrength().getAsuLevel()); //Get the signal level as an asu value between 0..31, 99 is unknown Asu is calculated based on 3GPP RSRP.
                                baseStation.setSignalLevel(cellInfoWcdma.getCellSignalStrength().getLevel()); //Get signal level as an int from 0..4
                                baseStation.setDbm(cellInfoWcdma.getCellSignalStrength().getDbm()); //Get the signal strength as dBm
                                */
                            }
                        } else if (cellInfo instanceof CellInfoLte) {
                            //4G
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();

                            cellInfoMap.put("lte_bandwidth", cellIdentityLte.getBandwidth());
                            cellInfoMap.put("lte_ci", cellIdentityLte.getCi());
                            cellInfoMap.put("lte_tac", cellIdentityLte.getTac());
                            cellInfoMap.put("lte_mcc", cellIdentityLte.getMccString());
                            cellInfoMap.put("lte_mnc", cellIdentityLte.getMncString());
                            cellInfoMap.put("lte_pci", cellIdentityLte.getPci());
                            cellInfoMap.put("lte_earfcn", cellIdentityLte.getEarfcn());

                            if (cellInfoLte.getCellSignalStrength() != null) {
                                /*cellInfoMap.put("lte_rsrq", cellInfoLte.getCellSignalStrength().getRsrq());
                                cellInfoMap.put("lte_rssi", cellInfoLte.getCellSignalStrength().getRssi());
                                cellInfoMap.put("lte_rssnr", cellInfoLte.getCellSignalStrength().getRssnr());*/
                            }
                        }  else if (cellInfo instanceof CellInfoNr) {
                            //NR
                            CellInfoNr cellInfoNr = (CellInfoNr) cellInfo;
                            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();

                            cellInfoMap.put("nr_mcc", cellIdentityNr.getMccString());
                            cellInfoMap.put("nr_mnc", cellIdentityNr.getMncString());
                            cellInfoMap.put("nr_nci", cellIdentityNr.getNci());
                            cellInfoMap.put("nr_pci", cellIdentityNr.getPci());
                            cellInfoMap.put("nr_tac", cellIdentityNr.getTac());
                            cellInfoMap.put("nr_nrarfcn", cellIdentityNr.getNrarfcn());

                            if (cellInfoNr.getCellSignalStrength() != null) {
                                /*
                                baseStation.setAsuLevel(cellInfoGsm.getCellSignalStrength().getAsuLevel());
                                baseStation.setSignalLevel(cellInfoGsm.getCellSignalStrength().getLevel());
                                baseStation.setDbm(cellInfoGsm.getCellSignalStrength().getDbm());
                                */
                            }

                        } else {
                            //电信2/3G
                            Log.e(TAG, "CDMA CellInfo................................................");
                        }


                        // operator_alpha_long or operator_alpha_short
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            CharSequence alphalong = cellInfo.getCellIdentity().getOperatorAlphaLong();
                            if (alphalong != null) {
                                cellInfoMap.put("operator_alpha_long", alphalong);
                            }

                            CharSequence alphashort = cellInfo.getCellIdentity().getOperatorAlphaShort();
                            if (alphashort != null) {
                                cellInfoMap.put("operator_alpha_short", alphashort);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return cellInfoMap;
    }


}
