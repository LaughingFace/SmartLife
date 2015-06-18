package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.DeviceMonitor;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.ModelProvider;
import com.laughingFace.microWash.util.Log;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zihao on 15-5-25.
 */
public class BaseActivity extends Activity implements DeviceMonitor {
    protected ModelManager modelManager;
    private Button btnConnect;
    private Notification notification;//用于在通知栏中显示进度
    private NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);

        /**
         * 初始化通知栏通知对象（用于显示进度）
         */
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon= R.drawable.ic_launcher;
        notification.contentView = new RemoteViews(getPackageName(),R.layout.process_notification);
        notification.contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, WorkingActivity.class), 0);
    }

    @Override
    public void setContentView(int layoutResID) {
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式
        super.setContentView(layoutResID);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
        ModelManager.getInstance().setDeviceMonitor(this);
        modelManager = ModelManager.getInstance();

    }

    @Override
    public void onLine(Device device) {
        Log.i("xixi", "online");
        Toast.makeText(this,"online",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void offLine() {
        Log.i("xixi", "offline");
        Toast.makeText(this,"offline",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {
        Log.i("xixi", "start" + model.getStateCode());
        Toast.makeText(this,"start:::"+type,Toast.LENGTH_SHORT).show();
        notification.tickerText=model.getName();
    }

    @Override
    public void onProcessing(Model model) {
        Log.i("xixi", "processing-----" + model.getProgress().getPercentage());

        /**
         * 定时类型
         */
        if(model.getDelay() >0){
            notification.contentView.setTextViewText(R.id.content_view_text1, model.getProgress().getRemain() + " 分钟后启动！");
            notification.contentView.setProgressBar(R.id.content_view_progress, (int) model.getProgress().getTotal(), (int) model.getProgress().getRemain(), false);
        }
        else {
            notification.contentView.setTextViewText(R.id.content_view_text1, model.getName()+" "+(int) (model.getProgress().getPercentage() * 100) + "%");
            notification.contentView.setProgressBar(R.id.content_view_progress, (int) model.getProgress().getTotal(), (int) (model.getProgress().getTotal() - model.getProgress().getRemain()), false);
        }

        notificationManager.notify(0, notification);
    }

    @Override
    public void onFinish(Model model) {
 Log.i("xixi", "finsish");
        Toast.makeText(this,model.getName()+" finish",Toast.LENGTH_SHORT).show();
        if(model.getId() == ModelProvider.standard.getId()){
            notification.contentView.setTextViewText(R.id.content_view_text1, model.getName()+"完成！");
            notification.contentView.setProgressBar(R.id.content_view_progress, (int) model.getProgress().getTotal(), (int) (model.getProgress().getTotal() - model.getProgress().getRemain()), false);

        }
        else if(model.getId()== ModelProvider.dryoff.getId()){
            notification.contentView.setTextViewText(R.id.content_view_text1, model.getName()+"完成！");
            notification.contentView.setProgressBar(R.id.content_view_progress, (int) model.getProgress().getTotal(), (int) (model.getProgress().getTotal() - model.getProgress().getRemain()), false);

        }
        else if(model.getId()== ModelProvider.timingWash.getId()){
            notification.contentView.setTextViewText(R.id.content_view_text1, model.getName());
            notification.contentView.setProgressBar(R.id.content_view_progress, (int) model.getProgress().getTotal(), (int) model.getProgress().getRemain(), false);
        }
        notificationManager.notify(0, notification);
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
        modelManager.setDeviceMonitor(this);
        //initView();
    }
    /*public void initView()
    {
        btnConnect = (Button) findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnConnect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                modelManager.startModel(ModelProvider.stop);
                ( (Vibrator)BaseActivity.this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, 270}, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                return true;
            }
        });
    }*/
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }
}
