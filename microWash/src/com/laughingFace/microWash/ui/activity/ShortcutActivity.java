package com.laughingFace.microWash.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.util.Settings;

/**
 * Created by zihao on 15-6-15.
 */
public class ShortcutActivity extends BaseActivity {
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
        if (null != launcherBounds) {

            layoutParams.width = launcherBounds.width();
            layoutParams.height = launcherBounds.width();
            layoutParams.leftMargin =launcherBounds.left;
            layoutParams.topMargin = launcherBounds.top;
            layoutt.setLayoutParams(layoutParams);
        }
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShortcutActivity.this.finish();
                return false;
            }
        });

    }
}
