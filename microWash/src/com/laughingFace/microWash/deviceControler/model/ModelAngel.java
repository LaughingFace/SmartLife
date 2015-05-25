package com.laughingFace.microWash.deviceControler.model;


import com.laughingFace.microWash.deviceControler.device.Device;
import com.laughingFace.microWash.deviceControler.device.DeviceAngel;
import com.laughingFace.microWash.deviceControler.device.infc.DeviceStateListener;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;
import com.laughingFace.microWash.net.ModelRunningState;
import com.laughingFace.microWash.net.NetInterface;
import com.laughingFace.microWash.net.NetProvider;

public class ModelAngel implements DeviceStateListener,ModelRunningState{

	private DeviceAngel deviceAngel;
	private NetInterface net = NetProvider.getDefaultProduct();
	private ModelStateListener modelStateListener;
	private DeviceStateListener deviceStateListener;
	private Device device;
	private Model RunningModel;
	public ModelAngel(ModelStateListener modelStateListener,DeviceStateListener deviceStateListener)
	{
		deviceAngel = new DeviceAngel();
		deviceAngel.setDeviceStateListener(this);
		this.modelStateListener = modelStateListener;
		this.deviceStateListener = deviceStateListener;
	}
	public void startModel(Model model) {
		if (null == device)
		{
			modelStateListener.faillOnStart(model);
			return;
		}
		this.RunningModel = model;
		net.send(model.getCmd());
	}


	protected void notifyFinish() {

	}

	@Override
	public void onLine(Device device) {
		this.device = device;

		deviceStateListener.onLine(device);
	}

	@Override
	public void offLine() {
		this.device = null;
		if (null != RunningModel)
		{
			modelStateListener.onInterupt(RunningModel);
			RunningModel = null;
		}

		deviceStateListener.offLine();
	}

	@Override
	public void onModelState(int modelState) {
		if (null == RunningModel)
		{
			if (modelState == 3 || modelState == 4) {
				RunningModel = ModelProvider.getModelByStateCode(modelState);
				startProgress();
			}
		}
		else
		{
			if(RunningModel.getStateCode() == modelState) {
				startProgress();
			}
			else if (0 == modelState)
			{
				modelStateListener.onFinish(RunningModel);

				endModel();
			}
			else
			{
				modelStateListener.faillOnStart(RunningModel);

				endModel();
			}
		}
	}

	@Override
	public void onProcessingState(int processing) {

	}

	private void startProgress()
	{

	}
	private void endProgress()
	{

	}
	private void endModel()
	{
		RunningModel = null;
		endProgress();
	}

	public Model getRunningModel() {
		return RunningModel;
	}
}
