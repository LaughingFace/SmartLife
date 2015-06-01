package com.laughingFace.microWash.PostmanTest;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelProvider;
import com.laughingFace.microWash.ui.activity.BaseActivity;
import com.laughingFace.microWash.ui.view.ProgressBtn;
import com.laughingFace.microWash.util.Log;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Process;

public class DeviceActivityTest extends BaseActivity implements View.OnClickListener,View.OnTouchListener{
    private ImageButton nextPage;
    private Intent intent;
    Button btnConnect = null;
    Button a;
    LinearLayout aa;
    LinearLayout bb;
    ProgressBtn stand;
    ProgressBtn dyoff;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case 1:
                    btnConnect.setText("已连接");
                    btnConnect.setTextColor(Color.WHITE);

                    break;
                case 2:
                    btnConnect.setText("未连接");
                    btnConnect.setTextColor(Color.RED);
                    break;

            }
        }
    };
    float lastx = 0;
    float lasty = 0;
    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testactivity);

//        intent = new Intent(this,WorkingActivity.class);
//        nextPage = (ImageButton)findViewById(R.id.nextPage);
//
//        nextPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(intent);
//            }
//        });
        a = (Button) findViewById(R.id.closedoor);
        a.setOnClickListener(this);
        a.setOnTouchListener(this);
        View v = findViewById(R.id.opendoor);v.setOnClickListener(this);
        findViewById(R.id.opendoor).setOnTouchListener(this);
        dyoff = (ProgressBtn) findViewById(R.id.dryoff);
        dyoff.setOnClickListener(this);
        findViewById(R.id.dryoff).setOnTouchListener(this);

        stand = (ProgressBtn) findViewById(R.id.standard);
        stand.setOnClickListener(this);
        findViewById(R.id.standard).setOnTouchListener(this);

        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.stop).setOnTouchListener(this);

        btnConnect = (Button) findViewById(R.id.connect);

        aa = (LinearLayout) findViewById(R.id.aa);

        aa.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
//                Log.i("xixi", "" + event.getX());
//                if (lastx == 0 || lasty == 0)
//                {
//                    lastx = event.getX();
//                    lasty = event.getY();
//                    return true;
//                }
//               float dx = event.getX() - lastx;
//                float dy = event.getY() -lasty;
//                lastx = event.getX();
//                lasty = event.getY();
//                a.layout((int)(a.getLeft()+dx),(int)(a.getTop()+dy),(int)(a.getRight()+dx), (int) (a.getBottom()+dy));
                return true;
            }
        });


    }


    @Override
    public void onLine(Device device) {
        super.onLine(device);
        handler.obtainMessage(1).sendToTarget();

    }

    @Override
    public void offLine() {
        super.offLine();
      handler.obtainMessage(2).sendToTarget();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    static int color = 0;
    static int color2 = 0;

    @Override
    public void onFinish(Model model) {
        super.onFinish(model);
        if(model.getStateCode() == 3)
        {
            handler2.obtainMessage(3).sendToTarget();
        }
        else
        {
            handler2.obtainMessage(4).sendToTarget();
        }
    }

    @Override
    public void onProcessing(Model model) {
        super.onProcessing(model);
        if (model.getStateCode() == 3)
        {
            if (color == 0)
            {
                color = stand.getDrawingCacheBackgroundColor();
            }
            stand.progress(model.getProgress().getPercentage(),Color.parseColor("#0080FF"), color);
            handler2.obtainMessage(1).sendToTarget();
        }else
        {
            if (color2 == 0)
            {
                color2 = dyoff.getDrawingCacheBackgroundColor();
            }
            dyoff.progress(model.getProgress().getPercentage(),Color.parseColor("#0080FF"), color);
            handler2.obtainMessage(2).sendToTarget();
        }
    }
    Handler handler2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1)
            {
                stand.invalidate();
            }
            else if (msg.what == 2)
            {
                dyoff.invalidate();
            }
            else if (msg.what == 3)
            {
                stand.finish();
                stand.invalidate();

            }
            else if (msg.what == 4)
            {
                dyoff.finish();
                dyoff.invalidate();

            }
        }
    };
    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.closedoor:
//                Log.i("xixi", "close door");
                String a = null;
                a.length();
                modelManager.startModel(ModelProvider.closeDoor());
                break;
            case R.id.opendoor:
                Log.i("xixi","json:"+getDeviceInfo(this));
//                Log.i("xixi", "open door");
                modelManager.startModel(ModelProvider.openDoor());
                break;
            case R.id.dryoff:
//                Log.i("xixi","dryoff");
                modelManager.startModel(ModelProvider.dryoff());
                break;
            case R.id.standard:
//                Log.i("xixi","standard");
                modelManager.startModel(ModelProvider.standard());
                break;
            case R.id.stop:
//                Log.i("xixi","stop");
                modelManager.startModel(ModelProvider.stop());

                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                v.setAlpha(0.75f);
                break;
            case MotionEvent.ACTION_UP:
                v.setAlpha(1f);
                break;
        }

        return false;
    }
    public void progress(View v,int i)
    {
        Paint shaderPaint = new Paint();

        LinearGradient gradient = new LinearGradient(0, 0, 100, 100, Color.RED, Color.YELLOW, Shader.TileMode.MIRROR);
        shaderPaint.setShader(gradient);
    }
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
