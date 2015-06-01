package com.laughingFace.microWash.deviceControler.model;



import com.laughingFace.microWash.deviceControler.device.infc.DeviceStateListener;
import com.laughingFace.microWash.deviceControler.model.infc.ModelStateListener;
import com.laughingFace.microWash.deviceControler.utils.Timer;
import com.laughingFace.microWash.net.ModelRunningState;
import com.laughingFace.microWash.net.NetInterface;
import com.laughingFace.microWash.net.NetProvider;
import com.laughingFace.microWash.util.Log;

public class ModelAngel implements ModelRunningState,Timer.OnTimingActionListener{
	private NetInterface net = NetProvider.getDefaultProduct();
	private ModelStateListener modelStateListener;
	private DeviceStateListener deviceStateListener;
	private Model RunningModel;
	private Timer hearbeatProgress;
	private boolean isRunning = false;
	private StartType startType = StartType.Normal;
	public ModelAngel()
	{
		net.setOnModelRunningState(this);
	}
	public void setModelStateListener(ModelStateListener modelStateListener)
	{
		this.modelStateListener = modelStateListener;
	}
	public void setDeviceStateListener(DeviceStateListener deviceStateListener)
	{
		this.deviceStateListener = deviceStateListener;
	}
	public void startModel(Model model) {
		if (model.getStateCode() == CmdProvider.ModelStateCode.STOP)
		{
			if (null != RunningModel)
			{
			endModel();
			}
			setRunning(true);
		}
		setRunningModel(model);
		net.send(model.getCmd(),true);
	}
	protected void notifyFinish() {
		net.send(RunningModel.getCmdRequestProgress());
	}



	public void close() {

		if (null != RunningModel)
		{
			endModel();
		}
	}

	@Override
	public String onModelState(int modelState) {
        Log.i("xixi", "state:" + modelState);
		String cmd =null;
		if (-1 == modelState)
		{
			modelStateListener.faillOnStart(RunningModel,StartFaillType.Timeout);
			setRunningModel(null);
			return null;
		}

		if (null != RunningModel)
		{
			cmd = RunningModel.getCmd();
		}
        if (modelState ==  CmdProvider.ModelStateCode.STANDARD || modelState ==  CmdProvider.ModelStateCode.DRYOFF) {
            if (null == RunningModel || RunningModel.getStateCode() != modelState) {
					if (null == RunningModel){
						startType = StartType.ACCIDENT;
					}else{
						startType = StartType.OtherRunning;
					}
                setRunningModel(ModelProvider.getModelByStateCode(modelState));
            }else
			{
				startType = StartType.Normal;
			}
            startProgress();
        }
		else if(modelState == CmdProvider.ModelStateCode.OPEN_DOOR || modelState == CmdProvider.ModelStateCode.CLOSE_DOOR)
		{
//			setRunningModel(null);
			setRunning(true);
			modelStateListener.onModelStart(RunningModel, startType);
		}
        else if (CmdProvider.ModelStateCode.STOP == modelState) {
            if (isRunning) {
                modelStateListener.onFinish(RunningModel);

            } else {
                Log.e("xixi", "don't know device state but state = 0");
            }
        }
        else
        {
            Log.e("xixi","don't know device state but state = "+modelState);
        }
		return cmd;
	}

	@Override
	public void onProcessingState(int processing) {
		Log.i("xixi","p:"+processing);
		if (null == hearbeatProgress)
		{
			Log.i("xixi","start progress");
			hearbeatProgress = new Timer(this,processing*20,50);
			hearbeatProgress.start();
			RunningModel.getProgress().setTotal(processing*1000);
			RunningModel.getProgress().setRemain(processing*1000);
			modelStateListener.onModelStart(RunningModel, startType);
            setRunning(true);
			startType = StartType.Normal;
		}
		else
		{
			RunningModel.getProgress().setRemain(processing*1000);
			modelStateListener.onProcessing(RunningModel);
		}
	}

	private void startProgress()
	{
		net.send(RunningModel.getCmdRequestProgress());
	}
	private void endProgress()
	{
		Log.i("xixi","stop progress");
		hearbeatProgress.stop();
		hearbeatProgress = null;
	}
	private void endModel()
	{
		Log.i("xixi", "end model");
		setRunning(false);
        if (null != hearbeatProgress)
		{
			hearbeatProgress.stop();
			hearbeatProgress = null;
		}
		setRunningModel(null);


	}

	public Model getRunningModel() {
		return RunningModel;
	}

	public void setRunningModel(Model model)
	{
		this.RunningModel = model;
		Log.i("xixi","runningmodel state : "+(model == null?"null":"not null"));
	}
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
		Log.i("xixi","running state : "+isRunning);
	}

	@Override
	public void befor() {
	}

	@Override
	public void action() {
		net.send(RunningModel.getCmdRequestProgress());
	}

	@Override
	public void after() {
	}
	public enum StartType {
		Normal,OtherRunning,ACCIDENT,AlreadyRunning
	}
	public enum StartFaillType{
		Timeout,Offline
	}

}
