package com.laughingFace.microWash.deviceControler.model;


public class Model {
	public Model(String cmd,int stateCode)
	{
		this.cmd = cmd;
		this.stateCode = stateCode;
	}
	public Model(int stateCode)
	{
		this.cmd = "{A:"+stateCode+"}";
		this.stateCode = stateCode;
	}
	private String cmd;

	private int stateCode;
	private Progress progress;

	public String getCmd() {
		return cmd;
	}

	public int getStateCode() {
		return stateCode;
	}
}
