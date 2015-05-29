package com.laughingFace.microWash.ui.view;
        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.DragEvent;
        import android.view.MotionEvent;
        import android.view.View;
        import com.laughingFace.microWash.R;
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
    private boolean canDrag = true;
    private List<Circle> circles = new ArrayList<Circle>();
    private int waveCount = 5;//波波的总个数
    private int color = 0x00ce9b;//波的颜色

    private float breathDirection = 1;//呼吸方向（+1为变亮，-1为变暗）
    private float breathSpeed = 0.02f;//呼吸速度
    private boolean isBreathing = false;

    private OnCollisionListener onCollisionListener;


    public WaterRipplesView(Context context) {
        super(context);
        init(context,null);
    }

    public WaterRipplesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public WaterRipplesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        viewUtil = new ViewUtil(this);
        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

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

        /**
         * 获取xml的配置参数
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.waterRipplesView, 0, 0);
        try {
            waveCount = a.getInteger(R.styleable.waterRipplesView_waveCount, 3);
            isStarting = a.getBoolean(R.styleable.waterRipplesView_waveOnLoad,true);
            canDrag = a.getBoolean(R.styleable.waterRipplesView_canDrag,true);
            //isBreathing = a.getBoolean(R.styleable.waterRipplesView_breathing,true);
            color = a.getColor(R.styleable.waterRipplesView_waveColor,0x00ce9b);

        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(canDrag){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:

                    start();
                    this.startDrag(null, new DragShadowBuilder(this), null, 0);//按下本view即可拖动本view
                    stop();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private int a = 0;
    private float breath = 1f;//控制呼吸

    @Override
    public void onDraw(Canvas canvas) {

        float alphaSpeed;
        float radiusSpeed;
        float hw = getWidth()/2f;

        /**
         * 根据view的宽度计算半径和透明度的变化速度，（为了尽力保证半径和透明度同时达到最值）
         */
        if(hw >255f){
            radiusSpeed = hw/255f;
            alphaSpeed = 1f;
        }else {
            alphaSpeed = 255f/hw;
            radiusSpeed = 1f;
        }

        /**
         * 控制呼吸
         */
        if(isBreathing){
            breath=breath+breathSpeed*breathDirection;
            if(breath>1 ){
                breathDirection *= -1;//呼吸反向
                if(beforBreathIsAwave){
                    isBreathing = false;
                    isStarting = true;
                    breath = 1;
                }

            }else if(breath <0.001){
                breathDirection *= -1;//呼吸反向
                if(!beforBreathIsAwave){
                    isBreathing = false;
                    isStarting = false;
                    breath = 1;

                }
            }
        }


        /**
         * 当达到设定的波距或第一次运行时 添加一个新波
         */
        if (++a>= (hw/waveCount) || circles.size()<1){
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
            temp.setRadius(temp.getRadius() + radiusSpeed);//增加波的半径

            paint.setColor(temp.getColor());
            int tempAlpha = (int)(temp.getAlpha()*breath);//乘以breath是为了达到呼吸的效果
            paint.setAlpha(tempAlpha<0?0:tempAlpha);

            if(isStarting){
                canvas.drawCircle(temp.getX(), temp.getY(), temp.getRadius(), paint);//绘制波
            }

            /**
             * 当波的半径大于本控件的宽大时删除这个波
             */
            if( temp.getRadius() >getWidth() || temp.getAlpha() <0){
                circles.remove(temp);
            }
        }
        invalidate();
    }

    //地震波开始/继续进行
    public void start() {
        isStarting = true;
        invalidate();
    }

    //地震波暂停
    public void stop() {
        isStarting = false;
        invalidate();

    }

    public boolean isStarting() {
        return isStarting;
    }

    private boolean beforBreathIsAwave = true;

    public void breath(){
        this.beforBreathIsAwave =  isStarting;
        if(beforBreathIsAwave){
            breath = 1;
            breathDirection =-1;
        }
        else {
            breath = 0.01f;
            breathDirection = 1;
        }
        start();
        isBreathing = true;

    }

    /**
     * 代表每个波的类
     */
    public class Circle{
        private float x;
        private float y;
        private int color;
        private float alpha;
        private float radius;

        public float getRadius() {
            return radius;
        }

        public Circle setRadius(float radius) {
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

        public float getAlpha() {
            return alpha;
        }

        public Circle setAlpha(float alpha) {
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