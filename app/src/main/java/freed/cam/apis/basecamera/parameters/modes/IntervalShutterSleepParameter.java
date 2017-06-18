/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package freed.cam.apis.basecamera.parameters.modes;

import android.text.TextUtils;

import com.troop.freedcam.R;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.parameters.AbstractParameter;

/**
 * Created by troop on 08.01.2016.
 */
public class IntervalShutterSleepParameter extends AbstractParameter
{
    private String current = "1 sec";
    private CameraWrapperInterface cameraUiWrapper;
    public IntervalShutterSleepParameter(CameraWrapperInterface cameraUiWrapper)
    {
        this.cameraUiWrapper = cameraUiWrapper;
        if (TextUtils.isEmpty(cameraUiWrapper.getAppSettingsManager().interval.get()))
            cameraUiWrapper.getAppSettingsManager().interval.set(current);
        else
            current = cameraUiWrapper.getAppSettingsManager().interval.get();
    }

    @Override
    public boolean IsSupported() {
        return true;
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCamera) {
        current = valueToSet;
    }

    @Override
    public String GetStringValue() {
        return current;
    }

    @Override
    public String[] getStringValues() {
        return cameraUiWrapper.getContext().getResources().getStringArray(R.array.interval_shutter_sleep);
    }
}
