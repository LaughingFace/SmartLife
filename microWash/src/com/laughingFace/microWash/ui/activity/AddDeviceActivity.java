package com.laughingFace.microWash.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.devicesDispatcher.ModelManager;
import com.laughingFace.microWash.net.NetworkManager;
import com.laughingFace.microWash.net.UdpSocket;
import com.laughingFace.microWash.receiver.WifiStateReceiver;
import com.laughingFace.microWash.smartConnect.IoTManagerNative;
import com.laughingFace.microWash.util.DisplayUtil;
import com.laughingFace.microWash.util.wifi.WifiAdmin;
import com.laughingFace.microWash.util.wifi.WifiCfg;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mathcoder on 7/04/15.
 */
public class AddDeviceActivity extends BaseActivity {
    private static final String TAG = "addDeviceActivity";
    public static final int REQUEST_CODE = 9234;
    private EditText etType;
    private EditText etSsid;
    private EditText etPwd;
    private ImageView IVChangePwdShow;
    private Context mContext;
    private ImageView ivCancel;
    private ImageView ivOk;
    private WifiAdmin wifiAdmin;
    private TextView tvTitle ;
    private LinearLayout llCfgWifi;
    private ImageView rivLoading;
    private final int PROCESS_VALID_WIFI = 1;
    private final int PROCESS_TO_AP = 2;
    private final int PROCESS_FINISH = 3;
    private boolean isConnecting = false;
    private boolean isHandler = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddevice);

        mContext = this;

        ///初始化wifi,在视图之前初始化
        wifiAdmin = new WifiAdmin(mContext);
        wifiAdmin.openWifi();

        //初始化视图。
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ModelManager.getInstance().stopAngel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModelManager.getInstance().startAngel();

    }

    private void init() {
        llCfgWifi = (LinearLayout) this.findViewById(R.id.ll_cfg_wifi);
        rivLoading = (ImageView) this.findViewById(R.id.riv_loading);
        etType = (EditText) this.findViewById(R.id.et_type);
        etSsid = (EditText) this.findViewById(R.id.et_ssid);
        etPwd = (EditText) this.findViewById(R.id.et_pwd);
        ivCancel = (ImageView) this.findViewById(R.id.iv_cancel);
        ivOk = (ImageView) this.findViewById(R.id.iv_ok);
        tvTitle = (TextView) findViewById(R.id.tv_add_device_title);
        IVChangePwdShow = (ImageView) findViewById(R.id.iv_change_pwd_show);
        setType("微洗");
        setSsidPwd(wifiAdmin.getSSID(), wifiAdmin.getBSSID());
        IVChangePwdShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPwd.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPwd.setSelection(etPwd.getText().toString().length());
            }
        });
        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = new ArrayList<String>();
                if (data.size() > 0)
                    showItem(data, v);
            }
        });

        etSsid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = wifiAdmin.getScanWifi();
                if (data.size() >= 1)

                    showItem(data, v);
            }
        });

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnecting = true;
                Log.i("haha", wifiAdmin.getRouterIP());
                //等待界面。。。。
                changeShow();

                //验证用户输入wifi的账号密码可行性。
                wifiAdmin.connect(etSsid.getText().toString(), etPwd.getText().toString(), wifiStateListener, PROCESS_VALID_WIFI);
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnecting)
                {
                    changeShow();
                }
                else
                {
                    finish();
                }

                isConnecting = false;

            }
        });
    }

    private void setType(String type)
    {
        //设置类型，之后操作数据库和网络。
        etType.setText(type);
    }
    /**
     * 设置家用路由器的ssid和pwd
     * @param ssid
     * @param mac
     */
    private void setSsidPwd(String ssid,String mac)
    {
//        List<HomeRouteTab> home = DataSupport.where("ssid = ?",ssid)
//
//                                            .find(HomeRouteTab.class);
//        if (StringUtil.isNotEmpty(home)) {
//            //填充密码并显示
//            etSsid.setText(home.get(0).getSsid());
//            etPwd.setText(home.get(0).getPwd());
////            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//        }
//        else
//        {
            etSsid.setText(ssid);
            etPwd.setText("");
//            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        }

    }

    private void changeShow()
    {
        rivLoading.clearAnimation();
        if (rivLoading.getVisibility() == View.GONE){
            RotateAnimation animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setRepeatCount(-1);
            animation.setDuration(2000);
            animation.setInterpolator(new LinearInterpolator());
            rivLoading.setAnimation(animation);

        }
        int visibility = llCfgWifi.getVisibility();
        llCfgWifi.setVisibility(rivLoading.getVisibility());
        ivOk.setVisibility(rivLoading.getVisibility());
        rivLoading.setVisibility(visibility);
//        llCfgWifi.setVisibility(View.INVISIBLE);
//        rivLoading.setVisibility(View.VISIBLE);
    }
    private void showItem(final List<String> data,final View v)
    {

        //初始化弹出框
        final ListView lvData = new ListView(getApplication().getApplicationContext());
        lvData.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(mContext, 50)));
        ItemAdapter adapter = new ItemAdapter(mContext,data);
        lvData.setAdapter(adapter);
        lvData.setCacheColorHint(0);
        lvData.setSelector(android.R.color.transparent);

        lvData.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.select_item_bg));
        final PopupWindow popupWindow = new PopupWindow(lvData,
                v.getWidth(), LayoutParams.WRAP_CONTENT, true);

        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.tran));
        popupWindow.getContentView().measure(0, 0);

        popupWindow.showAsDropDown(v);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText et = (EditText) v;
                et.setText(data.get(position));
                setSsidPwd(data.get(position),"");
                popupWindow.dismiss();
            }
        });
    }
    private final class ItemAdapter extends BaseAdapter {
        List<String> mData;
        Context mContext;
        public ItemAdapter(Context context,List<String> data)
        {
            this.mData = data;
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tvItem.setText(mData.get(position));
            return convertView;
        }
        private class ViewHolder {
            TextView tvItem;
        }
    }
    private WifiStateReceiver.OnWifiStateListener wifiStateListener = new WifiStateReceiver.OnWifiStateListener() {
        @Override
        public void OnConnected(int id) {
            if (!isConnecting)
                return;
            wifiAdmin.updateWifiInfo();
            switch(id)
            {
                case PROCESS_VALID_WIFI:
                    Log.i("haha", "wifi验证通过");
                    //连接至wifi模块AP,通过机型来判定
//                    HomeRouteTab home = new HomeRouteTab();
//                    home.setMac(wifiAdmin.getBSSID());
//                    home.setSsid(etSsid.getText().toString());
//                    home.setPwd(etPwd.getText().toString());
//                    DBHelper.Save(home,HomeRouteTab.class);
                    List<String> data = wifiAdmin.getScanWifi();
//                    if(!data.contains(getResources().getString(R.string.wifi_ap_ssid)))
//                    {
//                        wifiStateListener.OnDisConnected(PROCESS_TO_AP);
//                    }
//                    else
//                    {
//                        wifiAdmin.connect(getResources().getString(R.string.wifi_ap_ssid), getResources().getString(R.string.wifi_ap_pwd),wifiStateListener,PROCESS_TO_AP);
//                    }
                    if(!data.contains(etSsid.getText().toString()))
                    {
                        wifiStateListener.OnDisConnected(PROCESS_TO_AP);
                    }
                    else
                    {
                        wifiAdmin.connect(etSsid.getText().toString(), etPwd.getText().toString(),wifiStateListener,PROCESS_TO_AP);
                    }
//                    wifiAdmin.connect(etSsid.getText().toString(), getResources().getString(R.string.wifi_ap_pwd),wifiStateListener,PROCESS_TO_AP);
                    break;
                case PROCESS_TO_AP:
//                    //发送ssid,pwd信息
//                    Log.i("haha", "成功连接至wifi_ap");
//                    //校验wifi模块返回的配置信息。
//                    Log.i("haha", WifiCfg.getJson(etSsid.getText().toString(), etPwd.getText().toString()));
////                    dispatcher.sendMessage(WifiCfg.getJson(etSsid.getText().toString(), etPwd.getText().toString()), wifiAdmin.getRouterIP(), mContext.getResources().getInteger(R.integer.tcp_config_port));
//
//                            Log.i("haha", "发送配置信息");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UdpSocket udpSocket = new UdpSocket(0,"255.255.255.255",7878);
//                            udpSocket.send(WifiCfg.getJson(etSsid.getText().toString(), etPwd.getText().toString()).getBytes());
//
//                            try {
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            wifiAdmin.connect(etSsid.getText().toString(), etPwd.getText().toString(), wifiStateListener, PROCESS_FINISH);
//                            Log.i("haha", "重新连接原ap");
//                        }
//                    }).start();
                    IoTManagerNative iot = new IoTManagerNative();
                    iot.InitSmartConnection();

                    String mac = "";
                    for (ScanResult wifi : wifiAdmin.getWifiList())
                    {
                        if (wifi.SSID.equals(etSsid.getText().toString()))
                        {
                            mac = wifi.BSSID;
                        }
                    }
                    Log.i("xixi", etSsid.getText().toString() +",pwd:"+
                            etPwd.getText().toString()+",mac:"+mac);
                    iot.StartSmartConnection(etSsid.getText().toString(),
                            etPwd.getText().toString(), "", (byte) 0);
                    try {
                        Thread.sleep(5000);
                        iot.StopSmartConnection();
                        wifiStateListener.OnConnected(PROCESS_FINISH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case PROCESS_FINISH:
                    Log.i("haha", "配置完成");
                    changeShow();
                    ivOk.setVisibility(View.VISIBLE);
                    setResult(REQUEST_CODE);
                    finish();
                    isConnecting = false;
                    break;
            }
        }

        @Override
        public void OnDisConnected(int id) {
            switch(id)
            {
                case PROCESS_VALID_WIFI:
                    Log.i("haha", "校验wifi信息失败");
                    changeShow();
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPwd.setTextColor(Color.RED);
                    isConnecting = false;
                    break;
                case PROCESS_TO_AP:
                    Log.i("haha", "连接至ap失败");
                    changeShow();
                    isConnecting = false;
                    break;
                case PROCESS_FINISH:

                    break;
            }
        }
    };
}
