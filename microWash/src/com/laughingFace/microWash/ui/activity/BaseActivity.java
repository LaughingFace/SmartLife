package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;

import android.view.Window;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.DeviceMonitor;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.ModelProvider;
import com.laughingFace.microWash.deviceControler.model.Progress;
import com.laughingFace.microWash.util.Log;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zihao on 15-5-25.
 */
public class BaseActivity extends Activity implements DeviceMonitor {
    protected ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式
        super.setContentView(layoutResID);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
//        ModelManager.getInstance().setDeviceMonitor(this);
        modelManager = ModelManager.getInstance();
        modelManager.setDeviceMonitor(this);
    }

    @Override
    public void onStart(Model model,ModelAngel.StartType type) {
        Log.i("xixi", "start"+model.getStateCode());
        Toast.makeText(this,"start:"+type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLine(Device device) {
        Log.i("xixi", "online");
        Toast.makeText(this,"online",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void offLine() {
        Log.i("xixi","offline");
        Toast.makeText(this,"offline",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProcessing(Model model) {
        Log.i("xixi","processing-----"+model.getProgress().getPercentage());
    }

    @Override
    public void onFinish(Model model) {
        Log.i("xixi","finsish");
        Toast.makeText(this,"online",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterupt(Model model) {

    }

    @Override
    public void faillOnStart(Model model,ModelAngel.StartFaillType type) {
        Log.i("xixi","faillonstart"+type);
        Toast.makeText(this,"fail on start "+type,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        Log.i("xixi", "destory");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
