package com.huace.landstar.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;

import com.huace.landstar.data.C;
import com.huace.landstar.data.ConstPath;
import com.huace.landstar.projectManager.CloudStatusMonitor;
import com.huace.landstar.util.Lg;
import com.huace.landstar.util.StringUtils;
import com.huace.landstar.util.UtilByte;
import com.huace.landstar.util.UtilFile;
import com.huace.landstar.util.UtilSDCardManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App extends MultiDexApplication {

    private int mMenuGroup = -1;
    private String mMainMenuItemKey = "";

    private long mStartTimeMs = -1;
    private long mExitCode = 1;// 正常退出为0
    private String mVersion = "";
    public static final String HCE300 = "HCE300";
    public static final String HUACE = "HUACE";

    /**
     * 用于调试打印
     */
    public static boolean IS_PRINT_DEBUG_LOG = true;

    public static App instance = null;

    private static Handler handlerMainActivity = new Handler();

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static App getApplication() {
        return instance;
    }

    public static Handler getHandler() {
        return handlerMainActivity;
    }


    public boolean isHuacePda() {
        boolean boo = false;
        if (EmFuncCfg.ONLINE_REGISTRATION.isEnable()) {
            return false;
        }
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        String mtyb = android.os.Build.BRAND;// 手机品牌
        String mtype = android.os.Build.MODEL; // 手机型号
        if (!mtyb.equals(HCE300) && !mtype.equals(HCE300)) {
            return false;
        }
        String hwVerStr = new String();
        Object object = new Object();

        Method getService = null;
        try {
            getService = Class.forName("android.os.ServiceManager").getMethod(
                    "getService", String.class);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        Object obj = null;
        try {
            obj = getService.invoke(object, new Object[]{new String(
                    "NvRAMAgent")});
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }

        IBinder binder = (IBinder) obj;
        if (binder != null) {
            NvRAMAgent agent = NvRAMAgent.Stub.asInterface(binder);
            int file_lid0 = 45; // The lid of
            int file_lid1 = 71;
            // AP_CFG_CUSTOM_FILE_HARDWARE_INFO_LID is 45
            byte[] buff = null;
            try {
                int version = Build.VERSION.SDK_INT;
                if (version < 23) {//HCE300 更新到6.0后，修改了获取注册信息的方案。
                    buff = agent.readFile(file_lid0);
                } else {
                    buff = agent.readFile(file_lid1);
                }
                hwVerStr = UtilByte.getString_UTF8(buff);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e1) {
                Lg.printException(e1);
            } catch (Exception e2) {
                Lg.printException(e2);
            }
        } else {
            Lg.e("TAG", "NvRAMAgent service = " + binder);
            hwVerStr = "NULL";
        }
        hwVerStr = hwVerStr.replace("\0", "");
        if (hwVerStr.isEmpty()) {
            hwVerStr = "NULL";
        }

        if (hwVerStr.equals(HUACE)) {
            boo = true;
        }
        return boo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStartTimeMs = System.currentTimeMillis();
        instance = this;
        Lg.init();
        initVersion();
        // 在Appliction里面设置我们的异常处理器ncaughtExceptionHandler处理器
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        //初始化gis平台
        LoadAppEnviroment.loadGisEngine();
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        String mtyb = android.os.Build.BRAND;// 手机品牌
        String mtype = android.os.Build.MODEL; // 手机型号
        String imei = tm.getDeviceId();
        String imsi = tm.getSubscriberId();

        Lg.i("App", "App lifecycle on created = " + getVersion() + ",this = "
                + this + "\n手机品牌：" + mtyb + "\n手机型号：" + mtype + "\nIMEI:"
                + imei + "\nIMSI:" + imsi);

    }

    public <T> long countInstanceOfClass(Class<T> c) {
        try {
            Method method = Debug.class.getMethod("countInstancesOfClass",
                    new Class[]{Class.class});
            Object ret = method.invoke(null, c);
            return (Long) ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        return mVersion;
    }

    public void initVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            mVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCurrentMenuGroup() {
        return mMenuGroup;
    }

    public String getMainMenuItemKey() {
        return mMainMenuItemKey;
    }

    public void setCurrentMenuItem(int menuGroup, String mainMenuItemKey) {
        mMenuGroup = menuGroup;
        mMainMenuItemKey = mainMenuItemKey;
        saveMenuStatus();
    }

    public void setExitCode(int code) {
        mExitCode = code;
    }

    public void saveMenuStatus() {
        String str = mStartTimeMs + "," + mMenuGroup + "," + mMainMenuItemKey + "," + mExitCode;
        writeMenuStatus(str);
    }

    /**
     * @return 上次退出代码，正常为0，不正常为1
     */
    public int readMenuStatus() {
        int exitCode = 0;
        try {
            mMenuGroup = -1;
            mMainMenuItemKey = "";
            String path = getMenuStatusFilePath();
            if (UtilFile.existFile(path)) {
                String line = UtilFile.readFile(path);
                if (null != line && !line.isEmpty()) {
                    String[] strs = line.split(",");
                    if (strs.length >= 4) {
                        long lastStartTimeMs = Long.valueOf(strs[0]);
                        long dt = mStartTimeMs - lastStartTimeMs;
                        if (Math.abs(dt) > 8000) {
                            mMenuGroup = Integer.valueOf(strs[1]);
                            mMainMenuItemKey = strs[2];
                        }
                        // if(dt<60000) {
                        exitCode = StringUtils.string2int(strs[3]);
                        // }
                    }
                }
                writeMenuStatus(mStartTimeMs + ",-1,-1," + mExitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Lg.printException(getClass().getSimpleName(), e);
        }
        return exitCode;
    }

    private void writeMenuStatus(String str) {
        try {
            String path = getMenuStatusFilePath();
            UtilFile.writeFile(path, str);
        } catch (Exception e) {
            e.printStackTrace();
            Lg.printException(getClass().getSimpleName(), e);
        }
    }

    private String getMenuStatusFilePath() {
        return UtilSDCardManager.GetFirstSDCardPath() + ConstPath.APPLOGPATH
                + C.Str.logFileName_crashStatus;
    }

    public void exit() {
        try {
            Lg.i("App lifecycle--------------exit");
            setExitCode(0);
            setCurrentMenuItem(-1, "");
            saveMenuStatus();
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Lg.i("App lifecycle--------------exit begin");
                        LandstarService ls = LandstarService.getExistInstance();
                        if (null != ls) {
                            ls.stopSelf();
                            Lg.i("App lifecycle--------------stop LandstarService");
                        }
                        //System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Lg.printException(getClass().getSimpleName(), e);
                    }
                }
            }, 100);
            CloudStatusMonitor.getInstance().stop();
        } catch (Exception e) {
            e.printStackTrace();
            Lg.printException(getClass().getSimpleName(), e);
        }
    }

}
