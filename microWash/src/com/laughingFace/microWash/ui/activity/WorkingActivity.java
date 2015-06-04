package com.laughingFace.microWash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.ModelProvider;
import com.laughingFace.microWash.ui.plug.waterWaveProgress.WaterWaveProgress;
import com.laughingFace.microWash.ui.view.SlidingMenu;
import com.laughingFace.microWash.ui.view.WheelTimePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zihao on 15-5-25.
 */
public class WorkingActivity extends BaseActivity implements View.OnClickListener{

    public static final String INTENT_MODEL = "model";

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
    private WheelTimePicker timePicker;
    private WaterWaveProgress process;//显示进度或倒计时
    private Model readyModel;//准备在倒计时完毕后 并且在倒计时过程中没有点击过“切换模式”按钮  启动的模式

    private SlidingMenu slidingMenu;
    private TextView runningModelName;//显示当前正在运行的模式名称

    private List<Button> modelBtns;//侧滑菜单中的所有按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working);
        init();
        Log.i("hehe", "WorkingActivity onCreate...");

    }

    private void init(){

        timePicker = new WheelTimePicker(this);
        timePicker.setTimePickerListener(new WheelTimePicker.TimePickerListener() {
            @Override
            public void onSelected(long minutes) {
                ModelManager.getInstance().startModel(readyModel);
            }

            @Override
            public void onCancel() {
                readyModel = null;

            }
        });
        modelBtns = new ArrayList<>();

        modelBtns.add(((Button)findViewById(R.id.working_model_standard)));
        modelBtns.add(((Button)findViewById(R.id.working_model_dryoff)));
        modelBtns.add(((Button)findViewById(R.id.working_model_sterilization)));
        modelBtns.add( ((Button)findViewById(R.id.working_model_timingwash)));
        for(Button btn:modelBtns){
            btn.setOnClickListener(this);
        }

        runningModelName = (TextView)findViewById(R.id.runningModelName);
        process  = (WaterWaveProgress)findViewById(R.id.process);

        slidingMenu = (SlidingMenu) findViewById(R.id.slideMenuLayout);
        slidingMenu.setSlidingMenuListenear(new SlidingMenu.SlidingMenuListenear() {
            @Override
            public void onClosed() {
                showProgress();
            }

            @Override
            public void onOpened() {
                hideProgress();
            }
        });

        countDownDialog  = new CountDownDialog(this){
            @Override
            public void onCounttingDownOver() {
                if(null != readyModel){
                    ModelManager.getInstance().startModel(readyModel);
                    readyModel = null;
                }
            }

            @Override
            public void onChangeModel() {
                Log.i("xixi", "更换模式......");
                readyModel = null;
                slidingMenu.show();

            }
        };

    }

    @Override
    protected void onResume() {

        Log.i("haha", "----------- 模式触发: " + getIntent().getIntExtra(INTENT_MODEL, -1) + "--------------");
        switch (getIntent().getIntExtra(INTENT_MODEL, -1)){
            case STANDARD:
                Log.i("xixi", "----------- 标准模式触发 ----------------");
                readyModel = ModelProvider.standard;
                break;
            case TIMINGWASH:
                readyModel = ModelProvider.timingWash;
                Log.i("xixi", "----------- 定时清洗触发 ----------------");
                break;
            case DRYOFF:
                readyModel = ModelProvider.dryoff;
                Log.i("xixi", "----------- 烘干模式触发 ----------------");
                break;
            /*case STERILIZATION:
                readyModel = ModelProvider.sterilization;
                Log.i("xixi", "----------- 杀菌模式触发 ----------------");
                break;*/
        }

        //将intent的值覆盖为无效的值避免设备锁屏后唤醒时重复启动模式
        getIntent().putExtra(INTENT_MODEL, -1);
        prepareStart();

        super.onResume();
    }

    private void prepareStart(){

        if(null == readyModel) return;

        if(readyModel.getId() == ModelProvider.standard.getId() || readyModel.getId() == ModelProvider.dryoff.getId()){
            /**
             * 启动倒计时
             */
            countDownDialog.setTitle(readyModel.getName());
            hideProgress();
            slidingMenu.hide();
            countDownDialog.start();
        }
        else if(readyModel.getId() == ModelProvider.timingWash.getId()) {
            timePicker.show();
        }

    }

    @Override
    public void offLine() {
        super.offLine();
        com.laughingFace.microWash.util.Log.i("xixi", "offline");
        Toast.makeText(this, "offline", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DeviceActivity.class));
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {

        Log.i("hehe", "----------- " + model.getName() + " 启动----------------");
        int witch =-1;
        if(readyModel.getId() == ModelProvider.standard.getId()){
            process.setMaxProgress(1000);
            witch = R.id.working_model_standard;
            process.setProgress(0);
        }
        else if(readyModel.getId()== ModelProvider.dryoff.getId()){
            process.setMaxProgress(1000);
            witch = R.id.working_model_dryoff;
            process.setProgress(0);
        }
        else if(readyModel.getId()== ModelProvider.timingWash.getId()){
            process.setMaxProgress((int) model.getDelay());
            witch = R.id.working_model_timingwash;
            process.setModel(2);
            process.setProgress(process.getMaxProgress());
        }

        /**
         * 将侧滑菜单中的按钮全部禁用并将与当前正在进行的模式对于的按钮背景改变
         */
        if(-1 != witch){
            for (Button btn:modelBtns){
                if (btn.getId() == witch){
                    btn.setBackgroundResource(R.drawable.round_btn_bg);
                }
                btn.setEnabled(false);
            }
        }

        dismissDia();
        runningModelName.setText(model.getName());
        showProgress();
        super.onModelStart(model, type);
    }

    @Override
    public void onProcessing(Model model) {

        if(model.getDelay() >0){
            process.setProgress((int) model.getDelay());
        }
        else {
            process.setProgress((int) (model.getProgress().getPercentage() * 1000));
        }
        super.onProcessing(model);
    }

    @Override
    public void onFinish(Model model) {
        int witch =-1;
        if(readyModel.getId() == ModelProvider.standard.getId()){
            witch = R.id.working_model_standard;
            process.setProgress(process.getMaxProgress());
        }
        else if(readyModel.getId()== ModelProvider.dryoff.getId()){
            witch = R.id.working_model_dryoff;
            process.setProgress(process.getMaxProgress());
        }
        else if(readyModel.getId()== ModelProvider.timingWash.getId()){
            witch = R.id.working_model_timingwash;
            process.setProgress(0);
        }

        if(-1 != witch){
            for (Button btn:modelBtns){
                if (btn.getId() == witch){
                    btn.setBackgroundResource(R.drawable.tran);
                }
                btn.setEnabled(true);
            }
        }
        super.onFinish(model);
    }

    @Override
    public void onInterupt(Model model) {
        super.onInterupt(model);
    }

    @Override
    public void faillOnStart(Model model,ModelAngel.StartFaillType type) {
        super.faillOnStart(model, type);
        process.setProgress(0);
        com.laughingFace.microWash.util.Log.i("xixi", "faillonstart" + type);
        Toast.makeText(this,"fail on start "+type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        startActivity(new Intent(this, DeviceActivity.class));
        dismissDia();
    }

    private void dismissDia(){
        if(countDownDialog.isShowing()){
            countDownDialog.dismiss();
        }
        if(slidingMenu.isShowing()){
            slidingMenu.hide();
        }
    }

    /**
     *
     * 更新Intent
     * @param intent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void hideProgress(){
        process.setVisibility(View.INVISIBLE);
        runningModelName.setVisibility(View.INVISIBLE);
    }
    private void showProgress(){
        process.setVisibility(View.VISIBLE);
        runningModelName.setVisibility(View.VISIBLE);
    }

    /**
     * activity被销毁前让所有显示的弹出窗口消失
     */
    @Override
    protected void onDestroy() {
        dismissDia();
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.working_model_standard:
                readyModel = ModelProvider.standard;
                break;
            case R.id.working_model_dryoff:
                readyModel = ModelProvider.dryoff;
                break;
            /*case R.id.working_model_sterilization:
                readyModel = ModelProvider.sterilization;
                break;*/
            case R.id.working_model_timingwash:
                readyModel = ModelProvider.timingWash;
                break;
        }
        prepareStart();
    }
}
