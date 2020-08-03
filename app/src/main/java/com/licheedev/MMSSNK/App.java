package com.licheedev.MMSSNK;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.licheedev.MMSSNK.util.PrefHelper;
import com.licheedev.MMSSNK.utils.AidlUtil;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class App extends Application {

    private WifiManager wifiManager;

    private Handler mUiHandler;
    private static App sInstance;

    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        mUiHandler = new Handler();
        initUtils();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);

    }

    private void initUtils() {
        PrefHelper.initDefault(this);
    }

    public static App instance() {
        return sInstance;
    }

    public static Handler getUiHandler() {
        return instance().mUiHandler;
    }

}
