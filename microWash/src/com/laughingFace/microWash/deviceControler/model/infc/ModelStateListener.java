package com.laughingFace.microWash.deviceControler.model.infc;

import com.laughingFace.microWash.deviceControler.model.Model;
import com.laughingFace.microWash.deviceControler.model.ModelAngel;
import com.laughingFace.microWash.deviceControler.model.Progress;

public interface ModelStateListener {

	public abstract void onStart(Model model,ModelAngel.StartType type);

	public abstract void onProcessing(Model model);

	public abstract void onFinish(Model model);

	public abstract void onInterupt(Model model);

	public abstract void faillOnStart(Model model,ModelAngel.StartFaillType type);

}
