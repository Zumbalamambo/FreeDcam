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

package freed.utils;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import com.troop.freedcam.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import freed.cam.apis.basecamera.modules.VideoMediaProfile;
import freed.cam.apis.sonyremote.sonystuff.XmlElement;
import freed.dng.CustomMatrix;
import freed.dng.DngProfile;
import freed.utils.DeviceUtils.Devices;

/**
 * Created by troop on 19.08.2014.
 */
public class AppSettingsManager {

    public class SettingMode
    {
        //String to get if supported
        private String supported_key;
        //String to get the values
        private String values_key;
        //String to get the value
        private String value_key;
        //String to get the value from the cameraparameters
        private String KEY_value;

        public SettingMode(String value_key)
        {
            this.value_key = value_key;
            this.values_key = value_key + getResString(R.string.aps_values);
            this.supported_key= value_key + getResString(R.string.aps_supported);
            this.KEY_value = value_key + getResString(R.string.aps_key);
        }

        public void setValues(String[] ar)
        {
            setStringArray(values_key, ar);
        }

        public String[] getValues()
        {
            return getStringArray(values_key);
        }

        public boolean contains(String value)
        {
            String[] values = getValues();
            for (String v : values)
            {
                if (v.equals(value))
                    return true;
            }
            return false;
        }

        public boolean isSupported()
        {
            return getBoolean(supported_key,false);
        }

        public void setIsSupported(boolean supported)
        {
            setBoolean(supported_key, supported);
        }

        public String get()
        {
            return getApiString(value_key);
        }

        public void set(String valueToSet)
        {
            setApiString(value_key,valueToSet);
        }

        public String getKEY()
        {
            return getApiString(KEY_value);
        }

        public void setKEY(String KEY)
        {
            setApiString(KEY_value,KEY);
        }

    }

    public class TypeSettingsMode extends SettingMode
    {
        private String type;
        private String mode;

        public TypeSettingsMode(String value_key) {
            super(value_key);
            this.type = value_key + getResString(R.string.aps_type);
            this.mode = value_key + getResString(R.string.aps_mode);
        }

        public int getType()
        {
            return getApiInt(type);
        }

        public void setType(int typevalue)
        {
            setApiInt(type,typevalue);
        }

        public String getMode()
        {
            return getApiString(mode);
        }

        public void setMode(String modevalue)
        {
            setApiString(mode,modevalue);
        }
    }

    private final String TAG = AppSettingsManager.class.getSimpleName();

    private int currentcamera;
    private String camApiString = AppSettingsManager.API_1;
    private Devices device;
    private String mDevice;
    private HashMap<String, CustomMatrix> matrixes;
    private HashMap<Long, DngProfile> dngProfileHashMap;

    private final String FEATUREDETECTED = "featuredetected";

    public static final int JPEG= 0;
    public static final int RAW = 1;
    public static final int DNG = 2;


    public static final int SHUTTER_HTC =0;
    public static final int SHUTTER_LG = 1;
    public static final int SHUTTER_MTK = 2;
    public static final int SHUTTER_QCOM_MILLISEC = 3;
    public static final int SHUTTER_QCOM_MICORSEC = 4;
    public static final int SHUTTER_MEIZU = 5;
    public static final int SHUTTER_KRILLIN = 6;
    public static final int SHUTTER_SONY = 7;
    public static final int SHUTTER_G2PRO = 8;
    public static final int SHUTTER_ZTE = 9;

    public static final int FRAMEWORK_NORMAL = 0;
    public static final int FRAMEWORK_LG = 1;
    public static final int FRAMEWORK_MTK = 2;
    public static final int FRAMEWORK_MOTO_EXT = 3;
    public static final String FRAMEWORK = "framework";


    public static final String CURRENTCAMERA = "currentcamera";
    public static final String NIGHTMODE = "nightmode";
    public static final String VIDEOPROFILE = "videoprofile";
    public static final String CUSTOMMATRIX = "custommatrix";
    public static final String TIMELAPSEFRAME = "timelapseframe";
    public static final String SETTING_API = "sonyapi";
    public static final String SETTING_LOCATION = "location";
    public static final String SETTING_EXTERNALSHUTTER = "externalShutter";
    public static final String SETTING_OrientationHack = "orientationHack";
    public static final String SETTING_TIMER = "timer";

    public static final String SETTING_FOCUSPEAK = "focuspeak";

    public static final String SETTING_EXTERNALSD = "extSD";

