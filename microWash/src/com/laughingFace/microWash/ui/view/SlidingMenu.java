package com.laughingFace.microWash.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.laughingFace.microWash.R;

/**
 * Created by zihao on 15-4-20.
 */
public class SlidingMenu extends FrameLayout {

    private GestureDetector mGestureDetector;
    private View menu;
    private View menuContent;
    private int showingWidth =0;//菜单完全显示的宽度
    private int offsetX = 0;
    private boolean isShowing = false;
    private LinearLayout.LayoutParams layoutParams;
    private final int k = 3;//“劲度系数”控制菜单完全显示后继续拉动菜单的难度（参考胡克定律：f=k.x）
    private SlidingMenuListenear slidingMenuListenear;

    private ImageView menuHandle;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init( Context context,AttributeSet attrs){

        mGestureDetector = new GestureDetector(context,new MySimpleGL());
        this.setLongClickable(true);//没这句的话不能触发滑动事件
        this.post(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                menu = SlidingMenu.this.findViewById(R.id.menu);
                menuContent = SlidingMenu.this.findViewById(R.id.menu_content);
                menuHandle = (ImageView) SlidingMenu.this.findViewById(R.id.menu_handle);
                /**
                 * 点击把手显示/关闭菜单
                 */
                menuHandle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isShowing()) {
                            SlidingMenu.this.hide();
                        } else {
                            SlidingMenu.this.show();
                        }

                    }
                });
                showingWidth = menuContent.getWidth();
                layoutParams = (LinearLayout.LayoutParams) menuContent.getLayoutParams();
                layoutParams.width = showingWidth;
                layoutParams.weight = 0;
                offsetX = showingWidth;
                menu.scrollTo(offsetX, 0);
            }
        });

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (offsetX < showingWidth / 2) {
                            show();
                        } else {
                            hide();
                        }
                        break;
                }
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    private class MySimpleGL extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            offsetX += distanceX;
            if(offsetX >= showingWidth){
                offsetX = showingWidth;
            }

            if(offsetX>=0){
                menu.scrollTo(offsetX, 0);
            }
            else {
                layoutParams.width-= distanceX/k;
                menuContent.setLayoutParams(layoutParams);
            }

            /**
             * 通过scale这个比例计算出当前背景的透明度
             */
            float scale = ((float)showingWidth - (float)offsetX) / (float)showingWidth;//菜单显示出来部分宽度与总宽度之比

            SlidingMenu.this.setAlpha(0.5f+scale);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            hide();
            /*if(offsetX <= 0){
                layoutParams.width= showingWidth;
                menuContent.setLayoutParams(layoutParams);
                offsetX = showingWidth;
                menu.scrollTo(offsetX, 0);
                if(null != slidingMenuListenear){
                    slidingMenuListenear.onClosed();
                }
            }*/
            return true;
        }
    }

    /**
     * 完全显示菜单
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void show(){
        offsetX = 0;//完全显示菜单
        if (null != slidingMenuListenear) {
            slidingMenuListenear.onOpened();
        }

        layoutParams.width = showingWidth;
        menuContent.setLayoutParams(layoutParams);
        menu.scrollTo(offsetX, 0);
        isShowing = true;
    }

    /**
     * //隐藏菜单
     */
    public void hide(){
        offsetX = showingWidth;
        layoutParams.width = showingWidth;
        menuContent.setLayoutParams(layoutParams);
        menu.scrollTo(offsetX, 0);
        isShowing = false;
        if(null != slidingMenuListenear){
            slidingMenuListenear.onClosed();
        }
    }

    public SlidingMenuListenear getSlidingMenuListenear() {
        return slidingMenuListenear;
    }

    public void setSlidingMenuListenear(SlidingMenuListenear slidingMenuListenear) {
        this.slidingMenuListenear = slidingMenuListenear;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface SlidingMenuListenear{
        void onClosed();
        void onOpened();
    }
}
