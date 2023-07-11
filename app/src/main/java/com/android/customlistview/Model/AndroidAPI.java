package com.android.customlistview.Model;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.customlistview.Model.Model;
import com.android.customlistview.R;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class AndroidAPI {
    private Context mContext;
    private int rsrp, rsrq, snr;

    public AndroidAPI(Context ctx){
        mContext = ctx;
    }

    //Device manufacturer
    public Model getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        return new Model("", "", "cell_tower_info", "manufacturer");
    }

    //Device model
    public Model getDeviceModel() {
        String manufacturer = Build.MODEL;
        String config_text = LoadText(R.raw.config);
        Yaml yaml = new Yaml();
        yaml.load(config_text);

        System.out.println(yaml.toString());

        return new Model("", "", "cell_tower_info", "manufacturer");
    }

    // lte_rsrq
    public Model get_lte_rsrq(){
        List<CellSignalStrength> cellInfoList;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellInfoList = tm.getSignalStrength().getCellSignalStrengths();

                for (CellSignalStrength cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellSignalStrengthLte) {
                        rsrp = ((CellSignalStrengthLte) cellInfo).getRsrp();
                        rsrq = ((CellSignalStrengthLte) cellInfo).getRsrq();
                        snr = ((CellSignalStrengthLte) cellInfo).getRssnr();
                        Log.d("get_lte_rsrq", String.valueOf(rsrq));
                    }
                }
            }
        }

        return new Model("", "", "cell_tower_info", String.valueOf(rsrq));
    }

    private String LoadText(int resourceId) {
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
}
