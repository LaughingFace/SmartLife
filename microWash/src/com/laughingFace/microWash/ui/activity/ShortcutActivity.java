package com.laughingFace.microWash.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.ui.plug.CircularFloatingActionMenu.FloatingActionMenu;
import com.laughingFace.microWash.util.Settings;

/**
 * Created by zihao on 15-6-15.
 */
public class ShortcutActivity extends BaseActivity {
    private View vMenuCenter = null;
    FloatingActionMenu circleMenu;

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
        /*
        Button btn = (Button)findViewById(R.id.btnsh);
        btn.layout(500,50,600,7000);*/

        LinearLayout layoutt = (LinearLayout) findViewById(R.id.layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutt.getLayoutParams();
        Rect launcherBounds = getIntent().getSourceBounds();
        if (null == launcherBounds) {
            this.finish();
        }
        layoutParams.width = launcherBounds.width();
        layoutParams.height = launcherBounds.width();
        layoutParams.leftMargin = launcherBounds.left;
        layoutParams.topMargin = launcherBounds.top - 50;
        layoutt.setLayoutParams(layoutParams);
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ShortcutActivity.this.finish();
                overridePendingTransition(R.anim.normal_anim,android.R.anim.fade_out);
                return false;
            }
        });
        initMenu();
    }

    private void initMenu() {
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

        vMenuCenter = findViewById(R.id.menuCenter);
        circleMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(-150) // A whole circle!
                .setEndAngle(-30)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.radius_large))
                .addSubActionView(iv3)
                .addSubActionView(iv1)
                .addSubActionView(iv2)
                .attachTo(vMenuCenter)
                .build();
        vMenuCenter.post(new Runnable() {
            @Override
            public void run() {
                circleMenu.toggle(circleMenu.isAnimated());
            }
        });
    }
}
