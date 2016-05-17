package com.freedcam.apis.camera1.camera.parameters.manual;

import android.hardware.Camera;

import com.freedcam.apis.basecamera.camera.parameters.AbstractParameterHandler;
import com.freedcam.apis.camera1.camera.parameters.CamParametersHandler;
import com.freedcam.utils.Logger;
import com.freedcam.utils.StringUtils;

import java.util.HashMap;

/**
 * Created by troop on 21.02.2016.
 */
public class ShutterManual_ExposureTime_FloatToSixty extends ShutterManual_ExposureTime_Micro
{

    private final static String TAG = ShutterManual_ExposureTime_FloatToSixty.class.getSimpleName();
    /**
     * @param parameters
     * @param camParametersHandler
     */
    public ShutterManual_ExposureTime_FloatToSixty(Camera.Parameters parameters, CamParametersHandler camParametersHandler, String[] shuttervalues, String max, String min) {
        super(parameters, camParametersHandler, shuttervalues, "exposure-time", "max-exposure-time", "min-exposure-time");
    }

    @Override
    protected void setvalue(int valueToset)
    {
        currentInt = valueToset;
        if(!stringvalues[currentInt].equals("Auto"))
        {
            String shutterstring = StringUtils.FormatShutterStringToDouble(stringvalues[currentInt]);
            Logger.d(TAG, "StringUtils.FormatShutterStringToDouble:" + shutterstring);
            shutterstring = StringUtils.FLOATtoSixty4(shutterstring);
            parameters.set("exposure-time", shutterstring);
        }
        else
        {
            parameters.set("exposure-time", "0");
            Logger.d(TAG, "set exposure time to auto");
        }
        camParametersHandler.SetParametersToCamera(parameters);
    }
}