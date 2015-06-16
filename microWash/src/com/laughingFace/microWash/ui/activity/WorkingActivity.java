package com.laughingFace.microWash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.laughingFace.microWash.util.Settings;

import java.util.ArrayList;
import java.util.List;

import static com.laughingFace.microWash.deviceControler.model.ModelProvider.*;

/**
 * Created by zihao on 15-5-25.
 */
public class WorkingActivity extends BaseActivity implements View.OnClickListener {

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

    private TextView timingText;//定时文本信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //设置标题栏为自定义模式
        setContentView(R.layout.working);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);// 设置标题栏的布局
        init();
        Log.i("hehe", "WorkingActivity onCreate...");

    }

    private void init() {

        timePicker = new WheelTimePicker(this);
        timePicker.setTimePickerListener(new WheelTimePicker.TimePickerListener() {
            @Override
            public void onSelected(long minutes) {
                readyModel.setDelay(minutes);
                ModelManager.getInstance().startModel(readyModel);
                Settings.setTimingModelId(readyModel.getId());
                Settings.setTimingModelBegin(System.currentTimeMillis());
                Settings.setTimingModelHowLong(readyModel.getDelay() * 1000);
                readyModel = null;
            }

            @Override
            public void onCancel() {
                readyModel = null;

            }
        });
        modelBtns = new ArrayList<Button>();

        modelBtns.add(((Button) findViewById(R.id.working_model_standard)));
        modelBtns.add(((Button) findViewById(R.id.working_model_dryoff)));
        modelBtns.add(((Button) findViewById(R.id.working_model_sterilization)));
        modelBtns.add(((Button) findViewById(R.id.working_model_timingwash)));
        for (Button btn : modelBtns) {
            btn.setOnClickListener(this);
        }

        timingText = (TextView) findViewById(R.id.timing_text);

        runningModelName = (TextView) findViewById(R.id.runningModelName);
        process = (WaterWaveProgress) findViewById(R.id.process);

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

        countDownDialog = new CountDownDialog(this) {
            @Override
            public void onCounttingDownOver() {
                if (null != readyModel) {
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

    /**
     * 通过上一界面intent中传递的值决定启动那一个模式
     */
    @Override
    protected void onResume() {

        Log.i("haha", "----------- 模式触发: " + getIntent().getIntExtra(INTENT_MODEL, -1) + "--------------");
        switch (getIntent().getIntExtra(INTENT_MODEL, -1)) {
            case STANDARD:
                Log.i("xixi", "----------- 标准模式触发 ----------------");
                readyModel = standard;
                break;
            case TIMINGWASH:
                readyModel = timingWash;
                Log.i("xixi", "----------- 定时清洗触发 ----------------");
                break;
            case DRYOFF:
                readyModel = dryoff;
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

    private void prepareStart() {

        /**
         * 如果当前已有模式正在运行则直接显示正在运行的模式进度信息
         */
        if (null != ModelManager.getInstance().getRunningModel()) {
            onModelStart(ModelManager.getInstance().getRunningModel(), ModelAngel.StartType.Normal);
            return;
        }

        /**
         * 读取配置文件判断上次启动软件后是否设置了定时模式
         */
        if(Settings.getTimingModelId() == ModelProvider.timingWash.getId()){
            if(Settings.getTimingModelBegin()+Settings.getTimingModelHowLong()
                    > System.currentTimeMillis()){

                ModelProvider.timingWash.setDelay((Settings.getTimingModelHowLong() -(System.currentTimeMillis()-Settings.getTimingModelBegin()))/ 1000);
                ModelManager.getInstance().startModel(ModelProvider.timingWash);
                readyModel = null;
                return;

            }
        }

        if (null == readyModel) return;
        /**
         * 立即启动类型
         */
        if (readyModel.getId() == standard.getId()
                || readyModel.getId() == dryoff.getId()) {

            /*** 启动倒计时*/
            countDownDialog.setTitle(readyModel.getName());
            hideProgress();
            slidingMenu.hide();
            countDownDialog.start();
        }
        /**
         * 定时启动类型
         */
        else if (readyModel.getId() == timingWash.getId()) {
            timePicker.show();
        }
    }

    @Override
    public void offLine() {
        super.offLine();
        Toast.makeText(this, "offline", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DeviceActivity.class));
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {

        Log.i("hehe", "----------- " + model.getName() + " 启动----------------");
        int witch = -1;
        if (model.getId() == standard.getId()) {
            witch = R.id.working_model_standard;
            progressInit(WaterWaveProgress.PROGRESS_MODEL, model);
        } else if (model.getId() == dryoff.getId()) {
            witch = R.id.working_model_dryoff;
            progressInit(WaterWaveProgress.PROGRESS_MODEL, model);
        } else if (model.getId() == timingWash.getId()) {
            witch = R.id.working_model_timingwash;
            progressInit(WaterWaveProgress.TIMER_MODEL, model);
        }

        /**
         * 将侧滑菜单中的按钮全部禁用并将与当前正在进行的模式对于的按钮背景改变
         */
        if (-1 != witch) {
            for (Button btn : modelBtns) {
                if (btn.getId() == witch) {
                    btn.setBackgroundResource(R.drawable.round_btn_bg);
                }
                btn.setEnabled(false);
            }
        }

        dismissDia();
        super.onModelStart(model, type);
    }

    @Override
    public void onProcessing(Model model) {

        /**
         * 定时类型
         */
        if (model.getDelay() > 0) {
            process.setProgress((int) ((1 - model.getProgress().getPercentage()) * process.getMaxProgress()));
            timingText.setText(String.format(getString(R.string.timing_text), model.getProgress().getRemain()));
        } else {
            process.setProgress((int) (model.getProgress().getPercentage() * process.getMaxProgress()));
        }
        super.onProcessing(model);
    }

    @Override
    public void onFinish(Model model) {
        /**
         * 无延时立即启动类型
         */
        if (model.getId() == dryoff.getId()
                || model.getId() == standard.getId()) {

            process.setProgress(process.getMaxProgress());
        }
        /**
         * 延时启动类型
         */
        else if (model.getId() == timingWash.getId()) {
            process.setProgress(0);
        }

        /**
         * 让侧滑菜单中的各按钮回复可点击状态
         */
        for (Button btn : modelBtns) {
            if (!btn.isEnabled()) {
                btn.setBackgroundResource(R.drawable.tran);
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
    public void faillOnStart(Model model, ModelAngel.StartFaillType type) {
        super.faillOnStart(model, type);
        process.setProgress(0);
        com.laughingFace.microWash.util.Log.i("xixi", "faillonstart" + type);
        Toast.makeText(this, "fail on start " + type, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击手机上的返回键时让当前activity退回到后台而不是直接销毁
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        startActivity(new Intent(this, DeviceActivity.class));
        dismissDia();
    }

    private void progressInit(int type, Model model) {
        switch (type) {
            case WaterWaveProgress.TIMER_MODEL:
                timingText.setText(String.format(getString(R.string.timing_text), model.getProgress().getRemain()));
                timingText.setVisibility(View.VISIBLE);
                break;
            case WaterWaveProgress.PROGRESS_MODEL:
                timingText.setVisibility(View.INVISIBLE);
                break;
        }

        runningModelName.setText(model.getName());
        process.setModel(type);
        process.setVisibility(View.VISIBLE);
    }


    private void dismissDia() {
        if (countDownDialog.isShowing()) {
            countDownDialog.dismiss();
        }
        if (slidingMenu.isShowing()) {
            slidingMenu.hide();
        }
        if (timePicker.isShowing()) {
            timePicker.dismiss();
        }
    }

    /**
     * 更新Intent
     *
     * @param intent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void hideProgress() {
        process.setVisibility(View.INVISIBLE);
        runningModelName.setVisibility(View.INVISIBLE);
        timingText.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {

        process.setVisibility(View.VISIBLE);
        runningModelName.setVisibility(View.VISIBLE);
        /**
         * 如果进度空间当前是倒计时模式则显示倒计时文本框
         */
        if (process.getModel() == WaterWaveProgress.TIMER_MODEL) {
            timingText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * activity被销毁前让所有显示的弹出窗口消失
     */
    @Override
    protected void onDestroy() {
        dismissDia();
        super.onDestroy();
    }

    /**
     *
     * 侧滑菜单中按钮的点击事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.working_model_standard:
                readyModel = standard;
                break;
            case R.id.working_model_dryoff:
                readyModel = dryoff;
                break;
            /*case R.id.working_model_sterilization:
                readyModel = ModelProvider.sterilization;
                break;*/
            case R.id.working_model_timingwash:
                readyModel = timingWash;
                break;
        }
        prepareStart();
    }
}