    public static final String API_SONY = "playmemories";
    public static final String API_1 = "camera1";
    public static final String API_2 = "camera2";


    public static final String APPVERSION = "appversion";

    public static final String CAMERA2FULLSUPPORTED = "camera2fullsupport";

    public static final String SETTING_HORIZONT = "horizont";

    public static final String SETTING_AEB1 = "aeb1";
    public static final String SETTING_AEB2 = "aeb2";
    public static final String SETTING_AEB3 = "aeb3";

    public static final String SETTING_AEB4 = "aeb4";

    public static final String SETTING_AEB5 = "aeb5";
    public static final String SETTING_AEB6 = "aeb6";
    public static final String SETTING_AEB7 = "aeb7";

    public static final String SETTING_BASE_FOLDER = "base_folder";

    public static final String SETTING_MEDIAPROFILES = "media_profiles";

    public static final String SETTING_AFBRACKETMAX = "afbracketmax";
    public static final String SETTING_AFBRACKETMIN = "afbracketmin";
    public static final String SETTINGS_NIGHTOVERLAY = "nighoverlay";

    public final SettingMode pictureFormat;
    public final SettingMode rawPictureFormat;
    public final SettingMode pictureSize;
    public final SettingMode focusMode;
    public final SettingMode exposureMode;
    public final SettingMode whiteBalanceMode;
    public final SettingMode colorMode;
    public final SettingMode flashMode;
    public final SettingMode isoMode;
    public final SettingMode antiBandingMode;
    public final SettingMode imagePostProcessing;
    public final SettingMode previewSize;
    public final SettingMode jpegQuality;
    public final SettingMode aeBracket;
    public final SettingMode previewFps;
    public final SettingMode previewFormat;
    public final SettingMode sceneMode;
    public final SettingMode redEyeMode;
    public final SettingMode lenshade;
    public final SettingMode zeroshutterlag;
    public final SettingMode sceneDetectMode;
    public final SettingMode memoryColorEnhancement;
    public final SettingMode videoSize;
    public final SettingMode correlatedDoubleSampling;
    public final SettingMode opticalImageStabilisation;
    public final SettingMode videoHDR;
    public final SettingMode videoHFR;
    public final SettingMode denoiseMode;
    public final SettingMode controlMode;
    public final SettingMode edgeMode;
    public final SettingMode digitalImageStabilisationMode;
    public final SettingMode hotpixelMode;
    public final SettingMode aePriorityMode;
    public final SettingMode hdrMode;
    public final SettingMode modules;
    public final SettingMode nonZslManualMode;
    public final SettingMode virtualLensfilter;
    public final SettingMode nightMode;
    public final SettingMode videoProfile;
    public final SettingMode videoStabilisation;
    public final SettingMode interval;
    public final SettingMode intervalDuration;
    public final SettingMode opcode;
    public final SettingMode matrixset;
    public final SettingMode sdcardlocation;
    public final SettingMode colorCorrectionMode;
    public final SettingMode objectTracking;
    public final SettingMode toneMapMode;
    public final SettingMode postviewSize;
    public final SettingMode zoommode;
    public final SettingMode scalePreview;
    public final SettingMode guide;
    public final SettingMode previewFpsRange;

    public final TypeSettingsMode manualFocus;
    public final SettingMode manualExposureCompensation;
    public final TypeSettingsMode manualExposureTime;
    public final SettingMode manualIso;
    public final SettingMode manualSaturation;
    public final SettingMode manualSharpness;
    public final SettingMode manualBrightness;
    public final SettingMode manualContrast;
    public final SettingMode manualFnumber;
    public final SettingMode manualZoom;
    public final SettingMode manualBurst;
    public final SettingMode manualConvergence;
    public final SettingMode manualFx;
    public final SettingMode manualProgramShift;
    public final SettingMode manualPreviewZoom;

    public final TypeSettingsMode manualWhiteBalance;

    public String[] opcodeUrlList;


    private SharedPreferences settings;
    private Resources resources;

