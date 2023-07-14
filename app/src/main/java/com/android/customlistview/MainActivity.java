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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);

        System.out.println("Bind data");

        checkPermissions();


        BindData();


    }


    void checkPermissions() {
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)
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
        //instantiate the AAPI
        AndroidAPI aapi = new AndroidAPI(getApplicationContext());

        /* SETTERS */
        //set cellinfo
        aapi.setCellInfo();

        //set cellsignalinfl
        aapi.setCellSignalInfo();

        /* GETTERS */
        //OS
        models.add(aapi.get_os());

        //network type
        models.add(aapi.get_network_type());

        //phone type
        models.add(aapi.get_phone_type());

        //Device manufacturer
        models.add(aapi.getDeviceManufacturer());

        //Device model
        models.add(aapi.get_device_type());

        //os_Version
        models.add(aapi.getOsVersion());

        //lte_rscp
        models.add(aapi.get_lte_rscp());

        //lte_rsrq
        models.add(aapi.get_lte_rsrq());

        adapter = new Adapter(getApplicationContext(), models);
        lv.setAdapter(adapter);
    }
}
