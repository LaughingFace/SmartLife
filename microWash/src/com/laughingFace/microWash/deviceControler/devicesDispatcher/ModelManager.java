package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import android.util.Log;
import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.utils.Timer;

public class ModelManager extends ModelAngel implements DeviceMonitor {

    private static ModelManager instance;
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
        setDeviceStateListener(this);
        onTimingActionListener = new Timer.OnTimingActionListener() {
            @Override
            public void befor() {
            }

            @Override
            public void action() {
                deviceMonitor.onProcessing(getRunningModel());
            }

            @Override
            public void after() {
                notifyFinish();
            }
        };
        timer = new Timer(onTimingActionListener,99);
    }

    @Override
    public void startModel(Model model) {

        if(null != onLineDevice){
            super.startModel(model);
        }
        else {
            deviceMonitor.faillOnStart( model);
        }

    }

    @Override
    public void onLine(Device device) {
        this.onLineDevice = device;
        deviceMonitor.onLine(device);
    }

    @Override
    public void offLine() {
        onLineDevice = null;
        deviceMonitor.offLine();
    }

    @Override
    public void onStart(Model model) {
        deviceMonitor.onStart(model);
        timer.setInterval((int) (model.getProgress().getTotal()/timer.getRepeatCount())).start();

    }

    @Override
    public void onProcessing(Model model) {

        if(timer==null || getRunningModel() == null) {
            return;
        }
        //计算误差
        long deviation  =(model.getProgress().getTotal()-(timer.getCurt()*timer.getInterval())) - model.getProgress().getRemain();
        Log.i("xixi", "current deviation:" + deviation + " total:" + model.getProgress().getTotal() * 1000 + " timer:" + timer.getCurt() * timer.getInterval());
        /**
         * 修正误差
         */
        if(Math.abs(deviation) >model.getProgress().getMaxDeviation()){
            timer.puase(-deviation);
        }
    }

    @Override
    public void onFinish(Model model) {
        timer.stop();
        deviceMonitor.onFinish(model);

    }

    @Override
    public void onInterupt(Model model) {
        timer.stop();
        deviceMonitor.onInterupt(model);
    }

    @Override
    public void faillOnStart(Model model) {
        deviceMonitor.faillOnStart(model);
    }
}
