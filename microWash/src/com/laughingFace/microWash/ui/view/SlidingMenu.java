package com.laughingFace.microWash.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.laughingFace.microWash.R;

/**
 * Created by zihao on 15-4-20.
 */
public class SlidingMenu extends FrameLayout {

    private GestureDetector mGestureDetector;
    private View menu;
    private View content;
    private View menuContent;
    private int showingWidth =0;//菜单完全显示的宽度
    private int offsetX = 0;
    private boolean isShowing = false;
    private LinearLayout.LayoutParams layoutParams;
    private long currentbg = 0x00000000;//默认全透明
    private final long MINALPHA = 0x80;//透明度的最低值（值越高越透明 八位十六进制的最高两位代表透明度）
    private long maskColor =0x202020 ;//菜单出现后周围遮罩颜色
    private final int k = 3;//“劲度系数”控制菜单完全显示后继续拉动菜单的难度（参考胡克定律：f=k.x）
    private SlidingMenuListenear onMenuOpened;
    private SlidingMenuListenear onMenuClosed;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init( Context context,AttributeSet attrs){

        mGestureDetector = new GestureDetector(context,new MySimpleGL());
        this.setLongClickable(true);//没这句的话不能触发滑动事件
        this.post(new Runnable() {
            @Override
            public void run() {
                menu = SlidingMenu.this.findViewById(R.id.menu);
                content = SlidingMenu.this.findViewById(R.id.content);
                menuContent = SlidingMenu.this.findViewById(R.id.menu_content);
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
                            show();//完全显示菜单
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
            currentbg =  0x01000000l * (long)(MINALPHA*scale)+maskColor;
            if(currentbg > 0x01000000l * MINALPHA+maskColor){
                currentbg = 0x01000000l * MINALPHA+maskColor;
            }
            menu.setBackgroundColor((int) currentbg);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(offsetX <= 0){
                layoutParams.width= showingWidth;
                menuContent.setLayoutParams(layoutParams);
                offsetX = showingWidth;
                menu.scrollTo(offsetX, 0);
                menu.setBackgroundColor(0x00000000);
                if(null != onMenuClosed){
                    onMenuClosed.action();
                }
            }
            content.setVisibility(VISIBLE);
            return true;
        }
    }

    public SlidingMenuListenear getOnMenuOpened() {
        return onMenuOpened;
    }

    public void setOnMenuOpened(SlidingMenuListenear onMenuOpened) {
        this.onMenuOpened = onMenuOpened;
    }

    public SlidingMenuListenear getOnMenuClosed() {
        return onMenuClosed;
    }

    public void setOnMenuClosed(SlidingMenuListenear onMenuClosed) {
        this.onMenuClosed = onMenuClosed;
    }

    public void show(){
        offsetX = 0;//完全显示菜单
        menu.setBackgroundColor((int) (MINALPHA * 0x01000000 + maskColor));//背景设置为半透明
        content.setVisibility(INVISIBLE);
        if (null != onMenuOpened) {
            onMenuOpened.action();
        }

        layoutParams.width = showingWidth;
        menuContent.setLayoutParams(layoutParams);
        menu.scrollTo(offsetX, 0);
        isShowing = true;
    }

    public void hide(){
        offsetX = showingWidth;//隐藏菜单
        menu.setBackgroundColor(0x00000000);//背景设置为全透明
        content.setVisibility(VISIBLE);

        layoutParams.width = showingWidth;
        menuContent.setLayoutParams(layoutParams);
        menu.scrollTo(offsetX, 0);
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface SlidingMenuListenear{
        public void action();
    }
}
