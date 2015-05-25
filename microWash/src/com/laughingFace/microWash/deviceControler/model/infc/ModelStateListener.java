package com.laughingFace.microWash.deviceControler.model.infc;

import com.laughingFace.microWash.deviceControler.model.Model;

public interface ModelStateListener {

	public abstract void onStart(Model model);

	public abstract void onProcessing();

	public abstract void onFinish();

	public abstract void onInterupt();

	public abstract void faillOnStart();

}
