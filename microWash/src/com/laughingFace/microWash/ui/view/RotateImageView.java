package com.laughingFace.microWash.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.laughingFace.microWash.R;

/**
 * Created by zihao on 15-4-2.
 */
public class RotateImageView extends ImageView {


    private Context context;
    private Animation animation = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void prepareAnimation(AttributeSet attrs){

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.rotateImageView);
        int duration = array.getInteger(R.styleable.rotateImageView_duration,800);
        int repeatCount = array.getInteger(R.styleable.rotateImageView_repeatCount,-1);
        int from = array.getInteger(R.styleable.rotateImageView_fromDegrees,0);
        int to = array.getInteger(R.styleable.rotateImageView_toDegrees,360);
        int v = array.getInteger(R.styleable.rotateImageView_interpolator,0);
        animation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(repeatCount);
        animation.setDuration(duration);
        if (v == 1)
        animation.setInterpolator(new LinearInterpolator());
        ImageView iv = new ImageView(context);
        //iv.setBackground(this.getBackground());
        iv.setBackground(this.getBackground());
        iv.setMaxWidth(array.getInteger(R.styleable.rotateImageView_innerMaxWidth, 50));
        iv.setMaxHeight(array.getInteger(R.styleable.rotateImageView_innerMaxHeight,50));

        RotateAnimation animation1 = new RotateAnimation(from,to*-1, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);

        //iv.setAnimation(animation1);
        //iv.startAnimation(animation1);

        boolean start = array.getBoolean(R.styleable.rotateImageView_startOnLoad,true);
        if(start){
            animation.start();
        }else {
            animation.cancel();
        }

        array.recycle();
    }
    public RotateImageView(Context context) {
        super(context);
        this.context = context;
        prepareAnimation(null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        prepareAnimation(attrs);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        prepareAnimation(attrs);
    }

    public void startRotate(){
        if(null != animation){

            animation.start();
        }
    }
    public void stopRotate(){
        animation.cancel();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            this.clearAnimation();
        super.setVisibility(visibility);
    }

    /**
     *
     * @param ms 完成一次动画所需要的时间单位毫秒
     */
    public void setDuration(int ms){
        animation.setDuration(ms);
    }

    /**
     *
     * @param count 动画的重复次数 -1为永不停息
     */
    public void setRepeatCount(int count){
        animation.setRepeatCount(count);
    }


    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
