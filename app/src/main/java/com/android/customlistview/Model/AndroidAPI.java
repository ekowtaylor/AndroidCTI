package com.android.customlistview.Model;

import android.os.Build;
import com.android.customlistview.Model.Model;

public class AndroidAPI {

    //Device manufacturer
    public Model getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        return new Model("", "", "cell_tower_info", "manufacturer");
    }

    //Device model
    public Model getDeviceModel() {
        String manufacturer = Build.MODEL;
        return new Model("", "", "cell_tower_info", "manufacturer");
    }
}
