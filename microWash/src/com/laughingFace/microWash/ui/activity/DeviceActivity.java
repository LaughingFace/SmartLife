package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.ui.view.DeviceSpinner;
import com.laughingFace.microWash.ui.view.WaterRipplesView;

public class DeviceActivity extends BaseActivity{
    private ImageButton nextPage;
    private Intent intent;
    private MainLogo mainLogo;
    private static Activity instance;
    private DeviceSpinner deviceSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("hehe", "DeviceActivity onCreate...");

        mainLogo = new MainLogo(findViewById(R.id.device_top_container));
        intent = new Intent(this,WorkingActivity.class);
        nextPage = (ImageButton)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        deviceSpinner = (DeviceSpinner) findViewById(R.id.ds_device);
        deviceSpinner.notifyChangeItemData(new DeviceSpinner.OnListener() {
            @Override
            public void onClick() {

            }

            @Override
            public void changeSelect(Device text) {

            }
        });
        deviceSpinner.setAddOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, AddDeviceActivity.class);
                startActivity(intent);
//                overridePendingTransition(0, anim_exit);
            }
        });
        instance = this;
    }

    @Override
    public void onLine(Device device) {
        super.onLine(device);
        deviceSpinner.getTvName().setText(device.getName());
    }

    @Override
    public void offLine() {
        super.offLine();
        deviceSpinner.getTvName().setText("add device");
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {
        Log.i("hehe","start.........");
    }

    public static Activity getInstance(){
        return instance;
    }

}
