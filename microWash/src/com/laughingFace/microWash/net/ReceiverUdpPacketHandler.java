package com.laughingFace.microWash.net;

import android.util.Log;
import com.laughingFace.microWash.MyApplication;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.deviceControler.device.Device;

import java.net.DatagramPacket;

/**
 * Created by mathcoder23 on 15-5-26.
 */
public class ReceiverUdpPacketHandler {
    private ModelRunningState modelRunningState;
    private DeviceState deviceState;
    private Device device;
    private static final int UPDATE_STATE_LEN = "{A:1}".length();
    private static final int PROGRESS_LEN = "{B:11}".length();
    private static final int MIN_LEN = "{A:!}".length();
    public ReceiverUdpPacketHandler()
    {

    }
    public void handler(DatagramPacket packet)
    {
        byte[] data = packet.getData();
        if (data.length >= MIN_LEN)
        {
            if (null == device)
            {
                device = new Device();
                device.setName(MyApplication.getContext().getString(R.string.device_name));
            }
            deviceState.onLineDevice(device);
            switch(data[1])
            {
                case 'A':
                    if (data.length == UPDATE_STATE_LEN)
                    {
                        int state = data[3]-'0';
                        if (null != modelRunningState)
                        {
                            if(!NetworkManager.msgQueue.remove(modelRunningState.onModelState(state)))
                            {
                                Log.e("xixi","remove msgQueue faild");
                            }
                        }
                    }
                    break;
                case 'B':
                    if (data.length == PROGRESS_LEN)
                    {
                        if (null != modelRunningState)
                        {
                            int processing = data[3]*256 + data[4];
                            modelRunningState.onProcessingState(processing);
                        }
                    }
                    break;
                case 'W':
//                    if (null == device)
//                    device = new Device();
//                    deviceState.onLineDevice(device);
                    break;
            }
        }
    }
    public void setOnModelRunningState(ModelRunningState listener) {
        modelRunningState = listener;
    }
    public void setOnDeviceState(DeviceState listener)
    {
        this.deviceState = listener;
    }
}
