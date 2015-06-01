package com.laughingFace.microWash.util.wifi;

/**
 * Created by mathcoder on 7/04/15.
 */
public class WifiCfg {
    public static String getJson(String ssid,String pwd)
    {
        return ssid+"&"+pwd;
    }
}
