package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;

public class ModelManager extends ModelAngel implements DeviceMonitor {

    @Override
    public void startModel(Model model, ModelStateListener modelStateListener) {


        super.startModel(model, modelStateListener);
    }

    @Override
    public void onLine() {

    }

    @Override
    public void offLine() {

    }

    @Override
    public void onStart(Model model) {


    }

    @Override
    public void onProcessing() {

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
