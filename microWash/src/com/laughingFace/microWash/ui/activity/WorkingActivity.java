package com.laughingFace.microWash.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.laughingFace.microWash.R;

/**
 * Created by zihao on 15-5-25.
 */
public class WorkingActivity extends BaseActivity {

    private CountDownDialog countDownDialog;//用于倒计时的弹出对话框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working);

        Log.i("xixi", "WorkingActivity onCreate...");

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

        countDownDialog.setTitle("XX模式");

        //countDownDialog.start();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
