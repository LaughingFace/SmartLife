package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
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

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
        Log.i("hehe", "DeviceActivity onCreate...");

        mainLogo = new MainLogo(findViewById(R.id.device_top_container));
        intent = new Intent(this,WorkingActivity.class);
        nextPage = (ImageButton)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ModelManager.getInstance().isOnline()){
                    Toast.makeText(DeviceActivity.getInstance(), "请先连接设备", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(intent);
            }
        });
        tv_device = (TextView) findViewById(R.id.ds_device);
        tv_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelManager.isOnline())
                {
                    return;
                }
                Intent intent = new Intent(DeviceActivity.this, AddDeviceActivity.class);
                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                overridePendingTransition(R.anim.in_from_down,R.anim.out_to_up);
                overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
            }
        });
        tv_device.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, AddDeviceActivity.class);
                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                overridePendingTransition(R.anim.in_from_down,R.anim.out_to_up);
                overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
                return true;
            }
        });
        instance = this;
    }

    @Override
    public void onLine(Device device) {
        super.onLine(device);
        tv_device.setText(device.getName());
    }

    @Override
    public void offLine() {
        super.offLine();
        tv_device.setText(getString(R.string.not_found_device));
        tv_device.setEnabled(true);
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {
        Log.i("hehe", "-----------deviceActivity: " + model.getName() + " 启动----stateCode:"+model.getStateCode()+"------------");

        super.onModelStart(model, type);
    }

    public static Activity getInstance(){
        return instance;
    }

    /**
     * 返回home 而不是退出
     */
    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);

        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

}
