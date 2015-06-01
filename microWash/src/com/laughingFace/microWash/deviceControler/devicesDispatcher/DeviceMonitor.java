package com.laughingFace.microWash.deviceControler.devicesDispatcher;


import com.laughingFace.microWash.deviceControler.device.infc.DeviceStateListener;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;

public interface DeviceMonitor extends DeviceStateListener, ModelStateListener {
}
