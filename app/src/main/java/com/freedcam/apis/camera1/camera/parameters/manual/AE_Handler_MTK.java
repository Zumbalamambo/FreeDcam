package com.freedcam.apis.camera1.camera.parameters.manual;

import android.hardware.Camera;

import com.freedcam.apis.KEYS;
import com.freedcam.apis.camera1.camera.CameraHolderApi1;
import com.freedcam.apis.camera1.camera.parameters.CamParametersHandler;
import com.freedcam.utils.Logger;

/**
 * Created by GeorgeKiarie on 20/04/2016.
 */
public class AE_Handler_MTK
    {
        private ISOManualParameterMTK isoManualParameter;
        private ShutterManualMtk shutterPrameter;
        private int currentIso = 0;
        private int currentShutter = 0;
        private Camera.Parameters parameters;
        boolean auto = true;
        private CamParametersHandler camParametersHandler;

        final String TAG = AE_Handler_MTK.class.getSimpleName();

        enum AeManual
        {
            shutter,
            iso,
        }

        public AE_Handler_MTK(Camera.Parameters parameters, CameraHolderApi1 cameraHolder, CamParametersHandler camParametersHandler, int maxiso)
        {
            this.camParametersHandler = camParametersHandler;
            this.isoManualParameter = new ISOManualParameterMTK(parameters,cameraHolder, camParametersHandler, aeevent, maxiso);
            camParametersHandler.ManualIso = isoManualParameter;
            this.shutterPrameter = new ShutterManualMtk(parameters, camParametersHandler, aeevent);
            camParametersHandler.ManualShutter = shutterPrameter;
            this.parameters = parameters;
        }

        public interface AeManualEvent
        {
            void onManualChanged(AeManual fromManual, boolean automode, int value);
        }

        AeManualEvent aeevent =  new AeManualEvent() {
            @Override
            public void onManualChanged(AeManual fromManual, boolean automode, int value)
            {
                if (automode)
                {
                    Logger.d(TAG, "AutomodeActive");
                    auto = automode;


                    switch (fromManual) {
                        case shutter:
                            currentIso = isoManualParameter.GetValue();
                            isoManualParameter.setValue(0);
                            break;
                        case iso:
                            currentShutter = shutterPrameter.GetValue();
                            shutterPrameter.setValue(0);
                            break;
                    }
                }
                else
                {
                    if (auto)
                    {
                        Logger.d(TAG, "Automode Deactivated, set last values");
                        auto = false;
                        switch (fromManual) {
                            case shutter:
                                isoManualParameter.setValue(currentIso);
                                break;
                            case iso:
                                if (currentShutter == 0) currentShutter =9;
                                shutterPrameter.setValue(currentShutter);
                                break;
                        }
                    }
                    else
                    {
                        Logger.d(TAG, "Automode Deactivated, set UserValues");
                        switch (fromManual) {
                            case shutter:
                                shutterPrameter.setValue(value);
                                break;
                            case iso:
                                isoManualParameter.setValue(value);
                                break;
                        }
                    }
                   // parameters.put("lg-manual-mode-reset", "0");
                }
                camParametersHandler.SetParametersToCamera(parameters);
                if (automode) {
                    String t = camParametersHandler.IsoMode.GetValue();
                    if (!t.equals(KEYS.ISO100))
                        camParametersHandler.IsoMode.SetValue(KEYS.ISO100, true);
                    else
                        camParametersHandler.IsoMode.SetValue(KEYS.AUTO, true);
                    camParametersHandler.IsoMode.SetValue(t, true);
                }
            }
        };
    }