package com.laughingFace.microWash.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.ui.view.RotateImageView;

/**
 * Created by zihao on 15-5-18.
 */
public abstract class CountDownDialog extends Dialog {
    private CountDownTimer timer;

    private TextView title;
    private TextView content;
    private Button changeModel;
    private final String DELAY_INFO = "倒计时%s秒开始";
    private LinearLayout delayStart;
    private LinearLayout waiteStart;

    public CountDownDialog(Context context) {
        this(context, 5, 1);
    }

    public CountDownDialog(final Context context, final int secondInFuture, final int interval) {
        super(context, R.style.MyDialog);
        init();
        timer = new CountDownTimer(secondInFuture*1000,interval*1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                content.setText(String.format(DELAY_INFO, millisUntilFinished / (interval * 1000)));
            }

            @Override
            public void onFinish() {
                waiteStart.setVisibility(View.VISIBLE);
                delayStart.setVisibility(View.INVISIBLE);
                onCounttingDownOver();
            }
        };
    }

    private void init(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.countdown_dialog);
        this.title = (TextView) findViewById(R.id.countdown_dialog_title);
        this.content =  (TextView) findViewById(R.id.countdown_dialog_content);
        this.changeModel = (Button) findViewById(R.id.change_model);
        this.waiteStart = (LinearLayout)findViewById(R.id.waite_start);
        this.delayStart = (LinearLayout)findViewById(R.id.delay_start);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);//按返回键也不消失
        changeModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                dismiss();
                onChangeModel();
            }
        });
    }

    public void start(){
        timer.start();
        waiteStart.setVisibility(View.INVISIBLE);
        delayStart.setVisibility(View.VISIBLE);
        this.show();
    }

    public CountDownDialog setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public  abstract void onCounttingDownOver();
    public abstract void onChangeModel();

    @Override
    public void dismiss() {
        waiteStart.setVisibility(View.INVISIBLE);
        delayStart.setVisibility(View.VISIBLE);
        super.dismiss();
    }
}
