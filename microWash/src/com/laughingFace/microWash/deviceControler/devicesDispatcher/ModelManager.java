package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.device.DeviceAngel;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.utils.Timer;
import com.laughingFace.microWash.util.Log;

public class ModelManager extends ModelAngel implements DeviceMonitor {

    private static ModelManager instance;
    private DeviceAngel deviceAngel;
    private DeviceMonitor deviceMonitor;
    private Device onLineDevice;
    private Timer.OnTimingActionListener onTimingActionListener;
    private Timer timer;

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

                deviceMonitor.onProcessing(getRunningModel());
            }

            @Override
            public void after() {
                if (timer != null)
                notifyFinish();
            }
        };

    }

    @Override
    public void startModel(Model model) {
        if (null != onLineDevice)
        {
            super.startModel(model);
        }
        else
        {
            deviceMonitor.faillOnStart(model,StartFaillType.Offline);
        }
    }

    @Override
    public void onLine(Device device) {
        if (null == onLineDevice)
        {
            this.onLineDevice = device;
            deviceMonitor.onLine(device);
        }
    }

    @Override
    public void offLine() {
        deviceMonitor.offLine();
        this.onLineDevice = null;
        super.close();
    }

    @Override
    public void onStart(Model model,StartType type) {
        deviceMonitor.onStart(model,type);
        timer = new Timer(onTimingActionListener,99);
        timer.setInterval((int) (model.getProgress().getTotal()/timer.getRepeatCount())).start();
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

        deviceMonitor.onFinish(model);
    }

    @Override
    public void onInterupt(Model model) {
        timer.stop();
        deviceMonitor.onInterupt(model);
    }

    @Override
    public void faillOnStart(Model model,StartFaillType type) {
        deviceMonitor.faillOnStart(model,type);
    }
}
