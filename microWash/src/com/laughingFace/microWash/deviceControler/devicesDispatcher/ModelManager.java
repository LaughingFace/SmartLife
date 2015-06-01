package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import android.os.Handler;
import android.os.Message;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.device.DeviceAngel;
import com.laughingFace.microWash.deviceControler.model.CmdProvider;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.utils.Timer;
import com.laughingFace.microWash.util.Log;

public class ModelManager extends ModelAngel implements DeviceMonitor {
    private final static int HANDLER_ON_START = 1;
    private final static int HANDLER_FAIL_ON_START  = 2;
    private final static int HANDLER_ON_FINISH = 3;
    private final static int HANDLER_PROCEING = 4;
    private final static int HANDLER_ONLINE = 5;
    private final static int HANDLER_OFFLINE = 6;
    private static final int HANDLER_ON_INTERUPT = 7;

    private static ModelManager instance;
    private DeviceAngel deviceAngel;
    private DeviceMonitor deviceMonitor;
    private Device onLineDevice;
    private Timer.OnTimingActionListener onTimingActionListener;
    private Timer timer;
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
             switch(msg.what)
             {
                 case HANDLER_ON_START:
                     deviceMonitor.onModelStart(getRunningModel(), (StartType) msg.obj);
                     break;
                 case HANDLER_FAIL_ON_START:
                     deviceMonitor.faillOnStart(getRunningModel(), (StartFaillType) msg.obj);
                     break;
                 case HANDLER_ON_FINISH:
                     deviceMonitor.onFinish(getRunningModel());
                     break;
                 case HANDLER_PROCEING:
                     deviceMonitor.onProcessing(getRunningModel());
                     break;
                 case HANDLER_OFFLINE:
                     deviceMonitor.offLine();
                     break;
                 case HANDLER_ONLINE:
                     deviceMonitor.onLine((Device) msg.obj);
                     break;
             }
        }
    };
    public DeviceMonitor getDeviceMonitor() {
        return deviceMonitor;
    }

    public void setDeviceMonitor(DeviceMonitor deviceMonitor) {
        this.deviceMonitor = deviceMonitor;
    }

    public static ModelManager getInstance() {
        if(null == instance){
            instance = new ModelManager();
        }
        return instance;
    }

    private ModelManager(){
        setModelStateListener(this);
        deviceAngel = new DeviceAngel();
        deviceAngel.setDeviceStateListener(this);
        deviceAngel.searchDevice();
        onTimingActionListener = new Timer.OnTimingActionListener() {
            @Override
            public void befor() {
            }

            @Override
            public void action() {
                Log.i("xixi", "mypro" + timer.getInterval() * timer.getCurt());
                getRunningModel().getProgress().setRemain(getRunningModel().getProgress().getTotal()-timer.getInterval()*timer.getCurt());

                mHandler.obtainMessage(HANDLER_PROCEING).sendToTarget();
            }

            @Override
            public void after() {
                if (timer != null)
                notifyFinish();
            }
        };

    }
    public void startAngel()
    {
        deviceAngel.searchDevice();
    }
    public void stopAngel(){
        deviceAngel.stopSearchDevice();
    }
    @Override
    public void startModel(Model model) {
        if (null != onLineDevice)
        {
            if (null == getRunningModel())
            {
                super.startModel(model);
            }
            else
            {
                mHandler.obtainMessage(HANDLER_ON_START,
                        model == getRunningModel()?
                                StartType.AlreadyRunning:StartType.OtherRunning
                        ).sendToTarget();
            }
        }
        else
        {
            mHandler.obtainMessage(HANDLER_FAIL_ON_START,StartFaillType.Offline).sendToTarget();
        }
    }

    @Override
    public void onLine(Device device) {
        if (null == onLineDevice)
        {
            this.onLineDevice = device;
            mHandler.obtainMessage(HANDLER_ONLINE,device).sendToTarget();
        }
    }

    @Override
    public void offLine() {
        mHandler.obtainMessage(HANDLER_OFFLINE).sendToTarget();
        this.onLineDevice = null;
        super.close();
    }

    @Override
    public void onModelStart(Model model,StartType type) {
        mHandler.obtainMessage(HANDLER_ON_START, type).sendToTarget();
        if (model.getStateCode() ==  CmdProvider.ModelStateCode.STANDARD || model.getStateCode() ==  CmdProvider.ModelStateCode.DRYOFF)
        {
            timer = new Timer(onTimingActionListener,99);
            timer.setInterval((int) (model.getProgress().getTotal()/timer.getRepeatCount())).start();
        }
    }

    @Override
    public void onProcessing(Model model) {

        if(timer==null || getRunningModel() == null) {
            return;
        }
        //计算误差
        long deviation  =(model.getProgress().getTotal()-(timer.getCurt()*timer.getInterval())) - model.getProgress().getRemain();
        Log.i("xixi", "current deviation:" + deviation + " total:" + model.getProgress().getTotal() + " timer:" + timer.getCurt() * timer.getInterval());
        /**
         * 修正误差
         */
        if(Math.abs(deviation) >model.getProgress().getMaxDeviation()){
            timer.puase(-deviation);
        }
    }

    @Override
    public void onFinish(Model model) {
        if (null != timer)
        {
            Log.i("xixi","stop timer");
            timer.stop();
            timer = null;
        }

        mHandler.obtainMessage(HANDLER_ON_FINISH,model).sendToTarget();
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onInterupt(Model model) {
        timer.stop();
        mHandler.obtainMessage(HANDLER_ON_INTERUPT,model).sendToTarget();
    }

    @Override
    public void faillOnStart(Model model,StartFaillType type) {
        mHandler.obtainMessage(HANDLER_FAIL_ON_START,type).sendToTarget();
    }

    public boolean isOnline() {
        return getRunningModel() != null;
    }
}
