package com.laughingFace.microWash.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.ui.view.WaterRipplesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zihao on 15-5-27.
 */
public class MainLogo  implements WaterRipplesView.OnCollisionListener{

    private View contentView;
    private TextView dragModelName;
    private String dragingModel;//视觉上正被拖拽着的模式
    private WaterRipplesView checkArea;
    private WaterRipplesView model_standard;
    private WaterRipplesView model_dryoff;
    private WaterRipplesView model_timingwash;
    private WaterRipplesView model_sterilization;
    private LinearLayout maskView;
    private boolean isRandomBreath = true;
    private boolean isChangeMaskAlpha = false;//是否让遮罩透明度渐变
    private List<WaterRipplesView> waterRipplesViewList;
    private Intent toWorkingActivityIntent;
    private int modelCode = -1;

    private Drawable maskDrawable ;


     Handler changeMaskAlpha = new Handler(){
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            maskDrawable.setAlpha(Integer.parseInt(msg.obj.toString()));
            maskView.setBackground(maskDrawable);
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public MainLogo(View contentView){
        this.contentView = contentView;
        model_standard =  (WaterRipplesView)contentView.findViewById(R.id.model_standard);
        model_dryoff =  (WaterRipplesView)contentView.findViewById(R.id.model_dryoff);
        model_timingwash =  (WaterRipplesView)contentView.findViewById(R.id.model_timingwash);
        model_sterilization =  (WaterRipplesView)contentView.findViewById(R.id.model_sterilization);

        model_standard.setOnCollisionListener(this);
        model_dryoff.setOnCollisionListener(this);
        model_timingwash.setOnCollisionListener(this);
        model_sterilization.setOnCollisionListener(this);

        checkArea = (WaterRipplesView)contentView.findViewById(R.id.checkArea);
        checkArea.setOnCollisionListener(this);
        dragModelName = (TextView)contentView.findViewById(R.id.tv_mode);

        waterRipplesViewList = new ArrayList<WaterRipplesView>();
        waterRipplesViewList.add(model_standard);
        waterRipplesViewList.add(model_dryoff);
        waterRipplesViewList.add(model_timingwash);
        waterRipplesViewList.add(model_sterilization);

        maskView = (LinearLayout)contentView.findViewById(R.id.mask);
        maskDrawable = contentView.getResources().getDrawable(R.drawable.device_activity_mask);
        maskDrawable.setAlpha(0);


        for(WaterRipplesView wr:waterRipplesViewList){
            wr.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        checkArea.breath();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isChangeMaskAlpha = true;
                                int i = 0;
                                while (isChangeMaskAlpha){
                                    i++;
                                    try {
                                            changeMaskAlpha.obtainMessage(1,""+i).sendToTarget();
                                        Thread.sleep(2);
                                        if(i>=255){
                                            isChangeMaskAlpha = false;
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();

                        isRandomBreath = false;
                        waterRipplesViewList.get(witch).stop();
                    }
                    return false;
                }
            });
        }
        randomBreath();
    }


    @Override
    public void onLeave(View perpetrators, View wounder) {
        dragModelName.setText(dragingModel);

    }

    @Override
    public void onMove(View perpetrators, View wounder) {

    }

    @Override
    public void onEnter(View perpetrators, View wounder) {
        checkArea.start();
        /**
         *让手机震动
         */
        ( (Vibrator)contentView.getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, 40}, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

        switch (wounder.getId()){
            case R.id.model_standard:
                //Log.i("xixi", "----------- 标准模式 ----------------");
                modelCode = WorkingActivity.STANDARD;
                dragingModel = "标准模式";
                break;
            case R.id.model_dryoff:
                modelCode = WorkingActivity.DRYOFF;
                //Log.i("xixi", "----------- 烘干模式 ----------------");
                dragingModel = "烘干模式";
                break;
            case R.id.model_timingwash:
                modelCode = WorkingActivity.TIMINGWASH;
               // Log.i("xixi", "----------- 定时清洗 ----------------");
                dragingModel = "定时清洗";
                break;
            case R.id.model_sterilization:
                modelCode = WorkingActivity.STERILIZATION;
                dragingModel = "杀菌模式";
                break;
            case  R.id.checkArea:
                ( (Vibrator)contentView.getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, 170,100, 170}, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

                break;
        }
        dragModelName.setText(dragingModel);
    }

    @Override
    public void onRealse(View perpetrators, View wounder) {

        checkArea.stop();
        isRandomBreath = true;
        isChangeMaskAlpha = false;
        changeMaskAlpha.obtainMessage(1,""+0).sendToTarget();//通过让背景全透明消除遮罩

        if(null != wounder && wounder.getId() == R.id.checkArea){

            if(!ModelManager.getInstance().isOnline()){
                Toast.makeText(DeviceActivity.getInstance(), "请先连接设备", Toast.LENGTH_SHORT).show();
            return;
            }

            if(null != ModelManager.getInstance().getRunningModel()){
               modelCode = -1;
                Log.i("haha", "---------设备繁忙---------------");
            }
            Log.i("haha", "---------准备： "+modelCode+" ----------------");
            toWorkingActivityIntent = new Intent(DeviceActivity.getInstance(),WorkingActivity.class);
            toWorkingActivityIntent.putExtra(WorkingActivity.INTENT_MODEL,modelCode);
            DeviceActivity.getInstance().startActivity(toWorkingActivityIntent);
        }
        else {
            dragModelName.setText("");
        }

    }

    private  int witch = 0;

    Handler breathHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(isRandomBreath){
                    waterRipplesViewList.get(witch).breath();
            }
        }
    };

    /**
     * 随机时间让随机的一个小水波呼吸显示一下
     */
    private void randomBreath(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        while (true) {
                            Thread.sleep(500);
                            if(!waterRipplesViewList.get(witch).isStarting()){
                                long interval = (long) (Math.random() * 7000);
                                Thread.sleep(interval);
                                int rand = ((int) (Math.random()*waterRipplesViewList.size()));
                                witch = witch==rand?((int) (Math.random()*waterRipplesViewList.size())):rand;
                                breathHandler.obtainMessage().sendToTarget();
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }).start();
    }


}
