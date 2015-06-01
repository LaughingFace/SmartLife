package com.laughingFace.microWash.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
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
    private List<WaterRipplesView> waterRipplesViewList;
    private Intent toWorkingActivityIntent;


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

        for(WaterRipplesView wr:waterRipplesViewList){
            wr.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        maskView.setBackgroundResource(R.drawable.device_activity_mask);
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
        isRandomBreath = false;
        maskView.setBackgroundResource(R.drawable.device_activity_mask);

        /**
         *让手机震动
         */
        ( (Vibrator)contentView.getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, 70}, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

        switch (wounder.getId()){
            case R.id.model_standard:
                Log.i("xixi", "----------- 标准模式 ----------------");
                dragingModel = "标准模式";
                break;
            case R.id.model_dryoff:
                Log.i("xixi", "----------- 烘干模式 ----------------");
                dragingModel = "烘干模式";
                break;
            case R.id.model_timingwash:
                Log.i("xixi", "----------- 定时清洗 ----------------");
                dragingModel = "定时清洗";
                break;
            case R.id.model_sterilization:
                Log.i("xixi", "----------- 杀菌模式 ----------------");
                dragingModel = "杀菌模式";
                break;
        }
        dragModelName.setText(dragingModel);
    }

    @Override
    public void onRealse(View perpetrators, View wounder) {
        if(null != wounder && wounder.getId() == R.id.checkArea){
            Log.i("xixi", "----------- 模式触发 ----------------");
            toWorkingActivityIntent = new Intent(DeviceActivity.getInstance(),WorkingActivity.class);
            DeviceActivity.getInstance().startActivity(toWorkingActivityIntent);
        }
        else {
            dragModelName.setText("");
        }

        checkArea.stop();
        isRandomBreath = true;
        maskView.setBackgroundResource(R.drawable.tran);

    }

    Handler breathHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("xixi", "handleMessage..............");

            int witch = 0;
            int rand = ((int) (Math.random()*waterRipplesViewList.size()));
            witch = witch==rand?((int) (Math.random()*waterRipplesViewList.size())):rand;
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

                while (true) {
                    try {
                        Thread.sleep(500);
                        long interval = (long) (Math.random() * 7000+4000);
                        breathHandler.obtainMessage().sendToTarget();
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
