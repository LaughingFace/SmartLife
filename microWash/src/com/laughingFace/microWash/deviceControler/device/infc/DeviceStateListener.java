package com.laughingFace.microWash.deviceControler.device.infc;

import com.laughingFace.microWash.deviceControler.device.Device;

public interface DeviceStateListener {

	public abstract void onLine(Device device);

	public abstract void offLine();

}
