package com.android.ctisandbox;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.ListView;

import com.android.ctisandbox.Adapter.Adapter;
import com.android.ctisandbox.Model.AndroidAPI;
import com.android.ctisandbox.Model.Model;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.permissionx.guolindev.PermissionX;

import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<Model> models = new ArrayList<>();
    Adapter adapter;

    //instantiate the AAPI
    AndroidAPI aapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);

        // instantiate AAPI
        aapi = new AndroidAPI(getApplicationContext());

        // check permissions and bind data
        checkPermissions();

    }


    void checkPermissions() {
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    scope.showRequestReasonDialog(deniedList, "CTI Sandbox needs the following permissions to continue", "Allow");
                })
                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Toast.makeText(MainActivity.this, "All permissions are granted", Toast.LENGTH_SHORT).show();

                        //Bind data
                        BindData();
                    } else {
                        Toast.makeText(MainActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void BindData() {
        System.out.println("Bind data");

        /* SETTERS */
        setCommonParams();

        /* GETTERS */
        /* DEVICE INFO */
        //Device manufacturer
        models.add(aapi.get_device_manufacturer());

        //Device model
        models.add(aapi.get_device_type());

        //OS
        models.add(aapi.get_os());

        //os_Version
        models.add(aapi.get_os_version());

        //country

        //carrier

        //network type
        models.add(aapi.get_network_type());

        //phone type
        models.add(aapi.get_phone_type());

        //sim country iso
        models.add(aapi.get_sim_country_iso());

        //sim operator mcc mnc
        models.add(aapi.get_sim_operator_mcc_mnc());

        //sim operator name
        models.add(aapi.get_sim_operator_name());

        //has icc card
        models.add(aapi.get_has_icc_card());


        //lte_rscp
        models.add(aapi.get_lte_rscp());

        //lte_rsrq
        models.add(aapi.get_lte_rsrq());


        // Set adaptor
        adapter = new Adapter(getApplicationContext(), models);
        lv.setAdapter(adapter);
    }

    void setCommonParams() {
        // set sim info
        aapi.setSimInfo();

        //set device info
        aapi.setDeviceInfo();

        //set device location info
        aapi.setDeviceLocationInfo();

        //set cellinfo
        aapi.setCellInfo();

        //set cellsignalinfo
        aapi.setCellSignalInfo();

        //set connection info
        aapi.setConnectionInfo();


    }

    // Tower change listener - Method 1
    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return;
            }
            super.onCellLocationChanged(location);

            int cid = 0;
            int lac = 0;

            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    cid = ((GsmCellLocation) location).getCid();
                    lac = ((GsmCellLocation) location).getLac();
                }
                else if (location instanceof CdmaCellLocation) {
                    cid = ((CdmaCellLocation) location).getBaseStationId();
                    lac = ((CdmaCellLocation) location).getSystemId();
                }
            }
        }
    };
}
