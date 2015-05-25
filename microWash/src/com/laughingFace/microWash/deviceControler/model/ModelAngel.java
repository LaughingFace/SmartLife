package com.laughingFace.microWash.deviceControler.model;


import com.laughingFace.microWash.deviceControler.device.DeviceAngel;
import com.laughingFace.microWash.deviceControler.device.infc.DeviceStateListener;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;
import com.laughingFace.microWash.net.NetInterface;
import com.laughingFace.microWash.net.NetProvider;

public class ModelAngel {

	private DeviceAngel deviceAngel;
	private NetInterface net = NetProvider.getDefaultProduct();

	public void startModel(Model model) {

	}

	public void setModelStateListener(ModelStateListener modelStateListener) {

	}

	public void setDeviceStateListener(DeviceStateListener deviceStateListener) {

	}

	protected void notifyFinish() {

	}

}
