package com.laughingFace.microWash.deviceControler.model;

public class ModelProvider {
    public static Model getModelByStateCode(int i)
    {
        return new Model(i);
    }
}
