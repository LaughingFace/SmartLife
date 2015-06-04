package com.laughingFace.microWash.deviceControler.model;


public class Model {
	public static final int DELAY_OFFSET = 10;
	private String name;
	private String cmd;
	private String cmdRequestProgress = CmdProvider.Model.REQUEST_PROCESSING;
	private int stateCode;
	private Progress progress;
	private Progress delayStartProgress;
	private String cmdRequestState = CmdProvider.Model.REQUEST_STATE;
	private int id;
	private long delay  =0;



	public int getId() {
		return id+(delay>0?DELAY_OFFSET:0);
	}

	public Model(String cmd,int stateCode)
	{
		progress = new Progress();
		delayStartProgress = new Progress();
		this.cmd = cmd;
		this.stateCode = stateCode;
	}
	public Model(int stateCode)
	{

		this.cmd = CmdProvider.Model.setState(stateCode);
		this.stateCode = stateCode;
		this.id = stateCode;
	}

	public String getCmd() {
		return cmd;
	}

	public String getCmdRequestProgress() {
		return cmdRequestProgress;
	}

	public int getStateCode() {
		return stateCode;
	}

	public Progress getProgress() {
		return progress;
	}

	public String getCmdRequestState() {
		return cmdRequestState;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Progress getDelayStartProgress() {
		return delayStartProgress;
	}

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
