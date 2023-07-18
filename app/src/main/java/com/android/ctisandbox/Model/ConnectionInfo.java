package com.android.ctisandbox.Model;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthNr;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import androidx.core.app.ActivityCompat;

import com.android.ctisandbox.Utils.Connectivity;
import com.android.ctisandbox.Utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionInfo {
    private final Context mContext;
    private final TelephonyManager telMgr;
    private final ConnectivityManager conMgr;
    Map<String, Object> connectionInfoMap = new HashMap<String, Object>();
    String TAG = "CellInfo";


    public ConnectionInfo(Context ctx, TelephonyManager tm) {
        mContext = ctx;
        telMgr = tm;
        conMgr =  (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public Map setConnectionInfo() {
        try {
            // Set default values first
            String[] columns = {
                    "carrier",
                    "carrier_id",
                    "asn",
                    "hostname",
                    "client_public_address",
                    "ip_version",
                    "server_cluster",
                    "network_type",
                    "connection_type",
                    "connection_subtype",
                    "override_network_type",
                    "is_nr_nsa_signal_strength",
                    "tower_changed",
                    "registered_plmn",
                    "network_country_iso",
                    "network_operator_mcc_mnc",
                    "network_operator_name",
                    "is_network_roaming",
                    "network_type_info",
                    "network_generation",
                    "network_params",
                    "network_provider_detector_results",
                    "override_network_type",
                    "network_provider_input_features",
                    "is_data_roaming_enabled",
                    "unmetered"
            };

            // For loop for iterating over the List
            for (String column : columns) {
                connectionInfoMap.put(column, null);
            }

            // Start setting ConnectionInfo
            connectionInfoMap.put("carrier", "not used");

            connectionInfoMap.put("carrier_id", "internal");

            connectionInfoMap.put("asn", "internal");

            connectionInfoMap.put("hostname", "internal");

            // Method 1 - from NetworkInterFace
            String ipv4 = Utils.getIPAddress(true);
            String ipv6 = Utils.getIPAddress(false);

            // Method 2 - from WifiManager (Deprecated)
            WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            // Method 3 - from ConnectivityManager
            //Utils.getDefaultIp(mContext);

            connectionInfoMap.put("client_public_address", ipv4);

            // Depends on outcome of client_public_address
            connectionInfoMap.put("ip_version", "v4"); //v4 or v6

            connectionInfoMap.put("server_cluster", "internal");

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return null;
            } else {
                int nt = telMgr.getNetworkType();
                //int nt = telMgr.getDataNetworkType();

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

                connectionInfoMap.put("network_type", mobile_network_type);

                //network_type_info
                connectionInfoMap.put("network_type_info", mobile_network_type);
            }

            // Method 1 - ConnectionManager
            NetworkInfo info = Connectivity.getNetworkInfo(mContext);
            String connection_type = "";
            int type = info.getType();
            switch (type) {
                case ConnectivityManager.TYPE_BLUETOOTH:
                    connection_type = "bluetooth";
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    connection_type = "mobile";
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    connection_type = "wifi";
                    //Todo
                    //Add other use cases
            }

            // Method 2 - NetworkCapabilities
            connectionInfoMap.put("connection_type", connection_type);

            // Method 1 - ConnectionManager
            String connection_subtype = "";
            int subtype = info.getSubtype();
            switch (type) {
                case ConnectivityManager.TYPE_BLUETOOTH:
                    connection_type = "bluetooth";
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    connection_type = "mobile";
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    connection_type = "wifi";
                    //Todo
                    //Add other use cases
            }

            // Method 2 - TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                int subtype2 = telMgr.getDataNetworkType();
                String connection_subtype2 = "";
                switch (type) {
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                        connection_subtype2 = "HSPA";
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        connection_subtype2 = "IDEN";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        connection_subtype2 = "LTE";
                }
            }
            connectionInfoMap.put("connection_subtype", connection_subtype);


            //override_network_type
            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                TelephonyDisplayInfo tdi = new TelephonyDisplayInfo();
            }
            tdi..getOverrideNetworkType()

            if (android.os.Build.VERSION.SDK_INT >= 31 && telMgr.getDataNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                // Delay update of the network type to check whether this is actually 5G-NSA.
                Api31.disambiguate4gAnd5gNsa(context, /* instance= */ //NetworkTypeObserver.this);
            /*} else {
                updateNetworkType(networkType);
            }*/
            //connectionInfoMap.put("override_network_type", connection_subtype);

            TelephonyManager tm;
            List<CellSignalStrength> cellSignalInfo;

            tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellSignalInfo = tm.getSignalStrength().getCellSignalStrengths();

                for (CellSignalStrength cellInfo : cellSignalInfo) {
                    if (cellInfo instanceof CellSignalStrengthNr) {
                        connectionInfoMap.put("is_nr_nsa_signal_strength", "true");
                    } else {
                        connectionInfoMap.put("is_nr_nsa_signal_strength", "false");
                    }
                }
            }

            //network_country_iso
            connectionInfoMap.put("network_country_iso", telMgr.getNetworkCountryIso());

            //network_operator_mcc_mnc
            connectionInfoMap.put("network_operator_mcc_mnc", telMgr.getNetworkOperator());

            //network_operator_name
            connectionInfoMap.put("network_operator_name", telMgr.getNetworkOperatorName());

            //is_network_roaming
            connectionInfoMap.put("is_network_roaming", telMgr.isNetworkRoaming());

            //network_generation
            connectionInfoMap.put("network_generation", getNetworkGeneration());

            //is_data_roaming_enabled
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                connectionInfoMap.put("is_data_roaming_enabled", telMgr.isDataRoamingEnabled());
            }

            //unmetered
            final Network network;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                network = conMgr.getActiveNetwork();
                NetworkCapabilities capabilities = conMgr
                        .getNetworkCapabilities(network);
                connectionInfoMap.put("unmetered", capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return connectionInfoMap;
    }


    public String getNetworkGeneration() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
        }
        int networkType = telMgr.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "Unknown";
        }
    }

}
