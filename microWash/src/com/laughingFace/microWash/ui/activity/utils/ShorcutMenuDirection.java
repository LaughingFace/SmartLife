package com.laughingFace.microWash.ui.activity.utils;

import android.graphics.Rect;
import com.laughingFace.microWash.ui.plug.CircularFloatingActionMenu.FloatingActionMenu;
import com.laughingFace.microWash.util.Log;
import com.laughingFace.microWash.util.ScreenUtil;

/**
 * Created by mathcoder23 on 15-6-23.
 */
public class ShorcutMenuDirection {
    private static int locateLeftTop = 45;
    private static int locateLeftBottom = -45;
    private static int locateRightTop = 135;
    private static int locateRightBottom = -135;

    private static int locateTop = 90;
    private static int locateBottom = -90;
    private static int locateLeft = 0;
    private static int locateRight = -180;


    private static int getDirection(Rect menuCenterLocation, int menuRadius)
    {
        float distanceLeftMenuCenter = (menuCenterLocation.left+menuCenterLocation.right)/2;
        float distanceTopMenuCenter = (menuCenterLocation.top + menuCenterLocation.bottom)/2;
        float distanceBottomMenuCenter = ScreenUtil.getScreenHeight()-ScreenUtil.getStatusHeight()-distanceTopMenuCenter;
        float distanceRightMenuCenter = ScreenUtil.getScreenWidth() - distanceLeftMenuCenter;
        if (distanceTopMenuCenter < menuRadius)
        {
            if (distanceLeftMenuCenter < menuRadius)
            {
                Log.i("xixi", "locateLeftTop");
                return locateLeftTop;
            }
            else if (distanceRightMenuCenter < menuRadius)
            {
                Log.i("xixi", "locateRightTop");

                return locateRightTop;
            }
            Log.i("xixi", "locateTop");

            return locateTop;
        }
        else if (distanceBottomMenuCenter < menuRadius)
        {
            if (distanceLeftMenuCenter < menuRadius)
            {
                Log.i("xixi", "locateLeftBottom");

                return locateLeftBottom;
            }
            else if (distanceRightMenuCenter < menuRadius)
            {
                Log.i("xixi", "locateRightBottom");

                return locateRightBottom;
            }
            Log.i("xixi", "locateBottom");

            return locateBottom;
        }
        else if (distanceLeftMenuCenter < menuRadius)
        {
            Log.i("xixi", "locateLeftBottom");

            return locateLeft;
        }
        else if (distanceRightMenuCenter < menuRadius)
        {
            Log.i("xixi", "locateRightBottom");

            return locateRight;
        }
        Log.i("xixi", "locateBottom");

        return locateBottom;

    }
    public static void setDirection(Rect menuCenterLocation, FloatingActionMenu floatingActionMenu)
    {
        int angleBisector = getDirection(menuCenterLocation, floatingActionMenu.getRadius()+floatingActionMenu.getSubActionItems().get(0).width);
        int angle = 90;

            floatingActionMenu.setStartAngle(angleBisector - angle / 2);
        floatingActionMenu.setEndAngle(angleBisector + angle / 2);
//        floatingActionMenu.setRadius(floatingActionMenu.getRadius()*2);
        Log.i("xixi", "radius:" + floatingActionMenu.getRadius());
        Log.i("xixi", "start angle:" + floatingActionMenu.getStartAngle());
        Log.i("xixi", "end angle:" + floatingActionMenu.getEndAngle());
    }
}
