package com.laughingFace.microWash.ui.view;
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.util.AttributeSet;
        import android.view.DragEvent;
        import android.view.MotionEvent;
        import android.view.View;
        import com.laughingFace.microWash.util.ViewUtil;

        import java.util.ArrayList;
        import java.util.List;

/**
 * 水波纹效果
 * @author tony
 *
 */

public class WaterRipplesView extends View {
    private Paint paint;
    ViewUtil viewUtil;
    private boolean isStarting = true;
    private List<Circle> circles = new ArrayList<Circle>();
    private int alphaSpeed = 4;//每个波颜色的透明值递减速度，值越大产生的每个波的可见最大半径越小(1为最大)
    private int roundWidth = 20;//每个波之间的间隔距离,值越小波的闪动平率越高（没有单位）
    private int color = 0x00ce9b;//波的颜色

    private OnCollisionListener onCollisionListener;


    public WaterRipplesView(Context context) {
        super(context);
        init();
    }

    public WaterRipplesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterRipplesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        viewUtil = new ViewUtil(this);
        setOnDragListener(new OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
               // WaterRipplesView.this.layout(ll, tt, rr, bb);
                WaterRipplesView.this.postInvalidate();

                if (null == onCollisionListener) {
                    return false;
                }

                switch (event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:

                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        onCollisionListener.onEnter(v, WaterRipplesView.this);
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        onCollisionListener.onMove(v, WaterRipplesView.this);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        onCollisionListener.onLeave(v, WaterRipplesView.this);

                        break;
                    case DragEvent.ACTION_DROP:
                        onCollisionListener.onRealse(v, WaterRipplesView.this);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        onCollisionListener.onRealse(v, null);
                        break;
                    default:


                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    this.startDrag(null, new View.DragShadowBuilder(this), null, 0);//按下本view即可拖动本view
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        return super.onTouchEvent(event);
    }

    static int a = 0;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //刷新界面
        invalidate();
        if(!isStarting){
            return;
        }

        /**
         * 当达到设定的波距或第一次运行时 添加一个新波
         */
        if (++a>= roundWidth || circles.size()<1){
            a = 0;
        Circle c = new Circle();
        c.setX(getWidth() / 2).setY(getHeight() / 2).setColor(color).setAlpha(255).setRadius(1);
        circles.add(c);
        }


        for (int i= 0;i< circles.size();i++){
            Circle temp = circles.get(i);
            temp.setAlpha(temp.getAlpha() - alphaSpeed);//改变波的透明度
            if(temp.getAlpha() <0){
                temp.setAlpha(0);
            }
            temp.setRadius(temp.getRadius() + 1);//增加波的半径

            paint.setColor(temp.getColor());
            paint.setAlpha(temp.getAlpha());
            canvas.drawCircle(temp.getX(), temp.getY(), temp.getRadius(), paint);//绘制波
          //  Log.i("haha", "x:" + temp.getX() + " Y:" + temp.getY());

            /**
             * 当波的半径大于本控件的宽大时删除这个波
             */
            if( temp.getRadius() >getWidth()){
                circles.remove(temp);
            }
        }

        return;
    }

    //地震波开始/继续进行
    public void start() {
        isStarting = true;
    }

    //地震波暂停
    public void stop() {
        isStarting = false;
    }

    public boolean isStarting() {
        return isStarting;
    }

    /**
     * 代表每个波的类
     */
    public class Circle{
        private float x;
        private float y;
        private int color;
        private int alpha;
        private int radius;

        public int getRadius() {
            return radius;
        }

        public Circle setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public float getX() {
            return x;
        }

        public Circle setX(float x) {
            this.x = x;
            return this;
        }

        public float getY() {
            return y;
        }

        public Circle setY(float y) {
            this.y = y;
            return this;
        }

        public int getColor() {
            return color;
        }

        public Circle setColor(int color) {
            this.color = color;
            return this;
        }

        public int getAlpha() {
            return alpha;
        }

        public Circle setAlpha(int alpha) {
            this.alpha = alpha;
            return this;
        }
    }
    public OnCollisionListener getOnCollisionListener() {
        return onCollisionListener;
    }

    public void setOnCollisionListener(OnCollisionListener onCollisionListener) {
        this.onCollisionListener = onCollisionListener;
    }

    public interface OnCollisionListener{
        void onLeave(View perpetrators,View wounder);
        void onMove(View perpetrators,View wounder);
        void onEnter(View perpetrators,View wounder);
        void onRealse(View perpetrators,View wounder);
    }

}