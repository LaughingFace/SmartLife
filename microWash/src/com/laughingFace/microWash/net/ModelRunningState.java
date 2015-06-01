package com.laughingFace.microWash.net;

/**
 * Created by mathcoder23 on 15-5-25.
 */
public interface ModelRunningState {
    String onModelState(int modelState);
    void onProcessingState(int processing);
}
