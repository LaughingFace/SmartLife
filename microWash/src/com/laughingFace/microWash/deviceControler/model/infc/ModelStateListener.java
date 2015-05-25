package com.laughingFace.microWash.deviceControler.model.infc;

public interface ModelStateListener {

	public abstract void onStart();

	public abstract void onProcessing();

	public abstract void onFinish();

	public abstract void onInterupt();

	public abstract void faillOnStart();

}
