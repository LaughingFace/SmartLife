package com.laughingFace.microWash.deviceControler.device;


import com.laughingFace.microWash.FileOptions.HomeApCfg;
import com.laughingFace.microWash.deviceControler.device.infc.DeviceStateListener;
import com.laughingFace.microWash.deviceControler.model.CmdProvider;
import com.laughingFace.microWash.deviceControler.utils.Timer;
import com.laughingFace.microWash.net.DeviceState;
import com.laughingFace.microWash.net.NetInterface;
import com.laughingFace.microWash.net.NetProvider;
import com.laughingFace.microWash.smartConnect.IoTManagerNative;
import com.laughingFace.microWash.util.Log;

public class DeviceAngel implements DeviceState,Timer.OnTimingActionListener{
	private DeviceStateListener deviceStateListener;
	private NetInterface net = NetProvider.getDefaultProduct();
	private Timer hearbeatRequest;
	private int interval = 4000;
	private boolean isMeat = false;
	private int hate = 0;
	IoTManagerNative iot;
	public DeviceAngel()
	{
		hearbeatRequest = new Timer(this,interval,Timer.FOREVER);
		net.setOnDeviceState(this);
		iot =new IoTManagerNative();
	}
	@Override
	public void onLineDevice(Device device) {
		deviceStateListener.onLine(device);
		isMeat = true;
		Log.i("error","new meat");
	}

	public void searchDevice() {
		if (null != hearbeatRequest)
		{
			stopSearchDevice();
		}
		hearbeatRequest = new Timer(this,interval,Timer.FOREVER);
		hearbeatRequest.start();
		net.start();
	}
	public void stopSearchDevice()
	{
		hearbeatRequest.stop();
		hearbeatRequest = null;
		isMeat = false;
		hate = 0;
		net.stop();
	}

	public void setDeviceStateListener(DeviceStateListener listener)
	{
		deviceStateListener = listener;
	}

	@Override
	public void befor() {
		net.send(CmdProvider.Request.REQUEST_DEVICE);
	}
	@Override
	public void action() {
		if(isMeat)
		{
			hate = 0;
		}
		else
		{
//			net.send(CmdProvider.Request.REQUEST_DEVICE);
			net.send(CmdProvider.Model.REQUEST_STATE);
			hate++;
			Log.i("error ", "request meat");
			if (hate > 4)
			{
				Log.e("error","hate = "+hate);
				deviceStateListener.offLine();
				hate = 0;
			}
		}
		isMeat = false;
	}

	@Override
	public void after() {

	}
}