    public AppSettingsManager(SharedPreferences sharedPreferences, Resources resources)
    {
        settings = sharedPreferences;
        this.resources = resources;

        //first time init
        if (TextUtils.isEmpty(mDevice = getDeviceString()))
        {
            parseAndFindSupportedDevice();
        }
        else //load only stuff for dng
        {
            opcodeUrlList = new String[2];
            dngProfileHashMap = getDngProfiles();
        }
        matrixes = getMatrixes();


       /* if (getdevice() == null)
            SetDevice(new DeviceUtils().getDevice(getResources()));*/

        pictureFormat = new SettingMode(getResString(R.string.aps_pictureformat));
        rawPictureFormat = new SettingMode(getResString(R.string.aps_rawpictureformat));
        pictureSize = new SettingMode(getResString(R.string.aps_picturesize));
        focusMode = new SettingMode(getResString(R.string.aps_focusmode));
        exposureMode = new SettingMode(getResString(R.string.aps_exposuremode));
        whiteBalanceMode = new SettingMode(getResString(R.string.aps_whitebalancemode));
        colorMode = new SettingMode(getResString(R.string.aps_colormode));
        flashMode = new SettingMode(getResString(R.string.aps_flashmode));
        isoMode = new SettingMode(getResString(R.string.aps_isomode));
        antiBandingMode = new SettingMode(getResString(R.string.aps_antibandingmode));
        imagePostProcessing = new SettingMode(getResString(R.string.aps_ippmode));
        previewSize = new SettingMode(getResString(R.string.aps_previewsize));
        jpegQuality = new SettingMode(getResString(R.string.aps_jpegquality));
        aeBracket = new SettingMode(getResString(R.string.aps_aebrackethdr));
        previewFps = new SettingMode(getResString(R.string.aps_previewfps));
        previewFormat = new SettingMode(getResString(R.string.aps_previewformat));
        sceneMode = new SettingMode(getResString(R.string.aps_scenemode));
        redEyeMode = new SettingMode(getResString(R.string.aps_redeyemode));
        lenshade = new SettingMode(getResString(R.string.aps_lenshademode));
        zeroshutterlag = new SettingMode(getResString(R.string.aps_zslmode));
        sceneDetectMode = new SettingMode(getResString(R.string.aps_scenedetectmode));
        memoryColorEnhancement = new SettingMode(getResString(R.string.aps_memorycolorenhancementmode));
        videoSize = new SettingMode(getResString(R.string.aps_videosize));
        correlatedDoubleSampling = new SettingMode(getResString(R.string.aps_cds));
        opticalImageStabilisation = new SettingMode(getResString(R.string.aps_ois));
        videoHDR = new SettingMode(getResString(R.string.aps_videohdr));
        videoHFR = new SettingMode(getResString(R.string.aps_videohfr));
        controlMode = new SettingMode(getResString(R.string.aps_controlmode));
        denoiseMode = new SettingMode(getResString(R.string.aps_denoisemode));
        edgeMode = new SettingMode(getResString(R.string.aps_edgemode));
        digitalImageStabilisationMode = new SettingMode(getResString(R.string.aps_digitalimagestabmode));
        hotpixelMode = new SettingMode(getResString(R.string.aps_hotpixel));
        aePriorityMode = new SettingMode(getResString(R.string.aps_ae_priortiy));
        hdrMode = new SettingMode(getResString(R.string.aps_hdrmode));
        modules = new SettingMode(getResString(R.string.aps_module));
        nonZslManualMode = new SettingMode(getResString(R.string.aps_nonzslmanualmode));
        virtualLensfilter = new SettingMode(getResString(R.string.aps_virtuallensfilter));
        nightMode = new SettingMode(getResString(R.string.aps_nightmode));
        videoProfile = new SettingMode(getResString(R.string.aps_videoProfile));
        videoStabilisation = new SettingMode(getResString(R.string.aps_videoStabilisation));
        interval = new SettingMode(getResString(R.string.aps_interval));
        intervalDuration = new SettingMode(getResString(R.string.aps_interval_duration));
        opcode = new SettingMode(getResString(R.string.aps_opcode));
        matrixset = new SettingMode(getResString(R.string.aps_matrixset));
        sdcardlocation = new SettingMode(getResString(R.string.aps_sdcard));
        colorCorrectionMode = new SettingMode(getResString(R.string.aps_cctmode));
        objectTracking = new SettingMode(getResString(R.string.aps_objecttracking));
        toneMapMode = new SettingMode(getResString(R.string.aps_tonemapmode));
        postviewSize = new SettingMode(getResString(R.string.aps_postviewsize));
        zoommode = new SettingMode(getResString(R.string.aps_zoommode));
        scalePreview = new SettingMode(getResString(R.string.aps_scalePreview));
        guide = new SettingMode(getResString(R.string.aps_guide));
        previewFpsRange = new SettingMode(getResString(R.string.aps_previewfpsrange));


        manualFocus = new TypeSettingsMode(getResString(R.string.aps_manualfocus));
        manualExposureCompensation = new SettingMode(getResString(R.string.aps_manualexpocomp));
        manualExposureTime = new TypeSettingsMode(getResString(R.string.aps_manualexpotime));
        manualWhiteBalance = new TypeSettingsMode(getResString(R.string.aps_manualwb));
        manualIso = new SettingMode(getResString(R.string.aps_manualiso));
        manualSaturation = new SettingMode(getResString(R.string.aps_manualsaturation));
        manualSharpness = new SettingMode(getResString(R.string.aps_manualsharpness));
        manualBrightness = new SettingMode(getResString(R.string.aps_manualbrightness));
        manualContrast = new SettingMode(getResString(R.string.aps_manualcontrast));
        manualFnumber = new SettingMode(getResString(R.string.aps_manualfnum));
        manualZoom = new SettingMode(getResString(R.string.aps_manualzoom));
        manualBurst = new SettingMode(getResString(R.string.aps_manualburst));
        manualConvergence = new SettingMode(getResString(R.string.aps_manualconvergence));
        manualFx = new SettingMode(getResString(R.string.aps_manualfx));
        manualProgramShift = new SettingMode(getResString(R.string.aps_manualprogramshift));
        manualPreviewZoom = new SettingMode(getResString(R.string.aps_manualpreviewzoom));
    }

