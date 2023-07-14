package com.android.customlistview.Model;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.customlistview.Model.Model;
import com.android.customlistview.R;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidAPI {
    private static final String TAG = "Android API";
    private final Context mContext;
    Map<String, Object> cellInfoMap = new HashMap<String, Object>();
    Map<String, Object> cellSignalInfoMap = new HashMap<String, Object>();
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

    public void setCellInfo() {
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
                    "nr_tac"
            };

            // For loop for iterating over the List
            for (String column : columns) {
                cellInfoMap.put(column, null);
            }

            // Start setting CellInfo

            List<CellInfo> cellInfoList = null;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            } else {
                cellInfoList = tm.getAllCellInfo();
                List<CellSignalStrength> cellSignalInfo;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    for (CellInfo cellInfo : cellInfoList) {
                        if (cellInfo instanceof CellInfoWcdma) {
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
                        } else if (cellInfo instanceof CellInfoGsm) {
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
                        } else if (cellInfo instanceof CellInfoNr) {
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
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setCellSignalInfo() {
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
                    "nr_ss_sinr"
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
                    } else {
                        System.out.println("Other");
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //Device manufacturer
    public Model getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        return new Model("", "", "cell_tower_info", "manufacturer", manufacturer);
    }

    //os
    public Model get_os() {
        return new Model("", "", "cell_tower_info", "os", "Android");
    }

    //network_type
    public Model get_network_type() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        else {
            int nt = tm.getNetworkType();
            // get the network type string
            String mobile_network_type = "unknown";
            switch (nt) {
                case 0:
                    mobile_network_type = "TYPE_UNKNOWN";
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    mobile_network_type = "GPRS";
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    mobile_network_type = "EDGE";
                    break;
                case TelephonyManager.NETWORK_TYPE_GSM:
                    mobile_network_type = "GSM";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    mobile_network_type = "UMTS";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    mobile_network_type = "CDMA";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    mobile_network_type = "EVDO_0";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    mobile_network_type = "EVDO_A";
                    break;
                case 7:
                    mobile_network_type = "1xRTT";
                    break;
                case 8:
                    mobile_network_type = "HSDPA";
                    break;
                case 9:
                    mobile_network_type = "HSUPA";
                    break;
                case 10:
                    mobile_network_type = "HSPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    mobile_network_type = "LTE";
                    break;
                case TelephonyManager.NETWORK_TYPE_NR:
                    mobile_network_type = "NR";
                    break;
                    //Todo
                    //Add other use cases

            }

            return new Model("", "", "cell_tower_info", "network_type", mobile_network_type);
        }
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

    //device type
    public Model get_device_type() {
        String model = Build.MODEL;
        String config_text = LoadText(R.raw.config);
        Yaml yaml = new Yaml();
        yaml.load(config_text);

        //System.out.println(yaml.toString());

        return new Model("", "", "cell_tower_info", "device_type", model);
    }

    //Device OS version
    public Model getOsVersion() {
        //int os_v = Build.VERSION.SDK_INT; //API level
        String os_v = Build.VERSION.RELEASE; //OS version number
        System.out.println(os_v);
        return new Model("", "", "cell_tower_info", "os_version", String.valueOf(os_v));
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
