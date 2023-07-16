package com.android.customlistview;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.android.customlistview.Adapter.Adapter;
import com.android.customlistview.Model.AndroidAPI;
import com.android.customlistview.Model.Model;

import java.security.Permissions;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
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

        aapi = new AndroidAPI(getApplicationContext());

        checkPermissions();

        BindData();

    }


    void checkPermissions() {
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    scope.showRequestReasonDialog(deniedList, "API Checker needs following permissions to continue", "Allow");
                })
                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Toast.makeText(MainActivity.this, "All permissions are granted", Toast.LENGTH_SHORT).show();
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

        adapter = new Adapter(getApplicationContext(), models);
        lv.setAdapter(adapter);
    }

    void setCommonParams(){
        // set sim info
        aapi.setSimInfo();

        //set device info
        aapi.setDeviceInfo();

        //set connection info
        aapi.setConnectionInfo();

        //set cellinfo
        aapi.setCellInfo();

        //set cellsignalinfo
        aapi.setCellSignalInfo();
    }
}
