package com.xmtj.wtf.gather;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.xmtj.wtf.gather.contants.Urls;
import com.xmtj.wtf.gather.tinker.Log.LogImp;
import com.xmtj.wtf.gather.tinker.util.ApplicationContext;
import com.xmtj.wtf.gather.tinker.util.TinkerManager;
import com.xmtj.wtf.network.Configuration;
import com.xmtj.wtf.network.RetrofitFactory;

/**
 * Created by wanglei on 2017/10/31.
 */

@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.xmtj.wtf.gather.App",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class AppLike extends DefaultApplicationLike {
    private static final String TAG = "Tinker.SampleApplicationLike";

    public AppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                   long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        ApplicationContext.application = getApplication();
        ApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);

        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new LogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
        initRetrofit(base);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    private void initRetrofit(Context context) {
        RetrofitFactory.setBaseUrl(Urls.ROOT_URL);
        if (BuildConfig.isDebug)
            Configuration.enableLoggingNetworkParams();
    }

}