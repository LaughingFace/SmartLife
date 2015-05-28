package com.laughingFace.microWash.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.laughingFace.microWash.util.CircleDetection;


/**
 * Created by zihao on 15-4-3.
 */
public class DragableFrameLayout extends FrameLayout {

    private int mode ;
    private CircleDetection checkRegion;
    private CircleDetection.OnContainListener onContainActionListener;

    private boolean isContain = false;

    public DragableFrameLayout(Context context) {
        super(context);
    }

    public DragableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DragableFrameLayout setMode(int id)
    {
        this.mode = id;
        return this;
    }
    public int getMode()
    {
        return this.mode;
    }


    public static final String Tag = "CreatView";
    int lastX = -100;
    int lastY = -100;
    int startLeft = 0;
    int startTop = 0;
    int startRight = 0;
    int startBottom = 0 ;
    int ll;
    int rr;
    int tt;
    int bb;
    private int centerX;//绝对位置
    private int centerY;//绝对位置。
    private int[] location = new int[2];//x,y绝对坐标
    private int maxe = 10;//最大偏转量。
    private int[] locE = new int[2];
    boolean isFirst = true;
    public void setLastXY(int x,int y)
    {
        this.lastX = x;
        this.lastY = y;
    }

    public void reLocation()
    {
        if (startLeft != 0) {
            this.layout(startLeft, startTop, startRight, startBottom);
            isFirst = true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                startLeft = this.getLeft();
                startTop = this.getTop();
                startRight = this.getRight();
                startBottom = this.getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isFirst)
                {
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();

                    startLeft = this.getLeft();
                    startTop = this.getTop();
                    startRight = this.getRight();
                    startBottom = this.getBottom();
                    isFirst = false;
                }

                locE[0] = (int) event.getRawX() - lastX;
                locE[1] = (int) event.getRawY() - lastY;
                eRepair(locE,event);//合成两个方向变化量。


                if(Math.abs(locE[0])>10|| Math.abs(locE[1]) >10) {
                     ll = this.getLeft() + locE[0];
                     tt = this.getTop() + locE[1];
                     rr = this.getRight() + locE[0];
                     bb = this.getBottom() + locE[1];

                    /**
                     * 边缘检测
                     */
                    /*if (ll < 0) {
                        ll = 0;
                        rr =this.getWidth();
                    }
                    if(tt<0){
                        tt = 0;
                        bb = this.getHeight();
                    }*/

                    //this.layout(ll, tt, rr, bb);
                    this.scrollTo((int) event.getX(), (int) event.getY());
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    //this.postInvalidate();

                }

                break;
            case MotionEvent.ACTION_UP:
                isFirst = true;
                lastX = -100;
                lastY = -100;
                this.layout(startLeft, startTop, startRight, startBottom);
                if (null != checkRegion)
                    isContain = checkRegion.contains((int)event.getRawX(),(int)event.getRawY());

                this.postInvalidate();

                /**
                 * 当发生碰撞并设置了onContainActionListener 就回调需要执行的动作
                 */
                if(isContain && null  != onContainActionListener){
                    onContainActionListener.contain();
                    isContain = false;
                }

                Log.i("hahah",isContain+"");
                break;
        }
        return true;
    }

    public void setOnContainActionListener(CircleDetection.OnContainListener onContainActionListener) {
        this.onContainActionListener = onContainActionListener;
    }

    public CircleDetection getCheckRegion() {
        return checkRegion;
    }

    public void setCheckRegion(CircleDetection checkRegion) {
        this.checkRegion = checkRegion;
    }

    public void eRepair(int[] locE,MotionEvent e)
    {
        this.getLocationOnScreen(location);
        centerX = location[0] + this.getWidth()/2;
        centerY = location[1] + this.getHeight()/2;
        locE[0] += (e.getRawX()-centerX) % maxe;
        locE[1] += (e.getRawY()-centerY) % maxe;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DragableFrameLayout)
        {
            if (this.mode + 1 == ((DragableFrameLayout) o).mode+1)
                return true;
        }
        return false;
    }
}