    public String getResString(int id)
    {
        return resources.getString(id);
    }

    public Resources getResources()
    { return resources;}

    public HashMap<Long, DngProfile> getDngProfilesMap()
    {
        return dngProfileHashMap;
    }

    public HashMap<String, CustomMatrix> getMatrixesMap()
    {
        return matrixes;
    }

    public boolean areFeaturesDetected()
    {
        return settings.getBoolean(FEATUREDETECTED,false);
    }

    public void setAreFeaturesDetected(boolean detected)
    {
        settings.edit().putBoolean(FEATUREDETECTED,detected).commit();
    }

    public boolean openCamera1Legacy()
    {
        return settings.getBoolean(getResString(R.string.aps_opencamera1legacy), false);
    }

    private void setOpenCamera1Legacy(boolean legacy)
    {
        settings.edit().putBoolean(getResString(R.string.aps_opencamera1legacy),legacy).commit();
    }

    public boolean useQcomFocus()
    {
        return settings.getBoolean(getResString(R.string.aps_qcomfocus),false);
    }

    public void setUseQcomFocus(boolean hasQcomFocus)
    {
        settings.edit().putBoolean(getResString(R.string.aps_qcomfocus),hasQcomFocus).commit();
    }

    private void setDngManualsSupported(boolean supported)
    {
        setBoolean("dngmanualSupported", supported);
    }

    public boolean getDngManualsSupported()
    {
        return getBoolean("dngmanualSupported", true);
    }

    private void putString(String settingsval, String toSet)
    {
        settings.edit().putString(settingsval,toSet).commit();
    }

    public boolean getBoolean(String settings_key, boolean defaultValue)
    {
        return settings.getBoolean(getApiSettingString(settings_key), defaultValue);
    }

    public void setBoolean(String settings_key, boolean valuetoSet) {

        settings.edit().putBoolean(getApiSettingString(settings_key), valuetoSet).commit();
    }

    public void setCamApi(String api) {
        camApiString = api;
        putString(SETTING_API, api);
    }

    public String getCamApi() {
        camApiString = settings.getString(SETTING_API, API_1);
        return camApiString;
    }

    /*public void SetDevice(Devices device) {
        this.device = device;
        String t = device.name();
        putString("DEVICE", t);
    }*/

    private void setDevice(String device) {
        this.mDevice = device;
        putString("DEVICE", mDevice);
    }

    public Devices getDevice() {
        return device;
    }

    public String getDeviceString() {
        return mDevice;
    }

    private Devices getdevice()
    {
        String t = settings.getString("DEVICE", null);
        device = TextUtils.isEmpty(t) ? null : Devices.valueOf(t);
        return device;
    }

    public void setshowHelpOverlay(boolean value) {
        settings.edit().putBoolean("showhelpoverlay", value).commit();
    }

    public boolean getShowHelpOverlay() {
        return settings.getBoolean("showhelpoverlay", true);
    }

    public void SetBaseFolder(String uri) {
        putString(SETTING_BASE_FOLDER, uri);
    }

    public String GetBaseFolder() {
        return settings.getString(SETTING_BASE_FOLDER, null);
    }

