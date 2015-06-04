package com.laughingFace.microWash.deviceControler.model;

public class ModelProvider {

    public static Model standard;
    public static Model dryoff;
    public static Model sterilization;
    public static Model timingWash;
    static{
        standard = getModelByStateCode(3);
        standard.setName("标准模式");
        dryoff = getModelByStateCode(4);
        dryoff.setName("烘干模式");
        sterilization = getModelByStateCode(4);
        sterilization.setName("杀菌模式");
        timingWash  = getModelByStateCode(3);
        timingWash.setName("定时清洗");
    }

    public static Model getModelByStateCode(int i)
    {
        switch(i)
        {
            case 3:
                return standard;
            case 4:
                return dryoff;
            default:
                return new Model(i);
        }
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
    public static final int ID_STANDARD = CmdProvider.ModelStateCode.STANDARD;
    public static final int ID_DRYOFF = CmdProvider.ModelStateCode.DRYOFF;
    public static final int ID_TIMINGWASH = ID_STANDARD + Model.DELAY_OFFSET;
    public static final int ID_TIMINGDRYOFF = ID_DRYOFF + Model.DELAY_OFFSET;


}
