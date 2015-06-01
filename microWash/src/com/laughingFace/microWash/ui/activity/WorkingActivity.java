package com.laughingFace.microWash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.ui.plug.waterWaveProgress.WaterWaveProgress;
import com.laughingFace.microWash.ui.view.WaterRipplesView;

/**
 * Created by zihao on 15-5-25.
 */
public class WorkingActivity extends BaseActivity {

    public static final String INTENT_MODEL = "model";

    //只是进行页面跳转不开启某个模式
    public static final int NEXTPAGE = 0;
    //标准模式
    public static final int STANDARD = 1;
    //定时烘干
    public static final int TIMINGDRYOFF = 2;
    //定时清洗
    public static final int TIMINGWASH = 3;
    //烘干
    public static final int DRYOFF = 4;
    //杀菌
    public static final int STERILIZATION = 5;
    public static final int OPENTHEDOOR = 6;
    public static final int CLOSETHEDOOR = 7;
    public static final int RESUME = 8;

    private CountDownDialog countDownDialog;//用于倒计时的弹出对话框
    private WaterWaveProgress process;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working);

        Log.i("xixi", "WorkingActivity onCreate...");

        process  = (WaterWaveProgress)findViewById(R.id.process);

        countDownDialog  = new CountDownDialog(this){
            @Override
            public void onCounttingDownOver() {

            }

            @Override
            public void onChangeModel() {
                Log.i("xixi", "更换模式......");

            }
        };

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if(!hasFocus){
            return;
        }

        switch (getIntent().getIntExtra(INTENT_MODEL, -1)){
            case STANDARD:
                Log.i("xixi", "----------- 标准模式触发 ----------------");

                break;
            case TIMINGWASH:

                Log.i("xixi", "----------- 定时清洗触发 ----------------");

                break;
            case DRYOFF:

                Log.i("xixi", "----------- 烘干模式触发 ----------------");

                break;
            case STERILIZATION:

                Log.i("xixi", "----------- 杀菌模式触发 ----------------");

                break;

        }

        //将intent的值覆盖为无效的值避免设备锁屏后唤醒时重复启动模式
        getIntent().putExtra(INTENT_MODEL,-1);

        countDownDialog.setTitle("XX模式");

        //countDownDialog.start();
        super.onWindowFocusChanged(hasFocus);
    }
    @Override
    public void offLine() {
        com.laughingFace.microWash.util.Log.i("xixi", "offline");
        Toast.makeText(this, "offline", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DeviceActivity.class));
    }

    @Override
    public void onProcessing(Model model) {
        process.setProgress((int) (model.getProgress().getPercentage()*100));
        com.laughingFace.microWash.util.Log.i("xixi", "processing-----" + model.getProgress().getPercentage());
    }

    @Override
    public void onFinish(Model model) {
        process.setProgress(100);
        com.laughingFace.microWash.util.Log.i("xixi", "finsish");
        Toast.makeText(this,"online",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterupt(Model model) {

    }

    @Override
    public void faillOnStart(Model model,ModelAngel.StartFaillType type) {
        process.setProgress(0);
        com.laughingFace.microWash.util.Log.i("xixi", "faillonstart" + type);
        Toast.makeText(this,"fail on start "+type,Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