    public void SetCurrentCamera(int currentcamera) {
        this.currentcamera = currentcamera;
        settings.edit().putInt(CURRENTCAMERA, currentcamera).commit();
    }

    public int GetCurrentCamera() {
        return settings.getInt(CURRENTCAMERA, 0);
    }

    public void SetCurrentModule(String modulename) {
        modules.set(modulename);
    }

    public String GetCurrentModule()
    {
        if (modules.get().equals(""))
            return getResString(R.string.module_picture);
        return modules.get();
    }

    /**
     * All apis can have same parameters and to use same SETTINGS strings in ui
     * that create the extended string to load it
     * so when setting is like mexposure it gets extended to camera1mexposure0
     * camera1 is the api
     * mexposure is the settingsName
     * 0 is the camera to that the settings belong
     *
     * @param settingsName to use
     * @return
     */
    private String getApiSettingString(String settingsName) {
        StringBuilder newstring = new StringBuilder();
        if (API_SONY.equals(camApiString))
            newstring.append(API_SONY).append(settingsName);
        else if (API_1.equals(camApiString))
            newstring.append(API_1).append(settingsName).append(currentcamera);
        else
            newstring.append(API_2).append(settingsName).append(currentcamera);
        return newstring.toString();
    }


    public boolean GetWriteExternal() {
        return getBoolean(SETTING_EXTERNALSD, false);
    }

    public void SetWriteExternal(boolean write) {
        setBoolean(SETTING_EXTERNALSD, write);
    }

    public void SetCamera2FullSupported(String value) {
        putString(CAMERA2FULLSUPPORTED, value);
    }

    public String IsCamera2FullSupported() {
        String t = settings.getString(CAMERA2FULLSUPPORTED, "");
        return TextUtils.isEmpty(t) ? "" : t;
    }

    public String getApiString(String valueToGet, String defaultValue) {
        return settings.getString(getApiSettingString(valueToGet), defaultValue);
    }

    public String getApiString(String valueToGet) {
        return settings.getString(getApiSettingString(valueToGet),"");
    }

    private int getApiInt(String valueToGet) {
        return settings.getInt(getApiSettingString(valueToGet),0);
    }

    private void setApiInt(String key,int valueToSet) {
        settings.edit().putInt(getApiSettingString(key),valueToSet).commit();
    }

    public void setApiString(String settingsName, String Value) {
        putString(getApiSettingString(settingsName), Value);
    }

    public final static String SPLITTCHAR = "'";
    public void setStringArray(String settingsName, String[] Value) {
        String tmp ="";
        for (int i= 0; i<Value.length;i++)
            tmp += Value[i]+SPLITTCHAR;
        putString(getApiSettingString(settingsName), tmp);
    }

    public String[] getStringArray(String settingsname)
    {
        return getApiString(settingsname).split(SPLITTCHAR);
    }



    public HashMap<String,VideoMediaProfile> getMediaProfiles()
    {
        Set<String> tmp = settings.getStringSet(getApiSettingString(SETTING_MEDIAPROFILES),new HashSet<String>());
        String[] split = new String[tmp.size()];
        tmp.toArray(split);
        HashMap<String,VideoMediaProfile>  hashMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            VideoMediaProfile mp = new VideoMediaProfile(split[i]);
            hashMap.put(mp.ProfileName, mp);
        }

