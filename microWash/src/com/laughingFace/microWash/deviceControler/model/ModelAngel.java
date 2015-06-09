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

	public void startModel(Model model) {
		if (model.getDelay() > 0)
		{
			setRunningModel(model);
			modelStateListener.onModelStart(model, StartType.Normal);
			return;
		}
		if (model.getStateCode() == CmdProvider.ModelStateCode.STOP)
		{
			net.send(model.getCmd());
			return;
		}
		setRunningModel(model);
		net.send(model.getCmd(),true);
		requestState();
	}
	protected void notifyFinish() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (null != RunningModel)
		net.send(RunningModel.getCmdRequestState());
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
			if (isRunning)
			{
				if (RunningModel.getStateCode() == modelState)
				{
					return cmd;
				}
			}
			if (null == RunningModel || RunningModel.getStateCode() != modelState) {
					if (null == RunningModel){
						startType = StartType.ACCIDENT;
					}
					else{
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
				RunningModel.getProgress().setRemain(0);
                modelStateListener.onFinish(RunningModel);
            } else {
                Log.i("xixi", "don't know device state but state = 0");
            }
        }
        else
        {
            Log.i("xixi","don't know device state but state = "+modelState);
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
	protected void requestState(Model model)
	{
		net.send(model.getCmdRequestState());
	}
	protected void requestState()
	{
		net.send(CmdProvider.Model.REQUEST_STATE);
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
		Normal,OtherRunning,ACCIDENT,AlreadyRunning, Delay_Start
	}
	public enum StartFaillType{
		Timeout,Offline
	}

}
