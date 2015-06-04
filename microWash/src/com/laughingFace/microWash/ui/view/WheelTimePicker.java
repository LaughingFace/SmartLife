package com.laughingFace.microWash.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.laughingFace.microWash.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zihao on 15-6-3.
 */
public class WheelTimePicker extends Dialog {

    private int currentHour = 0;
    private int currentMinute = 0;

    private LinearLayout pickTime;
    private LinearLayout waitStart;

    private PickerView hour;
    private PickerView minute;
    private ImageView ok;
    private ImageView cancel;

    private TimePickerListener timePickerListener;

    public WheelTimePicker(Context context) {
        super(context, R.style.MyDialog);
        this.setContentView(R.layout.wheel_tim_picker);

        pickTime = (LinearLayout)findViewById(R.id.pick_time);
        waitStart = (LinearLayout)findViewById(R.id.waite_start);
        pickTime.setVisibility(View.VISIBLE);
        waitStart.setVisibility(View.INVISIBLE);

        ok = (ImageView)findViewById(R.id.timePicker_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != timePickerListener){
                    timePickerListener.onSelected(getTotalMinute());
                }
                pickTime.setVisibility(View.INVISIBLE);
                waitStart.setVisibility(View.VISIBLE);
            }
        });

        cancel = (ImageView)findViewById(R.id.timePicker_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(null != timePickerListener){
                    timePickerListener.onCancel();
                }
            }
        });

        hour = (PickerView) findViewById(R.id.hour);
         List<String> hours = new ArrayList<String>();
        for (int i = 1; i < 13; i++)
        {

            hours.add(i < 10 ? "0" + i : "" + i);
        }
        hour.setData(hours);
      //  hour.setSelected(0);
        hour.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currentHour = Integer.parseInt(text);
            }
        });

        minute = (PickerView) findViewById(R.id.minute);
        List<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++)
        {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }
        minute.setData(minutes);
       // minute.setSelected(0);
        minute.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currentMinute = Integer.parseInt(text);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        pickTime.setVisibility(View.VISIBLE);
        waitStart.setVisibility(View.INVISIBLE);
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentMinute(int currentMinute) {
        this.currentMinute = currentMinute;
    }

    public TimePickerListener getTimePickerListener() {
        return timePickerListener;
    }

    public void setTimePickerListener(TimePickerListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    public long getTotalMinute(){
        long total = currentHour*60 + currentMinute;
        return total;
    }

    public interface TimePickerListener{
        void onSelected(long minutes);
        void onCancel();
    }
}
