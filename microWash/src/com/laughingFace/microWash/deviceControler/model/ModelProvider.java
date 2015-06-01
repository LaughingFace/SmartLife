package com.laughingFace.microWash.deviceControler.model;

public class ModelProvider {
    public static Model getModelByStateCode(int i)
    {
        return new Model(i);
    }
    public static Model openDoor()
    {
        return getModelByStateCode(1);
    }
    public static Model closeDoor()
    {
        return getModelByStateCode(2);
    }
    public static Model standard()
    {
        return getModelByStateCode(3);
    }
    public static Model dryoff()
    {
        return getModelByStateCode(4);
    }
    public static Model stop()
    {
        return getModelByStateCode(0);
    }
}
