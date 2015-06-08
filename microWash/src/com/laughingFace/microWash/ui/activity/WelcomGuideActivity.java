package com.laughingFace.microWash.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.laughingFace.microWash.R;

import java.util.ArrayList;

/**
 * @author dwtedx
 *	功能描述：主程序入口类,欢迎界面
 */
public class WelcomGuideActivity extends Activity implements OnClickListener,OnPageChangeListener {
	//定义ViewPager对象
	private ViewPager viewPager;
	
	//定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;
	
	//定义一个ArrayList来存放View
	private ArrayList<View> views;

	//引导图片资源
    private static final int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3,R.drawable.guide4};
    
    //底部小点的图片
    private ImageView[] points;
    
    //记录当前选中位置
    private int currentIndex;

    private Button begin;

    private SharedPreferences preferences;
     SharedPreferences.Editor editor;

    final String SETTING_NAME = "microWashSettings";
    final String SETTING_ISFIRST = "isFirst";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(SETTING_NAME,MODE_APPEND);
         editor = preferences.edit();
        boolean isFirst = true;
        isFirst = preferences.getBoolean(SETTING_ISFIRST,true);
        Log.i("gg","isFirst:"+isFirst);
        /**
         * 不是第一次启动软件直接跳到设备界面
         */
        if(!isFirst){
            startActivity(new Intent(WelcomGuideActivity.this, DeviceActivity.class));
            this.finish();
            return;
        }
        setContentView(R.layout.welcom_guide);


		initView();
		initData();
	}

	/**
	 * 初始化组件
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView(){

		//实例化ArrayList对象
		views = new ArrayList<View>();
		
		//实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);


		//实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){

        LayoutInflater inflater = getLayoutInflater();
        View v1 = inflater.inflate(R.layout.welcom_first, null);
        View v2 = inflater.inflate(R.layout.welcom_second, null);
        View v3 = inflater.inflate(R.layout.welcom_third, null);

        views.add(v1);
        views.add(v2);
        views.add(v3);

        begin = (Button)v3.findViewById(R.id.begin);
        begin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomGuideActivity.this, DeviceActivity.class));
                WelcomGuideActivity.this.finish();
                /**
                 * 保存软件设置-已经不是第一次启动了以后不再显示欢迎界面了
                 */
                editor.putBoolean(SETTING_ISFIRST, false);
                editor.commit();
            }
        });
		//定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                														  LinearLayout.LayoutParams.MATCH_PARENT);
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
		
        points = new ImageView[views.size()];

        //循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
        	//得到一个LinearLayout下面的每一个子元素
        	points[i] = (ImageView) linearLayout.getChildAt(i);
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * 当滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	
	/**
	 * 当当前页面被滑动时调用
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
        Log.i("hh","position:"+position+"len:"+pics.length);
        if(pics.length-1 == position){
            begin.setVisibility(View.VISIBLE);
        }
	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		 int position = (Integer)v.getTag();
         setCurView(position);
         setCurDot(position);		
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position){
         if (position < 0 || position >= pics.length) {
             return;
         }
         viewPager.setCurrentItem(position);
     }

     /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
         if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
             return;
         }
         points[positon].setEnabled(false);
         points[currentIndex].setEnabled(true);

         currentIndex = positon;
     }
	
}
