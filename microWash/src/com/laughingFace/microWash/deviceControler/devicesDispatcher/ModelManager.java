package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.Progress;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;
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

            }

            @Override
            public void after() {

            }
        };
        timer = new Timer(onTimingActionListener);
        timer.setRepeatCount(100);
    }

    @Override
    public void startModel(Model model) {

        if(null != onLineDevice){
            super.startModel(model);
        }
        else {
            deviceMonitor.faillOnStart();
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

    }

    @Override
    public void onProcessing(Progress progress) {
        deviceMonitor.onProcessing(progress);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onInterupt() {

    }

    @Override
    public void faillOnStart() {

    }
}