        return hashMap;
    }

    public void saveMediaProfiles(HashMap<String,VideoMediaProfile> mediaProfileHashMap)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(getApiSettingString(SETTING_MEDIAPROFILES));
        editor.commit();
        Set<String> set =  new HashSet<String>();
        for (VideoMediaProfile profile : mediaProfileHashMap.values())
            set.add(profile.GetString());
        editor.putStringSet(getApiSettingString(SETTING_MEDIAPROFILES), set);
        if (!settings.getBoolean("tmp", false))
            editor.putBoolean("tmp", true);
        else
            editor.putBoolean("tmp",false);
        editor.commit();
    }




    public void setFramework(int frameWork)
    {
        settings.edit().putInt(FRAMEWORK, frameWork).commit();
    }

    public int getFrameWork()
    {
        return settings.getInt(FRAMEWORK,0);
    }


    public static final String FRONTCAMERA ="frontcamera";
    public void setIsFrontCamera(boolean isFront)
    {
        settings.edit().putBoolean(getApiSettingString(FRONTCAMERA), isFront).commit();
    }

    public boolean getIsFrontCamera()
    {
        return settings.getBoolean(getApiSettingString(FRONTCAMERA), false);
    }


    ///XML STUFF

    private void parseAndFindSupportedDevice()
    {
        try {
            String xmlsource = getString(resources.openRawResource(R.raw.supported_devices));
            XmlElement rootElement = XmlElement.parse(xmlsource);
            if (rootElement.getTagName().equals("devices"))
            {
                List<XmlElement> devicesList = rootElement.findChildren("device");

                for (XmlElement device_element: devicesList)
                {
                    List<XmlElement> models = device_element.findChild("models").findChildren("item");
                    for (XmlElement mod : models)
                    {
                        if (mod.getValue().equals(Build.MODEL)) {
                            setDevice(device_element.getAttribute("name",""));
                            if (device_element.findChild("camera1").findChild("dngmanual") != null)
                                setDngManualsSupported(Boolean.parseBoolean(device_element.findChild("camera1").findChild("dngmanual").getValue()));
                            else
                                setDngManualsSupported(true);

                            if (device_element.findChild("camera1").findChild("opencameralegacy") != null)
                                setOpenCamera1Legacy(Boolean.parseBoolean(device_element.findChild("camera1").findChild("opencameralegacy").getValue()));
                            else
                                setOpenCamera1Legacy(false);

                            dngProfileHashMap = new HashMap<>();
                            getDngStuff(dngProfileHashMap, device_element);

                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private HashMap<Long, DngProfile> getDngProfiles()
    {
        HashMap<Long,DngProfile> map = new HashMap<>();
        try {
            String xmlsource = getString(resources.openRawResource(R.raw.supported_devices));
            XmlElement rootElement = XmlElement.parse(xmlsource);
            if (rootElement.getTagName().equals("devices"))
            {
                List<XmlElement> devicesList = rootElement.findChildren("device");

                for (XmlElement device_element: devicesList)
                {
                    if (device_element.getAttribute("name", "").equals(device.name()))
                    {
                        getDngStuff(map, device_element);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void getDngStuff(HashMap<Long, DngProfile> map, XmlElement device_element) {
        opcodeUrlList[0] = device_element.getAttribute("opcode2", "");
        opcodeUrlList[1] = device_element.getAttribute("opcode3", "");
        List<XmlElement> fsizeList = device_element.findChildren("filesize");
        for (XmlElement filesize_element : fsizeList) {
            long filesize = Long.parseLong(filesize_element.getAttribute("size", "0"));
            DngProfile profile = getProfile(filesize_element);
            map.put(filesize, profile);
        }
    }

    private DngProfile getProfile(XmlElement element)
    {
        int blacklvl = Integer.parseInt(element.findChild("blacklvl").getValue());
        int width = Integer.parseInt(element.findChild("width").getValue());
        int height = Integer.parseInt(element.findChild("height").getValue());
        int rawType = Integer.parseInt(element.findChild("rawtype").getValue());
        String colorpattern = element.findChild("colorpattern").getValue();
        int rowsize = Integer.parseInt(element.findChild("rowsize").getValue());
        String matrixset = element.findChild("matrixset").getValue();

        return new DngProfile(blacklvl,width,height,rawType,colorpattern,rowsize,matrixes.get(matrixset));
    }

    private HashMap<String, CustomMatrix> getMatrixes()
    {
        HashMap<String, CustomMatrix> matrixHashMap = new HashMap<>();
        try {
            String xmlsource = getString(resources.openRawResource(R.raw.matrixes));
            XmlElement rootElement = XmlElement.parse(xmlsource);
            if (rootElement.getTagName().equals("matrixes"))
            {
                List<XmlElement> profileElements = rootElement.findChildren("matrix");
                for (XmlElement xmlElement: profileElements)
                {
                    String name  = xmlElement.getAttribute("name", "");
                    String c1 = xmlElement.findChild("color1").getValue();
                    String c2 = xmlElement.findChild("color2").getValue();
                    String neut = xmlElement.findChild("neutral").getValue();
                    String forward1 = xmlElement.findChild("forward1").getValue();
                    String forward2 = xmlElement.findChild("forward2").getValue();
                    String reduction1 = xmlElement.findChild("reduction1").getValue();
                    String reduction2 = xmlElement.findChild("reduction2").getValue();
                    String noise = xmlElement.findChild("noise").getValue();
                    CustomMatrix mat = new CustomMatrix(c1,c2,neut,forward1,forward2,reduction1,reduction2,noise);
                    matrixHashMap.put(name,mat);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrixHashMap;
    }

    private String getString(InputStream inputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        return buf.toString();
    }
}
