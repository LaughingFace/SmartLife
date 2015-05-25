package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.view.Window;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.DeviceMonitor;
import com.laughingFace.microWash.deviceControler.model.Model;

/**
 * Created by zihao on 15-5-25.
 */
public class BaseActivity extends Activity implements DeviceMonitor {
    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式

        super.setContentView(layoutResID);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
    }//

    @Override
    public void onStart(Model model) {

    }

    @Override
    public void onLine() {

    }

    @Override
    public void offLine() {

    }

    @Override
    public void onProcessing() {

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
