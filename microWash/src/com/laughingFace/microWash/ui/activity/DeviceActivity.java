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
import android.widget.TextView;
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
    private TextView tv_device;

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
        tv_device = (TextView) findViewById(R.id.ds_device);
        tv_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, AddDeviceActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        instance = this;
    }

    @Override
    public void onLine(Device device) {
        super.onLine(device);
        tv_device.setText(device.getName());
        tv_device.setEnabled(false);
    }

    @Override
    public void offLine() {
        super.offLine();
        tv_device.setText("add device");
        tv_device.setEnabled(true);
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {
        Log.i("hehe","start.........");
    }

    public static Activity getInstance(){
        return instance;
    }

}
