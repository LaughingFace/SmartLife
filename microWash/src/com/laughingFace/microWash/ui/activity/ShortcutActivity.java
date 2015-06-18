package com.laughingFace.microWash.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.ModelProvider;
import com.laughingFace.microWash.ui.plug.CircularFloatingActionMenu.FloatingActionMenu;
import com.laughingFace.microWash.ui.plug.Kurt.Mbanje.FabButton.FabButton;
import com.laughingFace.microWash.util.Log;
import com.laughingFace.microWash.util.Settings;

/**
 * Created by zihao on 15-6-15.
 */
public class ShortcutActivity extends BaseActivity {
    //private View vMenuCenter = null;
    FloatingActionMenu circleMenu;
    LinearLayout layout;
    FabButton menuCenter;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 若是第一次启动软件跳到欢迎界面
         */
        if (Settings.isFirst()) {
            startActivity(new Intent(ShortcutActivity.this, WelcomGuideActivity.class));
            /**
             * 保存软件设置-已经不是第一次启动了以后不再显示欢迎界面了
             */
            Settings.setIsFirst(false);
            this.finish();
            return;
        }
        setContentView(R.layout.shortcut);

         layout = (LinearLayout) findViewById(R.id.layout);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(ShortcutActivity.this, "haha", Toast.LENGTH_LONG).show();
                circleMenu.close(circleMenu.isAnimated());

                ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(300);//设置动画持续时间
                animation.setFillAfter(true);//动画执行完后停留在执行完的状态
                menuCenter.startAnimation(animation);
                animation.startNow();
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(final Animation animation) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                moveTaskToBack(true);
                                Looper.loop();
                            }
                        }).start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        initMenu();
    }

    /**
     * 快捷菜单
     */
    private void initMenu() {
        menuCenter = (FabButton)findViewById(R.id.menuCenter);

        ImageView iv1 = new ImageButton(this);
        ImageView iv2 = new ImageButton(this);
        ImageView iv3 = new ImageButton(this);

        iv1.setBackgroundResource(R.drawable.shortcut_start);
        iv2.setBackgroundResource(R.drawable.shortcut_door_closed);
        iv3.setBackgroundResource(R.drawable.shortcut_door_opened);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.menu_item_size), getResources().getDimensionPixelSize(R.dimen.menu_item_size));
        iv1.setLayoutParams(tvParams);
        iv2.setLayoutParams(tvParams);
        iv3.setLayoutParams(tvParams);

//        vMenuCenter = findViewById(R.id.menuCenter);

        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后停留在执行完的状态
        menuCenter.startAnimation(animation);

        circleMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(-150) // A whole circle!
                .setEndAngle(-30)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.radius_large))
                .addSubActionView(iv3)
                .addSubActionView(iv1)
                .addSubActionView(iv2)
                .attachTo(menuCenter)
                .build();

        circleMenu.setActionViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelManager mm = ModelManager.getInstance();
                if(mm.isOnline()&& null != mm.getRunningModel()){
                    startActivity(new Intent(ShortcutActivity.this, WorkingActivity.class));
                }else {
                    startActivity(new Intent(ShortcutActivity.this, DeviceActivity.class));
                }
            }
        });
        menuCenter.post(new Runnable() {
            @Override
            public void run() {
                circleMenu.toggle(circleMenu.isAnimated());
            }
        });
    }

    /**
     * 返回home 而不是退出
     */
    @Override
    public void onBackPressed() {

        circleMenu.close(circleMenu.isAnimated());

        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后停留在执行完的状态
        menuCenter.startAnimation(animation);
        animation.startNow();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        moveTaskToBack(true);
                        Looper.loop();
                    }
                }).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {

        /**
         * 计算弹出快捷菜单的位置和大小信息
         */
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        Rect launcherBounds = getIntent().getSourceBounds();
        if(null == launcherBounds){
            //startActivity(new Intent(ShortcutActivity.this, WorkingActivity.class));
            finish();
            super.onResume();
            return;
        }
        layoutParams.width = launcherBounds.width();
        layoutParams.height = launcherBounds.width();
        layoutParams.leftMargin = launcherBounds.left;
        layoutParams.topMargin = launcherBounds.top - 100;
        layout.setLayoutParams(layoutParams);
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后停留在执行完的状态
        menuCenter.startAnimation(animation);

        menuCenter.post(new Runnable() {
            @Override
            public void run() {
                circleMenu.toggle(circleMenu.isAnimated());
            }
        });

        /**
         * 如果当前已有模式正在运行则直接显示正在运行的模式进度信息
         */
        if (null != ModelManager.getInstance().getRunningModel()) {
            onModelStart(ModelManager.getInstance().getRunningModel(), ModelAngel.StartType.Normal);
            super.onResume();
            return;
        }

        /**
         * 读取配置文件判断上次启动软件后是否设置了定时模式
         */
        if(Settings.getTimingModelId() == ModelProvider.timingWash.getId()){
            if(Settings.getTimingModelBegin()+Settings.getTimingModelHowLong()
                    > System.currentTimeMillis()){

                ModelProvider.timingWash.setDelay((Settings.getTimingModelHowLong() - (System.currentTimeMillis() - Settings.getTimingModelBegin())) / 1000);
                ModelManager.getInstance().startModel(ModelProvider.timingWash);
                super.onResume();
                return;
            }
        }

        super.onResume();
    }

    @Override
    public void onModelStart(Model model, ModelAngel.StartType type) {
//        Log.i("xixi", "start" + model.getStateCode());
        menuCenter.resetIcon();
        menuCenter.showProgress(true);
        menuCenter.setProgress(0);
        super.onModelStart(model,type);
    }

    @Override
    public void onProcessing(Model model) {
//        Log.i("xixi", "processing-----" + model.getProgress().getPercentage());
        menuCenter.setProgress(model.getProgress().getPercentage()*100);
        super.onProcessing(model);
    }

    @Override
    public void onFinish(Model model) {
        menuCenter.setProgress(100);
        super.onFinish(model);
    }

    @Override
    public void onInterupt(Model model) {

    }

    @Override
    public void faillOnStart(Model model,ModelAngel.StartFaillType type) {
        Log.i("xixi","faillonstart"+type);
        Toast.makeText(this,"fail on start "+type,Toast.LENGTH_SHORT).show();

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
}
