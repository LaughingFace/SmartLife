package com.laughingFace.microWash.deviceControler.model;


public class Model {
	private Model()
	{
		progress = new Progress();
	}
	public Model(String cmd,int stateCode)
	{
		this();
		this.cmd = cmd;
		this.stateCode = stateCode;
	}
	public Model(int stateCode)
	{
		this();
		this.cmd = CmdProvider.Model.setState(stateCode);
		this.stateCode = stateCode;
	}
	private String cmd;
	private String cmdRequestProgress = CmdProvider.Model.REQUEST_PROCESSING;
	private int stateCode;
	private Progress progress;
	private String cmdRequestState = CmdProvider.Model.REQUEST_STATE;
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
}
