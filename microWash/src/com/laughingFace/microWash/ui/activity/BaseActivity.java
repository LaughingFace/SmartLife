package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.view.Window;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.DeviceMonitor;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.Progress;

/**
 * Created by zihao on 15-5-25.
 */
public class BaseActivity extends Activity implements DeviceMonitor {
    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式
        super.setContentView(layoutResID);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
        ModelManager.getInstance().setDeviceMonitor(this);
    }

    @Override
    public void onStart(Model model) {

    }

    @Override
    public void onLine(Device device) {

    }

    @Override
    public void offLine() {

    }

    @Override
    public void onProcessing(Progress progress) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onInterupt() {

    }

    @Override
    public void faillOnStart() {

    }
}
